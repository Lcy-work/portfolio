package com.cqupt.health_insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="医疗服务设施查询统计", description="")
public class MedicalServiceDto {

    @ApiModelProperty(value = "页码")
    private Integer pageSize;
    @ApiModelProperty(value = "页数")
    private Integer pageNumber;
    @ApiModelProperty(value = "医疗服务设施名")
    private String medicalName;

}