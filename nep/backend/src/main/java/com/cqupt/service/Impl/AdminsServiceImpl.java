package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.mapper.AdminsMapper;
import com.cqupt.pojo.Admins;
import com.cqupt.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: fengye
 * @create: 2026-06-09 09:17
 * @Description:
 */
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper,Admins> implements AdminsService {

    @Autowired
    private AdminsMapper adminsMapper;

    @Override
    public Admins getAdminsByCodeByPass(Admins admins) {
        return adminsMapper.getAdminsByCodeByPass(admins);
    }

    @Override
    public boolean register(Admins admins) {
        return save(admins);
    }

    @Override
    public boolean checkTel(String phone) {
        QueryWrapper<Admins> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        return count(wrapper) > 0;
    }
}