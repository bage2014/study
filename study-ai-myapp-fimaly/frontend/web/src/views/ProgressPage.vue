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
            <h1 class="text-xl font-bold text-gray-900">项目进度</h1>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div v-if="loading" class="flex justify-center py-8">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
      </div>
      <div v-else-if="!progress" class="text-center py-8">
        <p class="text-gray-600">无法加载项目进度</p>
      </div>
      <div v-else>
        <!-- Overall Progress -->
        <div class="bg-white p-6 rounded-lg shadow mb-6">
          <h3 class="text-lg font-medium text-gray-900 mb-4">整体进度</h3>
          <div class="flex justify-between mb-2">
            <span class="text-sm font-medium text-gray-700">完成度</span>
            <span class="text-sm font-medium text-green-600">{{ progress.overallProgress }}%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-4">
            <div 
              class="bg-green-500 h-4 rounded-full transition-all duration-500" 
              :style="{ width: progress.overallProgress + '%' }"
            ></div>
          </div>
          <div class="mt-4 grid grid-cols-2 gap-4 text-center">
            <div class="bg-green-50 p-4 rounded-lg">
              <p class="text-2xl font-bold text-green-600">{{ progress.completedFeatures.length }}</p>
              <p class="text-sm text-gray-600">已完成功能</p>
            </div>
            <div class="bg-yellow-50 p-4 rounded-lg">
              <p class="text-2xl font-bold text-yellow-600">{{ progress.pendingTasks.length }}</p>
              <p class="text-sm text-gray-600">待完成任务</p>
            </div>
          </div>
        </div>

        <!-- Completed Features -->
        <div class="bg-white p-6 rounded-lg shadow mb-6">
          <h3 class="text-lg font-medium text-gray-900 mb-4">已完成功能</h3>
          <div v-if="progress.completedFeatures.length === 0" class="text-center py-8">
            <p class="text-gray-600">暂无已完成功能</p>
          </div>
          <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div 
              v-for="feature in progress.completedFeatures" 
              :key="feature.id"
              class="border border-green-200 rounded-md p-4 hover:shadow-md bg-green-50"
            >
              <div class="flex items-start">
                <div class="flex-shrink-0">
                  <svg class="h-6 w-6 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <div class="ml-3 flex-1">
                  <h4 class="text-sm font-medium text-gray-900">{{ feature.name }}</h4>
                  <p class="text-sm text-gray-600 mt-1">{{ feature.description }}</p>
                  <p class="text-xs text-gray-500 mt-2">
                    完成时间: {{ formatDate(feature.completedAt) }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Pending Tasks -->
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-medium text-gray-900 mb-4">待完成任务</h3>
          <div v-if="progress.pendingTasks.length === 0" class="text-center py-8">
            <p class="text-gray-600">暂无待完成任务</p>
          </div>
          <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div 
              v-for="task in progress.pendingTasks" 
              :key="task.id"
              class="border rounded-md p-4 hover:shadow-md"
              :class="{
                'border-red-200 bg-red-50': task.priority === 'high',
                'border-yellow-200 bg-yellow-50': task.priority === 'medium',
                'border-green-200 bg-green-50': task.priority === 'low'
              }"
            >
              <div class="flex justify-between items-start mb-2">
                <h4 class="text-sm font-medium text-gray-900">{{ task.name }}</h4>
                <span 
                  class="text-xs px-2 py-1 rounded-full"
                  :class="{
                    'bg-red-100 text-red-800': task.priority === 'high',
                    'bg-yellow-100 text-yellow-800': task.priority === 'medium',
                    'bg-green-100 text-green-800': task.priority === 'low'
                  }"
                >
                  {{ task.priority === 'high' ? '高' : task.priority === 'medium' ? '中' : '低' }}优先级
                </span>
              </div>
              <p class="text-sm text-gray-600 mt-1">{{ task.description }}</p>
              <div class="mt-3 flex items-center text-xs text-gray-500">
                <svg class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                截止日期: {{ task.dueDate }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { readProgress } from '../utils/progress'

export default {
  name: 'ProgressPage',
  setup() {
    const router = useRouter()
    const progress = ref(null)
    const loading = ref(true)

    const navigateTo = (path) => {
      router.push(path)
    }

    const formatDate = (dateString) => {
      if (!dateString) return '未知'
      return new Date(dateString).toLocaleDateString('zh-CN')
    }

    onMounted(async () => {
      loading.value = true
      try {
        progress.value = await readProgress()
      } catch (error) {
        console.error('Error loading progress:', error)
      } finally {
        loading.value = false
      }
    })

    return {
      progress,
      loading,
      navigateTo,
      formatDate
    }
  }
}
</script>