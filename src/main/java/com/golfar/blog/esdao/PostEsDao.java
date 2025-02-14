package com.golfar.blog.esdao;

import com.golfar.blog.pojo.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
}
