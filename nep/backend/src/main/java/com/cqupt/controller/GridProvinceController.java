package com.cqupt.controller;

import com.cqupt.pojo.GridProvince;
import com.cqupt.service.GridProvinceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-06 14:08
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/gridProvince")
@Tag(name = "网格员省份管理", description = "网格员所在省份相关接口")
public class GridProvinceController {

    @Autowired
    private GridProvinceService gridProvinceService;

    @Operation(summary = "查询所有省份列表", description = "获取系统中所有的省份信息")
    @PostMapping("/listGridProvinceAll")
    public List<GridProvince> listGridProvinceAll(){
        return gridProvinceService.listGridProvinceAll();
    }
}