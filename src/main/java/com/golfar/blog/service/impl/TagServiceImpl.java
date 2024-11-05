package com.golfar.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.Category;
import com.golfar.blog.pojo.entity.Tag;
import com.golfar.blog.pojo.vo.category.CategoryVO;
import com.golfar.blog.pojo.vo.tag.TagVO;
import com.golfar.blog.service.TagService;
import com.golfar.blog.mapper.TagMapper;
import com.golfar.blog.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Loong
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public Page<TagVO> getTagPageVO(Page<Tag> tagPage) {
        List<Tag> tagList = tagPage.getRecords();
        Page<TagVO> tagVOPage = new Page<>(tagPage.getCurrent(), tagPage.getSize(), tagPage.getTotal());
        if(CollUtil.isEmpty(tagList)){
            return tagVOPage;
        }
        List<TagVO> tagVOList = BeanCopyUtils.copyList(tagList, TagVO.class);
        tagVOPage.setRecords(tagVOList);
        return tagVOPage;
    }
}




