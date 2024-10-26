package com.golfar.blog.exception;

import com.golfar.blog.common.ErrorCode;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 抛出异常工具类
 * @date : 2024-10-26 23:46
 **/
public class ThrowUtils {

    /**
     * 条件成立抛出运行时异常
     * @param condition
     * @param e
     */
    public static void throwIf(boolean condition, RuntimeException e){
        if(condition){
            throw e;
        }
    }

    /**
     * 抛出业务异常，默认异常信息
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode){
        if(condition){
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 抛出业务异常，自定义异常信息
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message){
        if(condition){
            throw new BusinessException(errorCode, message);
        }
    }

    /**
     * 抛出业务异常，自定义错误码和异常信息
     * @param condition
     * @param code
     * @param message
     */
    public static void throwIf(boolean condition, int code, String message){
        if(condition){
            throw new BusinessException(code, message);
        }
    }
}
