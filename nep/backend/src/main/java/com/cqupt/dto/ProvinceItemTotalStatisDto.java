package com.cqupt.dto;

import lombok.Data;

/**
 * @author: fengye
 * @create: 2026-06-09 19:10
 * @Description: 省份汇总统计DTO
 */
@Data
public class ProvinceItemTotalStatisDto {
    private Integer provinceId;
    private String provinceAbbr;
    private String provinceName;
    private Long so2Total;
    private Long coTotal;
    private Long spmTotal;
    private Long aqiTotal;
}