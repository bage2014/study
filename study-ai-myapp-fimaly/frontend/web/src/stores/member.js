import { defineStore } from 'pinia'
import api from '../utils/axios'

export const useMemberStore = defineStore('member', {
  state: () => ({
    members: [],
    currentMember: null,
    loading: false,
    error: null
  }),
  getters: {
    allMembers: (state) => state.members,
    selectedMember: (state) => state.currentMember,
    getMembersByFamilyId: (state) => (familyId) => {
      return state.members.filter(member => member.familyId === familyId)
    }
  },
  actions: {
    async fetchMembers() {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/members')
        if (response.data.code === 200 && response.data.data) {
          this.members = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch members')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error('Failed to fetch members:', error)
      } finally {
        this.loading = false
      }
    },
    async fetchMembersByFamilyId(familyId) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get(`/families/${familyId}/members`)
        if (response.data.code === 200 && response.data.data) {
          this.members = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch members')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to fetch members for family ${familyId}:`, error)
      } finally {
        this.loading = false
      }
    },
    async searchMembers(searchData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/members/search', {
          params: searchData
        })
        if (response.data.code === 200 && response.data.data) {
          this.members = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to search members')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error('Failed to search members:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async fetchMember(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.get(`/members/${id}`)
        if (response.data.code === 200 && response.data.data) {
          this.currentMember = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch member')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to fetch member ${id}:`, error)
      } finally {
        this.loading = false
      }
    },
    async createMember(memberData) {
      this.loading = true
      this.error = null
      try {
        const { familyId, ...memberDataWithoutFamilyId } = memberData
        const response = await api.post(`/families/${familyId}/members`, memberDataWithoutFamilyId)
        if (response.data.code === 200 && response.data.data) {
          this.members.push(response.data.data)
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to create member')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error('Failed to create member:', error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateMember(id, memberData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put(`/members/${id}`, memberData)
        if (response.data.code === 200 && response.data.data) {
          const index = this.members.findIndex(m => m.id === id)
          if (index !== -1) {
            this.members[index] = response.data.data
          }
          if (this.currentMember && this.currentMember.id === id) {
            this.currentMember = response.data.data
          }
          return response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to update member')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to update member ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteMember(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.delete(`/members/${id}`)
        if (response.data.code === 200) {
          this.members = this.members.filter(m => m.id !== id)
          if (this.currentMember && this.currentMember.id === id) {
            this.currentMember = null
          }
        } else {
          throw new Error(response.data.message || 'Failed to delete member')
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message
        console.error(`Failed to delete member ${id}:`, error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})