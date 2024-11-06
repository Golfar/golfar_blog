package com.golfar.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.pojo.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.golfar.blog.pojo.vo.category.CategoryVO;

/**
* @author Loong
* @description 针对表【category(类别)】的数据库操作Service
* @createDate 2024-10-27 15:05:09
*/
public interface CategoryService extends IService<Category> {

    /**
     * 分页获取类别接口
     * @param categoryPage
     * @return
     */
    Page<CategoryVO> getCategoryPageVO(Page<Category> categoryPage);
}
