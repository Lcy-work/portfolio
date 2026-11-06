package com.cqupt.health_insurance.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cqupt.health_insurance.dto.MedicalServiceDto;
import com.cqupt.health_insurance.mapper.MedicalServiceMapper;
import com.cqupt.health_insurance.pojo.MedicalService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cqupt.health_insurance.service.MedicalServiceService;
@Service
public class MedicalServiceServiceImpl implements MedicalServiceService{
	@Autowired
	private MedicalServiceMapper medicalServiceMapper;

	@Override
	public MedicalService listById(Integer id) {
		return medicalServiceMapper.selectById(id);
	}

	@Override
	public ResultVo<Page<MedicalService>> selectPageList(MedicalServiceDto medicalServiceDto) throws Exception {
		// 创建分页对象，pageNumber为当前页码，pageSize为每页记录数
		Page<MedicalService> page = new Page<>(medicalServiceDto.getPageNumber(), medicalServiceDto.getPageSize());

		// 构建查询条件
		QueryWrapper<MedicalService> qw = new QueryWrapper<>();
		if (medicalServiceDto.getMedicalName() != null && !medicalServiceDto.getMedicalName().isEmpty()) {
			qw.like("medical_name", medicalServiceDto.getMedicalName());
		}

		// 执行分页查询
		Page<MedicalService> pageData = medicalServiceMapper.selectPage(page, qw);
		// 封装前端需要的格式：data为数组，total为总条数
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pageData.getRecords()); // 表格数据（数组）
		resultMap.put("total", pageData.getTotal()); // 总记录数

		// 返回统一格式的结果
		return ResultVo.ok(pageData);
	}


	//根据id删除数据
	@Override
	public int deleteById(Integer id) {
		return medicalServiceMapper.deleteById(id);
	}

	@Override
	public int add(MedicalService addDrug) {
		int n = medicalServiceMapper.insert(addDrug);
		return n;
	}

	@Override
	public int update(MedicalService addDrug) {
		int n = medicalServiceMapper.updateById(addDrug);
		return n;
	}
}
