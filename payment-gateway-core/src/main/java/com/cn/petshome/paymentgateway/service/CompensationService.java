package com.cn.petshome.paymentgateway.service;

import com.cn.petshome.paymentgateway.bo.DelayNotifyElement;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import org.springframework.stereotype.Service;

/**
 *
 * 补偿机制服务接口
 * @date 2022/4/5 13:25
 */
@Service
public interface CompensationService {

    /**
     *
     * 加入延时任务
     * @param element 延时任务
     * @author hjr
     * @date 2022/4/5 13:59
     */
    public void put(DelayNotifyElement<NotifyInfo> element);

    /**
     *
     * 取消延时任务
     * @param element 延时任务
     * @author hjr
     * @date 2022/4/5 13:59
     */
    public void remove(DelayNotifyElement<NotifyInfo> element);
}
