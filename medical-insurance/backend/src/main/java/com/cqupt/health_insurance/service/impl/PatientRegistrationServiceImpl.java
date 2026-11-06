package com.cqupt.health_insurance.service.impl;

import com.cqupt.health_insurance.dto.PatientRegistrationDto;
import com.cqupt.health_insurance.mapper.PatientRegistrationMapper;
import com.cqupt.health_insurance.pojo.PatientRegistration;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cqupt.health_insurance.service.PatientRegistrationService;

import java.util.HashMap;
import java.util.Map;

@Service
public class PatientRegistrationServiceImpl implements PatientRegistrationService {
	@Autowired
	private PatientRegistrationMapper patientRegistrationMapper;

	@Override
	public int add(PatientRegistration entity) {
		return patientRegistrationMapper.insert(entity);
	}

	@Override
	public ResultVo<Page<PatientRegistration>> selectPageList(PatientRegistrationDto patientRegistrationDto) throws Exception {
		// 创建分页对象，pageNumber为当前页码，pageSize为每页记录数
		Page<PatientRegistration> page = new Page<>(patientRegistrationDto.getPageNumber(), patientRegistrationDto.getPageSize());

		// 构建查询条件
		QueryWrapper<PatientRegistration> qw = new QueryWrapper<>();
		if (patientRegistrationDto.getRealName() != null && !patientRegistrationDto.getRealName().isEmpty()) {
			qw.like("real_name", patientRegistrationDto.getRealName());
		}

		// 执行分页查询
		Page<PatientRegistration> pageData = patientRegistrationMapper.selectPage(page, qw);
		// 封装前端需要的格式：data为数组，total为总条数
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pageData.getRecords()); // 表格数据（数组）
		resultMap.put("total", pageData.getTotal()); // 总记录数

		// 返回统一格式的结果
		return ResultVo.ok(pageData);
	}

	@Override
	public PatientRegistration selectById(Integer id) {
		return patientRegistrationMapper.selectById(id);
	}

	@Override
	public int update(PatientRegistration entity) {
		return patientRegistrationMapper.updateById(entity);
	}

}
