package com.cqupt.health_insurance.service;


import com.cqupt.health_insurance.pojo.DiagnosisTreatment;
import com.cqupt.health_insurance.pojo.InpatientDiagnosis;
import com.cqupt.health_insurance.utils.ResultVo;

import java.util.List;

public interface InpatientDiagnosisService {

	int add(InpatientDiagnosis entity);

    ResultVo<List<DiagnosisTreatment>> selectPageByPatient(Integer patientId);

}
