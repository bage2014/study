import axios from 'axios'
import { v4 as uuidv4 } from 'uuid'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json'
  }
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    config.headers['X-Request-Id'] = uuidv4()
    config.headers['X-Client-Type'] = 'web'
    config.headers['X-Client-Version'] = '1.0.0'
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  (response) => {
    const { data } = response
    
    if (data.code === 200) {
      return data.data
    } else {
      console.error('API Error:', data.message)
      return Promise.reject(new Error(data.message))
    }
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          console.error('未授权，请重新登录')
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          window.location.href = '/login'
          break
        case 403:
          console.error('无权限访问')
          break
        case 404:
          console.error('资源不存在')
          break
        case 500:
          console.error('服务器内部错误')
          break
        default:
          console.error('请求失败:', data?.message || error.message)
      }
    } else if (error.request) {
      console.error('请求超时或网络异常')
    } else {
      console.error('请求配置错误:', error.message)
    }
    
    return Promise.reject(error)
  }
)

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
}

export interface UserDTO {
  id: number
  username: string
  email: string
  nickname: string
  avatar: string
  createdAt: string
}

export interface FamilyDTO {
  id: number
  name: string
  description: string
  creatorId: number
  creatorName: string
  memberCount: number
  createdAt: string
}

export interface FamilyRequest {
  name: string
  description?: string
}

export interface MemberDTO {
  id: number
  name: string
  gender: string
  birthDate: string
  deathDate: string
  occupation: string
  education: string
  phone: string
  email: string
  familyId: number
  createdAt: string
}

export interface MemberRequest {
  name: string
  gender?: string
  birthDate?: string
  deathDate?: string
  occupation?: string
  education?: string
  phone?: string
  email?: string
  familyId: number
}

export interface RelationshipDTO {
  id: number
  member1Id: number
  member1Name: string
  member2Id: number
  member2Name: string
  relationshipType: string
  familyId: number
}

export interface RelationshipRequest {
  member1Id: number
  member2Id: number
  relationshipType: string
  familyId: number
}

export interface HistoricalEventDTO {
  id: number
  title: string
  description: string
  eventDate: string
  location: string
  familyId: number
  createdAt: string
}

export interface HistoricalEventRequest {
  title: string
  description?: string
  eventDate?: string
  location?: string
  familyId: number
}

export interface MultimediaDTO {
  id: number
  type: string
  url: string
  description: string
  familyId: number
  memberId: number
  uploaderId: number
  uploadedAt: string
}

export interface AiRelationshipPredictionDTO {
  predictionId: string
  predictedRelationships: {
    member1: { id: number; name: string; birthYear: number }
    member2: { id: number; name: string; birthYear: number }
    relationship: string
    confidence: number
    reasoning: string[]
    aiComment: string
  }[]
  missingDataSuggestions: string[]
}

export interface AiStoryDTO {
  storyId: string
  title: string
  content: string
  storyType: string
  aiComment: string
}

export const userAPI = {
  login: (data: LoginRequest) => api.post('/users/login', data),
  register: (data: RegisterRequest) => api.post('/users/register', data),
  logout: () => api.post('/users/logout'),
  getCurrentUser: () => api.get<UserDTO>('/users/current'),
  updateProfile: (data: Partial<UserDTO>) => api.put('/users/profile', data)
}

export const familyAPI = {
  create: (data: FamilyRequest) => api.post<FamilyDTO>('/families', data),
  update: (id: number, data: FamilyRequest) => api.put<FamilyDTO>(`/families/${id}`, data),
  delete: (id: number) => api.delete(`/families/${id}`),
  getById: (id: number) => api.get<FamilyDTO>(`/families/${id}`),
  getByUser: () => api.get<FamilyDTO[]>('/families/user'),
  transferAdmin: (id: number, data: { newAdminId: number }) => api.post(`/families/${id}/transfer-admin`, data)
}

export const memberAPI = {
  create: (data: MemberRequest) => api.post<MemberDTO>('/members', data),
  update: (id: number, data: MemberRequest) => api.put<MemberDTO>(`/members/${id}`, data),
  delete: (id: number) => api.delete(`/members/${id}`),
  getById: (id: number) => api.get<MemberDTO>(`/members/${id}`),
  getByFamily: (familyId: number) => api.get<MemberDTO[]>('/members/family', { params: { familyId } }),
  search: (keyword: string) => api.get<MemberDTO[]>('/members/search', { params: { keyword } })
}

export const relationshipAPI = {
  create: (data: RelationshipRequest) => api.post<RelationshipDTO>('/relationships', data),
  delete: (id: number) => api.delete(`/relationships/${id}`),
  getByMember: (memberId: number) => api.get<RelationshipDTO[]>('/relationships/member', { params: { memberId } }),
  getByFamily: (familyId: number) => api.get<RelationshipDTO[]>('/relationships/family', { params: { familyId } })
}

export const eventAPI = {
  create: (data: HistoricalEventRequest) => api.post<HistoricalEventDTO>('/events', data),
  update: (id: number, data: HistoricalEventRequest) => api.put<HistoricalEventDTO>(`/events/${id}`, data),
  delete: (id: number) => api.delete(`/events/${id}`),
  getById: (id: number) => api.get<HistoricalEventDTO>(`/events/${id}`),
  getByFamily: (familyId: number) => api.get<HistoricalEventDTO[]>('/events/family', { params: { familyId } })
}

export const mediaAPI = {
  upload: (data: { familyId: number; memberId?: number; type: string; url: string; description?: string }) => 
    api.post<MultimediaDTO>('/media', data),
  delete: (id: number) => api.delete(`/media/${id}`),
  getById: (id: number) => api.get<MultimediaDTO>(`/media/${id}`),
  getByFamily: (familyId: number) => api.get<MultimediaDTO[]>('/media/family', { params: { familyId } }),
  getByMember: (memberId: number) => api.get<MultimediaDTO[]>('/media/member', { params: { memberId } })
}

export const aiAPI = {
  predictRelationships: (familyId: number) => api.get<AiRelationshipPredictionDTO>('/ai/predict-relationships', { params: { familyId } }),
  generateStory: (familyId: number, storyType: string = 'general') => 
    api.get<AiStoryDTO>('/ai/generate-story', { params: { familyId, storyType } })
}

export default api