package com.ruoyi.payOrder.service;

import java.util.List;
import com.ruoyi.payOrder.domain.PayOrder;

/**
 * 支付订单Service接口
 * 
 * @author ruoyi
 * @date 2022-03-25
 */
public interface IPayOrderService 
{
    /**
     * 查询支付订单
     * 
     * @param id 支付订单主键
     * @return 支付订单
     */
    public PayOrder selectPayOrderById(String id);

    /**
     * 查询支付订单列表
     * 
     * @param payOrder 支付订单
     * @return 支付订单集合
     */
    public List<PayOrder> selectPayOrderList(PayOrder payOrder);

    /**
     * 新增支付订单
     * 
     * @param payOrder 支付订单
     * @return 结果
     */
    public int insertPayOrder(PayOrder payOrder);

    /**
     * 修改支付订单
     * 
     * @param payOrder 支付订单
     * @return 结果
     */
    public int updatePayOrder(PayOrder payOrder);

    /**
     * 批量删除支付订单
     * 
     * @param ids 需要删除的支付订单主键集合
     * @return 结果
     */
    public int deletePayOrderByIds(String[] ids);

    /**
     * 删除支付订单信息
     * 
     * @param id 支付订单主键
     * @return 结果
     */
    public int deletePayOrderById(String id);
}
