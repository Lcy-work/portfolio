package com.cqupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.pojo.Supervisor;

/**
 * @author: fengye
 * @create: 2026-06-06 09:55
 * @Description:
 */
public interface SupervisorService extends IService<Supervisor> {

    // 根据手机号查询用户
    Supervisor getSupervisorByTelId(String telId);

    // 检查手机号是否已注册
    boolean checkTel(String telId);

    // 监督员注册
    int saveSupervisor(Supervisor supervisor);

    // 账号密码登录
    Supervisor getSupervisorByIdByPass(Supervisor supervisor);
}