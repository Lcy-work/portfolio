// src/api/druginfo.js
import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8081/druginfo',
  timeout: 5000
});

// 导出药品信息相关 API 方法
export const drugInfoApi = {
  // 添加药品
  add(data) {
    return service.post('add', data);
  },
  // 分页查询药品列表
  getPageList(params) {
    return service.get('selectPageList', { params });
  },
  // 单个删除药品
  deleteById(id) {
    return service.get('deleteById', { params: { id } });
  },
  // 批量删除药品
  deleteBatch(ids) {
    return service.post('delMany', ids);
  },
  // 根据 ID 查询药品详情（用于修改时回显数据）
  getById(id) {
    return service.get('listById', { params: { id } });
  },
  // 更新药品信息（修改操作）
  update(data) {
    return service.post('update', data);
  }
};