package com.golfar.blog.pojo.dto.tag;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagUpdateRequest implements Serializable {
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
