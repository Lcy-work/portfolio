import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'

// 引入图标库
import 'font-awesome/css/font-awesome.min.css'

// 配置 axios 基础路径
axios.defaults.baseURL = '/nepm/'
axios.defaults.timeout = 10000
axios.defaults.withCredentials = true

// 路由守卫：实现权限认证
router.beforeEach(function(to, from, next){
    // 从前端 session 中获取登录信息
    let gridMember = sessionStorage.getItem('gridMember');
    // 访问需要登录的页面时，检查是否已登录
    if(to.path == '/feedbackList' || to.path == '/feedbackInfo'){
        if(gridMember == null){
            router.push('/login');
        }
    }
    next();
});

const app = createApp(App)

app.use(ElementPlus)
app.use(router)

// 注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 将 axios 注入，子组件通过 inject('axios') 来使用
app.provide('axios', axios)

app.mount('#app')
