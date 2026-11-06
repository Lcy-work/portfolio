package com.cqupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.pojo.GridCity;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 15:05
 * @Description:
 */
public interface GridCityService extends IService<GridCity> {
    List<GridCity> listGridCityByProvinceId(Integer provinceId);
}