package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.User;
import com.golfar.blog.service.UserService;
import com.golfar.blog.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-10-27 00:11:45
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




