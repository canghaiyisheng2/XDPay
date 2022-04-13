package com.cn.xidian.fixedLength;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * 定长报文域实体
 * @date 2022/4/7 15:31
 */
@Data
@AllArgsConstructor
public class FixedLengthField {

    /**
     * 域对应实体的字段名
     */
    private String fieldName;

    /**
     * 域长度
     */
    private int fieldLength;

    /**
     * 域类型
     */
    private String fieldType;


}
