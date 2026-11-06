package com.cqupt.health_insurance.controller;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.DiagnosisTreatmentDto;
import com.cqupt.health_insurance.pojo.DiagnosisTreatment;
import com.cqupt.health_insurance.service.DiagnosisTreatmentService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/diagnosistreatment")
public class DiagnosisTreatmentController {
	@Autowired
	private DiagnosisTreatmentService diagnosisTreatmentService;
	
	@GetMapping("/listById")
	public DiagnosisTreatment ListDrugById(Integer id) {
		return diagnosisTreatmentService.listById(id);
	}


	@GetMapping("/selectPageList")
	ResultVo<Page<DiagnosisTreatment>> selectPageList(DiagnosisTreatmentDto diagnosisTreatmentDto) throws Exception{
		return diagnosisTreatmentService.selectPageList(diagnosisTreatmentDto);
	}

	
	@GetMapping("/deleteById")
	public String deleteById(Integer id) {
		int num = diagnosisTreatmentService.deleteById(id);
		String rust = "";
		if(num == 0) {
			rust = "没有删除数据";
		}else {
			rust = "删除成功，删除了"+num+"条记录";
		}
		return rust;
	}
	
	@PostMapping("/delMany")
	public String delMany(@RequestBody Integer[] ids) {
		int num = 0;
		for(int i=0;i<ids.length;i++) {
			int n = diagnosisTreatmentService.deleteById(ids[i]);
			num += n;
		}
		String rust = "";
		if(num == ids.length) {
			rust = "删除成功，删除了"+num+"条记录";
		}else {
			rust = "删除出现问题，只删除了"+num+"条记录";
		}
		return rust;
	}
	
	@PostMapping("/add")
	public String add(@RequestBody DiagnosisTreatment addDrug) {
		int num = diagnosisTreatmentService.add(addDrug);
		String rust = "";
		if(num == 0) {
			rust = "没有增加数据";
		}else {
			rust = "增加成功，增加了"+num+"条记录";
		}
		return rust;
	}
	
	@PostMapping("/update")
	public String update(@RequestBody DiagnosisTreatment addDrug) {
		int num = diagnosisTreatmentService.update(addDrug);
		String rust = "";
		if(num == 0) {
			rust = "没有修改数据";
		}else {
			rust = "修改成功，修改了"+num+"条记录";
		}
		return rust;
	}
}
