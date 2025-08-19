import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // 配置代理规则
      '/*': {
        target: '*',
        changeOrigin: true
      }
    }
  }
})
