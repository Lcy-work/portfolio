package com.cqupt.health_insurance.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.cqupt.health_insurance.mapper.DiseaseMapper;
import com.cqupt.health_insurance.mapper.InpatientDiseaseMapper;
import com.cqupt.health_insurance.pojo.DiseaseInfo;
import com.cqupt.health_insurance.pojo.InpatientDisease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.health_insurance.service.InpatientDiseaseService;


@Service
public class InpatientDiseaseServiceImpl implements InpatientDiseaseService {
	@Autowired
	private InpatientDiseaseMapper inpatientDiseaseMapper;
	@Autowired
	private DiseaseMapper diseaseMapper;

	@Override
	public String add(Integer patientId, Integer diseaseId,Integer diseaseType) {
		InpatientDisease entity = new InpatientDisease();
		entity.setPatientId(patientId);
		entity.setDiseaseId(diseaseId);
		entity.setOrderTime(new Timestamp(new Date().getTime()));
		entity.setDiseaseType(diseaseType);
		int num = inpatientDiseaseMapper.insert(entity);
		if(num == 0) {
			return "没有增加数据";
		}
		return "增加成功，增加了"+num+"条记录";
	}
	@Override
	public String queryDiseaseByPatientid(Integer patientid, Integer diseaseType) {
		QueryWrapper<InpatientDisease> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("patient_id", patientid);
		queryWrapper.eq("disease_type", diseaseType);
		List<InpatientDisease> inpatientDiagnosisList = inpatientDiseaseMapper.selectList(queryWrapper);
		StringBuffer strBuffer = new StringBuffer();
		for(InpatientDisease entity:inpatientDiagnosisList) {
//			System.out.println(entity.getDiseaseId());
			DiseaseInfo en = diseaseMapper.selectById(entity.getDiseaseId());
			strBuffer.append(en.getDiseaseName()+" ");
		}
		return strBuffer.toString();
	}

}
