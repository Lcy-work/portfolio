package com.cqupt.health_insurance.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.DrugInfoDto;
import com.cqupt.health_insurance.pojo.DrugInfo;
import com.cqupt.health_insurance.service.DrugInfoService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/druginfo")
public class DrugInfoController {
	@Autowired
	private DrugInfoService drugInfoService;
	
	@GetMapping("/listById")
	public DrugInfo ListDrugById(Integer id) {
		return drugInfoService.listDrugById(id);
	}


	@GetMapping("/selectPageList")
	ResultVo<Page<DrugInfo>> selectPageList(DrugInfoDto drugInfoDto) throws Exception{
		return drugInfoService.selectPageList(drugInfoDto);
	}
	
	@GetMapping("/deleteById")
	public String deleteById(Integer id) {
		int num = drugInfoService.deleteById(id);
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
			int n = drugInfoService.deleteById(ids[i]);
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
	public String add(@RequestBody DrugInfo addDrug) {
		int num = drugInfoService.add(addDrug);
		String rust = "";
		if(num == 0) {
			rust = "没有增加数据";
		}else {
			rust = "增加成功，增加了"+num+"条记录";
		}
		return rust;
	}
	
	@PostMapping("/update")
	public String update(@RequestBody DrugInfo addDrug) {
		int num = drugInfoService.update(addDrug);
		String rust = "";
		if(num == 0) {
			rust = "没有修改数据";
		}else {
			rust = "修改成功，修改了"+num+"条记录";
		}
		return rust;
	}
}
