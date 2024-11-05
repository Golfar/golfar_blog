package com.golfar.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.mapper.PostMapper;
import com.golfar.blog.pojo.dto.post.*;
import com.golfar.blog.pojo.entity.*;
import com.golfar.blog.pojo.vo.post.PostDetailVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.*;
import com.golfar.blog.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Loong
* @description 针对表【post(帖子)】的数据库操作Service实现
* @createDate 2024-10-27 15:05:09
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private PostFavourService postFavourService;
    @Resource
    private PostThumbService postThumbService;

    @Override
    public long addPost(PostAddRequest postAddRequest, HttpServletRequest request) {

        Post post = BeanCopyUtils.copy(postAddRequest, Post.class);
        // Tags 为JSON 需要单独复制
        List<String> tags = postAddRequest.getTags();
        if(tags != null){
            post.setTags(JSONUtil.toJsonStr(tags));
        }
        this.validPost(post);
        User loginUser = userService.getLoginUser(request);
        post.setUserId(loginUser.getId());
        boolean result = this.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建帖子失败");

        return post.getId();
    }

    @Override
    public void validPost(Post post) {
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR, "需要校验的帖子不存在");
        String title = post.getTitle();
        String content = post.getContent();
        String tags = post.getTags();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR, "帖子内容不能为空");
        if(post.getIsDraft().equals("0")){
            ThrowUtils.throwIf(title.length() > 80 || content.length() > 8192, ErrorCode.PARAMS_ERROR, "标题或内容过长");
        }
    }

    @Override
    public boolean deletePost(PostDeleteRequest postDeleteRequest, HttpServletRequest request) {
        Long postId = postDeleteRequest.getId();
        Post post = this.getById(postId);
        ThrowUtils.throwIf(postId == null || postId < 0, ErrorCode.PARAMS_ERROR, "要删除的文章不存在");
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 删除文章id相同，且创建用户相同的文章
        ThrowUtils.throwIf(!userId.equals(post.getUserId()), ErrorCode.PARAMS_ERROR, "无权限删除");
        return this.removeById(postId);
    }

    @Override
    public boolean updatePost(PostUpdateRequest postUpdateRequest, HttpServletRequest request) {
        Long postId = postUpdateRequest.getId();
        List<String> tags = postUpdateRequest.getTags();
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "更新的文章不存在");
        Post post = BeanCopyUtils.copy(postUpdateRequest, Post.class);
        if(tags != null){
            post.setTags(JSONUtil.toJsonStr(tags));
        }
        this.validPost(post);
        // 判断帖子 id 是否存在
        Post oldPost = this.getById(postId);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.PARAMS_ERROR, "更新的文章不存在");
        // 判断创建用户 id 是否相同
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        ThrowUtils.throwIf(!loginUserId.equals(oldPost.getUserId()), ErrorCode.PARAMS_ERROR, "无权限更新");
        // 更新文章
        return this.updateById(post);
    }

    @Override
    public PostDetailVO getPostDetail(PostQueryDetailRequest postQueryDetailRequest, HttpServletRequest request) {

        // 参数校验
        Long postId = postQueryDetailRequest.getId();
        Boolean isMyPost = postQueryDetailRequest.getIsMyPost();
        ThrowUtils.throwIf(postId == null || postId <= 0, ErrorCode.PARAMS_ERROR, "查看的文章不存在");
        // 检查文章是否存在，是否为草稿
        LambdaQueryWrapper<Post> wrapper = Wrappers.lambdaQuery(Post.class).eq(Post::getId, postId)
                .eq(!isMyPost, Post::getIsDraft, "0");
        Post post = this.getOne(wrapper);
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR, "查看的文章不存在");
        // 将字符串转为JSON
        PostDetailVO postDetailVO = BeanCopyUtils.copy(post, PostDetailVO.class);
        postDetailVO.setTags(JSONUtil.toList(JSONUtil.parseArray(post.getTags()), String.class));
        // 根据文章作者id， 返回文章作者昵称
        Long userId = post.getUserId();
        LambdaQueryWrapper<User> userWrapper = Wrappers.lambdaQuery(User.class)
                .select(User::getUserName)
                .eq(User::getId, userId);
        User user = userService.getOne(userWrapper);
        postDetailVO.setUserName(user.getUserName());
        // 根据类别id，找到类别名称
        Long categoryId = post.getCategoryId();
        LambdaQueryWrapper<Category> categoryWrapper = Wrappers.lambdaQuery(Category.class)
                .select(Category::getName)
                .eq(Category::getId, categoryId);
        Category category = categoryService.getOne(categoryWrapper);
        postDetailVO.setCategoryName(category.getName());
        // 根据当前登录用户，判断用户是否可以对该文章点赞或收藏
        User loginUser = userService.getLoginUserPermitNull(request);
        postDetailVO.setHasThumb(false);
        postDetailVO.setHasFavour(false);
        // 用户已经登录，判断用户是否已经收藏或者点赞该文章
        if(loginUser != null){
            Long loginUserId = loginUser.getId();
            // 判断点赞
            LambdaQueryWrapper<PostThumb> thumbWrapper = Wrappers.lambdaQuery(PostThumb.class)
                    .eq(PostThumb::getPostId, postId)
                    .eq(PostThumb::getUserId, loginUserId);
            PostThumb postThumb = postThumbService.getOne(thumbWrapper);
            if(postThumb != null){
                postDetailVO.setHasThumb(true);
            }
            // 判断收藏
            LambdaQueryWrapper<PostFavour> favourWrapper = Wrappers.lambdaQuery(PostFavour.class)
                    .eq(PostFavour::getPostId, postId)
                    .eq(PostFavour::getUserId, loginUserId);
            PostFavour postFavour = postFavourService.getOne(favourWrapper);
            if(postFavour != null){
                postDetailVO.setHasFavour(true);
            }
        }
        // 返回封装类
        return postDetailVO;
    }

    @Override
    public Page<PostPageVO> getPostPage(PostQueryPageRequest postQueryPageRequest, HttpServletRequest request) {
        Integer pageSize = postQueryPageRequest.getPageSize();
        Integer pageNum = postQueryPageRequest.getPageNum();
        Boolean isMyPost = postQueryPageRequest.getIsMyPost();
        // 参数校验
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        Long loginUserId = 0L;
        if(isMyPost != null && isMyPost){
            loginUserId = userService.getLoginUser(request).getId();
        } else{
            isMyPost = false;
        }
        // 分页查询
        LambdaQueryWrapper<Post> wrapper = Wrappers.lambdaQuery(Post.class)
                .eq(!isMyPost, Post::getIsDraft, "0")
                .eq(isMyPost, Post::getUserId, loginUserId)
                .orderByDesc(Post::getCreateTime);
        Page<Post> postPage = this.page(new Page<>(pageNum, pageSize), wrapper);
        // 封装结果，返回封装类
        return this.getPostPageVO(postPage);
    }

    @Override
    public Page<PostPageVO> getPostPageVO(Page<Post> postPage) {
        List<Post> postList = postPage.getRecords();
        Page<PostPageVO> postVOPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if(CollUtil.isEmpty(postList)){
            return postVOPage;
        }
        // 根据用户 id 查询用户名称
        Set<Long> userIdSet = postList.stream().map(Post::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 根据类别 id 查询类别名称
        Set<Long> categoryIdSet = postList.stream().map(Post::getCategoryId).collect(Collectors.toSet());
        Map<Long, List<Category>> categoryIdCategoryListMap = categoryService.listByIds(categoryIdSet).stream()
                .collect(Collectors.groupingBy(Category::getId));
        List<PostPageVO> postPageVOList = new ArrayList<>();
        for (Post post : postList) {
            PostPageVO postPageVO = BeanCopyUtils.copy(post, PostPageVO.class);
            // 根据类别 id 查询类别名称
            Category category = categoryIdCategoryListMap.get(post.getCategoryId()).get(0);
            postPageVO.setCategoryName(category.getName());
            // 根据用户 id 查询用户名称
            User user = userIdUserListMap.get(post.getUserId()).get(0);
            postPageVO.setUserName(user.getUserName());
            // 封装 tags
            postPageVO.setTags(JSONUtil.toList(JSONUtil.parseArray(post.getTags()), String.class));
            postPageVOList.add(postPageVO);
        }
        postVOPage.setRecords(postPageVOList);
        return postVOPage;
    }


}




