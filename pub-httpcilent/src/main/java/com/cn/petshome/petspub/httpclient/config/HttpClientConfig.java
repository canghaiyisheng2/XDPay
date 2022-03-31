package com.cn.petshome.petspub.httpclient.config;

import com.cn.petshome.petspub.httpclient.exception.HttpClientException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * 配置HttpClient
 * @date 2022/2/24 20:27
 */
@Data
@Configuration
@PropertySource("classpath:httpClient.properties")
@Slf4j
public class HttpClientConfig {
    /**
     * 最大连接数
     */
    @Value(value = "${client.maxTotal}")
    private Integer maxTotal;
    /**
     * 最大并发链接数
     */
    @Value(value = "${client.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;
    /**
     * 创建链接的最大时间
     */
    @Value(value = "${http.connectTimeout}")
    private Integer connectTimeout;
    /**
     * 链接获取超时时间
     */
    @Value(value = "${http.connectRequestTimeout}")
    private Integer connectRequestTimeout;
    /**
     * 数据传输最长时间
     */
    @Value(value = "${http.socketTimeout}")
    private Integer socketTimeout;

    /**
     *
     * 获取初始化配置的HttpClient
     * @param requestConfig 请求配置
     * @return 初始化配置的HttpClient
     * @author hjr
     * @date 2022/2/27 12:44
     */
    @Bean(value = "defaultHttpAsyncClient")
    public CloseableHttpAsyncClient getCloseableHttpAsyncClient(@Qualifier(value = "getRequestConfig") RequestConfig requestConfig){
        try {
            //配置io线程
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                    setIoThreadCount(Runtime.getRuntime().availableProcessors())
                    .setSoKeepAlive(true)
                    .build();

            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

            PoolingNHttpClientConnectionManager manager = new PoolingNHttpClientConnectionManager(ioReactor);
            manager.setMaxTotal(maxTotal);
            manager.setDefaultMaxPerRoute(defaultMaxPerRoute);

            CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                    .setConnectionManager(manager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            return httpClient;
        } catch (IOReactorException ioReactorException) {
            throw new HttpClientException("HttpClient初始化失败", ioReactorException);
        }
    }

    /**
     *
     * 获取初始化配置的RequestConfig
     * @return 初始化配置的RequestConfig
     * @author hjr
     * @date 2022/2/27 12:45
     */
    @Bean
    public RequestConfig getRequestConfig(){
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectRequestTimeout)
                .setSocketTimeout(socketTimeout);
        return builder.build();
    }
}
