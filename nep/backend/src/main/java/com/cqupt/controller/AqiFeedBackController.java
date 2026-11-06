package com.cqupt.controller;

import com.cqupt.dto.AfPageRequestDto;
import com.cqupt.dto.PageResponseDto;
import com.cqupt.pojo.AqiFeedback;
import com.cqupt.pojo.Statistics;
import com.cqupt.service.AqiFeedbackService;
import com.cqupt.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 9:58
 * @Description: AQI反馈控制器
 */
@RestController
@RequestMapping("/aqiFeedback")
@CrossOrigin
@Tag(name = "AQI环保反馈管理接口")
public class AqiFeedBackController {

    @Autowired
    private AqiFeedbackService aqiFeedbackService;

    @Autowired
    private StatisticsService statisticsService;

    // ==================== NEPS公众监督员端 ====================
    @PostMapping("/listAqiFeedbackByTelId")
    @Operation(summary = "展示公众反馈列表", description = "根据手机号查询反馈列表")
    public List<AqiFeedback> listAqiFeedbackByTelId(@RequestBody AqiFeedback aqiFeedback) {
        String telId = aqiFeedback.getTelId();
        if (telId == null || telId.isBlank()) {
            return List.of();
        }
        return aqiFeedbackService.listAqiFeedbackByTelId(telId);
    }

    /**
     * 监督员查询自己的反馈列表（按手机号 GET）
     */
    @GetMapping("/listAqiFeedbackByTelId")
    @Operation(summary = "按监督员手机号查询反馈列表")
    public List<AqiFeedback> listAqiFeedbackByTelId(@Parameter(description = "监督员手机号") @RequestParam String telId){
        return aqiFeedbackService.listAqiFeedbackByTelId(telId);
    }

    /**
     * 监督员提交新反馈（POST）
     */
    @PostMapping("/saveAqiFeedback")
    @Operation(summary = "监督员提交新的环保反馈")
    public boolean saveAqiFeedback(@RequestBody AqiFeedback aqiFeedback){
        int rows = aqiFeedbackService.saveAqiFeedback(aqiFeedback);
        return rows > 0;
    }

    // ==================== NEPG网格员端 ====================
    /**
     * 网格员查询反馈列表（列表页接口 POST）
     */
    @PostMapping("/listAqiFeedbackByGmId")
    @Operation(summary = "展示网格员反馈列表", description = "根据网格员id和状态查询反馈列表")
    public List<AqiFeedback> listAqiFeedbackByGmId(@RequestBody AqiFeedback aqiFeedback){
        if (aqiFeedback.getGmId() == null && aqiFeedback.getState() == null) {
            return List.of();
        }
        return aqiFeedbackService.listAqiFeedbackByGmId(aqiFeedback);
    }

    /**
     * 根据id查询详情（详情页面接口 GET）
     */
    @GetMapping("/getAqiFeedbackById")
    @Operation(summary = "根据afId查询反馈详情(详情渲染页面)")
    public AqiFeedback getAqiFeedbackById(@Parameter(description = "反馈主键id") @RequestParam Integer afId){
        List<AqiFeedback> list = aqiFeedbackService.getAqiFeedbackById(afId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 网格员提交实测数据确认
     */
    @PostMapping("/confirm")
    @Operation(summary = "网格员提交实测数据")
    public boolean confirm(@RequestBody Statistics statistics){
        return statisticsService.saveStatistics(statistics) > 0;
    }

    /**
     * 更新反馈任务处理状态
     */
    @PutMapping("/updateState")
    @Operation(summary = "更新反馈任务状态")
    public int updateState(@RequestBody AqiFeedback aqiFeedback){
        return aqiFeedbackService.updateAqiFeedbackState(aqiFeedback);
    }

    // ==================== NEPM系统管理员端 ====================
    @Operation(summary = "分页查询反馈列表", description = "根据省份id、城市id、等级、时间查询反馈列表")
    @PostMapping("/listAqiFeedbackPage")
    public PageResponseDto<AqiFeedback> listAqiFeedbackPage(@RequestBody AfPageRequestDto afPageRequestDto){
        return aqiFeedbackService.listAqiFeedbackPage(afPageRequestDto);
    }

    // ==================== 【新增】分配指派任务接口 ====================
    @Operation(summary = "分配指派任务", description = "管理员将指派任务分配给网格员处理")
    @PostMapping("/updateAqiFeedbackAssign")
    public int updateAqiFeedbackAssign(@RequestBody AqiFeedback aqiFeedback) {
        return aqiFeedbackService.updateAqiFeedbackAssign(aqiFeedback);
    }

}