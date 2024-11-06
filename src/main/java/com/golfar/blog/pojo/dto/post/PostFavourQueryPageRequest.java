package com.golfar.blog.pojo.dto.post;

import lombok.Data;

import java.io.Serializable;

/**
 * 查看收藏的帖子请求类
 */
@Data
public class PostFavourQueryPageRequest implements Serializable {
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
