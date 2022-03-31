package com.cn.petshome.paymentgateway.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 *
 * 处理请求相关的工具类
 * @date 2022/1/18 14:23
 */
@Slf4j
public class RequestUtil {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    /**
     *
     * 解析请求参数
     * @param request 请求
     * @return 参数map
     * @author hjr
     * @date 2022/1/21 15:20
     */
    public static Map<String, String> getAllRequestParams(HttpServletRequest request){
        log.info("进入请求参数解析方法，入参：{}", request);

        Map<String, String> params = new HashMap<>(20);
        Enumeration<String> paramNames = request.getParameterNames();
        if (paramNames != null){
            while (paramNames.hasMoreElements()){
                String key = paramNames.nextElement();
                String value = request.getParameter(key);
                params.put(key, value);
            }
        }

        log.info("请求参数解析完成，返回：{}", params);
        return params;
    }

}

