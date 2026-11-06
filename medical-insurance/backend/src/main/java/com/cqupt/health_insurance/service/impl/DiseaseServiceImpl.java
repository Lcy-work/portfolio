package com.cqupt.health_insurance.service.impl;

import com.cqupt.health_insurance.dto.DiseaseDto;
import com.cqupt.health_insurance.mapper.DiseaseMapper;
import com.cqupt.health_insurance.pojo.DiseaseInfo;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.service.DiseaseService;

import java.util.HashMap;
import java.util.Map;

@Service
public class DiseaseServiceImpl implements DiseaseService {
	@Autowired
	private DiseaseMapper diseaseMapper;

	@Override
	public ResultVo<Page<DiseaseInfo>> selectPageList(DiseaseDto diseaseDto) throws Exception {
		// 创建分页对象，pageNumber为当前页码，pageSize为每页记录数
		Page<DiseaseInfo> page = new Page<>(diseaseDto.getPageNumber(), diseaseDto.getPageSize());

		// 构建查询条件
		QueryWrapper<DiseaseInfo> qw = new QueryWrapper<>();
		if (diseaseDto.getDiseaseName() != null && !diseaseDto.getDiseaseName().isEmpty()) {
			qw.like("disease_name", diseaseDto.getDiseaseName());
		}

		// 执行分页查询
		Page<DiseaseInfo> pageData = diseaseMapper.selectPage(page, qw);
		// 封装前端需要的格式：data为数组，total为总条数
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pageData.getRecords()); // 表格数据（数组）
		resultMap.put("total", pageData.getTotal()); // 总记录数

		// 返回统一格式的结果
		return ResultVo.ok(pageData);
	}

}
