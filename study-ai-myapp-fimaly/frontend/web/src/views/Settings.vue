<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="设置"></Header>

    <!-- Main Content -->
    <main class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up">
        <!-- Personal Information -->
        <div class="p-6 border-b border-gray-100 hover:bg-gray-50 transition-colors duration-200">
          <div class="flex items-center mb-4">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-100 to-blue-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
            <h2 class="text-lg font-semibold text-gray-900">个人信息</h2>
          </div>
          <form @submit.prevent="updateProfile">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1.5">邮箱</label>
                <input type="email" id="email" v-model="profileForm.email" disabled class="mt-1 block w-full px-4 py-2.5 border border-gray-200 rounded-lg bg-gray-50 text-gray-500 cursor-not-allowed">
              </div>
              <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1.5">手机号</label>
                <input type="tel" id="phone" v-model="profileForm.phone" class="mt-1 block w-full px-4 py-2.5 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-200 focus:border-green-500 transition-all duration-200">
              </div>
              <div>
                <label for="nickname" class="block text-sm font-medium text-gray-700 mb-1.5">昵称</label>
                <input type="text" id="nickname" v-model="profileForm.nickname" class="mt-1 block w-full px-4 py-2.5 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-200 focus:border-green-500 transition-all duration-200">
              </div>
            </div>
            <div class="mt-4">
              <button type="submit" :disabled="userStore.loading" class="px-5 py-2.5 bg-green-500 text-white rounded-lg hover:bg-green-600 disabled:opacity-50 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5">
                {{ userStore.loading ? '更新中...' : '更新个人信息' }}
              </button>
            </div>
          </form>
        </div>

        <!-- Change Password -->
        <div class="p-6 border-b border-gray-100 hover:bg-gray-50 transition-colors duration-200">
          <div class="flex items-center mb-4">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-orange-100 to-orange-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-orange-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <h2 class="text-lg font-semibold text-gray-900">修改密码</h2>
          </div>
          <form @submit.prevent="changePassword">
            <div class="space-y-4">
              <div>
                <label for="currentPassword" class="block text-sm font-medium text-gray-700 mb-1.5">当前密码</label>
                <input type="password" id="currentPassword" v-model="passwordForm.currentPassword" required class="mt-1 block w-full px-4 py-2.5 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-200 focus:border-green-500 transition-all duration-200">
              </div>
              <div>
                <label for="newPassword" class="block text-sm font-medium text-gray-700 mb-1.5">新密码</label>
                <input type="password" id="newPassword" v-model="passwordForm.newPassword" required class="mt-1 block w-full px-4 py-2.5 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-200 focus:border-green-500 transition-all duration-200">
              </div>
              <div>
                <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1.5">确认新密码</label>
                <input type="password" id="confirmPassword" v-model="passwordForm.confirmPassword" required class="mt-1 block w-full px-4 py-2.5 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-200 focus:border-green-500 transition-all duration-200">
              </div>
            </div>
            <div class="mt-4">
              <button type="submit" :disabled="userStore.loading" class="px-5 py-2.5 bg-green-500 text-white rounded-lg hover:bg-green-600 disabled:opacity-50 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5">
                {{ userStore.loading ? '修改中...' : '修改密码' }}
              </button>
            </div>
          </form>
        </div>

        <!-- Privacy Settings -->
        <div class="p-6 hover:bg-gray-50 transition-colors duration-200">
          <div class="flex items-center mb-4">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <h2 class="text-lg font-semibold text-gray-900">隐私设置</h2>
          </div>
          <div class="space-y-4">
            <div class="flex items-center justify-between p-3 rounded-lg hover:bg-green-50 transition-colors duration-200">
              <span class="text-sm text-gray-700">允许其他用户搜索到我的家族</span>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacyForm.allowSearch" class="sr-only peer">
                <div class="w-12 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-green-500 shadow-inner"></div>
              </label>
            </div>
            <div class="flex items-center justify-between p-3 rounded-lg hover:bg-green-50 transition-colors duration-200">
              <span class="text-sm text-gray-700">接收家族活动通知</span>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacyForm.receiveNotifications" class="sr-only peer">
                <div class="w-12 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-green-500 shadow-inner"></div>
              </label>
            </div>
          </div>
          <div class="mt-4">
            <button @click="updatePrivacy" :disabled="userStore.loading" class="px-5 py-2.5 bg-green-500 text-white rounded-lg hover:bg-green-600 disabled:opacity-50 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5">
              {{ userStore.loading ? '更新中...' : '更新隐私设置' }}
            </button>
          </div>
        </div>
      </div>
    </main>

    <!-- Message Modal -->
    <Modal 
      :visible="showMessageModal" 
      :title="messageModalTitle" 
      :icon="messageModalIcon"
      @close="showMessageModal = false"
    >
      <p class="text-gray-700">{{ messageModalContent }}</p>
      <template #footer>
        <button @click="showMessageModal = false" class="btn-primary w-full">
          确定
        </button>
      </template>
    </Modal>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import Header from '../components/Header.vue'
import Modal from '../components/Modal.vue'

export default {
  name: 'Settings',
  components: {
    Header,
    Modal
  },
  setup() {
    const userStore = useUserStore()
    const router = useRouter()
    const showMessageModal = ref(false)
    const messageModalTitle = ref('')
    const messageModalContent = ref('')
    const messageModalIcon = ref('info')

    const showMessage = (title, content, icon = 'info') => {
      messageModalTitle.value = title
      messageModalContent.value = content
      messageModalIcon.value = icon
      showMessageModal.value = true
    }

    const profileForm = ref({
      email: '',
      phone: '',
      nickname: ''
    })
    const passwordForm = ref({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    const privacyForm = ref({
      allowSearch: true,
      receiveNotifications: true
    })

    const navigateTo = (path) => {
      router.push(path)
    }

    const fetchUserProfile = async () => {
      if (!userStore.user) {
        await userStore.fetchCurrentUser()
      }
      profileForm.value = {
        email: userStore.user.email,
        phone: userStore.user.phone || '',
        nickname: userStore.user.nickname || ''
      }
      privacyForm.value = {
        allowSearch: userStore.user.allowSearch !== false,
        receiveNotifications: userStore.user.receiveNotifications !== false
      }
    }

    const updateProfile = async () => {
      try {
        await userStore.updateProfile({
          phone: profileForm.value.phone,
          nickname: profileForm.value.nickname
        })
        showMessage('操作成功', '个人信息更新成功！', 'success')
      } catch (error) {
        showMessage('操作失败', '个人信息更新失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const changePassword = async () => {
      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        showMessage('提示', '新密码和确认密码不一致！', 'warning')
        return
      }
      try {
        await userStore.changePassword({
          currentPassword: passwordForm.value.currentPassword,
          newPassword: passwordForm.value.newPassword
        })
        showMessage('操作成功', '密码修改成功！', 'success')
        passwordForm.value = {
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        }
      } catch (error) {
        showMessage('操作失败', '密码修改失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const updatePrivacy = async () => {
      try {
        await userStore.updatePrivacySettings(privacyForm.value)
        showMessage('操作成功', '隐私设置更新成功！', 'success')
      } catch (error) {
        showMessage('操作失败', '隐私设置更新失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    onMounted(() => {
      fetchUserProfile()
    })

    return {
      userStore,
      showMessageModal,
      messageModalTitle,
      messageModalContent,
      messageModalIcon,
      profileForm,
      passwordForm,
      privacyForm,
      navigateTo,
      updateProfile,
      changePassword,
      updatePrivacy
    }
  }
}
</script>