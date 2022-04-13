package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.exception.ExternalException;
import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.common.response.PrePlacePayOrderResponse;
import com.cn.petshome.paymentgateway.common.util.*;
import com.cn.petshome.paymentgateway.service.*;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.mapper.OrderPayMethodMapper;
import com.cn.petshome.paymentgateway.mapper.PayOrderMapper;
import com.cn.petshome.paymentgateway.mapper.PayTxnJnlMapper;
import com.cn.petshome.paymentgateway.po.OrderPayMethodPO;
import com.cn.petshome.paymentgateway.po.PayOrderPO;
import com.cn.petshome.paymentgateway.po.PayTxnJnlPO;
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
    private WechatPayService wechatPayService;

    @Autowired
    private RocketmqProducerService rocketmqProducerService;


    /**
     *
     * 预下单功能
     * @param request 下单请求vo
     * @return {@link ResponseBean} 响应包含：支付订单号，跳转支付页面html
     * @author hjr
     * @date 2022/1/19 14:18
     */
    @Override
    @Transactional(rollbackFor = {DataAccessException.class, ExternalException.class})
    public ResponseBean<PrePlacePayOrderResponse> prePlacePayOrder(PayOrderRequest request){
        log.info("进入交易预下单方法，入参：{}", request);

        //根据request_order_id,查询对应订单状态
        PayOrderPO order = null;
        try {
            order = payOrderMapper.selectByRequestOrderId(request.getRequestOrderId());
        }catch (DataAccessException dataAccessException){
            throw new DaoException("查询订单表失败", dataAccessException);
        }
        if (!ObjectUtils.isEmpty(order) &&
                StatusEnum.PAY_ORDER_STATUS_PAID.getCode().equals(order.getStatus())){
            throw new PaymentException("订单已完成，请重新核查请求表单");
        }

        // 校验参数合法性（判断支付方式金额之和是否等于总金额、支付渠道合法性）
        PaymentChannelEnum channelKey = PaymentChannelEnum.getPaymentChannelByCode(request.getChannelType());
        boolean isValid = PayOrderRequestUtil.requestExamine(request)
                && !ObjectUtils.isEmpty(channelKey);
        if (!isValid) {
            throw new PaymentException("非法请求");
        }

        // 若非重复订单则申请订单号、生成订单,生成支付方式列表
        if (ObjectUtils.isEmpty(order)){
            String payOrderId = NumberGeneratorUtil.getPayOrderNumbere();
            log.info("生成订单号：{}", payOrderId);
            order = PayOrderRequestUtil.initAndGetPayOrder(request, payOrderId);
        }else {
            log.info("订单{}已存在，重新对其进行下单", order);
            PayOrderRequestUtil.initAndGetPayOrder(request, order.getPayOrderId());
        }
        List<OrderPayMethodPO> payMethods = PayOrderRequestUtil.getMethods(request);


        // 在选择调用第三方支付渠道前对于传入订单参数在本地预处理
        PayTxnJnlPO cashPayTxnJnlPO;
        cashPayTxnJnlPO = preprocessPayOrder(order, payMethods);

        // 对接支付渠道进行下单
        String returnForm;
        returnForm = switchChannelToPay(channelKey, order, cashPayTxnJnlPO);

        ResponseBean<PrePlacePayOrderResponse> response = ResponseBean.buildSuccess(new PrePlacePayOrderResponse(order.getPayOrderId(), returnForm));
        log.info("交易预下单方法正常返回：{}", response);
        return response;
    }


    /**
     *
     * 检查订单积分、代金券冻结情况并冲正
     * @param order 订单
     * @author hjr
     * @date 2022/1/20 16:24
     */
    private void reverse(PayOrderPO order) {
        log.info("进入冲正方法，入参：{}", order);

        //订单冻结积分解冻
        OrderPayMethodPO pointMethod = orderPayMethodMapper.
                selectByPayOrderIdAndPayMethod(order.getPayOrderId(),
                        PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        callUnfrozen(order, pointMethod);

        //订单冻结代金券解冻
        OrderPayMethodPO couponMethod = orderPayMethodMapper.
                selectByPayOrderIdAndPayMethod(order.getPayOrderId(),
                        PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        callUnfrozen(order, couponMethod);

        log.info("完成冲正操作");
    }


    /**
     *
     * 在选择调用第三方支付渠道前对于传入订单数据在本地预处理
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlPO} 输出现金流供渠道选择使用
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlPO preprocessPayOrder(PayOrderPO order, List<OrderPayMethodPO> payMethods) {
        log.info("进入订单数据本地预处理方法，入参：{}, {}", order, payMethods);

        //若订单id为空说明为非重复订单
        if (ObjectUtils.isEmpty(order.getId())) {
            try {
                log.info("登记支付订单表：{}", order);
                payOrderMapper.insert(order);
            } catch (DataAccessException dataAccessException) {
                throw new DaoException("订单表插入失败", dataAccessException);
            }

            try {
                log.info("登记支付方式表：{}", payMethods);
                orderPayMethodMapper.insertBatch(payMethods);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("支付方式表插入失败", dataAccessException);
            }
        }

        // 处理积分流
        handlePointPayTxnJnl(order, payMethods);

        // 处理代金券流
        handleCouponPayTxnJnl(order, payMethods);

        // 处理现金流
        PayTxnJnlPO cashPayTxnJnlPO = handleCashPayTxnJnl(order, payMethods);

        log.info("进入订单数据本地预处理方法正常返回：{}", cashPayTxnJnlPO);
        return cashPayTxnJnlPO;
    }


    /**
     *
     * 处理积分流
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlPO} 积分流
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlPO handlePointPayTxnJnl(PayOrderPO order, List<OrderPayMethodPO> payMethods){
        log.info("进入积分流处理方法，入参：{}, {}", order, payMethods);

        OrderPayMethodPO pointPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(payMethods, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        if (ObjectUtils.isEmpty(pointPayMethod)){
            log.info("积分流处理方法正常返回：null");
            return null;
        }

        PayTxnJnlPO pointPayTxnJnlPO = PayOrderRequestUtil.getTxnJnl(order, pointPayMethod);
        try {
            log.info("登记积分流水：{}", pointPayTxnJnlPO);
            payTxnJnlMapper.insert(pointPayTxnJnlPO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("积分流水登记失败", dataAccessException);
        }

        // 冻结积分
        String holdNo = externalService.callPointFrozen(order.getUserId(),
                pointPayTxnJnlPO.getNumber().longValue());

        pointPayTxnJnlPO.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        String status = StringUtils.hasLength(holdNo) ?
                StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode()
                : StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode();
        pointPayTxnJnlPO.setStatus(status);
        try {
            log.info("根据冻结结果更新支付流水表：{}", pointPayTxnJnlPO);
            payTxnJnlMapper.updateByRequestTxnJnl(pointPayTxnJnlPO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("积分流水更新失败", dataAccessException);
        }

        status = StringUtils.hasLength(holdNo) ?
                StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode()
                : StatusEnum.PAY_METHOD_STATUS_FAIL.getCode();
        pointPayMethod.setStatus(status);
        String voucher = StringUtils.hasLength(holdNo) ? holdNo : "";
        pointPayMethod.setPayVoucher(voucher);
        try {
            log.info("根据冻结结果更新支付方式表：{}", pointPayMethod);
            orderPayMethodMapper.updateByPayOrderIdAndPayMethod(pointPayMethod);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("积分方式更新失败", dataAccessException);
        }

        //冻结失败,冲正后终止交易
        if (!StringUtils.hasLength(holdNo)) {
            reverse(order);
            throw new PaymentException("积分方式支付失败");
        }

        log.info("积分流处理方法正常返回：{}", pointPayTxnJnlPO);
        return pointPayTxnJnlPO;
    }


    /**
     *
     * 处理代金券流
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlPO} 代金券流
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlPO handleCouponPayTxnJnl(PayOrderPO order, List<OrderPayMethodPO> payMethods) {
        log.info("进入代金券流处理方法，入参：{}, {}", order, payMethods);

        OrderPayMethodPO couponPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(payMethods, PaymentKeyEnum.PAY_METHOD_COUPON.getKeyCode());
        if (ObjectUtils.isEmpty(couponPayMethod)){
            log.info("积分流处理方法正常返回：null");
            return null;
        }

        PayTxnJnlPO couponPayTxnJnlPO = PayOrderRequestUtil.getTxnJnl(order, couponPayMethod);
        try {
            log.info("登记代金券流水：{}", couponPayTxnJnlPO);
            payTxnJnlMapper.insert(couponPayTxnJnlPO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("代金券流水登记失败", dataAccessException);
        }

        // 冻结代金券
        String holdNo = externalService.callPointFrozen(order.getUserId(), couponPayTxnJnlPO.getNumber().longValue());

        couponPayTxnJnlPO.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        String status = StringUtils.hasLength(holdNo) ?
                StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode()
                : StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode();
        couponPayTxnJnlPO.setStatus(status);
        try {
            log.info("根据冻结结果更新支付流水表：{}", couponPayTxnJnlPO);
            payTxnJnlMapper.updateByRequestTxnJnl(couponPayTxnJnlPO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("代金券流水更新失败", dataAccessException);
        }

        status = StringUtils.hasLength(holdNo) ?
                StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode()
                : StatusEnum.PAY_METHOD_STATUS_FAIL.getCode();
        couponPayMethod.setStatus(status);
        String voucher = StringUtils.hasLength(holdNo) ? holdNo : "";
        couponPayMethod.setPayVoucher(voucher);
        try {
            log.info("根据冻结结果更新支付方式表：{}", couponPayMethod);
            orderPayMethodMapper.updateByPayOrderIdAndPayMethod(couponPayMethod);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("代金券方式更新失败", dataAccessException);
        }

        //冻结失败,冲正后终止交易
        if (!StringUtils.hasLength(holdNo)) {
            reverse(order);
            throw new PaymentException("代金券方式支付失败");
        }

        log.info("代金券流处理方法正常返回：{}", couponPayTxnJnlPO);
        return couponPayTxnJnlPO;
    }


    /**
     *
     * 处理现金流
     * @param order 传入订单vo
     * @param payMethods 传入订单支付方式vo列表
     * @return {@link PayTxnJnlPO} 现金流
     * @author hjr
     * @date 2022/1/24 17:26
     */
    private PayTxnJnlPO handleCashPayTxnJnl(PayOrderPO order, List<OrderPayMethodPO> payMethods) {
        log.info("进入现金流处理方法，入参：{}, {}", order, payMethods);

        OrderPayMethodPO cashPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(payMethods, PaymentKeyEnum.PAY_METHOD_CASH.getKeyCode());
        PayTxnJnlPO cashPayTxnJnlPO;
        if (ObjectUtils.isEmpty(cashPayMethod)){
            reverse(order);
            throw new PaymentException("现金流缺失，请重新核对订单");
        }else {
            cashPayTxnJnlPO = PayOrderRequestUtil.getTxnJnl(order, cashPayMethod);
            try {
                log.info("登记现金流水：{}", cashPayTxnJnlPO);
                payTxnJnlMapper.insert(cashPayTxnJnlPO);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("现金流水登记失败", dataAccessException);
            }
        }

        log.info("现金流处理方法正常返回：{}", cashPayTxnJnlPO);
        return cashPayTxnJnlPO;
    }

    /**
     *
     * 选择支付渠道下单，更新现金流水
     * @param channelKey 支付渠道
     * @param order 订单po
     * @param cashPayTxnJnlPO 现金流水po
     * @return 支付网页表单
     * @author hjr
     * @date 2022/1/24 17:34
     */
    private String switchChannelToPay(PaymentChannelEnum channelKey, PayOrderPO order, PayTxnJnlPO cashPayTxnJnlPO) {
        log.info("进入选择支付渠道并下单方法，入参：{}, {}, {}", channelKey, order, cashPayTxnJnlPO);

        String returnForm = "";

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
            default: break;
        }
        String status = StringUtils.hasLength(returnForm) ?
                StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode()
                : StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode();
        cashPayTxnJnlPO.setStatus(status);
        cashPayTxnJnlPO.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        try {
            log.info("根据渠道请求结果更新流水表：{}", cashPayTxnJnlPO);
            payTxnJnlMapper.updateByRequestTxnJnl(cashPayTxnJnlPO);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("现金流水更新失败", dataAccessException);
        }
        if (!StringUtils.hasLength(returnForm)) {
            reverse(order);
            //发送异步通知
            NotifyInfo notifyInfo = new NotifyInfo(order.getPayOrderId(),
                    order.getNotifyUrl(), NotifyInfo.TRADE_FAIL);
            rocketmqProducerService.sendMessage("NotifyMessage", "notify", notifyInfo.toJson());
        }

        log.info("选择支付渠道并下单完成，返回支付表单：{}", returnForm);
        return returnForm;
    }

    /**
     *
     * 调用解冻程序
     * @param order 订单
     * @param method 支付方式
     * @author hjr
     * @date 2022/4/12 11:05
     */
    public void callUnfrozen(PayOrderPO order, OrderPayMethodPO method){
        boolean isPoint = PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode().equals(method.getPayMethod());
        boolean isCoupon = PaymentKeyEnum.PAY_METHOD_COUPON.getKeyCode().equals(method.getPayMethod());
        if (ObjectUtils.isEmpty(method) &&
                StringUtils.hasLength(method.getPayVoucher()) &&
                StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode().equals(method.getStatus())) {
            boolean isUnFrozen = false;
            if (isPoint){
                isUnFrozen = externalService.callPointUnFrozen(order.getUserId(),
                        method.getPayVoucher());
            }else if (isCoupon){
                isUnFrozen = externalService.callCouponUnFrozen(order.getUserId(),
                        method.getPayVoucher());
            }else {
                return;
            }

            //登记解冻流水
            PayTxnJnlPO payTxnJnl = PayOrderRequestUtil.getTxnJnl(order, method);
            payTxnJnl.setTxnType(PaymentKeyEnum.TXN_TYPE_UNFROZEN.getKeyCode());
            payTxnJnl.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
            payTxnJnl.setStatus(isUnFrozen?
                    StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode() :
                    StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode());
            try {
                payTxnJnlMapper.insertSelective(payTxnJnl);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("解冻流水登记失败", dataAccessException);
            }

            //更新支付方式表
            method.setStatus(isUnFrozen ?
                    StatusEnum.PAY_METHOD_STATUS_FAIL.getCode()
                    : StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode());
            try {
                orderPayMethodMapper.updateByPrimaryKeySelective(method);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("支付方式更新失败", dataAccessException);
            }

            if (!isUnFrozen){
                String payMethod = "";
                if (isPoint){
                    payMethod = "积分";
                }else {
                    payMethod = "代金券";
                }
                log.error("{}解冻失败，详见{}模块处理", payMethod, payMethod);
            }
        }
    }

}
