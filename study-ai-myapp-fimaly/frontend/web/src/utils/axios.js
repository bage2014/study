import axios from 'axios'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'
const REQUEST_TIMEOUT = 30000
const MAX_RETRY_COUNT = 3

const api = axios.create({
  baseURL: BASE_URL,
  timeout: REQUEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json'
  }
})

let isRefreshing = false
let refreshSubscribers = []

const subscribeTokenRefresh = (callback) => {
  refreshSubscribers.push(callback)
}

const onTokenRefreshed = (token) => {
  refreshSubscribers.forEach(callback => callback(token))
  refreshSubscribers = []
}

api.interceptors.request.use(
  (config) => {
    const startTime = Date.now()
    config.metadata = { startTime }

    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    const traceId = crypto.randomUUID().toString().replace(/-/g, '').substring(0, 16)
    config.headers['X-Trace-Id'] = traceId

    console.log(`[REQUEST] ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`, {
      traceId,
      params: config.params,
      data: config.data
    })

    return config
  },
  (error) => {
    console.error('[REQUEST ERROR]', error)
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  (response) => {
    const { config, data, status } = response
    const duration = Date.now() - (config.metadata?.startTime || Date.now())

    console.log(`[RESPONSE] ${config.method?.toUpperCase()} ${config.url}`, {
      status,
      duration: `${duration}ms`,
      data
    })

    if (data?.code !== undefined && data.code !== 200 && data.code !== 0) {
      console.warn('[BUSINESS ERROR]', data)
    }

    return response
  },
  async (error) => {
    const { config, response } = error
    const duration = config?.metadata?.startTime
      ? Date.now() - config.metadata.startTime
      : 0

    console.error(`[RESPONSE ERROR] ${config?.method?.toUpperCase()} ${config?.url}`, {
      status: response?.status,
      duration: `${duration}ms`,
      error: error.message,
      data: response?.data
    })

    if (!response) {
      return handleNetworkError(error)
    }

    switch (response.status) {
      case 400:
        return handleBadRequest(response.data)
      case 401:
        return handleUnauthorized(config, response.data)
      case 403:
        return handleForbidden(response.data)
      case 404:
        return handleNotFound(response.data)
      case 500:
        return handleServerError(response.data)
      default:
        return Promise.reject(createError(response.data))
    }
  }
)

function handleNetworkError(error) {
  const err = new Error('网络连接失败，请检查网络设置')
  err.type = 'NETWORK_ERROR'
  err.detail = error.message
  return Promise.reject(err)
}

function handleBadRequest(data) {
  const message = data?.message || '请求参数错误'
  const err = new Error(message)
  err.type = 'BAD_REQUEST'
  err.code = data?.errorCode || 'G002'
  err.detail = data
  return Promise.reject(err)
}

async function handleUnauthorized(config, data) {
  if (isRefreshing) {
    return new Promise((resolve) => {
      subscribeTokenRefresh((token) => {
        config.headers.Authorization = `Bearer ${token}`
        resolve(api(config))
      })
    })
  }

  const refreshToken = localStorage.getItem('refreshToken')

  if (!refreshToken) {
    redirectToLogin()
    return Promise.reject(new Error('请重新登录'))
  }

  isRefreshing = true

  try {
    const refreshResponse = await axios.post(`${BASE_URL}/auth/refresh`, {
      refreshToken
    })

    const { token, refreshToken: newRefreshToken } = refreshResponse.data.data

    localStorage.setItem('token', token)
    localStorage.setItem('refreshToken', newRefreshToken)

    onTokenRefreshed(token)

    config.headers.Authorization = `Bearer ${token}`
    return api(config)
  } catch (refreshError) {
    console.error('[Token Refresh Failed]', refreshError)
    redirectToLogin()
    return Promise.reject(refreshError)
  } finally {
    isRefreshing = false
    refreshSubscribers = []
  }
}

function handleForbidden(data) {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  redirectToLogin()

  const err = new Error(data?.message || '登录已过期，请重新登录')
  err.type = 'FORBIDDEN'
  err.code = 'G005'
  return Promise.reject(err)
}

function handleNotFound(data) {
  const err = new Error(data?.message || '请求的资源不存在')
  err.type = 'NOT_FOUND'
  err.code = 'G003'
  return Promise.reject(err)
}

function handleServerError(data) {
  const err = new Error(data?.message || '服务器内部错误')
  err.type = 'SERVER_ERROR'
  err.code = 'G001'
  err.detail = data
  return Promise.reject(err)
}

function redirectToLogin() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')

  if (window.location.pathname !== '/login') {
    window.location.href = '/login'
  }
}

function createError(data) {
  const err = new Error(data?.message || '请求失败')
  err.type = 'UNKNOWN_ERROR'
  err.code = data?.errorCode
  err.detail = data
  return err
}

export function createFormData(data) {
  const formData = new FormData()
  Object.keys(data).forEach(key => {
    if (data[key] !== undefined && data[key] !== null) {
      formData.append(key, data[key])
    }
  })
  return formData
}

export function downloadFile(blob, filename) {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}

export async function requestWithRetry(url, options = {}, retryCount = 0) {
  try {
    return await api(url, options)
  } catch (error) {
    if (retryCount < MAX_RETRY_COUNT && shouldRetry(error)) {
      console.log(`[RETRY] Attempt ${retryCount + 1} for ${url}`)
      await delay(Math.pow(2, retryCount) * 1000)
      return requestWithRetry(url, options, retryCount + 1)
    }
    throw error
  }
}

function shouldRetry(error) {
  return !error.response ||
         error.response.status >= 500 ||
         error.type === 'NETWORK_ERROR'
}

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}

export default api