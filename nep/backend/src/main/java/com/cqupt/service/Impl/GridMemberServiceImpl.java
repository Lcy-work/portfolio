package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.mapper.GridMemberMapper;
import com.cqupt.pojo.GridMember;
import com.cqupt.service.GridMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 08:21
 * @Description:
 */
@Service
public class GridMemberServiceImpl implements GridMemberService {

    @Autowired
    private GridMemberMapper gridMemberMapper;

    // ==================== nepg ====================
    @Override
    public GridMember getGridMemberByCodeByPass(GridMember gridMember) {
        return gridMemberMapper.getGridMemberByCodeByPass(gridMember);
    }

    @Override
    public boolean register(GridMember gridMember) {
        // 如果未传 gmCode，用 gmName 作为工号
        if (gridMember.getGmCode() == null || gridMember.getGmCode().isBlank()) {
            gridMember.setGmCode(gridMember.getGmName());
        }
        // 设置默认状态：启用
        if (gridMember.getState() == null) {
            gridMember.setState(1);
        }
        // 注册时省份/城市默认为0（后续由管理员分配）
        if (gridMember.getProvinceId() == null) {
            gridMember.setProvinceId(0);
        }
        if (gridMember.getCityId() == null) {
            gridMember.setCityId(0);
        }
        return gridMemberMapper.insert(gridMember) > 0;
    }

    @Override
    public GridMember getGridMemberByName(String gmName) {
        QueryWrapper<GridMember> qw = new QueryWrapper<>();
        qw.eq("gm_name", gmName);
        return gridMemberMapper.selectOne(qw);
    }

    // ==================== nepm ====================
    @Override
    public List<GridMember> listGridMemberByProvinceId(GridMember gridMember) {
        QueryWrapper<GridMember> queryWrapper = new QueryWrapper<>();
        // 查询条件：省份id、城市id、状态（仅非 null 时才作为过滤条件）
        queryWrapper.eq(gridMember.getProvinceId() != null, "province_id", gridMember.getProvinceId());
        queryWrapper.eq(gridMember.getCityId() != null, "city_id", gridMember.getCityId());
        queryWrapper.eq(gridMember.getState() != null, "state", gridMember.getState());
        return gridMemberMapper.selectList(queryWrapper);
    }
}