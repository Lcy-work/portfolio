package com.cqupt.health_insurance.service.impl;

import java.util.List;

import com.cqupt.health_insurance.mapper.HospitalReimbursementMapper;
import com.cqupt.health_insurance.pojo.HospitalReimbursement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.health_insurance.service.HospitalReimbursementService;


@Service
public class HospitalReimbursementServiceImpl implements HospitalReimbursementService {
	@Autowired
	private HospitalReimbursementMapper hospitalReimbursementServiceMapper;
	@Override
	public List<HospitalReimbursement> listByHostpital(String hospitalLevel) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("hospital_level", hospitalLevel);
		queryWrapper.orderByAsc("people_type");
		queryWrapper.orderByAsc("min_pay_level");
		return hospitalReimbursementServiceMapper.selectList(queryWrapper);
	}

	@Override
	public HospitalReimbursement listById(Integer id) {
		return hospitalReimbursementServiceMapper.selectById(id);
	}
	@Override
	public Integer updateById(HospitalReimbursement entity) {
		return hospitalReimbursementServiceMapper.updateById(entity);
	}

	@Override
	public Integer add(HospitalReimbursement entity) {
		return hospitalReimbursementServiceMapper.insert(entity);
	}

	@Override
	public Integer deleteById(Integer id) {
		return hospitalReimbursementServiceMapper.deleteById(id);
	}

}
