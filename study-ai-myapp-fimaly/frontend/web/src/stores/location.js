import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useLocationStore = defineStore('location', {
  state: () => ({
    locations: [],
    loading: false,
    error: null
  }),

  getters: {
    getLocationsByMemberId: (state) => (memberId) => {
      return state.locations.filter(location => location.member.id === memberId)
    },
    getSharedLocationsByMemberId: (state) => (memberId) => {
      return state.locations.filter(location => location.member.id === memberId && location.isShared)
    },
    getPrimaryLocationByMemberId: (state) => (memberId) => {
      return state.locations.find(location => location.member.id === memberId && location.isPrimary)
    }
  },

  actions: {
    async fetchLocationsByMemberId(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/locations/member/${memberId}`)
        this.locations = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch locations'
      } finally {
        this.loading = false
      }
    },

    async fetchSharedLocationsByMemberId(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/locations/member/${memberId}/shared`)
        this.locations = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch shared locations'
      } finally {
        this.loading = false
      }
    },

    async fetchPrimaryLocationByMemberId(memberId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/locations/member/${memberId}/primary`)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch primary location'
        return null
      } finally {
        this.loading = false
      }
    },

    async fetchLocationById(id) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/locations/${id}`)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch location'
        return null
      } finally {
        this.loading = false
      }
    },

    async createLocation(location) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('/locations', location)
        this.locations.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to create location'
        return null
      } finally {
        this.loading = false
      }
    },

    async updateLocation(id, location) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.put(`/locations/${id}`, location)
        const index = this.locations.findIndex(l => l.id === id)
        if (index !== -1) {
          this.locations[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to update location'
        return null
      } finally {
        this.loading = false
      }
    },

    async deleteLocation(id) {
      this.loading = true
      this.error = null
      try {
        await axios.delete(`/locations/${id}`)
        this.locations = this.locations.filter(l => l.id !== id)
        return true
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to delete location'
        return false
      } finally {
        this.loading = false
      }
    }
  }
})
