package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.pojo.GridCity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 15:05
 * @Description:
 */
@Mapper
public interface GridCityMapper extends BaseMapper<GridCity> {
    List<GridCity> listCityByProvinceId(Integer provinceId);
}