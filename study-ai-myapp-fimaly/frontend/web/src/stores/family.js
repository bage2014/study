import { defineStore } from 'pinia'
import api from '../utils/axios'

export const useFamilyStore = defineStore('family', {
  state: () => ({
    families: [],
    currentFamily: null,
    loading: false,
    error: null
  }),

  getters: {
    allFamilies: (state) => state.families,
    selectedFamily: (state) => state.currentFamily
  },

  actions: {
    async fetchFamilies() {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/families')
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          this.families = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch families')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
      } finally {
        this.loading = false
      }
    },

    async fetchFamily(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get(`/families/${id}`)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          this.currentFamily = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch family')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
      } finally {
        this.loading = false
      }
    },

    async createFamily(familyData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/families', familyData)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          this.families.push(response.data.data)
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to create family')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateFamily(id, familyData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put(`/families/${id}`, familyData)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          const index = this.families.findIndex(f => f.id === id)
          if (index !== -1) {
            this.families[index] = response.data.data
          }
          if (this.currentFamily && this.currentFamily.id === id) {
            this.currentFamily = response.data.data
          }
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update family')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteFamily(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.delete(`/families/${id}`)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200) {
          this.families = this.families.filter(f => f.id !== id)
          if (this.currentFamily && this.currentFamily.id === id) {
            this.currentFamily = null
          }
        } else {
          throw new Error(response.data.message || 'Failed to delete family')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async leaveFamily(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post(`/families/${id}/leave`)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200) {
          this.families = this.families.filter(f => f.id !== id)
          if (this.currentFamily && this.currentFamily.id === id) {
            this.currentFamily = null
          }
        } else {
          throw new Error(response.data.message || 'Failed to leave family')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateAdministrator(id, administratorId) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put(`/families/${id}/administrator`, administratorId)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200 && response.data.data) {
          const index = this.families.findIndex(f => f.id === id)
          if (index !== -1) {
            this.families[index] = response.data.data
          }
          if (this.currentFamily && this.currentFamily.id === id) {
            this.currentFamily = response.data.data
          }
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update administrator')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async isAdministrator(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get(`/families/${id}/administrator`)
        
        // 从新的返回格式中提取数据
        if (response.data.code === 200) {
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to check administrator status')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})
