import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入 Font Awesome 图标库的 CSS
import 'font-awesome/css/font-awesome.min.css'

createApp(App).use(router).mount('#app')