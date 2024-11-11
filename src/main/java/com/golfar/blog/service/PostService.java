package com.golfar.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.golfar.blog.pojo.dto.post.*;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.pojo.vo.post.PostDetailVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Loong
* @description 针对表【post(帖子)】的数据库操作Service
* @createDate 2024-10-27 15:05:09
*/
public interface PostService extends IService<Post> {

    /**
     * 新增帖子
     * @param postAddRequest
     * @param request
     * @return
     */
    long addPost(PostAddRequest postAddRequest, HttpServletRequest request);

    /**
     * 帖子内容校验
     * 帖子是否合法
     * @param post
     */
    void validPost(Post post);

    /**
     * 删除帖子
     * @param postDeleteRequest
     * @param request
     * @return
     */
    boolean deletePost(PostDeleteRequest postDeleteRequest, HttpServletRequest request);

    /**
     * 编辑帖子
     * @param postUpdateRequest
     * @param request
     * @return
     */
    boolean updatePost(PostUpdateRequest postUpdateRequest, HttpServletRequest request);

    /**
     * 获取帖子详情
     * @param postQueryDetailRequest
     * @param request
     * @return
     */
    PostDetailVO getPostDetail(PostQueryDetailRequest postQueryDetailRequest, HttpServletRequest request);

    /**
     * 分页获取帖子列表
     * 首页帖子，全部帖子
     * @param postQueryPageRequest
     * @param request
     * @return
     */
    Page<PostPageVO> getPostPage(PostQueryPageRequest postQueryPageRequest, HttpServletRequest request);

    /**
     * 帖子对象转换
     * @param postPage
     * @return
     */
    Page<PostPageVO> getPostPageVO(Page<Post> postPage);

    Page<PostPageVO> getFavourPostPage(PostFavourQueryPageRequest postFavourQueryPageRequest, HttpServletRequest request);

}
