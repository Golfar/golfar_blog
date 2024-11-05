package com.golfar.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.common.ResultUtils;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.post.*;
import com.golfar.blog.pojo.vo.post.PostDetailVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.PostService;
import com.golfar.blog.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
public class PostController {
    @Resource
    private PostService postService;

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
     * 需要登录用户对象判断用户是否已经点赞收藏
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
     * 查看帖子列表，查看首页帖子或我的帖子
     * @param postQueryPageRequest
     * @return
     */
    @PostMapping("/get/page")
    public BaseResponse<Page<PostPageVO>> getPostPage(@RequestBody PostQueryPageRequest postQueryPageRequest, HttpServletRequest request){
        ThrowUtils.throwIf(postQueryPageRequest == null || request == null, ErrorCode.PARAMS_ERROR, "查看帖子参数为空或连接异常");
        Page<PostPageVO> postPageVOPage = postService.getPostPage(postQueryPageRequest, request);
        return ResultUtils.success(postPageVOPage);
    }

}
