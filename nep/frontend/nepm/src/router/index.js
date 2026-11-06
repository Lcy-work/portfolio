import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Index from '../views/Index.vue'

import FeedbackList from '../views/feedback/FeedbackList.vue'
import StatisticsList from '../views/feedback/StatisticsList.vue'
import StatisticsInfo from '../views/feedback/StatisticsInfo.vue'
import FeedbackInfo from '../views/feedback/FeedbackInfo.vue'
import AssignFeedback from '../views/feedback/AssignFeedback.vue'
import ProvinceItemTotalStatis from '../views/statistics/ProvinceItemTotalStatis.vue'
import AqiDistributeTotalStatis from '../views/statistics/AqiDistributeTotalStatis.vue'
import AqiTrendTotalStatis from '../views/statistics/AqiTrendTotalStatis.vue'
import OtherTotalStatis from '../views/statistics/OtherTotalStatis.vue'

const routes = [
  // 登录页
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  
  // 主框架路由，children 内的路由渲染在 Index.vue 的 <router-view> 中
  {
    path: '/index',
    component: Index,
    children: [
      { path: '', redirect: '/index/feedback/list' },
      { path: 'feedback/list', component: FeedbackList },
      { path: 'feedback/info/:id', component: FeedbackInfo },
      { path: 'feedback/assign/:id', component: AssignFeedback },
      { path: 'statistics/list', component: StatisticsList },
      { path: 'statistics/info/:id', component: StatisticsInfo },
      { path: 'statistics/province', component: ProvinceItemTotalStatis },
      { path: 'statistics/aqi-distribute', component: AqiDistributeTotalStatis },
      { path: 'statistics/aqi-trend', component: AqiTrendTotalStatis },
      { path: 'statistics/other', component: OtherTotalStatis }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),   // Hash 模式，URL 含 # 号，无需服务端配置
  routes
})

export default router