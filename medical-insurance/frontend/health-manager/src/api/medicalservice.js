// src/api/medicalServiceApi.js
import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8081/medicalservice',
  timeout: 5000
});

// 导出医疗服务相关 API 方法
export const medicalServiceApi = {
  // 获取分页列表
   getPageList(params) {
    return service.get('selectPageList', { params });
  },
  // 根据 ID 获取单个医疗服务信息
  getById(id) {
    return service.get('listById', { params: { id } });
  },
  // 添加医疗服务
  add(data) {
    return service.post('add', data);
  },
  // 修改医疗服务
  update(data) {
    return service.post('update', data);
  },
  // 根据 ID 删除单个医疗服务
  deleteById(id) {
    return service.get('deleteById', { params: { id } });
  },
  // 批量删除医疗服务
  deleteBatch(ids) {
    return service.post('delMany', ids);
  }
};