package com.cqupt.health_insurance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.PatientRegistrationDto;
import com.cqupt.health_insurance.pojo.PatientRegistration;
import com.cqupt.health_insurance.service.PatientRegistrationService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/patientregistration")
public class PatientRegistrationController {
	@Autowired
	private PatientRegistrationService patientRegistrationService;

	@PostMapping("/add")
	public String add(@RequestBody PatientRegistration entity) {
		int n = patientRegistrationService.add(entity);
		if(n == 1) {
			return "成功登记一位患者";
		}else {
			return "登记失败，加入了"+n+"条记录";
		}
	}
	@PostMapping("/update")
	public String update(@RequestBody PatientRegistration entity) {
		int n = patientRegistrationService.update(entity);
		if(n == 1) {
			return "成功修改一位患者";
		}else {
			return "修改失败，修改了"+n+"条记录";
		}
	}

	@GetMapping("/selectPageList")
	ResultVo<Page<PatientRegistration>> selectPageList(PatientRegistrationDto patientRegistrationDto) throws Exception {
		return patientRegistrationService.selectPageList(patientRegistrationDto);
	}

	@GetMapping("/selectById")
	public PatientRegistration selectById(Integer id) {
		return patientRegistrationService.selectById(id);
	}
}

