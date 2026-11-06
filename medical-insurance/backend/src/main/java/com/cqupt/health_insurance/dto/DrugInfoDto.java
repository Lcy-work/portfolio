package com.cqupt.health_insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="药品查询条件", description="")
public class DrugInfoDto {

    @ApiModelProperty(value = "页码")
    private Integer pageSize;
    @ApiModelProperty(value = "页数")
    private Integer pageNumber;
    @ApiModelProperty(value = "药品名字")
    private String drugName;

}
