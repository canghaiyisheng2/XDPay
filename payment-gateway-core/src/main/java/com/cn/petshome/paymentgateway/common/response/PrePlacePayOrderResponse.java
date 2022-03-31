package com.cn.petshome.paymentgateway.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * 预下单返回响应体数据
 * @date 2022/1/22 11:13
 */
@Data
@AllArgsConstructor
public class PrePlacePayOrderResponse {

    String payOrderId;
    String returnForm;
}
