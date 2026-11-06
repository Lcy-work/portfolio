package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.mapper.GridCityMapper;
import com.cqupt.pojo.GridCity;
import com.cqupt.service.GridCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 15:05
 * @Description:
 */
@Service
public class GridCityServiceImpl extends ServiceImpl<GridCityMapper,GridCity> implements GridCityService {

    @Autowired
    private GridCityMapper gridCityMapper;

    @Override
    public List<GridCity> listGridCityByProvinceId(Integer provinceId) {
        return gridCityMapper.listCityByProvinceId(provinceId);
    }
}