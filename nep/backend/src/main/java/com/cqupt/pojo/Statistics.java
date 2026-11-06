package com.cqupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("statistics")
public class Statistics {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer provinceId;
    private Integer cityId;
    private String address;
    private Integer so2Value;
    private Integer so2Level;
    private Integer coValue;
    private Integer coLevel;
    private Integer spmValue;
    private Integer spmLevel;
    private Integer aqiId;
    private String confirmDate;
    private String confirmTime;
    private Integer gmId;
    private String fdId;
    private String information;
    private String remarks;

    //非数据库关联字段
    @TableField(exist = false)
    private Supervisor supervisor;
    @TableField(exist = false)
    private GridProvince gridProvince;
    @TableField(exist = false)
    private GridCity gridCity;
    @TableField(exist = false)
    private Aqi aqi;
    @TableField(exist = false)
    private GridMember gridMember;
}