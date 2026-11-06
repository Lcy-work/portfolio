package com.cqupt.health_insurance.service;


import com.cqupt.health_insurance.pojo.DrugReimbursement;
import java.util.List;

public interface DrugReimbursementService {

	public List<DrugReimbursement> selectAll();

	public DrugReimbursement selectById(Integer id);

	public Integer updateById(DrugReimbursement formData);

	public Integer add(DrugReimbursement formData);

	public Integer deleteById(Integer id);
}
