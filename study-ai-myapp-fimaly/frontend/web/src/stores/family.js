import { defineStore } from 'pinia'
import axios from 'axios'

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
        const response = await axios.get('/api/families')
        
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
        const response = await axios.get(`/api/families/${id}`)
        
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
        const response = await axios.post('/api/families', familyData)
        
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
        const response = await axios.put(`/api/families/${id}`, familyData)
        
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
        const response = await axios.delete(`/api/families/${id}`)
        
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
        const response = await axios.post(`/api/families/${id}/leave`)
        
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
    }
  }
})
