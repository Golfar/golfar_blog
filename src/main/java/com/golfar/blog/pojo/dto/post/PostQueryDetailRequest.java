package com.golfar.blog.pojo.dto.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 查看帖子详情请求类
 */
@Data
public class PostQueryDetailRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 是否查看我的帖子，区别是否显式草稿
     * 1 是我的贴子； 2 不是我的帖子
     */
    private Boolean isMyPost;

    private static final long serialVersionUID = 1L;

}
