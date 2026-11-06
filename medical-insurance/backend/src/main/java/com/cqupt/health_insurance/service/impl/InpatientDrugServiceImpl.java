package com.cqupt.health_insurance.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.cqupt.health_insurance.mapper.DrugInfoMapper;
import com.cqupt.health_insurance.mapper.InpatientDrugMapper;
import com.cqupt.health_insurance.pojo.DrugInfo;
import com.cqupt.health_insurance.pojo.InpatientDrug;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.health_insurance.service.InpatientDrugService;


@Service
public class InpatientDrugServiceImpl implements InpatientDrugService {
	@Autowired
	private InpatientDrugMapper inpatientDrugMapper;
	@Autowired
	private DrugInfoMapper drugInfoMapper;

	@Override
	public int add(InpatientDrug entity) {
		return inpatientDrugMapper.insert(entity);
	}
	@Override
	public ResultVo<List<DrugInfo>> selectPageByPatient(Integer patientId) {

		// 1. 查询患者关联的住院药品记录
		QueryWrapper<InpatientDrug> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("patient_id", patientId);
		List<InpatientDrug> inpatientDrugList = inpatientDrugMapper.selectList(queryWrapper);

		// 2. 若没有关联记录，直接返回空列表
		if (inpatientDrugList.isEmpty()) {
			return ResultVo.ok(new ArrayList<>());
		}

		// 3. 优化查询：批量查询药品信息（替代循环查询，提升性能）
		List<Integer> drugIds = new ArrayList<>();
		for (InpatientDrug entity : inpatientDrugList) {
			drugIds.add(entity.getDrugId());  // 收集所有药品ID
		}

		// 批量查询药品信息（避免N+1查询问题）
		QueryWrapper<DrugInfo> drugQueryWrapper = new QueryWrapper<>();
		drugQueryWrapper.in("id", drugIds);  // 使用in条件批量查询
		List<DrugInfo> drugList = drugInfoMapper.selectList(drugQueryWrapper);

		// 4. 用ResultVo封装结果（统一响应格式）
		return ResultVo.ok(drugList);
	}

}
