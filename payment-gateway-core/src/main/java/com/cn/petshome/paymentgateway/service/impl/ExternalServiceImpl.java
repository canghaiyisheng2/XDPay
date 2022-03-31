package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.service.ExternalService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * 调用其他模块查询服务
 * @date 2022/1/18 17:39
 */
@Service
public class ExternalServiceImpl implements ExternalService {

    /**
     *
     * 调用积分模块冻结积分
     * @param userId 用户名
     * @return 冻结编号
     * @author hjr
     * @date 2022/1/20 14:52
     */
    @Override
    public String callPointFrozen(String userId, Long frozenPoint) {
        //调用其他模块相关功能冻结积分
        String holdNo = String.valueOf(System.currentTimeMillis());
        return holdNo;
    }

    /**
     *
     * 调用积分模块解冻积分
     * @param userId 用户ID
     * @param holdNo 冻结编号
     * @return
     * @author hjr
     * @date 2022/1/20 14:52
     */
    @Override
    @Async
    public boolean callPointUnFrozen(String userId, String holdNo){
        //调用其他模块相关功能解冻积分

        return true;
    }

    /**
     *
     * 调用积分模块解冻支取积分
     * @param userId 用户ID
     * @param holdNo 冻结编号
     * @param withDrawPoint 支取积分
     * @return
     * @author hjr
     * @date 2022/1/20 14:52
     */
    @Override
    @Async
    public boolean callPointUnFrozenWithDraw(String userId, String holdNo, Long withDrawPoint){
        //调用其他模块相关功能

        return true;
    }

    /**
     *
     * 调用代金券模块冻结代金券
     * @param userId 用户ID
     * @return String 代金券编号
     * @author hjr
     * @date 2022/1/20 14:52
     */
    @Override
    public String callCouponFrozen(String userId) {
        return null;
    }

    /**
     *
     * 调用代金券模块解冻代金券
     *
     * @param userId 用户ID
     * @return String 代金券编号
     * @author hjr
     * @date 2022/1/20 14:52
     */
    @Override
    public boolean callCouponUnFrozen(String userId) {
        return true;
    }

    /**
     *
     * 调用代金券模块解冻支取代金券
     * @param userId 用户ID
     * @return String 代金券编号
     * @author hjr
     * @date 2022/1/20 14:52
     */
    @Override
    public boolean callCouponUnFrozenWithDraw(String userId){
        return true;
    }
}
