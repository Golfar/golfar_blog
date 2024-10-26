package com.golfar.blog.exception;

import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 全局异常处理器
 * @date : 2024-10-26 23:37
 **/
@RestControllerAdvice   //这个类可以为所有的 @RequestMapping 处理方法，提供通用的异常处理和数据绑定等增强功能；一旦controller层抛出异常，就会触发
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException exception){
        log.error("Business Exception : ", exception);
        return ResultUtils.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException exception){
        log.error("Runtime Exception : ", exception);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
