import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export const pipelineApi = {
  getAllPipelines: () => api.get('/pipeline'),
  getPipeline: (id) => api.get(`/pipeline/${id}`),
  startPipeline: (data) => api.post('/pipeline/start', data),
  getStages: (id) => api.get(`/pipeline/${id}/stages`),
  getGeneratedFiles: (id) => api.get(`/pipeline/${id}/files`),
  getFileContent: (id, fileName) => api.get(`/pipeline/${id}/files/${fileName}`),
  cancelPipeline: (id) => api.post(`/pipeline/${id}/cancel`)
}

export default api
