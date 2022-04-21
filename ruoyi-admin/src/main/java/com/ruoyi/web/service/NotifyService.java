package com.ruoyi.web.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @date 2022/4/19 10:26
 */
public interface NotifyService {

    /**
     *
     * 解析异步通知
     * @param request 请求主体
     * @return 异步通知主体
     * @author hjr
     * @date 2022/4/19 10:28
     */
    public Map<String, String> resolveNotify(HttpServletRequest request);
}
