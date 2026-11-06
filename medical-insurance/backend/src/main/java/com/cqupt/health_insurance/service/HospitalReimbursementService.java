package com.cqupt.health_insurance.service;

import com.cqupt.health_insurance.pojo.HospitalReimbursement;
import java.util.List;

public interface HospitalReimbursementService {

	List<HospitalReimbursement> listByHostpital(String hospitalLevel);

	HospitalReimbursement listById(Integer id);

	Integer updateById(HospitalReimbursement entity);

	Integer add(HospitalReimbursement entity);

	Integer deleteById(Integer id);

}
