package com.golfar.blog.pojo.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 增加评论请求类
 */
@Data
public class CommentAddRequest implements Serializable {

    /**
     * 所属帖子 id
     */
    private Long postId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论 id, -1为根评论
     */
    private Long rootCommentId;

    /**
     * 回复目标评论 id
     */
    private Long toCommentId;

    /**
     * 回复目标评论用户 id
     */
    private Long toUserId;

    private static final long serialVersionUID = 1L;
}
