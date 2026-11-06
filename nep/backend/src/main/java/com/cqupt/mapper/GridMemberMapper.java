package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.pojo.GridMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: fengye
 * @create: 2026-06-05 08:21
 * @Description:
 */
@Mapper
public interface GridMemberMapper extends BaseMapper<GridMember> {
    // nepg 登录查询
    GridMember getGridMemberByCodeByPass(GridMember gridMember);
}