package com.cqupt.health_insurance.controller;

import java.util.List;

import com.cqupt.health_insurance.pojo.HospitalReimbursement;
import com.cqupt.health_insurance.service.HospitalReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/hospitalreimbursement")
public class HospitalReimbursementController {
	@Autowired
	private HospitalReimbursementService hospitalReimbursementService;
	@GetMapping("/listByHostpital")
	public List<HospitalReimbursement> listByHostpital(String hospitalLevel){
		return hospitalReimbursementService.listByHostpital(hospitalLevel);
	}
	@GetMapping("/listById")
	public HospitalReimbursement listById(Integer id) {
		return hospitalReimbursementService.listById(id);
	}
	@PostMapping("/updateById")
	public String updateById(@RequestBody HospitalReimbursement entity) {
		Integer n = hospitalReimbursementService.updateById(entity);
		if( n == 1) {
			return "成功修改一条记录";
		}else {
			return "修改出现问题，改了"+n+"条记录";
		}
	}
	@PostMapping("/add")
	public String add(@RequestBody HospitalReimbursement entity) {
		Integer n = hospitalReimbursementService.add(entity);
		if( n == 1) {
			return "成功新增一条记录，请在右侧补全信息";
		}else {
			return "新增出现问题，增加了"+n+"条记录";
		}
	}
	@GetMapping("/deleteById")
	public String deleteById(Integer id) {
		Integer n = hospitalReimbursementService.deleteById(id);
		if( n == 1) {
			return "成功删除一条记录";
		}else {
			return "删除出现问题，删除了"+n+"条记录";
		}
	}
}
