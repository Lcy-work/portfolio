package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.dto.AqiDistributeTotalStatisDto;
import com.cqupt.dto.AqiTrendTotalStatisDto;
import com.cqupt.dto.ProvinceItemTotalStatisDto;
import com.cqupt.dto.StatisticsPageRequestDto;
import com.cqupt.pojo.Statistics;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 08:56
 * @Description:
 */
@Mapper
public interface StatisticsMapper extends BaseMapper<Statistics> {

    // nepm工程
    // 总条数
    int getStatisticsCount(StatisticsPageRequestDto statisticsPageRequestDto);
    // 分页列表
    List<Statistics> listStatisticsPage(StatisticsPageRequestDto statisticsPageRequestDto);

    // 省份污染汇总统计
    List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis();

    // AQI分布统计
    List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis();

    // 查询AQI趋势的统计信息(带参数)
    List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis(List<AqiTrendTotalStatisDto> list);

    // 去重统计有数据的省份数量
    Long countDistinctProvince();
    // 去重统计有数据的城市数量
    Long countDistinctCity();
}
