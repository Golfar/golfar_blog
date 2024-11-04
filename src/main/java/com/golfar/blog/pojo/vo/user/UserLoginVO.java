package com.golfar.blog.pojo.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 已登录用户信息响应类
 * @date : 2024-10-27 15:09
 **/
@Data
public class UserLoginVO implements Serializable {

    /**
     * 用户 id
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

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
