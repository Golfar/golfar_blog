package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.PostFavour;
import com.golfar.blog.service.PostFavourService;
import com.golfar.blog.mapper.PostFavourMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【post_favour(帖子收藏)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
    implements PostFavourService{

}




