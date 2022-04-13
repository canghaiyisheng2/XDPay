package com.cn.petshome.paymentgateway.bo;

import com.alibaba.fastjson.JSON;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 异步通知返回vo
 * @date 2022/1/21 16:39
 */
@Data
@AllArgsConstructor
public class NotifyInfo {

    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String TRADE_FAIL = "TRADE_FAIL";

    String payOrderId;
    String notifyUrl;
    String status;

    public NotifyInfo(String payOrderId, String status){
        this.payOrderId = payOrderId;
        this.status = status;
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>(20);
        map.put("payOrderId", payOrderId);
        map.put("status", status);
        return map;
    }
}
