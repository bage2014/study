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
        this.events = response.data
      } catch (error) {
        this.error = error.message
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
        this.events = response.data
      } catch (error) {
        this.error = error.message
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
        this.events.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
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
        const index = this.events.findIndex(event => event.id === id)
        if (index !== -1) {
          this.events[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
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
        await api.delete(`/events/${id}`)
        this.events = this.events.filter(event => event.id !== id)
      } catch (error) {
        this.error = error.message
        console.error(`Failed to delete event ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})