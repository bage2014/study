import { defineStore } from 'pinia'
import { ref } from 'vue'
import { compareService } from '../services/api'

export const useSearchStore = defineStore('search', () => {
  const result = ref(null)
  const loading = ref(false)
  const error = ref(null)
  const searchKeyword = ref('')
  const selectedAddressId = ref('')
  const history = ref([])

  const search = async (keyword, addressId) => {
    if (!keyword?.trim()) {
      throw new Error('请输入商品名称')
    }

    loading.value = true
    error.value = null
    searchKeyword.value = keyword.trim()
    selectedAddressId.value = addressId || ''

    try {
      const response = await compareService.search(searchKeyword.value, selectedAddressId.value)
      result.value = response.data
      
      if (!history.value.find(h => h.keyword === searchKeyword.value)) {
        history.value.unshift({
          keyword: searchKeyword.value,
          timestamp: new Date().toLocaleString()
        })
        if (history.value.length > 10) {
          history.value = history.value.slice(0, 10)
        }
      }

      return response.data
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  const clearResult = () => {
    result.value = null
  }

  const clearHistory = () => {
    history.value = []
  }

  return {
    result,
    loading,
    error,
    searchKeyword,
    selectedAddressId,
    history,
    search,
    clearResult,
    clearHistory
  }
})