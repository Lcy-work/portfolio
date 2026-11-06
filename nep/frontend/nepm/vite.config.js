import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 8082,       // 指定前端开发服务器端口
    strictPort: true, // 端口被占用时直接报错，不自动切换到5173
    proxy: {
      '/nepm': {
        target: 'http://localhost:8080',   // 后端 Spring Boot 地址
        changeOrigin: true,
        // 后端 context-path 是 /nepm，不需要 rewrite
      }
    }
  }
})