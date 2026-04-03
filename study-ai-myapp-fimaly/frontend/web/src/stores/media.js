import { defineStore } from 'pinia'
import axios from 'axios'

export const useMediaStore = defineStore('media', {
  state: () => ({
    media: [],
    loading: false,
    error: null
  }),
  getters: {
    getMediaByFamilyId: (state) => (familyId) => {
      return state.media.filter(item => item.familyId === familyId)
    },
    getMediaByType: (state) => (type) => {
      return state.media.filter(item => item.type === type)
    },
    getMediaByFamilyAndType: (state) => (familyId, type) => {
      return state.media.filter(item => item.familyId === familyId && item.type === type)
    }
  },
  actions: {
    async fetchMedia() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/api/media')
        this.media = response.data
      } catch (error) {
        this.error = error.message
        console.error('Failed to fetch media:', error)
      } finally {
        this.loading = false
      }
    },
    async fetchMediaByFamilyId(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/api/media/family/${familyId}`)
        this.media = response.data
      } catch (error) {
        this.error = error.message
        console.error(`Failed to fetch media for family ${familyId}:`, error)
      } finally {
        this.loading = false
      }
    },
    async uploadMedia(familyId, file, type, description) {
      this.loading = true
      this.error = null
      try {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('familyId', familyId)
        formData.append('type', type)
        formData.append('description', description)

        const response = await axios.post('/api/media/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        this.media.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Failed to upload media:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteMedia(id) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/api/media/${id}`)
        this.media = this.media.filter(item => item.id !== id)
      } catch (error) {
        this.error = error.message
        console.error(`Failed to delete media ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})