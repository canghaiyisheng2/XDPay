package com.cn.xidian.fixedLength;

import java.lang.annotation.*;

/**
 *
 * 解析定长报文时忽略字段
 * @author hjr
 * @date 2022/4/7 19:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ResolveIgnore {

    boolean value() default true;
}
