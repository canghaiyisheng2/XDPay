package com.cn.petshome.paymentgateway.common.response;

import com.cn.xidian.fixedLength.FixedLength;
import com.cn.xidian.fixedLength.Nullable;
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
public class ResponseBean<T> {

    @FixedLength(2)
    private Integer code;
    @FixedLength(3)
    private Integer status;
    @FixedLength(2048)
    @Nullable
    private T data;
    @FixedLength(128)
    private String msg;

    public static<T> ResponseBean<T> buildSuccess(T data){
        return buildSuccess(200, data, "");
    }

    public static<T> ResponseBean<T> buildSuccess(T data, String msg){
        return buildSuccess(200, data, msg);
    }

    public static<T> ResponseBean<T> buildSuccess(Integer status, T data, String msg){
        return new ResponseBean<T>(0, status, data, msg);
    }

    public static<T> ResponseBean<T> buildError(String msg){
        return buildError(500, null, msg);
    }

    public static<T> ResponseBean<T> buildError(T data, String msg){
        return buildError(500, data, msg);
    }

    public static<T> ResponseBean<T> buildError(Integer status, T data, String msg){
        return new ResponseBean<T>(-1, status, data, msg);
    }
}
