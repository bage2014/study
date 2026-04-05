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
            <button @click="openCreateModal" class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
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
        <div v-if="familyStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="familyStore.families.length === 0" class="text-center py-16">
          <p class="text-gray-600">您还没有创建家族</p>
          <button @click="openCreateModal" class="mt-4 px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600">
            创建第一个家族
          </button>
        </div>
        <div v-else class="space-y-4">
          <div v-for="family in familyStore.families" :key="family.id" class="border rounded-md p-4 hover:shadow-md">
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
                <button @click="leaveFamily(family.id)" class="text-orange-600 hover:text-orange-800 mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
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
              <button @click="navigateTo(`/family-tree?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-green-500 text-white rounded-md hover:bg-green-600">
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
              <input type="text" id="name" v-model="form.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700">描述</label>
              <textarea id="description" v-model="form.description" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"></textarea>
            </div>
            <div>
              <label for="avatar" class="block text-sm font-medium text-gray-700">家族头像</label>
              <input type="file" id="avatar" @change="handleFileChange" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-md text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 hover:shadow-lg focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50 transition-all duration-200">
              {{ familyStore.loading ? '保存中...' : '保存' }}
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
import { useRouter } from 'vue-router'

export default {
  name: 'FamilyManagement',
  setup() {
    const familyStore = useFamilyStore()
    const router = useRouter()
    const showModal = ref(false)
    const editingFamily = ref(null)
    const form = ref({
      name: '',
      description: '',
      avatar: ''
    })
    const selectedFile = ref(null)

    const navigateTo = (path) => {
      router.push(path)
    }

    const fetchFamilies = async () => {
      await familyStore.fetchFamilies()
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

    const deleteFamily = async (familyId) => {
      if (confirm('确定要删除这个家族吗？')) {
        try {
          await familyStore.deleteFamily(familyId)
          alert('家族删除成功')
        } catch (error) {
          alert('家族删除失败: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    const leaveFamily = async (familyId) => {
      if (confirm('确定要退出这个家族吗？')) {
        try {
          await familyStore.leaveFamily(familyId)
          alert('家族退出成功')
        } catch (error) {
          alert('家族退出失败: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    const handleFileChange = (event) => {
      selectedFile.value = event.target.files[0]
    }

    const handleSubmit = async () => {
      try {
        if (editingFamily.value) {
          // 编辑家族
          await familyStore.updateFamily(editingFamily.value.id, form.value)
          alert('家族更新成功')
        } else {
          // 创建家族
          await familyStore.createFamily(form.value)
          alert('家族创建成功')
        }
        showModal.value = false
      } catch (error) {
        alert('操作失败: ' + (error.response?.data?.message || error.message))
      }
    }

    onMounted(() => {
      fetchFamilies()
    })

    return {
      familyStore,
      showModal,
      editingFamily,
      form,
      navigateTo,
      formatDate,
      openCreateModal,
      editFamily,
      deleteFamily,
      leaveFamily,
      handleFileChange,
      handleSubmit
    }
  }
}
</script>