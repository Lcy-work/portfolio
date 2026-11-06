package com.cqupt.controller;

import com.cqupt.pojo.GridMember;
import com.cqupt.service.GridMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 08:21
 * @Description: 网格员控制器
 */
@RestController
@RequestMapping("/gridMember")
@CrossOrigin
@Tag(name = "网格员管理接口")
public class GridMemberController {

    @Autowired
    private GridMemberService gridMemberService;

    // ==================== nepg 网格员端 ====================
    @PostMapping("/getGridMemberByCodeByPass")
    @Operation(summary = "网格员账号密码登录", description = "校验手机号密码登录")
    public GridMember getGridMemberByCodeByPass(@RequestBody GridMember gridMember){
        return gridMemberService.getGridMemberByCodeByPass(gridMember);
    }

    @PostMapping("/register")
    @Operation(summary = "网格员注册")
    public boolean register(@RequestBody GridMember gridMember){
        return gridMemberService.register(gridMember);
    }

    @PostMapping("/forgotPassword")
    @Operation(summary = "忘记密码")
    public String forgotPassword(@RequestBody GridMember gridMember){
        GridMember result = gridMemberService.getGridMemberByName(gridMember.getGmName());
        return result != null ? result.getPassword() : null;
    }

    // ==================== nepm 管理员端 ====================
    @PostMapping("/listGridMemberByProvinceId")
    @Operation(summary = "按省市区状态筛选网格员列表", description = "管理员后台筛选网格员数据")
    public List<GridMember> listGridMemberByProvinceId(@RequestBody GridMember gridMember){
        return gridMemberService.listGridMemberByProvinceId(gridMember);
    }
}