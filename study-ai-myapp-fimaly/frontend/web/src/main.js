import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './stores'
import axios from 'axios'

// 配置axios
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.withCredentials = true

// 响应拦截器
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      store.dispatch('user/logout')
    }
    return Promise.reject(error)
  }
)

// Toast吐司提示 - 简单的alert替代方案
let toastId = 0
const toasts = []

function showToast(message, type = 'info', duration = 3000) {
  const id = ++toastId
  const toast = { id, message, type }
  toasts.push(toast)

  setTimeout(() => {
    const index = toasts.findIndex(t => t.id === id)
    if (index > -1) {
      toasts.splice(index, 1)
    }
    updateToastDisplay()
  }, duration)

  updateToastDisplay()
}

function updateToastDisplay() {
  let container = document.getElementById('toast-container')
  if (!container) {
    container = document.createElement('div')
    container.id = 'toast-container'
    container.style.cssText = 'position:fixed;top:20px;right:20px;z-index:9999;display:flex;flex-direction:column;gap:10px;pointer-events:none;'
    document.body.appendChild(container)
  }

  container.innerHTML = toasts.map(t => {
    const colors = {
      success: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
      error: 'linear-gradient(135deg, #ef4444 0%, #dc2626 100%)',
      warning: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)',
      info: 'linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)'
    }
    const icons = {
      success: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />',
      error: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />',
      warning: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />',
      info: '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />'
    }
    return `<div style="display:flex;align-items:center;padding:12px 20px;border-radius:10px;box-shadow:0 4px 12px rgba(0,0,0,0.15);min-width:240px;max-width:400px;pointer-events:auto;background:${colors[t.type] || colors.info};color:white;animation:toast-in 0.3s ease-out;">
      <svg xmlns="http://www.w3.org/2000/svg" style="width:20px;height:20px;flex-shrink:0;margin-right:10px;" fill="none" viewBox="0 0 24 24" stroke="currentColor">${icons[t.type] || icons.info}</svg>
      <span style="font-size:14px;font-weight:500;">${t.message}</span>
    </div>`
  }).join('')
}

window.showToastMessage = showToast

const app = createApp(App)
app.use(router)
app.use(store)
app.mount('#app')
