package com.cn.xidian.fixedLength;

import lombok.Data;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 定长报文解析器主体
 * @date 2022/4/7 15:29
 */
@Data
public class Resolver<T> {

    /**
     *
     * 报文解析模板规则
     */
    List<FixedLengthPiece> resolveRules = new ArrayList<>();
    Class<T> clazz;

    public Resolver(Class<T> clazz) {
        resolveRules = FixedLengthUtil.resolveClass(clazz);
        this.clazz = clazz;
    }

    /**
     *
     * 解析定长报文字符串
     * @param fixedLengthString 定长报文
     * @return 解析后的实体
     * @author hjr
     * @date 2022/4/8 11:33
     */
    public T parse(String fixedLengthString) throws FixedLengthResolveException {
        FixedLengthUtil.index = 0;
        return FixedLengthUtil.parseToObject(fixedLengthString, resolveRules, this.clazz);
    }

    /**
     *
     * 转换对应实体为定长报文
     * @param fixedLengthObject 待转换实体
     * @return 转换后的定长报文字符串
     * @author hjr
     * @date 2022/4/8 11:33
     */
    public String stringify(Object fixedLengthObject) throws FixedLengthResolveException{
        StringBuilder sb = new StringBuilder();
        for (FixedLengthPiece piece : resolveRules){
            String pieceString = FixedLengthUtil.stringifyPieceInObject(piece, fixedLengthObject);
            sb.append(pieceString);
        }
        return sb.toString();
    }
}
