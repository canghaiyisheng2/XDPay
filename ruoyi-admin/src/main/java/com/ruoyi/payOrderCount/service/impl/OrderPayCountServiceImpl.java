package com.ruoyi.payOrderCount.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.payOrderCount.mapper.OrderPayCountMapper;
import com.ruoyi.payOrderCount.domain.OrderPayCount;
import com.ruoyi.payOrderCount.service.IOrderPayCountService;

/**
 * 订单请求数记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-24
 */
@Service
public class OrderPayCountServiceImpl implements IOrderPayCountService 
{
    @Autowired
    private OrderPayCountMapper orderPayCountMapper;

    /**
     * 查询订单请求数记录
     * 
     * @param id 订单请求数记录主键
     * @return 订单请求数记录
     */
    @Override
    public OrderPayCount selectOrderPayCountById(String id)
    {
        return orderPayCountMapper.selectOrderPayCountById(id);
    }

    /**
     * 查询订单请求数记录列表
     * 
     * @param orderPayCount 订单请求数记录
     * @return 订单请求数记录
     */
    @Override
    public List<OrderPayCount> selectOrderPayCountList(OrderPayCount orderPayCount)
    {
        return orderPayCountMapper.selectOrderPayCountList(orderPayCount);
    }

    /**
     * 新增订单请求数记录
     * 
     * @param orderPayCount 订单请求数记录
     * @return 结果
     */
    @Override
    public int insertOrderPayCount(OrderPayCount orderPayCount)
    {
        return orderPayCountMapper.insertOrderPayCount(orderPayCount);
    }

    /**
     * 修改订单请求数记录
     * 
     * @param orderPayCount 订单请求数记录
     * @return 结果
     */
    @Override
    public int updateOrderPayCount(OrderPayCount orderPayCount)
    {
        return orderPayCountMapper.updateOrderPayCount(orderPayCount);
    }

    /**
     * 批量删除订单请求数记录
     * 
     * @param ids 需要删除的订单请求数记录主键
     * @return 结果
     */
    @Override
    public int deleteOrderPayCountByIds(String[] ids)
    {
        return orderPayCountMapper.deleteOrderPayCountByIds(ids);
    }

    /**
     * 删除订单请求数记录信息
     * 
     * @param id 订单请求数记录主键
     * @return 结果
     */
    @Override
    public int deleteOrderPayCountById(String id)
    {
        return orderPayCountMapper.deleteOrderPayCountById(id);
    }
}
