package com.cn.petshome.paymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 支付网关服务启动
 */
@SpringBootApplication(scanBasePackages = {"com.cn.petshome.paymentgateway", "com.cn.petshome.petspub.httpclient"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
