package com.cn.petshome.paymentgateway.service.impl;

import com.alipay.api.internal.util.WebUtils;
import com.cn.petshome.paymentgateway.common.config.WechatPayResource;
import com.cn.petshome.paymentgateway.common.exception.ApiException;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.RequestUtil;
import com.cn.petshome.paymentgateway.common.util.enums.PaymentTypeEnum;
import com.cn.petshome.paymentgateway.po.PayOrderPO;
import com.cn.petshome.paymentgateway.service.WechatPayService;
import com.github.wxpay.sdk.WXPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2022/3/30 10:41
 */
@Service
@Slf4j
public class WechatPayServiceImpl implements WechatPayService {

    @Autowired
    private WechatPayResource wechatPayResource;


    /**
     *
     * 跳转至微信支付网关
     * @param order 支付订单po
     * @return 支付网页表单
     * @author hjr
     * @date 2022/1/17 11:17
     * @throws PaymentException
     */
    @Override
    public String goPay(PayOrderPO order) throws ApiException {
        log.info("进入微信下单方法，入参：{}", order);


        String returnForm = "";
        PaymentTypeEnum typeEnum = PaymentTypeEnum.getPaymentTypeByCode(order.getPaymentType());
        if (ObjectUtils.isEmpty(typeEnum)) {
            throw new ApiException("错误的支付方式参数");
        }
        String alipayForm = "";
        WebUtils.setNeedCheckServerTrusted(false);
        switch (typeEnum){
            case PAY_TYPE_PC:
            case PAY_TYPE_MOBILE:
                alipayForm = goWeb(order);break;
            case PAY_TYPE_NATIVE: alipayForm = goNative(order);break;
            default: throw new ApiException("错误的支付方式参数");
        }

        return returnForm;
    }

    /**
     * H5支付
     * @param order 支付订单
     * @return 返回表单
     * @author hjr
     * @date 2022/5/20 18:45
     */
    private String goWeb(PayOrderPO order){
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(wechatPayResource, true, true);
        } catch (Exception e) {
            throw new ApiException("初始化微信支付异常");
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", order.getSubject());
        data.put("out_trade_no", order.getPayOrderId());
        data.put("fee_type", "CNY");
        data.put("total_fee", order.getActualPayAmt().toString());
        data.put("spbill_create_ip", "124.90.39.130");
        data.put("notify_url", wechatPayResource.getNotifyUrl());
        data.put("trade_type", "MWEB");

        String returnForm = null;
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("微信支付返回：{}", resp);
            returnForm = resp.get("h5_url");
        } catch (Exception e) {
            throw new ApiException("微信API调用下单接口失败", e);
        }

        if (StringUtils.hasLength(returnForm)){
            log.info("微信下单完成，返回：{}", returnForm);
        }else {
            throw new ApiException("微信下单失败，返回空字符串");
        }

        return returnForm;
    }

    /**
     * 扫码支付
     * @param order 支付订单
     * @return 返回表单
     * @author hjr
     * @date 2022/5/20 18:45
     */
    private String goNative(PayOrderPO order){
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(wechatPayResource, true, true);
        } catch (Exception e) {
            throw new ApiException("初始化微信支付异常");
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", order.getSubject());
        data.put("out_trade_no", order.getPayOrderId());
        data.put("fee_type", "CNY");
        data.put("total_fee", order.getActualPayAmt().toString());
        data.put("spbill_create_ip", "124.90.39.130");
        data.put("notify_url", wechatPayResource.getNotifyUrl());
        data.put("trade_type", "NATIVE");

        String returnForm = null;
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("微信支付返回：{}", resp);
            returnForm = resp.get("code_url");
        } catch (Exception e) {
            throw new ApiException("微信API调用下单接口失败", e);
        }

        if (StringUtils.hasLength(returnForm)){
            log.info("微信下单完成，返回：{}", returnForm);
        }else {
            throw new ApiException("微信下单失败，返回空字符串");
        }

        return returnForm;
    }


    /**
     *
     * 处理微信的异步通知
     * @param request 来自微信的异步通知
     * @return {@link NotifyInfo}
     * @author hjr
     * @date 2022/1/17 14:33
     * @throws NotifyException
     */
    @Override
    public NotifyInfo notifyHandle(HttpServletRequest request) throws NotifyException {
        log.info("进入微信异步通知处理方法，入参：{}", request);

        Map<String, String> params = RequestUtil.getAllRequestParams(request);
        //验签、转换为同一异步消息
        log.info("接受到异步参数{}", params);
        return new NotifyInfo(params.get("orderId"), NotifyInfo.TRADE_SUCCESS);
    }
}
