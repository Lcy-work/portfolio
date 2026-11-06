// src/main.js（最终优化版 - 最大化启动速度）
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'

// 按需导入使用的图标，而非全量导入
import { User, Lock, View, Hide, UserFilled, Calendar, Male, Female } from '@element-plus/icons-vue'

import 'font-awesome/css/font-awesome.min.css'

// 配置 axios 基础路径 - 使用 /api 前缀通过代理访问后端
axios.defaults.baseURL = '/api' // 通过代理访问后端
axios.defaults.timeout = 10000 // 设置超时时间为10秒

// 允许携带 cookie
axios.defaults.withCredentials = true

// 路由守卫：在每次路由跳转前执行
router.beforeEach(function(to, from, next) {
  // 从 sessionStorage 中取出登录信息
  let supervisor = sessionStorage.getItem('supervisor');

  // 以下三个页面需要登录才能访问
  if (to.path == '/selectGrid' || to.path == '/selectAqi' || to.path == '/feedbackList') {
    if (supervisor == null) {
      router.push('/login');  // 未登录则强制跳到登录页
    }
  }
  next();  // 放行
});

const app = createApp(App)

// 按需注册使用的图标组件
app.component('User', User)
app.component('Lock', Lock)
app.component('View', View)
app.component('Hide', Hide)
app.component('UserFilled', UserFilled)
app.component('Calendar', Calendar)
app.component('Male', Male)
app.component('Female', Female)

// 提供 axios 实例给所有组件使用
app.provide('axios', axios)

app.use(ElementPlus)
app.use(router)
app.mount('#app')