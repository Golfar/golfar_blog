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
}




