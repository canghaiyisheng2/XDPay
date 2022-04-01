package com.ruoyi.payOrderCount.mapper;

import java.util.List;
import com.ruoyi.payOrderCount.domain.OrderPayCount;

/**
 * 订单请求数记录Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-24
 */
public interface OrderPayCountMapper 
{
    /**
     * 查询订单请求数记录
     * 
     * @param id 订单请求数记录主键
     * @return 订单请求数记录
     */
    public OrderPayCount selectOrderPayCountById(String id);

    /**
     * 查询订单请求数记录列表
     * 
     * @param orderPayCount 订单请求数记录
     * @return 订单请求数记录集合
     */
    public List<OrderPayCount> selectOrderPayCountList(OrderPayCount orderPayCount);

    /**
     * 新增订单请求数记录
     * 
     * @param orderPayCount 订单请求数记录
     * @return 结果
     */
    public int insertOrderPayCount(OrderPayCount orderPayCount);

    /**
     * 修改订单请求数记录
     * 
     * @param orderPayCount 订单请求数记录
     * @return 结果
     */
    public int updateOrderPayCount(OrderPayCount orderPayCount);

    /**
     * 删除订单请求数记录
     * 
     * @param id 订单请求数记录主键
     * @return 结果
     */
    public int deleteOrderPayCountById(String id);

    /**
     * 批量删除订单请求数记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderPayCountByIds(String[] ids);
}
