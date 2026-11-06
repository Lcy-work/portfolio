package com.cqupt.health_insurance.service;

public interface InpatientDiseaseService {

	String add(Integer patientId, Integer diseaseId,Integer diseaseType);
	
	String queryDiseaseByPatientid(Integer patientid, Integer diseaseType);

}
