package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.service.RocketmqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 *
 * MQ生产者服务
 * @date 2022/2/23 20:07
 */
@Slf4j
@Service
public class RocketmqProducerServiceImpl implements RocketmqProducerService {

    @Autowired
    private DefaultMQProducer producer;

    /**
     *
     * 调用生产者发送非延时MQ消息
     * @param topic topic
     * @param tags tags
     * @param msg 消息内容
     * @author hjr
     * @date 2022/2/24 11:21
     */
    @Override
    public void sendMessage(String topic, String tags, String msg){
        sendMessage(topic, tags, msg, 0);
    }

    /**
     *
     * 调用生产者发送延时MQ消息
     * @param topic topic
     * @param tags tags
     * @param msg 消息内容
     * @param delayLevel 消息延时级别
     * @author hjr
     * @date 2022/2/24 11:21
     */
    @Override
    public void sendMessage(String topic, String tags, String msg, Integer delayLevel){
        try {
            log.info("发送MQ消息：{}", msg);
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            message.setDelayTimeLevel(delayLevel);
            SendResult sendResult = producer.send(message);
            log.info("MQ消息发送响应：{}", sendResult);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new NotifyException("不支持的编码格式", unsupportedEncodingException);
        } catch (RemotingException | MQClientException | MQBrokerException | InterruptedException sendingException){
            throw new NotifyException("生产者发送消息异常", sendingException);
        }
    }
}
