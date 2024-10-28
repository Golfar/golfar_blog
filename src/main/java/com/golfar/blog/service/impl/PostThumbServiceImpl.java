package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.PostThumb;
import com.golfar.blog.service.PostThumbService;
import com.golfar.blog.mapper.PostThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【post_thumb(帖子点赞)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
    implements PostThumbService{

}




