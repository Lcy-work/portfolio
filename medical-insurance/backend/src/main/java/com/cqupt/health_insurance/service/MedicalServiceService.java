package com.cqupt.health_insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.MedicalServiceDto;
import com.cqupt.health_insurance.pojo.MedicalService;
import com.cqupt.health_insurance.utils.ResultVo;

public interface MedicalServiceService {

	MedicalService listById(Integer id) ;
	ResultVo<Page<MedicalService>> selectPageList(MedicalServiceDto medicalServiceDto) throws Exception;
	int deleteById(Integer id);
	int add(MedicalService addDrug);
	int update(MedicalService addDrug);
}
