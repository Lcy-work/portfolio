package com.cqupt.controller;

import com.cqupt.pojo.GridCity;
import com.cqupt.service.GridCityService;
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
 * @create: 2026-06-06 14:55
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/gridCity")
@Tag(name = "网格员城市管理", description = "网格员所在城市相关接口")
public class GridCityController {

    @Autowired
    private GridCityService gridCityService;

    @Operation(summary = "查询省份的所有城市列表", description = "根据省份的id来查询所有城市信息")
    @PostMapping("/listGridCityByProvinceId")
    public List<GridCity> listGridCityByProvinceId(@RequestBody GridCity gridCity){
        Integer provinceId = gridCity.getProvinceId();
        if (provinceId == null) {
            return List.of();
        }
        return gridCityService.listGridCityByProvinceId(provinceId);
    }
}