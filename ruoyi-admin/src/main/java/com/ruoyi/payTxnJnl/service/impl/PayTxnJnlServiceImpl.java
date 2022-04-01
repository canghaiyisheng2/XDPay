package com.ruoyi.payTxnJnl.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.payTxnJnl.mapper.PayTxnJnlMapper;
import com.ruoyi.payTxnJnl.domain.PayTxnJnl;
import com.ruoyi.payTxnJnl.service.IPayTxnJnlService;

/**
 * 支付流水Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
@Service
public class PayTxnJnlServiceImpl implements IPayTxnJnlService 
{
    @Autowired
    private PayTxnJnlMapper payTxnJnlMapper;

    /**
     * 查询支付流水
     * 
     * @param id 支付流水主键
     * @return 支付流水
     */
    @Override
    public PayTxnJnl selectPayTxnJnlById(String id)
    {
        return payTxnJnlMapper.selectPayTxnJnlById(id);
    }

    /**
     * 查询支付流水列表
     * 
     * @param payTxnJnl 支付流水
     * @return 支付流水
     */
    @Override
    public List<PayTxnJnl> selectPayTxnJnlList(PayTxnJnl payTxnJnl)
    {
        return payTxnJnlMapper.selectPayTxnJnlList(payTxnJnl);
    }

    /**
     * 新增支付流水
     * 
     * @param payTxnJnl 支付流水
     * @return 结果
     */
    @Override
    public int insertPayTxnJnl(PayTxnJnl payTxnJnl)
    {
        return payTxnJnlMapper.insertPayTxnJnl(payTxnJnl);
    }

    /**
     * 修改支付流水
     * 
     * @param payTxnJnl 支付流水
     * @return 结果
     */
    @Override
    public int updatePayTxnJnl(PayTxnJnl payTxnJnl)
    {
        return payTxnJnlMapper.updatePayTxnJnl(payTxnJnl);
    }

    /**
     * 批量删除支付流水
     * 
     * @param ids 需要删除的支付流水主键
     * @return 结果
     */
    @Override
    public int deletePayTxnJnlByIds(String[] ids)
    {
        return payTxnJnlMapper.deletePayTxnJnlByIds(ids);
    }

    /**
     * 删除支付流水信息
     * 
     * @param id 支付流水主键
     * @return 结果
     */
    @Override
    public int deletePayTxnJnlById(String id)
    {
        return payTxnJnlMapper.deletePayTxnJnlById(id);
    }
}
