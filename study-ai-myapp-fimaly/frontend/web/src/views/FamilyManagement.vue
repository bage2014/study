<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <button @click="navigateTo('/home')" class="mr-4 text-gray-600 hover:text-gray-900">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h1 class="text-xl font-bold text-gray-900">家族管理</h1>
          </div>
          <div class="flex items-center">
            <button @click="openCreateModal" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
              创建家族
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white p-6 rounded-lg shadow">
        <!-- Families List -->
        <div v-if="loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
        <div v-else-if="families.length === 0" class="text-center py-16">
          <p class="text-gray-600">您还没有创建家族</p>
          <button @click="openCreateModal" class="mt-4 px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
            创建第一个家族
          </button>
        </div>
        <div v-else class="space-y-4">
          <div v-for="family in families" :key="family.id" class="border rounded-md p-4 hover:shadow-md">
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-medium text-gray-900">{{ family.name }}</h3>
                <p class="text-sm text-gray-600 mt-1">{{ family.description || '无描述' }}</p>
                <p class="text-xs text-gray-500 mt-1">创建于: {{ formatDate(family.createdAt) }}</p>
              </div>
              <div>
                <button @click="editFamily(family)" class="text-green-600 hover:text-green-800 mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
                <button @click="deleteFamily(family.id)" class="text-red-600 hover:text-red-800">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
            <div class="mt-4 flex space-x-2">
              <button @click="navigateTo(`/family-tree?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-primary text-white rounded-md hover:bg-blue-700">
                查看家族树
              </button>
              <button @click="navigateTo(`/members?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300">
                管理成员
              </button>
              <button @click="navigateTo(`/events?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300">
                管理事件
              </button>
              <button @click="navigateTo(`/media?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300">
                管理媒体
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Create/Edit Family Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">{{ editingFamily ? '编辑家族' : '创建家族' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label for="name" class="block text-sm font-medium text-gray-700">家族名称</label>
              <input type="text" id="name" v-model="form.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700">描述</label>
              <textarea id="description" v-model="form.description" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"></textarea>
            </div>
            <div>
              <label for="avatar" class="block text-sm font-medium text-gray-700">家族头像</label>
              <input type="file" id="avatar" @change="handleFileChange" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              保存
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'

export default {
  name: 'FamilyManagement',
  setup() {
    const familyStore = useFamilyStore()
    const loading = ref(false)
    const showModal = ref(false)
    const editingFamily = ref(null)
    const form = ref({
      name: '',
      description: '',
      avatar: ''
    })
    const selectedFile = ref(null)

    const navigateTo = (path) => {
      window.location.href = path
    }

    const fetchFamilies = async () => {
      loading.value = true
      await familyStore.fetchFamilies()
      loading.value = false
    }

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString()
    }

    const openCreateModal = () => {
      editingFamily.value = null
      form.value = {
        name: '',
        description: '',
        avatar: ''
      }
      selectedFile.value = null
      showModal.value = true
    }

    const editFamily = (family) => {
      editingFamily.value = family
      form.value = {
        name: family.name,
        description: family.description,
        avatar: family.avatar
      }
      selectedFile.value = null
      showModal.value = true
    }

    const deleteFamily = (familyId) => {
      if (confirm('确定要删除这个家族吗？')) {
        // 这里应该调用删除家族的API
        console.log('删除家族:', familyId)
        // 模拟删除成功
        familyStore.families = familyStore.families.filter(family => family.id !== familyId)
      }
    }

    const handleFileChange = (event) => {
      selectedFile.value = event.target.files[0]
    }

    const handleSubmit = async () => {
      if (editingFamily.value) {
        // 编辑家族
        console.log('编辑家族:', form.value)
        const index = familyStore.families.findIndex(family => family.id === editingFamily.value.id)
        if (index !== -1) {
          familyStore.families[index] = { ...editingFamily.value, ...form.value }
        }
      } else {
        // 创建家族
        await familyStore.createFamily(
          form.value.name,
          form.value.description,
          form.value.avatar
        )
      }
      showModal.value = false
    }

    onMounted(() => {
      fetchFamilies()
    })

    return {
      families: familyStore.families,
      loading,
      showModal,
      editingFamily,
      form,
      navigateTo,
      formatDate,
      openCreateModal,
      editFamily,
      deleteFamily,
      handleFileChange,
      handleSubmit
    }
  }
}
</script>