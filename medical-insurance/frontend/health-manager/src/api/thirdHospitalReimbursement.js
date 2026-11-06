// src/api/thirdHospitalReimbursement.js
import axios from 'axios'

// 配置三级医院报销接口基础路径
const request = axios.create({
  baseURL: 'http://localhost:8081/hospitalreimbursement'
})

// 三级医院报销接口集合
export default {
  /**
   * 获取三级医院费用阶梯列表
   * 固定查询参数：hospitalLevel=三级医院
   */
  getThirdHospitalList() {
    return request.get('/listByHostpital?hospitalLevel=三级医院')
  },

  /**
   * 根据ID获取三级医院费用阶梯详情
   * @param {number} id - 数据唯一标识ID
   */
  getThirdHospitalById(id) {
    return request.get(`/listById?id=${id}`)
  },

  /**
   * 新增三级医院费用阶梯数据
   * @param {object} data - 新增数据对象（需包含hospitalLevel=三级医院）
   */
  addThirdHospital(data) {
    return request.post('/add', data)
  },

  /**
   * 更新三级医院费用阶梯数据
   * @param {object} data - 待更新数据对象（需包含ID）
   */
  updateThirdHospitalById(data) {
    return request.post('/updateById', data)
  },

  /**
   * 根据ID删除三级医院费用阶梯数据
   * @param {number} id - 数据唯一标识ID
   */
  deleteThirdHospitalById(id) {
    return request.get(`/deleteById?id=${id}`)
  }
}