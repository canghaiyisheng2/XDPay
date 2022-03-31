package com.cn.petshome.paymentgateway.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 请求响应体
 * @date 2022/2/21 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResponse<T> {
    private Integer code;
    private Integer status;
    private T data;
    private String msg;

    public static<T> JsonResponse<T> buildSuccess(T data){
        return buildSuccess(200, data, "");
    }

    public static<T> JsonResponse<T> buildSuccess(T data, String msg){
        return buildSuccess(200, data, msg);
    }

    public static<T> JsonResponse<T> buildSuccess(Integer status, T data, String msg){
        return new JsonResponse<T>(0, status, data, msg);
    }

    public static<T> JsonResponse<T> buildError(String msg){
        return buildError(500, null, msg);
    }

    public static<T> JsonResponse<T> buildError(T data, String msg){
        return buildError(500, data, msg);
    }

    public static<T> JsonResponse<T> buildError(Integer status, T data, String msg){
        return new JsonResponse<T>(-1, status, data, msg);
    }
}
