// src/router/index.js
import { createRouter, createWebHashHistory } from 'vue-router'

// 使用动态导入实现懒加载，减少初始包体积
const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const SelectGrid = () => import('../views/SelectGrid.vue')
const SelectAqi = () => import('../views/SelectAqi.vue')
const FeedbackList = () => import('../views/FeedbackList.vue')

const routes = [
  {
    path: '/',          // 根路径默认跳到登录页
    name: 'home',
    component: Login
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/selectGrid',
    name: 'SelectGrid',
    component: SelectGrid
  },
  {
    path: '/selectAqi',
    name: 'SelectAqi',
    component: SelectAqi
  },
  {
    path: '/feedbackList',
    name: 'FeedbackList',
    component: FeedbackList
  }
]

const router = createRouter({
  history: createWebHashHistory(),  // 使用 Hash 模式，URL 形如 /#/login
  routes
})

export default router