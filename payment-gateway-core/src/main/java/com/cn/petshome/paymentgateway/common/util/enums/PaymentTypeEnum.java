package com.cn.petshome.paymentgateway.common.util.enums;

/**
 * 支付方式封装
 * @date 2022/5/20 16:34
 */
public enum PaymentTypeEnum {
    /**
     * 交易方式
     */
    PAY_TYPE_PC("0"),
    PAY_TYPE_MOBILE("1"),
    PAY_TYPE_NATIVE("2");

    private final String keyCode;

    private PaymentTypeEnum(String keyCode){
        this.keyCode = keyCode;
    }

    public String getKeyCode(){
        return this.keyCode;
    }

    /**
     *
     * 解决switch-case下无法使用枚举常量的问题
     * @param keyCode keyCode
     * @return PaymentType枚举类
     * @author hjr
     * @date 2022/3/20 15:05
     */
    public static PaymentTypeEnum getPaymentTypeByCode(String keyCode){
        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()){
            if (paymentTypeEnum.getKeyCode().equals(keyCode)){
                return paymentTypeEnum;
            }
        }
        return null;
    }
}
