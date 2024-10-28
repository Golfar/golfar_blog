package com.golfar.blog.pojo.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 用户查询请求类
 * @date : 2024-10-27 15:21
 **/
@Data
public class UserQueryRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
}
