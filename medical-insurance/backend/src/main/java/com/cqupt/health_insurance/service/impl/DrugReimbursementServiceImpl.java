package com.cqupt.health_insurance.service.impl;

import java.util.List;

import com.cqupt.health_insurance.mapper.DrugReimbursementMapper;
import com.cqupt.health_insurance.pojo.DrugReimbursement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cqupt.health_insurance.service.DrugReimbursementService;


@Service
public class DrugReimbursementServiceImpl implements DrugReimbursementService {
	@Autowired
	private DrugReimbursementMapper drugReimbursementMapper;

	@Override
	public List<DrugReimbursement> selectAll() {
		return drugReimbursementMapper.selectList(null);
	}

	@Override
	public DrugReimbursement selectById(Integer id) {
		return drugReimbursementMapper.selectById(id);
	}

	@Override
	public Integer updateById(DrugReimbursement formData) {
		Integer n = drugReimbursementMapper.updateById(formData);
		return n;
	}

	@Override
	public Integer add(DrugReimbursement formData) {
		return drugReimbursementMapper.insert(formData);
	}

	@Override
	public Integer deleteById(Integer id) {
		return drugReimbursementMapper.deleteById(id);
	}
}
