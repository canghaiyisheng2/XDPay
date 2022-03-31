package com.cn.petshome.paymentgateway.mapper;

import com.cn.petshome.paymentgateway.po.PayTxnJnlDO;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * 支付流水持久层
 * @date 2022/2/21 14:39
 */
@Mapper
public interface PayTxnJnlMapper {

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
    int insert(PayTxnJnlDO record);

    /**
     *
     * 插入记录（忽略空字段）
     * @param record 待插入记录
     * @return 插入数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int insertSelective(PayTxnJnlDO record);

    /**
     *
     * 根据主键查询记录
     * @param id id
     * @return 查询到的记录
     * @author hjr
     * @date 2022/2/21 14:15
     */
    PayTxnJnlDO selectByPrimaryKey(Long id);

    /**
     *
     * 根据主键更新记录（忽略空字段）
     * @param record 待更新记录
     * @return 更新记录数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int updateByPrimaryKeySelective(PayTxnJnlDO record);

    /**
     *
     * 根据主键更新记录
     * @param record 待更新记录
     * @return 更新记录数
     * @author hjr
     * @date 2022/2/21 14:15
     */
    int updateByPrimaryKey(PayTxnJnlDO record);

    /**
     *
     * 根据request_txn_jnl更新字段response_txn_jnl,status
     * @param record 更新记录
     * @return 更新记录数
     * @author hjr
     * @date 2022/1/20 15:09
     */
    int updateByRequestTxnJnl(PayTxnJnlDO record);
}