package com.cqupt.dto;

import lombok.Data;

/**
 * @author: fengye
 * @create: 2026-06-09 10:00
 * @Description: 反馈分页查询请求DTO
 */
@Data
public class AfPageRequestDto {

    // 业务查询条件
    private Integer provinceId;
    private Integer cityId;
    private Integer estimatedGrade;
    private String afDate;
    private Integer state;

    // 分页参数
    private Integer pageNum;        // 当前页
    private Integer maxPageNum;     // 每页最大行数
    private Integer beginNum;       // 起始记录数
}