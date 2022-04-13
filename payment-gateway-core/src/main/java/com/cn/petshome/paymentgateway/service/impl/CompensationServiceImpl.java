package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.bo.DelayNotifyElement;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.util.CompensationUtil;
import com.cn.petshome.paymentgateway.service.CompensationService;
import com.cn.petshome.petspub.httpclient.exception.HttpClientException;
import com.cn.petshome.petspub.httpclient.service.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;

/**
 *
 * 补偿机制服务接口
 * @date 2022/4/5 13:50
 */
@Slf4j
@Component
public class CompensationServiceImpl implements CommandLineRunner, CompensationService {

    @Autowired
    @Qualifier(value = "getExecutor")
    Executor compensationExecutor;

    @Autowired
    HttpClientService httpClientService;

    private final DelayQueue<DelayNotifyElement<NotifyInfo>> notifyElementDelayQueue
            = new DelayQueue<>();

    /**
     *
     * 加入延时任务
     * @param element 延时任务
     * @author hjr
     * @date 2022/4/5 13:59
     */
    @Override
    public void put(DelayNotifyElement<NotifyInfo> element){
        log.info("加入延时任务：{}", element);
        notifyElementDelayQueue.offer(element);
    }

    /**
     *
     * 取消延时任务
     * @param element 延时任务
     * @author hjr
     * @date 2022/4/5 13:59
     */
    @Override
    public void remove(DelayNotifyElement<NotifyInfo> element){
        log.info("取消延时任务：{}", element);
        notifyElementDelayQueue.remove(element);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("初始化延时队列……");
        compensationExecutor.execute(new CompensationThread());
    }

    private class CompensationThread implements Runnable{

        @Autowired
        private CompensationService compensationService;

        public CompensationThread(){
            log.info("异步补偿机制实现线程启动成功");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DelayNotifyElement<NotifyInfo> notifyElement =
                            notifyElementDelayQueue.take();
                    NotifyInfo notifyInfo = notifyElement.getData();
                    int delayLevel = notifyElement.getDelayLevel();

                    httpClientService.doPost(
                            notifyInfo.getNotifyUrl(),
                            notifyInfo.toMap(),
                            CompensationUtil.getFutureCallback(notifyInfo, delayLevel + 1),
                            "UTF-8");
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
