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
@ApiModel(value = "DrugInfo对象", description = "药品表")
@TableName("drug_info")
public class DrugInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "报销类别")
    private String insuranceType;

    @ApiModelProperty(value = "药品中文名称")
    private String chinaName;

    @ApiModelProperty(value = "药品商品名称")
    private String goodsName;

    @ApiModelProperty(value = "规格")
    private String specifications;

    @ApiModelProperty(value = "药品单位")
    private String drugUnit;

    @ApiModelProperty(value = "生产企业")
    private String drugManufacturer;

    @ApiModelProperty(value = "价格")
    private BigDecimal drugPrice;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
