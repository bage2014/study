<template>
  <header class="bg-white shadow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex items-center">
          <button @click="navigateTo('/home')" class="mr-4 text-gray-600 hover:text-gray-900">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
            </svg>
          </button>
          <h1 class="text-xl font-bold text-gray-900">{{ title }}</h1>
        </div>
        <div class="flex items-center space-x-3">
          <slot name="actions"></slot>
          <div class="flex items-center space-x-4">
            <!-- 用户头像（点击跳转个人信息） -->
            <div class="relative">
              <img 
                :src="userAvatarUrl || defaultAvatar" 
                :alt="displayName"
                class="w-8 h-8 rounded-full object-cover border-2 border-gray-200 cursor-pointer hover:border-gray-400"
                @click="navigateTo('/profile')"
              />
            </div>
            <!-- 用户名（优先昵称，为空显示邮箱） -->
            <span class="text-sm text-gray-700">{{ displayName }}</span>
            <button @click="handleLogout" class="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100">
              退出
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { computed, ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'

export default {
  name: 'Header',
  props: {
    title: {
      type: String,
      default: '家庭族谱'
    }
  },
  setup() {
    const userStore = useUserStore()
    const router = useRouter()
    const userAvatarUrl = ref('')
    const defaultAvatar = 'https://via.placeholder.com/32'

    // 优先显示昵称，为空时显示邮箱
    const displayName = computed(() => {
      const user = userStore.user
      if (!user) return '未登录'
      return user.nickname || user.email || '未设置'
    })

    const handleLogout = () => {
      userStore.logout()
      router.push('/login')
    }

    const navigateTo = (path) => {
      router.push(path)
    }

    const loadUserAvatar = async () => {
      const user = userStore.user
      if (user && user.avatar) {
        try {
          const response = await fetch(user.avatar, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
          })
          if (response.ok) {
            const blob = await response.blob()
            userAvatarUrl.value = URL.createObjectURL(blob)
          }
        } catch (error) {
          console.error('Failed to load user avatar:', error)
        }
      }
    }

    onMounted(async () => {
      if (!userStore.user) {
        await userStore.fetchCurrentUser()
      }
      await loadUserAvatar()
    })

    return {
      user: userStore.user,
      displayName,
      userAvatarUrl,
      defaultAvatar,
      handleLogout,
      navigateTo
    }
  }
}
</script>