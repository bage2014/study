import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { familyAPI, type FamilyDTO, type FamilyRequest } from '@/api'

export const useFamilyStore = defineStore('family', () => {
  const families = ref<FamilyDTO[]>([])
  const currentFamily = ref<FamilyDTO | null>(null)

  const familyOptions = computed(() => 
    families.value.map(f => ({ value: f.id, label: f.name }))
  )

  async function loadFamilies() {
    families.value = await familyAPI.getByUser()
    if (families.value.length > 0 && !currentFamily.value) {
      currentFamily.value = families.value[0]
    }
  }

  async function createFamily(data: FamilyRequest) {
    const family = await familyAPI.create(data)
    families.value.push(family)
    currentFamily.value = family
    return family
  }

  async function updateFamily(id: number, data: FamilyRequest) {
    const family = await familyAPI.update(id, data)
    const index = families.value.findIndex(f => f.id === id)
    if (index !== -1) {
      families.value[index] = family
      if (currentFamily.value?.id === id) {
        currentFamily.value = family
      }
    }
    return family
  }

  async function deleteFamily(id: number) {
    await familyAPI.delete(id)
    families.value = families.value.filter(f => f.id !== id)
    if (currentFamily.value?.id === id) {
      currentFamily.value = families.value.length > 0 ? families.value[0] : null
    }
  }

  function setCurrentFamily(family: FamilyDTO) {
    currentFamily.value = family
  }

  return {
    families,
    currentFamily,
    familyOptions,
    loadFamilies,
    createFamily,
    updateFamily,
    deleteFamily,
    setCurrentFamily
  }
})