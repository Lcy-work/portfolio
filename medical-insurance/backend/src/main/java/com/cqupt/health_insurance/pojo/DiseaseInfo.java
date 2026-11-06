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
@ApiModel(value = "DiseaseInfo对象", description = "疾病表")
@TableName("disease_info")
public class DiseaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "疾病助记编码")
    private String diseaseCode;

    @ApiModelProperty(value = "疾病名称")
    private String diseaseName;

    @ApiModelProperty(value = "国际ICD编码")
    private String diseaseicd;

    @ApiModelProperty(value = "疾病所属分类")
    private String diseaseCategory;
}
