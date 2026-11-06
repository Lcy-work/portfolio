import { createRouter, createWebHashHistory } from 'vue-router'
import Index from '../views/Index.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Index
  },
  {
    path: '/index',
    name: 'Index',
    component: Index
  }
]

const router = createRouter({
  history: createWebHashHistory(),  // 使用 Hash 模式，避免部署时 404
  routes
})

export default router