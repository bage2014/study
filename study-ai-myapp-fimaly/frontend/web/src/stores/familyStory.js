import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useFamilyStoryStore = defineStore('familyStory', {
  state: () => ({
    familyStory: '',
    memberStory: '',
    loading: false,
    error: null
  }),

  actions: {
    async generateFamilyStory(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/stories/family/${familyId}`)
        this.familyStory = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to generate family story'
        return ''
      } finally {
        this.loading = false
      }
    },

    async generateMemberStory(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/stories/member/${memberId}`)
        this.memberStory = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to generate member story'
        return ''
      } finally {
        this.loading = false
      }
    }
  }
})
