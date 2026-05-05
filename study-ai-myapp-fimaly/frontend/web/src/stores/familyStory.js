import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useFamilyStoryStore = defineStore('familyStory', {
  state: () => ({
    currentStory: null,
    loading: false,
    error: null,
    storyTypes: ['migration', 'biography', 'legend', 'default']
  }),

  actions: {
    async generateFamilyStory(familyId, storyType = 'migration', keywords = []) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`/ai/stories/generate/${familyId}`, {
          storyType,
          keywords
        })
        if (response.data.code === 200 && response.data.data) {
          this.currentStory = response.data.data
          return response.data.data
        } else {
          throw new Error(response.data.message || '故事生成失败')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message || '故事生成失败'
        return null
      } finally {
        this.loading = false
      }
    },

    async getStoryTypes() {
      try {
        const response = await axios.get('/ai/stories/types')
        if (response.data.code === 200 && response.data.data) {
          this.storyTypes = response.data.data
        }
      } catch (error) {
        console.error('获取故事类型失败', error)
      }
    },

    clearStory() {
      this.currentStory = null
      this.error = null
    }
  }
})
