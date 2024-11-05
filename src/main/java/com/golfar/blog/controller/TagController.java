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
import com.golfar.blog.pojo.dto.tag.TagAddRequest;
import com.golfar.blog.pojo.dto.tag.TagDeleteRequest;
import com.golfar.blog.pojo.dto.tag.TagQueryRequest;
import com.golfar.blog.pojo.dto.tag.TagUpdateRequest;
import com.golfar.blog.pojo.entity.Category;
import com.golfar.blog.pojo.entity.Tag;
import com.golfar.blog.pojo.vo.category.CategoryVO;
import com.golfar.blog.pojo.vo.tag.TagVO;
import com.golfar.blog.service.CategoryService;
import com.golfar.blog.service.TagService;
import com.golfar.blog.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tag")
public class TagController {

    // TODO 除get方法，所有接口都需要管理员才能操作，需要鉴权

    @Resource
    private TagService tagService;
    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addTag(@RequestBody TagAddRequest tagAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(tagAddRequest == null || request == null, ErrorCode.PARAMS_ERROR, "添加标签参数错误或连接错误");
        String name = tagAddRequest.getName();
        ThrowUtils.throwIf(StringUtils.isEmpty(name), ErrorCode.PARAMS_ERROR, "标签名称为空");
        Long id = userService.getLoginUser(request).getId();
        Tag tag = new Tag();
        tag.setName(name);
        tag.setUserId(id);
        boolean saved = tagService.save(tag);
        ThrowUtils.throwIf(!saved, ErrorCode.OPERATION_ERROR, "类别保存失败");
        return ResultUtils.success(tag.getId());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTag(@RequestBody TagDeleteRequest tagDeleteRequest){
        ThrowUtils.throwIf(tagDeleteRequest == null, ErrorCode.PARAMS_ERROR, "删除类别参数错误");
        Long id = tagDeleteRequest.getId();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(id), ErrorCode.PARAMS_ERROR, "删除的类不存在");
        Tag category = tagService.getById(id);
        ThrowUtils.throwIf(category == null, ErrorCode.PARAMS_ERROR, "删除的类不存在");
        boolean removed = tagService.removeById(id);
        return ResultUtils.success(removed);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTag(@RequestBody TagUpdateRequest tagUpdateRequest){
        ThrowUtils.throwIf(tagUpdateRequest == null, ErrorCode.PARAMS_ERROR, "修改类别参数错误");
        Long id = tagUpdateRequest.getId();
        String name = tagUpdateRequest.getName();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(id), ErrorCode.PARAMS_ERROR, "修改的类不存在");
        ThrowUtils.throwIf(StringUtils.isEmpty(name), ErrorCode.PARAMS_ERROR, "类别名称为空");
        Tag tag = new Tag();
        tag.setName(name);
        tag.setUserId(id);
        boolean updated = tagService.updateById(tag);
        return ResultUtils.success(updated);
    }

    @PostMapping("/getAll")
    public BaseResponse<Page<TagVO>> getAllTag(@RequestBody TagQueryRequest tagQueryRequest){
        ThrowUtils.throwIf(tagQueryRequest == null, ErrorCode.PARAMS_ERROR, "类别查询参数错误");
        Integer pageSize = tagQueryRequest.getPageSize();
        Integer pageNum = tagQueryRequest.getPageNum();
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        LambdaQueryWrapper<Tag> wrapper = Wrappers.lambdaQuery(Tag.class).select(Tag::getName, Tag::getId);
        Page<Tag> tagPage = tagService.page(new Page<>(pageNum, pageSize), wrapper);
        Page<TagVO> tagVOPage = tagService.getTagPageVO(tagPage);
        return ResultUtils.success(tagVOPage);
    }
}
