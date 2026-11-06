package com.cqupt.controller;

import com.cqupt.pojo.Supervisor;
import com.cqupt.service.SupervisorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fengye
 * @create: 2026-06-06 09:55
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/supervisor")
@Tag(name = "公众监督员管理", description = "公众监督员注册、登录相关的接口")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Operation(summary = "检查手机号是否已注册")
    @GetMapping("/checkTel/{telId}")
    public boolean checkTel(@PathVariable String telId){
        return supervisorService.checkTel(telId);
    }

    @Operation(summary = "公众监督员注册", description = "注册公众监督员信息")
    @PostMapping("/saveSupervisor")
    public int saveSupervisor(@RequestBody Supervisor supervisor){
        return supervisorService.saveSupervisor(supervisor);
    }

    @Operation(summary = "监督员账号密码登录", description = "根据账号密码登录")
    @PostMapping("/getSupervisorByIdByPass")
    public Supervisor getSupervisorByIdByPass(@RequestBody Supervisor supervisor){
        return supervisorService.getSupervisorByIdByPass(supervisor);
    }
}