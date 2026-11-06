import axios from '@/api/util/request';


export const getDiseaseListPage = (params) => {
  return axios.get(`/diseaseinfo/ListPage`, { params });
};

// 添加入院诊断 (疾病类型1)
export const addAdmissionDisease = (patientId, diseaseId) => {
  return axios.get(`/inpatientdisease/add?patientId=${patientId}&diseaseId=${diseaseId}&diseaseType=1`);
};

// 添加主要诊断 (疾病类型2)
export const addMainDisease = (patientId, diseaseId) => {
  return axios.get(`/inpatientdisease/add?patientId=${patientId}&diseaseId=${diseaseId}&diseaseType=2`);
};

// 添加其他诊断 (疾病类型3)
export const addOtherDisease = (patientId, diseaseId) => {
  return axios.get(`/inpatientdisease/add?patientId=${patientId}&diseaseId=${diseaseId}&diseaseType=3`);
};