/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : cz-blog

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 15/07/2024 22:14:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_config`;
CREATE TABLE `t_sys_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长名称',
  `about_me_introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '关于我页面-自我介绍',
  `about_me_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '关于我页面-内容',
  `about_me_skill` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '关于我页面-我的技能',
  `site_profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '站点概览-个人简介',
  `site_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点概览-位置',
  `site_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点概览-邮箱',
  `site_qq` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点概览-QQ',
  `site_wechat` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点概览-微信',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_config
-- ----------------------------
INSERT INTO `t_sys_config` VALUES (10, 'ChenZhen', '我是陈震，欢迎来到我的博客！我热爱写作、探索技术和分享知识。', '<p>做这个博客的初衷是为了找一个练手的springboot项目，而且当时看别人都有个人博客，自己也眼红了，于是便着手搭建博客。\n<p>博客就相当于自己的学习笔记一样，将自己学到的东西写成博文的方式记录下来，可以加深你对知识点的理解，记录足迹，反映成长。写博客实际上是记录一个人思考和解决问题的成长过程。很久之后，你再翻看自己以前写的文章，你会感觉得到自己的变化和进步。</p>\n<p>技术是很容易遗忘的，你学习完一门新的技术，很可能现在懂了，但是过一段时间没用很快就会遗忘，当你把你学习的内容写成博客，不仅可以加深记忆和理解，如果遗忘了还可以回头看看，也能帮助和你一样处于同一阶段的人。</p>\n', '面向GPT编程,CSS,JavaScript,撒打算', '酷爱Jpop、纯音、ACG、电子音乐，如果你也听：安田丽、泽野弘之、尾浦由纪、ClariS、桑田佳佑、YOASOBI的歌，说明我们品味很相似。\n\n最崇拜的作曲家：a_hisa、神前晓。\n\n喜欢玩lol，最拿手的是上单，一区ID：IGTheXian丶（可以随时找我开黑）。\n\n本项目开源：\n<a href=\"https://github.com/chenzhen7/cz-blog\" class=\"cz-text-green\" target=\"_blank\">https://github.com/chenzhen7/cz-blog</a> \n请多多Star支持', '揭阳', '1583296383', '158329638', 'ChenZhen_7');

SET FOREIGN_KEY_CHECKS = 1;
