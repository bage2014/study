<template>
  <header class="bg-white shadow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex items-center">
          <h1 class="text-xl font-bold text-gray-900">家庭族谱</h1>
        </div>
        <div class="flex items-center space-x-4">
          <span class="text-sm text-gray-700">{{ user?.nickname || user?.email }}</span>
          <button @click="handleLogout" class="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100">
            退出登录
          </button>
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
  setup() {
    const userStore = useUserStore()
    const router = useRouter()

    const handleLogout = () => {
      userStore.logout()
      router.push('/login')
    }

    onMounted(async () => {
      if (!userStore.user) {
        await userStore.fetchCurrentUser()
      }
    })

    return {
      user: userStore.user,
      handleLogout
    }
  }
}
</script>