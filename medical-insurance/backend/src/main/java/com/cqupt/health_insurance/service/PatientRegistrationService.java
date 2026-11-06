package com.cqupt.health_insurance.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.dto.PatientRegistrationDto;
import com.cqupt.health_insurance.pojo.PatientRegistration;
import com.cqupt.health_insurance.utils.ResultVo;

public interface PatientRegistrationService {

	int add(PatientRegistration entity);

	ResultVo<Page<PatientRegistration>> selectPageList(PatientRegistrationDto patientRegistrationDto) throws Exception;

	PatientRegistration selectById(Integer id);

	int update(PatientRegistration entity);

}
