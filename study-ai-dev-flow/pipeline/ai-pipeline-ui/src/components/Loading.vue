<template>
  <div class="flex items-center justify-center py-16">
    <div class="text-center">
      <div class="w-6 h-6 mx-auto mb-3 border-2 border-line border-t-ink rounded-full animate-spin"></div>
      <p class="text-ink-secondary text-sm">{{ text || '加载中' }}</p>
      <p v-if="subtext" class="text-ink-muted text-xs mt-1">{{ subtext }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  text: { type: String, default: '加载中' },
  subtext: { type: String, default: '' }
})
</script>
