import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ForgotPassword from '../views/ForgotPassword.vue'
import FeedbackList from '../views/FeedbackList.vue'
import FeedbackInfo from '../views/FeedbackInfo.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
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
    path: '/forgotPassword',
    name: 'ForgotPassword',
    component: ForgotPassword
  },
  {
    path: '/feedbackList',
    name: 'FeedbackList',
    component: FeedbackList
  },
  {
    path: '/feedbackInfo',
    name: 'FeedbackInfo',
    component: FeedbackInfo
  }
]

const router = createRouter({
  history: createWebHashHistory(),  // Hash 模式，URL 带 #，不需要服务器配置
  routes
})

export default router