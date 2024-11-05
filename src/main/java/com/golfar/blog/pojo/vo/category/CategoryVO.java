package com.golfar.blog.pojo.vo.category;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryVO implements Serializable {
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
