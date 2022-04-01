package com.ruoyi.web.task;

import com.ruoyi.payMethod.mapper.OrderPayMethodMapper;
import com.ruoyi.payOrder.mapper.PayOrderMapper;
import com.ruoyi.payOrderCount.domain.OrderPayCount;
import com.ruoyi.payOrderCount.mapper.OrderPayCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 2022/3/24 15:42
 * @author hjr
 */
@Component("payOrderTask")
public class payOrderTask {
    @Autowired
    private OrderPayCountMapper orderPayCountMapper;
    @Autowired
    private PayOrderMapper payOrderMapper;

    private Long lastCount = null;

    public void recordCount(){
        Long currentCount = payOrderMapper.getOrderCount();
        OrderPayCount orderPayCount = new OrderPayCount();
        orderPayCount.setRecordDate(new Date());
        orderPayCount.setRecordTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        orderPayCount.setCount(lastCount == null? 0:currentCount - lastCount);
        lastCount = currentCount;
        orderPayCountMapper.insertOrderPayCount(orderPayCount);

    }
}
