package com.cqupt.health_insurance.controller;


import com.cqupt.health_insurance.pojo.InpatientMedical;
import com.cqupt.health_insurance.pojo.MedicalService;
import com.cqupt.health_insurance.service.InpatientMedicalService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//患者医疗服务费用信息
@CrossOrigin
@RestController
@RequestMapping("/inpatientmedical")
public class InpatientMedicalController {
	@Autowired
	private InpatientMedicalService inpatientMedicalService;
	@PostMapping("/add")
	public String add(@RequestBody InpatientMedical entity) {
		int num = inpatientMedicalService.add(entity);
		if(num == 1) {
			return "成功增加一条记录";
		}else {
			return "增加出现问题，增加了 "+num+"条记录";
		}
	}

	@GetMapping("/selectPageByPatient")
	ResultVo<List<MedicalService>> selectPageByPatient(Integer patientId) {
		return inpatientMedicalService.selectPageByPatient(patientId);
	}
}
