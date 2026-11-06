package com.cqupt.dto;

import lombok.Data;

/**
 * @author: fengye
 * @create: 2026-06-10 19:15
 * @Description: AQI等级分布统计DTO
 */
@Data
public class AqiDistributeTotalStatisDto {
    private Integer aqiId;
    private String aqiExplain;
    private Long total;
}