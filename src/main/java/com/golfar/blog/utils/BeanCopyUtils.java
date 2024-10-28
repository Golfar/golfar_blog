package com.golfar.blog.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 实体类拷贝工具类
 * @date : 2024-10-27 16:18
 **/
public class BeanCopyUtils {

    public static <T> T copy(Object source, Class<T> tClass){
        T result = null;
        try{
            result = tClass.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static <O, T> List<T> copyList(List<O> source, Class<T> tClass){
        return source.stream()
                .map(o -> copy(o, tClass))
                .collect(Collectors.toList());
    }
}
