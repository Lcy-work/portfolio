package com.cqupt.health_insurance.service.impl;

import com.cqupt.health_insurance.mapper.UserMapper;
import com.cqupt.health_insurance.pojo.SysUser;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.health_insurance.service.UserService;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Override
	public List<SysUser> selectList(String username) {
		QueryWrapper<SysUser> qw = new QueryWrapper<>();
		qw.eq("login_name",username);
		return userMapper.selectList(qw);
	}

	@Override
	public ResultVo<SysUser> login(String username, String password) {
		// 调用服务层方法查询用户列表
		List<SysUser> list = selectList(username);

		// 检查用户名是否存在
		if (list == null || list.size() == 0) {
			return ResultVo.fail("用户名输入错误");
		}

		// 遍历用户列表，检查密码是否匹配
		for (SysUser user : list) {
			if (user.getPassword().equals(password)) {
				return ResultVo.ok(user, "登录成功");
			}
		}

		// 密码不匹配，返回失败信息
		return ResultVo.fail("密码输入错误");
	}
}

