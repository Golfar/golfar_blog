package com.golfar.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.golfar.blog.pojo.dto.user.UserQueryRequest;
import com.golfar.blog.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.golfar.blog.pojo.vo.LoginUserVo;
import com.golfar.blog.pojo.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Loong
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-10-27 15:05:09
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param userName
     * @param checkPassword
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String userName, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVo userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * todo 这个接口是干啥的
     * 获取当前登录用户信息（允许未登录）
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 当前登录用户是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 获取用户是否为管理员
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户登出
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取已登录用户脱敏信息
     * @param user
     * @return
     */
    LoginUserVo getLoginUserVo(User user);

    /**
     * 获取用户脱敏信息
     * @param user
     * @return
     */
    UserVo getUserVo(User user);

    /**
     * 获取用户脱敏信息
     * @param userList
     * @return
     */
    List<UserVo> getUserVo(List<User> userList);

    /**
     * 获取查询条件
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}
