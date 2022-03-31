package com.cn.petshome.paymentgateway;

import com.cn.petshome.paymentgateway.common.util.RequestUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hjr
 * @date 2022/1/22 10:10
 * @description
 */
public class RequestTest {

    @Test
    public void requestTest(){
        Map<String, String> mapData = new HashMap<>();
        mapData.put("payOrderId", "value");
        mapData.put("tradeStatus", "TRADE_FAIL");
//        RequestUtil.sendNotify("http://localhost:8081/api/notify", mapData);
    }

}
