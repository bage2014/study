<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h2 class="text-xl font-bold text-gray-800">流水线列表</h2>
        <p class="text-sm text-gray-500 mt-1">管理和查看所有AI流水线执行记录</p>
      </div>
      <router-link 
        to="/" 
        class="bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-all font-medium shadow-md hover:shadow-lg"
      >
        + 录入新需求
      </router-link>
    </div>

    <Loading v-if="loading" text="加载流水线列表..." subtext="请稍候" />
    
    <template v-else>
      <div v-if="pipelines.length === 0" class="bg-white rounded-xl shadow-md p-12 text-center">
        <div class="text-gray-300 text-6xl mb-4">📋</div>
        <p class="text-gray-500">暂无流水线记录</p>
        <router-link 
          to="/" 
          class="inline-block mt-4 bg-indigo-600 text-white px-5 py-2.5 rounded-lg hover:bg-indigo-700 font-medium"
        >
          录入需求
        </router-link>
      </div>

      <div v-else class="grid grid-cols-1 lg:grid-cols-2 gap-4">
        <div
          v-for="pipeline in pipelines"
          :key="pipeline.pipelineId"
          @click="goToDetail(pipeline.pipelineId)"
          class="bg-white rounded-xl shadow-md p-5 hover:shadow-xl transition-all cursor-pointer border-l-4"
          :class="statusBorderClass(pipeline.status)"
        >
          <div class="flex justify-between items-start">
            <div class="flex-1">
              <div class="flex items-center mb-2">
                <span class="font-mono text-sm text-gray-500">{{ pipeline.pipelineId.substring(0, 12) }}...</span>
                <span
                  :class="[
                    'ml-2 px-2.5 py-1 rounded-full text-xs font-medium',
                    statusClass(pipeline.status)
                  ]"
                >
                  {{ statusText(pipeline.status) }}
                </span>
              </div>
              <p class="text-gray-700 font-medium mb-2">{{ pipeline.requirementTitle || '需求处理中' }}</p>
              <p class="text-sm text-gray-500 mb-1">当前阶段: {{ pipeline.currentStage }}</p>
              <p class="text-xs text-gray-400">{{ formatTime(pipeline.createdAt) }}</p>
            </div>
            <div class="text-indigo-500">
              <span class="text-xl">→</span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { pipelineApi } from '../api'
import Loading from '../components/Loading.vue'

const router = useRouter()
const showToast = inject('showToast')

const pipelines = ref([])
const loading = ref(true)

const statusClass = (status) => {
  switch (status) {
    case 'RUNNING':
      return 'bg-yellow-100 text-yellow-700'
    case 'COMPLETED':
      return 'bg-green-100 text-green-700'
    case 'FAILED':
      return 'bg-red-100 text-red-700'
    case 'CANCELLED':
      return 'bg-gray-100 text-gray-700'
    case 'WAITING_APPROVAL':
      return 'bg-orange-100 text-orange-700'
    default:
      return 'bg-blue-100 text-blue-700'
  }
}

const statusBorderClass = (status) => {
  switch (status) {
    case 'RUNNING':
      return 'border-yellow-500'
    case 'COMPLETED':
      return 'border-green-500'
    case 'FAILED':
      return 'border-red-500'
    case 'CANCELLED':
      return 'border-gray-400'
    case 'WAITING_APPROVAL':
      return 'border-orange-500'
    default:
      return 'border-gray-300'
  }
}

const statusText = (status) => {
  switch (status) {
    case 'RUNNING':
      return '执行中'
    case 'COMPLETED':
      return '已完成'
    case 'FAILED':
      return '失败'
    case 'CANCELLED':
      return '已取消'
    case 'WAITING_APPROVAL':
      return '等待审批'
    default:
      return status
  }
}

const formatTime = (timestamp) => {
  if (!timestamp) return '未知'
  const diff = Date.now() - (typeof timestamp === 'number' ? timestamp : new Date(timestamp).getTime())
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const minutes = Math.floor(diff / (1000 * 60))
  
  if (days > 0) return `${days}天前`
  if (hours > 0) return `${hours}小时前`
  if (minutes > 0) return `${minutes}分钟前`
  return '刚刚'
}

const goToDetail = (pipelineId) => {
  router.push(`/pipelines/${pipelineId}`)
}

const loadPipelines = async () => {
  loading.value = true
  try {
    const response = await pipelineApi.getAllPipelines()
    pipelines.value = response.data
  } catch (e) {
    console.error('加载流水线失败:', e)
    showToast?.error('加载流水线列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadPipelines()
})
</script>
