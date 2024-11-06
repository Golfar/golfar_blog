package com.golfar.blog.pojo.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户编辑个人信息请求类
 */
@Data
public class UserUpdateMyInfoRequest implements Serializable {

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
