package com.cqupt.health_insurance.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cqupt.health_insurance.dto.DrugInfoDto;
import com.cqupt.health_insurance.mapper.DrugInfoMapper;
import com.cqupt.health_insurance.pojo.DrugInfo;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.health_insurance.service.DrugInfoService;

@Service
public class DrugInfoServiceImpl implements DrugInfoService{
	@Autowired
	private DrugInfoMapper drugInfoMapper;

	@Override
	public DrugInfo listDrugById(Integer id) {
		return drugInfoMapper.selectById(id);
	}

	@Override
	public ResultVo<Page<DrugInfo>> selectPageList(DrugInfoDto drugInfoDto) throws Exception {
		// 创建分页对象，pageNumber为当前页码，pageSize为每页记录数
		Page<DrugInfo> page = new Page<>(drugInfoDto.getPageNumber(), drugInfoDto.getPageSize());

		// 构建查询条件
		QueryWrapper<DrugInfo> qw = new QueryWrapper<>();
		if (drugInfoDto.getDrugName() != null && !drugInfoDto.getDrugName().isEmpty()) {
			qw.like("china_name", drugInfoDto.getDrugName());
		}

		// 执行分页查询
		Page<DrugInfo> pageData = drugInfoMapper.selectPage(page, qw);
		// 封装前端需要的格式：data为数组，total为总条数
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pageData.getRecords()); // 表格数据（数组）
		resultMap.put("total", pageData.getTotal()); // 总记录数

		// 返回统一格式的结果
		return ResultVo.ok(pageData);
	}

	//删除
	@Override
	public int deleteById(Integer id) {
		return drugInfoMapper.deleteById(id);
	}

	@Override
	public int add(DrugInfo addDrug) {
		if("0".equals(addDrug.getInsuranceType())) {
			addDrug.setInsuranceType("甲类");
		}else if("1".equals(addDrug.getInsuranceType())) {
			addDrug.setInsuranceType("乙类");
		}else if("2".equals(addDrug.getInsuranceType())) {
			addDrug.setInsuranceType("丙类");
		}else {
			addDrug.setInsuranceType("丙类");
		}
		int n = drugInfoMapper.insert(addDrug);
		return n;
	}

	@Override
	public int update(DrugInfo addDrug) {
		if("0".equals(addDrug.getInsuranceType())) {
			addDrug.setInsuranceType("甲类");
		}else if("1".equals(addDrug.getInsuranceType())) {
			addDrug.setInsuranceType("乙类");
		}else if("2".equals(addDrug.getInsuranceType())) {
			addDrug.setInsuranceType("丙类");
		}else {
			addDrug.setInsuranceType("丙类");
		}
		int n = drugInfoMapper.updateById(addDrug);
		return n;
	}
}
