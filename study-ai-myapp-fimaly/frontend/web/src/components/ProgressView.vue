<template>
  <div class="bg-white p-6 rounded-lg shadow mb-8">
    <h3 class="text-lg font-medium text-gray-900 mb-4">项目进度</h3>
    <div v-if="loading" class="flex justify-center py-8">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
    </div>
    <div v-else-if="!progress" class="text-center py-8">
      <p class="text-gray-600">无法加载项目进度</p>
    </div>
    <div v-else>
      <!-- 整体进度 -->
      <div class="mb-6">
        <div class="flex justify-between mb-2">
          <span class="text-sm font-medium text-gray-700">整体进度</span>
          <span class="text-sm font-medium text-green-600">{{ progress.overallProgress }}%</span>
        </div>
        <div class="w-full bg-gray-200 rounded-full h-2.5">
          <div 
            class="bg-green-500 h-2.5 rounded-full" 
            :style="{ width: progress.overallProgress + '%' }"
          ></div>
        </div>
      </div>

      <!-- 已完成功能 -->
      <div class="mb-6">
        <h4 class="text-md font-medium text-gray-900 mb-3">已完成功能</h4>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div 
            v-for="feature in progress.completedFeatures" 
            :key="feature.id"
            class="border rounded-md p-3 hover:shadow-sm"
          >
            <h5 class="font-medium text-gray-900">{{ feature.name }}</h5>
            <p class="text-sm text-gray-600 mt-1">{{ feature.description }}</p>
            <p class="text-xs text-gray-500 mt-2">
              完成时间: {{ formatDate(feature.completedAt) }}
            </p>
          </div>
        </div>
      </div>

      <!-- 待完成任务 -->
      <div>
        <h4 class="text-md font-medium text-gray-900 mb-3">待完成任务</h4>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div 
            v-for="task in progress.pendingTasks" 
            :key="task.id"
            class="border rounded-md p-3 hover:shadow-sm"
          >
            <div class="flex justify-between items-start">
              <h5 class="font-medium text-gray-900">{{ task.name }}</h5>
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
            <p class="text-xs text-gray-500 mt-2">
              截止日期: {{ task.dueDate }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { readProgress } from '../utils/progress'

export default {
  name: 'ProgressView',
  setup() {
    const progress = ref(null)
    const loading = ref(true)

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
      formatDate
    }
  }
}
</script>