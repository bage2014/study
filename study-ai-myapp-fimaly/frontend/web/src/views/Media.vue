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
            <h1 class="text-xl font-bold text-gray-900">多媒体库</h1>
          </div>
          <div class="flex items-center">
            <button @click="openUploadModal" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
              上传媒体
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white p-6 rounded-lg shadow">
        <!-- Family Selector -->
        <div class="mb-6">
          <label for="family" class="block text-sm font-medium text-gray-700 mb-2">选择家族</label>
          <select id="family" v-model="selectedFamilyId" @change="fetchMedia" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary">
            <option value="">请选择家族</option>
            <option v-for="family in families" :key="family.id" :value="family.id">{{ family.name }}</option>
          </select>
        </div>

        <!-- Media Type Filter -->
        <div class="mb-6">
          <div class="flex space-x-2">
            <button @click="selectedType = ''" :class="['px-4 py-2 rounded-md text-sm font-medium', selectedType === '' ? 'bg-primary text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-300']">
              全部
            </button>
            <button @click="selectedType = 'photo'" :class="['px-4 py-2 rounded-md text-sm font-medium', selectedType === 'photo' ? 'bg-primary text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-300']">
              照片
            </button>
            <button @click="selectedType = 'video'" :class="['px-4 py-2 rounded-md text-sm font-medium', selectedType === 'video' ? 'bg-primary text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-300']">
              视频
            </button>
            <button @click="selectedType = 'document'" :class="['px-4 py-2 rounded-md text-sm font-medium', selectedType === 'document' ? 'bg-primary text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-300']">
              文档
            </button>
          </div>
        </div>

        <!-- Media Grid -->
        <div v-if="mediaStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
        <div v-else-if="filteredMedia.length === 0" class="text-center py-16">
          <p class="text-gray-600">暂无媒体文件</p>
        </div>
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          <div v-for="media in filteredMedia" :key="media.id" class="border rounded-md overflow-hidden hover:shadow-md">
            <div class="relative">
              <img v-if="media.type === 'photo'" :src="media.url" :alt="media.description" class="w-full h-48 object-cover">
              <div v-else-if="media.type === 'video'" class="w-full h-48 bg-gray-200 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div v-else class="w-full h-48 bg-gray-200 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <button @click="deleteMedia(media.id)" class="absolute top-2 right-2 bg-white rounded-full p-1 text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
            <div class="p-4">
              <p class="text-sm font-medium text-gray-900">{{ media.description || '无描述' }}</p>
              <p class="text-xs text-gray-500 mt-1">{{ formatDate(media.uploadedAt) }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Upload Media Modal -->
    <div v-if="showUploadModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">上传媒体</h3>
        <form @submit.prevent="handleUpload">
          <div class="space-y-4">
            <div>
              <label for="mediaType" class="block text-sm font-medium text-gray-700">媒体类型</label>
              <select id="mediaType" v-model="uploadForm.type" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
                <option value="">请选择</option>
                <option value="photo">照片</option>
                <option value="video">视频</option>
                <option value="document">文档</option>
              </select>
            </div>
            <div>
              <label for="mediaFile" class="block text-sm font-medium text-gray-700">文件</label>
              <input type="file" id="mediaFile" @change="handleFileChange" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700">描述</label>
              <input type="text" id="description" v-model="uploadForm.description" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showUploadModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" :disabled="mediaStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-50">
              {{ mediaStore.loading ? '上传中...' : '上传' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useMediaStore } from '../stores/media'
import { useRouter } from 'vue-router'

export default {
  name: 'Media',
  setup() {
    const familyStore = useFamilyStore()
    const mediaStore = useMediaStore()
    const router = useRouter()
    const selectedFamilyId = ref('')
    const selectedType = ref('')
    const showUploadModal = ref(false)
    const uploadForm = ref({
      type: '',
      description: ''
    })
    const selectedFile = ref(null)

    const navigateTo = (path) => {
      router.push(path)
    }

    const fetchMedia = async () => {
      if (selectedFamilyId.value) {
        await mediaStore.fetchMediaByFamilyId(selectedFamilyId.value)
      }
    }

    const filteredMedia = computed(() => {
      if (!selectedFamilyId.value) {
        return []
      }
      if (!selectedType.value) {
        return mediaStore.getMediaByFamilyId(selectedFamilyId.value)
      }
      return mediaStore.getMediaByFamilyAndType(selectedFamilyId.value, selectedType.value)
    })

    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleDateString()
    }

    const handleFileChange = (event) => {
      selectedFile.value = event.target.files[0]
    }

    const handleUpload = async () => {
      if (!selectedFile.value) {
        alert('请选择文件')
        return
      }
      if (!selectedFamilyId.value) {
        alert('请选择家族')
        return
      }

      try {
        await mediaStore.uploadMedia(
          selectedFamilyId.value,
          selectedFile.value,
          uploadForm.value.type,
          uploadForm.value.description
        )
        alert('文件上传成功')
        showUploadModal.value = false
        uploadForm.value = {
          type: '',
          description: ''
        }
        selectedFile.value = null
      } catch (error) {
        alert('文件上传失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const deleteMedia = async (mediaId) => {
      if (confirm('确定要删除这个媒体文件吗？')) {
        try {
          await mediaStore.deleteMedia(mediaId)
          alert('文件删除成功')
        } catch (error) {
          alert('文件删除失败: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    onMounted(async () => {
      await familyStore.fetchFamilies()
    })

    return {
      familyStore,
      mediaStore,
      families: familyStore.families,
      filteredMedia,
      selectedFamilyId,
      selectedType,
      showUploadModal,
      uploadForm,
      navigateTo,
      fetchMedia,
      formatDate,
      handleFileChange,
      handleUpload,
      deleteMedia
    }
  }
}
</script>