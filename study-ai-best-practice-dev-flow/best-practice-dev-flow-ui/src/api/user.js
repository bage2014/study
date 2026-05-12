const BASE_URL = 'http://localhost:8080/api'

export const userApi = {
  login: async (username, password) => {
    const response = await fetch(`${BASE_URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username, password })
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || '登录失败')
    }
    return response.json()
  },

  register: async (username, email, password) => {
    const response = await fetch(`${BASE_URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username, email, password })
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || '注册失败')
    }
    return response.json()
  },

  getAllUsers: async () => {
    const response = await fetch(`${BASE_URL}/users`)
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || '获取用户列表失败')
    }
    return response.json()
  },

  getUserById: async (id) => {
    const response = await fetch(`${BASE_URL}/users/${id}`)
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || '获取用户信息失败')
    }
    return response.json()
  },

  updateUser: async (id, data) => {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || '更新用户信息失败')
    }
    return response.json()
  },

  deleteUser: async (id) => {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
      method: 'DELETE'
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || '删除用户失败')
    }
  }
}