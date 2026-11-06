package com.cqupt.controller;

import com.cqupt.dto.*;
import com.cqupt.pojo.Statistics;
import com.cqupt.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 08:26
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/statistics")
@Tag(name = "统计管理", description = "统计相关接口")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "保存统计信息", description = "保存网格员的更新统计信息")
    @PostMapping("/saveStatistics")
    public int saveStatistics(@RequestBody Statistics statistics) {
        return statisticsService.saveStatistics(statistics);
    }

    // ==================== nepm ====================
    @Operation(summary = "分页查询统计数据", description = "管理员分页查询统计信息")
    @PostMapping("/listStatisticsPage")
    public PageResponseDto<Statistics> listStatisticsPage(@RequestBody StatisticsPageRequestDto statisticsPageRequestDto) {
        return statisticsService.listStatisticsPage(statisticsPageRequestDto);
    }

    @Operation(summary = "通过id查询统计信息", description = "管理员通过id查询统计信息")
    @PostMapping("/getStatisticsById")
    public Statistics getStatisticsById(@RequestBody Statistics statistics) {
        return statisticsService.getStatisticsById(statistics.getId());
    }

    @Operation(summary = "查询所有省份污染统计信息", description = "管理员查询所有省份污染统计信息")
    @PostMapping("/listProvinceItemTotalStatis")
    public List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis() {
        return statisticsService.listProvinceItemTotalStatis();
    }

    @Operation(summary = "查询所有AQI分布统计信息", description = "管理员查询所有AQI分布统计信息")
    @PostMapping("/listAqiDistributeTotalStatis")
    public List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis() {
        return statisticsService.listAqiDistributeTotalStatis();
    }

    @Operation(summary = "查询AQI趋势的统计信息", description = "管理员查询每日AQI等级趋势统计")
    @PostMapping("/listAqiTrendTotalStatis")
    public List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis() {
        return statisticsService.listAqiTrendTotalStatis();
    }

    @Operation(summary = "查询AQI总数", description = "管理员查询AQI数量")
    @PostMapping("/getAqiCount")
    public long getAqiCount() {
        return statisticsService.getAqiCount();
    }

    @Operation(summary = "查询AQI优良总数", description = "管理员查询AQI优良数量")
    @PostMapping("/getAqiGoodCount")
    public long getAqiGoodCount() {
        return statisticsService.getAqiGoodCount();
    }

    @Operation(summary = "查询省份覆盖范围", description = "管理员查询省份覆盖范围")
    @PostMapping("/getProvinceCoverage")
    public String getProvinceCoverage() {
        return statisticsService.getProvinceCoverage();
    }

    @Operation(summary = "查询城市覆盖范围", description = "管理员查询城市覆盖范围")
    @PostMapping("/getCityCoverage")
    public String getCityCoverage() {
        return statisticsService.getCityCoverage();
    }
}