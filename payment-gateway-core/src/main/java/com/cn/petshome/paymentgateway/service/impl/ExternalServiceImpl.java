package com.cn.petshome.paymentgateway.service.impl;

import com.cn.petshome.paymentgateway.common.config.CommonOption;
import com.cn.petshome.paymentgateway.common.exception.ExternalException;
import com.cn.petshome.paymentgateway.common.util.NumberGeneratorUtil;
import com.cn.petshome.paymentgateway.service.ExternalService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
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
     * @date 2022/1/20 14:52
     */
    @Override
    public String callPointFrozen(String userId, Long frozenPoint) {
        if(!CommonOption.usePoint){
            throw new ExternalException("积分功能已禁用");
        }

        /*
            添加对应功能代码调用其他模块
         */
        return NumberGeneratorUtil.getPointNumbere();
    }

    /**
     *
     * 调用积分模块解冻积分
     * @param userId 用户ID
     * @param holdNo 冻结编号
     * @return 操作成功与否
     * @date 2022/1/20 14:52
     */
    @Override
    @Async
    public boolean callPointUnFrozen(String userId, String holdNo){
        if(!CommonOption.usePoint){
            throw new ExternalException("积分功能已禁用");
        }

        /*
            添加对应功能代码调用其他模块
         */

        return true;
    }

    /**
     *
     * 调用积分模块解冻支取积分
     * @param userId 用户ID
     * @param holdNo 冻结编号
     * @return 操作成功与否
     * @date 2022/1/20 14:52
     */
    @Override
    @Async
    public boolean callPointUnFrozenWithDraw(String userId, String holdNo){
        if(!CommonOption.usePoint){
            throw new ExternalException("积分功能已禁用");
        }

        /*
            添加对应功能代码调用其他模块
         */

        return true;
    }

    /**
     *
     * 调用代金券模块冻结代金券
     * @param userId 用户ID
     * @param holdNo 代金券编号
     * @return 操作成功与否
     * @date 2022/1/20 14:52
     */
    @Override
    public String callCouponFrozen(String userId, String holdNo) {
        if(!CommonOption.useCoupon){
            throw new ExternalException("代金券功能已禁用");
        }

        /*
            添加对应功能代码调用其他模块
         */

        return NumberGeneratorUtil.getCouponNumbere();

    }

    /**
     *
     * 调用代金券模块解冻代金券
     *
     * @param userId 用户ID
     * @param holdNo 代金券编号
     * @return 操作成功与否
     * @date 2022/1/20 14:52
     */
    @Override
    public boolean callCouponUnFrozen(String userId, String holdNo) {
        if(!CommonOption.useCoupon){
            throw new ExternalException("代金券功能已禁用");
        }

        /*
            添加对应功能代码调用其他模块
         */

        return true;
    }

    /**
     *
     * 调用代金券模块解冻支取代金券
     * @param userId 用户ID
     * @param holdNo 代金券编号
     * @return 操作成功与否
     * @date 2022/1/20 14:52
     */
    @Override
    public boolean callCouponUnFrozenWithDraw(String userId, String holdNo){
        if(!CommonOption.useCoupon){
            throw new ExternalException("代金券功能已禁用");
        }

        /*
            添加对应功能代码调用其他模块
         */

        return true;
    }
}
