// src/api/diagnosisTreatment.js
import axios from 'axios';

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8081/diagnosistreatment',
  timeout: 5000
});

// 导出诊疗项目相关API方法
export const diagnosisTreatmentApi = {
  // 添加诊疗项目
  add(data) {
    return service.post('add', data);
  },
  // 分页查询列表
  getPageList(params) {
    return service.get('selectPageList', { params });
  },
  // 单个删除
  deleteById(id) {
    return service.get('deleteById', { params: { id } });
  },
  // 批量删除
  deleteBatch(ids) {
    return service.post('delMany', ids);
  },
  // 根据ID查询详情（用于修改时回显数据）
  getById(id) {
    return service.get('listById', { params: { id } });
  },
  // 更新诊疗项目（修改操作）
  update(data) {
    return service.post('update', data);
  }
};