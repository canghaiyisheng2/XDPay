package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.*;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.common.util.enums.PaymentChannelEnum;
import com.cn.petshome.paymentgateway.common.util.enums.PaymentKeyEnum;
import com.cn.petshome.paymentgateway.common.util.enums.StatusEnum;
import com.cn.petshome.paymentgateway.mapper.OrderPayMethodMapper;
import com.cn.petshome.paymentgateway.mapper.PayOrderMapper;
import com.cn.petshome.paymentgateway.mapper.PayTxnJnlMapper;
import com.cn.petshome.paymentgateway.po.OrderPayMethodPO;
import com.cn.petshome.paymentgateway.po.PayOrderPO;
import com.cn.petshome.paymentgateway.po.PayTxnJnlPO;
import com.cn.petshome.paymentgateway.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private WechatPayService wechatPayService;


    /**
     *
     * 接收处理来自第三方支付渠道的异步请求
     * @param notifyRequest 来自第三方支付渠道的异步请求
     * @param channelType   第三方支付渠道类型
     * @return {@link ResponseBean} 包含支付订单编号
     * @author hjr
     * @date 2022/1/21 11:46
     */
    @Override
    public ResponseBean<String> receivePayResultNotify(HttpServletRequest notifyRequest, String channelType) {
        log.info("进入接受处理异步通知的方法，入参：{}, {}", notifyRequest, channelType);

        //调用对应渠道的异步通知处理接口生成统一vo
        NotifyInfo notifyInfo = null;
        PaymentChannelEnum channelKey = PaymentChannelEnum.getPaymentChannelByCode(channelType);
        if (ObjectUtils.isEmpty(channelKey)){
            throw new NotifyException("非法方法参数");
        }
        switch (channelKey) {
            case CHANNEL_TYPE_WEIXINPAY:{
                log.info("处理来自微信的异步通知");
                notifyInfo = wechatPayService.notifyHandle(notifyRequest);
                break;
            }
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
            default: throw new NotifyException("非法请求");
        }

        // 查询订单表、支付方式信息表
        if (ObjectUtils.isEmpty(notifyInfo)){
            throw new NotifyException("异步通知处理错误");
        }
        String payOrderId = notifyInfo.getPayOrderId();
        PayOrderPO order = payOrderMapper.selectByPayOrderId(payOrderId);
        List<OrderPayMethodPO> methodList = null;
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
        log.info("发送异步通知：{}", notifyInfo);
        rocketmqProducerService.sendMessage("NotifyMessage", "notify", notifyInfo.toJson());

        ResponseBean<String> response = ResponseBean.buildSuccess("");
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
    @Transactional(rollbackFor = {DaoException.class, NotifyException.class})
    private void updateOrderAndMethodsByNotifyInfo(PayOrderPO order, List<OrderPayMethodPO> methodList, NotifyInfo notifyInfo){
        log.info("进入根据异步通知更新订单方法，入参：{}, {}, {}", order, methodList, notifyInfo);

        boolean isSuccess = NotifyInfo.TRADE_SUCCESS.equals(notifyInfo.getStatus());
        String status = isSuccess ? StatusEnum.PAY_ORDER_STATUS_PAID.getCode()
                : StatusEnum.PAY_ORDER_STATUS_PLACED.getCode();
        order.setStatus(status);
        order.setNotifyDate(new Date());
        order.setNotifyTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
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
        OrderPayMethodPO pointPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(methodList, PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode());
        callUnfrozen(order, pointPayMethod, isSuccess);

        // 代金券解冻支取
        OrderPayMethodPO couponPayMethod =
                PayOrderRequestUtil.getOrderPayMethod(methodList, PaymentKeyEnum.PAY_METHOD_COUPON.getKeyCode());
        callUnfrozen(order, couponPayMethod, isSuccess);

        log.info("根据异步通知更新订单成功");
    }

    /**
     *
     * 调用解冻程序
     * @param order 订单
     * @param method 支付方式
     * @author hjr
     * @date 2022/4/12 11:05
     */
    private void callUnfrozen(PayOrderPO order, OrderPayMethodPO method, boolean isDraw){
        log.info("进入解冻方法，入参：{}，{}，{}",order, method, isDraw);
        if (!ObjectUtils.isEmpty(method) &&
                StatusEnum.PAY_METHOD_STATUS_FROZEN.getCode().equals(method.getStatus())){
            boolean isPoint = PaymentKeyEnum.PAY_METHOD_POINT.getKeyCode().equals(method.getPayMethod());
            boolean isCoupon = PaymentKeyEnum.PAY_METHOD_COUPON.getKeyCode().equals(method.getPayMethod());
            boolean isUnfrozen = false;
            if (isPoint){
                isUnfrozen = isDraw?
                        externalService.callPointUnFrozenWithDraw(order.getUserId(), method.getPayVoucher())
                        : externalService.callPointUnFrozen(order.getUserId(), method.getPayVoucher());
            } else if (isCoupon){
                isUnfrozen = isDraw?
                        externalService.callCouponUnFrozenWithDraw(order.getUserId(), method.getPayVoucher())
                        : externalService.callCouponUnFrozen(order.getUserId(), method.getPayVoucher());
            }else {
                return;
            }

            //登记解冻流水
            PayTxnJnlPO payTxnJnl = PayOrderRequestUtil.getTxnJnl(order, method);
            payTxnJnl.setTxnType(PaymentKeyEnum.TXN_TYPE_UNFROZEN_WITH_DRAW.getKeyCode());
            payTxnJnl.setResponseTxnJnl(NumberGeneratorUtil.getTxnNumbere());
            payTxnJnl.setStatus(isUnfrozen?
                    StatusEnum.PAY_TXN_JNL_STATUS_SUCCESS.getCode() :
                    StatusEnum.PAY_TXN_JNL_STATUS_FAIL.getCode());
            try {
                payTxnJnlMapper.insertSelective(payTxnJnl);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("解冻流水登记失败", dataAccessException);
            }

            //更新支付方式表
            method.setStatus(isUnfrozen && isDraw ?
                    StatusEnum.PAY_METHOD_STATUS_PAID.getCode()
                    : StatusEnum.PAY_METHOD_STATUS_FAIL.getCode());
            try {
                orderPayMethodMapper.updateByPrimaryKeySelective(method);
            }catch (DataAccessException dataAccessException){
                throw new DaoException("支付方式更新失败", dataAccessException);
            }

            if (!isUnfrozen){
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