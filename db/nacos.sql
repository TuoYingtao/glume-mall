/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 21/10/2021 13:48:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'DEV', 'DEV_GROUP', 'config:\r\n    info: is DEV_GROUP Provid', '6972b63a4448d6f254afa77d8c0fe2fe', '2021-09-01 02:14:34', '2021-09-01 02:14:34', NULL, '0:0:0:0:0:0:0:1', '', 'b0a84036-41b8-45b5-ac9f-5a4ed8253024', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (5, 'cloudalibaba-sentinel-service', 'DEFAULT_GROUP', '[\n    {\n        \"resource\": \"/rateLimit/byUrl\",\n        \"limitApp\": \"default\",\n        \"grade\": \"1\",\n        \"count\": \"2\",\n        \"strategy\": \"0\",\n        \"controlBehavior\": \"0\",\n        \"clusterMode\": false\n    }\n]', 'b24e6beb4a53af5602712ec2c02a9b1d', '2021-09-03 03:28:14', '2021-09-03 03:34:56', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'json', '');
INSERT INTO `config_info` VALUES (9, 'glume-coupon.properties', 'DEFAULT_GROUP', 'coupon.user.age=21\ncoupon.user.name=XXX\n', 'ce5574de7d7f1b1a30f7398d1b4205d5', '2021-10-14 06:18:01', '2021-10-14 06:49:31', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'properties', '');
INSERT INTO `config_info` VALUES (19, 'glume-coupon.properties', 'DEFAULT_GROUP', 'coupon.user.age=21\r\ncoupon.user.name=张三', '065b72af3d63f6d4ab259694df882b16', '2021-10-14 06:48:44', '2021-10-14 06:48:44', NULL, '0:0:0:0:0:0:0:1', '', '49519571-80e9-4266-9205-f9013858ccb2', NULL, NULL, NULL, 'properties', NULL);
INSERT INTO `config_info` VALUES (21, 'glume-coupon.properties', 'DEFAULT_GROUP', 'coupon.user.age=21\ncoupon.user.name=tyt\n', '54d551955fcc868684dbc92170941e3b', '2021-10-14 06:52:29', '2021-10-14 06:52:39', 'nacos', '0:0:0:0:0:0:0:1', '', '2eecedd5-c791-439d-bd7d-c7180be61da6', '', '', '', 'properties', '');
INSERT INTO `config_info` VALUES (23, 'glume-coupon.properties', 'dev', 'coupon.user.age=21\ncoupon.user.name=Dev', 'eeac5baff6bc2d5e9cc7c5f21a206f5d', '2021-10-14 06:57:36', '2021-10-14 06:59:36', 'nacos', '0:0:0:0:0:0:0:1', '', '2eecedd5-c791-439d-bd7d-c7180be61da6', '', '', '', 'properties', '');
INSERT INTO `config_info` VALUES (24, 'glume-coupon.properties', 'provide', 'coupon.user.age=21\ncoupon.user.name=provide', '4c0387707a79c8d59e2a61db2ed52e18', '2021-10-14 06:59:09', '2021-10-14 06:59:51', 'nacos', '0:0:0:0:0:0:0:1', '', '2eecedd5-c791-439d-bd7d-c7180be61da6', '', '', '', 'properties', '');
INSERT INTO `config_info` VALUES (27, 'datasource.yaml', 'dev', 'spring:\r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    username: root\r\n    password: root\r\n    url: jdbc:mysql://localhost:3306/glumemall_sms?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8', 'd590a768288129af0190a27e2d22c7b1', '2021-10-14 07:22:23', '2021-10-14 07:22:23', NULL, '0:0:0:0:0:0:0:1', '', '2eecedd5-c791-439d-bd7d-c7180be61da6', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (28, 'mybatis.yaml', 'dev', 'mybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  global-config:\r\n    db-config:\r\n      id-type: auto #主键自增', '9e5ea3a80ee8b9ff346a1a3624b1a7ae', '2021-10-14 07:22:46', '2021-10-14 07:22:46', NULL, '0:0:0:0:0:0:0:1', '', '2eecedd5-c791-439d-bd7d-c7180be61da6', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (29, 'other.yaml', 'dev', 'server:\r\n  port: 7000\r\nspring:\r\n  profiles:\r\n    active: dev\r\n  application:\r\n    name: glume-coupon\r\n  cloud:\r\n    nacos:\r\n      discovery:\r\n        server-addr: localhost:8848', 'c1fea799e490fcbac1b320db2958719a', '2021-10-14 07:23:18', '2021-10-14 07:23:18', NULL, '0:0:0:0:0:0:0:1', '', '2eecedd5-c791-439d-bd7d-c7180be61da6', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (30, 'glume-gateway.yaml', 'dev', 'spring:\r\n    application:\r\n        name: glume-gateway', '59feff90e88e2d71dc37ef5105bb6fcb', '2021-10-14 07:50:28', '2021-10-14 07:50:28', NULL, '0:0:0:0:0:0:0:1', '', 'c0aa677b-6aad-4d3c-bf42-d431617f847a', NULL, NULL, NULL, 'yaml', NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 9, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.page=20', 'fef367a938d4f5ce30300c1dd40f2277', '2021-10-14 13:58:38', '2021-10-14 05:58:38', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (7, 10, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.page=20', 'fef367a938d4f5ce30300c1dd40f2277', '2021-10-14 14:16:18', '2021-10-14 06:16:19', NULL, '0:0:0:0:0:0:0:1', 'D', '');
INSERT INTO `his_config_info` VALUES (0, 11, 'bootstrapProperties-glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21', '34468e90405b3369f3b22da22c069daa', '2021-10-14 14:16:51', '2021-10-14 06:16:52', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 12, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21', '34468e90405b3369f3b22da22c069daa', '2021-10-14 14:18:00', '2021-10-14 06:18:01', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (8, 13, 'bootstrapProperties-glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21', '34468e90405b3369f3b22da22c069daa', '2021-10-14 14:19:27', '2021-10-14 06:19:28', NULL, '0:0:0:0:0:0:0:1', 'D', '');
INSERT INTO `his_config_info` VALUES (9, 14, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21', '34468e90405b3369f3b22da22c069daa', '2021-10-14 14:20:34', '2021-10-14 06:20:34', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 15, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=22', '5e6122712902cb4849452c9c41972cf5', '2021-10-14 14:22:37', '2021-10-14 06:22:37', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 16, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=张三', '8987d86469349402805d393ac25cc393', '2021-10-14 14:23:22', '2021-10-14 06:23:22', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 17, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=22\ncoupon.user.name=张三', 'a5556ebfc337587c020c6f5fe0c95be4', '2021-10-14 14:24:14', '2021-10-14 06:24:15', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 18, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=22\ncoupon.user.name=王二', 'd7540a0f6e621b7b9952cc23ec513fb6', '2021-10-14 14:26:16', '2021-10-14 06:26:16', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 19, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=张三', '8987d86469349402805d393ac25cc393', '2021-10-14 14:26:45', '2021-10-14 06:26:45', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 20, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=张三', '8987d86469349402805d393ac25cc393', '2021-10-14 14:26:49', '2021-10-14 06:26:50', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 21, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=张三', '8987d86469349402805d393ac25cc393', '2021-10-14 14:31:51', '2021-10-14 06:31:52', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 22, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=22\ncoupon.user.name=王二\n', 'ada95c6e11ebfb031af2c5d8cc131476', '2021-10-14 14:43:37', '2021-10-14 06:43:37', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 23, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\r\ncoupon.user.name=张三', '065b72af3d63f6d4ab259694df882b16', '2021-10-14 14:48:43', '2021-10-14 06:48:44', NULL, '0:0:0:0:0:0:0:1', 'I', '49519571-80e9-4266-9205-f9013858ccb2');
INSERT INTO `his_config_info` VALUES (9, 24, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=张三\n', '34545afcacac174a8ae9bdebf0fb4b00', '2021-10-14 14:49:31', '2021-10-14 06:49:31', 'nacos', '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 25, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=XXX\n', 'ce5574de7d7f1b1a30f7398d1b4205d5', '2021-10-14 14:52:28', '2021-10-14 06:52:29', NULL, '0:0:0:0:0:0:0:1', 'I', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (21, 26, 'glume-coupon.properties', 'DEFAULT_GROUP', '', 'coupon.user.age=21\ncoupon.user.name=XXX\n', 'ce5574de7d7f1b1a30f7398d1b4205d5', '2021-10-14 14:52:39', '2021-10-14 06:52:39', 'nacos', '0:0:0:0:0:0:0:1', 'U', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (0, 27, 'glume-coupon.properties', 'dev', '', 'coupon.user.age=21\r\ncoupon.user.name=tyt', '5780f9dc2f52c0d2f020b8b0d06830e3', '2021-10-14 14:57:36', '2021-10-14 06:57:36', NULL, '0:0:0:0:0:0:0:1', 'I', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (0, 28, 'glume-coupon.properties', 'provide', '', 'coupon.user.age=21\r\ncoupon.user.name=tyt', '5780f9dc2f52c0d2f020b8b0d06830e3', '2021-10-14 14:59:08', '2021-10-14 06:59:09', NULL, '0:0:0:0:0:0:0:1', 'I', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (23, 29, 'glume-coupon.properties', 'dev', '', 'coupon.user.age=21\r\ncoupon.user.name=tyt', '5780f9dc2f52c0d2f020b8b0d06830e3', '2021-10-14 14:59:36', '2021-10-14 06:59:36', 'nacos', '0:0:0:0:0:0:0:1', 'U', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (24, 30, 'glume-coupon.properties', 'provide', '', 'coupon.user.age=21\r\ncoupon.user.name=tyt', '5780f9dc2f52c0d2f020b8b0d06830e3', '2021-10-14 14:59:50', '2021-10-14 06:59:51', 'nacos', '0:0:0:0:0:0:0:1', 'U', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (0, 31, 'datasource.yaml', 'dev', '', 'spring:\r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    username: root\r\n    password: root\r\n    url: jdbc:mysql://localhost:3306/glumemall_sms?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8', 'd590a768288129af0190a27e2d22c7b1', '2021-10-14 15:22:23', '2021-10-14 07:22:23', NULL, '0:0:0:0:0:0:0:1', 'I', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (0, 32, 'mybatis.yaml', 'dev', '', 'mybatis-plus:\r\n  mapper-locations: classpath:/mapper/**/*.xml\r\n  global-config:\r\n    db-config:\r\n      id-type: auto #主键自增', '9e5ea3a80ee8b9ff346a1a3624b1a7ae', '2021-10-14 15:22:45', '2021-10-14 07:22:46', NULL, '0:0:0:0:0:0:0:1', 'I', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (0, 33, 'other.yaml', 'dev', '', 'server:\r\n  port: 7000\r\nspring:\r\n  profiles:\r\n    active: dev\r\n  application:\r\n    name: glume-coupon\r\n  cloud:\r\n    nacos:\r\n      discovery:\r\n        server-addr: localhost:8848', 'c1fea799e490fcbac1b320db2958719a', '2021-10-14 15:23:18', '2021-10-14 07:23:18', NULL, '0:0:0:0:0:0:0:1', 'I', '2eecedd5-c791-439d-bd7d-c7180be61da6');
INSERT INTO `his_config_info` VALUES (0, 34, 'glume-gateway.yaml', 'dev', '', 'spring:\r\n    application:\r\n        name: glume-gateway', '59feff90e88e2d71dc37ef5105bb6fcb', '2021-10-14 15:50:27', '2021-10-14 07:50:28', NULL, '0:0:0:0:0:0:0:1', 'I', 'c0aa677b-6aad-4d3c-bf42-d431617f847a');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'b0a84036-41b8-45b5-ac9f-5a4ed8253024', 'provid', 'provid', 'nacos', 1630462418407, 1630462418407);
INSERT INTO `tenant_info` VALUES (2, '1', '49519571-80e9-4266-9205-f9013858ccb2', 'dev', '开发环境', 'nacos', 1634194029326, 1634194029326);
INSERT INTO `tenant_info` VALUES (3, '1', 'b17ba112-2efc-4f1d-bd36-787b3304bcec', 'test', '测试环境', 'nacos', 1634194052222, 1634194052222);
INSERT INTO `tenant_info` VALUES (4, '1', '2eecedd5-c791-439d-bd7d-c7180be61da6', 'coupon', '优惠卷服务', 'nacos', 1634194336002, 1634194336002);
INSERT INTO `tenant_info` VALUES (5, '1', 'c0aa677b-6aad-4d3c-bf42-d431617f847a', 'gateway', 'API网关服务', 'nacos', 1634197611766, 1634197611766);
INSERT INTO `tenant_info` VALUES (6, '1', '93e4a05e-b268-4598-8f2d-3a47ff36edf9', 'product', '商品服务', 'nacos', 1634201499517, 1634201499517);
INSERT INTO `tenant_info` VALUES (7, '1', '510cd728-988e-4e39-9142-9ee7f8f7b383', 'admin', '后台系统服务', 'nacos', 1634268513303, 1634268513303);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
