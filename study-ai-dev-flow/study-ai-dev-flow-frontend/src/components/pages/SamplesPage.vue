<template>
  <div class="space-y-6">
    <h2 class="text-2xl font-bold text-gray-900">示例数据</h2>
    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">名称</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">描述</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="item in samples" :key="item.id">
            <td class="px-6 py-4 text-sm text-gray-900">{{ item.id }}</td>
            <td class="px-6 py-4 text-sm text-gray-900">{{ item.name }}</td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ item.description }}</td>
          </tr>
          <tr v-if="samples.length === 0">
            <td colspan="3" class="px-6 py-4 text-center text-gray-500">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { sampleApi } from '@/services/api'
import type { Sample } from '@/types'

const samples = ref<Sample[]>([])

onMounted(async () => {
  try {
    const { data } = await sampleApi.findAll()
    samples.value = data
  } catch {
    console.log('后端未启动或无数据')
  }
})
</script>
