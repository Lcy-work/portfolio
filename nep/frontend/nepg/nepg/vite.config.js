import { fileURLToPath, URL } from 'node:url'
import vue from '@vitejs/plugin-vue'

export default {
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 8083,
    proxy: {
      '/nepm': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
