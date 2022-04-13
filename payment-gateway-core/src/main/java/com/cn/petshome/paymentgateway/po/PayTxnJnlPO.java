package com.cn.petshome.paymentgateway.po;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 支付流水po
 * @date 2022/2/21 14:40
 */
@Data
public class PayTxnJnlPO implements Serializable {
    private Long id;

    private String userId;

    private String payOrderId;

    private String requestTxnJnl;

    private String responseTxnJnl;

    private String requestOrderId;

    private String requestOrderType;

    private String payMethod;

    private Integer number;

    private Long amount;

    private String txnType;

    private String status;

    private String tmpSmp;

    private String nodeId;

    private String regionId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getRequestTxnJnl() {
        return requestTxnJnl;
    }

    public void setRequestTxnJnl(String requestTxnJnl) {
        this.requestTxnJnl = requestTxnJnl;
    }

    public String getResponseTxnJnl() {
        return responseTxnJnl;
    }

    public void setResponseTxnJnl(String responseTxnJnl) {
        this.responseTxnJnl = responseTxnJnl;
    }

    public String getRequestOrderId() {
        return requestOrderId;
    }

    public void setRequestOrderId(String requestOrderId) {
        this.requestOrderId = requestOrderId;
    }

    public String getRequestOrderType() {
        return requestOrderType;
    }

    public void setRequestOrderType(String requestOrderType) {
        this.requestOrderType = requestOrderType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTmpSmp() {
        return tmpSmp;
    }

    public void setTmpSmp(String tmpSmp) {
        this.tmpSmp = tmpSmp;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}