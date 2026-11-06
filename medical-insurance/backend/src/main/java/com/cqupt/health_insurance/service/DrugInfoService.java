package com.cqupt.health_insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.DrugInfoDto;
import com.cqupt.health_insurance.pojo.DrugInfo;
import com.cqupt.health_insurance.utils.ResultVo;

public interface DrugInfoService {
	DrugInfo listDrugById(Integer id) ;

    ResultVo<Page<DrugInfo>> selectPageList(DrugInfoDto drugInfoDto) throws Exception;
	
	int deleteById(Integer id);
	
	int add(DrugInfo addDrug);

	
	int update(DrugInfo addDrug);
}
