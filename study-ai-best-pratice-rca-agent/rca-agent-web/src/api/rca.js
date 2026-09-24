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

/**
 * 分析历史列表（分页）
 */
export function getHistory(page = 0, size = 20) {
  return request.get('/history', { params: { page, size } })
}

/**
 * 分析历史详情（含请求、响应、录制）
 */
export function getHistoryDetail(id) {
  return request.get(`/history/${id}`)
}

/**
 * 删除分析历史
 */
export function deleteHistory(id) {
  return request.delete(`/history/${id}`)
}

/**
 * 录制列表
 */
export function getRecordings() {
  return request.get('/recordings')
}

/**
 * 录制详情（全部交互明细）
 */
export function getRecordingDetail(id) {
  return request.get(`/recordings/${id}`)
}

/**
 * 基于历史录制回放
 */
export function replay(id) {
  return request.post(`/replay/${id}`)
}
