package com.golfar.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.constant.UserConstant;
import com.golfar.blog.exception.BusinessException;
import com.golfar.blog.mapper.UserMapper;
import com.golfar.blog.pojo.dto.user.UserQueryRequest;
import com.golfar.blog.pojo.entity.User;
import com.golfar.blog.pojo.vo.UserLoginVO;
import com.golfar.blog.pojo.vo.UserVO;
import com.golfar.blog.service.UserService;
import com.golfar.blog.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
* @author Loong
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 密码加盐，混淆密码
     */
    private static final String SALT = "golfar";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        // 参数校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号，用户名或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 密码和校验密码
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }

        // 同步代码块，锁定账号
        synchronized (userAccount.intern()){
            // 账号不能重复
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUserAccount, userAccount);
            List<User> result = list(wrapper);
            if(result != null && !result.isEmpty()){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复了");
            }
            // 密码加盐加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            // TODO 给用户添加随机昵称

            boolean saved = save(user);
            if(!saved){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public UserLoginVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 校验用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        User user = getOne(wrapper);
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        // 4. 登录成功，在Session中记录登陆状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVo(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {

        // 判断用户是否登录
        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(loginUser == null || loginUser.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        // TODO 如何保证拿得到user是最新的user
        return loginUser;
    }

    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 判断用户是否登录
        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(loginUser == null || loginUser.getId() == null){
            return null;
        }

        // TODO 如何保证拿得到user是最新的user
        return loginUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {

        // 仅管理员可查询
        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return isAdmin(loginUser);
    }

    @Override
    public boolean isAdmin(User user) {

        // 参数校验
        if(user == null || user.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User hardUser = getById(user.getId());
        if(hardUser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return UserConstant.ADMIN_ROLE.equals(hardUser.getUserRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute(UserConstant.USER_LOGIN_STATE) == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserLoginVO getLoginUserVo(User user) {
        if(user == null || user.getId() == null || user.getId() <= 0){
            return null;
        }
        return BeanCopyUtils.copy(user, UserLoginVO.class);
    }

    @Override
    public UserVO getUserVo(User user) {
        if(user == null || user.getId() == null || user.getId() <= 0){
            return null;
        }
        return BeanCopyUtils.copy(user, UserVO.class);
    }

    @Override
    public List<UserVO> getUserVo(List<User> userList) {
        if(userList == null || userList.isEmpty()){
            return new ArrayList<>();
        }
        return BeanCopyUtils.copyList(userList, UserVO.class);
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if(userQueryRequest == null);
        return null;
    }
}




