package com.cn.petshome.paymentgateway.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 *
 * 异步线程配置
 * @date 2022/4/5 12:52
 */
@Configuration
@EnableAsync
public class AsyncThreadConfig {

    /**
     *
     * 线程池配置
     * @author hjr
     * @date 2022/4/5 12:56
     * @return null
     */
    @Bean
    public Executor getExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(10);
        //设置最大线程数
        executor.setMaxPoolSize(100);
        //线程池所使用的缓冲队列
        executor.setQueueCapacity(250);
        //设置线程名
        executor.setThreadNamePrefix("gateway-Async");
        // 初始化线程
        executor.initialize();
        return executor;
    }
}
