package com.cn.petshome.paymentgateway.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * 获取对接支付宝配置信息
 * @date 2022/1/17 11:25
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
@PropertySource("classpath:alipay.properties")
public class AliPayResource {
    private String appId;
    private String merchantPrivateKey;
    private String alipayPublicKey;

    private String notifyUrl;
    private String returnUrl;

    private String signType;
    private String charset;
    private String gatewayUrl;

    private String productCode;
}
