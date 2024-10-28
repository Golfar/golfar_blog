package com.golfar.blog;

import com.golfar.blog.pojo.entity.User;
import com.golfar.blog.pojo.vo.LoginUserVo;
import com.golfar.blog.utils.BeanCopyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : golfar
 * @project : golfar_blog
 * @description : BeanCopyUtils测试类
 * @date : 2024-10-27 16:27
 **/
public class BeanCopyUtilsTest {

    @Test
    public void test(){
        User user = new User();
        user.setUserName("golfar");
        user.setUserPassword("123");
        user.setId(12324L);

        User user2 = new User();
        user2.setUserName("golfar");
        user2.setUserPassword("123");
        user2.setId(12324L);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        LoginUserVo copy = BeanCopyUtils.copy(user, LoginUserVo.class);
        List<LoginUserVo> ts = BeanCopyUtils.copyList(users, LoginUserVo.class);
        System.out.println(copy);
        System.out.println(ts);

    }
}
