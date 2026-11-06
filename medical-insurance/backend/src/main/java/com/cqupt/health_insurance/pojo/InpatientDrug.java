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
@ApiModel(value = "InpatientDrug对象", description = "患者医嘱药品信息表")
@TableName("inpatient_drug")
public class InpatientDrug implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "医嘱内容")
    private String doctorOrder;

    @ApiModelProperty(value = "医嘱频次（次/天）")
    private Integer orderNumber;

    @ApiModelProperty(value = "用法")
    private String useMethod;

    @ApiModelProperty(value = "药品表外键")
    private Integer drugId;

    @ApiModelProperty(value = "患者表外键")
    private Integer patientId;

    @ApiModelProperty(value = "医嘱状态：1-正常执行 0-作废 2-停止")
    private Integer status;
}
