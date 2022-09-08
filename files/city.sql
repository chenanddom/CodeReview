/*
 Navicat Premium Data Transfer

 Source Server         : MySQL_Local_DB-8
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : user_db

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 08/09/2022 14:08:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`  (
  `id` int(0) NOT NULL,
  `parent_id` int(0) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES (1, 0, '广东省');
INSERT INTO `city` VALUES (2, 1, '广州市');
INSERT INTO `city` VALUES (3, 1, '深圳市区');
INSERT INTO `city` VALUES (4, 1, '珠海市');
INSERT INTO `city` VALUES (5, 3, '南山区');
INSERT INTO `city` VALUES (6, 3, '福田区');
INSERT INTO `city` VALUES (7, 3, '宝安区');
INSERT INTO `city` VALUES (8, 3, '龙岗区');
INSERT INTO `city` VALUES (9, 3, '龙华区');
INSERT INTO `city` VALUES (10, 3, '光明区');
INSERT INTO `city` VALUES (11, 3, '盐田区');
INSERT INTO `city` VALUES (12, 2, '天河区');
INSERT INTO `city` VALUES (13, 2, '越秀区');
INSERT INTO `city` VALUES (14, 2, '白云区');
INSERT INTO `city` VALUES (15, 2, '荔湾区');
INSERT INTO `city` VALUES (16, 2, '南沙区');
INSERT INTO `city` VALUES (17, 2, '黄埔军');
INSERT INTO `city` VALUES (18, 2, '海珠区');
INSERT INTO `city` VALUES (19, 2, '番禺');
INSERT INTO `city` VALUES (20, 2, '花都区');
INSERT INTO `city` VALUES (21, 2, '从化区');
INSERT INTO `city` VALUES (22, 2, '增城区');
INSERT INTO `city` VALUES (23, 5, '珠光村');
INSERT INTO `city` VALUES (24, 5, '西丽');
INSERT INTO `city` VALUES (25, 5, '桃园村');
INSERT INTO `city` VALUES (26, 5, '茶馆村');
INSERT INTO `city` VALUES (27, 6, '黄岗村');
INSERT INTO `city` VALUES (28, 6, '莲花山');

SET FOREIGN_KEY_CHECKS = 1;
