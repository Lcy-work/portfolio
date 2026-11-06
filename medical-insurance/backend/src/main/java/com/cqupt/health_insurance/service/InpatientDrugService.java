package com.cqupt.health_insurance.service;


import com.cqupt.health_insurance.pojo.DrugInfo;
import com.cqupt.health_insurance.pojo.InpatientDrug;
import com.cqupt.health_insurance.utils.ResultVo;

import java.util.List;

public interface InpatientDrugService {

	int add(InpatientDrug entity);

	ResultVo<List<DrugInfo>> selectPageByPatient(Integer patientId);

}
