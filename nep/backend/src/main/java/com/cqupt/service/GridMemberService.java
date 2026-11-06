package com.cqupt.service;

import com.cqupt.pojo.GridMember;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 08:21
 * @Description: 网格员业务接口
 */
public interface GridMemberService {

    // ==================== nepg 网格员端 ====================
    /**
     * 网格员账号密码登录校验
     */
    GridMember getGridMemberByCodeByPass(GridMember gridMember);

    /**
     * 网格员注册
     */
    boolean register(GridMember gridMember);

    /**
     * 根据姓名查找网格员
     */
    GridMember getGridMemberByName(String gmName);

    // ==================== nepm 管理员端 ====================
    /**
     * 根据省份id、城市id、状态筛选网格员列表
     */
    List<GridMember> listGridMemberByProvinceId(GridMember gridMember);
}