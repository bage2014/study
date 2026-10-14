import axios from 'axios'

const api = axios.create({ baseURL: '/api', timeout: 10000 })

export const productApi = {
  list: () => api.get('/products'),
  create: (data) => api.post('/products', data),
  update: (id, data) => api.put(`/products/${id}`, data),
  remove: (id) => api.delete(`/products/${id}`)
}