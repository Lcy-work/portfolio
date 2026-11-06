package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.pojo.Supervisor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: fengye
 * @create: 2026-06-06 09:55
 * @Description:
 */
@Mapper
public interface SupervisorMapper extends BaseMapper<Supervisor> {
    // 账号密码查询登录
    Supervisor getSupervisorByIdByPass(Supervisor supervisor);
}