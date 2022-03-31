package com.cn.petshome.paymentgateway.service;

/**
 *
 * RocketMQ生产者服务
 * @date 2022/2/24 16:25
 */
public interface RocketmqProducerService {

    /**
     *
     * 调用生产者发送非延时MQ消息
     * @param topic topic
     * @param tags tags
     * @param msg 消息内容
     * @author hjr
     * @date 2022/2/24 11:21
     */
    public void sendMessage(String topic, String tags, String msg);

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
    public void sendMessage(String topic, String tags, String msg, Integer delayLevel);
}
