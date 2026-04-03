import { defineStore } from 'pinia'
import axios from 'axios'

export const useRelationshipStore = defineStore('relationship', {
  state: () => ({
    relationships: [],
    loading: false,
    error: null
  }),

  getters: {
    getRelationshipsByMemberId: (state) => (memberId) => {
      return state.relationships.filter(r => 
        r.member1Id === memberId || r.member2Id === memberId
      )
    }
  },

  actions: {
    async fetchRelationships() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/api/relationships')
        if (response.data.code === 200 && response.data.data) {
          this.relationships = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch relationships')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchRelationshipsByMemberId(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/api/relationships/member/${memberId}`)
        if (response.data.code === 200 && response.data.data) {
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch relationships')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async createRelationship(relationshipData) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/api/relationships', relationshipData)
        if (response.data.code === 200 && response.data.data) {
          this.relationships.push(response.data.data)
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to create relationship')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteRelationship(id) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.delete(`/api/relationships/${id}`)
        if (response.data.code === 200) {
          this.relationships = this.relationships.filter(r => r.id !== id)
        } else {
          throw new Error(response.data.message || 'Failed to delete relationship')
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
