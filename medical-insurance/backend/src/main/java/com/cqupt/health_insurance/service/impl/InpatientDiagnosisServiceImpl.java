package com.cqupt.health_insurance.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.cqupt.health_insurance.mapper.DiagnosisTreatmentMapper;
import com.cqupt.health_insurance.mapper.InpatientDiagnosisMapper;
import com.cqupt.health_insurance.pojo.DiagnosisTreatment;
import com.cqupt.health_insurance.pojo.InpatientDiagnosis;
import com.cqupt.health_insurance.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.health_insurance.service.InpatientDiagnosisService;


@Service
public class InpatientDiagnosisServiceImpl implements InpatientDiagnosisService {
	@Autowired
	private InpatientDiagnosisMapper inpatientDiagnosisMapper;
	@Autowired
	private DiagnosisTreatmentMapper diagnosisTreatmentMapper;

	@Override
	public int add(InpatientDiagnosis entity) {
		return inpatientDiagnosisMapper.insert(entity);
	}
	@Override
	public ResultVo<List<DiagnosisTreatment>> selectPageByPatient(Integer patientId) {
		// 1. 查询所有符合条件的中间表记录
		QueryWrapper<InpatientDiagnosis> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("patient_id", patientId);
		List<InpatientDiagnosis> inpatientDiagnosisList = inpatientDiagnosisMapper.selectList(queryWrapper);

		// 2. 关联查询诊疗记录
		List<DiagnosisTreatment> diagnosisTreatmentList = new ArrayList<>();
		for (InpatientDiagnosis entity : inpatientDiagnosisList) {
			DiagnosisTreatment diagnosisTreatment = diagnosisTreatmentMapper.selectById(entity.getDiagnosisId());
			if (diagnosisTreatment != null) { // 避免空指针
				diagnosisTreatmentList.add(diagnosisTreatment);
			}
		}

		// 3. 按第一个项目风格，用ResultVo封装列表数据
		return ResultVo.ok(diagnosisTreatmentList);
	}
	
}
