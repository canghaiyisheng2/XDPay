package com.cn.petshome.paymentgateway.common.response;

import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.xidian.fixedLength.FixedLength;
import com.cn.xidian.fixedLength.FixedLengthResolveException;
import com.cn.xidian.fixedLength.Resolver;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * 预下单返回响应体数据
 * @date 2022/1/22 11:13
 */
@Data
@AllArgsConstructor
public class PrePlacePayOrderResponse {

    @FixedLength(30)
    String payOrderId;
    @FixedLength(2018)
    String returnForm;

    public String toFixedLengthString() throws FixedLengthResolveException {
        return new Resolver<>(PrePlacePayOrderResponse.class).stringify(this);
    }
}
