package com.cn.petshome.petspub.httpclient.exception;

/**
 *
 * 自定义HttpClient异常
 * @date 2022/2/24 21:17
 */
public class HttpClientException extends RuntimeException{
    public HttpClientException(){
        super();
    }

    public HttpClientException(String message){
        super(message);
    }

    public HttpClientException(String message, Throwable cause){
        super(message, cause);
    }
}
