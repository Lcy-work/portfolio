package com.cqupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aqi_feedback")
public class AqiFeedback {
    @TableId(value = "af_id", type = IdType.AUTO)
    private Integer afId;
    private String telId;
    private Integer provinceId;
    private Integer cityId;
    private String address;
    private String information;
    private Integer estimatedGrade;
    private String afDate;
    private String afTime;
    private Integer gmId;
    private String assignDate;
    private String assignTime;
    private Integer state;
    private String remarks;

    //非数据库字段，关联查询使用
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