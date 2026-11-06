// src/api/secondHospitalReimbursement.js
import axios from 'axios'

// 配置二级医院报销接口基础路径
const request = axios.create({
  baseURL: 'http://localhost:8081/hospitalreimbursement'
})

// 二级医院报销接口集合
export default {
  /**
   * 获取二级医院费用阶梯列表
   * 固定查询参数：hospitalLevel=二级医院
   */
  getSecondHospitalList() {
    return request.get('/listByHostpital?hospitalLevel=二级医院')
  },

  /**
   * 根据ID获取二级医院费用阶梯详情
   * @param {number} id - 数据ID
   */
  getSecondHospitalById(id) {
    return request.get(`/listById?id=${id}`)
  },

  /**
   * 新增二级医院费用阶梯
   * @param {object} data - 新增数据（包含hospitalLevel=二级医院）
   */
  addSecondHospital(data) {
    return request.post('/add', data)
  },

  /**
   * 更新二级医院费用阶梯
   * @param {object} data - 更新后的数据（包含ID）
   */
  updateSecondHospitalById(data) {
    return request.post('/updateById', data)
  },

  /**
   * 删除二级医院费用阶梯
   * @param {number} id - 数据ID
   */
  deleteSecondHospitalById(id) {
    return request.get(`/deleteById?id=${id}`)
  }
}