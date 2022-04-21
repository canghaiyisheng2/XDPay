package com.ruoyi.web.service.impl;

import com.ruoyi.web.service.NotifyService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2022/4/19 10:26
 */
@Service
public class NotifyServiceImpl implements NotifyService {


    /**
     *
     * 解析异步通知
     * @param request 请求主体
     * @return 异步通知主体
     * @author hjr
     * @date 2022/4/19 10:28
     */
    @Override
    public Map<String, String> resolveNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(20);
        Enumeration<String> paramNames = request.getParameterNames();
        if (paramNames != null){
            while (paramNames.hasMoreElements()){
                String key = paramNames.nextElement();
                String value = request.getParameter(key);
                params.put(key, value);
            }
        }

        return params;
    }
}
