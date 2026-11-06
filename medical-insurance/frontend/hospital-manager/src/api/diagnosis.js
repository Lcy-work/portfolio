import axios from '@/api/util/request';

// 获取诊疗项目分页列表
export const getDiagnosisTreatmentList = (params) => {
  return axios.get(`/diagnosistreatment/selectPageList`,{params});
};


// 添加诊疗项目医嘱
export const addDiagnosisOrder = (orderData) => {
  return axios.post('/inpatientdiagnosis/add', orderData);
};