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
            <h1 class="text-xl font-bold text-gray-900">设置</h1>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white p-6 rounded-lg shadow">
        <!-- Personal Information -->
        <div class="mb-8">
          <h2 class="text-lg font-medium text-gray-900 mb-4">个人信息</h2>
          <form @submit.prevent="updateProfile">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label for="email" class="block text-sm font-medium text-gray-700">邮箱</label>
                <input type="email" id="email" v-model="profileForm.email" disabled class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm bg-gray-50">
              </div>
              <div>
                <label for="phone" class="block text-sm font-medium text-gray-700">手机号</label>
                <input type="tel" id="phone" v-model="profileForm.phone" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
              </div>
              <div>
                <label for="nickname" class="block text-sm font-medium text-gray-700">昵称</label>
                <input type="text" id="nickname" v-model="profileForm.nickname" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
              </div>
            </div>
            <div class="mt-4">
              <button type="submit" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
                更新个人信息
              </button>
            </div>
          </form>
        </div>

        <!-- Change Password -->
        <div class="mb-8">
          <h2 class="text-lg font-medium text-gray-900 mb-4">修改密码</h2>
          <form @submit.prevent="changePassword">
            <div class="space-y-4">
              <div>
                <label for="currentPassword" class="block text-sm font-medium text-gray-700">当前密码</label>
                <input type="password" id="currentPassword" v-model="passwordForm.currentPassword" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
              </div>
              <div>
                <label for="newPassword" class="block text-sm font-medium text-gray-700">新密码</label>
                <input type="password" id="newPassword" v-model="passwordForm.newPassword" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
              </div>
              <div>
                <label for="confirmPassword" class="block text-sm font-medium text-gray-700">确认新密码</label>
                <input type="password" id="confirmPassword" v-model="passwordForm.confirmPassword" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
              </div>
            </div>
            <div class="mt-4">
              <button type="submit" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
                修改密码
              </button>
            </div>
          </form>
        </div>

        <!-- Privacy Settings -->
        <div>
          <h2 class="text-lg font-medium text-gray-900 mb-4">隐私设置</h2>
          <div class="space-y-4">
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-700">允许其他用户搜索到我的家族</span>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacyForm.allowSearch" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
              </label>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-700">接收家族活动通知</span>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="privacyForm.receiveNotifications" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
              </label>
            </div>
          </div>
          <div class="mt-4">
            <button @click="updatePrivacy" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
              更新隐私设置
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'

export default {
  name: 'Settings',
  setup() {
    const userStore = useUserStore()
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
      window.location.href = path
    }

    const fetchUserProfile = async () => {
      if (!userStore.user) {
        await userStore.fetchUser()
      }
      profileForm.value = {
        email: userStore.user.email,
        phone: userStore.user.phone || '',
        nickname: userStore.user.nickname || ''
      }
    }

    const updateProfile = async () => {
      // 这里应该调用更新个人信息的API
      console.log('更新个人信息:', profileForm.value)
      // 模拟更新成功
      alert('个人信息更新成功！')
    }

    const changePassword = async () => {
      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        alert('新密码和确认密码不一致！')
        return
      }
      // 这里应该调用修改密码的API
      console.log('修改密码:', passwordForm.value)
      // 模拟更新成功
      alert('密码修改成功！')
      passwordForm.value = {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }

    const updatePrivacy = async () => {
      // 这里应该调用更新隐私设置的API
      console.log('更新隐私设置:', privacyForm.value)
      // 模拟更新成功
      alert('隐私设置更新成功！')
    }

    onMounted(() => {
      fetchUserProfile()
    })

    return {
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