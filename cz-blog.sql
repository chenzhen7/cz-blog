/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.11 : Database - cz-blog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cz-blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

/*Table structure for table `t_blog` */

DROP TABLE IF EXISTS `t_blog`;

CREATE TABLE `t_blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '博客标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '注释',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `first_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '博客首图',
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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `t_blog` */

/*Table structure for table `t_blog_tag` */

DROP TABLE IF EXISTS `t_blog_tag`;

CREATE TABLE `t_blog_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) DEFAULT NULL,
  `tag_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表和文章表的中间表';

/*Data for the table `t_blog_tag` */

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `root_comment_id` bigint(20) DEFAULT NULL COMMENT '根评论ID，为空代表未根评论',
  `parent_comment_id` bigint(20) DEFAULT NULL COMMENT '父评论ID',
  `blog_id` bigint(20) DEFAULT NULL COMMENT '博客ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '内容',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论者的博客地址',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `admin_comment` bit(1) NOT NULL COMMENT '是否为管理员评论',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

/*Data for the table `t_comment` */

/*Table structure for table `t_friend` */

DROP TABLE IF EXISTS `t_friend`;

CREATE TABLE `t_friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blog_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '博客地址',
  `blog_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '博客名称',
  `blog_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '博客描述',
  `picture_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片地址',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核通知邮箱地址',
  `status` int(2) DEFAULT '0' COMMENT '审核状态【0.审核中 1.通过 -1.不通过】',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核失败理由',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

/*Data for the table `t_friend` */

insert  into `t_friend`(`id`,`blog_address`,`blog_name`,`blog_description`,`picture_address`,`email`,`status`,`reason`,`create_time`,`update_time`) values 
(6,'http://www.chenzhen.space/','chenzhen的博客',NULL,'http://www.chenzhen.space/images/me.jpg',NULL,1,NULL,'2022-09-17 17:11:18',NULL);

/*Table structure for table `t_message` */

DROP TABLE IF EXISTS `t_message`;

CREATE TABLE `t_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '留言内容',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '留言者博客地址',
  `root_message_id` bigint(20) DEFAULT NULL COMMENT '根留言ID',
  `parent_message_id` bigint(20) DEFAULT NULL COMMENT '父留言ID',
  `admin_message` bit(1) NOT NULL COMMENT '是否为管理员留言',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8;

/*Data for the table `t_message` */

/*Table structure for table `t_music` */

DROP TABLE IF EXISTS `t_music`;

CREATE TABLE `t_music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '音乐标题',
  `artist` varchar(255) DEFAULT NULL COMMENT '艺术家或歌手名',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(255) DEFAULT NULL COMMENT '音乐文件路径',
  `cover_path` varchar(255) DEFAULT NULL COMMENT '封面路径',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_music` */

/*Table structure for table `t_sys_config` */

DROP TABLE IF EXISTS `t_sys_config`;

CREATE TABLE `t_sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参数名',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '参数值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';

/*Data for the table `t_sys_config` */

insert  into `t_sys_config`(`id`,`name`,`value`,`description`,`create_time`,`update_time`) values 
(1,'about_me_introduction','我是陈震，欢迎来到我的博客！我热爱写作、探索技术和分享知识。','关于我页面-自我介绍','2024-01-20 13:08:30','2024-01-20 13:08:30'),
(2,'about_me_content','<h4 class=\"fw-bold pb-3\">初衷</h4>\n<p>做这个博客的初衷是为了找一个练手的springboot项目，而且当时看别人都有个人博客，自己也眼红了，于是便着手搭建博客。</p>\n<p>博客就相当于自己的学习笔记一样，将自己学到的东西写成博文的方式记录下来，可以加深你对知识点的理解，记录足迹，反映成长。写博客实际上是记录一个人思考和解决问题的成长过程。很久之后，你再翻看自己以前写的文章，你会感觉得到自己的变化和进步。</p>\n<p>技术是很容易遗忘的，你学习完一门新的技术，很可能现在懂了，但是过一段时间没用很快就会遗忘，当你把你学习的内容写成博客，不仅可以加深记忆和理解，如果遗忘了还可以回头看看，也能帮助和你一样处于同一阶段的人。</p>\n<h4 class=\"fw-bold pb-3\">关于我</h4>','关于我页面-内容','2024-01-20 13:14:48','2024-01-20 13:14:48'),
(3,'about_me_tag','面向GPT编程,CSS,JavaScript,Python\n','关于我页面-我的标签','2024-01-20 13:21:13','2024-01-20 13:21:13'),
(4,'author','ChenZhen','站长名称','2024-01-20 13:57:22','2024-01-20 13:57:22'),
(5,'site_profile','这个人不懒，却什么都没有写.','站点概览-个人简介','2024-01-20 14:21:50','2024-01-20 14:21:50'),
(6,'site_location','揭阳','站点概览-位置','2024-01-20 14:22:43','2024-01-20 14:22:43'),
(7,'site_email','1583296383@qq.com','站点概览-邮箱','2024-01-20 14:23:12','2024-01-20 14:23:12'),
(8,'site_qq','1583296383','站点概览-QQ','2024-01-20 14:23:27','2024-01-20 14:23:27'),
(9,'site_wechat','ChenZhen_7','站点概览-微信','2024-01-20 14:23:50','2024-01-20 14:23:50');

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签名',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_tag` */

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名/账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮件',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`nickname`,`email`,`avatar`,`create_time`,`update_time`) values 
(1,'admin','e10adc3949ba59abbe56e057f20f883e','chenzhen','1583296383@qq.com','/images/me.jpg','2022-09-11 20:47:02','2022-09-11 20:47:06');

/*Table structure for table `t_views` */

DROP TABLE IF EXISTS `t_views`;

CREATE TABLE `t_views` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `total_views` bigint(20) unsigned DEFAULT NULL COMMENT '网站的总浏览量（包含所有文章及页面的浏览量）',
  `yesterday_views` bigint(20) DEFAULT NULL COMMENT '昨天的总浏览量',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_views` */

insert  into `t_views`(`id`,`total_views`,`yesterday_views`,`create_time`,`update_time`) values 
(3,22,5,'2024-02-23 16:52:52','2024-02-24 01:08:56');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
