// src/api/index.js
import axios from 'axios';

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8081',
  timeout: 5000
});

// 导出所有API方法
export const api = {
  // 参保人员信息相关
  patientRegistration: {
    // 分页查询参保人员
    getPageList(params) {
      return service.get('/patientregistration/selectPageList', { params });
    },
    // 根据ID查询参保人员
    getById(id) {
      return service.get('/patientregistration/selectById', { params: { id } });
    },
    // 更新参保人员信息
    update(data) {
      return service.post('/patientregistration/update', data);
    }
  },
 
  // 住院药品相关
  inpatientDrug: {
    // 根据患者ID查询药品费用
    getByPatientId(patientId) {
      return service.get('/inpatientdrug/selectPageByPatient', { params: { patientId } });
    }
  },

  // 住院诊疗项目相关
  inpatientDiagnosis: {
    // 根据患者ID查询诊疗项目费用
    getByPatientId(patientId) {
      return service.get('/inpatientdiagnosis/selectPageByPatient', { params: { patientId } });
    }
  },

  // 住院医疗服务相关
  inpatientMedical: {
    // 根据患者ID查询医疗服务费用
    getByPatientId(patientId) {
      return service.get('/inpatientmedical/selectPageByPatient', { params: { patientId } });
    }
  },

  // 药品报销比例相关
  drugReimbursement: {
    // 获取所有药品报销比例
    getAll() {
      return service.get('/drugreimbursement/listAll');
    }
  },

  // 医院报销比例相关
  hospitalReimbursement: {
    // 根据医院等级查询报销比例
    getByLevel(level) {
      return service.get('/hospitalreimbursement/listByHostpital', { params: { hospitalLevel: level } });
    }
  },

  // 住院诊断相关
  inpatientDisease: {
    // 根据患者ID和诊断类型查询
    getByPatientAndType(patientId, diseaseType) {
      return service.get('/inpatientdisease/queryDiseaseByPatientid', { 
        params: { patientid: patientId, diseaseType } 
      });
    }
  }
};