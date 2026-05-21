import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { familyAPI, type FamilyDTO, type FamilyRequest } from '@/api'

export const useFamilyStore = defineStore('family', () => {
  const families = ref<FamilyDTO[]>([])
  const currentFamily = ref<FamilyDTO | null>(null)
  const loading = ref(false)

  const familyOptions = computed(() => 
    families.value.map(f => ({ value: f.id, label: f.name }))
  )

  async function loadFamilies() {
    loading.value = true
    try {
      const data = await familyAPI.getByUser() as unknown as FamilyDTO[]
      families.value = data
      if (families.value.length > 0 && !currentFamily.value) {
        currentFamily.value = families.value[0]
      }
    } catch (error) {
      console.error('加载家族列表失败', error)
    } finally {
      loading.value = false
    }
  }

  async function createFamily(data: FamilyRequest) {
    loading.value = true
    try {
      const family = await familyAPI.create(data) as unknown as FamilyDTO
      families.value.push(family)
      currentFamily.value = family
      return family
    } catch (error) {
      console.error('创建家族失败', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  async function updateFamily(id: number, data: FamilyRequest) {
    loading.value = true
    try {
      const family = await familyAPI.update(id, data) as unknown as FamilyDTO
      const index = families.value.findIndex(f => f.id === id)
      if (index !== -1) {
        families.value[index] = family
        if (currentFamily.value?.id === id) {
          currentFamily.value = family
        }
      }
      return family
    } catch (error) {
      console.error('更新家族失败', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  async function deleteFamily(id: number) {
    loading.value = true
    try {
      await familyAPI.delete(id)
      families.value = families.value.filter(f => f.id !== id)
      if (currentFamily.value?.id === id) {
        currentFamily.value = families.value.length > 0 ? families.value[0] : null
      }
    } catch (error) {
      console.error('删除家族失败', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  function setCurrentFamily(family: FamilyDTO) {
    currentFamily.value = family
  }

  function clearCurrentFamily() {
    currentFamily.value = null
  }

  return {
    families,
    currentFamily,
    loading,
    familyOptions,
    loadFamilies,
    createFamily,
    updateFamily,
    deleteFamily,
    setCurrentFamily,
    clearCurrentFamily
  }
})