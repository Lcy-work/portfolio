-- ============================================================
-- 新能源充电桩大数据项目 - MySQL初始化脚本
-- ============================================================

CREATE DATABASE IF NOT EXISTS enger CHARACTER SET utf8 COLLATE utf8_general_ci;
USE enger;

-- 1. 平均组电压和充电电流表
DROP TABLE IF EXISTS t_enger1;
CREATE TABLE t_enger1 (
    id INT NOT NULL AUTO_INCREMENT,
    record_time VARCHAR(50),
    avg_pack_voltage DOUBLE,
    avg_charge_current DOUBLE,
    PRIMARY KEY (id)
);

-- 2. 最高/最低单体电压表
DROP TABLE IF EXISTS t_enger2;
CREATE TABLE t_enger2 (
    id INT NOT NULL AUTO_INCREMENT,
    voltage_type VARCHAR(50),
    voltage_value DOUBLE,
    PRIMARY KEY (id)
);

-- 3. 每时电池平均SOC表
DROP TABLE IF EXISTS t_enger3;
CREATE TABLE t_enger3 (
    id INT NOT NULL AUTO_INCREMENT,
    record_hour VARCHAR(50),
    avg_soc DOUBLE,
    PRIMARY KEY (id)
);

-- 4. 充电频率分析表
DROP TABLE IF EXISTS t_enger4;
CREATE TABLE t_enger4 (
    id INT NOT NULL AUTO_INCREMENT,
    charge_hour VARCHAR(50),
    charge_count INT,
    frequency_ratio DOUBLE,
    PRIMARY KEY (id)
);

-- 5. 充电时间分布表
DROP TABLE IF EXISTS t_enger5;
CREATE TABLE t_enger5 (
    id INT NOT NULL AUTO_INCREMENT,
    time_range VARCHAR(50),
    session_count INT,
    PRIMARY KEY (id)
);

-- 6. 平均充电速率表
DROP TABLE IF EXISTS t_enger6;
CREATE TABLE t_enger6 (
    id INT NOT NULL AUTO_INCREMENT,
    record_hour VARCHAR(50),
    avg_charge_rate DOUBLE,
    PRIMARY KEY (id)
);

-- 7. 性能指标分类表
DROP TABLE IF EXISTS t_enger7;
CREATE TABLE t_enger7 (
    id INT NOT NULL AUTO_INCREMENT,
    station_id VARCHAR(50),
    total_kwh DOUBLE,
    avg_charge_time DOUBLE,
    total_fees DOUBLE,
    session_count INT,
    PRIMARY KEY (id)
);
