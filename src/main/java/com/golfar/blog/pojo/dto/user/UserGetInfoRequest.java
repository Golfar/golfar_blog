package com.golfar.blog.pojo.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 查找用户个人信息请求类
 */
@Data
public class UserGetInfoRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
