package com.golfar.blog.pojo.dto.post;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.golfar.blog.pojo.entity.Post;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ES 对应实体类
 */
@Document(indexName = "post")
@Data
public class PostEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 文章所属分类Id
     */
    private Long categoryId;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 文章状态， 1 草稿； 0 已发布
     */
    private String isDraft;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     * @param post
     * @return
     */
    public static PostEsDTO objToDto(Post post) {
        if (post == null) {
            return null;
        }
        PostEsDTO questionEsDTO = new PostEsDTO();
        BeanUtils.copyProperties(post, questionEsDTO);
        String tagsStr = post.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            questionEsDTO.setTags(JSONUtil.toList(JSONUtil.parseArray(tagsStr), String.class));
        }
        return questionEsDTO;
    }

    /**
     * 包装类转对象
     * @param postEsDTO
     * @return
     */
    public static Post dtoToObj(PostEsDTO postEsDTO) {
        if (postEsDTO == null) {
            return null;
        }
        Post post = new Post();
        BeanUtils.copyProperties(postEsDTO, post);
        List<String> tagList = postEsDTO.getTags();
        if (CollUtil.isNotEmpty(tagList)) {
            post.setTags(JSONUtil.toJsonStr(tagList));
        }
        return post;
    }
}
