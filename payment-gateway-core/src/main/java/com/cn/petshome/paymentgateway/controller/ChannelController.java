package com.cn.petshome.paymentgateway.controller;

import com.cn.petshome.paymentgateway.common.response.JsonResponse;
import com.cn.petshome.paymentgateway.common.util.PaymentChannelEnum;
import com.cn.petshome.paymentgateway.service.NotifyService;
import com.cn.petshome.paymentgateway.service.WechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 接收第三方支付渠道的同、异步通知
 * @date 2022/2/21 10:43
 */
@RestController
@RequestMapping(value = "/notify", produces = "application/json")
public class ChannelController {
    @Autowired
    NotifyService notifyService;

    //TODO:仅测试同步响应
    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public JsonResponse returnPage(HttpServletRequest request) {
        //返回前端同步返回页面
        return JsonResponse.buildSuccess(request.getParameterMap());
    }

    /**
     *
     * 处理支付宝渠道异步通知
     * @param request 异步通知请求
     * @return 接口响应
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    public JsonResponse<String> alipayNotify(HttpServletRequest request) {
        return notifyService.receivePayResultNotify(request, PaymentChannelEnum.CHANNEL_TYPE_ALIPAY.getCode());
    }

    @Autowired
    WechatPayService wechatPayService;
    /**
     *
     * 处理微信支付渠道异步通知
     * @param request 异步通知请求
     * @return 接口响应
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/weixin", method = RequestMethod.POST)
    public JsonResponse<String> weixinNotify(HttpServletRequest request) {
        return notifyService.receivePayResultNotify(request, PaymentChannelEnum.CHANNEL_TYPE_WEIXINPAY.getCode());
//        wechatPayService.notifyHandle(request);
    }


    /**
     *
     * 处理银联支付渠道异步通知
     * @param request 异步通知请求
     * @return 接口响应
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/unionpay", method = RequestMethod.POST)
    public JsonResponse<String> unionpayNotify(HttpServletRequest request) {
        return notifyService.receivePayResultNotify(request, PaymentChannelEnum.CHANNEL_TYPE_UNIONPAY.getCode());
    }
}
