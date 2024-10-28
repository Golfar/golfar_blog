package com.golfar.blog.aop;

import com.golfar.blog.annotation.AuthCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : 权限校验aop
 * @date : 2024-10-27 00:04
 **/
@Aspect //这是一个aop切面
@Component
public class AuthInterceptor {

//    @Resource
//    private UserService userService;

    /**
     * 执行拦截，进行权限校验
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable{
        String mustRole = authCheck.mustRole();
        return null;
    }

}
