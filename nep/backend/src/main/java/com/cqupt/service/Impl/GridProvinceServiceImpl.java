package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.mapper.GridProvinceMapper;
import com.cqupt.pojo.GridProvince;
import com.cqupt.service.GridProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 14:08
 * @Description:
 */
@Service
public class GridProvinceServiceImpl extends ServiceImpl<GridProvinceMapper,GridProvince> implements GridProvinceService {

    @Autowired
    private GridProvinceMapper gridProvinceMapper;

    @Override
    public List<GridProvince> listGridProvinceAll() {
        return gridProvinceMapper.listGridProvinceAll();
    }
}