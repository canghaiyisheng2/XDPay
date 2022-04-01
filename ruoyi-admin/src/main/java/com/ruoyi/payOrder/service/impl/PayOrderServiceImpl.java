package com.ruoyi.payOrder.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.payOrder.mapper.PayOrderMapper;
import com.ruoyi.payOrder.domain.PayOrder;
import com.ruoyi.payOrder.service.IPayOrderService;

/**
 * 支付订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-25
 */
@Service
public class PayOrderServiceImpl implements IPayOrderService 
{
    @Autowired
    private PayOrderMapper payOrderMapper;

    /**
     * 查询支付订单
     * 
     * @param id 支付订单主键
     * @return 支付订单
     */
    @Override
    public PayOrder selectPayOrderById(String id)
    {
        return payOrderMapper.selectPayOrderById(id);
    }

    /**
     * 查询支付订单列表
     * 
     * @param payOrder 支付订单
     * @return 支付订单
     */
    @Override
    public List<PayOrder> selectPayOrderList(PayOrder payOrder)
    {
        return payOrderMapper.selectPayOrderList(payOrder);
    }

    /**
     * 新增支付订单
     * 
     * @param payOrder 支付订单
     * @return 结果
     */
    @Override
    public int insertPayOrder(PayOrder payOrder)
    {
        return payOrderMapper.insertPayOrder(payOrder);
    }

    /**
     * 修改支付订单
     * 
     * @param payOrder 支付订单
     * @return 结果
     */
    @Override
    public int updatePayOrder(PayOrder payOrder)
    {
        return payOrderMapper.updatePayOrder(payOrder);
    }

    /**
     * 批量删除支付订单
     * 
     * @param ids 需要删除的支付订单主键
     * @return 结果
     */
    @Override
    public int deletePayOrderByIds(String[] ids)
    {
        return payOrderMapper.deletePayOrderByIds(ids);
    }

    /**
     * 删除支付订单信息
     * 
     * @param id 支付订单主键
     * @return 结果
     */
    @Override
    public int deletePayOrderById(String id)
    {
        return payOrderMapper.deletePayOrderById(id);
    }
}
