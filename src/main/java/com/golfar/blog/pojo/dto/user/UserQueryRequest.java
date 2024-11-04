package com.golfar.blog.pojo.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 用户查询请求类
 * @date : 2024-10-27 15:21
 **/
@Data
public class UserQueryRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;
}
