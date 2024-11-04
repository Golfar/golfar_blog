package com.golfar.blog.pojo.vo.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PostPageVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章所属分类名称
     */
    private String categoryName;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 创建用户 昵称
     */
    private String userName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
