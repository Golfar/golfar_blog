package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.Comment;
import com.golfar.blog.service.CommentService;
import com.golfar.blog.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【comment(帖子评论)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




