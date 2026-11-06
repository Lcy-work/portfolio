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
@ApiModel(value = "PatientRegistration对象", description = "患者信息表")
@TableName("patient_registration")
public class PatientRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "住院号")
    private String caseNumber;

    @ApiModelProperty(value = "患者姓名")
    private String realName;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "身份证号")
    private String cardNumber;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "年龄类型（岁、月、日）")
    private String ageType;

    @ApiModelProperty(value = "住址")
    private String homeAddress;

    @ApiModelProperty(value = "入院日期")
    private Date visitDate;

    @ApiModelProperty(value = "结算类别（自费、医保）")
    private String registMethod;

    @ApiModelProperty(value = "工作状态（在职、退休）")
    private String workStatus;
}
