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
        if (response.data.code === 200 && response.data.data) {
          this.familyStory = response.data.data
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to generate family story')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.memberStory = response.data.data
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to generate member story')
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to generate member story'
        return ''
      } finally {
        this.loading = false
      }
    }
  }
})
