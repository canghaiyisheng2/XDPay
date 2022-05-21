package com.cn.petshome.paymentgateway.controller;

import com.cn.petshome.paymentgateway.common.config.CommonOption;
import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.common.util.enums.PaymentChannelEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 系统设置控制器
 * @date 2022/5/21 13:08
 */
@RestController
@RequestMapping("/common")
public class CommonOptionController {

    /**
     *
     * 返回当前积分开关值
     * @return 当前积分开关值
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/isUsePoint", method = RequestMethod.GET)
    public ResponseBean<Boolean> isUsePoint() {
        return ResponseBean.buildSuccess(CommonOption.usePoint);
    }

    /**
     *
     * 修改当前积分开关值
     * @return 修改后积分开关值
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/togglePoint", method = RequestMethod.GET)
    public ResponseBean<Boolean> togglePoint() {
        CommonOption.usePoint = !CommonOption.usePoint;
        return ResponseBean.buildSuccess(CommonOption.usePoint);
    }

    /**
     *
     * 返回当前代金券开关值
     * @return 当前代金券开关值
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/isUseCoupon", method = RequestMethod.GET)
    public ResponseBean<Boolean> isUseCoupon() {
        return ResponseBean.buildSuccess(CommonOption.useCoupon);
    }

    /**
     *
     * 修改当前代金券开关值
     * @return 修改后代金券开关值
     * @author hjr
     * @date 2022/3/10 10:17
     */
    @RequestMapping(value = "/toggleCoupon", method = RequestMethod.GET)
    public ResponseBean<Boolean> toggleCoupon() {
        CommonOption.useCoupon = !CommonOption.useCoupon;
        return ResponseBean.buildSuccess(CommonOption.useCoupon);
    }
}
