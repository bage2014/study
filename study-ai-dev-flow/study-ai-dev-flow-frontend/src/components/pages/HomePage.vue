<template>
  <div class="space-y-6">
    <div class="text-center">
      <h1 class="text-3xl font-bold text-gray-900">研发流程标准前端项目</h1>
      <p class="mt-2 text-gray-600">Vue 3 + TypeScript + Vite + TailwindCSS</p>
    </div>
    <div class="flex justify-center">
      <div class="bg-white rounded-lg shadow p-6 space-y-4">
        <p class="text-gray-700">计数器: {{ count }}</p>
        <p class="text-gray-500 text-sm">双倍: {{ doubleCount }}</p>
        <div class="flex space-x-2">
          <button
            @click="increment"
            class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
          >
            +1
          </button>
          <button
            @click="decrement"
            class="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 transition"
          >
            -1
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useCounterStore } from '@/stores/counter'
import { storeToRefs } from 'pinia'

const counterStore = useCounterStore()
const { count, doubleCount } = storeToRefs(counterStore)
const { increment, decrement } = counterStore
</script>
