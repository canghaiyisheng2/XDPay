package com.cn.petshome.paymentgateway.common.util;

/**
 *
 * 关键数值常量封装
 * @date 2022/2/21 11:01
 */
public enum PaymentKeyEnum {
    /**
     * 支付方式
     */
    PAY_METHOD_CASH("0"),
    PAY_METHOD_POINT("1"),
    PAY_METHOD_COUPON("2"),


    /**
     * 交易类型
     */
    TXN_TYPE_FROZEN("0"),
    TXN_TYPE_CHK("1"),
    TXN_TYPE_UNFROZEN("2"),
    TXN_TYPE_UNFROZEN_WITH_DRAW("3"),
    TXN_TYPE_PRE_PLACE_ORDER("4");

    private final String keyCode;

    private PaymentKeyEnum(String keyCode){
        this.keyCode = keyCode;
    }

    public String getKeyCode(){
        return this.keyCode;
    }


}
