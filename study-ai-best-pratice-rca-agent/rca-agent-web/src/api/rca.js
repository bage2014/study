import axios from 'axios'

const request = axios.create({
  baseURL: '/api/rca',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    return Promise.reject(error)
  }
)

/**
 * 执行 RCA 分析
 * @param {Object} payload { appId, alarmDescription, alarmTime, scene, enableLlm }
 */
export function analyze(payload) {
  return request.post('/analyze', payload)
}

/**
 * Mock 场景便捷分析
 * @param {string} scene
 * @param {boolean} enableLlm
 */
export function analyzeMock(scene = 'default', enableLlm = false) {
  return request.get('/analyze/mock', { params: { scene, enableLlm } })
}

/**
 * 服务健康检查
 */
export function health() {
  return request.get('/health')
}
