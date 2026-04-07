<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="操作日志" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <!-- 搜索和筛选 -->
    <div class="mb-4 grid grid-cols-1 md:grid-cols-3 gap-4">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">操作类型</label>
        <select v-model="filter.operationType" @change="fetchLogs" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
          <option value="">全部</option>
          <option value="CREATE">创建</option>
          <option value="UPDATE">更新</option>
          <option value="DELETE">删除</option>
          <option value="READ">读取</option>
          <option value="OTHER">其他</option>
        </select>
      </div>
      
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">开始日期</label>
        <input v-model="filter.startDate" type="datetime-local" @change="fetchLogs" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
      </div>
      
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">结束日期</label>
        <input v-model="filter.endDate" type="datetime-local" @change="fetchLogs" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
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

const operationLogStore = useOperationLogStore()
const userStore = useUserStore()

const filter = ref({
  operationType: '',
  startDate: '',
  endDate: ''
})

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
