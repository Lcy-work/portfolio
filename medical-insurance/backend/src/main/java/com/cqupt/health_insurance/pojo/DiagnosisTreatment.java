package com.cqupt.health_insurance.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Diagnosistreatment对象", description = "诊疗项目表")
@TableName("diagnosis_treatment")
public class DiagnosisTreatment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目类别")
    private String medicalType;

    @ApiModelProperty(value = "项目编码")
    private String medicalNumber;

    @ApiModelProperty(value = "项目国家编码")
    private String countryNumber;

    @ApiModelProperty(value = "项目名称")
    private String medicalName;

    @ApiModelProperty(value = "项目说明")
    private String medicalInfo;

    @ApiModelProperty(value = "除外内容")
    private String medicalExclude;

    @ApiModelProperty(value = "计价单位")
    private String medicalUnit;

    @ApiModelProperty(value = "价格")
    private BigDecimal medicalPrice;

    @ApiModelProperty(value = "备注")
    private String remark;
}
