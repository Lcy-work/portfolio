package com.cqupt.service.Impl;

import com.cqupt.mapper.AqiMapper;
import com.cqupt.pojo.Aqi;
import com.cqupt.service.AqiService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 16:04
 * @Description:
 */
@Service
public class AqiServiceImpl implements AqiService {

    @Autowired
    private AqiMapper aqiMapper;

    @Override
    public List<Aqi> listAqiAll() {
        QueryWrapper<Aqi> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("aqi_id");
        return aqiMapper.selectList(queryWrapper);
    }
}