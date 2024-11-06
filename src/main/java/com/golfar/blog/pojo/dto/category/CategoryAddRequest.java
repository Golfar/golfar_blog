package com.golfar.blog.pojo.dto.category;

import lombok.Data;

import java.io.Serializable;

/**
 * 增加类别请求类
 */
@Data
public class CategoryAddRequest implements Serializable {

    /**
     * 类别名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}
