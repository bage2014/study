import axios from 'axios'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || '/api'

const api = axios.create({
  baseURL: apiBaseUrl,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.response.use(
  response => response,
  error => {
    console.error('API请求失败:', error)
    return Promise.reject(error)
  }
)

export const compareService = {
  search(productName, addressId) {
    return api.post('/compare/search', { productName, addressId })
  },
  searchGet(productName, addressId) {
    const params = { productName }
    if (addressId) {
      params.addressId = addressId
    }
    return api.get('/compare/search', { params })
  }
}

export const addressService = {
  list() {
    return api.get('/addresses')
  },
  get(id) {
    return api.get(`/addresses/${id}`)
  },
  create(data) {
    return api.post('/addresses', data)
  },
  update(id, data) {
    return api.post(`/addresses/${id}/update`, data)
  },
  delete(id) {
    return api.post(`/addresses/${id}/delete`)
  }
}

export const healthService = {
  check() {
    return api.get('/health')
  }
}
