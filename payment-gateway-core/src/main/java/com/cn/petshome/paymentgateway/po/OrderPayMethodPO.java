package com.cn.petshome.paymentgateway.po;

import com.cn.xidian.fixedLength.FixedLength;
import com.cn.xidian.fixedLength.ResolveIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 支付方式po
 * @date 2022/2/21 14:40
 */
@Data
public class OrderPayMethodPO implements Serializable {

    @ResolveIgnore
    private Long id;

    @ResolveIgnore
    private String payOrderId;

    @ResolveIgnore
    private String requestOrderId;

    @FixedLength(2)
    private String payMethod;

    @FixedLength(20)
    private String payVoucher;

    @FixedLength(9)
    private Integer number;

    @FixedLength(9)
    private Long amount;

    @ResolveIgnore
    private String status;

    @ResolveIgnore
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getRequestOrderId() {
        return requestOrderId;
    }

    public void setRequestOrderId(String requestOrderId) {
        this.requestOrderId = requestOrderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayVoucher() {
        return payVoucher;
    }

    public void setPayVoucher(String payVoucher) {
        this.payVoucher = payVoucher;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}