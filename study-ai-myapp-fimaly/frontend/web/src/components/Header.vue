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
            <span class="text-sm text-gray-700">{{ user?.nickname || user?.email }}</span>
            <button @click="handleLogout" class="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100">
              退出登录
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'

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

    const handleLogout = () => {
      userStore.logout()
      router.push('/login')
    }

    const navigateTo = (path) => {
      router.push(path)
    }

    onMounted(async () => {
      if (!userStore.user) {
        await userStore.fetchCurrentUser()
      }
    })

    return {
      user: userStore.user,
      handleLogout,
      navigateTo
    }
  }
}
</script>