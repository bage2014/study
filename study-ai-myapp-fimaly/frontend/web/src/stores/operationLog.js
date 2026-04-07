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
        if (response.data.code === 200 && response.data.data) {
          this.logs = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch logs')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.logs = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch logs by operator')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.logs = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch logs by operation type')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.logs = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch logs by time range')
        }
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
        if (response.data.code === 200 && response.data.data) {
          this.logs = response.data.data
        } else {
          throw new Error(response.data.message || 'Failed to fetch logs by related data')
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Failed to fetch logs by related data'
      } finally {
        this.loading = false
      }
    }
  }
})
