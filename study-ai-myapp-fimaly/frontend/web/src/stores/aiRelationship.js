import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useAiRelationshipStore = defineStore('aiRelationship', {
  state: () => ({
    predictedRelationships: [],
    loading: false,
    error: null
  }),

  actions: {
    async analyzeRelationships(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`/ai/relationships/analyze/${familyId}`)
        this.predictedRelationships = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to analyze relationships'
        return []
      } finally {
        this.loading = false
      }
    },

    async getPredictedRelationships(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/ai/relationships/predict/${familyId}`)
        this.predictedRelationships = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to get predicted relationships'
        return []
      } finally {
        this.loading = false
      }
    }
  }
})
