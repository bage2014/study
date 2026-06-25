import { defineStore } from 'pinia'
import { ref } from 'vue'
import { addressService } from '../services/api'

export const useAddressStore = defineStore('address', () => {
  const addresses = ref([])
  const loading = ref(false)
  const error = ref(null)

  const loadAddresses = async () => {
    loading.value = true
    error.value = null
    try {
      const response = await addressService.list()
      addresses.value = response.data
    } catch (e) {
      error.value = e.message
      console.error('加载地址失败:', e)
    } finally {
      loading.value = false
    }
  }

  const getAddressById = (id) => {
    return addresses.value.find(addr => addr.id === id)
  }

  const getDefaultAddress = () => {
    return addresses.value.find(addr => addr.isDefault)
  }

  const createAddress = async (data) => {
    try {
      const response = await addressService.create(data)
      addresses.value.push(response.data)
      return response.data
    } catch (e) {
      error.value = e.message
      throw e
    }
  }

  const updateAddress = async (id, data) => {
    try {
      const response = await addressService.update(id, data)
      const index = addresses.value.findIndex(addr => addr.id === id)
      if (index !== -1) {
        addresses.value[index] = response.data
      }
      return response.data
    } catch (e) {
      error.value = e.message
      throw e
    }
  }

  const deleteAddress = async (id) => {
    try {
      await addressService.delete(id)
      addresses.value = addresses.value.filter(addr => addr.id !== id)
    } catch (e) {
      error.value = e.message
      throw e
    }
  }

  return {
    addresses,
    loading,
    error,
    loadAddresses,
    getAddressById,
    getDefaultAddress,
    createAddress,
    updateAddress,
    deleteAddress
  }
})