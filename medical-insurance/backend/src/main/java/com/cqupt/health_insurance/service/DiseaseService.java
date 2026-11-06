package com.cqupt.health_insurance.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.DiseaseDto;
import com.cqupt.health_insurance.pojo.DiseaseInfo;
import com.cqupt.health_insurance.utils.ResultVo;

public interface DiseaseService {

	ResultVo<Page<DiseaseInfo>> selectPageList(DiseaseDto diseaseDto) throws Exception;

}
