package com.cn.petshome.paymentgateway.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * 配置、初始化消费者
 * @date 2022/2/24 16:33
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq.consumer")
@PropertySource("classpath:rocketmq.properties")
@Slf4j
public class MqConsumerConfig {

    private String groupName;
    private String nameSrvAddr;
    private String topics;
    /**
     *
     * 消费者线程数据量
     */
    private Integer consumeThreadMin;
    private Integer consumeThreadMax;
    private Integer consumeMessageBatchMaxSize;

    @Autowired
    @Qualifier(value = "dispatcher")
    MessageListenerConcurrently messageListenerConcurrently;

    /**
     *
     * 获取初始化的消费者
     * @return 初始化的消费者
     * @author hjr
     * @date 2022/2/27 12:41
     */
    @Bean(value = "consumer")
    public DefaultMQPushConsumer getDefaultConsumer(){

        log.info("正在创建MQ消费者……");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        consumer.setMessageListener(messageListenerConcurrently);

        try {
            //订阅topics
            String[] topicArr = topics.split(";");
            for (String topic : topicArr){
                String[] tagArr = topic.split("~");
                consumer.subscribe(tagArr[0], tagArr[1]);
            }
            consumer.start();
            log.info("MQ消费者启动成功 groupName={}, topics={}, nameSrvAddr={}",
                    groupName, topics, nameSrvAddr);
        }catch (MQClientException mqClientException){
            log.error("MQ消费者启动失败", mqClientException);
        }
        return consumer;
    }
}
