package com.cqupt.health_insurance.controller;

import java.util.List;

import com.cqupt.health_insurance.pojo.DrugReimbursement;
import com.cqupt.health_insurance.service.DrugReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/drugreimbursement")
public class DrugReimbursementController {
	@Autowired
	private DrugReimbursementService drugReimbursementServcie;
	@GetMapping("/listAll")
	public List<DrugReimbursement> selectAll(){
		return drugReimbursementServcie.selectAll();
	}
	@GetMapping("/listById")
	public DrugReimbursement selectById(Integer id){
		return drugReimbursementServcie.selectById(id);
	}
	@PostMapping("/updateById")
	public String updateById(@RequestBody DrugReimbursement formData){
		Integer n = drugReimbursementServcie.updateById(formData);
		if( n == 1) {
			return "成功修改一条记录";
		}else {
			return "修改出现问题，改了"+n+"条记录";
		}
	}
	@PostMapping("/add")
	public String add(@RequestBody DrugReimbursement formData){
		Integer n = drugReimbursementServcie.add(formData);
		if( n == 1) {
			return "成功增加一条记录，请点击补全详细信息";
		}else {
			return "增加出现问题，增加"+n+"条记录";
		}
	}
	@GetMapping("/deleteById")
	public String deleteById(Integer id){
		Integer n = drugReimbursementServcie.deleteById(id);
		if( n == 1) {
			return "成功删除一条记录";
		}else {
			return "删除出现问题，删除"+n+"条记录";
		}
	}
}
