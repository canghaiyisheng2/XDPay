package com.cn.petshome.paymentgateway.common.config;

import com.cn.petshome.paymentgateway.common.exception.PaymentException;
import com.cn.petshome.paymentgateway.common.response.JsonResponse;
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
    public JsonResponse<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("校验异常", methodArgumentNotValidException);
        return JsonResponse.buildError(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     *
     * 支付异常处理
     * @param paymentException 异常对象
     */
    @ExceptionHandler(value = {PaymentException.class})
    public JsonResponse<String> handlerPaymentException(PaymentException paymentException){
        log.error("支付异常", paymentException);
        return JsonResponse.buildError(paymentException.getMessage());
    }

    /**
     *
     * 异步通知时异常处理
     * @param notifyException 异常对象
     */
    @ExceptionHandler(value = {NotifyException.class})
    public JsonResponse<String> handlerNotifyException(NotifyException notifyException){
        log.error("异步通知异常", notifyException);
        //异步通知处理失败，需要补偿措施

        return JsonResponse.buildError("系统内部错误");
    }

    /**
     *
     * 持久层异常处理
     * @param daoException 异常对象
     */
    @ExceptionHandler(value = {DaoException.class})
    public JsonResponse<String> handlerDaoException(DaoException daoException){
        log.error("持久层异常", daoException);
        return JsonResponse.buildError("系统内部错误");
    }

    /**
     *
     * HttpClient异常处理
     * @param httpClientException 异常对象
     */
    @ExceptionHandler(value = {HttpClientException.class})
    public JsonResponse<String> handlerHttpClientException(HttpClientException httpClientException){
        log.error("HttpClient异常", httpClientException);
        return JsonResponse.buildError(httpClientException.getMessage());
    }
}
