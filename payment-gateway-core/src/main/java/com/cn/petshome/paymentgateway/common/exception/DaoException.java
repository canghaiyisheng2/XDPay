package com.cn.petshome.paymentgateway.common.exception;

/**
 *
 * 自定义持久层异常
 * @date 2022/2/11 13:33
 */
public class DaoException extends RuntimeException{
    public DaoException(){
        super();
    }

    public DaoException(String message){
        super(message);
    }

    public DaoException(String message, Throwable cause){
        super(message, cause);
    }
}
