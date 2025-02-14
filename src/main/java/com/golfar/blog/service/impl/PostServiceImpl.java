package com.golfar.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.golfar.blog.common.ErrorCode;
import com.golfar.blog.constant.CommonConstant;
import com.golfar.blog.exception.ThrowUtils;
import com.golfar.blog.mapper.PostMapper;
import com.golfar.blog.pojo.dto.post.*;
import com.golfar.blog.pojo.entity.*;
import com.golfar.blog.pojo.vo.post.PostDetailVO;
import com.golfar.blog.pojo.vo.post.PostPageVO;
import com.golfar.blog.service.*;
import com.golfar.blog.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

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
        // 更新浏览量
        // TODO 用缓存装viewCount
        if(post.getIsDraft().equals("0")){
            post.setViewCount(post.getViewCount() + 1);
        }
        this.updateById(post);
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

    @Override
    public Page<PostPageVO> getFavourPostPage(PostFavourQueryPageRequest postFavourQueryPageRequest, HttpServletRequest request) {
        // 参数校验
        Long loginUserId = userService.getLoginUser(request).getId();
        Integer pageSize = postFavourQueryPageRequest.getPageSize();
        Integer pageNum = postFavourQueryPageRequest.getPageNum();
        ThrowUtils.throwIf(pageSize == null || pageSize <= 0 || pageNum == null || pageNum <= 0, ErrorCode.PARAMS_ERROR, "请求的页数不合法");
        // 查询我收藏的帖子
        LambdaQueryWrapper<PostFavour> postFavourWrapper = Wrappers.lambdaQuery(PostFavour.class)
                .select(PostFavour::getPostId)
                .eq(PostFavour::getUserId, loginUserId);
        List<PostFavour> postFavourList = postFavourService.list(postFavourWrapper);
        // 获得我收藏的帖子 id
        Set<Long> postIdSet = postFavourList.stream().map(PostFavour::getPostId).collect(Collectors.toSet());
        // 根据帖子 id 查询我收藏的帖子内容
        LambdaQueryWrapper<Post> postWrapper = Wrappers.lambdaQuery(Post.class)
                .in(Post::getId, postIdSet);
        Page<Post> postPage = this.page(new Page<>(pageNum, pageSize), postWrapper);
        return this.getPostPageVO(postPage);
    }

    @Override
    public Page<Post> searchFromEs(PostSearchFromEsRequest postSearchFromEsRequest, HttpServletRequest request) {
        // 获取参数
        Long id = postSearchFromEsRequest.getId();
        Long notId = postSearchFromEsRequest.getNotId();
        String searchText = postSearchFromEsRequest.getSearchText();
        String title = postSearchFromEsRequest.getTitle();
        String content = postSearchFromEsRequest.getContent();
        List<String> tags = postSearchFromEsRequest.getTags();
        Long categoryId = postSearchFromEsRequest.getCategoryId();
        String isDraft = postSearchFromEsRequest.getIsDraft();
        Long userId = postSearchFromEsRequest.getUserId();
        Integer pageSize = postSearchFromEsRequest.getPageSize();
        // es 其实页数为0
        Integer pageNum = postSearchFromEsRequest.getPageNum() - 1;
        // 构造查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if(id != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if(notId != null){
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
        }
        if(userId != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        if(categoryId != null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryId", categoryId));
        }
        // 必须包含所有标签
        if(CollUtil.isNotEmpty(tags)){
            for(String tag: tags){
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 按关键词搜索
        if(StringUtils.isNotBlank(searchText)){
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("answer", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .build();
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        // 复用 myBatis 分页对象，封装返回结果
        Page<Post> postPage = new Page<>();
        postPage.setTotal(searchHits.getTotalHits());
        List<Post> resourceList = new ArrayList<>();
        if(searchHits.hasSearchHits()){
            List<SearchHit<PostEsDTO>> searchHitsList = searchHits.getSearchHits();
            for (SearchHit<PostEsDTO> questionEsDTOSearchHit : searchHitsList) {
                resourceList.add(PostEsDTO.dtoToObj(questionEsDTOSearchHit.getContent()));
            }
        }
        postPage.setRecords(resourceList);
        return postPage;
    }


}




