package com.cn.petshome.paymentgateway.common.request;

import com.cn.petshome.paymentgateway.po.OrderPayMethodDO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * 订单请求vo
 */
@Data
public class PayOrderRequest {

    @NotBlank(message = "用户ID不得为空")
    private String userId;

    @NotBlank(message = "请求订单编号不得为空")
    private String requestOrderId;

    @NotBlank(message = "请求订单类型不得为空")
    private String requestOrderType;

    @NotNull(message = "订单总金额不得为空")
    private Long totalAmount;

    @NotBlank(message = "支付渠道不得为空")
    private String channelType;

    @NotBlank(message = "支付类型不得为空")
    private String paymentType;

    @NotBlank(message = "异步通知地址不得为空")
    private String notifyUrl;

    @NotBlank(message = "订单标题不得为空")
    private String subject;

    @NotBlank(message = "附加信息不得为空")
    private String orderAppendix;

    @NotEmpty(message = "支付方式不得为空")
    private List<OrderPayMethodDO> payMethods;

    public List<OrderPayMethodDO> getPayMethods() {
        return payMethods;
    }

}