<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="多媒体库">
      <template #actions>
        <button @click="openUploadModal" class="btn-primary">
          上传媒体
        </button>
      </template>
    </Header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div class="bg-white p-6 rounded-lg shadow animate-slide-up">
        <!-- Family Selector -->
        <div class="mb-6">
          <label for="family" class="block text-sm font-medium text-gray-700 mb-2">选择家族</label>
          <Select
            id="family"
            v-model="selectedFamilyId"
            :options="familyOptions"
            placeholder="请选择家族"
            @change="fetchMedia"
          />
        </div>

        <!-- Media Type Filter -->
        <div class="mb-6">
          <div class="flex space-x-2">
            <button @click="selectedType = ''" :class="['px-4 py-2 rounded-xl text-sm font-medium cursor-pointer transition-all duration-200', selectedType === '' ? 'bg-green-500 text-white shadow-md' : 'bg-gray-100 text-gray-700 hover:bg-gray-200']">
              全部
            </button>
            <button @click="selectedType = 'photo'" :class="['px-4 py-2 rounded-xl text-sm font-medium cursor-pointer transition-all duration-200', selectedType === 'photo' ? 'bg-green-500 text-white shadow-md' : 'bg-gray-100 text-gray-700 hover:bg-gray-200']">
              照片
            </button>
            <button @click="selectedType = 'video'" :class="['px-4 py-2 rounded-xl text-sm font-medium cursor-pointer transition-all duration-200', selectedType === 'video' ? 'bg-green-500 text-white shadow-md' : 'bg-gray-100 text-gray-700 hover:bg-gray-200']">
              视频
            </button>
            <button @click="selectedType = 'document'" :class="['px-4 py-2 rounded-xl text-sm font-medium cursor-pointer transition-all duration-200', selectedType === 'document' ? 'bg-green-500 text-white shadow-md' : 'bg-gray-100 text-gray-700 hover:bg-gray-200']">
              文档
            </button>
          </div>
        </div>

        <!-- Media Grid -->
        <div v-if="mediaStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="filteredMedia.length === 0" class="text-center py-16">
          <p class="text-gray-600">暂无媒体文件</p>
        </div>
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          <div v-for="(media, index) in filteredMedia" :key="media.id" class="border border-gray-200 rounded-lg overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2 cursor-pointer group animate-fade-in" :style="{ animationDelay: `${index * 30}ms` }">
            <div class="relative overflow-hidden">
              <img v-if="media.type === 'photo'" :src="media.url" :alt="media.description" class="w-full h-48 object-cover transition-transform duration-500 group-hover:scale-110">
              <div v-else-if="media.type === 'video'" class="w-full h-48 bg-gradient-to-br from-indigo-100 to-indigo-200 flex items-center justify-center">
                <div class="w-16 h-16 rounded-full bg-white/80 flex items-center justify-center shadow-lg transition-transform duration-300 group-hover:scale-110">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-600 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
              </div>
              <div v-else class="w-full h-48 bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center">
                <div class="w-16 h-16 rounded-lg bg-white/80 flex items-center justify-center shadow-lg transition-transform duration-300 group-hover:scale-110">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                </div>
              </div>
              <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center">
                <button @click.stop="previewMedia(media)" class="w-12 h-12 rounded-full bg-white/90 flex items-center justify-center shadow-lg transform scale-0 group-hover:scale-100 transition-transform duration-300">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-green-600 ml-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </button>
              </div>
              <button @click.stop="deleteMedia(media.id)" class="absolute top-2 right-2 bg-white/90 rounded-full p-1.5 text-red-500 hover:text-red-700 hover:bg-red-50 shadow-md transform translate-y-2 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all duration-200">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
            <div class="p-4 bg-gray-50">
              <p class="text-sm font-medium text-gray-900 truncate">{{ media.description || '无描述' }}</p>
              <div class="flex items-center justify-between mt-2">
                <span class="text-xs text-gray-500">{{ formatDate(media.uploadedAt) }}</span>
                <span :class="['px-2 py-0.5 rounded-full text-xs font-medium', media.type === 'photo' ? 'bg-green-100 text-green-700' : media.type === 'video' ? 'bg-indigo-100 text-indigo-700' : 'bg-purple-100 text-purple-700']">
                  {{ media.type === 'photo' ? '照片' : media.type === 'video' ? '视频' : '文档' }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Upload Media Modal -->
    <div v-if="showUploadModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showUploadModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-pink-100 to-pink-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-pink-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">上传媒体</h3>
        </div>
        <form @submit.prevent="handleUpload">
          <div class="space-y-4">
            <div>
              <label for="mediaType" class="block text-sm font-medium text-gray-700 mb-1.5">媒体类型</label>
              <Select
                id="mediaType"
                v-model="uploadForm.type"
                :options="mediaTypeOptions"
                placeholder="请选择"
                required
              />
            </div>
            <div>
              <label for="mediaFile" class="block text-sm font-medium text-gray-700 mb-1.5">文件</label>
              <input type="file" id="mediaFile" @change="handleFileChange" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700 mb-1.5">描述</label>
              <input type="text" id="description" v-model="uploadForm.description" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showUploadModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="mediaStore.loading" class="btn-primary">
              {{ mediaStore.loading ? '上传中...' : '上传' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Delete Confirm Modal -->
    <ConfirmModal 
      :visible="showDeleteConfirm"
      title="确认删除"
      message="确定要删除该媒体文件吗？此操作不可撤销。"
      type="delete"
      @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false"
    />

  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useMediaStore } from '../stores/media'
import { useRouter } from 'vue-router'
import Header from '../components/Header.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import Select from '../components/Select.vue'

export default {
  name: 'Media',
  components: {
    Header,
    Select,
    ConfirmModal
  },
  setup() {
    const familyStore = useFamilyStore()
    const mediaStore = useMediaStore()
    const router = useRouter()
    const selectedFamilyId = ref('')
    const selectedType = ref('')
    const showUploadModal = ref(false)
    const showDeleteConfirm = ref(false)
    const deletingMediaId = ref(null)
    const selectedFile = ref(null)

    const uploadForm = ref({
      type: '',
      description: ''
    })

    const showToastMsg = (message, type = 'info') => {
      window.showToastMessage(message, type)
    }

    const familyOptions = computed(() => {
      return familyStore.families.map(family => ({
        value: family.id,
        label: family.name
      }))
    })

    const mediaTypeOptions = [
      { value: 'photo', label: '照片' },
      { value: 'video', label: '视频' },
      { value: 'document', label: '文档' }
    ]

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
        showToastMsg('请选择文件', 'warning')
        return
      }
      if (!selectedFamilyId.value) {
        showToastMsg('请选择家族', 'warning')
        return
      }

      try {
        await mediaStore.uploadMedia(
          selectedFamilyId.value,
          selectedFile.value,
          uploadForm.value.type,
          uploadForm.value.description
        )
        showToastMsg('文件上传成功', 'success')
        showUploadModal.value = false
        uploadForm.value = {
          type: '',
          description: ''
        }
        selectedFile.value = null
      } catch (error) {
        showToastMsg('文件上传失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const deleteMedia = (mediaId) => {
      deletingMediaId.value = mediaId
      showDeleteConfirm.value = true
    }

    const confirmDelete = async () => {
      try {
        await mediaStore.deleteMedia(deletingMediaId.value)
        showToastMsg('文件删除成功', 'success')
      } catch (error) {
        showToastMsg('文件删除失败: ' + (error.response?.data?.message || error.message), 'error')
      } finally {
        showDeleteConfirm.value = false
        deletingMediaId.value = null
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
      showDeleteConfirm,
      uploadForm,
      navigateTo,
      fetchMedia,
      formatDate,
      handleFileChange,
      handleUpload,
      deleteMedia,
      confirmDelete,
      showToastMsg
    }
  }
}
</script>