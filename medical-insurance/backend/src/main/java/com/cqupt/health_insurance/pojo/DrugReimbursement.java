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
@ApiModel(value = "DrugReimbursement对象", description = "药品报销等级表")
@TableName("drug_reimbursement")
public class DrugReimbursement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "药品报销级别")
    private String drugReimbursementType;

    @ApiModelProperty(value = "药品报销比例")
    private Integer drugReimbursementProportion;

    @ApiModelProperty(value = "药品报销等级说明")
    private String drugReimbursementInfo;

    @ApiModelProperty(value = "状态：1-正常 0-禁用")
    private Integer status;
}
