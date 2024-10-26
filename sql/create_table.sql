# 数据库初始化
# @author <a href="https://github.com/golfar">Golfar</a>

-- 创建库
create database if not exists golfar_blog;

-- 切换库
use golfar_blog;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    email        varchar(128)                           null comment '邮箱',
    phoneNumber  varchar(32)                            null comment '电话号码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    unique key idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    categoryId bigint                             not null comment '文章所属分类Id',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    viewCount  int      default 0                 not null comment '浏览量',
    isDraft    char(1)  default '1'               not null comment '文章状态， 1 草稿； 0 已发布',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index idx_categoryId (categoryId),
    index idx_createTime (createTime DESC)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞' collate = utf8mb4_unicode_ci;

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏' collate = utf8mb4_unicode_ci;

-- 帖子评论表
create table if not exists comment
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '所属帖子 id',
    content    varchar(1024)                      not null comment '评论内容',
    rootCommentId bigint   default -1             not null comment '父评论 id, -1为根评论',
    thumbNum   int      default 0                 not null comment '点赞数',
    toCommentId int                               null     comment '回复目标评论 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_postId (postId),
    index idx_createTime (createTime)
) comment '帖子评论' collate = utf8mb4_unicode_ci;

-- 评论点赞表（硬删除）
create table if not exists comment_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    commentId     bigint                             not null comment '评论 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_commentId (commentId),
    index idx_userId (userId)
) comment '评论点赞' collate = utf8mb4_unicode_ci;

-- 类别表
create table if not exists category
(
    id         bigint auto_increment comment 'id' primary key,
    `name`     varchar(128)                       not null comment '类别名称',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '类别' collate = utf8mb4_unicode_ci;

-- 标签表
create table if not exists tag
(
    id         bigint auto_increment comment 'id' primary key,
    `name`     varchar(128)                       not null comment '标签名称',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '标签' collate = utf8mb4_unicode_ci;