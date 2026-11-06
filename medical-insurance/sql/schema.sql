-- 东软医疗保险报销系统 · 建库建表脚本
-- 生成依据：
--   1) docs/第5组_东软医疗保险报销系统_数据库设计文档.docx（表结构）
--   2) 后端实体 com.cqupt.health_insurance.pojo（列名以 MyBatis-Plus 默认映射「驼峰→下划线」为准）
-- 说明：
--   * 价格字段按 BigDecimal 用 decimal(10,2)
--   * 外键仅注释标注，未加 FOREIGN KEY 约束（与项目现状一致，避免插入顺序报错）
--   * 疾病表 ICD 列名：文档写 diseaseICD，后端实体字段为 diseaseicd（全小写），此处取 diseaseicd

CREATE DATABASE IF NOT EXISTS `health_insurance` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `health_insurance`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 诊疗项目表 diagnosis_treatment
-- ----------------------------
DROP TABLE IF EXISTS `diagnosis_treatment`;
CREATE TABLE `diagnosis_treatment` (
  `id`              INT           NOT NULL AUTO_INCREMENT COMMENT '主键',
  `medical_type`    VARCHAR(10)   DEFAULT NULL COMMENT '项目类别',
  `medical_number`  VARCHAR(25)   DEFAULT NULL COMMENT '项目编码',
  `country_number`  VARCHAR(100)  DEFAULT NULL COMMENT '项目国家编码',
  `medical_name`    VARCHAR(255)  DEFAULT NULL COMMENT '项目名称',
  `medical_info`    VARCHAR(255)  DEFAULT NULL COMMENT '项目说明',
  `medical_exclude` VARCHAR(255)  DEFAULT NULL COMMENT '除外内容',
  `medical_unit`    VARCHAR(10)   DEFAULT NULL COMMENT '计价单位',
  `medical_price`   DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `remark`          VARCHAR(255)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='诊疗项目表';

-- ----------------------------
-- 2. 药品信息表 drug_info
-- ----------------------------
DROP TABLE IF EXISTS `drug_info`;
CREATE TABLE `drug_info` (
  `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
  `insurance_type`   VARCHAR(10)   DEFAULT NULL COMMENT '报销类别',
  `china_name`       VARCHAR(255)  DEFAULT NULL COMMENT '药品中文名称',
  `goods_name`       VARCHAR(255)  DEFAULT NULL COMMENT '药品商品名称',
  `specifications`   VARCHAR(255)  DEFAULT NULL COMMENT '规格',
  `drug_unit`        VARCHAR(10)   DEFAULT NULL COMMENT '药品单位',
  `drug_manufacturer` VARCHAR(255) DEFAULT NULL COMMENT '生产企业',
  `drug_price`       DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `remarks`          VARCHAR(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品信息表';

-- ----------------------------
-- 3. 药品报销等级信息表 drug_reimbursement
-- ----------------------------
DROP TABLE IF EXISTS `drug_reimbursement`;
CREATE TABLE `drug_reimbursement` (
  `id`                           INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `drug_reimbursement_type`      VARCHAR(20)  DEFAULT NULL COMMENT '药品报销级别',
  `drug_reimbursement_proportion` INT          DEFAULT NULL COMMENT '药品报销比例',
  `drug_reimbursement_info`      VARCHAR(255) DEFAULT NULL COMMENT '药品报销等级说明',
  `status`                       TINYINT      DEFAULT NULL COMMENT '状态：1-正常 0-禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品报销等级信息表';

-- ----------------------------
-- 4. 医院报销等级信息表 hospital_reimbursement
-- ----------------------------
DROP TABLE IF EXISTS `hospital_reimbursement`;
CREATE TABLE `hospital_reimbursement` (
  `id`             INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `min_pay_level`  INT          DEFAULT NULL COMMENT '报销起付金额',
  `max_pay_level`  INT          DEFAULT NULL COMMENT '报销等级线',
  `people_type`    TINYINT      DEFAULT NULL COMMENT '人员类别(1在职人员，0退休人员)',
  `pay_proportion` INT          DEFAULT NULL COMMENT '报销比例',
  `hospital_level` VARCHAR(255) DEFAULT NULL COMMENT '医院等级',
  `status`         TINYINT      DEFAULT NULL COMMENT '状态：1-正常 0-禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院报销等级信息表';

-- ----------------------------
-- 5. 医疗服务设施表 medical_service
-- ----------------------------
DROP TABLE IF EXISTS `medical_service`;
CREATE TABLE `medical_service` (
  `id`              INT           NOT NULL AUTO_INCREMENT COMMENT '主键',
  `medical_type`    VARCHAR(10)   DEFAULT NULL COMMENT '项目类别',
  `medical_number`  VARCHAR(25)   DEFAULT NULL COMMENT '项目编码',
  `country_number`  VARCHAR(100)  DEFAULT NULL COMMENT '项目国家编码',
  `medical_name`    VARCHAR(255)  DEFAULT NULL COMMENT '项目名称',
  `medical_info`    VARCHAR(255)  DEFAULT NULL COMMENT '项目说明',
  `medical_exclude` VARCHAR(255)  DEFAULT NULL COMMENT '除外内容',
  `medical_unit`    VARCHAR(10)   DEFAULT NULL COMMENT '计价单位',
  `medical_price`   DECIMAL(10,2) DEFAULT NULL COMMENT '价格',
  `remark`          VARCHAR(255)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医疗服务设施表';

-- ----------------------------
-- 6. 用户信息表 sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`           INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name`   VARCHAR(64)  DEFAULT NULL COMMENT '登录名',
  `user_name`    VARCHAR(64)  DEFAULT NULL COMMENT '实际名称',
  `gender`       TINYINT      DEFAULT NULL COMMENT '性别：1-男 2-女',
  `password`     VARCHAR(100) DEFAULT NULL COMMENT '密码',
  `avatar`       VARCHAR(255) DEFAULT NULL COMMENT '用户头像',
  `mobile`       VARCHAR(20)  DEFAULT NULL COMMENT '联系方式',
  `status`       TINYINT      DEFAULT NULL COMMENT '用户类型：1-管理 2-医院 3-报销',
  `email`        VARCHAR(128) DEFAULT NULL COMMENT '用户邮箱',
  `deleted`      TINYINT      DEFAULT NULL COMMENT '逻辑删除标识：0-未删除；1-已删除',
  `gmt_create`   DATETIME     DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 7. 住院患者信息表 patient_registration
-- ----------------------------
DROP TABLE IF EXISTS `patient_registration`;
CREATE TABLE `patient_registration` (
  `id`            INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `case_number`   VARCHAR(20)  DEFAULT NULL COMMENT '住院号',
  `real_name`     VARCHAR(50)  DEFAULT NULL COMMENT '患者姓名',
  `gender`        CHAR(2)      DEFAULT NULL COMMENT '性别',
  `card_number`   VARCHAR(20)  DEFAULT NULL COMMENT '身份证号',
  `birthday`      DATE         DEFAULT NULL COMMENT '出生日期',
  `age`           INT          DEFAULT NULL COMMENT '年龄',
  `age_type`      CHAR(2)      DEFAULT NULL COMMENT '年龄类型（岁、月、日）',
  `home_address`  VARCHAR(255) DEFAULT NULL COMMENT '住址',
  `visit_date`    DATETIME     DEFAULT NULL COMMENT '入院日期',
  `regist_method` VARCHAR(20)  DEFAULT NULL COMMENT '结算类别（自费、医保）',
  `work_status`   VARCHAR(20)  DEFAULT NULL COMMENT '工作状态（在职、退休）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='住院患者信息表';

-- ----------------------------
-- 8. 患者医嘱药品信息表 inpatient_drug
-- ----------------------------
DROP TABLE IF EXISTS `inpatient_drug`;
CREATE TABLE `inpatient_drug` (
  `id`           INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `start_time`   DATETIME     DEFAULT NULL COMMENT '起始时间',
  `end_time`     DATETIME     DEFAULT NULL COMMENT '结束时间',
  `doctor_order` VARCHAR(255) DEFAULT NULL COMMENT '医嘱内容',
  `order_number` INT          DEFAULT NULL COMMENT '医嘱频次（次/天）',
  `use_method`   VARCHAR(255) DEFAULT NULL COMMENT '用法',
  `drug_id`      INT          DEFAULT NULL COMMENT '药品表外键',
  `patient_id`   INT          DEFAULT NULL COMMENT '患者表外键',
  `status`       INT          DEFAULT NULL COMMENT '医嘱状态：1-正常执行 0-作废 2-停止',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者医嘱药品信息表';

-- ----------------------------
-- 9. 患者医嘱诊疗项目信息表 inpatient_diagnosis
-- ----------------------------
DROP TABLE IF EXISTS `inpatient_diagnosis`;
CREATE TABLE `inpatient_diagnosis` (
  `id`           INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_time`   DATETIME     DEFAULT NULL COMMENT '开立时间',
  `doctor_order` VARCHAR(255) DEFAULT NULL COMMENT '医生医嘱诊疗项目信息',
  `use_method`   VARCHAR(255) DEFAULT NULL COMMENT '用法详情说明',
  `diagnosis_id` INT          DEFAULT NULL COMMENT '诊疗项目表外键',
  `status`       INT          DEFAULT NULL COMMENT '医嘱状态：1-正常执行 0-作废 2-停止',
  `patient_id`   INT          DEFAULT NULL COMMENT '患者表外键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者医嘱诊疗项目信息表';

-- ----------------------------
-- 10. 患者医嘱医疗服务项目信息表 inpatient_medical
-- ----------------------------
DROP TABLE IF EXISTS `inpatient_medical`;
CREATE TABLE `inpatient_medical` (
  `id`           INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_time`   DATETIME     DEFAULT NULL COMMENT '开立时间',
  `doctor_order` VARCHAR(255) DEFAULT NULL COMMENT '医嘱内容',
  `use_method`   VARCHAR(255) DEFAULT NULL COMMENT '用法详情说明',
  `medical_id`   INT          DEFAULT NULL COMMENT '医疗服务表外键',
  `status`       INT          DEFAULT NULL COMMENT '医嘱状态：1-正常执行 0-作废 2-停止',
  `patient_id`   INT          DEFAULT NULL COMMENT '患者id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者医嘱医疗服务项目信息表';

-- ----------------------------
-- 11. 疾病信息表 disease_info
-- ----------------------------
DROP TABLE IF EXISTS `disease_info`;
CREATE TABLE `disease_info` (
  `id`               INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `disease_code`     VARCHAR(50)  DEFAULT NULL COMMENT '疾病助记编码',
  `disease_name`     VARCHAR(255) DEFAULT NULL COMMENT '疾病名称',
  `diseaseicd`       VARCHAR(50)  DEFAULT NULL COMMENT '国际ICD编码',
  `disease_category` VARCHAR(50)  DEFAULT NULL COMMENT '疾病所属分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='疾病信息表';

-- ----------------------------
-- 12. 患者诊断疾病表 inpatient_disease
-- ----------------------------
DROP TABLE IF EXISTS `inpatient_disease`;
CREATE TABLE `inpatient_disease` (
  `id`           INT      NOT NULL AUTO_INCREMENT COMMENT '主键',
  `patient_id`   INT      DEFAULT NULL COMMENT '住院患者表外键',
  `disease_id`   INT      DEFAULT NULL COMMENT '疾病表外键',
  `order_time`   DATETIME DEFAULT NULL COMMENT '疾病诊断时间',
  `disease_type` TINYINT  DEFAULT NULL COMMENT '诊断类型（1、入院诊断，2、主要诊断，3、其他诊断）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者诊断疾病表';

SET FOREIGN_KEY_CHECKS = 1;
