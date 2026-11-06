package com.cqupt.dto;

import lombok.Data;

/**
 * @author: fengye
 * @create: 2026-06-09 15:57
 * @Description:
 */
@Data
public class StatisticsPageRequestDto {
    // 业务查询条件
    private Integer provinceId;
    private Integer cityId;
    private String confirmDate;

    // 分页参数
    private Integer pageNum;      // 当前页
    private Integer maxPageNum;  // 每页最大行数
    private Integer beginNum;    // 起始记录数
}