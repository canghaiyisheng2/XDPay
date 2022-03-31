package com.cn.petshome.petspub.httpclient.service.impl;

import com.cn.petshome.petspub.httpclient.exception.HttpClientException;
import com.cn.petshome.petspub.httpclient.service.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 提供HttpClient服务
 * @date 2022/2/24 21:08
 */
@Service
@Slf4j
public class HttpClientServiceImpl implements HttpClientService {

    @Autowired
    @Qualifier(value = "defaultHttpAsyncClient")
    private CloseableHttpAsyncClient httpClient;

    /**
     *
     * 发送post请求
     * @param url 请求url地址
     * @param params 请求携带参数
     * @param charset 字符标准
     * @author hjr
     * @date 2022/2/27 12:46
     */
    @Override
    public void doPost(String url, Map<String, String> params, String charset) {
        doPost(url, params, getDefaultFutureCallback(charset), charset);
    }

    /**
     *
     * 发送post请求
     * @param url 请求url地址
     * @param params 请求携带参数
     * @param futureCallback 异步请求返回调用
     * @param charset 字符标准
     * @author hjr
     * @date 2022/2/27 12:46
     */
    @Override
    public void doPost(String url, Map<String, String> params, FutureCallback<HttpResponse> futureCallback, String charset){
        log.info("正在启动HttpClient……");
        httpClient.start();
        log.info("HttpClient启动成功");

        // 定义请求类型
        HttpPost httpPost = new HttpPost(url);

        // 判断字符集是否为null
        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        // 是否传递参数
        if (!ObjectUtils.isEmpty(params)) {
            // 将数据封装到List集合中
            List<NameValuePair> parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 模拟表单提交
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, charset);
                httpPost.setEntity(formEntity);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new HttpClientException("不支持的编码格式", unsupportedEncodingException);
            }
        }

        // 发送异步请求
        httpClient.execute(httpPost, futureCallback);
        try {
            log.info("关闭");
            httpClient.close();
        } catch (IOException ioException) {
            throw new HttpClientException("HttpClient关闭失败", ioException);
        }
    }

    @Override
    public void doJsonPost(String url, String jsonString, String charset) {
        doJsonPost(url, jsonString, getDefaultFutureCallback(charset), charset);
    }

    @Override
    public void doJsonPost(String url, String jsonString, FutureCallback<HttpResponse> futureCallback, String charset) {
        log.info("正在启动HttpClient……");
        httpClient.start();
        log.info("HttpClient启动成功");

        // 定义请求类型
        HttpPost httpPost = new HttpPost(url);

        // 判断字符集是否为null
        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        // 是否传递参数
        if (!StringUtils.isEmpty(jsonString)) {

            // 模拟表单提交
            try {
                httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
                StringEntity stringEntity = new StringEntity(jsonString);
                stringEntity.setContentEncoding(charset);
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new HttpClientException("不支持的编码格式", unsupportedEncodingException);
            }
        }

        // 发送异步请求
        httpClient.execute(httpPost, futureCallback);
        try {
            log.info("关闭");
            httpClient.close();
        } catch (IOException ioException) {
            throw new HttpClientException("HttpClient关闭失败", ioException);
        }
    }

    /**
     *
     * 设置异步处理调用
     * @param charset 字符标准
     * @return 异步处理方法
     * @author hjr
     * @date 2022/2/27 12:48
     */
    private FutureCallback<HttpResponse> getDefaultFutureCallback(String charset){
        return new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try{
                    log.info("异步请求成功返回：{}", result.getStatusLine());
                    // 判断返回值状态
                    if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String resultContent = EntityUtils.toString(result.getEntity(), charset);
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
                throw new HttpClientException("请求发送失败", ex);
            }

            @Override
            public void cancelled() {
                log.info("异步请求取消");
            }
        };
    }
}
