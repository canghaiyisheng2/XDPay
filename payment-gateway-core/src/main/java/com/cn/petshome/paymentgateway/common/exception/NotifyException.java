package com.cn.petshome.paymentgateway.common.exception;

/**
 *
 * 自定义异步通知处理异常
 * @date 2022/2/11 13:08
 */
public class NotifyException extends RuntimeException{
    public NotifyException(){
        super();
    }

    public NotifyException(String message){
        super(message);
    }

    public NotifyException(String message, Throwable cause){
        super(message, cause);
    }
}


