package com.cqupt.health_insurance.controller;

import com.cqupt.health_insurance.pojo.DrugInfo;
import com.cqupt.health_insurance.pojo.InpatientDrug;
import com.cqupt.health_insurance.service.InpatientDrugService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


//患者药品费用信息
@CrossOrigin
@RestController
@RequestMapping("/inpatientdrug")
public class InpatientDrugController {
	@Autowired
	private InpatientDrugService inpatientDrugService;
	
	@PostMapping("/add")
	public String add(@RequestBody InpatientDrug formData) {
		int num = inpatientDrugService.add(formData);
		if(num == 1) {
			return "成功增加一条记录";
		}else {
			return "增加出现问题，增加了 "+num+"条记录";
		}
	}

	@GetMapping("/selectPageByPatient")
	ResultVo<List<DrugInfo>> selectPageByPatient(Integer patientId){
		return inpatientDrugService.selectPageByPatient(patientId);
	}
}
