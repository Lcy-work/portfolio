import axios from '@/api/util/request';

// 获取医疗服务分页列表
export const getMedicalServiceList = (params) => {
  return axios.get(`/medicalservice/selectPageList`,{params});
};

// 添加医疗服务医嘱
export const addMedicalServiceOrder = (orderData) => {
  return axios.post('/inpatientmedical/add', orderData);
};