package com.golfar.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.golfar.blog.pojo.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.golfar.blog.pojo.vo.tag.TagVO;

/**
* @author Loong
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-10-27 15:05:09
*/
public interface TagService extends IService<Tag> {

    /**
     * 分页获取全部标签
     * @param tagPage
     * @return
     */
    Page<TagVO> getTagPageVO(Page<Tag> tagPage);
}
