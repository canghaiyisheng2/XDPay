package com.cn.petshome.paymentgateway.common.exception;

/**
 *
 * 调用外部接口产生的异常
 * @date 2022/4/3 15:39
 */
public class ExternalException extends RuntimeException{
    public ExternalException(){
        super();
    }

    public ExternalException(String message){
        super(message);
    }

    public ExternalException(String message, Throwable cause){
        super(message, cause);
    }
}