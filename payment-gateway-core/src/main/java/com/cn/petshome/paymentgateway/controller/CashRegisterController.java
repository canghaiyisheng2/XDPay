package com.cn.petshome.paymentgateway.controller;

import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.petshome.paymentgateway.common.response.JsonResponse;
import com.cn.petshome.paymentgateway.common.response.PrePlacePayOrderResponse;
import com.cn.petshome.paymentgateway.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 支付网关收银台控制器，接收支付下单请求
 * @date 2022/1/18 13:51
 */
@RestController
@RequestMapping(value = "/cashRegister", consumes = "application/json", produces = "application/json")
public class CashRegisterController {

    @Autowired
    private PaymentService paymentService;

    /**
     *
     * 预下单接口
     * @param request PayOrderRequest请求
     * @return 接口响应
     * @author hjr
     * @date 2022/3/10 10:07
     */
    @RequestMapping(value = "/goPay", method = RequestMethod.POST)
    public JsonResponse<PrePlacePayOrderResponse> goPay(@RequestBody @Validated PayOrderRequest request){
        return paymentService.prePlacePayOrder(request);
    }

}
