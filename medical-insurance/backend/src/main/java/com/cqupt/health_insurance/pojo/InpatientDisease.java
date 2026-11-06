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
@ApiModel(value= "InpatientDisease对象", description = "患者诊断疾病表")
@TableName("inpatient_disease")
public class InpatientDisease implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "住院患者表外键")
    private Integer patientId;

    @ApiModelProperty(value = "疾病表外键")
    private Integer diseaseId;

    @ApiModelProperty(value = "疾病诊断时间")
    private Date orderTime;

    @ApiModelProperty(value = "诊断类型（1、入院诊断，2、主要诊断，3、其他诊断）")
    private Integer diseaseType;
}
