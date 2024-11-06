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

    /**
     * 添加评论
     * @param commentAddRequest
     * @param request
     * @return
     */
    CommentVO addComment(CommentAddRequest commentAddRequest, HttpServletRequest request);

    /**
     * 评论对象转换
     * @param comment
     * @return
     */
    CommentVO comment2CommentVO(Comment comment);

    /**
     * 删除评论
     * @param commentId
     * @param request
     * @return
     */
    Boolean deleteComment(Long commentId, HttpServletRequest request);

    /**
     * 分页获取评论
     * @param commentQueryRequest
     * @return
     */
    Page<CommentVO> getCommentPage(CommentQueryRequest commentQueryRequest);

    /**
     * 评论对象转换
     * @param rootCommentPage
     * @param haveChildren  是否需要拷贝子评论
     * @param childPageNum  子评论当前页码
     * @param childPageSize 子评论页大小
     * @return
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> rootCommentPage, Boolean haveChildren, Integer childPageNum, Integer childPageSize);

    /**
     * 分页获取子评论
     * @param commentChildQueryRequest
     * @return
     */
    Page<CommentVO> getChildCommentPage(CommentChildQueryRequest commentChildQueryRequest);
}
