package com.cqupt.health_insurance.service;


import com.cqupt.health_insurance.pojo.InpatientMedical;
import com.cqupt.health_insurance.pojo.MedicalService;
import com.cqupt.health_insurance.utils.ResultVo;

import java.util.List;

public interface InpatientMedicalService {
	int add(InpatientMedical entity);
	ResultVo<List<MedicalService>> selectPageByPatient(Integer patientId);

}
