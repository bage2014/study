import axios from 'axios'
import { getAuthToken, removeAuthToken } from './auth'
import { useRouter } from 'vue-router'

// 使用环境变量中的API基础URL
const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'

const request = axios.create({
  baseURL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest'
  }
})

request.interceptors.request.use(
  (config) => {
    const token = getAuthToken()
    if (token) {
      config.headers.Authorization = `${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      removeAuthToken()
      const router = useRouter()
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

/**
 * 获取完整的URL链接
 * @param {string} relativeUrl - 相对URL后缀
 * @returns {string} 完整的URL路径
 */
function getFullUrl(relativeUrl) {
  // 确保relativeUrl以/开头
  const formattedUrl = relativeUrl.startsWith('/') ? relativeUrl : `/${relativeUrl}`
  // 移除baseURL末尾的/（如果有）
  const cleanBaseUrl = baseURL.endsWith('/') ? baseURL.slice(0, -1) : baseURL
  return `${cleanBaseUrl}${formattedUrl}`
}

export { getFullUrl }
export default request