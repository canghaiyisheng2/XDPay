package com.cn.petshome.paymentgateway.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 支付订单po
 * @date 2022/2/21 14:40
 */
@Data
public class PayOrderPO implements Serializable {
    private Long id;

    private String userId;

    private String payOrderId;

    private String requestOrderId;

    private String requestOrderType;

    private Long totalAmount;

    private Long actualPayAmt;

    private Long otherPayAmt;

    private String authCode;

    private String payData;

    private String payImageUrl;

    private String orderAppendix;

    private String subject;

    private String expireTime;

    private String channelType;

    private String paymentType;

    private String status;

    private Date requestDate;

    private String requestTime;

    private Date finishDate;

    private String finishTime;

    private String notifyUrl;

    private Date notifyDate;

    private String notifyTime;

    private String chkSts;

    private String chkBatNo;

    private String settleDate;

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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getActualPayAmt() {
        return actualPayAmt;
    }

    public void setActualPayAmt(Long actualPayAmt) {
        this.actualPayAmt = actualPayAmt;
    }

    public Long getOtherPayAmt() {
        return otherPayAmt;
    }

    public void setOtherPayAmt(Long otherPayAmt) {
        this.otherPayAmt = otherPayAmt;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getPayData() {
        return payData;
    }

    public void setPayData(String payData) {
        this.payData = payData;
    }

    public String getPayImageUrl() {
        return payImageUrl;
    }

    public void setPayImageUrl(String payImageUrl) {
        this.payImageUrl = payImageUrl;
    }

    public String getOrderAppendix() {
        return orderAppendix;
    }

    public void setOrderAppendix(String orderAppendix) {
        this.orderAppendix = orderAppendix;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getChkSts() {
        return chkSts;
    }

    public void setChkSts(String chkSts) {
        this.chkSts = chkSts;
    }

    public String getChkBatNo() {
        return chkBatNo;
    }

    public void setChkBatNo(String chkBatNo) {
        this.chkBatNo = chkBatNo;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
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