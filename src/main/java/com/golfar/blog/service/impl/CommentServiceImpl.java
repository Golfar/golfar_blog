package com.golfar.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.constant.CommentConstant;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.comment.CommentAddRequest;
import com.golfar.blog.pojo.dto.comment.CommentChildQueryRequest;
import com.golfar.blog.pojo.dto.comment.CommentQueryRequest;
import com.golfar.blog.pojo.entity.Category;
import com.golfar.blog.pojo.entity.Comment;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.pojo.entity.User;
import com.golfar.blog.pojo.vo.comment.CommentVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.CommentService;
import com.golfar.blog.mapper.CommentMapper;
import com.golfar.blog.service.PostService;
import com.golfar.blog.service.UserService;
import com.golfar.blog.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Loong
* @description 针对表【comment(帖子评论)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    @Override
    public CommentVO addComment(CommentAddRequest commentAddRequest, HttpServletRequest request) {
        // 参数校验
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Long postId = commentAddRequest.getPostId();
        String content = commentAddRequest.getContent();
        Long rootCommentId = commentAddRequest.getRootCommentId();
        Long toCommentId = commentAddRequest.getToCommentId();
        Long toUserId = commentAddRequest.getToUserId();
        ThrowUtils.throwIf(postId == null || rootCommentId == null || toCommentId == null || toUserId == null, ErrorCode.PARAMS_ERROR, "参数错误");
        ThrowUtils.throwIf(StringUtils.isBlank(content), ErrorCode.PARAMS_ERROR, "评论内容为空");
        // postId 是否存在
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR, "评论的帖子不存在");
        // toCommentId 和 rootCommentId 是否存在
        List<Long> commentIdList = new ArrayList<>();
        commentIdList.add(toCommentId);
        if(!rootCommentId.equals(CommentConstant.ROOT_COMMENT)){
            commentIdList.add(rootCommentId);
        }
        LambdaQueryWrapper<Comment> commentWrapper = Wrappers.lambdaQuery(Comment.class)
                .in(Comment::getId, commentIdList);
        List<Comment> commnetList = this.list(commentWrapper);
        ThrowUtils.throwIf(commnetList == null, ErrorCode.PARAMS_ERROR, "回复的评论不存在");
        ThrowUtils.throwIf(commnetList.size() != commentIdList.size(), ErrorCode.PARAMS_ERROR, "回复的评论不存在");
        // 检查 toUserId 和 toCommentId 是否匹配
        commentWrapper = Wrappers.lambdaQuery(Comment.class)
                .eq(Comment::getId, toCommentId)
                .eq(Comment::getUserId, toUserId);
        Comment commentCheck = this.getOne(commentWrapper);
        ThrowUtils.throwIf(commentCheck == null, ErrorCode.PARAMS_ERROR, "回复用户id和回复评论id不匹配");
        // 插入评论
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setRootCommentId(rootCommentId);
        comment.setToCommentId(toCommentId);
        comment.setUserId(loginUserId);
        comment.setToUserId(toUserId);
        this.save(comment);
        return this.comment2CommentVO(comment);
    }

    @Override
    public CommentVO comment2CommentVO(Comment comment){
        CommentVO commentVO = BeanCopyUtils.copy(comment, CommentVO.class);
        // 获取回复的评论用户名称
        LambdaQueryWrapper<User> toUserWrapper = Wrappers.lambdaQuery(User.class).select(User::getUserName)
                .eq(User::getId, comment.getToUserId());
        String toUserName = userService.getObj(toUserWrapper, obj -> (String) obj);
        // 获取创建评论用户名称
        LambdaQueryWrapper<User> userWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getId, comment.getUserId());
        String userName = userService.getObj(userWrapper, obj -> (String) obj);
        // 复制封装类
        commentVO.setToUserName(toUserName);
        commentVO.setUserName(userName);
        return commentVO;
    }

    @Override
    public Boolean deleteComment(Long commentId, HttpServletRequest request) {
        // TODO 开启事务
        // 参数校验
        Long loginUserId = userService.getLoginUser(request).getId();
        // 评论是否存在
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.PARAMS_ERROR, "评论不存在");
        // 评论创建 id 和删除 id 是否一致
        ThrowUtils.throwIf(loginUserId.equals(comment.getUserId()), ErrorCode.NO_AUTH_ERROR, "无权删除评论");
        boolean removed = this.removeById(commentId);
        ThrowUtils.throwIf(!removed, ErrorCode.OPERATION_ERROR, "删除失败");
        // 判断是否为根评论，若为根评论，要将他的子评论也删除
        Long rootCommentId = comment.getRootCommentId();
        if(rootCommentId != -1){
            return true;
        }
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery(Comment.class)
                .eq(Comment::getRootCommentId, commentId);
        removed = this.remove(wrapper);
        ThrowUtils.throwIf(!removed, ErrorCode.OPERATION_ERROR, "删除失败");
        return true;
    }

    @Override
    public Page<CommentVO> getCommentPage(CommentQueryRequest commentQueryRequest) {
        // 参数校验
        Long postId = commentQueryRequest.getPostId();
        Integer pageSize = commentQueryRequest.getPageSize();
        Integer pageNum = commentQueryRequest.getPageNum();
        Integer childPageNum = commentQueryRequest.getChildPageNum();
        Integer childPageSize = commentQueryRequest.getChildPageSize();
        ThrowUtils.throwIf(postId == null, ErrorCode.PARAMS_ERROR, "请求的帖子不存在");
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        ThrowUtils.throwIf(childPageNum == null || childPageNum <= 0 || childPageSize == null || childPageSize <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR, "请求的帖子不存在");
        // 查询所有根评论
        LambdaQueryWrapper<Comment> rootCommentWrapper = Wrappers.lambdaQuery(Comment.class)
                .eq(Comment::getPostId, postId)
                .eq(Comment::getRootCommentId, CommentConstant.ROOT_COMMENT)
                .orderByDesc(Comment::getCreateTime);
        Page<Comment> rootCommentPage = this.page(new Page<>(pageNum, pageSize), rootCommentWrapper);
        return this.getCommentVOPage(rootCommentPage, true, childPageNum, childPageSize);
    }

    @Override
    public Page<CommentVO> getCommentVOPage(Page<Comment> rootCommentPage, Boolean haveChildren, Integer childPageNum, Integer childPageSize) {
        List<Comment> rootCommentList = rootCommentPage.getRecords();
        Page<CommentVO> rootCommentVOPage = new Page<>(rootCommentPage.getCurrent(), rootCommentPage.getSize(), rootCommentPage.getTotal());
        if(CollUtil.isEmpty(rootCommentList)){
            return rootCommentVOPage;
        }
        // 获取子评论请求对象
        CommentChildQueryRequest commentChildQueryRequest = new CommentChildQueryRequest();
        if(haveChildren){
            commentChildQueryRequest.setPageSize(childPageSize);
            commentChildQueryRequest.setPageNum(childPageNum);
        }
        // 获取回复这条评论的用户名称
        Set<Long> toUserIdSet = rootCommentList.stream().map(Comment::getToUserId)
                .collect(Collectors.toSet());
        Map<Long, List<User>> toUserIdUserMap = userService.listByIds(toUserIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 获取创建评论用户名称
        Set<Long> userIdSet = rootCommentList.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment rootComment : rootCommentList) {
            CommentVO commentVO = BeanCopyUtils.copy(rootComment, CommentVO.class);
            // 根据 id 获取回复这条评论的用户名称
            User toUser = toUserIdUserMap.get(rootComment.getToUserId()).get(0);
            commentVO.setToUserName(toUser.getUserName());
            // 根据 id 获取创建评论用户名称
            User user = userIdUserMap.get(rootComment.getUserId()).get(0);
            commentVO.setUserName(user.getUserName());
            if(haveChildren){
                commentChildQueryRequest.setRootCommentId(rootComment.getId());
                commentVO.setChildren(this.getChildCommentPage(commentChildQueryRequest));
            }
            commentVOList.add(commentVO);
        }
        rootCommentVOPage.setRecords(commentVOList);
        return rootCommentVOPage;
    }

    @Override
    public Page<CommentVO> getChildCommentPage(CommentChildQueryRequest commentChildQueryRequest) {
        Long rootCommentId = commentChildQueryRequest.getRootCommentId();
        Integer pageSize = commentChildQueryRequest.getPageSize();
        Integer pageNum = commentChildQueryRequest.getPageNum();
        ThrowUtils.throwIf(rootCommentId == null, ErrorCode.PARAMS_ERROR, "找不到该根评论");
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        // 判断根评论 id 是否合法
        Comment comment = this.getById(rootCommentId);
        ThrowUtils.throwIf(comment == null, ErrorCode.PARAMS_ERROR, "找不到该根评论");
        // 找到所有子评论
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery(Comment.class)
                .eq(Comment::getRootCommentId, rootCommentId);
        Page<Comment> childCommentPage = this.page(new Page<>(pageNum, pageSize), wrapper);
        return this.getCommentVOPage(childCommentPage, false, 0, 0);
    }
}




