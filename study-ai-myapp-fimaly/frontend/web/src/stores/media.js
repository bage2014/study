import { defineStore } from 'pinia'
import api from '../utils/axios'

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
        const response = await api.get('/media')
        if (response.data.code === 200 && response.data.data) {
          this.media = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch media')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error('Failed to fetch media:', error)
      } finally {
        this.loading = false
      }
    },
    async fetchMediaByFamilyId(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/media/family', {
          params: { familyId }
        })
        if (response.data.code === 200 && response.data.data) {
          this.media = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch media')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
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

        const response = await api.post('/media', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        if (response.data.code === 200 && response.data.data) {
          this.media.push(response.data.data)
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to upload media')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
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
        const response = await api.delete(`/media/${id}`)
        if (response.data.code === 200) {
          this.media = this.media.filter(item => item.id !== id)
        } else {
          throw new Error(response.data.message || 'Failed to delete media')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to delete media ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})