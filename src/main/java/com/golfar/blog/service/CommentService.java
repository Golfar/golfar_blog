package com.golfar.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.pojo.dto.comment.CommentAddRequest;
import com.golfar.blog.pojo.dto.comment.CommentChildQueryRequest;
import com.golfar.blog.pojo.dto.comment.CommentQueryRequest;
import com.golfar.blog.pojo.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.golfar.blog.pojo.vo.comment.CommentVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Loong
* @description 针对表【comment(帖子评论)】的数据库操作Service
* @createDate 2024-10-27 15:05:09
*/
public interface CommentService extends IService<Comment> {

    CommentVO addComment(CommentAddRequest commentAddRequest, HttpServletRequest request);

    CommentVO comment2CommentVO(Comment comment);

    Boolean deleteComment(Long commentId, HttpServletRequest request);

    Page<CommentVO> getCommentPage(CommentQueryRequest commentQueryRequest);

    Page<CommentVO> getCommentVOPage(Page<Comment> rootCommentPage, Boolean haveChildren, Integer childPageNum, Integer childPageSize);

    Page<CommentVO> getChildCommentPage(CommentChildQueryRequest commentChildQueryRequest);
}
