package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.common.response.JsonResponse;
import com.cn.petshome.paymentgateway.common.response.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.PayOrderRequestUtil;
import com.cn.petshome.paymentgateway.common.util.PaymentChannelEnum;
import com.cn.petshome.paymentgateway.common.util.StatusEnum;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.common.util.PaymentKeyEnum;
import com.cn.petshome.paymentgateway.mapper.OrderPayMethodMapper;
import com.cn.petshome.paymentgateway.mapper.PayOrderMapper;
import com.cn.petshome.paymentgateway.mapper.PayTxnJnlMapper;
import com.cn.petshome.paymentgateway.po.OrderPayMethodDO;
import com.cn.petshome.paymentgateway.po.PayOrderDO;
import com.cn.petshome.paymentgateway.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * 第三方支付渠道异步通知处理服务
 * @date 2022/2/21 10:46
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private ExternalService externalService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private OrderPayMethodMapper orderPayMethodMapper;

    @Autowired
    private PayTxnJnlMapper payTxnJnlMapper;

    @Autowired
    private RocketmqProducerService rocketmqProducerService;

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private UnionPayService unionPayService;
//    //微信支付服务
//    @Autowired
//    WechatPayService wechatPayService;


    /**
     *
     * 接收处理来自第三方支付渠道的异步请求
     * @param notifyRequest 来自第三方支付渠道的异步请求
     * @param channelType   第三方支付渠道类型
     * @return {@link JsonResponse} 包含支付订单编号
     * @author hjr
     * @date 2022/1/21 11:46
     */
    @Override
    public JsonResponse<String> receivePayResultNotify(HttpServletRequest notifyRequest, String channelType) {
        log.info("进入接受处理异步通知的方法，入参：{}, {}", notifyRequest, channelType);

        //调用对应渠道的异步通知处理接口生成统一vo
        NotifyInfo notifyInfo = null;
        PaymentChannelEnum channelKey = PaymentChannelEnum.getPaymentChannelByCode(channelType);
        if (ObjectUtils.isEmpty(channelKey)){
            throw new NotifyException("参数错误");
        }
        switch (channelKey) {
//            case PaymentKeyUtil.CHANNEL_TYPE_WEIXINPAY:
            case CHANNEL_TYPE_ALIPAY: {
                log.info("处理来自支付宝的异步通知");
                notifyInfo = aliPayService.notifyHandle(notifyRequest);
                break;
            }
            case CHANNEL_TYPE_UNIONPAY: {
                log.info("处理来自支付宝的异步通知");
                notifyInfo = unionPayService.notifyHandle(notifyRequest);
                break;
            }
        }

        // 查询订单表、支付方式信息表
        if (ObjectUtils.isEmpty(notifyInfo)){
            throw new NotifyException("异步通知处理错误");
        }
        String payOrderId = notifyInfo.getPayOrderId();
        PayOrderDO order = payOrderMapper.selectByPayOrderId(payOrderId);
        List<OrderPayMethodDO> methodList = null;
        if (ObjectUtils.isEmpty(order)) {
            throw new NotifyException("找不到订单");
        } else if (order.getOtherPayAmt() > 0) {
            methodList = orderPayMethodMapper.selectByPayOrderId(payOrderId);
            if (ObjectUtils.isEmpty(methodList)) {
                throw new NotifyException("找不到订单支付方式");
            }
        }

        notifyInfo.setNotifyUrl(order.getNotifyUrl());
        updateOrderAndMethodsByNotifyInfo(order, methodList, notifyInfo);
        //发送异步通知
        rocketmqProducerService.sendMessage("NotifyMessage", "notify", notifyInfo.toJson());

        JsonResponse<String> response = JsonResponse.buildSuccess("");
        log.info("处理异步通知完成，返回：{}", response);
        return response;
    }

    /**
     *
     * 根据异步通知更新订单
     * @param notifyInfo 统一异步通知参数vo
     * @author hjr
     * @date 2022/1/27 14:45
     */
    @Transactional(rollbackFor = {NotifyException.class})
    private void updateOrderAndMethodsByNotifyInfo(PayOrderDO order, List<OrderPayMethodDO> methodList, NotifyInfo notifyInfo){
        log.info("进入根据异步通知更新订单方法，入参：{}, {}, {}", order, methodList, notifyInfo);

        boolean isSuccess = NotifyInfo.TRADE_SUCCESS.equals(notifyInfo.getStatus());
        String status = isSuccess ? StatusEnum.PAY_ORDER_STATUS_PAID.getCode()
                : StatusEnum.PAY_ORDER_STATUS_PLACED.getCode();
        order.setStatus(status);
        try {
            log.info("更新订单表：{}", order);
            payOrderMapper.updateByPayOrderId(order);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("订单表更新失败", dataAccessException);
        }

        status = isSuccess ? StatusEnum.PAY_METHOD_STATUS_PAID.getCode()
                : StatusEnum.PAY_METHOD_STATUS_FAIL.getCode();
        try {
            log.info("更新现金流水{}状态：{}", order.getPayOrderId(), status);
            orderPayMethodMapper.updateCashStatusByPayOrderId(order.getPayOrderId(), status);
        }catch (DataAccessException dataAccessException){
            throw new DaoException("现金流水更新失败", dataAccessException);
        }

        // 积分解冻支取
        OrderPayMethodDO pointPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(methodList, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        if (!ObjectUtils.isEmpty(pointPayMethod)){
            boolean isUnfrozen = isSuccess?
                    callPointUnFrozenWithDraw(order.getUserId(), order.getPayOrderId())
                    : externalService.callPointUnFrozen(order.getUserId(), pointPayMethod.getPayVoucher());
            if (!isUnfrozen){
                throw new NotifyException("积分解冻失败");
            }
        }

        // 代金券解冻支取
        OrderPayMethodDO couponPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(methodList, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        if (!ObjectUtils.isEmpty(couponPayMethod)){
            boolean isUnfrozen = isSuccess?
                    externalService.callCouponUnFrozenWithDraw(order.getUserId())
                    : externalService.callCouponUnFrozen(order.getUserId());
            if (!isUnfrozen){
                throw new NotifyException("代金券解冻失败");
            }
        }

        log.info("根据异步通知更新订单成功");
    }

    /**
     *
     * 调用积分解冻支取
     * @param userId     用户ID
     * @param payOrderId 订单号
     * @return 解冻支取成功与否
     * @author hjr
     * @date 2022/1/21 19:10
     */
    public boolean callPointUnFrozenWithDraw(String userId, String payOrderId) {
        log.info("进入调用积分解冻支取方法，入参：{}, {}", userId, payOrderId);

        OrderPayMethodDO pointPayMethod;
        try{
            log.info("查询对应积分支付方式");
            pointPayMethod =
                    orderPayMethodMapper.selectByPayOrderIdAndPayMethod(payOrderId, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        }catch (DataAccessException dataAccessException){
            throw new DaoException("支付方式查询失败");
        }
        boolean isSueccess;
        if (!ObjectUtils.isEmpty(pointPayMethod)
                && PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode().equals(pointPayMethod.getPayMethod())) {
            isSueccess = externalService.callPointUnFrozenWithDraw(userId,
                    pointPayMethod.getPayVoucher(), pointPayMethod.getNumber().longValue());
            String status = isSueccess ? StatusEnum.PAY_METHOD_STATUS_PAID.getCode()
                    : StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode();
            pointPayMethod.setStatus(status);
            try {
                log.info("更新支付方式表：{}", pointPayMethod);
                orderPayMethodMapper.updateByPayOrderIdAndPayMethod(pointPayMethod);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("更新支付方式表失败");
            }
        }else {
            isSueccess = true;
        }

        log.info("调用解冻支取方法成功，返回：{}", isSueccess);
        return isSueccess;
    }
}
