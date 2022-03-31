package com.cn.petshome.petspub.httpclient.service;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.util.Map;

/**
 *
 * 提供HttpClient服务
 * @date 2022/2/25 15:24
 */
public interface HttpClientService {

    /**
     *
     * 发送post请求
     * @param url 请求url地址
     * @param params 请求携带参数
     * @param charset 字符标准
     * @author hjr
     * @date 2022/2/27 12:46
     */
    public void doPost(String url, Map<String, String> params, String charset);

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
    public void doPost(String url, Map<String, String> params, FutureCallback<HttpResponse> futureCallback, String charset);

    /**
     *
     * 发送post请求
     * @param url 请求url地址
     * @param jsonString 请求携带Json
     * @param charset 字符标准
     * @author hjr
     * @date 2022/2/27 12:46
     */
    public void doJsonPost(String url, String jsonString, String charset);

    /**
     *
     * 发送post请求
     * @param url 请求url地址
     * @param jsonString 请求携带参数
     * @param futureCallback 异步请求返回调用
     * @param charset 字符标准
     * @author hjr
     * @date 2022/2/27 12:46
     */
    public void doJsonPost(String url, String jsonString, FutureCallback<HttpResponse> futureCallback, String charset);
}
