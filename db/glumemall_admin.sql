/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : glumemall_admin

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 29/04/2022 18:29:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', '0/10 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME6', 'DEFAULT', '0/5 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME7', 'DEFAULT', '0/15 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 'com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution', '0', '1', '0', '0', 0x230D0A23546875204170722032382031343A34393A30342043535420323032320D0A5441534B5F50524F504552544945533D7B226265616E4E616D65225C3A224A6F6254657374222C22636F6E63757272656E74225C3A312C2263726561746554696D65225C3A313633343039353232333030302C2263726F6E45787072657373696F6E225C3A22302F3130202A202A202A202A203F222C22696E766F6B65546172676574225C3A226A6F62546573742E6A6F62506172616D732827746573742729222C226A6F6247726F7570225C3A2244454641554C54222C226A6F624964225C3A312C226A6F624E616D65225C3A225C75374346425C75374544465C75394544385C75384241345C75464630385C75363730395C75353343325C7546463039222C226D697366697265506F6C696379225C3A322C22706172616D73225C3A2274657374222C2272656D61726B225C3A225C75354239415C75363546365C75344546425C75353241315C75363730395C75353343325C75364434425C7538424435222C22737461747573225C3A307D0D0A);
INSERT INTO `qrtz_job_details` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME6', 'DEFAULT', NULL, 'com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution', '0', '1', '0', '0', 0x230D0A23546875204170722032382031373A30373A31322043535420323032320D0A5441534B5F50524F504552544945533D7B226265616E4E616D65225C3A224A6F6254657374222C22636F6E63757272656E74225C3A312C2263726561746554696D65225C3A313635313039333431373030302C2263726F6E45787072657373696F6E225C3A22302F35202A202A202A202A203F222C22696E766F6B65546172676574225C3A226A6F62546573742E6A6F624E6F506172616D73222C226A6F6247726F7570225C3A2244454641554C54222C226A6F624964225C3A362C226A6F624E616D65225C3A225C75374346425C75374544465C75394544385C75384241345C75464630385C75363545305C75353343325C7546463039222C226D697366697265506F6C696379225C3A312C226E65787456616C696454696D65225C3A313635313039333633353030302C22706172616D73225C3A22222C2272656D61726B225C3A225C75354239415C75363546365C75344546425C75353241315C75363545305C75353343325C75364434425C7538424435222C22737461747573225C3A317D0D0A);
INSERT INTO `qrtz_job_details` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME7', 'DEFAULT', NULL, 'com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution', '0', '1', '0', '0', 0x230D0A23546875204170722032382031343A34393A33312043535420323032320D0A5441534B5F50524F504552544945533D7B226265616E4E616D65225C3A224A6F6254657374222C22636F6E63757272656E74225C3A312C2263726561746554696D65225C3A313635313131323233323030302C2263726F6E45787072657373696F6E225C3A22302F3135202A202A202A202A203F222C22696E766F6B65546172676574225C3A226A6F62546573742E6A6F624D756C7469706C65506172616D732820275C75353443385C7535344338272C20747275652C2031304C2C2032352E35442C2031302029222C226A6F6247726F7570225C3A2244454641554C54222C226A6F624964225C3A372C226A6F624E616D65225C3A225C75374346425C75374544465C75394544385C75384241345C75464630385C75353931415C75353343325C7546463039222C226D697366697265506F6C696379225C3A332C22706172616D73225C3A22222C2272656D61726B225C3A225C75354239415C75363546365C75344546425C75353241315C75353931415C75353343325C75364434425C75384244355C5C6E222C22737461747573225C3A307D0D0A);
INSERT INTO `qrtz_job_details` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', NULL, 'io.renren.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000017C77AA50D87874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000672656E72656E74000CE58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RenrenScheduler', 'PC-20200917MKXU1634263655154', 1634268607517, 15000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int NULL DEFAULT NULL,
  `INT_PROP_2` int NULL DEFAULT NULL,
  `LONG_PROP_1` bigint NULL DEFAULT NULL,
  `LONG_PROP_2` bigint NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PRIORITY` int NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 1651129030000, 1651129020000, 5, 'PAUSED', 'CRON', 1651128544000, 0, NULL, 1, '');
INSERT INTO `qrtz_triggers` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME6', 'DEFAULT', 'TASK_CLASS_NAME6', 'DEFAULT', NULL, 1651136835000, -1, 5, 'PAUSED', 'CRON', 1651136832000, 0, NULL, -1, '');
INSERT INTO `qrtz_triggers` VALUES ('MyDefaultQuartzScheduler', 'TASK_CLASS_NAME7', 'DEFAULT', 'TASK_CLASS_NAME7', 'DEFAULT', NULL, 1651129035000, 1651129020000, 5, 'PAUSED', 'CRON', 1651128571000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', 'TASK_1', 'DEFAULT', NULL, 1634270400000, 1634268600000, 5, 'WAITING', 'CRON', 1634095628000, 0, NULL, 2, 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002E696F2E72656E72656E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000017C77AA50D87874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000174000672656E72656E74000CE58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '??????id',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `invoke_target` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????????????????',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'spring bean??????',
  `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron?????????',
  `misfire_policy` bigint NULL DEFAULT NULL COMMENT 'cron ???????????????0-?????? ???1-?????????????????????2-?????????????????????3-?????????????????????',
  `concurrent` bigint NULL DEFAULT NULL COMMENT '?????????????????????0-?????????1-??????',
  `status` tinyint NULL DEFAULT NULL COMMENT '????????????  0?????????  1?????????',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES (1, '????????????????????????', 'DEFAULT', 'jobTest.jobParams(\'test\')', 'JobTest', '0/10 * * * * ?', 2, 1, 1, 'test', '????????????????????????', '2021-10-13 11:20:23');
INSERT INTO `schedule_job` VALUES (6, '????????????????????????', 'DEFAULT', 'jobTest.jobNoParams', 'JobTest', '0/5 * * * * ?', 1, 1, 1, '', '????????????????????????', '2022-04-28 05:03:37');
INSERT INTO `schedule_job` VALUES (7, '????????????????????????', 'DEFAULT', 'jobTest.jobMultipleParams( \'??????\', true, 10L, 25.5D, 10 )', 'JobTest', '0/15 * * * * ?', 3, 1, 1, '', '????????????????????????\n', '2022-04-28 10:17:12');

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '????????????id',
  `job_id` bigint NOT NULL COMMENT '??????id',
  `status` tinyint NOT NULL COMMENT '????????????    0?????????    1?????????',
  `job_message` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `times` int NOT NULL COMMENT '??????(???????????????)',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '??????????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------
INSERT INTO `schedule_job_log` VALUES (1, 6, 1, '????????????????????????-????????????6??????', 'java.lang.reflect.InvocationTargetException\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:52)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:31)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution.doExecute(QuartzJobDisallowConcurrentExecution.java:17)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.AbstractQuartzJob.execute(AbstractQuartzJob.java:42)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\nCaused by: java.lang.ArithmeticException: / by zero\r\n	at com.glume.glumemall.admin.scheduled.quartz.test.JobTest.jobNoParams(JobTest.java:26)\r\n	... 10 more\r\n', 6, '2022-04-29 17:27:12', '2022-04-29 17:27:12', '2022-04-29 17:27:12');
INSERT INTO `schedule_job_log` VALUES (2, 6, 1, '????????????????????????-????????????3453??????', 'java.lang.reflect.InvocationTargetException\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:52)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:31)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution.doExecute(QuartzJobDisallowConcurrentExecution.java:17)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.AbstractQuartzJob.execute(AbstractQuartzJob.java:42)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\nCaused by: java.lang.ArithmeticException: / by zero\r\n	at com.glume.glumemall.admin.scheduled.quartz.test.JobTest.jobNoParams(JobTest.java:26)\r\n	... 10 more\r\n', 3453, '2022-04-29 17:28:55', '2022-04-29 17:28:59', '2022-04-29 17:28:59');
INSERT INTO `schedule_job_log` VALUES (3, 6, 1, '????????????????????????-????????????1??????', 'java.lang.reflect.InvocationTargetException\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:52)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:31)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution.doExecute(QuartzJobDisallowConcurrentExecution.java:17)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.AbstractQuartzJob.execute(AbstractQuartzJob.java:42)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\nCaused by: java.lang.ArithmeticException: / by zero\r\n	at com.glume.glumemall.admin.scheduled.quartz.test.JobTest.jobNoParams(JobTest.java:26)\r\n	... 10 more\r\n', 1, '2022-04-29 17:29:05', '2022-04-29 17:29:05', '2022-04-29 17:29:05');
INSERT INTO `schedule_job_log` VALUES (4, 7, 0, '????????????????????????-????????????5??????', NULL, 5, '2022-04-29 17:29:06', '2022-04-29 17:29:06', '2022-04-29 17:29:06');
INSERT INTO `schedule_job_log` VALUES (5, 7, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:07', '2022-04-29 17:29:07', '2022-04-29 17:29:07');
INSERT INTO `schedule_job_log` VALUES (6, 7, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:08', '2022-04-29 17:29:08', '2022-04-29 17:29:08');
INSERT INTO `schedule_job_log` VALUES (7, 1, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:09', '2022-04-29 17:29:09', '2022-04-29 17:29:09');
INSERT INTO `schedule_job_log` VALUES (8, 1, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:10', '2022-04-29 17:29:10', '2022-04-29 17:29:10');
INSERT INTO `schedule_job_log` VALUES (9, 1, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:11', '2022-04-29 17:29:11', '2022-04-29 17:29:11');
INSERT INTO `schedule_job_log` VALUES (10, 1, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:12', '2022-04-29 17:29:12', '2022-04-29 17:29:12');
INSERT INTO `schedule_job_log` VALUES (11, 1, 0, '????????????????????????-????????????1??????', NULL, 1, '2022-04-29 17:29:14', '2022-04-29 17:29:14', '2022-04-29 17:29:14');
INSERT INTO `schedule_job_log` VALUES (12, 1, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:14', '2022-04-29 17:29:14', '2022-04-29 17:29:14');
INSERT INTO `schedule_job_log` VALUES (13, 1, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:15', '2022-04-29 17:29:15', '2022-04-29 17:29:15');
INSERT INTO `schedule_job_log` VALUES (14, 6, 1, '????????????????????????-????????????1??????', 'java.lang.reflect.InvocationTargetException\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:52)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:31)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution.doExecute(QuartzJobDisallowConcurrentExecution.java:17)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.AbstractQuartzJob.execute(AbstractQuartzJob.java:42)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\nCaused by: java.lang.ArithmeticException: / by zero\r\n	at com.glume.glumemall.admin.scheduled.quartz.test.JobTest.jobNoParams(JobTest.java:26)\r\n	... 10 more\r\n', 1, '2022-04-29 17:29:17', '2022-04-29 17:29:17', '2022-04-29 17:29:17');
INSERT INTO `schedule_job_log` VALUES (15, 6, 1, '????????????????????????-????????????15??????', 'java.lang.reflect.InvocationTargetException\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:52)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:31)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution.doExecute(QuartzJobDisallowConcurrentExecution.java:17)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.AbstractQuartzJob.execute(AbstractQuartzJob.java:42)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\nCaused by: java.lang.ArithmeticException: / by zero\r\n	at com.glume.glumemall.admin.scheduled.quartz.test.JobTest.jobNoParams(JobTest.java:26)\r\n	... 10 more\r\n', 15, '2022-04-29 17:29:18', '2022-04-29 17:29:18', '2022-04-29 17:29:18');
INSERT INTO `schedule_job_log` VALUES (16, 6, 1, '????????????????????????-????????????0??????', 'java.lang.reflect.InvocationTargetException\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:52)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.JobInvokeUtil.invokeMethod(JobInvokeUtil.java:31)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.QuartzJobDisallowConcurrentExecution.doExecute(QuartzJobDisallowConcurrentExecution.java:17)\r\n	at com.glume.glumemall.admin.scheduled.quartz.job.AbstractQuartzJob.execute(AbstractQuartzJob.java:42)\r\n	at org.quartz.core.JobRunShell.run(JobRunShell.java:202)\r\n	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573)\r\nCaused by: java.lang.ArithmeticException: / by zero\r\n	at com.glume.glumemall.admin.scheduled.quartz.test.JobTest.jobNoParams(JobTest.java:26)\r\n	... 10 more\r\n', 0, '2022-04-29 17:29:19', '2022-04-29 17:29:19', '2022-04-29 17:29:19');
INSERT INTO `schedule_job_log` VALUES (17, 7, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:20', '2022-04-29 17:29:20', '2022-04-29 17:29:20');
INSERT INTO `schedule_job_log` VALUES (18, 7, 0, '????????????????????????-????????????1??????', NULL, 1, '2022-04-29 17:29:21', '2022-04-29 17:29:21', '2022-04-29 17:29:21');
INSERT INTO `schedule_job_log` VALUES (19, 7, 0, '????????????????????????-????????????1??????', NULL, 1, '2022-04-29 17:29:22', '2022-04-29 17:29:22', '2022-04-29 17:29:22');
INSERT INTO `schedule_job_log` VALUES (20, 7, 0, '????????????????????????-????????????0??????', NULL, 0, '2022-04-29 17:29:23', '2022-04-29 17:29:23', '2022-04-29 17:29:23');

-- ----------------------------
-- Table structure for sys_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `sys_blacklist`;
CREATE TABLE `sys_blacklist`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `kick_out_id` bigint NULL DEFAULT NULL COMMENT '?????????ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????????????????',
  `token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '????????????',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP??????',
  `login_out_time` datetime(0) NULL DEFAULT NULL COMMENT '??????????????????',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_blacklist
-- ----------------------------
INSERT INTO `sys_blacklist` VALUES (1, 1001, 'admin', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVUYWciOiJhZG1pbiIsImV4cCI6MTY0OTcyNzA1NSwidXNlcklkIjoxMDAxLCJpYXQiOjE2NDk2NDA2NTUsImp0aSI6ImVmNzgzYWUwLWRlMjQtNDY3My1iMWQxLWQ5ZmFlOWMyMGNjNyIsInVzZXJuYW1lIjoiYWRtaW4ifQ.pj9yhLjYjf5tGCsjYxzQKZGo89CjUteOp4AQtnLHoDpJ9w6TZfgJfY-F5gb9pYV1_HpuylvMyU5MUDuv354hsA', '192.168.50.116', '2022-04-11 11:36:50');
INSERT INTO `sys_blacklist` VALUES (2, 1001, 'user', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZVRhZyI6ImNvbW1vbiIsImV4cCI6MTY0OTczNDcxOCwidXNlcklkIjoxMDAyLCJpYXQiOjE2NDk2NDgzMTgsImp0aSI6IjZiNGI3Mjc2LWUxYmEtNDZhNS04ZDg5LTIyZDIyOTkxZjcwOSIsInVzZXJuYW1lIjoidXNlciJ9.tMFjb9oqnImKaXfVTL98aEC-ahncEqHI6y4qKSZggwB4ZJlMhfdbLqljJ-rVwm-IE7akVF9mKF0Ev7QuO2tKmA', '192.168.50.116', '2022-04-11 11:39:13');
INSERT INTO `sys_blacklist` VALUES (3, 1001, 'admin', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVUYWciOiJhZG1pbiIsImV4cCI6MTY1MDk2MzA0MywidXNlcklkIjoxMDAxLCJpYXQiOjE2NTA4NzY2NDMsImp0aSI6ImVlOGRlMjkwLWUzOTgtNGMwOC1iOTNiLTUzYjNmYjEzNzk3ZCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.rClvLer9M1WaFgDN3onJrwnLL5vvrVErgL89JyvuaWbe_9QluGCZWYs_GSHdmemEPXkCdiz6RpB9RB7SB6zRFw', '192.168.50.116', '2022-04-25 17:00:39');
INSERT INTO `sys_blacklist` VALUES (4, 1001, 'admin', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVUYWciOiJhZG1pbiIsImV4cCI6MTY1MDk2Mzc5MCwidXNlcklkIjoxMDAxLCJpYXQiOjE2NTA4NzczOTAsImp0aSI6ImJjMzI5ODI1LTFlNzMtNGY2OC1hNGVhLTA1N2E4NGI4NDMwMSIsInVzZXJuYW1lIjoiYWRtaW4ifQ.dapX3UTrqdxcDpHD0tRGXwQ6si5c-wo7nfl32GTtYowSSlZkUzJlbyPDmFoDPKAyBSofsTy1keoYXPjE1jqTfw', '192.168.50.116', '2022-04-25 17:27:48');

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '?????????',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '???????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------
INSERT INTO `sys_captcha` VALUES ('bb7206a5-8900-4241-875e-4a1abe6aabdd', '6cdnw', '2021-10-15 10:14:18');
INSERT INTO `sys_captcha` VALUES ('e5ae514e-0c2b-4a91-8e7c-e4362aee7092', 'a6ywx', '2021-10-15 10:13:45');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint NULL DEFAULT 1 COMMENT '??????   0?????????   1?????????',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '?????????????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', 0, '?????????????????????');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `time` bigint NOT NULL COMMENT '????????????(??????)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP??????',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_login`;
CREATE TABLE `sys_login`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????',
  `token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '????????????',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP??????',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????\r\n',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '???????????????',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '???????????????',
  `engine` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????????????????',
  `engine_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????????????????',
  `os` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????????????????',
  `platform` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login
-- ----------------------------
INSERT INTO `sys_login` VALUES (24, 'admin', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVUYWciOiJhZG1pbiIsImV4cCI6MTY1MTI4OTg1OCwidXNlcklkIjoxMDAxLCJpYXQiOjE2NTEyMDM0NTgsImp0aSI6IjEwZmY0ZjAyLTYwZjctNDQ5OS05NDE1LWU3MDFlY2E4ZWEyNCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.LE3-LjR9ErsKn2MZOCYSMr97IkYsAs5JKHWZYQCkpsUN4NkGb3pPL5X0RuDz1pWe48gzH-vsNHe-Q4TrzhcFag', '192.168.50.116', '2022-04-29 11:37:38', 'Chrome', '100.0.4896.127', 'Webkit', '537.36', 'Windows 10 or Windows Server 2016', 'Windows');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT NULL COMMENT '?????????ID??????????????????0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '???????????????0?????? 1?????????',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '???????????????0?????? 1?????????',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????(??????????????????????????????user:list,user:create)',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '???????????????M?????? C?????? F?????????',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '????????????',
  `order_num` int NULL DEFAULT 0 COMMENT '??????',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '????????????', 'sys', NULL, NULL, '0', '0', NULL, 'M', 'system', 0, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (2, 57, '???????????????', '/system/userInfo', 'system/userInfo/index', '', '0', '0', 'sys:userinfo:list', 'C', 'peoples', 3, 'admin', '2016-11-11 00:00:00', 'admin', '2022-04-11 00:00:00', '');
INSERT INTO `sys_menu` VALUES (3, 0, '????????????', '/roleManage', '', '', '0', '0', '', 'M', 'user', 2, 'admin', '2016-11-11 11:11:11', 'admin', '2022-04-11 00:00:00', '');
INSERT INTO `sys_menu` VALUES (4, 0, '????????????', '/menuManage', '', '', '0', '0', '', 'M', 'tree-table', 1, 'admin', '2016-11-11 00:00:00', 'admin', '2022-04-11 00:00:00', '');
INSERT INTO `sys_menu` VALUES (5, 58, 'SQL??????', '/monitor/druid', 'monitor/druid/index', '', '0', '0', 'sys:monitor:druid', 'C', 'druid', 98, 'admin', '2016-11-11 00:00:00', 'admin', '2022-04-13 00:00:00', '');
INSERT INTO `sys_menu` VALUES (6, 58, '????????????', '/monitor/jobSchedule', 'monitor/jobSchedule/index', '', '0', '0', 'sys:monitor:jobschedule', 'C', 'job', 99, 'admin', '2016-11-11 11:11:11', 'admin', '2022-04-26 00:00:00', '');
INSERT INTO `sys_menu` VALUES (7, 6, '??????', NULL, NULL, NULL, '0', '0', 'sys:schedule:list,sys:schedule:info', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (8, 6, '??????', NULL, NULL, NULL, '0', '0', 'sys:schedule:save', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (9, 6, '??????', NULL, NULL, NULL, '0', '0', 'sys:schedule:update', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (10, 6, '??????', NULL, NULL, NULL, '0', '0', 'sys:schedule:delete', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (11, 6, '??????', NULL, NULL, NULL, '0', '0', 'sys:schedule:pause', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (12, 6, '??????', NULL, NULL, NULL, '0', '0', 'sys:schedule:resume', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (13, 6, '????????????', NULL, NULL, NULL, '0', '0', 'sys:schedule:run', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (14, 6, '????????????', NULL, NULL, NULL, '0', '0', 'sys:schedule:log', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (15, 2, '??????', NULL, NULL, NULL, '0', '0', 'sys:user:list,sys:user:info', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (16, 2, '??????', NULL, NULL, NULL, '0', '0', 'sys:user:save,sys:role:select', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (17, 2, '??????', NULL, NULL, NULL, '0', '0', 'sys:user:update,sys:role:select', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (18, 2, '??????', NULL, NULL, NULL, '0', '0', 'sys:user:delete', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (19, 37, '??????', NULL, NULL, NULL, '0', '0', 'sys:role:list,sys:role:info', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (20, 37, '??????', NULL, NULL, NULL, '0', '0', 'sys:role:save,sys:menu:list', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (21, 37, '??????', NULL, NULL, NULL, '0', '0', 'sys:role:update,sys:menu:list', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (22, 37, '??????', NULL, NULL, NULL, '0', '0', 'sys:role:delete', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (23, 33, '??????', NULL, NULL, NULL, '0', '0', 'sys:menu:list,sys:menu:info', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (24, 33, '??????', NULL, NULL, NULL, '0', '0', 'sys:menu:save,sys:menu:select', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (25, 33, '??????', NULL, NULL, NULL, '0', '0', 'sys:menu:update,sys:menu:select', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (26, 33, '??????', NULL, NULL, NULL, '0', '0', 'sys:menu:delete', 'F', '#', 999, 'admin', '2016-11-11 11:11:11', 'admin', '2016-11-11 11:11:11', NULL);
INSERT INTO `sys_menu` VALUES (33, 4, '????????????', '/menuManage/list', 'menuManage/index', '', '0', '0', 'sys:menu:list', 'C', 'list', 6, 'admin', '2016-11-11 00:00:00', 'admin', '2021-11-06 00:00:00', '');
INSERT INTO `sys_menu` VALUES (37, 3, '????????????', '/role/list', 'roleManage/index', '', '0', '0', 'sys:role:lsit', 'C', 'user', 1, 'admin', '2021-11-06 00:00:00', 'admin', '2021-11-06 00:00:00', '');
INSERT INTO `sys_menu` VALUES (38, 0, '????????????', '/commodityManage', '', '', '0', '0', '', 'M', 'dianpu', 5, 'admin', '2021-11-09 14:49:38', 'admin', '2022-04-11 00:00:00', '');
INSERT INTO `sys_menu` VALUES (39, 38, '????????????', '/commodityManage/property/list', 'commodityManage/propertyManage/index', '', '0', '0', 'sys:property:list', 'C', 'input', 2, 'admin', '2021-11-09 14:51:33', 'admin', '2021-11-15 00:00:00', '');
INSERT INTO `sys_menu` VALUES (45, 38, '????????????', '/commodityManage/brandManage', 'commodityManage/brandManage/index', '', '0', '0', 'sys:brand:list', 'C', 'shopping', 1, 'admin', '2021-11-12 15:13:41', 'admin', '2021-11-12 00:00:00', '');
INSERT INTO `sys_menu` VALUES (47, 38, '????????????', '/commodityManage/commodityProperty/list', 'commodityManage/commodityProperty/index', NULL, '0', '0', 'sys:commodity:list', 'C', 'express-model', 3, 'admin', '2021-11-15 15:30:40', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (48, 0, '????????????', 'memberManage', '', '', '0', '0', '', 'M', 'peoples', 4, 'admin', '2021-11-18 22:04:38', 'admin', '2022-04-11 00:00:00', '');
INSERT INTO `sys_menu` VALUES (49, 48, '????????????', '/memberManage/memberLevel', 'memberManage/memberLevel/index', '', '0', '0', 'sys:memberlevel:list', 'C', 'tree', 1, 'admin', '2021-11-18 22:07:45', 'admin', '2021-11-18 00:00:00', '');
INSERT INTO `sys_menu` VALUES (50, 38, '????????????', '/commodityManage/issuedCommodity', 'commodityManage/issuedCommodity/index', '', '0', '0', 'sys:issuedcommodity:form', 'C', 'jiageprice1', 4, 'admin', '2021-11-19 14:21:17', 'admin', '2021-11-19 00:00:00', '');
INSERT INTO `sys_menu` VALUES (51, 0, '????????????', 'discountsManage', NULL, NULL, '0', '0', NULL, 'M', 'shopping', 6, 'admin', '2022-03-11 16:43:37', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (52, 51, '??????????????????', '/discountsManage/sessionSeckill', 'discountsManage/SessionSeckill/index', '', '0', '0', 'sys:sessionseckill:list', 'C', '', 2, 'admin', '2022-03-11 16:46:15', 'admin', '2022-03-12 00:00:00', '');
INSERT INTO `sys_menu` VALUES (53, 51, '??????????????????', '/discountsManage/dailyseckill', 'discountsManage/DailySeckill/index', NULL, '0', '0', 'sys:dailyseckill:list', 'C', '', 1, 'admin', '2022-03-12 15:09:09', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (54, 51, '??????????????????', '/discountsManage/goodsseckillrelation', 'discountsManage/GoodsSeckillRelation/index', '', '0', '0', 'sys:goodsseckillrelation:list', 'C', '', 3, 'admin', '2022-03-17 11:47:09', 'admin', '2022-03-17 00:00:00', '');
INSERT INTO `sys_menu` VALUES (56, 58, '??????????????????', '/monitor/onlineList/list', 'monitor/onlineList/index', '', '0', '0', 'sys:onlinelist:index', 'C', 'diannao-tianchong', 1, 'admin', '2022-04-09 16:27:54', 'admin', '2022-04-13 00:00:00', '');
INSERT INTO `sys_menu` VALUES (57, 0, '???????????????', '/system/userInfo', '', '', '0', '0', '', 'M', 'peoples', 3, 'admin', '2022-04-11 12:11:42', 'admin', '2022-04-11 00:00:00', '');
INSERT INTO `sys_menu` VALUES (58, 0, '?????????', '/monitor', NULL, NULL, '0', '0', NULL, 'M', 'monitor', 7, 'admin', '2022-04-13 14:44:26', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (59, 58, '????????????', '/monitor/jobsechdulelog', 'monitor/jobScheduleLog/index', '', '1', '0', 'sys:jobsechdulelog:list', 'C', 'log', 99, 'admin', '2022-04-29 11:05:37', 'admin', '2022-04-29 00:00:00', '');

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'URL??????',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '?????????ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `role_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '??????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2001, '???????????????', 'admin', 1001, '2021-10-15 22:09:44', 'admin');
INSERT INTO `sys_role` VALUES (2002, '????????????', '????????????', 1001, '2021-10-15 22:09:44', 'common');
INSERT INTO `sys_role` VALUES (2003, '????????????', '????????????', 1001, '2022-04-12 18:01:05', 'TestUser');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NULL DEFAULT NULL COMMENT '??????ID',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '??????ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 237 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '???????????????????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (108, 2002, 1);
INSERT INTO `sys_role_menu` VALUES (110, 2002, 2);
INSERT INTO `sys_role_menu` VALUES (111, 2002, 3);
INSERT INTO `sys_role_menu` VALUES (112, 2002, 4);
INSERT INTO `sys_role_menu` VALUES (113, 2002, 5);
INSERT INTO `sys_role_menu` VALUES (114, 2002, 6);
INSERT INTO `sys_role_menu` VALUES (175, 2001, 1);
INSERT INTO `sys_role_menu` VALUES (177, 2001, 2);
INSERT INTO `sys_role_menu` VALUES (178, 2001, 15);
INSERT INTO `sys_role_menu` VALUES (179, 2001, 16);
INSERT INTO `sys_role_menu` VALUES (180, 2001, 17);
INSERT INTO `sys_role_menu` VALUES (181, 2001, 18);
INSERT INTO `sys_role_menu` VALUES (182, 2001, 38);
INSERT INTO `sys_role_menu` VALUES (183, 2001, 39);
INSERT INTO `sys_role_menu` VALUES (184, 2001, 3);
INSERT INTO `sys_role_menu` VALUES (185, 2001, 37);
INSERT INTO `sys_role_menu` VALUES (186, 2001, 19);
INSERT INTO `sys_role_menu` VALUES (187, 2001, 20);
INSERT INTO `sys_role_menu` VALUES (188, 2001, 21);
INSERT INTO `sys_role_menu` VALUES (189, 2001, 22);
INSERT INTO `sys_role_menu` VALUES (190, 2001, 4);
INSERT INTO `sys_role_menu` VALUES (191, 2001, 33);
INSERT INTO `sys_role_menu` VALUES (192, 2001, 23);
INSERT INTO `sys_role_menu` VALUES (193, 2001, 24);
INSERT INTO `sys_role_menu` VALUES (194, 2001, 25);
INSERT INTO `sys_role_menu` VALUES (195, 2001, 26);
INSERT INTO `sys_role_menu` VALUES (196, 2001, 5);
INSERT INTO `sys_role_menu` VALUES (197, 2001, 6);
INSERT INTO `sys_role_menu` VALUES (198, 2001, 7);
INSERT INTO `sys_role_menu` VALUES (199, 2001, 8);
INSERT INTO `sys_role_menu` VALUES (200, 2001, 9);
INSERT INTO `sys_role_menu` VALUES (201, 2001, 10);
INSERT INTO `sys_role_menu` VALUES (202, 2001, 11);
INSERT INTO `sys_role_menu` VALUES (203, 2001, 12);
INSERT INTO `sys_role_menu` VALUES (204, 2001, 13);
INSERT INTO `sys_role_menu` VALUES (205, 2001, 14);
INSERT INTO `sys_role_menu` VALUES (211, 2001, 45);
INSERT INTO `sys_role_menu` VALUES (213, 2001, 47);
INSERT INTO `sys_role_menu` VALUES (214, 2001, 48);
INSERT INTO `sys_role_menu` VALUES (215, 2001, 49);
INSERT INTO `sys_role_menu` VALUES (216, 2001, 50);
INSERT INTO `sys_role_menu` VALUES (217, 2001, 51);
INSERT INTO `sys_role_menu` VALUES (218, 2001, 52);
INSERT INTO `sys_role_menu` VALUES (219, 2001, 53);
INSERT INTO `sys_role_menu` VALUES (220, 2001, 54);
INSERT INTO `sys_role_menu` VALUES (222, 2001, 56);
INSERT INTO `sys_role_menu` VALUES (223, 2001, 57);
INSERT INTO `sys_role_menu` VALUES (224, 2003, 1);
INSERT INTO `sys_role_menu` VALUES (225, 2003, 5);
INSERT INTO `sys_role_menu` VALUES (226, 2003, 6);
INSERT INTO `sys_role_menu` VALUES (227, 2003, 7);
INSERT INTO `sys_role_menu` VALUES (228, 2003, 8);
INSERT INTO `sys_role_menu` VALUES (229, 2003, 9);
INSERT INTO `sys_role_menu` VALUES (230, 2003, 10);
INSERT INTO `sys_role_menu` VALUES (231, 2003, 11);
INSERT INTO `sys_role_menu` VALUES (232, 2003, 12);
INSERT INTO `sys_role_menu` VALUES (233, 2003, 13);
INSERT INTO `sys_role_menu` VALUES (234, 2003, 14);
INSERT INTO `sys_role_menu` VALUES (235, 2001, 58);
INSERT INTO `sys_role_menu` VALUES (236, 2001, 59);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '?????????',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '???',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '?????????',
  `status` tinyint NULL DEFAULT 0 COMMENT '??????  0?????????   1?????????   2?????????',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '?????????ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1001, 'admin', '$2a$10$9zvuRVKXL5asr8OyR3XQB.nGGdjZplG1fb6Ef3aWaZA5hHZUfoVp2', 'YzcmCZNvbXocrsz9dm8e', 'tuoyingtao@163.com', '15233333333', 0, 1, '2016-11-11 11:11:11');
INSERT INTO `sys_user` VALUES (1002, 'user', '$2a$10$06mEvledBGQu/086v0ZC9eT39ViGR.jZ1kbi.fvfut7Vc1Rdp49.u', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', 0, 1, '2016-11-11 11:11:11');
INSERT INTO `sys_user` VALUES (1003, 'dytest', '$2a$10$DgJxOuvI4fLNti2s6vR2J.UHFSfAMqCGvpJej7DHHDetjxdDDdPvK', NULL, '15888888888@163.com', '15888888888', 0, 1001, '2022-04-12 18:22:29');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '??????ID',
  `role_id` bigint NULL DEFAULT NULL COMMENT '??????ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '???????????????????????????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1001, 2001);
INSERT INTO `sys_user_role` VALUES (2, 1002, 2002);
INSERT INTO `sys_user_role` VALUES (4, 1003, 2003);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '????????????Token' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (1, '14417ee29ff14896f18a631ee705bfd8', '2021-10-15 22:09:44', '2021-10-15 10:09:44');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '?????????',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '?????????',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '??????',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '??????' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'mark', '13612345678', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2017-03-23 22:37:41');

SET FOREIGN_KEY_CHECKS = 1;
