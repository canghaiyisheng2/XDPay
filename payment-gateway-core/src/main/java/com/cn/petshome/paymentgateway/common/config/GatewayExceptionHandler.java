package com.cn.petshome.paymentgateway.common.config;

import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.common.exception.DaoException;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.petspub.httpclient.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 *
 * 全局异常处理
 * @author hjr
 */
@RestControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

    /**
     *
     * 校验异常处理
     * @param methodArgumentNotValidException 异常对象
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseBean<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("校验异常", methodArgumentNotValidException);
        return ResponseBean.buildError(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     *
     * 支付异常处理
     * @param paymentException 异常对象
     */
    @ExceptionHandler(value = {PaymentException.class})
    public ResponseBean<String> handlerPaymentException(PaymentException paymentException){
        log.error("支付异常", paymentException);
        return ResponseBean.buildError(paymentException.getMessage());
    }

    /**
     *
     * 异步通知时异常处理
     * @param notifyException 异常对象
     */
    @ExceptionHandler(value = {NotifyException.class})
    public ResponseBean<String> handlerNotifyException(NotifyException notifyException){
        log.error("异步通知异常", notifyException);
        return ResponseBean.buildError("系统内部错误");
    }

    /**
     *
     * 持久层异常处理
     * @param daoException 异常对象
     */
    @ExceptionHandler(value = {DaoException.class})
    public ResponseBean<String> handlerDaoException(DaoException daoException){
        log.error("持久层异常", daoException);
        return ResponseBean.buildError("系统内部错误");
    }

    /**
     *
     * HttpClient异常处理
     * @param httpClientException 异常对象
     */
    @ExceptionHandler(value = {HttpClientException.class})
    public ResponseBean<String> handlerHttpClientException(HttpClientException httpClientException){
        log.error("HttpClient异常", httpClientException);
        return ResponseBean.buildError(httpClientException.getMessage());
    }
}
