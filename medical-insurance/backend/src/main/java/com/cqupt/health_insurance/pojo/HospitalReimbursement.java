package com.cqupt.health_insurance.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "HospitalReimbursement对象", description = "医院报销等级表")
@TableName("hospital_reimbursement")
public class HospitalReimbursement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "报销起付金额")
    private Integer minPayLevel;

    @ApiModelProperty(value = "报销等级线")
    private Integer maxPayLevel;

    @ApiModelProperty(value = "人员类别(1在职人员，0退休人员)")
    private Integer peopleType;

    @ApiModelProperty(value = "报销比例")
    private Integer payProportion;

    @ApiModelProperty(value = "医院等级")
    private String hospitalLevel;

    @ApiModelProperty(value = "状态：1-正常 0-禁用")
    private Integer status;
}