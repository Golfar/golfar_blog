package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.Category;
import com.golfar.blog.service.CategoryService;
import com.golfar.blog.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【category(类别)】的数据库操作Service实现
* @createDate 2024-10-27 00:11:45
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




