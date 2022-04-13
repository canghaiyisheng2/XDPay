package com.cn.petshome.paymentgateway.mapper;

import com.cn.petshome.paymentgateway.po.OrderPayMethodPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * 支付方式持久层
 * @date 2022/2/21 14:39
 */
@Mapper
public interface OrderPayMethodMapper {
    /**
     *
     * 根据id删除记录
     * @param id id
     * @return 删除数
     * @author hjr
     * @date 2022/2/21 11:30
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * 插入单条记录
     * @param record 支付方式记录
     * @return 插入数
     * @author hjr
     * @date 2022/2/21 11:30
     */
    int insert(OrderPayMethodPO record);

    /**
     *
     * 插入单条记录（空字段忽略）
     * @param record 支付方式记录
     * @return 插入数
     * @author hjr
     * @date 2022/2/21 11:30
     */
    int insertSelective(OrderPayMethodPO record);

    /**
     *
     * 批量插入OrderPayMethod
     * @param records 插入记录
     * @return int 插入记录数
     * @author hjr
     * @date 2022/1/20 13:10
     */
    int insertBatch(List<OrderPayMethodPO> records);

    /**
     *
     * 查询记录
     * @param id id
     * @return 查询到的记录
     * @author hjr
     * @date 2022/2/21 11:30
     */
    OrderPayMethodPO selectByPrimaryKey(Long id);

    /**
     *
     * 根据订单号查询支付方式
     * @param payOrderId 订单号
     * @return {@link List<  OrderPayMethodPO  >}
     * @author hjr
     * @date 2022/1/21 15:10
     */
    List<OrderPayMethodPO> selectByPayOrderId(String payOrderId);

    /**
     *
     * 查询订单支付方式
     * @param payOrderId 订单号
     * @param payMethod 支付方式
     * @return {@link OrderPayMethodPO} 支付方式
     * @author hjr
     * @date 2022/1/21 15:08
     */
    OrderPayMethodPO selectByPayOrderIdAndPayMethod(String payOrderId, String payMethod);

    /**
     *
     * 根据记录主键更新（空字段忽略）
     * @param record 支付方式记录
     * @return 更新成功数
     * @author hjr
     * @date 2022/2/21 11:30
     */
    int updateByPrimaryKeySelective(OrderPayMethodPO record);

    /**
     *
     * 根据记录主键更新
     * @param record 支付方式记录
     * @return 更新成功数
     * @author hjr
     * @date 2022/2/21 11:30
     */
    int updateByPrimaryKey(OrderPayMethodPO record);

    /**
     *
     * 根据订单号更新状态
     * @param record 更新支付方式表
     * @return int 更新记录数
     * @author hjr
     * @date 2022/1/20 15:32
     */
    int updateByPayOrderIdAndPayMethod(OrderPayMethodPO record);

    /**
     *
     * 根据订单号更新现金支付方式状态
     * @param payOrderId 订单号
     * @param status 更新状态
     * @return 更新记录数
     * @author hjr
     * @date 2022/1/21 14:48
     */
    int updateCashStatusByPayOrderId(String payOrderId, String status);
}