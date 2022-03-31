package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.petshome.paymentgateway.common.response.JsonResponse;
import com.cn.petshome.paymentgateway.common.response.PrePlacePayOrderResponse;
import com.cn.petshome.paymentgateway.common.util.*;
import com.cn.petshome.paymentgateway.service.*;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.mapper.OrderPayMethodMapper;
import com.cn.petshome.paymentgateway.mapper.PayOrderMapper;
import com.cn.petshome.paymentgateway.mapper.PayTxnJnlMapper;
import com.cn.petshome.paymentgateway.po.OrderPayMethodDO;
import com.cn.petshome.paymentgateway.po.PayOrderDO;
import com.cn.petshome.paymentgateway.po.PayTxnJnlDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *
 * 网关提供的支付相关功能接口
 * @date 2022/1/19 13:26
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ExternalService externalService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private OrderPayMethodMapper orderPayMethodMapper;

    @Autowired
    private PayTxnJnlMapper payTxnJnlMapper;

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private UnionPayService unionPayService;

    @Autowired
    WechatPayService wechatPayService;


    /**
     *
     * 预下单功能
     * @param request 下单请求vo
     * @return {@link JsonResponse} 响应包含：支付订单号，跳转支付页面html
     * @author hjr
     * @date 2022/1/19 14:18
     */
    @Override
    @Transactional(rollbackFor = {DataAccessException.class, PaymentException.class})
    public JsonResponse<PrePlacePayOrderResponse> prePlacePayOrder(PayOrderRequest request){
        log.info("进入交易预下单方法，入参：{}", request);

        //TODO:根据订单类型request_order_type,调用相应接口,查询对应订单状态

        // 校验参数合法性（判断支付方式金额之和是否等于总金额）
        boolean isValid = PayOrderRequestUtil.requestExamine(request);
        if (!isValid) {
            throw new PaymentException("非法请求");
        }

        // 申请订单号，生成订单以及支付方式列表
        String payOrderId = NumberGeneratorUtil.getPayOrderNumbere();
        log.info("生成订单号：{}", payOrderId);
        PayOrderDO order = PayOrderRequestUtil.initAndGetPayOrder(request, payOrderId);
        List<OrderPayMethodDO> payMethods = PayOrderRequestUtil.getMethods(request);

        // 在选择调用第三方支付渠道前对于传入订单参数在本地预处理
        PayTxnJnlDO cashPayTxnJnlDO;
        cashPayTxnJnlDO = preprocessPayOrder(order, payMethods);

        // 对接支付渠道进行下单
        String returnForm;
        if (!ObjectUtils.isEmpty(cashPayTxnJnlDO)){
            returnForm = switchChannelToPay(request.getChannelType(), order, cashPayTxnJnlDO);
        }else {
            throw new PaymentException("现金流缺失");
        }

        JsonResponse<PrePlacePayOrderResponse> response = JsonResponse.buildSuccess(new PrePlacePayOrderResponse(payOrderId, returnForm));
        log.info("交易预下单方法正常返回：{}", response);
        return response;
    }


    /**
     *
     * 检查订单积分、代金券冻结情况并冲正
     * @param userId 用户ID
     * @param payOrderId 订单号
     * @author hjr
     * @date 2022/1/20 16:24
     */
    private void reverse(String userId, String payOrderId) {
        log.info("进入冲正方法，入参：{}, {}", userId, payOrderId);

        //TODO:订单冻结积分解冻
        String holdNo = orderPayMethodMapper.
                selectByPayOrderIdAndPayMethod(payOrderId, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode()).getPayVoucher();
        if (!StringUtils.isEmpty(holdNo)) {
            externalService.callPointUnFrozen(userId, holdNo);
        }
        //TODO:订单冻结代金券解冻


        log.info("完成冲正操作");
    }


    /**
     *
     * 在选择调用第三方支付渠道前对于传入订单数据在本地预处理
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlDO} 输出现金流供渠道选择使用
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlDO preprocessPayOrder(PayOrderDO order, List<OrderPayMethodDO> payMethods) {
        log.info("进入订单数据本地预处理方法，入参：{}, {}", order, payMethods);

        try {
            log.info("登记支付订单表：{}", order);
            payOrderMapper.insert(order);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("订单表插入失败", dataAccessException);
        }

        try {
            log.info("登记支付方式表：{}", payMethods);
            orderPayMethodMapper.insertBatch(payMethods);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("支付方式表插入失败", dataAccessException);
        }

        // 处理积分流
        handlePointPayTxnJnl(order, payMethods);

        // 处理代金券流
        handleCouponPayTxnJnl(order, payMethods);

        // 处理现金流
        PayTxnJnlDO cashPayTxnJnlDO = handleCashPayTxnJnl(order, payMethods);

        log.info("进入订单数据本地预处理方法正常返回：{}", cashPayTxnJnlDO);
        return cashPayTxnJnlDO;
    }


    /**
     *
     * 处理积分流
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlDO} 积分流
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlDO handlePointPayTxnJnl(PayOrderDO order, List<OrderPayMethodDO> payMethods){
        log.info("进入积分流处理方法，入参：{}, {}", order, payMethods);

        OrderPayMethodDO pointPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(payMethods, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        if (ObjectUtils.isEmpty(pointPayMethod)){
            log.info("积分流处理方法正常返回：null");
            return null;
        }

        PayTxnJnlDO pointPayTxnJnlDO = PayOrderRequestUtil.getTxnJnl(order, pointPayMethod);
        try {
            log.info("登记积分流水：{}", pointPayTxnJnlDO);
            payTxnJnlMapper.insert(pointPayTxnJnlDO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("积分流水登记失败", dataAccessException);
        }

        // 冻结积分
        String holdNo = externalService.callPointFrozen(order.getUserId(),
                pointPayTxnJnlDO.getNumber().longValue());

        pointPayTxnJnlDO.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        String status = !StringUtils.isEmpty(holdNo) ?
                StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode()
                : StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode();
        pointPayTxnJnlDO.setStatus(status);
        try {
            log.info("根据冻结结果更新支付流水表：{}", pointPayTxnJnlDO);
            payTxnJnlMapper.updateByRequestTxnJnl(pointPayTxnJnlDO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("积分流水更新失败", dataAccessException);
        }

        status = !StringUtils.isEmpty(holdNo) ?
                StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode()
                : StatusEnum.PAY_METHOD_STATUS_FAIL.getCode();
        pointPayMethod.setStatus(status);
        String voucher = !StringUtils.isEmpty(holdNo) ? holdNo : "";
        pointPayMethod.setPayVoucher(voucher);
        try {
            log.info("根据冻结结果更新支付方式表：{}", pointPayMethod);
            orderPayMethodMapper.updateByPayOrderIdAndPayMethod(pointPayMethod);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("积分方式更新失败", dataAccessException);
        }

        //冻结失败,冲正后终止交易
        if (StringUtils.isEmpty(holdNo)) {
            reverse(order.getUserId(), order.getPayOrderId());
            throw new PaymentException("积分方式支付失败");
        }

        log.info("积分流处理方法正常返回：{}", pointPayTxnJnlDO);
        return pointPayTxnJnlDO;
    }


    /**
     *
     * 处理代金券流
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlDO} 代金券流
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlDO handleCouponPayTxnJnl(PayOrderDO order, List<OrderPayMethodDO> payMethods) {
        log.info("进入代金券流处理方法，入参：{}, {}", order, payMethods);

        OrderPayMethodDO couponPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(payMethods, PaymentKeyEnum.PAY_METHOD_COUPON.getKeyCode());
        if (ObjectUtils.isEmpty(couponPayMethod)){
            log.info("积分流处理方法正常返回：null");
            return null;
        }

        PayTxnJnlDO couponPayTxnJnlDO = PayOrderRequestUtil.getTxnJnl(order, couponPayMethod);
        try {
            log.info("登记代金券流水：{}", couponPayTxnJnlDO);
            payTxnJnlMapper.insert(couponPayTxnJnlDO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("代金券流水登记失败", dataAccessException);
        }

        // 冻结代金券
        String holdNo = externalService.callPointFrozen(order.getUserId(), couponPayTxnJnlDO.getNumber().longValue());

        couponPayTxnJnlDO.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        String status = !StringUtils.isEmpty(holdNo) ?
                StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode()
                : StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode();
        couponPayTxnJnlDO.setStatus(status);
        try {
            log.info("根据冻结结果更新支付流水表：{}", couponPayTxnJnlDO);
            payTxnJnlMapper.updateByRequestTxnJnl(couponPayTxnJnlDO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("代金券流水更新失败", dataAccessException);
        }

        status = !StringUtils.isEmpty(holdNo) ?
                StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode()
                : StatusEnum.PAY_METHOD_STATUS_FAIL.getCode();
        couponPayMethod.setStatus(status);
        String voucher = !StringUtils.isEmpty(holdNo) ? holdNo : "";
        couponPayMethod.setPayVoucher(voucher);
        try {
            log.info("根据冻结结果更新支付方式表：{}", couponPayMethod);
            orderPayMethodMapper.updateByPayOrderIdAndPayMethod(couponPayMethod);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("代金券方式更新失败", dataAccessException);
        }

        //冻结失败,冲正后终止交易
        if (StringUtils.isEmpty(holdNo)) {
            reverse(order.getUserId(), order.getPayOrderId());
            throw new PaymentException("代金券方式支付失败");
        }

        log.info("代金券流处理方法正常返回：{}", couponPayTxnJnlDO);
        return couponPayTxnJnlDO;
    }


    /**
     *
     * 处理现金流
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlDO} 现金流
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlDO handleCashPayTxnJnl(PayOrderDO order, List<OrderPayMethodDO> payMethods) {
        log.info("进入现金流处理方法，入参：{}, {}", order, payMethods);

        OrderPayMethodDO cashPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(payMethods, PaymentKeyEnum.PAY_METHOD_CASH.getKeyCode());
        PayTxnJnlDO cashPayTxnJnlDO;
        if (ObjectUtils.isEmpty(cashPayMethod)){
            reverse(order.getUserId(), order.getPayOrderId());
            throw new PaymentException("现金流缺失");
        }else {
            cashPayTxnJnlDO = PayOrderRequestUtil.getTxnJnl(order, cashPayMethod);
            try {
                log.info("登记现金流水：{}", cashPayTxnJnlDO);
                payTxnJnlMapper.insert(cashPayTxnJnlDO);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("现金流水登记失败", dataAccessException);
            }
        }

        log.info("现金流处理方法正常返回：{}", cashPayTxnJnlDO);
        return cashPayTxnJnlDO;
    }

    /**
     *
     * 选择支付渠道下单，更新现金流水
     * @param channelType 支付渠道
     * @param order 订单po
     * @param cashPayTxnJnlDO 现金流水po
     * @return 支付网页表单
     * @author hjr
     * @date 2022/1/24 17:34
     */
    private String switchChannelToPay(String channelType, PayOrderDO order, PayTxnJnlDO cashPayTxnJnlDO) {
        log.info("进入选择支付渠道并下单方法，入参：{}, {}, {}", channelType, order, cashPayTxnJnlDO);

        String returnForm = "";
        PaymentChannelEnum channelKey = PaymentChannelEnum.getPaymentChannelByCode(channelType);
        if (ObjectUtils.isEmpty(channelKey)){
            throw new PaymentException("支付渠道不存在");
        }
        switch (channelKey) {
            case CHANNEL_TYPE_WEIXINPAY: {
                log.info("选择微信渠道");
                returnForm = wechatPayService.goPay(order);
                break;
            }
            //支付宝
            case CHANNEL_TYPE_ALIPAY: {
                log.info("选择支付宝渠道");
                returnForm = aliPayService.goPay(order);
                break;
            }
            //银联
            case CHANNEL_TYPE_UNIONPAY: {
                log.info("选择银联渠道");
                returnForm = unionPayService.goPay(order);
                break;
            }
            default: throw new PaymentException("渠道选择异常");
        }
        String status = !StringUtils.isEmpty(returnForm) ?
                StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode()
                : StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode();
        cashPayTxnJnlDO.setStatus(status);
        cashPayTxnJnlDO.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        try {
            log.info("根据渠道请求结果更新流水表：{}", cashPayTxnJnlDO);
            payTxnJnlMapper.updateByRequestTxnJnl(cashPayTxnJnlDO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("现金流水更新失败", dataAccessException);
        }
        if (StringUtils.isEmpty(returnForm)) {
            reverse(order.getUserId(), order.getPayOrderId());
        }

        log.info("选择支付渠道并下单完成，返回支付表单：{}", returnForm);
        return returnForm;
    }

}
