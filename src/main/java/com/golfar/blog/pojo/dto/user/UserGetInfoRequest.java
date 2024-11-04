package com.golfar.blog.pojo.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserGetInfoRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
