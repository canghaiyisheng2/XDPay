package com.cn.petshome.paymentgateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.config.AliPayResource;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.RequestUtil;
import com.cn.petshome.paymentgateway.po.PayOrderPO;
import com.cn.petshome.paymentgateway.service.AliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * 对接支付宝相关支付接口(手机网页支付)
 * @date 2022/1/17 11:13
 */
@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {

    private static final String OUT_TRADE_NO = "out_trade_no";
    private static final String TOTAL_AMOUNT = "total_amount";
    private static final String SUBJECT = "subject";
    private static final String PRODUCT_CODE = "product_code";

    @Autowired
    private AliPayResource aliPayResource;

    /**
     *
     * 获得初始化的支付宝客户端
     * @return {@link AlipayClient} 支付宝客户端对象
     * @author hjr
     * @date 2022/1/17 11:36
     */
    private AlipayClient getAlipayClient(){
        return new DefaultAlipayClient(aliPayResource.getGatewayUrl(),
                aliPayResource.getAppId(),
                aliPayResource.getMerchantPrivateKey(),
                "json",
                aliPayResource.getCharset(),
                aliPayResource.getAlipayPublicKey(),
                aliPayResource.getSignType());
    }

    /**
     *
     * 跳转至支付宝支付网关
     * @param order 支付订单po
     * @return 支付网页表单
     * @author hjr
     * @date 2022/1/16 13:17
     */
    @Override
    public String goPay(PayOrderPO order) throws PaymentException {
        log.info("进入支付宝下单方法，入参：{}", order);

        //设置请求参数
//        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(aliPayResource.getReturnUrl());
        request.setNotifyUrl(aliPayResource.getNotifyUrl());

        JSONObject bizContent = new JSONObject();
        bizContent.put(OUT_TRADE_NO, order.getPayOrderId());
        String totalAmount = new BigDecimal(order.getActualPayAmt()).divide(new BigDecimal(100)).toString();
        bizContent.put(TOTAL_AMOUNT, totalAmount);
        bizContent.put(SUBJECT, order.getSubject());
        bizContent.put(PRODUCT_CODE, aliPayResource.getProductCode());

        request.setBizContent(bizContent.toString());

        String alipayForm = "";
        try {
            log.info("通过SDK向支付宝发起支付请求：{}", request);
            AlipayTradePagePayResponse response = getAlipayClient().pageExecute(request);
            if(response.isSuccess()){
                log.info("支付宝SDK支付请求调用成功，接收应答：{}", response);
                alipayForm = response.getBody();
            } else {
                log.error("支付宝支付请求错误：{}", response.getBody());
            }
        }catch (AlipayApiException alipayApiException){
            log.error("支付宝SDK支付请求调用失败", alipayApiException);
        }

        if (StringUtils.hasLength(alipayForm)){
            log.info("支付宝下单完成，返回：{}", alipayForm);
        }else {
            log.error("支付宝下单失败，返回空字符串");
        }
        return alipayForm;

    }

    /**
     *
     * 处理支付宝的异步通知
     * @param request 来自支付宝的异步通知
     * @return {@link NotifyInfo}
     * @author hjr
     * @date 2022/1/17 14:33
     */
    @Override
    public NotifyInfo notifyHandle(HttpServletRequest request) {
        log.info("进入支付宝异步通知处理方法，入参：{}", request);

        //获取支付宝POST过来反馈信息
        Map<String, String> params = RequestUtil.getAllRequestParams(request);

        //调用SDK验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params,
                    aliPayResource.getAlipayPublicKey(),
                    aliPayResource.getCharset(),
                    aliPayResource.getSignType());
        } catch (AlipayApiException alipayApiException) {
            throw new NotifyException("调用SDK验签异常", alipayApiException);
        }
        //验证成功
        if (signVerified) {
            // 商户订单号
            String outTradeNo = params.get("out_trade_no");
            // 交易状态
            String tradeStatus = params.get("trade_status");
            boolean isSuccess = "TRADE_SUCCESS".equals(tradeStatus)
                    || "TRADE_FINISHED".equals(tradeStatus);

            String status = isSuccess? NotifyInfo.TRADE_SUCCESS: NotifyInfo.TRADE_FAIL;
            NotifyInfo notifyInfo = new NotifyInfo(outTradeNo, status);

            log.info("支付宝异步通知处理完成，返回：{}", notifyInfo);
            return notifyInfo;
        } else {
            throw new NotifyException("异步通知验签不通过");
        }
    }
}
