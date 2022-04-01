package com.ruoyi.payOrder.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付订单对象 t_pay_order
 * 
 * @author ruoyi
 * @date 2022-03-25
 */
public class PayOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 支付订单编号 */
    @Excel(name = "支付订单编号")
    private String payOrderId;

    /** 请求流水 */
    @Excel(name = "请求流水")
    private String requestOrderId;

    /** 订单类型  0-用品订单  1-服务订单 */
    @Excel(name = "订单类型  0-用品订单  1-服务订单")
    private String requestOrderType;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private Long totalAmount;

    /** 现金支付总金额 */
    @Excel(name = "现金支付总金额")
    private Long actualPayAmt;

    /** 其他方式支付金额 */
    @Excel(name = "其他方式支付金额")
    private Long otherPayAmt;

    /** 授权码，条码支付的授权码 */
    @Excel(name = "授权码，条码支付的授权码")
    private String authCode;

    /** 支付数据域：1.扫码支付时，返回的支付URL数据域；2.JSAPI支付时为支付机构的payInfo字段 */
    @Excel(name = "支付数据域：1.扫码支付时，返回的支付URL数据域；2.JSAPI支付时为支付机构的payInfo字段")
    private String payData;

    /** 支付图片URL：条码支付时，包含支付链接二维码图片的链接，考虑兼容银联支付 */
    @Excel(name = "支付图片URL：条码支付时，包含支付链接二维码图片的链接，考虑兼容银联支付")
    private String payImageUrl;

    /** 附加信息,请求上送后,需要原样返回的信息 */
    @Excel(name = "附加信息,请求上送后,需要原样返回的信息")
    private String orderAppendix;

    /** 订单标题 */
    @Excel(name = "订单标题")
    private String subject;

    /** 失效时间 */
    @Excel(name = "失效时间")
    private String expireTime;

    /** 支付渠道  0微信  1支付宝 */
    @Excel(name = "支付渠道  0微信  1支付宝")
    private String channelType;

    /** 支付类型 */
    @Excel(name = "支付类型")
    private String paymentType;

    /** 交易状态  0初始登记  1下单成功  2支付成功     */
    @Excel(name = "交易状态  0初始登记  1下单成功  2支付成功    ")
    private String status;

    /** 请求支付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "请求支付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date requestDate;

    /** 请求支付时间 */
    @Excel(name = "请求支付时间")
    private String requestTime;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishDate;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private String finishTime;

    /** 请求方异步通知地址 */
    @Excel(name = "请求方异步通知地址")
    private String notifyUrl;

    /** 请求方异步通知日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "请求方异步通知日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date notifyDate;

    /** 请求方异步通知时间 */
    @Excel(name = "请求方异步通知时间")
    private String notifyTime;

    /** 与账务方对账状态  0未对账 */
    @Excel(name = "与账务方对账状态  0未对账")
    private String chkSts;

    /** 对账批次号 */
    @Excel(name = "对账批次号")
    private String chkBatNo;

    /** 清算日期 */
    @Excel(name = "清算日期")
    private String settleDate;

    /** 时间戳 */
    @Excel(name = "时间戳")
    private String tmpSmp;

    /** 节点ID */
    @Excel(name = "节点ID")
    private String nodeId;

    /** 集群ID */
    @Excel(name = "集群ID")
    private String regionId;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
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
    public void setRequestOrderType(String requestOrderType) 
    {
        this.requestOrderType = requestOrderType;
    }

    public String getRequestOrderType() 
    {
        return requestOrderType;
    }
    public void setTotalAmount(Long totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public Long getTotalAmount() 
    {
        return totalAmount;
    }
    public void setActualPayAmt(Long actualPayAmt) 
    {
        this.actualPayAmt = actualPayAmt;
    }

    public Long getActualPayAmt() 
    {
        return actualPayAmt;
    }
    public void setOtherPayAmt(Long otherPayAmt) 
    {
        this.otherPayAmt = otherPayAmt;
    }

    public Long getOtherPayAmt() 
    {
        return otherPayAmt;
    }
    public void setAuthCode(String authCode) 
    {
        this.authCode = authCode;
    }

    public String getAuthCode() 
    {
        return authCode;
    }
    public void setPayData(String payData) 
    {
        this.payData = payData;
    }

    public String getPayData() 
    {
        return payData;
    }
    public void setPayImageUrl(String payImageUrl) 
    {
        this.payImageUrl = payImageUrl;
    }

    public String getPayImageUrl() 
    {
        return payImageUrl;
    }
    public void setOrderAppendix(String orderAppendix) 
    {
        this.orderAppendix = orderAppendix;
    }

    public String getOrderAppendix() 
    {
        return orderAppendix;
    }
    public void setSubject(String subject) 
    {
        this.subject = subject;
    }

    public String getSubject() 
    {
        return subject;
    }
    public void setExpireTime(String expireTime) 
    {
        this.expireTime = expireTime;
    }

    public String getExpireTime() 
    {
        return expireTime;
    }
    public void setChannelType(String channelType) 
    {
        this.channelType = channelType;
    }

    public String getChannelType() 
    {
        return channelType;
    }
    public void setPaymentType(String paymentType) 
    {
        this.paymentType = paymentType;
    }

    public String getPaymentType() 
    {
        return paymentType;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setRequestDate(Date requestDate) 
    {
        this.requestDate = requestDate;
    }

    public Date getRequestDate() 
    {
        return requestDate;
    }
    public void setRequestTime(String requestTime) 
    {
        this.requestTime = requestTime;
    }

    public String getRequestTime() 
    {
        return requestTime;
    }
    public void setFinishDate(Date finishDate) 
    {
        this.finishDate = finishDate;
    }

    public Date getFinishDate() 
    {
        return finishDate;
    }
    public void setFinishTime(String finishTime) 
    {
        this.finishTime = finishTime;
    }

    public String getFinishTime() 
    {
        return finishTime;
    }
    public void setNotifyUrl(String notifyUrl) 
    {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyUrl() 
    {
        return notifyUrl;
    }
    public void setNotifyDate(Date notifyDate) 
    {
        this.notifyDate = notifyDate;
    }

    public Date getNotifyDate() 
    {
        return notifyDate;
    }
    public void setNotifyTime(String notifyTime) 
    {
        this.notifyTime = notifyTime;
    }

    public String getNotifyTime() 
    {
        return notifyTime;
    }
    public void setChkSts(String chkSts) 
    {
        this.chkSts = chkSts;
    }

    public String getChkSts() 
    {
        return chkSts;
    }
    public void setChkBatNo(String chkBatNo) 
    {
        this.chkBatNo = chkBatNo;
    }

    public String getChkBatNo() 
    {
        return chkBatNo;
    }
    public void setSettleDate(String settleDate) 
    {
        this.settleDate = settleDate;
    }

    public String getSettleDate() 
    {
        return settleDate;
    }
    public void setTmpSmp(String tmpSmp) 
    {
        this.tmpSmp = tmpSmp;
    }

    public String getTmpSmp() 
    {
        return tmpSmp;
    }
    public void setNodeId(String nodeId) 
    {
        this.nodeId = nodeId;
    }

    public String getNodeId() 
    {
        return nodeId;
    }
    public void setRegionId(String regionId) 
    {
        this.regionId = regionId;
    }

    public String getRegionId() 
    {
        return regionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("payOrderId", getPayOrderId())
            .append("requestOrderId", getRequestOrderId())
            .append("requestOrderType", getRequestOrderType())
            .append("totalAmount", getTotalAmount())
            .append("actualPayAmt", getActualPayAmt())
            .append("otherPayAmt", getOtherPayAmt())
            .append("authCode", getAuthCode())
            .append("payData", getPayData())
            .append("payImageUrl", getPayImageUrl())
            .append("orderAppendix", getOrderAppendix())
            .append("subject", getSubject())
            .append("expireTime", getExpireTime())
            .append("channelType", getChannelType())
            .append("paymentType", getPaymentType())
            .append("status", getStatus())
            .append("requestDate", getRequestDate())
            .append("requestTime", getRequestTime())
            .append("finishDate", getFinishDate())
            .append("finishTime", getFinishTime())
            .append("notifyUrl", getNotifyUrl())
            .append("notifyDate", getNotifyDate())
            .append("notifyTime", getNotifyTime())
            .append("chkSts", getChkSts())
            .append("chkBatNo", getChkBatNo())
            .append("settleDate", getSettleDate())
            .append("tmpSmp", getTmpSmp())
            .append("nodeId", getNodeId())
            .append("regionId", getRegionId())
            .toString();
    }
}
