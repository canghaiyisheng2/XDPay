package com.cn.petshome.paymentgateway.service;

import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.response.NotifyInfo;
import com.cn.petshome.paymentgateway.po.PayOrderDO;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 对接银联相关支付接口(手机网页支付)
 * @date 2022/3/30 10:42
 */
public interface WechatPayService {
    /**
     *
     * 跳转至微信支付网关
     * @param order 支付订单po
     * @return 支付网页表单
     * @author hjr
     * @date 2022/1/17 11:17
     * @throws PaymentException
     */
    public String goPay(PayOrderDO order) throws PaymentException;

    /**
     *
     * 处理微信的异步通知
     * @param request 来自微信的异步通知
     * @return {@link NotifyInfo}
     * @author hjr
     * @date 2022/1/17 14:33
     * @throws NotifyException
     */
    public NotifyInfo notifyHandle(HttpServletRequest request) throws NotifyException;
}