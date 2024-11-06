package com.golfar.blog.pojo.dto.tag;

import lombok.Data;

import java.io.Serializable;

/**
 * 查找标签请求类
 */
@Data
public class TagQueryRequest implements Serializable {

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 页号
     */
    private Integer pageNum;

    private static final long serialVersionUID = 1L;
}
