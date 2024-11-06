package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.post.PostFavourQueryPageRequest;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.pojo.entity.PostFavour;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.PostFavourService;
import com.golfar.blog.mapper.PostFavourMapper;
import com.golfar.blog.service.PostService;
import com.golfar.blog.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Loong
* @description 针对表【post_favour(帖子收藏)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
    implements PostFavourService{

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Override
    public Page<PostPageVO> getFavourPostPage(PostFavourQueryPageRequest postFavourQueryPageRequest, HttpServletRequest request) {
        // 参数校验
        Long loginUserId = userService.getLoginUser(request).getId();
        Integer pageSize = postFavourQueryPageRequest.getPageSize();
        Integer pageNum = postFavourQueryPageRequest.getPageNum();
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        // 查询我收藏的帖子
        LambdaQueryWrapper<PostFavour> postFavourWrapper = Wrappers.lambdaQuery(PostFavour.class)
                .select(PostFavour::getPostId)
                .eq(PostFavour::getUserId, loginUserId);
        List<PostFavour> postFavourList = this.list(postFavourWrapper);
        // 获得我收藏的帖子 id
        Set<Long> postIdSet = postFavourList.stream().map(PostFavour::getPostId).collect(Collectors.toSet());
        // 根据帖子 id 查询我收藏的帖子内容
        LambdaQueryWrapper<Post> postWrapper = Wrappers.lambdaQuery(Post.class)
                .in(Post::getId, postIdSet);
        Page<Post> postPage = postService.page(new Page<>(pageNum, pageSize), postWrapper);
        return postService.getPostPageVO(postPage);
    }
}




