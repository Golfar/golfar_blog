package com.golfar.blog.pojo.dto.comment;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentQueryRequest implements Serializable {
    /**
     * 所属帖子 id
     */
    private Long postId;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 页号
     */
    private Integer pageNum;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 页面大小
     */
    private Integer childPageSize;

    /**
     * 页号
     */
    private Integer childPageNum;

    private static final long serialVersionUID = 1L;
}
