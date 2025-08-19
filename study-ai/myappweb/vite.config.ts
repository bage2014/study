import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // 配置代理规则
      '/api': {
        target: 'http://localhost:8080', // 后端API服务器地址
        changeOrigin: true, // 允许跨域
        rewrite: (path) => path.replace(/^\/api/, '') // 重写路径，去掉/api前缀
      }
    }
  }
})
