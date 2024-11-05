package com.golfar.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.Category;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.pojo.entity.User;
import com.golfar.blog.pojo.vo.category.CategoryVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.CategoryService;
import com.golfar.blog.mapper.CategoryMapper;
import com.golfar.blog.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Loong
* @description 针对表【category(类别)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Override
    public Page<CategoryVO> getCategoryPageVO(Page<Category> categoryPage) {
        List<Category> categoryList = categoryPage.getRecords();
        Page<CategoryVO> categoryVOPage = new Page<>(categoryPage.getCurrent(), categoryPage.getSize(), categoryPage.getTotal());
        if(CollUtil.isEmpty(categoryList)){
            return categoryVOPage;
        }
        List<CategoryVO> categoryVOList = BeanCopyUtils.copyList(categoryList, CategoryVO.class);
        categoryVOPage.setRecords(categoryVOList);
        return categoryVOPage;
    }
}




