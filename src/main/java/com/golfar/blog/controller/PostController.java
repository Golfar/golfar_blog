package com.golfar.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.common.ResultUtils;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.post.*;
import com.golfar.blog.pojo.entity.CommentThumb;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.pojo.entity.PostFavour;
import com.golfar.blog.pojo.entity.PostThumb;
import com.golfar.blog.pojo.vo.post.PostDetailVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.PostFavourService;
import com.golfar.blog.service.PostService;
import com.golfar.blog.service.PostThumbService;
import com.golfar.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子相关接口
 */
@RestController
@RequestMapping("/post")
public class PostController {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private PostFavourService postFavourService;

    /**
     * 创建帖子
     * 仅登录用户能创建帖子
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postAddRequest == null || request == null, ErrorCode.PARAMS_ERROR, "添加帖子参数为空或连接异常");
        long postId = postService.addPost(postAddRequest, request);
        return ResultUtils.success(postId);
    }

    /**
     * 删除帖子
     * 仅创建该帖子的用户能够删除帖子
     * @param postDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(@RequestBody PostDeleteRequest postDeleteRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postDeleteRequest == null || request == null, ErrorCode.PARAMS_ERROR, "删除帖子参数为空或连接异常");
        boolean result = postService.deletePost(postDeleteRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 修改帖子
     * 仅创建该帖子的用户能够修改帖子
     * @param postUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> editPost(@RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postUpdateRequest == null || request == null, ErrorCode.PARAMS_ERROR, "编辑帖子参数为空或连接异常");
        boolean result = postService.updatePost(postUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 查看帖子详情，所有人都能查看帖子
     * 需要对已经登录用户对象判断用户是否已经点赞收藏
     * @param postQueryDetailRequest
     * @param request
     * @return
     */
    @PostMapping("/get/detail")
    public BaseResponse<PostDetailVO> getPostDetail(@RequestBody PostQueryDetailRequest postQueryDetailRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postQueryDetailRequest == null || request == null, ErrorCode.PARAMS_ERROR, "查看帖子详情参数为空或连接异常");
        PostDetailVO postDetailVO = postService.getPostDetail(postQueryDetailRequest, request);
        return ResultUtils.success(postDetailVO);
    }

    /**
     * 查看帖子列表
     * 查看首页帖子 或我的帖子
     * @param postQueryPageRequest
     * @return
     */
    @PostMapping("/get/page")
    public BaseResponse<Page<PostPageVO>> getPostPage(@RequestBody PostQueryPageRequest postQueryPageRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postQueryPageRequest == null || request == null, ErrorCode.PARAMS_ERROR, "查看帖子参数为空或连接异常");
        Page<PostPageVO> postPageVOPage = postService.getPostPage(postQueryPageRequest, request);
        return ResultUtils.success(postPageVOPage);
    }

    /**
     * 点赞帖子
     * 只有登录用户才能点赞
     * @param postId
     * @param request
     * @return
     */
    @GetMapping("/thumb")
    public BaseResponse<Boolean> thumbPost(Long postId, HttpServletRequest request){
        // TODO 有没有必要上事务 判断是否已经点赞
        // 参数校验
        ThrowUtils.throwIf(postId == null || request == null, ErrorCode.PARAMS_ERROR, "查看帖子参数为空或连接异常");
        Long loginUserId = userService.getLoginUser(request).getId();
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR, "点赞的帖子不存在");
        // 判断是否已经点赞
        LambdaQueryWrapper<PostThumb> postThumbWrapper = Wrappers.lambdaQuery(PostThumb.class)
                .eq(PostThumb::getPostId, postId)
                .eq(PostThumb::getUserId, loginUserId);
        long postThumbCount = postThumbService.count(postThumbWrapper);
        ThrowUtils.throwIf(postThumbCount == 1, ErrorCode.OPERATION_ERROR, "您已经点过赞了");
        // 插入点赞数据
        PostThumb postThumb = new PostThumb();
        postThumb.setPostId(postId);
        postThumb.setUserId(loginUserId);
        boolean saved = postThumbService.save(postThumb);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "点赞失败");
        // 点赞数加1
        // TODO 用缓存做比较好
        post.setThumbNum(post.getThumbNum() + 1);
        boolean updated = postService.updateById(post);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "点赞失败");
        return ResultUtils.success(true);
    }

    /**
     * 收藏帖子
     * 只有登录用户才能收藏
     * @param postId
     * @param request
     * @return
     */
    @GetMapping("/favour")
    public BaseResponse<Boolean> favourPost(Long postId, HttpServletRequest request){
        // TODO 有没有必要上事务
        // 参数校验
        ThrowUtils.throwIf(postId == null || request == null, ErrorCode.PARAMS_ERROR, "收藏的帖子不存在");
        Long loginUserId = userService.getLoginUser(request).getId();
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR, "收藏的帖子不存在");
        // 判断是否已经收藏
        LambdaQueryWrapper<PostFavour> postFavourWrapper = Wrappers.lambdaQuery(PostFavour.class)
                .eq(PostFavour::getUserId, loginUserId)
                .eq(PostFavour::getPostId, postId);
        long count = postFavourService.count(postFavourWrapper);
        ThrowUtils.throwIf(count == 0, ErrorCode.OPERATION_ERROR, "已经收藏过该帖子");
        // 插入收藏
        PostFavour postFavour = new PostFavour();
        postFavour.setPostId(postId);
        postFavour.setUserId(loginUserId);
        boolean saved = postFavourService.save(postFavour);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "收藏失败");
        // 收藏数加1
        // TODO 用缓存做比较好
        post.setFavourNum(post.getFavourNum() + 1);
        boolean updated = postService.updateById(post);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR, "收藏失败");
        return ResultUtils.success(true);
    }

    /**
     * 分页获取我收藏的帖子
     * 登录用户
     * @param postFavourQueryPageRequest
     * @param request
     * @return
     */
    @GetMapping("/get/page/favour")
    public BaseResponse<Page<PostPageVO>> getFavourPostPage(@RequestBody PostFavourQueryPageRequest postFavourQueryPageRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postFavourQueryPageRequest == null || request == null, ErrorCode.PARAMS_ERROR, "连接异常");
        Page<PostPageVO> postPageVOPage = postService.getFavourPostPage(postFavourQueryPageRequest, request);
        return ResultUtils.success(postPageVOPage);
    }

    @GetMapping("/search/page/vo")
    public BaseResponse<Page<PostPageVO>> searchPostVOByPage(@RequestBody PostSearchFromEsRequest postSearchFromEsRequest, HttpServletRequest request){
        Page<Post> postPage = postService.searchFromEs(postSearchFromEsRequest, request);
        return ResultUtils.success(postService.getPostPageVO(postPage));
    }

}
