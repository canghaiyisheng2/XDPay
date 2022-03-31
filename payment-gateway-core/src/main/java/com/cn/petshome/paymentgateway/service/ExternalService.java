package com.cn.petshome.paymentgateway.service;

import java.util.List;

/**
 *
 * 调用其他模块查询服务
 * @date 2022/1/18 17:39
 */
public interface ExternalService {
    /**
     *
     * 调用积分模块冻结积分
     * @param userId 用户ID
     * @param frozenPoint 冻结积分
     * @return String 冻结编号
     * @author hjr
     * @date 2022/1/18 17:41
     */
    public String callPointFrozen(String userId, Long frozenPoint);

    /**
     *
     * 调用积分模块解冻积分
     * @param userId 用户ID
     * @param holdNo 冻结编号
     * @return
     * @author hjr
     * @date 2022/1/18 17:41
     */
    public boolean callPointUnFrozen(String userId, String holdNo);

    /**
     *
     * 调用积分模块解冻支取积分
     * @param userId 用户ID
     * @param holdNo 冻结编号
     * @param withDrawPoint 支取积分
     * @return
     * @author hjr
     * @date 2022/1/18 17:41
     */
    public boolean callPointUnFrozenWithDraw(String userId, String holdNo, Long withDrawPoint);

    /**
     *
     * 调用代金券模块冻结代金券
     * @param userId 用户ID
     * @return String 代金券编号
     * @author hjr
     * @date 2022/1/18 17:42
     */
    public String callCouponFrozen(String userId);

    /**
     *
     * 调用代金券模块解冻代金券
     * @param userId 用户ID
     * @return String 代金券编号
     * @author hjr
     * @date 2022/1/18 17:42
     */
    public boolean callCouponUnFrozen(String userId);

    /**
     *
     * 调用代金券模块解冻支取代金券
     * @param userId 用户ID
     * @return String 代金券编号
     * @author hjr
     * @date 2022/1/18 17:42
     */
    public boolean callCouponUnFrozenWithDraw(String userId);
}
