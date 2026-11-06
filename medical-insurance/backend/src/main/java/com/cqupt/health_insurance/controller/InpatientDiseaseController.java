package com.cqupt.health_insurance.controller;

import com.cqupt.health_insurance.service.InpatientDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/inpatientdisease")
public class InpatientDiseaseController {
	@Autowired
	private InpatientDiseaseService inpatientDiseaseService;
	@GetMapping("/add")
	public String add(Integer patientId,Integer diseaseId,Integer diseaseType) {
		return inpatientDiseaseService.add(patientId,diseaseId,diseaseType);
	}
	@GetMapping("/queryDiseaseByPatientid")
	public String queryDiseaseByPatientid(Integer patientid,Integer diseaseType) {
		String s = inpatientDiseaseService.queryDiseaseByPatientid(patientid,diseaseType);
		return s;
	}
}
