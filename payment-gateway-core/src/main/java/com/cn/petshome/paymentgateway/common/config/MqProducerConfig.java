package com.cn.petshome.paymentgateway.common.config;

import com.cn.petshome.paymentgateway.service.impl.AliPayServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * 配置、初始化MQ生产者
 * @date 2022/2/24 11:02
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq.producer")
@PropertySource("classpath:rocketmq.properties")
@Slf4j
public class MqProducerConfig {

    private String groupName;
    private String nameSrvAddr;
    private boolean isVipChannelEnabled;
    /**
     * 消息最大值
     */
    private Integer maxMessageSize;
    /**
     * 消息发送超时时间
     */
    private Integer sendMsgTimeOut;
    /**
     * 失败重试次数
     */
    private Integer retryTimesWhenSendFailed;

    /**
     *
     * 初始化创建生产者
     * @return 创建的生产者
     * @date 2022/2/24 11:14
     */
    @Bean(value = "producer")
    public DefaultMQProducer getDefaultProducer(){
        log.info("正在创建MQ生产者……");
        DefaultMQProducer defaultProducer = new DefaultMQProducer(groupName);
        defaultProducer.setNamesrvAddr(nameSrvAddr);
        defaultProducer.setVipChannelEnabled(isVipChannelEnabled);
        defaultProducer.setMaxMessageSize(maxMessageSize);
        defaultProducer.setSendMsgTimeout(sendMsgTimeOut);
        defaultProducer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendFailed);
        try {
            defaultProducer.start();
        }catch (MQClientException mqClientException){
            log.error("MQ生产者启动失败", mqClientException);
        }
        log.info("MQ生产者启动成功");
        return defaultProducer;
    }
}
