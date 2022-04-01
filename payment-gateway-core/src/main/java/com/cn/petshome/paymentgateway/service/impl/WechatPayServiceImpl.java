package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.common.config.WechatPayResource;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.response.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.RequestUtil;
import com.cn.petshome.paymentgateway.po.PayOrderDO;
import com.cn.petshome.paymentgateway.service.WechatPayService;
import com.github.wxpay.sdk.WXPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String goPay(PayOrderDO order) throws PaymentException {


        WXPay wxpay = null;
        try {
            wxpay = new WXPay(wechatPayResource, true, true);
//            wxpay = new WXPay(weixinPayResource);
        } catch (Exception e) {
            throw new PaymentException("初始化微信支付异常");
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", order.getSubject());
        data.put("out_trade_no", order.getPayOrderId());
        data.put("fee_type", "CNY");
        data.put("total_fee", order.getActualPayAmt().toString());
        data.put("spbill_create_ip", "124.90.39.130");
        data.put("notify_url", wechatPayResource.getNotifyUrl());
        data.put("trade_type", "NATIVE");

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("微信支付返回：{}", resp);
            return "<img id=\"qrious\">\n" +
                    "<script src=\"https://cdn.bootcss.com/qrious/4.0.2/qrious.js\"></script>\n" +
                    "<script>\n" +
                    " var qr = new QRious({\n" +
                    "   element:document.getElementById('qrious'),\n" +
                    "   size:250,level:'H',value:'" + resp.get("code_url") + "'\n" +
                    "});\n" +
                    "</script>\n";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
        //TODO：微信异步通知
        log.info("进入微信异步通知处理方法，入参：{}", request);

        Map<String, String> params = RequestUtil.getAllRequestParams(request);
        log.info("接受到异步参数{}", params);
        return null;
    }
}
