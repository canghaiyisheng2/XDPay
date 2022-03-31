package com.cn.petshome.paymentgateway.common.util;

/**
 *
 * 订单相关状态常量封装
 * @date 2022/1/27 13:43
 */
public enum StatusEnum {
    /**
     * 支付方式状态
     */
    PAY_METHOD_STATUS_INITIAL("0"),
    PAY_METHOD_STATUS_FROZEN("1"),
    PAY_METHOD_STATUS_PAID("2"),
    PAY_METHOD_STATUS_FAIL("3"),

    /**
     * 支付流水状态
     */
    PAY_TXN_JNL_STATUS_SUCCESS("S"),
    PAY_TXN_JNL_STATUS_FAIL("F"),
    PAY_TXN_JNL_STATUS_TIMEOUT("U"),

    /**
     * 支付订单状态
     */
    PAY_ORDER_STATUS_INITIAL("0"),
    PAY_ORDER_STATUS_PLACED("1"),
    PAY_ORDER_STATUS_PAID("2"),

    /**
     * 对账状态
     */
    CHK_STATUS("0");

    private String code;

    private StatusEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
