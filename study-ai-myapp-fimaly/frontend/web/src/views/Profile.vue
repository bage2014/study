<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="个人信息" />
    <main class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div class="bg-white rounded-xl shadow-lg animate-slide-up">
        <!-- Profile Picture Section -->
        <div class="border-b border-gray-100 px-6 py-6">
          <div class="flex items-center">
            <div class="relative">
              <img 
                v-if="avatarUrl" 
                :src="avatarUrl" 
                :alt="profile.nickname || profile.username"
                class="w-24 h-24 rounded-full object-cover border-4 border-gray-200 hover:border-green-300 transition-all duration-300"
              />
              <div v-else class="w-24 h-24 rounded-full bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center border-4 border-gray-200 hover:border-green-300 transition-all duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
              <label class="absolute bottom-0 right-0 bg-green-500 text-white rounded-full p-2 hover:bg-green-600 cursor-pointer shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <input 
                  type="file" 
                  accept="image/jpeg,image/png,image/gif" 
                  @change="handleAvatarUpload"
                  class="hidden"
                  ref="avatarInput"
                />
              </label>
            </div>
            <div class="ml-6">
              <h2 class="text-xl font-bold text-gray-900">{{ profile.nickname || profile.username }}</h2>
              <p class="text-gray-600">{{ profile.email }}</p>
            </div>
          </div>
          <!-- Upload Progress -->
          <div v-if="uploading" class="mt-4">
            <div class="flex items-center space-x-2">
              <svg class="animate-spin h-4 w-4 text-green-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span class="text-sm text-gray-600">正在上传...</span>
            </div>
          </div>
        </div>

        <!-- Form Section -->
        <div class="p-6">
          <div class="flex items-center mb-6">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-100 to-blue-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4" />
              </svg>
            </div>
            <h2 class="text-lg font-semibold text-gray-900">编辑个人信息</h2>
          </div>
          <form @submit.prevent="updateProfile" class="space-y-5">
            <!-- Nickname -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">昵称</label>
              <input 
                v-model="formData.nickname"
                type="text" 
                class="input-field"
                placeholder="请输入昵称"
                maxlength="50"
              />
            </div>

            <!-- Username -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">用户名</label>
              <input 
                v-model="formData.username"
                type="text" 
                class="input-field bg-gray-50"
                disabled
                placeholder="用户名"
              />
            </div>

            <!-- Email -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">邮箱</label>
              <input 
                v-model="formData.email"
                type="email" 
                class="input-field"
                placeholder="请输入邮箱"
              />
            </div>

            <!-- Phone -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1.5">手机号</label>
              <input 
                v-model="formData.phone"
                type="tel" 
                class="input-field"
                placeholder="请输入手机号"
                maxlength="11"
              />
            </div>

            <!-- Submit Button -->
            <div class="flex justify-end space-x-3 pt-4">
              <button 
                type="button" 
                @click="resetForm"
                class="btn-secondary"
              >
                重置
              </button>
              <button 
                type="submit" 
                :disabled="loading"
                class="btn-primary"
              >
                <span v-if="loading" class="flex items-center">
                  <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  保存中...
                </span>
                <span v-else>保存</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>

  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'

export default {
  name: 'Profile',
  components: {
    Header
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const avatarInput = ref(null)
    const loading = ref(false)
    const uploading = ref(false)
    const avatarUrl = ref('')
    const profile = ref({
      id: null,
      username: '',
      email: '',
      phone: '',
      nickname: '',
      avatar: ''
    })

    const showToastMsg = (message, type = 'info') => {
      window.showToastMessage(message, type)
    }

    const formData = reactive({
      nickname: '',
      username: '',
      email: '',
      phone: ''
    })

    const fetchProfile = async () => {
      try {
        const response = await fetch('/api/profile', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        const result = await response.json()
        if (result.success) {
          profile.value = result.data
          formData.nickname = result.data.nickname || ''
          formData.username = result.data.username || ''
          formData.email = result.data.email || ''
          formData.phone = result.data.phone || ''
          // 如果有头像URL，获取头像
          if (result.data.avatar) {
            await loadAvatar(result.data.avatar)
          }
        }
      } catch (error) {
        console.error('Failed to fetch profile:', error)
      }
    }

    const loadAvatar = async (avatarPath) => {
      try {
        const response = await fetch(avatarPath, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        if (response.ok) {
          const blob = await response.blob()
          avatarUrl.value = URL.createObjectURL(blob)
        } else {
          console.warn('Failed to load avatar')
          avatarUrl.value = ''
        }
      } catch (error) {
        console.error('Failed to load avatar:', error)
        avatarUrl.value = ''
      }
    }

    const updateProfile = async () => {
      loading.value = true
      try {
        const response = await fetch('/api/profile', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({
            nickname: formData.nickname || null,
            email: formData.email || null,
            phone: formData.phone || null
          })
        })
        const result = await response.json()
        if (result.success) {
          showToastMsg('个人信息更新成功', 'success')
          profile.value = result.data
          userStore.user = result.data
        } else {
          showToastMsg('更新失败: ' + result.message, 'error')
        }
      } catch (error) {
        console.error('Failed to update profile:', error)
        showToastMsg('更新失败: ' + error.message, 'error')
      } finally {
        loading.value = false
      }
    }

    const handleAvatarUpload = async (event) => {
      const file = event.target.files[0]
      if (!file) return

      // 验证文件类型
      const validTypes = ['image/jpeg', 'image/png', 'image/gif']
      if (!validTypes.includes(file.type)) {
        showToastMsg('只支持JPG、PNG、GIF格式的图片', 'warning')
        return
      }

      // 验证文件大小（5MB）
      const maxSize = 5 * 1024 * 1024
      if (file.size > maxSize) {
        showToastMsg('图片大小不能超过5MB', 'warning')
        return
      }

      uploading.value = true
      try {
        const formData = new FormData()
        formData.append('file', file)

        const response = await fetch('/api/avatars/upload', {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: formData
        })

        const result = await response.json()
        if (result.success) {
          profile.value.avatar = result.avatarUrl
          userStore.user.avatar = result.avatarUrl
          // 重新加载头像（携带token）
          await loadAvatar(result.avatarUrl)
          showToastMsg('头像上传成功', 'success')
        } else {
          showToastMsg('头像上传失败: ' + result.message, 'error')
        }
      } catch (error) {
        console.error('Failed to upload avatar:', error)
        showToastMsg('头像上传失败: ' + error.message, 'error')
      } finally {
        uploading.value = false
        // 重置input，允许重复上传同一文件
        event.target.value = ''
      }
    }

    const resetForm = () => {
      formData.nickname = profile.value.nickname || ''
      formData.username = profile.value.username || ''
      formData.email = profile.value.email || ''
      formData.phone = profile.value.phone || ''
    }

    onMounted(() => {
      fetchProfile()
    })

    return {
      avatarInput,
      loading,
      uploading,
      avatarUrl,
      profile,
      formData,
      updateProfile,
      handleAvatarUpload,
      resetForm
    }
  }
}
</script>