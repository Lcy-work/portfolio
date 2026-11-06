package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cqupt.dto.*;
import com.cqupt.mapper.GridCityMapper;
import com.cqupt.mapper.GridProvinceMapper;
import com.cqupt.mapper.StatisticsMapper;
import com.cqupt.pojo.*;
import com.cqupt.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 09:00
 * @Description:
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {


    @Value("${global-data.province-number}")
    private Integer provinceNumber;
    @Value("${global-data.city-number}")
    private Integer cityNumber;

    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private GridProvinceMapper gridProvinceMapper;
    @Autowired
    private GridCityMapper gridCityMapper;

    @Override
    public PageResponseDto<Statistics> listStatisticsPage(StatisticsPageRequestDto request) {
        // 页码、每页条数容错兜底，防止null/0
        if (request.getPageNum() == null || request.getPageNum() < 1) {
            request.setPageNum(1);
        }
        if (request.getMaxPageNum() == null || request.getMaxPageNum() < 1) {
            request.setMaxPageNum(10);
        }

        PageResponseDto<Statistics> response = new PageResponseDto<>();
        int totalRow = statisticsMapper.getStatisticsCount(request);
        response.setTotalRow(totalRow);
        if (totalRow == 0) {
            return response;
        }

        // 现在maxPageNum一定>=1，不会出现除以0
        int pageSize = request.getMaxPageNum();
        int totalPageNum = totalRow / pageSize;
        if (totalRow % pageSize != 0) {
            totalPageNum = totalPageNum + 1;
        }
        response.setTotalPageNum(totalPageNum);

        int pageNum = request.getPageNum();
        response.setPageNum(pageNum);
        response.setPreNum(pageNum > 1 ? pageNum - 1 : pageNum);
        response.setNextNum(pageNum < totalPageNum ? pageNum + 1 : pageNum);

        request.setBeginNum((pageNum - 1) * pageSize);

        List<Statistics> list = statisticsMapper.listStatisticsPage(request);

        response.setMaxPageNum(pageSize);
        response.setList(list);
        return response;
    }

    @Override
    public int saveStatistics(Statistics statistics) {
        return statisticsMapper.insert(statistics);
    }

    @Override
    public Statistics getStatisticsById(Integer id) {
        return statisticsMapper.selectById(id);
    }

    @Override
    public List<ProvinceItemTotalStatisDto> listProvinceItemTotalStatis() {
        return statisticsMapper.listProvinceItemTotalStatis();
    }

    @Override
    public List<AqiDistributeTotalStatisDto> listAqiDistributeTotalStatis() {
        return statisticsMapper.listAqiDistributeTotalStatis();
    }

    // 无参对外入口：自动生成12个月（匹配数据中的年份范围）
    @Override
    public List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis() {
        List<AqiTrendTotalStatisDto> paramList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AqiTrendTotalStatisDto dto = new AqiTrendTotalStatisDto();
            dto.setMonth(String.format("2022-%02d", i));
            paramList.add(dto);
        }
        // 调用带参重载方法，统一走查询逻辑
        return listAqiTrendTotalStatis(paramList);
    }

    // 带参底层查询方法
    @Override
    public List<AqiTrendTotalStatisDto> listAqiTrendTotalStatis(List<AqiTrendTotalStatisDto> list) {
        return statisticsMapper.listAqiTrendTotalStatis(list);
    }

    @Override
    public long getAqiCount() {
        return statisticsMapper.selectCount(null);
    }

    @Override
    public long getAqiGoodCount() {
        QueryWrapper<Statistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("aqi_id", 1);
        //eq：等于 1 = 优
        return statisticsMapper.selectCount(queryWrapper);
    }

    @Override
    public String getProvinceCoverage() {
        long provinceNum = gridProvinceMapper.selectCount(null);
        // 47.06%
        return String.format("%.2f", provinceNum * 100.0 / provinceNumber);
    }

    @Override
    public String getCityCoverage() {
        long cityNum = gridCityMapper.selectCount(null);
        // 16.04%
        return String.format("%.2f", cityNum * 100.0 / cityNumber);
    }
}