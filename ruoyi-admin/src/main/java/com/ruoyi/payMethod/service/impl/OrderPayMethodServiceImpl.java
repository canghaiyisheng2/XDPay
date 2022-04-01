package com.ruoyi.payMethod.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.payMethod.mapper.OrderPayMethodMapper;
import com.ruoyi.payMethod.domain.OrderPayMethod;
import com.ruoyi.payMethod.service.IOrderPayMethodService;

/**
 * 订单支付方式信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
@Service
public class OrderPayMethodServiceImpl implements IOrderPayMethodService 
{
    @Autowired
    private OrderPayMethodMapper orderPayMethodMapper;

    /**
     * 查询订单支付方式信息
     * 
     * @param id 订单支付方式信息主键
     * @return 订单支付方式信息
     */
    @Override
    public OrderPayMethod selectOrderPayMethodById(String id)
    {
        return orderPayMethodMapper.selectOrderPayMethodById(id);
    }

    /**
     * 查询订单支付方式信息列表
     * 
     * @param orderPayMethod 订单支付方式信息
     * @return 订单支付方式信息
     */
    @Override
    public List<OrderPayMethod> selectOrderPayMethodList(OrderPayMethod orderPayMethod)
    {
        return orderPayMethodMapper.selectOrderPayMethodList(orderPayMethod);
    }

    /**
     * 新增订单支付方式信息
     * 
     * @param orderPayMethod 订单支付方式信息
     * @return 结果
     */
    @Override
    public int insertOrderPayMethod(OrderPayMethod orderPayMethod)
    {
        return orderPayMethodMapper.insertOrderPayMethod(orderPayMethod);
    }

    /**
     * 修改订单支付方式信息
     * 
     * @param orderPayMethod 订单支付方式信息
     * @return 结果
     */
    @Override
    public int updateOrderPayMethod(OrderPayMethod orderPayMethod)
    {
        return orderPayMethodMapper.updateOrderPayMethod(orderPayMethod);
    }

    /**
     * 批量删除订单支付方式信息
     * 
     * @param ids 需要删除的订单支付方式信息主键
     * @return 结果
     */
    @Override
    public int deleteOrderPayMethodByIds(String[] ids)
    {
        return orderPayMethodMapper.deleteOrderPayMethodByIds(ids);
    }

    /**
     * 删除订单支付方式信息信息
     * 
     * @param id 订单支付方式信息主键
     * @return 结果
     */
    @Override
    public int deleteOrderPayMethodById(String id)
    {
        return orderPayMethodMapper.deleteOrderPayMethodById(id);
    }
}
