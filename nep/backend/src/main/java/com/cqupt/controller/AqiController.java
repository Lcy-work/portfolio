package com.cqupt.controller;

import com.cqupt.pojo.Aqi;
import com.cqupt.service.AqiService;
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
 * @create: 2026-06-05 16:04
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/aqi")
@Tag(name = "AQI数据管理", description = "空气质量相关接口")
public class AqiController {

    @Autowired
    private AqiService aqiService;

    @Operation(summary = "查询所有AQI数据", description = "获取系统中所有AQI指数列表")
    @PostMapping("/listAqiAll")
    public List<Aqi> listAqiAll() {
        return aqiService.listAqiAll();
    }
}