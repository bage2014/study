<template>
  <div class="fixed top-4 right-4 z-50 space-y-2">
    <TransitionGroup name="toast">
      <div
        v-for="toast in toasts"
        :key="toast.id"
        :class="[
          'flex items-center px-4 py-3 rounded-lg shadow-lg min-w-80 max-w-sm',
          getToastClass(toast.type)
        ]"
      >
        <span class="text-xl mr-3">{{ getToastIcon(toast.type) }}</span>
        <div class="flex-1">
          <p class="font-medium">{{ toast.message }}</p>
          <p v-if="toast.detail" class="text-sm opacity-80">{{ toast.detail }}</p>
        </div>
        <button
          @click="removeToast(toast.id)"
          class="ml-2 text-gray-400 hover:text-gray-600"
        >
          <span class="text-lg">×</span>
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const toasts = ref([])

const getToastClass = (type) => {
  switch (type) {
    case 'success': return 'bg-green-50 border border-green-200 text-green-700'
    case 'error': return 'bg-red-50 border border-red-200 text-red-700'
    case 'warning': return 'bg-yellow-50 border border-yellow-200 text-yellow-700'
    default: return 'bg-blue-50 border border-blue-200 text-blue-700'
  }
}

const getToastIcon = (type) => {
  switch (type) {
    case 'success': return '✓'
    case 'error': return '✗'
    case 'warning': return '⚠️'
    default: return 'ℹ️'
  }
}

const addToast = (message, type = 'info', detail = '') => {
  const id = Date.now()
  toasts.value.push({ id, message, type, detail })
  setTimeout(() => removeToast(id), 4000)
}

const removeToast = (id) => {
  const index = toasts.value.findIndex(t => t.id === id)
  if (index > -1) {
    toasts.value.splice(index, 1)
  }
}

defineExpose({
  success: (msg, detail) => addToast(msg, 'success', detail),
  error: (msg, detail) => addToast(msg, 'error', detail),
  warning: (msg, detail) => addToast(msg, 'warning', detail),
  info: (msg, detail) => addToast(msg, 'info', detail),
  remove: removeToast
})
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>
