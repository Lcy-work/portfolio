import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 导入 Element Plus 框架及其样式
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';

// ★ 导入 Element Plus 中文语言包
import zhCn from 'element-plus/dist/locale/zh-cn.mjs';

// 引入图标库
import 'font-awesome/css/font-awesome.min.css';

// ★ 路由守卫：实现登录权限验证
// 每次路由跳转前都会执行此函数
router.beforeEach(function(to, from, next) {
    // 从 sessionStorage 中取登录信息
    let admins = sessionStorage.getItem('admins');

    // 如果目标路由不是登录/注册页面，且用户未登录，则强制跳转登录页
    if (!(to.path == '/' || to.path == '/login' || to.path == '/register')) {
        if (admins == null) {
            router.push('/login');
        }
    }
    next();  // 放行，继续路由跳转
});

// 创建 Vue 应用，链式注册插件，最后挂载到 index.html 的 #app 节点
createApp(App)
  .use(store)
  .use(router)
  .use(ElementPlus, { locale: zhCn })  // ★ 配置 Element Plus 为中文
  .mount('#app')
