package com.golfar.blog.exception;


import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 业务代码异常，自定义异常类
 * @date : 2024-10-26 23:16
 **/
public class BusinessException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message){
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code =errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message){
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
