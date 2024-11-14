package com.golfar.blog.controller;

import com.golfar.blog.common.BaseResponse;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.common.ResultUtils;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.pojo.dto.user.*;
import com.golfar.blog.pojo.entity.User;
import com.golfar.blog.pojo.vo.user.UserLoginVO;
import com.golfar.blog.pojo.vo.user.UserVO;
import com.golfar.blog.service.UserService;
import com.golfar.blog.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest){
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR, "注册请求参数为空");
        String userPassword = userRegisterRequest.getUserPassword();
        String userAccount = userRegisterRequest.getUserAccount();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long uid = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(uid);
    }

    /**
     * 用户登录接口
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        ThrowUtils.throwIf(userLoginRequest == null || request == null, ErrorCode.PARAMS_ERROR, "登录请求参数为空");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        UserLoginVO userLoginVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userLoginVO);
    }

    /**
     * 用户登出接口
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request){
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "连接异常");
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 修改用户信息接口
     * @param userUpdateMyInfoRequest
     * @param request
     * @return
     */
    @PostMapping("/update/userinfo")
    public BaseResponse<Boolean> updateMyInfo(@RequestBody UserUpdateMyInfoRequest userUpdateMyInfoRequest, HttpServletRequest request){
        ThrowUtils.throwIf(userUpdateMyInfoRequest == null || request == null, ErrorCode.PARAMS_ERROR, "修改用户信息参数为空或连接异常");
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        User updateInfo = BeanCopyUtils.copy(userUpdateMyInfoRequest, User.class);
        updateInfo.setId(loginUser.getId());
        boolean result = userService.updateById(updateInfo);
        return ResultUtils.success(result);
    }

    /**
     * 获取用户个人信息接口
     * userGetInfoRequest参数若为空，则查询指定用户信息，否则查询当前登录用户信息
     * @param userGetInfoRequest
     * @param request
     * @return
     */
    @PostMapping("/get/userinfo")
    public BaseResponse<UserVO> getUserInfo(@RequestBody UserGetInfoRequest userGetInfoRequest, HttpServletRequest request){
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "修改用户信息参数为空或连接异常");
        Long id;
        if(userGetInfoRequest != null || userGetInfoRequest.getId() != null){
            id = userGetInfoRequest.getId();
        }
        else {
            User loginUser = userService.getLoginUser(request);
            id = loginUser.getId();
        }
        User user = userService.getById(id);
        UserVO result = BeanCopyUtils.copy(user, UserVO.class);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/loginUser")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        UserVO userVo = userService.getUserVo(loginUser);
        return ResultUtils.success(userVo);
    }
}
