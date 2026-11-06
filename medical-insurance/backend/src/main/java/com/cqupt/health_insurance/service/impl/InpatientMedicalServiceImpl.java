package com.cqupt.health_insurance.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.cqupt.health_insurance.mapper.InpatientMedicalMapper;
import com.cqupt.health_insurance.mapper.MedicalServiceMapper;
import com.cqupt.health_insurance.pojo.InpatientMedical;
import com.cqupt.health_insurance.pojo.MedicalService;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.health_insurance.service.InpatientMedicalService;


@Service
public class InpatientMedicalServiceImpl implements InpatientMedicalService {
	@Autowired
	private InpatientMedicalMapper inpatientMedicalMapper;
	@Autowired
	private MedicalServiceMapper medicalServiceMapper;

	@Override
	public int add(InpatientMedical entity) {
		return inpatientMedicalMapper.insert(entity);
	}
	@Override
	public ResultVo<List<MedicalService>> selectPageByPatient(Integer patientId) {

		// 1. 查询患者关联的住院医疗记录
		QueryWrapper<InpatientMedical> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("patient_id", patientId);
		List<InpatientMedical> inpatientMedicalList = inpatientMedicalMapper.selectList(queryWrapper);

		// 2. 若没有关联记录，直接返回空列表（避免后续空循环）
		if (inpatientMedicalList.isEmpty()) {
			return ResultVo.ok(new ArrayList<>());
		}

		// 3. 优化查询：批量获取医疗服务信息（减少数据库交互）
		List<Integer> medicalIds = new ArrayList<>();
		for (InpatientMedical entity : inpatientMedicalList) {
			medicalIds.add(entity.getMedicalId());  // 收集所有医疗服务ID
		}

		// 批量查询医疗服务信息（替代循环单次查询，优化性能）
		QueryWrapper<MedicalService> medicalQueryWrapper = new QueryWrapper<>();
		medicalQueryWrapper.in("id", medicalIds);
		List<MedicalService> medicalServiceList = medicalServiceMapper.selectList(medicalQueryWrapper);

		// 4. 用统一响应格式封装结果
		return ResultVo.ok(medicalServiceList);
	}

}
