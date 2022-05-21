package com.cn.petshome.paymentgateway.common.exception;

/**
 *
 * 自定义支付异常(调用第三方支付api)
 * @date 2022/1/24 18:26
 */
public class ApiException extends RuntimeException{
    public ApiException(){
        super();
    }

    public ApiException(String message){
        super(message);
    }

    public ApiException(String message, Throwable cause){
        super(message, cause);
    }
}
