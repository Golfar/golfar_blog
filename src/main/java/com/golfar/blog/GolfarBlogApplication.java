package com.golfar.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.golfar.blog.mapper")
public class GolfarBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(GolfarBlogApplication.class, args);
    }
}