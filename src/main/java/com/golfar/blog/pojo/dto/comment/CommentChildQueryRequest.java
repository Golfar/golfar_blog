package com.golfar.blog.pojo.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询子评论请求类
 */
@Data
public class CommentChildQueryRequest implements Serializable {

    /**
     * 所属根评论 id
     */
    private Long rootCommentId;

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
