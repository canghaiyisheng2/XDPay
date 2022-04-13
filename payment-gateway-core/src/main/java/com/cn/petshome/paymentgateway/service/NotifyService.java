package com.cn.petshome.paymentgateway.service;

import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 处理第三方支付渠道的异步请求
 * @date 2022/2/21 10:47
 */
public interface NotifyService {
    /**
     *
     * 接收处理来自第三方支付渠道的异步请求
     * @param notifyRequest 来自第三方支付渠道的异步请求
     * @param channelType 第三方支付渠道类型
     * @return {@link ResponseBean} 包含支付订单编号
     * @author hjr
     * @date 2022/1/21 11:46
     * @throws NotifyException
     * @throws DaoException
     */
    public ResponseBean<String> receivePayResultNotify(HttpServletRequest notifyRequest, String channelType) throws NotifyException, DaoException;
}
