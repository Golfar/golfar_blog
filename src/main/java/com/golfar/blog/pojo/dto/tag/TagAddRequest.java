package com.golfar.blog.pojo.dto.tag;

import lombok.Data;

import java.io.Serializable;

/**
 * 增加标签请求类
 */
@Data
public class TagAddRequest implements Serializable {

    /**
     * 类别名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}
