package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.mapper.SupervisorMapper;
import com.cqupt.pojo.Supervisor;
import com.cqupt.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: fengye
 * @create: 2026-06-06 09:55
 * @Description:
 */
@Service
public class SupervisorServiceImpl extends ServiceImpl<SupervisorMapper,Supervisor> implements SupervisorService {

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Override
    public Supervisor getSupervisorByTelId(String telId) {
        return supervisorMapper.selectById(telId);
    }

    @Override
    public boolean checkTel(String telId) {
        Supervisor supervisor = supervisorMapper.selectById(telId);
        return supervisor != null;
    }

    @Override
    public int saveSupervisor(Supervisor supervisor) {
        // 先查询该手机号是否已存在
        Supervisor existingSupervisor = supervisorMapper.selectById(supervisor.getTelId());
        if (existingSupervisor != null) {
            // 如果已存在，则执行更新操作
            return supervisorMapper.updateById(supervisor);
        } else {
            // 如果不存在，则执行插入操作
            return supervisorMapper.insert(supervisor);
        }
    }

    @Override
    public Supervisor getSupervisorByIdByPass(Supervisor supervisor) {
        return supervisorMapper.getSupervisorByIdByPass(supervisor);
    }
}