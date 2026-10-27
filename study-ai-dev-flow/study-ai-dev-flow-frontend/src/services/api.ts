import axios from 'axios'
import type { Sample, User } from '@/types'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

export const sampleApi = {
  findAll() {
    return http.get<Sample[]>('/samples')
  },
  findById(id: number) {
    return http.get<Sample>(`/samples/${id}`)
  },
  create(data: { name: string; description?: string }) {
    return http.post<Sample>('/samples', data)
  },
  update(id: number, data: { name?: string; description?: string }) {
    return http.post<Sample>(`/samples/${id}/update`, data)
  },
  delete(id: number) {
    return http.post(`/samples/${id}/delete`)
  },
}

export const userApi = {
  findAll() {
    return http.get<User[]>('/users')
  },
  findById(id: number) {
    return http.get<User>(`/users/${id}`)
  },
  create(data: { username: string; email: string; phone?: string }) {
    return http.post<User>('/users', data)
  },
  update(id: number, data: { username?: string; email?: string; phone?: string }) {
    return http.post<User>(`/users/${id}/update`, data)
  },
  delete(id: number) {
    return http.post(`/users/${id}/delete`)
  },
}
