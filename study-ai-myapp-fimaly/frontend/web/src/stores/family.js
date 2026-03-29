import { defineStore } from 'pinia'
import axios from 'axios'

export const useFamilyStore = defineStore('family', {
  state: () => ({
    families: [],
    currentFamily: null,
    members: [],
    loading: false,
    error: null
  }),
  
  getters: {
    familyList: (state) => state.families,
    currentFamilyDetails: (state) => state.currentFamily,
    familyMembers: (state) => state.members
  },
  
  actions: {
    async fetchFamilies() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('http://localhost:8080/api/families')
        this.families = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch families'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async createFamily(name, description, avatar) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post('http://localhost:8080/api/families', {
          name,
          description,
          avatar
        })
        this.families.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to create family'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async fetchFamilyDetails(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`http://localhost:8080/api/families/${familyId}`)
        this.currentFamily = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch family details'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async fetchFamilyMembers(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`http://localhost:8080/api/families/${familyId}/members`)
        this.members = response.data
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch family members'
        throw error
      } finally {
        this.loading = false
      }
    },
    
    async addFamilyMember(familyId, member) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.post(`http://localhost:8080/api/families/${familyId}/members`, member)
        this.members.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to add family member'
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})