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
    isLoggedIn: (state) => !!state.token,
    currentUser: (state) => state.user
  },
  
  actions: {
    async login(email, password) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('http://localhost:8080/api/auth/login', {
          email,
          password
        })
        this.token = response.data.token
        localStorage.setItem('token', this.token)
        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`
        await this.fetchUser()
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Login failed'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async register(email, phone, password, nickname) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('http://localhost:8080/api/auth/register', {
          email,
          phone,
          password,
          nickname
        })
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Registration failed'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async fetchUser() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('http://localhost:8080/api/auth/me')
        this.user = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch user'
        throw error
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