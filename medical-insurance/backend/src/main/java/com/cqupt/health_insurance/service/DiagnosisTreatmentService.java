package com.cqupt.health_insurance.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.DiagnosisTreatmentDto;
import com.cqupt.health_insurance.pojo.DiagnosisTreatment;
import com.cqupt.health_insurance.utils.ResultVo;


public interface DiagnosisTreatmentService {
	DiagnosisTreatment listById(Integer id) ;

	ResultVo<Page<DiagnosisTreatment>> selectPageList(DiagnosisTreatmentDto diagnosisTreatmentDto) throws Exception;
	
	int deleteById(Integer id);
	
	int add(DiagnosisTreatment addDrug);
	
	int update(DiagnosisTreatment addDrug);
}
