<template>
  <div class="flex items-center justify-center h-full">
    <div class="text-center">
      <div class="relative w-16 h-16 mx-auto mb-4">
        <div class="absolute inset-0 border-4 border-indigo-200 rounded-full"></div>
        <div class="absolute inset-0 border-4 border-indigo-600 rounded-full border-t-transparent animate-spin"></div>
      </div>
      <p class="text-gray-500 font-medium">{{ text || '加载中...' }}</p>
      <p v-if="subtext" class="text-gray-400 text-sm mt-2">{{ subtext }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  text: {
    type: String,
    default: '加载中...'
  },
  subtext: {
    type: String,
    default: ''
  }
})
</script>
