package com.golfar.blog.pojo.dto.tag;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagDeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
