package com.cqupt.controller;

import com.cqupt.pojo.Admins;
import com.cqupt.service.AdminsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: fengye
 * @create: 2026-06-09 09:17
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/admins")
@Tag(name = "管理员管理", description = "管理员相关接口")
public class AdminsController {

    @Autowired
    private AdminsService adminsService;

    @Operation(summary = "管理员账号密码登录", description = "根据账号密码校验管理员登录信息")
    @PostMapping("/getAdminsByCodeByPass")
    public Admins getAdminsByCodeByPass(@RequestBody Admins admins){
        return adminsService.getAdminsByCodeByPass(admins);
    }

    @Operation(summary = "管理员注册", description = "新增管理员账号")
    @PostMapping("/register")
    public boolean register(@RequestBody Admins admins){
        return adminsService.register(admins);
    }

    @Operation(summary = "检查手机号是否已注册", description = "根据手机号查重")
    @GetMapping("/checkTel/{phone}")
    public boolean checkTel(@PathVariable String phone){
        return adminsService.checkTel(phone);
    }
}