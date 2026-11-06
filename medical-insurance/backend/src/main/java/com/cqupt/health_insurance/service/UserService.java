package com.cqupt.health_insurance.service;

import java.util.List;
import com.cqupt.health_insurance.pojo.SysUser;
import com.cqupt.health_insurance.utils.ResultVo;


public interface UserService {

	List<SysUser> selectList(String username);
	ResultVo<SysUser> login(String username, String password);

}
