import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  build: {
    outDir: '../study-gray-flow/src/main/resources/static',
    emptyOutDir: true
  },
  server: {
    port: 5174,
    proxy: {
      '/api': {
        target: 'http://localhost:8083',
        changeOrigin: true
      }
    }
  }
})