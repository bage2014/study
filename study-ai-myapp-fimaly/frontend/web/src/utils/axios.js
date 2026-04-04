import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    // 处理403错误
    if (error.response && error.response.status === 403) {
      // 清除token
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      // 跳转到登录页面
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api