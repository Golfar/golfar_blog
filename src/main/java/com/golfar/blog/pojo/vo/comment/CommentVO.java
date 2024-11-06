package com.golfar.blog.pojo.vo.comment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 所属帖子 id
     */
    private Long postId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 回复目标评论 id
     */
    private Long toCommentId;

    /**
     * 回复目标评论用户 id
     */
    private Long toUserId;

    /**
     * 回复目标评论用户名称
     */
    private String toUserName;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建用户 名称
     */
    private String userName;

    /**
     * 根评论的所有子评论
     */
    private Page<CommentVO> children;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
