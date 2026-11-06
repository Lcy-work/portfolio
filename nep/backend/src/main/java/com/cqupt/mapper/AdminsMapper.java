package com.cqupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqupt.pojo.Admins;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: fengye
 * @create: 2026-06-09 09:17
 * @Description:
 */
@Mapper
public interface AdminsMapper extends BaseMapper<Admins> {
    Admins getAdminsByCodeByPass(Admins admins);
}