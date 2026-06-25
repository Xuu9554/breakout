/*
 Navicat Premium Data Transfer

 Source Server         : Xu
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : game

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 08/06/2021 20:27:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `record_id`   varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci   NOT NULL
  COMMENT '记录id',
  `user_id`     varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL DEFAULT NULL
  COMMENT '用户id',
  `grade`       int(255)    NULL DEFAULT NULL
  COMMENT '成绩',
  `create_time` datetime(0) NULL DEFAULT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`record_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for setting_record
-- ----------------------------
DROP TABLE IF EXISTS `setting_record`;
CREATE TABLE `setting_record` (
  `setting_id`  varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL
  COMMENT '主键id',
  `user_id`     varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL DEFAULT NULL
  COMMENT '用户id',
  `ball_life`   int(255)  NULL DEFAULT NULL
  COMMENT '小球生命',
  `ball_size`   int(11)   NULL DEFAULT NULL
  COMMENT '小球大小',
  `period`      int(255)  NULL DEFAULT NULL
  COMMENT '刷新频率',
  `brick_count` int(255)  NULL DEFAULT NULL
  COMMENT '通关块数',
  PRIMARY KEY (`setting_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id`     varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci   NOT NULL
  COMMENT '用户id',
  `user_name`   varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL DEFAULT NULL
  COMMENT '用户姓名',
  `pass_word`   varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL DEFAULT NULL
  COMMENT '用户密码',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_login
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `login_id`     varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci    NOT NULL
  COMMENT '主键',
  `user_id`      varchar(255) CHARACTER SET utf8
  COLLATE utf8_general_ci    NULL DEFAULT NULL
  COMMENT '用户id',
  `login_time`   datetime(0) NULL DEFAULT NULL
  COMMENT '登陆时间',
  `expired_time` datetime(0) NULL DEFAULT NULL
  COMMENT '退出时间',
  PRIMARY KEY (`login_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
