package com.ruoyi.payMethod.mapper;

import java.util.List;
import com.ruoyi.payMethod.domain.OrderPayMethod;

/**
 * 订单支付方式信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-26
 */
public interface OrderPayMethodMapper 
{
    /**
     * 查询订单支付方式信息
     * 
     * @param id 订单支付方式信息主键
     * @return 订单支付方式信息
     */
    public OrderPayMethod selectOrderPayMethodById(String id);

    /**
     * 查询订单支付方式信息列表
     * 
     * @param orderPayMethod 订单支付方式信息
     * @return 订单支付方式信息集合
     */
    public List<OrderPayMethod> selectOrderPayMethodList(OrderPayMethod orderPayMethod);

    /**
     * 新增订单支付方式信息
     * 
     * @param orderPayMethod 订单支付方式信息
     * @return 结果
     */
    public int insertOrderPayMethod(OrderPayMethod orderPayMethod);

    /**
     * 修改订单支付方式信息
     * 
     * @param orderPayMethod 订单支付方式信息
     * @return 结果
     */
    public int updateOrderPayMethod(OrderPayMethod orderPayMethod);

    /**
     * 删除订单支付方式信息
     * 
     * @param id 订单支付方式信息主键
     * @return 结果
     */
    public int deleteOrderPayMethodById(String id);

    /**
     * 批量删除订单支付方式信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderPayMethodByIds(String[] ids);
}
