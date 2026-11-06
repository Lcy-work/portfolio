package com.cqupt.health_insurance.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.MedicalServiceDto;
import com.cqupt.health_insurance.pojo.MedicalService;
import com.cqupt.health_insurance.service.MedicalServiceService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/medicalservice")
public class MedicalServiceController {
	@Autowired
	private MedicalServiceService medicalServiceService;
	
	@GetMapping("/listById")
	public MedicalService ListDrugById(Integer id) {
		return medicalServiceService.listById(id);
	}


	@GetMapping("/selectPageList")
	ResultVo<Page<MedicalService>> selectPageList(MedicalServiceDto medicalServiceDto) throws Exception{
		return medicalServiceService.selectPageList(medicalServiceDto);
	}
	
	@GetMapping("/deleteById")
	public String deleteById(Integer id) {
		int num = medicalServiceService.deleteById(id);
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
			int n = medicalServiceService.deleteById(ids[i]);
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
	public String add(@RequestBody MedicalService addDrug) {
		int num = medicalServiceService.add(addDrug);
		String rust = "";
		if(num == 0) {
			rust = "没有增加数据";
		}else {
			rust = "增加成功，增加了"+num+"条记录";
		}
		return rust;
	}
	
	@PostMapping("/update")
	public String update(@RequestBody MedicalService addDrug) {
		int num = medicalServiceService.update(addDrug);
		String rust = "";
		if(num == 0) {
			rust = "没有修改数据";
		}else {
			rust = "修改成功，修改了"+num+"条记录";
		}
		return rust;
	}
}
