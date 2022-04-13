package com.cn.xidian.fixedLength;

import java.lang.annotation.*;

/**
 *
 * 定长注解
 * @author hjr
 * @date 2022/4/7 16:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FixedLength {

    /**
     * 对应域固定长度,修饰非循环字段
     */
    int value() default 0;
}
