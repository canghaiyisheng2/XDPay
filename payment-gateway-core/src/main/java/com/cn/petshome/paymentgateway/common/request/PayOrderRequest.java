package com.cn.petshome.paymentgateway.common.request;

import com.cn.petshome.paymentgateway.po.OrderPayMethodPO;
import com.cn.xidian.fixedLength.FixedLength;
import com.cn.xidian.fixedLength.Resolver;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 订单请求vo
 */
@Data
public class PayOrderRequest {

    @NotBlank(message = "用户ID不得为空")
    @FixedLength(20)
    private String userId;

    @NotBlank(message = "请求订单编号不得为空")
    @FixedLength(30)
    private String requestOrderId;

    @NotBlank(message = "请求订单类型不得为空")
    @FixedLength(1)
    private String requestOrderType;

    @NotNull(message = "订单总金额不得为空")
    @FixedLength(9)
    private Long totalAmount;

    @NotBlank(message = "支付渠道不得为空")
    @FixedLength(1)
    private String channelType;

    @NotBlank(message = "支付类型不得为空")
    @FixedLength(14)
    private String paymentType;

    @NotBlank(message = "异步通知地址不得为空")
    @FixedLength(256)
    @URL
    private String notifyUrl;

    @NotBlank(message = "订单标题不得为空")
    @FixedLength(64)
    private String subject;

    @NotBlank(message = "附加信息不得为空")
    @FixedLength(128)
    private String orderAppendix;

    @NotEmpty(message = "支付方式不得为空")
    private List<OrderPayMethodPO> payMethods;

    public List<OrderPayMethodPO> getPayMethods() {
        return payMethods;
    }
}