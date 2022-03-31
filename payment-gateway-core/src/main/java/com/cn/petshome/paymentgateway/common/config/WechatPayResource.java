package com.cn.petshome.paymentgateway.common.config;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * 获取对接微信配置信息
 * @author hjr
 * @date 2022/3/30 10:56
 */
@Configuration
@Setter
@ConfigurationProperties(prefix="weixin")
@PropertySource("classpath:weixin.properties")
public class WechatPayResource extends WXPayConfig {

    private String appID;
    private String merchID;
    private String key;
    private byte[] certData;
    private int httpConnectTimeoutMs;
    private int httpReadTimeoutMs;
    private String notifyUrl;

    @Override
    public String getAppID() {
        return this.appID;
    }

    @Override
    public String getMchID() {
        return this.merchID;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return this.httpConnectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return this.httpReadTimeoutMs;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;

    }

    public String getNotifyUrl(){
        return this.notifyUrl;
    }
}
