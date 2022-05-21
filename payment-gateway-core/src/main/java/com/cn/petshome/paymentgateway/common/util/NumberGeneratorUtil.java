package com.cn.petshome.paymentgateway.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @date 2022/3/31 18:59
 */
public class NumberGeneratorUtil {
    private static String getRandomNumber(int n) {
        String randomString = "";

        for(Random random = new Random(); randomString.length() < n; randomString = randomString + random.nextInt(10)) {
        }

        return randomString;
    }

    private static String getTimeString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public static String getPayOrderNumbere() {
        return "PAYORDER_" + getRandomNumber(3) + getTimeString() + getRandomNumber(3);
    }

    public static String getTxnNumbere() {
        return "Txn_" + getRandomNumber(3) + getTimeString() + getRandomNumber(3);
    }

    public static String getPointNumbere() {
        return "P_" + getTimeString() + getRandomNumber(4);
    }

    public static String getCouponNumbere() {
        return "C_" + getTimeString() + getRandomNumber(4);
    }

}
