package com.golfar.blog.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : http响应类
 * @date : 2024-10-26 23:24
 **/
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode code){
        this(code.getCode(), null, code.getMessage());
    }
}
