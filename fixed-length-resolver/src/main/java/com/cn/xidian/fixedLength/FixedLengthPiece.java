package com.cn.xidian.fixedLength;

import lombok.Data;

import java.util.List;

/**
 *
 * 定长报文片段（以循环与否作为区分）
 * @date 2022/4/7 15:52
 */
@Data
public class FixedLengthPiece {

    /**
     * 是否为循环片段
     */
    private boolean isCycle;

    /**
     * 非循环片段时的片段主体
     */
    private FixedLengthField singlePiece;

    /**
     * 循环片段时的循环字段名
     */
    private String cycleFieldName;

    /**
     * 循环片段时的循环字段域
     */
    private List<FixedLengthPiece> cycleField;

}
