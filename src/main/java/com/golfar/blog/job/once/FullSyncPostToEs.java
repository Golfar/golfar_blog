package com.golfar.blog.job.once;

import cn.hutool.core.collection.CollUtil;
import com.golfar.blog.esdao.PostEsDao;
import com.golfar.blog.pojo.dto.post.PostEsDTO;
import com.golfar.blog.pojo.entity.Post;
import com.golfar.blog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private PostService postService;
    @Resource
    private PostEsDao postEsDao;
    @Override
    public void run(String... args) throws Exception {
        // 全量获取题目（数据量不大的时候使用）
        List<Post> postList = postService.list();
        if (CollUtil.isEmpty(postList)) {
            return;
        }

        // 转为es实体类
        List<PostEsDTO> postEsDTOList = postList.stream().map(PostEsDTO::objToDto).collect(Collectors.toList());

        // 分页批量插入es
        final int pageSize = 500;
        int total = postEsDTOList.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            postEsDao.saveAll(postEsDTOList.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
    }
}
