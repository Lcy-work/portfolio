package com.cqupt.health_insurance.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cqupt.health_insurance.dto.DiagnosisTreatmentDto;
import com.cqupt.health_insurance.mapper.DiagnosisTreatmentMapper;
import com.cqupt.health_insurance.pojo.DiagnosisTreatment;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cqupt.health_insurance.service.DiagnosisTreatmentService;

@Service
public class DiagnosisTreatmentServiceImpl implements DiagnosisTreatmentService{
	@Autowired
	private DiagnosisTreatmentMapper diagnosisTreatmentMapper;

	@Override
	public DiagnosisTreatment listById(Integer id) {
		return diagnosisTreatmentMapper.selectById(id);
	}

	//分页查询
	@Override
	public ResultVo<Page<DiagnosisTreatment>> selectPageList(DiagnosisTreatmentDto diagnosisTreatmentDto) throws Exception {
		// 创建分页对象，pageNumber为当前页码，pageSize为每页记录数
		Page<DiagnosisTreatment> page = new Page<>(diagnosisTreatmentDto.getPageNumber(), diagnosisTreatmentDto.getPageSize());

		// 构建查询条件
		QueryWrapper<DiagnosisTreatment> qw = new QueryWrapper<>();
		if (diagnosisTreatmentDto.getMedicalName() != null && !diagnosisTreatmentDto.getMedicalName().isEmpty()) {
			qw.like("medical_name", diagnosisTreatmentDto.getMedicalName());
		}

		// 执行分页查询
		Page<DiagnosisTreatment> pageData = diagnosisTreatmentMapper.selectPage(page, qw);
		// 封装前端需要的格式：data为数组，total为总条数
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pageData.getRecords()); // 表格数据（数组）
		resultMap.put("total", pageData.getTotal()); // 总记录数

		// 返回统一格式的结果
		return ResultVo.ok(pageData);
	}


	@Override
	public int deleteById(Integer id) {

		return diagnosisTreatmentMapper.deleteById(id);
	}

	@Override
	public int add(DiagnosisTreatment addDrug) {
		int n = diagnosisTreatmentMapper.insert(addDrug);
		return n;
	}

	@Override
	public int update(DiagnosisTreatment addDrug) {
		int n = diagnosisTreatmentMapper.updateById(addDrug);
		return n;
	}
}
