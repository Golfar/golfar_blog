package com.golfar.blog.pojo.vo.tag;

import lombok.Data;

import java.io.Serializable;

/**
 * 标签响应类
 */
@Data
public class TagVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 类别名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}
