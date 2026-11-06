package com.cqupt.health_insurance.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "InpatientMedical对象", description = "患者医嘱医疗服务项目信息表")
@TableName("inpatient_medical")
public class InpatientMedical implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "开立时间")
    private Date orderTime;

    @ApiModelProperty(value = "医嘱内容")
    private String doctorOrder;

    @ApiModelProperty(value = "用法详情说明")
    private String useMethod;

    @ApiModelProperty(value = "医疗服务表外键")
    private Integer medicalId;

    @ApiModelProperty(value = "医嘱状态：1-正常执行 0-作废 2-停止")
    private Integer status;

    @ApiModelProperty(value = "患者id")
    private Integer patientId;
}