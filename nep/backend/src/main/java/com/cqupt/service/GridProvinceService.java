package com.cqupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.pojo.GridProvince;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 14:08
 * @Description:
 */
public interface GridProvinceService extends IService<GridProvince> {
    List<GridProvince> listGridProvinceAll();
}