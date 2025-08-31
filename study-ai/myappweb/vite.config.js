import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())
  // 确保proxyTarget是有效的URL，如果是相对路径则使用默认URL
  let proxyTarget = env.VITE_API_BASE_URL || 'http://localhost:8080'
  
  // 如果proxyTarget是相对路径（以/开头），则使用默认URL
  if (proxyTarget.startsWith('/')) {
    proxyTarget = 'http://localhost:8080'
  }
  
  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    },
    server: {
      proxy: {
        '/api': {
          target: proxyTarget,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      }
    }
  }
})
