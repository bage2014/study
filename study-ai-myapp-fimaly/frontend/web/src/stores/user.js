import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
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
        const response = await axios.post('/api/auth/login', loginData, {
          headers: { 'Content-Type': 'application/json' }
        })
        
        // 从新的返回格式中提取token
        if (response.data.code === 200 && response.data.data) {
          this.token = response.data.data.token
          localStorage.setItem('token', this.token)
          axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`
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
        const response = await axios.post('/api/auth/register', credentials)
        
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
        const response = await axios.get('/api/auth/me')
        
        // 从新的返回格式中提取用户数据
        if (response.data.code === 200 && response.data.data) {
          this.user = response.data.data
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

    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      delete axios.defaults.headers.common['Authorization']
    }
  }
})
