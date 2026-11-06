package com.cqupt.health_insurance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.DiseaseDto;
import com.cqupt.health_insurance.pojo.DiseaseInfo;
import com.cqupt.health_insurance.service.DiseaseService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/diseaseinfo")
public class DiseaseController {
	@Autowired
	private DiseaseService diseaseService;

	@GetMapping("/ListPage")
	ResultVo<Page<DiseaseInfo>> selectPageList(DiseaseDto diseaseDto) throws Exception{
		return diseaseService.selectPageList(diseaseDto);
	}
}
