import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useMilestoneStore = defineStore('milestone', {
  state: () => ({
    milestones: [],
    loading: false,
    error: null
  }),

  getters: {
    getMilestonesByMemberId: (state) => (memberId) => {
      return state.milestones.filter(milestone => milestone.member.id === memberId)
    }
  },

  actions: {
    async fetchMilestonesByMemberId(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/milestones/member/${memberId}`)
        this.milestones = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch milestones'
      } finally {
        this.loading = false
      }
    },

    async fetchPublicMilestonesByMemberId(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/milestones/member/${memberId}/public`)
        this.milestones = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch public milestones'
      } finally {
        this.loading = false
      }
    },

    async fetchMilestoneById(id) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/milestones/${id}`)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch milestone'
        return null
      } finally {
        this.loading = false
      }
    },

    async createMilestone(milestone) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/milestones', milestone)
        this.milestones.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to create milestone'
        return null
      } finally {
        this.loading = false
      }
    },

    async updateMilestone(id, milestone) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/milestones/${id}`, milestone)
        const index = this.milestones.findIndex(m => m.id === id)
        if (index !== -1) {
          this.milestones[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to update milestone'
        return null
      } finally {
        this.loading = false
      }
    },

    async deleteMilestone(id) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/milestones/${id}`)
        this.milestones = this.milestones.filter(m => m.id !== id)
        return true
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to delete milestone'
        return false
      } finally {
        this.loading = false
      }
    }
  }
})
