package com.golfar.blog.pojo.dto.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CategoryAddRequest implements Serializable {

    /**
     * 类别名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}