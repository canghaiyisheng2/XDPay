package com.cn.petshome.paymentgateway.common.util;

import com.cn.petshome.paymentgateway.bo.DelayNotifyElement;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.service.CompensationService;
import com.cn.petshome.petspub.httpclient.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 *
 * 补偿机制工具类
 * @date 2022/4/5 14:46
 */
@Slf4j
public class CompensationUtil {

    public static final long[] DELAY_LEVEL_LIST = {1000, 2000, 4000, 8000, 16000,
            32000, 64000, 128000, 256000, 512000, 1024000,
            2048000, 4096000, 8192000};

    public static FutureCallback<HttpResponse> getFutureCallback(NotifyInfo notifyInfo, int delayLevel){
        return new FutureCallback<HttpResponse>() {
            @Autowired
            CompensationService compensationService;

            @Override
            public void completed(HttpResponse result) {
                try{
                    log.info("异步请求成功返回：{}", result.getStatusLine());
                    // 判断返回值状态
                    if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String resultContent = EntityUtils.toString(result.getEntity(), "UTF-8");
                        log.info("请求返回内容：{}", resultContent);
                    } else {
                        throw new HttpClientException("状态码信息：" + result.getStatusLine().getStatusCode());
                    }
                } catch (IOException ioException) {
                    throw new HttpClientException("返回结果解析失败", ioException);
                }
            }

            @Override
            public void failed(Exception ex) {
                log.info("异步请求失败", ex);
                if (delayLevel < CompensationUtil.DELAY_LEVEL_LIST.length){
                    compensationService.put(new DelayNotifyElement<NotifyInfo>(delayLevel, notifyInfo));
                }
            }

            @Override
            public void cancelled() {
                log.info("异步请求取消");
            }
        };
    }
}
