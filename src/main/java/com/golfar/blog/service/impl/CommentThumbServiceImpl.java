package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.CommentThumb;
import com.golfar.blog.service.CommentThumbService;
import com.golfar.blog.mapper.CommentThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【comment_thumb(评论点赞)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class CommentThumbServiceImpl extends ServiceImpl<CommentThumbMapper, CommentThumb>
    implements CommentThumbService{

}




