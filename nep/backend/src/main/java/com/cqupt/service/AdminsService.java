package com.cqupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.pojo.Admins;

/**
 * @author: fengye
 * @create: 2026-06-09 09:17
 * @Description:
 */
public interface AdminsService extends IService<Admins> {
    Admins getAdminsByCodeByPass(Admins admins);
    boolean register(Admins admins);
    boolean checkTel(String phone);
}