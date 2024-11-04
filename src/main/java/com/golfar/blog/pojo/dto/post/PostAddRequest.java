package com.golfar.blog.pojo.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 文章所属分类Id
     */
    private Long categoryId;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 文章状态， 1 草稿； 0 已发布
     */
    private String isDraft;

    private static final long serialVersionUID = 1L;
}
