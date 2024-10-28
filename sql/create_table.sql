# 数据库初始化
# @author <a href="https://github.com/golfar">Golfar</a>

-- 创建库
create database if not exists golfar_blog;

-- 切换库
use golfar_blog;

-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    union_id      varchar(256)                           null comment '微信开放平台id',
    mpOpen_id     varchar(256)                           null comment '公众号openId',
    email         varchar(128)                           null comment '邮箱',
    phone_number  varchar(32)                            null comment '电话号码',
    user_name     varchar(256)                           null comment '用户昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_profile  varchar(512)                           null comment '用户简介',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    unique key idx_userAccount (user_account)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    category_id bigint                             not null comment '文章所属分类Id',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    thumb_num   int      default 0                 not null comment '点赞数',
    favour_num  int      default 0                 not null comment '收藏数',
    view_count  int      default 0                 not null comment '浏览量',
    is_draft    char(1)  default '1'               not null comment '文章状态， 1 草稿； 0 已发布',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (user_id),
    index idx_categoryId (category_id),
    index idx_createTime (create_time DESC)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id          bigint auto_increment comment 'id' primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (post_id),
    index idx_userId (user_id)
) comment '帖子点赞' collate = utf8mb4_unicode_ci;

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id          bigint auto_increment comment 'id' primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (post_id),
    index idx_userId (user_id)
) comment '帖子收藏' collate = utf8mb4_unicode_ci;

-- 帖子评论表
create table if not exists comment
(
    id              bigint auto_increment comment 'id' primary key,
    post_id         bigint                             not null comment '所属帖子 id',
    content         varchar(1024)                      not null comment '评论内容',
    root_comment_id bigint   default -1             not null comment '父评论 id, -1为根评论',
    thumb_num       int      default 0                 not null comment '点赞数',
    to_comment_id   int                               null     comment '回复目标评论 id',
    user_id         bigint                             not null comment '创建用户 id',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       tinyint  default 0                 not null comment '是否删除',
    index idx_postId (post_id),
    index idx_createTime (create_time)
) comment '帖子评论' collate = utf8mb4_unicode_ci;

-- 评论点赞表（硬删除）
create table if not exists comment_thumb
(
    id             bigint auto_increment comment 'id' primary key,
    comment_id     bigint                             not null comment '评论 id',
    user_id        bigint                             not null comment '创建用户 id',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_commentId (comment_id),
    index idx_userId (user_id)
) comment '评论点赞' collate = utf8mb4_unicode_ci;

-- 类别表
create table if not exists category
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(128)                       not null comment '类别名称',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除'
) comment '类别' collate = utf8mb4_unicode_ci;

-- 标签表
create table if not exists tag
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(128)                       not null comment '标签名称',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '标签' collate = utf8mb4_unicode_ci;