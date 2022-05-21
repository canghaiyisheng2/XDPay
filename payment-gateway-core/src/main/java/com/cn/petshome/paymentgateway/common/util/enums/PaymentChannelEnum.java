package com.cn.petshome.paymentgateway.common.util.enums;

/**
 * @date 2022/3/21 15:54
 */
public enum PaymentChannelEnum {

    /**
     * 支付渠道
     */
    CHANNEL_TYPE_WEIXINPAY("0"),
    CHANNEL_TYPE_ALIPAY("1"),
    CHANNEL_TYPE_UNIONPAY("2");

    private final String code;

    private PaymentChannelEnum(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    /**
     *
     * 解决switch-case下无法使用枚举常量的问题
     * @param keyCode keyCode
     * @return PaymentKey枚举类
     * @author hjr
     * @date 2022/3/20 15:05
     */
    public static PaymentChannelEnum getPaymentChannelByCode(String keyCode){
        for (PaymentChannelEnum paymentChannelEnum : PaymentChannelEnum.values()){
            if (paymentChannelEnum.getCode().equals(keyCode)){
                return paymentChannelEnum;
            }
        }
        return null;
    }
}
