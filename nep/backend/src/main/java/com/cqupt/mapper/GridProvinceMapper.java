package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.pojo.GridProvince;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 14:08
 * @Description:
 */
@Mapper
public interface GridProvinceMapper extends BaseMapper<GridProvince> {
    List<GridProvince> listGridProvinceAll();
}