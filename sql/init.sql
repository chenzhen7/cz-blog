/*
 * cz-blog 建库脚本（面向全新部署）
 * ---------------------------------------------------------------------------
 * 生成方式：表结构由 `SHOW CREATE TABLE` 导出后拼装，**不要手工改 DDL**。
 *           改结构的正确做法是先改库、再用 target/_verify/gen_init.py 重新生成，
 *           同时把变更写进 sql/migration/ 下的迁移脚本。
 *           生成日期：2026-09-30
 *
 * 字符集：全库统一 utf8mb4 / utf8mb4_general_ci。
 *   刻意不用 MySQL 8.0 的默认 utf8mb4_0900_ai_ci，因为它是 8.0 专属，
 *   5.7 上不存在；选 general_ci 是为了两种版本都能装。
 *   历史包袱见 sql/migration/2026-09-30-normalize-utf8mb4-collation.sql。
 *
 * 数据：只含**部署引导数据**（站长信息、管理员账号、计数器初值、一条示例友链），
 *   不含任何文章/评论内容。这几行是从旧版 init.sql 原样继承的，不是从库里 dump 的。
 */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- --- t_blog ---
DROP TABLE IF EXISTS `t_blog`;


CREATE TABLE `t_blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) DEFAULT NULL COMMENT '分类ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '博客标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '注释',
  `description` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `first_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '博客首图',
  `commentabled` bit(1) NOT NULL COMMENT '是否开启评论',
  `appreciation` bit(1) NOT NULL COMMENT '是否开启赞赏',
  `published` bit(1) NOT NULL COMMENT '博客发布状态',
  `recommend` bit(1) NOT NULL COMMENT '是否推荐博客',
  `share_statement` bit(1) NOT NULL COMMENT '是否开启转载声明',
  `copyright` int(11) DEFAULT NULL COMMENT '版权',
  `views` int(11) DEFAULT '0' COMMENT '浏览数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论数',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `TITLE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_blog_tag ---
DROP TABLE IF EXISTS `t_blog_tag`;


CREATE TABLE `t_blog_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) DEFAULT NULL,
  `tag_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='标签表和文章表的中间表';


-- --- t_comment ---
DROP TABLE IF EXISTS `t_comment`;


CREATE TABLE `t_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `root_comment_id` bigint(20) DEFAULT NULL COMMENT '根评论ID，为空代表未根评论',
  `parent_comment_id` bigint(20) DEFAULT NULL COMMENT '父评论ID',
  `blog_id` bigint(20) DEFAULT NULL COMMENT '博客ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '内容',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论者的博客地址',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `admin_comment` bit(1) NOT NULL COMMENT '是否为管理员评论',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_friend ---
DROP TABLE IF EXISTS `t_friend`;


CREATE TABLE `t_friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blog_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '博客地址',
  `blog_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '博客名称',
  `blog_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '博客描述',
  `picture_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片地址',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审核通知邮箱地址',
  `net_status` int(4) DEFAULT NULL COMMENT '友链网络状态【0.网络状态良好 1.状态一般 2.网络异常 3.网络超时】',
  `status` int(2) DEFAULT '0' COMMENT '审核状态【0.审核中 1.通过 -1.不通过】',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审核失败理由',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- t_friend 的部署引导数据
INSERT INTO `t_friend` VALUES (6, 'http://www.chenzhen.space/', 'chenzhen的博客', NULL, 'http://www.chenzhen.space/images/me.jpg', NULL, NULL, 1, NULL, '2022-09-17 17:11:18', NULL);


-- --- t_message ---
DROP TABLE IF EXISTS `t_message`;


CREATE TABLE `t_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '留言内容',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '留言者博客地址',
  `root_message_id` bigint(20) DEFAULT NULL COMMENT '根留言ID',
  `parent_message_id` bigint(20) DEFAULT NULL COMMENT '父留言ID',
  `admin_message` bit(1) NOT NULL COMMENT '是否为管理员留言',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_music ---
DROP TABLE IF EXISTS `t_music`;


CREATE TABLE `t_music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '音乐标题',
  `artist` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '艺术家或歌手名',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '音乐文件路径',
  `cover_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '封面路径',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_prompt ---
DROP TABLE IF EXISTS `t_prompt`;


CREATE TABLE `t_prompt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '翻译后的prompt',
  `original_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '原Prompt',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `recommend` bit(1) DEFAULT b'0' COMMENT '是否推荐 0不是 1是',
  `published` bit(1) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `views` int(10) unsigned DEFAULT '0' COMMENT '被浏览次数',
  `prompt_usage_count` int(10) unsigned DEFAULT '0' COMMENT '被使用次数',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_prompt_category ---
DROP TABLE IF EXISTS `t_prompt_category`;


CREATE TABLE `t_prompt_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_site ---
DROP TABLE IF EXISTS `t_site`;


CREATE TABLE `t_site` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `thumb` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=671 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_site_category ---
DROP TABLE IF EXISTS `t_site_category`;


CREATE TABLE `t_site_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_sys_config ---
DROP TABLE IF EXISTS `t_sys_config`;


CREATE TABLE `t_sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站长名称',
  `about_me_introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '关于我页面-自我介绍',
  `about_me_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '关于我页面-内容',
  `about_me_skill` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '关于我页面-我的技能',
  `site_profile` text COLLATE utf8mb4_general_ci COMMENT '站点概览-个人简介',
  `site_location` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点概览-位置',
  `site_email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点概览-邮箱',
  `site_qq` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点概览-QQ',
  `site_wechat` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点概览-微信',
  `csdn_session` text COLLATE utf8mb4_general_ci COMMENT 'CSDN的Session',
  `baidu_link_submit_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '在百度搜索资源平台申请的推送用的准入密钥',
  `baidu_link_submit_site` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '在百度搜索资源平台验证的站点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置表';


-- t_sys_config 的部署引导数据
INSERT INTO `t_sys_config` VALUES (10, 'ChenZhen', '我是陈震，欢迎来到我的博客！我热爱写作、探索技术和分享知识。', '<p>做这个博客的初衷是为了找一个练手的springboot项目，而且当时看别人都有个人博客，自己也眼红了，于是便着手搭建博客。\n<p>博客就相当于自己的学习笔记一样，将自己学到的东西写成博文的方式记录下来，可以加深你对知识点的理解，记录足迹，反映成长。写博客实际上是记录一个人思考和解决问题的成长过程。很久之后，你再翻看自己以前写的文章，你会感觉得到自己的变化和进步。</p>\n<p>技术是很容易遗忘的，你学习完一门新的技术，很可能现在懂了，但是过一段时间没用很快就会遗忘，当你把你学习的内容写成博客，不仅可以加深记忆和理解，如果遗忘了还可以回头看看，也能帮助和你一样处于同一阶段的人。</p>\n', '面向GPT编程,CSS,JavaScript,撒打算', '酷爱Jpop、纯音、ACG、电子音乐，如果你也听：安田丽、泽野弘之、尾浦由纪、ClariS、桑田佳佑、YOASOBI的歌，说明我们品味很相似。\n\n最崇拜的作曲家：a_hisa、神前晓。\n\n喜欢玩lol，最拿手的是上单，一区ID：IGTheXian丶（可以随时找我开黑）。\n\n本项目开源：\n<a href=\"https://github.com/chenzhen7/cz-blog\" class=\"cz-text-green\" target=\"_blank\">https://github.com/chenzhen7/cz-blog</a> \n请多多Star支持', '揭阳', '1583296383', '158329638', 'ChenZhen_7', NULL, NULL, NULL);


-- --- t_sys_log ---
DROP TABLE IF EXISTS `t_sys_log`;


CREATE TABLE `t_sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
  `content` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志内容',
  `params` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求参数',
  `os` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器类型',
  `spider_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '爬虫类型',
  `request_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求URL',
  `referer` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '引荐来源',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1381 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_tag ---
DROP TABLE IF EXISTS `t_tag`;


CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签名',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --- t_type ---
DROP TABLE IF EXISTS `t_type`;


CREATE TABLE `t_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;


-- --- t_user ---
DROP TABLE IF EXISTS `t_user`;


CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名/账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮件',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- t_user 的部署引导数据
INSERT INTO `t_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'chenzhen', '1583296383@qq.com', '/images/me.jpg', '2022-09-11 20:47:02', '2022-09-11 20:47:06');


-- --- t_views ---
DROP TABLE IF EXISTS `t_views`;


CREATE TABLE `t_views` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_views` bigint(20) unsigned DEFAULT NULL COMMENT '网站的总浏览量（包含所有文章及页面的浏览量）',
  `yesterday_views` bigint(20) DEFAULT NULL COMMENT '昨天的总浏览量',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- t_views 的部署引导数据
INSERT INTO `t_views` VALUES (3, 0, 0, '2024-02-23 16:52:52', '2024-06-09 23:51:36');


-- --- t_white_list ---
DROP TABLE IF EXISTS `t_white_list`;


CREATE TABLE `t_white_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
  `status` int(4) DEFAULT NULL COMMENT '状态【-1.禁止访问  1.允许访问】',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
