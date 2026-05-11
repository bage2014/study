<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="操作日志" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">

    <!-- 搜索和筛选 -->
    <div class="bg-white p-6 rounded-xl shadow-lg mb-6 animate-slide-up">
      <div class="flex items-center mb-4">
        <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-gray-100 to-gray-200 flex items-center justify-center mr-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
        </div>
        <h2 class="text-lg font-semibold text-gray-900">筛选条件</h2>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1.5">操作类型</label>
          <Select
            v-model="filter.operationType"
            :options="operationTypeOptions"
            placeholder="全部"
            @change="fetchLogs"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1.5">开始日期</label>
          <input v-model="filter.startDate" type="datetime-local" @change="fetchLogs" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1.5">结束日期</label>
          <input v-model="filter.endDate" type="datetime-local" @change="fetchLogs" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="operationLogStore.loading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-green-500"></div>
    </div>

    <!-- 错误信息 -->
    <div v-else-if="operationLogStore.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ operationLogStore.error }}
    </div>

    <!-- 日志列表 -->
    <div v-else class="mt-4">
      <div class="overflow-x-auto">
        <table class="min-w-full bg-white border border-gray-200">
          <thead>
            <tr>
              <th class="py-2 px-4 border-b border-gray-200 bg-gray-50">操作人</th>
              <th class="py-2 px-4 border-b border-gray-200 bg-gray-50">操作类型</th>
              <th class="py-2 px-4 border-b border-gray-200 bg-gray-50">操作描述</th>
              <th class="py-2 px-4 border-b border-gray-200 bg-gray-50">操作时间</th>
              <th class="py-2 px-4 border-b border-gray-200 bg-gray-50">备注</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="log in operationLogStore.logs" :key="log.id" class="hover:bg-gray-50">
              <td class="py-2 px-4 border-b border-gray-200">{{ log.operatorName }}</td>
              <td class="py-2 px-4 border-b border-gray-200">
                <span :class="getOperationTypeClass(log.operationType)">
                  {{ log.operationType }}
                </span>
              </td>
              <td class="py-2 px-4 border-b border-gray-200">{{ log.operationDescription }}</td>
              <td class="py-2 px-4 border-b border-gray-200">{{ formatDate(log.operationTime) }}</td>
              <td class="py-2 px-4 border-b border-gray-200">{{ log.remarks || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 无日志提示 -->
      <div v-if="operationLogStore.logs.length === 0" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center mt-4">
        <p class="text-gray-500">暂无操作日志记录</p>
      </div>
    </div>
  </main>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useOperationLogStore } from '../stores/operationLog'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'
import Select from '../components/Select.vue'

const operationLogStore = useOperationLogStore()
const userStore = useUserStore()

const filter = ref({
  operationType: '',
  startDate: '',
  endDate: ''
})

const operationTypeOptions = [
  { value: 'CREATE', label: '创建' },
  { value: 'UPDATE', label: '更新' },
  { value: 'DELETE', label: '删除' },
  { value: 'READ', label: '读取' },
  { value: 'OTHER', label: '其他' }
]

onMounted(async () => {
  if (!userStore.user) {
    await userStore.fetchCurrentUser()
  }
  await fetchLogs()
})

const fetchLogs = async () => {
  if (filter.value.operationType) {
    await operationLogStore.fetchLogsByOperationType(filter.value.operationType)
  } else if (filter.value.startDate && filter.value.endDate) {
    await operationLogStore.fetchLogsByTimeRange(filter.value.startDate, filter.value.endDate)
  } else {
    await operationLogStore.fetchAllLogs()
  }
}

const getOperationTypeClass = (type) => {
  const classMap = {
    'CREATE': 'px-2 py-1 bg-green-100 text-green-800 rounded-md text-sm',
    'UPDATE': 'px-2 py-1 bg-blue-100 text-blue-800 rounded-md text-sm',
    'DELETE': 'px-2 py-1 bg-red-100 text-red-800 rounded-md text-sm',
    'READ': 'px-2 py-1 bg-gray-100 text-gray-800 rounded-md text-sm',
    'OTHER': 'px-2 py-1 bg-yellow-100 text-yellow-800 rounded-md text-sm'
  }
  return classMap[type] || 'px-2 py-1 bg-gray-100 text-gray-800 rounded-md text-sm'
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>
