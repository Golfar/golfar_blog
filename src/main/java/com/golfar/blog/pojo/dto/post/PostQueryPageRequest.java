package com.golfar.blog.pojo.dto.post;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostQueryPageRequest implements Serializable {
    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 页号
     */
    private Integer pageNum;

    /**
     * 是否查看我的帖子
     * 1 是我的贴子； 2 不是我的帖子
     */
    private Boolean isMyPost;

    private static final long serialVersionUID = 1L;
}
