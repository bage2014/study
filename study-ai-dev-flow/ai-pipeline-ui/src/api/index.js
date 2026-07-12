import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 60000
})

api.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error.response?.data || error.message)
    return Promise.reject(error)
  }
)

export const pipelineApi = {
  getAllPipelines: () => api.get('/pipeline'),
  getPipeline: (id) => api.get(`/pipeline/${id}`),
  startPipeline: (data) => api.post('/pipeline/start', data),
  getStages: (id) => api.get(`/pipeline/${id}/stages`),
  getGeneratedFiles: (id) => api.get(`/pipeline/${id}/files`),
  getFileContent: (id, fileName) => api.get(`/pipeline/${id}/files/${fileName}`),
  cancelPipeline: (id) => api.post(`/pipeline/${id}/cancel`),
  getPipelinesByProject: (projectId) => api.get(`/pipeline/project/${projectId}`),
  approvePipeline: (pipelineId, stage, approved, reviewer, comment) => api.post(
    `/pipeline/${pipelineId}/approval/${stage}`,
    { approved, reviewer, comment }
  ),
  getApproval: (pipelineId, stage) => api.get(`/pipeline/${pipelineId}/approval/${stage}`)
}

export const projectApi = {
  getAllProjects: (params) => api.get('/project', { params }),
  getProject: (id) => api.get(`/project/${id}`),
  getProjectDetail: (id) => api.get(`/project/${id}/detail`),
  createProject: (data) => api.post('/project', data),
  updateProject: (id, data) => api.put(`/project/${id}`, data),
  deleteProject: (id) => api.delete(`/project/${id}`),
  getProjectStats: () => api.get('/project/stats')
}

export const requirementApi = {
  getRequirementsByProject: (projectId) => api.get(`/requirement/project/${projectId}`),
  getProjectRequirementStats: (projectId) => api.get(`/requirement/project/${projectId}/stats`),
  getRequirementsByStatus: (projectId, status) => api.get(`/requirement/project/${projectId}/${status}`),
  getRequirement: (id) => api.get(`/requirement/${id}`),
  getRequirementByPipelineId: (pipelineId) => api.get(`/requirement/pipeline/${pipelineId}`),
  createRequirement: (data) => api.post('/requirement', data),
  updateRequirement: (id, data) => api.put(`/requirement/${id}`, data),
  deleteRequirement: (id) => api.delete(`/requirement/${id}`)
}

export default api
