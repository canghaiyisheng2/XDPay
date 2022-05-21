package com.ruoyi.web.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.core.config.WebSocketServer;
import com.ruoyi.web.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @date 2022/4/18 16:55
 */
@RestController
public class ReceiveNotifyController {

    @Autowired
    private NotifyService notifyService;
    @Autowired
    private WebSocketServer webSocketServer;

    @PostMapping("notify")
    public AjaxResult receiveNotify(HttpServletRequest request) throws IOException {
        System.out.println("接收到异步消息");
        //解析异步请求
        Map<String, String> notifyParams = notifyService.resolveNotify(request);
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : notifyParams.entrySet()){
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        //推送到前端
        webSocketServer.sendToUser(jsonObject.toJSONString(), "1");

        return AjaxResult.success(notifyParams);
    }
}
