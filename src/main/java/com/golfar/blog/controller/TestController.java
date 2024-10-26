package com.golfar.blog.controller;

import com.golfar.blog.common.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 测试
 * @date : 2024-10-27 00:29
 **/
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public String test(String hello){
        System.out.println(hello);
        return hello;
    }
}
