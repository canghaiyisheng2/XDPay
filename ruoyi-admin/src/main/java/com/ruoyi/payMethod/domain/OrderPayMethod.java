package com.ruoyi.payMethod.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单支付方式信息对象 t_order_pay_method
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
public class OrderPayMethod extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 支付订单编号 */
    @Excel(name = "支付订单编号")
    private String payOrderId;

    /** 请求订单编号 */
    @Excel(name = "请求订单编号")
    private String requestOrderId;

    /** 支付方式 0现金 1积分  2代金券 */
    @Excel(name = "支付方式 0现金 1积分  2代金券")
    private String payMethod;

    /** 支付凭证   积分填冻结编号   代金券填代金券编号 */
    @Excel(name = "支付凭证   积分填冻结编号   代金券填代金券编号")
    private String payVoucher;

    /** 数量 */
    @Excel(name = "数量")
    private Long number;

    /** 折合金额 */
    @Excel(name = "折合金额")
    private Long amount;

    /** 交易状态  0初始登记  1冻结成功   2支付成功  3失败     */
    @Excel(name = "交易状态  0初始登记  1冻结成功   2支付成功  3失败    ")
    private String status;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setPayOrderId(String payOrderId) 
    {
        this.payOrderId = payOrderId;
    }

    public String getPayOrderId() 
    {
        return payOrderId;
    }
    public void setRequestOrderId(String requestOrderId) 
    {
        this.requestOrderId = requestOrderId;
    }

    public String getRequestOrderId() 
    {
        return requestOrderId;
    }
    public void setPayMethod(String payMethod) 
    {
        this.payMethod = payMethod;
    }

    public String getPayMethod() 
    {
        return payMethod;
    }
    public void setPayVoucher(String payVoucher) 
    {
        this.payVoucher = payVoucher;
    }

    public String getPayVoucher() 
    {
        return payVoucher;
    }
    public void setNumber(Long number) 
    {
        this.number = number;
    }

    public Long getNumber() 
    {
        return number;
    }
    public void setAmount(Long amount) 
    {
        this.amount = amount;
    }

    public Long getAmount() 
    {
        return amount;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("payOrderId", getPayOrderId())
            .append("requestOrderId", getRequestOrderId())
            .append("payMethod", getPayMethod())
            .append("payVoucher", getPayVoucher())
            .append("number", getNumber())
            .append("amount", getAmount())
            .append("status", getStatus())
            .toString();
    }
}
