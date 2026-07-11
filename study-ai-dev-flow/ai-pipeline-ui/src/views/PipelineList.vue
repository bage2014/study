<template>
  <div>
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold text-gray-800">流水线列表</h2>
      <router-link to="/" class="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors">
        录入新需求
      </router-link>
    </div>
    <div v-if="loading" class="text-center py-10">
      <div class="spinner-border" role="status"></div>
      <p class="mt-2 text-gray-500">加载中...</p>
    </div>
    <div v-else-if="pipelines.length === 0" class="text-center py-10">
      <div class="text-gray-400 text-6xl mb-4">📋</div>
      <p class="text-gray-500">暂无流水线记录</p>
      <router-link to="/" class="btn btn-primary mt-4">录入需求</router-link>
    </div>
    <div v-else class="grid gap-4">
      <div
        v-for="pipeline in pipelines"
        :key="pipeline.pipelineId"
        class="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow"
      >
        <div class="flex justify-between items-start">
          <div class="flex-1">
            <div class="flex items-center mb-2">
              <h3 class="font-semibold text-gray-800">{{ pipeline.pipelineId }}</h3>
              <span
                :class="[
                  'ml-2 px-2 py-1 rounded-full text-xs font-medium',
                  statusClass(pipeline.status)
                ]"
              >
                {{ pipeline.status }}
              </span>
            </div>
            <p class="text-gray-600 text-sm mb-2">当前阶段: {{ pipeline.currentStage }}</p>
            <p class="text-gray-500 text-sm">创建时间: {{ pipeline.createdAt }}</p>
          </div>
          <div>
            <router-link
              :to="`/pipelines/${pipeline.pipelineId}`"
              class="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors text-sm"
            >
              查看详情
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { pipelineApi } from '../api'

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
    default:
      return 'bg-blue-100 text-blue-700'
  }
}

const loadPipelines = async () => {
  loading.value = true
  try {
    const response = await pipelineApi.getAllPipelines()
    pipelines.value = response.data
  } catch (e) {
    console.error('加载流水线失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadPipelines()
})
</script>
