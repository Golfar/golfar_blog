package com.golfar.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.pojo.dto.post.PostFavourQueryPageRequest;
import com.golfar.blog.pojo.entity.PostFavour;
import com.baomidou.mybatisplus.extension.service.IService;
import com.golfar.blog.pojo.vo.post.PostPageVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Loong
* @description 针对表【post_favour(帖子收藏)】的数据库操作Service
* @createDate 2024-10-27 15:05:09
*/
public interface PostFavourService extends IService<PostFavour> {

    /**
     * 分页获取收藏的帖子
     * @param postFavourQueryPageRequest
     * @param request
     * @return
     */
    Page<PostPageVO> getFavourPostPage(PostFavourQueryPageRequest postFavourQueryPageRequest, HttpServletRequest request);
}
