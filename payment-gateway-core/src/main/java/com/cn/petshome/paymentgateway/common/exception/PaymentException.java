package com.cn.petshome.paymentgateway.common.exception;

/**
 *
 * 自定义支付异常
 * @date 2022/1/24 18:26
 */
public class PaymentException extends RuntimeException{
    public PaymentException(){
        super();
    }

    public PaymentException(String message){
        super(message);
    }

    public PaymentException(String message, Throwable cause){
        super(message, cause);
    }
}
