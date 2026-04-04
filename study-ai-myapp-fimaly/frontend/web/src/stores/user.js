import { defineStore } from 'pinia'
import api from '../utils/axios'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null,
    token: localStorage.getItem('token') || null,
    loading: false,
    error: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    currentUser: (state) => state.user
  },

  actions: {
    async login(credentials) {
      this.loading = true
      this.error = null
      try {
        const loginData = {
          email: String(credentials.email),
          password: String(credentials.password)
        }
        const response = await api.post('/auth/login', loginData, {
          headers: { 'Content-Type': 'application/json' }
        })
        
        // 从新的返回格式中提取token
        if (response.data.code === 200 && response.data.data) {
          this.token = response.data.data.token
          localStorage.setItem('token', this.token)
          await this.fetchCurrentUser()
        } else {
          throw new Error(response.data.message || 'Login failed')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async register(credentials) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/auth/register', credentials)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          await this.login({
            email: credentials.email,
            password: credentials.password
          })
        } else {
          throw new Error(response.data.message || 'Registration failed')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchCurrentUser() {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/auth/me')
        
        // 从新的返回格式中提取用户数据
        if (response.data.code === 200 && response.data.data) {
          this.user = response.data.data
          localStorage.setItem('user', JSON.stringify(this.user))
        } else {
          throw new Error(response.data.message || 'Failed to fetch user')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        this.logout()
      } finally {
        this.loading = false
      }
    },

    async updateProfile(profileData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put('/users/profile', profileData)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          this.user = response.data.data
          localStorage.setItem('user', JSON.stringify(this.user))
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update profile')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async changePassword(passwordData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put('/users/password', passwordData)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200) {
          return response.data
        } else {
          throw new Error(response.data.message || 'Failed to change password')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async updatePrivacySettings(settings) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put('/users/privacy', settings)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          this.user = response.data.data
          localStorage.setItem('user', JSON.stringify(this.user))
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update privacy settings')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
