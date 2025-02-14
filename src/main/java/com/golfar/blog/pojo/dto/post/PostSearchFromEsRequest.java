package com.golfar.blog.pojo.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostSearchFromEsRequest implements Serializable {
    Long id;
    Long notId;
    String searchText;
    String title;
    String content;
    List<String> tags;
    Long categoryId;
    String isDraft;
    Long userId;
    Integer pageSize;
    Integer pageNum;
}
