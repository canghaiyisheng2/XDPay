package com.cn.petshome.paymentgateway;

import com.cn.petshome.paymentgateway.service.impl.WechatPayServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @date 2022/2/25 16:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpClientTest {

//    @Autowired
//    HttpClientService httpClientService;
    @Autowired
WechatPayServiceImpl weixinPayService;

//    @Test
//    public void testSendPost(){
//        Map<String, String> param = new HashMap<>();
//        param.put("param1", "123456");
//        param.put("param2", "789101");
//        httpClientService.doPost("http://localhost:8080/api", param, "UTF-8");
//    }

    @Test
    public void testWeixin(){
        weixinPayService.goPay(null);
    }
}
