package com.ruoyi.payTxnJnl.mapper;

import java.util.List;
import com.ruoyi.payTxnJnl.domain.PayTxnJnl;

/**
 * 支付流水Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
public interface PayTxnJnlMapper 
{
    /**
     * 查询支付流水
     * 
     * @param id 支付流水主键
     * @return 支付流水
     */
    public PayTxnJnl selectPayTxnJnlById(String id);

    /**
     * 查询支付流水列表
     * 
     * @param payTxnJnl 支付流水
     * @return 支付流水集合
     */
    public List<PayTxnJnl> selectPayTxnJnlList(PayTxnJnl payTxnJnl);

    /**
     * 新增支付流水
     * 
     * @param payTxnJnl 支付流水
     * @return 结果
     */
    public int insertPayTxnJnl(PayTxnJnl payTxnJnl);

    /**
     * 修改支付流水
     * 
     * @param payTxnJnl 支付流水
     * @return 结果
     */
    public int updatePayTxnJnl(PayTxnJnl payTxnJnl);

    /**
     * 删除支付流水
     * 
     * @param id 支付流水主键
     * @return 结果
     */
    public int deletePayTxnJnlById(String id);

    /**
     * 批量删除支付流水
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayTxnJnlByIds(String[] ids);
}
