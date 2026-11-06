import axios from '@/api/util/request';

// // 获取患者分页列表
export const getPatientListPage = (params) => {
  return axios.get('/patientregistration/selectPageList', { params });
};

// 根据ID获取患者详情
export const getPatientById = (id) => {
  return axios.get(`/patientregistration/selectById?id=${id}`);
};

// 添加患者登记
export const addPatientRegistration = (patientData) => {
  return axios.post('/patientregistration/add', patientData);
};