package com.ruoyi.payTxnJnl.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付流水对象 t_pay_txn_jnl
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
public class PayTxnJnl extends BaseEntity
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

    /** 交易流水号 */
    @Excel(name = "交易流水号")
    private String requestTxnJnl;

    /** 交易流水号 */
    @Excel(name = "交易流水号")
    private String responseTxnJnl;

    /** 请求流水 */
    @Excel(name = "请求流水")
    private String requestOrderId;

    /** 订单类型  0-用品订单  1-服务订单 */
    @Excel(name = "订单类型  0-用品订单  1-服务订单")
    private String requestOrderType;

    /** 支付方式 0现金 1积分  2代金券 */
    @Excel(name = "支付方式 0现金 1积分  2代金券")
    private String payMethod;

    /** 数量 */
    @Excel(name = "数量")
    private Long number;

    /** 折合金额 */
    @Excel(name = "折合金额")
    private Long amount;

    /** 交易类型  0冻结   1记账   2解冻   3解冻支取  4预下单 */
    @Excel(name = "交易类型  0冻结   1记账   2解冻   3解冻支取  4预下单")
    private String txnType;

    /** 交易状态  S成功  F失败  U超时   */
    @Excel(name = "交易状态  S成功  F失败  U超时  ")
    private String status;

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
    public void setRequestTxnJnl(String requestTxnJnl) 
    {
        this.requestTxnJnl = requestTxnJnl;
    }

    public String getRequestTxnJnl() 
    {
        return requestTxnJnl;
    }
    public void setResponseTxnJnl(String responseTxnJnl) 
    {
        this.responseTxnJnl = responseTxnJnl;
    }

    public String getResponseTxnJnl() 
    {
        return responseTxnJnl;
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
    public void setPayMethod(String payMethod) 
    {
        this.payMethod = payMethod;
    }

    public String getPayMethod() 
    {
        return payMethod;
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
    public void setTxnType(String txnType) 
    {
        this.txnType = txnType;
    }

    public String getTxnType() 
    {
        return txnType;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("requestTxnJnl", getRequestTxnJnl())
            .append("responseTxnJnl", getResponseTxnJnl())
            .append("requestOrderId", getRequestOrderId())
            .append("requestOrderType", getRequestOrderType())
            .append("payMethod", getPayMethod())
            .append("number", getNumber())
            .append("amount", getAmount())
            .append("txnType", getTxnType())
            .append("status", getStatus())
            .append("tmpSmp", getTmpSmp())
            .append("nodeId", getNodeId())
            .append("regionId", getRegionId())
            .toString();
    }
}
