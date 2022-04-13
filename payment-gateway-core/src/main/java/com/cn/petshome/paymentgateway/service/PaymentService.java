package com.cn.petshome.paymentgateway.service;

import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.common.response.PrePlacePayOrderResponse;

/**
 *
 * 网关提供的支付相关功能接口
 * @date 2022/1/19 13:26
 */
public interface PaymentService {

    /**
     *
     * 预下单功能
     * @param request 下单请求vo
     * @return {@link ResponseBean} 响应包含：支付订单号，跳转支付页面html
     * @author hjr
     * @date 2022/1/19 14:18
     * @throws PaymentException
     * @throws DaoException
     */
    public ResponseBean<PrePlacePayOrderResponse> prePlacePayOrder(PayOrderRequest request) throws PaymentException, DaoException;
}
