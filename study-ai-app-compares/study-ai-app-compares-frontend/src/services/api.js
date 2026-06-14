import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

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