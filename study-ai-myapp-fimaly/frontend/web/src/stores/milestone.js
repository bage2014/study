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
        if (response.data.code === 200 && response.data.data) {
          this.milestones = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch milestones')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.milestones = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch public milestones')
        }
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
        if (response.data.code === 200 && response.data.data) {
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch milestone')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.milestones.push(response.data.data)
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to create milestone')
        }
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
        if (response.data.code === 200 && response.data.data) {
          const index = this.milestones.findIndex(m => m.id === id)
          if (index !== -1) {
            this.milestones[index] = response.data.data
          }
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update milestone')
        }
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
        const response = await axios.delete(`/milestones/${id}`)
        if (response.data.code === 200) {
          this.milestones = this.milestones.filter(m => m.id !== id)
          return true
        } else {
          throw new Error(response.data.message || 'Failed to delete milestone')
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to delete milestone'
        return false
      } finally {
        this.loading = false
      }
    }
  }
})
