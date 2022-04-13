package com.cn.xidian.fixedLength;

import java.lang.annotation.*;

/**
 *
 * 注释字段可以为空
 * @author hjr
 * @date 2022/4/11 15:49
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Nullable {

    /**
     * 是否允许为空
     */
    boolean value() default true;
}
