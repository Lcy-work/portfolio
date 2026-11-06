// src/api/hospitalReimbursement.js
import axios from 'axios'

// 创建axios实例并配置基础路径
const request = axios.create({
  baseURL: 'http://localhost:8081/hospitalreimbursement'
})

// 一级医院报销相关接口
export default {
  // 根据医院等级获取费用阶梯列表（固定查询“一级医院”）
  getListByHospital() {
    return request.get('/listByHostpital?hospitalLevel=一级医院')
  },

  // 根据ID获取详情
  getById(id) {
    return request.get(`/listById?id=${id}`)
  },

  // 新增费用阶梯
  add(data) {
    return request.post('/add', data)
  },

  // 根据ID更新费用阶梯
  updateById(data) {
    return request.post('/updateById', data)
  },

  // 根据ID删除费用阶梯
  deleteById(id) {
    return request.get(`/deleteById?id=${id}`)
  }
}