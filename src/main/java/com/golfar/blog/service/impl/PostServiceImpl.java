package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.service.PostService;
import com.golfar.blog.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【post(帖子)】的数据库操作Service实现
* @createDate 2024-10-27 00:11:45
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

}




