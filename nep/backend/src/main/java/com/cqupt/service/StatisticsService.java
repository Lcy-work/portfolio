package com.cqupt.service;

import com.cqupt.dto.*;
import com.cqupt.pojo.Statistics;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 09:00
 * @Description: 统计业务接口
 */
public interface StatisticsService {
    // nepm 分页查询统计
    PageResponseDto<Statistics> listStatisticsPage(StatisticsPageRequestDto request);

    // 新增保存统计数据
    int saveStatistics(Statistics statistics);

    // 根据id查询单条统计
    Statistics getStatisticsById(Integer id);

    // 查询全部省份污染汇总统计
    List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis();

    // 获取AQI分布的统计信息
    List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis();

    // 查询AQI趋势的统计信息
    List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis();

    List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis(List<AqiTrendTotalStatisDto> list);

    // AQI总记录数
    long getAqiCount();
    // AQI优良记录数
    long getAqiGoodCount();
    // 省份覆盖率（带%字符串）
    String getProvinceCoverage();
    // 城市覆盖率（带%字符串）
    String getCityCoverage();
}