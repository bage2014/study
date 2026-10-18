import axios from 'axios'

const api = axios.create({ baseURL: '/api', timeout: 10000 })

export const bookApi = {
  list: () => api.get('/books'),
  create: (data) => api.post('/books', data),
  update: (id, data) => api.put(`/books/${id}`, data),
  remove: (id) => api.delete(`/books/${id}`)
}
