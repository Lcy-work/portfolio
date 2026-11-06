import axios from '@/api/util/request';

// 获取药品分页列表
export const getDrugListPage = (params) => {
  return axios.get(`/druginfo/selectPageList`,{params});
};

// 添加药品医嘱
export const addDrugOrder = (orderData) => {
  return axios.post('/inpatientdrug/add', orderData);
};