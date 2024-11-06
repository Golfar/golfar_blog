package com.golfar.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.common.ResultUtils;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.category.CategoryAddRequest;
import com.golfar.blog.pojo.dto.category.CategoryDeleteRequest;
import com.golfar.blog.pojo.dto.category.CategoryQueryRequest;
import com.golfar.blog.pojo.dto.category.CategoryUpdateRequest;
import com.golfar.blog.pojo.entity.Category;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.pojo.vo.category.CategoryVO;
import com.golfar.blog.service.CategoryService;
import com.golfar.blog.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取类别，目录相关接口
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    // TODO 除get方法，所有接口都需要管理员才能操作，需要鉴权

    @Resource
    private CategoryService categoryService;
    @Resource
    private UserService userService;

    /**
     * 添加目录类别
     * TODO 只有管理员能够添加
     * @param categoryAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCategory(@RequestBody CategoryAddRequest categoryAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(categoryAddRequest == null || request == null, ErrorCode.PARAMS_ERROR, "添加类别参数错误或连接错误");
        String name = categoryAddRequest.getName();
        ThrowUtils.throwIf(StringUtils.isEmpty(name), ErrorCode.PARAMS_ERROR, "类别名称为空");
        Long id = userService.getLoginUser(request).getId();
        Category category = new Category();
        category.setName(name);
        category.setUserId(id);
        boolean saved = categoryService.save(category);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "类别保存失败");
        return ResultUtils.success(category.getId());
    }

    /**
     * 删除目录类别
     * TODO 只有管理员能够删除
     * @param categoryDeleteRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCategory(@RequestBody CategoryDeleteRequest categoryDeleteRequest){
        ThrowUtils.throwIf(categoryDeleteRequest == null, ErrorCode.PARAMS_ERROR, "删除类别参数错误");
        Long id = categoryDeleteRequest.getId();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(id), ErrorCode.PARAMS_ERROR, "删除的类不存在");
        Category category = categoryService.getById(id);
        ThrowUtils.throwIf(category == null, ErrorCode.PARAMS_ERROR, "删除的类不存在");
        boolean removed = categoryService.removeById(id);
        return ResultUtils.success(removed);
    }

    /**
     * 修改目录类别
     * TODO 只有管理员能够修改
     * @param categoryUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateCategory(@RequestBody CategoryUpdateRequest categoryUpdateRequest){
        ThrowUtils.throwIf(categoryUpdateRequest == null, ErrorCode.PARAMS_ERROR, "修改类别参数错误");
        Long id = categoryUpdateRequest.getId();
        String name = categoryUpdateRequest.getName();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(id), ErrorCode.PARAMS_ERROR, "修改的类不存在");
        ThrowUtils.throwIf(StringUtils.isEmpty(name), ErrorCode.PARAMS_ERROR, "类别名称为空");
        Category category = new Category();
        category.setName(name);
        category.setUserId(id);
        boolean updated = categoryService.updateById(category);
        return ResultUtils.success(updated);
    }

    /**
     * 获取目录类别
     * @param categoryQueryRequest
     * @return
     */
    @PostMapping("/getAll")
    public BaseResponse<Page<CategoryVO>> getAllCategory(@RequestBody CategoryQueryRequest categoryQueryRequest){
        ThrowUtils.throwIf(categoryQueryRequest == null, ErrorCode.PARAMS_ERROR, "类别查询参数错误");
        Integer pageSize = categoryQueryRequest.getPageSize();
        Integer pageNum = categoryQueryRequest.getPageNum();
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery(Category.class).select(Category::getName, Category::getId);
        Page<Category> categoryPage = categoryService.page(new Page<>(pageNum, pageSize), wrapper);
        Page<CategoryVO> categoryVOPage = categoryService.getCategoryPageVO(categoryPage);
        return ResultUtils.success(categoryVOPage);
    }
}
