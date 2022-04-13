package com.cn.xidian.fixedLength;

/**
 * @date 2022/4/7 16:33
 */
public class FixedLengthResolveException extends RuntimeException{
    public FixedLengthResolveException(){
        super();
    }

    public FixedLengthResolveException(String message){
        super(message);
    }

    public FixedLengthResolveException(String message, Throwable cause){
        super(message, cause);
    }
}