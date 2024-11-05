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

    long addPost(PostAddRequest postAddRequest, HttpServletRequest request);

    void validPost(Post post);

    boolean deletePost(PostDeleteRequest postDeleteRequest, HttpServletRequest request);

    boolean updatePost(PostUpdateRequest postUpdateRequest, HttpServletRequest request);

    PostDetailVO getPostDetail(PostQueryDetailRequest postQueryDetailRequest, HttpServletRequest request);

    Page<PostPageVO> getPostPage(PostQueryPageRequest postQueryPageRequest, HttpServletRequest request);

    Page<PostPageVO> getPostPageVO(Page<Post> postPage);

}
