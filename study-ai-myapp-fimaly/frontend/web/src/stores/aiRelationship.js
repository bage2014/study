import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useAiRelationshipStore = defineStore('aiRelationship', {
  state: () => ({
    predictedRelationships: [],
    loading: false,
    error: null,
    completionSuggestions: []
  }),

  actions: {
    async predictRelationships(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`/ai/relationships/predict/${familyId}`)
        if (response.data.code === 200 && response.data.data) {
          this.predictedRelationships = response.data.data.predictedRelationships || []
          return response.data.data
        } else {
          throw new Error(response.data.message || '关系预测失败')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message || '关系预测失败'
        return null
      } finally {
        this.loading = false
      }
    },

    async getCompletionSuggestions(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/ai/relationships/suggestions/${familyId}`)
        if (response.data.code === 200 && response.data.data) {
          this.completionSuggestions = response.data.data
          return response.data.data
        } else {
          throw new Error(response.data.message || '获取建议失败')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message || '获取建议失败'
        return []
      } finally {
        this.loading = false
      }
    },

    clearResults() {
      this.predictedRelationships = []
      this.completionSuggestions = []
      this.error = null
    }
  }
})
