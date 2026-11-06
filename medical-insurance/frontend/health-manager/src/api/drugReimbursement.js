// src/api/drugReimbursement.js
import axios from 'axios'

// 配置基础路径（若全局已配置可省略）
const request = axios.create({
  baseURL: 'http://127.0.0.1:8081/drugreimbursement'
})

// 药品保险报销相关接口
export default {
  // 获取所有报销等级列表
  getAllList() {
    return request.get('/listAll')
  },
  // 根据ID获取详情
  getById(id) {
    return request.get(`/listById?id=${id}`)
  },
  // 新增报销等级
  add(data) {
    return request.post('/add', data)
  },
  // 更新报销等级
  updateById(data) {
    return request.post('/updateById', data)
  },
  // 根据ID删除
  deleteById(id) {
    return request.get(`/deleteById?id=${id}`)
  }
}