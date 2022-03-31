package com.cn.petshome.paymentgateway.mapper;

import com.cn.petshome.paymentgateway.po.PayOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * 支付订单持久层
 * @date 2022/2/21 14:39
 */
@Mapper
public interface PayOrderMapper {
    /**
     *
     * 根据主键删除记录
     * @param id id
     * @return 删除数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * 插入记录
     * @param record 待插入记录
     * @return 插入数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int insert(PayOrderDO record);

    /**
     *
     * 插入记录（忽略空字段）
     * @param record 待插入记录
     * @return 插入数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int insertSelective(PayOrderDO record);

    /**
     *
     * 根据主键查询记录
     * @param id id
     * @return 查询到的记录
     * @author hjr
     * @date 2022/2/21 14:15
     */
    PayOrderDO selectByPrimaryKey(Long id);

    /**
     *
     * 根据订单号查询订单
     * @param payOrderId 订单号
     * @return {@link PayOrderDO} 查询到的记录
     * @author hjr
     * @date 2022/1/21 14:02
     */
    PayOrderDO selectByPayOrderId(String payOrderId);

    /**
     *
     * 根据主键更新记录（忽略空字段）
     * @param record 待更新记录
     * @return 更新记录数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int updateByPrimaryKeySelective(PayOrderDO record);


    /**
     *
     * 根据主键更新记录
     * @param record 待更新记录
     * @return 更新记录数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int updateByPrimaryKey(PayOrderDO record);

    /**
     *
     * 根据订单号更新
     * @param record 更新订单
     * @return 更新记录数
     * @author hjr
     * @date 2022/1/21 14:37
     */
    int updateByPayOrderId(PayOrderDO record);
}