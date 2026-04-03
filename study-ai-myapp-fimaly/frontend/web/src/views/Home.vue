<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <h1 class="text-xl font-bold text-gray-900">家庭族谱</h1>
          </div>
          <div class="flex items-center">
            <button @click="handleLogout" class="ml-4 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100">
              退出登录
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="text-center mb-8">
        <h2 class="text-2xl font-bold text-gray-900">欢迎回来，{{ user?.nickname || user?.email }}</h2>
        <p class="mt-2 text-gray-600">管理您的家族信息</p>
      </div>

      <!-- Quick Actions -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-6 mb-8">
        <div @click="navigateTo('/family-management')" class="bg-white p-6 rounded-lg shadow hover:shadow-md cursor-pointer">
          <div class="flex items-center justify-center h-12 w-12 rounded-md bg-red-100 text-red-600 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900">家族管理</h3>
          <p class="mt-2 text-sm text-gray-600">新建、编辑、退出家族</p>
        </div>

        <div @click="navigateTo('/family-tree')" class="bg-white p-6 rounded-lg shadow hover:shadow-md cursor-pointer">
          <div class="flex items-center justify-center h-12 w-12 rounded-md bg-blue-100 text-primary mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900">家族树</h3>
          <p class="mt-2 text-sm text-gray-600">查看家族关系</p>
        </div>

        <div @click="navigateTo('/members')" class="bg-white p-6 rounded-lg shadow hover:shadow-md cursor-pointer">
          <div class="flex items-center justify-center h-12 w-12 rounded-md bg-green-100 text-green-600 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900">成员管理</h3>
          <p class="mt-2 text-sm text-gray-600">查看、新建、编辑成员</p>
        </div>

        <div @click="navigateTo('/relationships')" class="bg-white p-6 rounded-lg shadow hover:shadow-md cursor-pointer">
          <div class="flex items-center justify-center h-12 w-12 rounded-md bg-purple-100 text-purple-600 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900">关系管理</h3>
          <p class="mt-2 text-sm text-gray-600">绑定成员关系</p>
        </div>

        <div @click="navigateTo('/events')" class="bg-white p-6 rounded-lg shadow hover:shadow-md cursor-pointer">
          <div class="flex items-center justify-center h-12 w-12 rounded-md bg-yellow-100 text-yellow-600 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900">历史记录</h3>
          <p class="mt-2 text-sm text-gray-600">查看家族历史</p>
        </div>

        <div @click="navigateTo('/media')" class="bg-white p-6 rounded-lg shadow hover:shadow-md cursor-pointer">
          <div class="flex items-center justify-center h-12 w-12 rounded-md bg-indigo-100 text-indigo-600 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900">多媒体库</h3>
          <p class="mt-2 text-sm text-gray-600">管理家族照片</p>
        </div>
      </div>

      <!-- Family Overview -->
      <div class="bg-white p-6 rounded-lg shadow mb-8">
        <h3 class="text-lg font-medium text-gray-900 mb-4">家族概览</h3>
        <div v-if="loading" class="flex justify-center py-8">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
        <div v-else-if="families.length === 0" class="text-center py-8">
          <p class="text-gray-600">您还没有创建家族</p>
          <button @click="navigateTo('/family-management')" class="mt-4 px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700 shadow-md hover:shadow-lg transition-all duration-200">
            创建家族
          </button>
        </div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div v-for="family in families" :key="family.id" class="border rounded-md p-4 hover:shadow-md">
            <h4 class="font-medium text-gray-900">{{ family.name }}</h4>
            <p class="text-sm text-gray-600 mt-1">{{ family.description || '无描述' }}</p>
            <div class="mt-4 flex space-x-2">
              <button @click="navigateTo(`/family-tree?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-primary text-white rounded-md hover:bg-blue-700">
                查看家族树
              </button>
              <button @click="navigateTo(`/family-management?familyId=${family.id}`)" class="px-3 py-1 text-sm bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300">
                管理
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { useUserStore } from '../stores/user'
import { useFamilyStore } from '../stores/family'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'

export default {
  name: 'Home',
  setup() {
    const userStore = useUserStore()
    const familyStore = useFamilyStore()
    const router = useRouter()

    const navigateTo = (path) => {
      router.push(path)
    }

    const handleLogout = () => {
      userStore.logout()
      router.push('/login')
    }

    onMounted(async () => {
      if (!userStore.user) {
        await userStore.fetchCurrentUser()
      }
      await familyStore.fetchFamilies()
    })

    return {
      user: userStore.user,
      families: familyStore.families,
      loading: familyStore.loading,
      navigateTo,
      handleLogout
    }
  }
}
</script>