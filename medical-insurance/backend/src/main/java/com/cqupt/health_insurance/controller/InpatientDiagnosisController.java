package com.cqupt.health_insurance.controller;

import com.cqupt.health_insurance.pojo.DiagnosisTreatment;
import com.cqupt.health_insurance.pojo.InpatientDiagnosis;
import com.cqupt.health_insurance.service.InpatientDiagnosisService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//诊疗项目费用信息
@CrossOrigin
@RestController
@RequestMapping("/inpatientdiagnosis")
public class InpatientDiagnosisController {
	@Autowired
	private InpatientDiagnosisService inpatientDiagnosisService;
	@PostMapping("/add")
	public String add(@RequestBody InpatientDiagnosis entity) {
		int num = inpatientDiagnosisService.add(entity);
		if(num == 1) {
			return "成功增加一条记录";
		}else {
			return "增加出现问题，增加了 "+num+"条记录";
		}
	}


	@GetMapping("/selectPageByPatient")
	ResultVo<List<DiagnosisTreatment>> selectPageByPatient(Integer patientId){
		return inpatientDiagnosisService.selectPageByPatient(patientId);
	}

}
