package com.golfar.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.common.ResultUtils;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.comment.CommentAddRequest;
import com.golfar.blog.pojo.dto.comment.CommentChildQueryRequest;
import com.golfar.blog.pojo.dto.comment.CommentQueryRequest;
import com.golfar.blog.pojo.entity.Comment;
import com.golfar.blog.pojo.entity.CommentThumb;
import com.golfar.blog.pojo.vo.comment.CommentVO;
import com.golfar.blog.service.CommentService;
import com.golfar.blog.service.CommentThumbService;
import com.golfar.blog.service.PostService;
import com.golfar.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 评论相关接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private UserService userService;
    @Resource
    private CommentThumbService commentThumbService;

    @PostMapping("/add")
    public BaseResponse<CommentVO> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request){
        // 参数校验
        ThrowUtils.throwIf(commentAddRequest == null || request == null, ErrorCode.PARAMS_ERROR, "新增评论参数错误或连接异常");
        CommentVO commentVO = commentService.addComment(commentAddRequest, request);
        return ResultUtils.success(commentVO);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteComment(Long commentId, HttpServletRequest request){
        // TODO 还要删除帖子点赞的信息
        ThrowUtils.throwIf(commentId == null || request == null, ErrorCode.PARAMS_ERROR, "新增评论参数错误或连接异常");
        Boolean result = commentService.deleteComment(commentId, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/get/page")
    public BaseResponse<Page<CommentVO>> getCommentPage(@RequestBody CommentQueryRequest commentQueryRequest){
        ThrowUtils.throwIf(commentQueryRequest == null, ErrorCode.PARAMS_ERROR, "新增评论参数错误或连接异常");
        Page<CommentVO> commentVOPage = commentService.getCommentPage(commentQueryRequest);
        return ResultUtils.success(commentVOPage);
    }

    @PostMapping("/get/page/child")
    public BaseResponse<Page<CommentVO>> getChildCommentPage(@RequestBody CommentChildQueryRequest commentChildQueryRequest){
        ThrowUtils.throwIf(commentChildQueryRequest == null, ErrorCode.PARAMS_ERROR, "新增评论参数错误或连接异常");
        Page<CommentVO> commentVOPage = commentService.getChildCommentPage(commentChildQueryRequest);
        return ResultUtils.success(commentVOPage);
    }

    @GetMapping("/thumb")
    public BaseResponse<Boolean> thumbComment(Long commentId, HttpServletRequest request){
        // TODO 加缓存，加事务
        // 参数校验
        ThrowUtils.throwIf(commentId == null || request == null, ErrorCode.PARAMS_ERROR, "新增评论参数错误或连接异常");
        Long loginUserId = userService.getLoginUser(request).getId();
        Comment comment = commentService.getById(commentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.PARAMS_ERROR, "评论不存在");
        // 判断是否已经点赞
        LambdaQueryWrapper<CommentThumb> commentThumbWrapper = Wrappers.lambdaQuery(CommentThumb.class)
                .eq(CommentThumb::getCommentId, commentId)
                .eq(CommentThumb::getUserId, loginUserId);
        long commentThumbCount = commentThumbService.count(commentThumbWrapper);
        ThrowUtils.throwIf(commentThumbCount == 1, ErrorCode.OPERATION_ERROR, "您已经点过赞了");
        // 插入点赞信息
        CommentThumb commentThumb = new CommentThumb();
        commentThumb.setCommentId(commentId);
        commentThumb.setUserId(loginUserId);
        boolean saved = commentThumbService.save(commentThumb);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "点赞失败");
        // 点赞数加1
        comment.setThumbNum(comment.getThumbNum() + 1);
        boolean updated = commentService.updateById(comment);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "点赞失败");
        return ResultUtils.success(true);
    }
}
