package com.cqupt.health_insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="诊疗项目查询条件", description="")
public class PatientRegistrationDto {

    @ApiModelProperty(value = "页码")
    private Integer pageSize;
    @ApiModelProperty(value = "页数")
    private Integer pageNumber;
    @ApiModelProperty(value = "患者姓名")
    private String realName;

}