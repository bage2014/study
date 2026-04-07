import { defineStore } from 'pinia'
import axios from '../utils/axios'

export const useOperationLogStore = defineStore('operationLog', {
  state: () => ({
    logs: [],
    loading: false,
    error: null
  }),

  actions: {
    async fetchAllLogs() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('/logs')
        this.logs = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch logs'
      } finally {
        this.loading = false
      }
    },

    async fetchLogsByOperatorId(operatorId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/logs/operator/${operatorId}`)
        this.logs = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch logs by operator'
      } finally {
        this.loading = false
      }
    },

    async fetchLogsByOperationType(operationType) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/logs/type/${operationType}`)
        this.logs = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch logs by operation type'
      } finally {
        this.loading = false
      }
    },

    async fetchLogsByTimeRange(startDate, endDate) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/logs/time-range?startDate=${startDate}&endDate=${endDate}`)
        this.logs = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch logs by time range'
      } finally {
        this.loading = false
      }
    },

    async fetchLogsByRelatedDataId(relatedDataId) {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get(`/logs/related/${relatedDataId}`)
        this.logs = response.data
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch logs by related data'
      } finally {
        this.loading = false
      }
    }
  }
})
