package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.pojo.entity.Tag;
import com.golfar.blog.service.TagService;
import com.golfar.blog.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author Loong
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




