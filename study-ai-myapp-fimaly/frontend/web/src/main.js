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

const app = createApp(App)
app.use(router)
app.use(store)
app.mount('#app')
