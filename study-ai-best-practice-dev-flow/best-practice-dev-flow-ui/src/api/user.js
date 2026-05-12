const BASE_URL = '/api'

const getToken = () => {
  return localStorage.getItem('token')
}

const setToken = (token) => {
  localStorage.setItem('token', token)
}

const removeToken = () => {
  localStorage.removeItem('token')
}

const getAuthHeaders = () => {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
}

const handleResponse = async (response) => {
  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: '请求失败' }))
    throw new Error(error.message || '请求失败')
  }
  return response.json()
}

export const authUtils = {
  getToken,
  setToken,
  removeToken
}

export const userApi = {
  login: async (username, password) => {
    const response = await fetch(`${BASE_URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username, password })
    })
    const result = await handleResponse(response)
    if (result.token) {
      setToken(result.token)
    }
    return result
  },

  register: async (username, email, password) => {
    const response = await fetch(`${BASE_URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username, email, password })
    })
    return handleResponse(response)
  },

  getAllUsers: async () => {
    const response = await fetch(`${BASE_URL}/users`, {
      headers: {
        ...getAuthHeaders()
      }
    })
    return handleResponse(response)
  },

  getUserById: async (id) => {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
      headers: {
        ...getAuthHeaders()
      }
    })
    return handleResponse(response)
  },

  updateUser: async (id, data) => {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders()
      },
      body: JSON.stringify(data)
    })
    return handleResponse(response)
  },

  deleteUser: async (id) => {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
      method: 'DELETE',
      headers: {
        ...getAuthHeaders()
      }
    })
    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: '删除失败' }))
      throw new Error(error.message || '删除失败')
    }
  }
}

export const trackApi = {
  getTrackPoints: async (userId = 1) => {
    const response = await fetch(`${BASE_URL}/trackpoints?userId=${userId}`, {
      headers: {
        ...getAuthHeaders()
      }
    })
    return handleResponse(response)
  },

  getTrackPointById: async (id) => {
    const response = await fetch(`${BASE_URL}/trackpoints/${id}`, {
      headers: {
        ...getAuthHeaders()
      }
    })
    return handleResponse(response)
  },

  createTrackPoint: async (latitude, longitude, name, description) => {
    const response = await fetch(`${BASE_URL}/trackpoints`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders()
      },
      body: JSON.stringify({ latitude, longitude, name, description })
    })
    return handleResponse(response)
  },

  updateTrackPoint: async (id, data) => {
    const response = await fetch(`${BASE_URL}/trackpoints/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders()
      },
      body: JSON.stringify(data)
    })
    return handleResponse(response)
  },

  deleteTrackPoint: async (id) => {
    const response = await fetch(`${BASE_URL}/trackpoints/${id}`, {
      method: 'DELETE',
      headers: {
        ...getAuthHeaders()
      }
    })
    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: '删除失败' }))
      throw new Error(error.message || '删除失败')
    }
  }
}