package com.ruoyi.payOrderCount.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单请求数记录对象 t_order_pay_count
 * 
 * @author ruoyi
 * @date 2022-03-24
 */
public class OrderPayCount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String id;

    /** 记录日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recordDate;

    /** 记录时间 */
    @Excel(name = "记录时间")
    private String recordTime;

    /** 记录请求数量 */
    @Excel(name = "记录请求数量")
    private Long count;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setRecordDate(Date recordDate) 
    {
        this.recordDate = recordDate;
    }

    public Date getRecordDate() 
    {
        return recordDate;
    }
    public void setRecordTime(String recordTime) 
    {
        this.recordTime = recordTime;
    }

    public String getRecordTime() 
    {
        return recordTime;
    }
    public void setCount(Long count) 
    {
        this.count = count;
    }

    public Long getCount() 
    {
        return count;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recordDate", getRecordDate())
            .append("recordTime", getRecordTime())
            .append("count", getCount())
            .toString();
    }
}
