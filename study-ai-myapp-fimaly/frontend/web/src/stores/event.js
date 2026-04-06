import { defineStore } from 'pinia'
import api from '../utils/axios'

export const useEventStore = defineStore('event', {
  state: () => ({
    events: [],
    loading: false,
    error: null
  }),
  getters: {
    getEventsByFamilyId: (state) => (familyId) => {
      return state.events.filter(event => event.familyId === familyId)
    },
    getEventsByMemberId: (state) => (memberId) => {
      return state.events.filter(event => 
        event.relatedMembers && event.relatedMembers.includes(memberId.toString())
      )
    }
  },
  actions: {
    async fetchEvents() {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/events')
        if (response.data.code === 200 && response.data.data) {
          this.events = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch events')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error('Failed to fetch events:', error)
      } finally {
        this.loading = false
      }
    },
    async fetchEventsByFamilyId(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get(`/events/family/${familyId}`)
        if (response.data.code === 200 && response.data.data) {
          this.events = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch events')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to fetch events for family ${familyId}:`, error)
      } finally {
        this.loading = false
      }
    },
    async createEvent(eventData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/events', eventData)
        if (response.data.code === 200 && response.data.data) {
          this.events.push(response.data.data)
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to create event')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error('Failed to create event:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateEvent(id, eventData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put(`/events/${id}`, eventData)
        if (response.data.code === 200 && response.data.data) {
          const index = this.events.findIndex(event => event.id === id)
          if (index !== -1) {
            this.events[index] = response.data.data
          }
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update event')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to update event ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteEvent(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.delete(`/events/${id}`)
        if (response.data.code === 200) {
          this.events = this.events.filter(event => event.id !== id)
        } else {
          throw new Error(response.data.message || 'Failed to delete event')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to delete event ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})