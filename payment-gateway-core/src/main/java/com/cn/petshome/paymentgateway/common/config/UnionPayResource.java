package com.cn.petshome.paymentgateway.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * 获取对接银联配置信息
 * @author hjr
 * @date 2022/1/17 14:56
 */
@Data
@Component
@ConfigurationProperties(prefix="acpsdk")
@PropertySource("classpath:acp_sdk.properties")
public class UnionPayResource {
    public static String encoding_UTF8 = "UTF-8";

    private String version;
    private String frontUrl;
    private String backUrl;
    private String frontTransUrl;
    private String singleQueryUrl;
    private String merchantId;
}