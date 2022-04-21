package com.cn.petshome.paymentgateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.CompensationUtil;
import com.cn.petshome.paymentgateway.service.CompensationService;
import com.cn.petshome.petspub.httpclient.service.HttpClientService;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * 消费者监听程序，异步消息处理、分发
 * @date 2022/2/24 16:57
 */
@Service(value = "dispatcher")
@Slf4j
public class NotifyMessageDispatcher implements MessageListenerConcurrently {

    @Autowired
    HttpClientService httpClientService;
    @Autowired
    CompensationService compensationService;

    /**
     *
     * 消息消费程序
     * @param msgs 消息
     * @param context 消费上下文
     * @return 消息消费状态
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        log.info("正在接收来自MQ的消息……");
        try {
            for (MessageExt messageExt : msgs){
                String notifyInfoJson = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                log.info("接受到消息主体：{}", notifyInfoJson);
                NotifyInfo notifyInfo = JSON.parseObject(notifyInfoJson, NotifyInfo.class);

                httpClientService.doPost(
                        notifyInfo.getNotifyUrl(),
                        notifyInfo.toMap(),
                        CompensationUtil.getFutureCallback(compensationService,
                                notifyInfo, 0),
                        "UTF-8");
            }
        } catch (Exception e){
            log.error("获取到的MQ消息内容异常", e);
        }

        ConsumeConcurrentlyStatus returnStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        log.info("消息消费状态：{}", returnStatus);
        return returnStatus;
    }

}
