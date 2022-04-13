package com.cn.petshome.paymentgateway.bo;

import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.common.util.CompensationUtil;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 * 补偿机制下延迟队列的元素
 * @date 2022/4/5 13:32
 */
@Data
public class DelayNotifyElement<T> implements Delayed {

    private long delayTime = System.currentTimeMillis();
    private int delayLevel = 0;
    private T data;

    public DelayNotifyElement(int delayLevel, T data) {
        if (delayLevel >= CompensationUtil.DELAY_LEVEL_LIST.length){
            throw new NotifyException("不合法的延迟级别");
        }
        this.delayTime = this.delayTime + CompensationUtil.DELAY_LEVEL_LIST[delayLevel];
        this.delayLevel = delayLevel;
        this.data = data;
    }

    public DelayNotifyElement(T data) {
        this.delayTime = this.delayTime + CompensationUtil.DELAY_LEVEL_LIST[this.delayLevel];
        this.data = data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }
}
