package com.cqupt.health_insurance.controller;


import com.cqupt.health_insurance.pojo.SysUser;
import com.cqupt.health_insurance.service.UserService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {
	@Autowired
	private UserService userService;


	@GetMapping("/login")
	public ResultVo<SysUser> login(String username, String password) {
		return userService.login(username, password);
	}
}


