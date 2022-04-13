package com.cn.petshome.paymentgateway.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *
 * web mvc配置
 * @date 2022/4/9 16:29
 */
@Configuration
public class GatewayWebmvcConfiguration implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        FixedLengthHttpMessageConverter flhc = new FixedLengthHttpMessageConverter();
        converters.add(flhc);
    }
}
