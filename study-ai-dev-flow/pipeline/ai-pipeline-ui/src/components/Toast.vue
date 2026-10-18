<template>
  <div class="fixed top-4 right-4 z-50 space-y-2">
    <TransitionGroup name="toast">
      <div
        v-for="toast in toasts"
        :key="toast.id"
        :class="[
          'flex items-start gap-3 px-3 py-2.5 rounded border bg-surface min-w-72 max-w-sm shadow-none',
          getBorderClass(toast.type)
        ]"
      >
        <span :class="['mt-0.5 w-1.5 h-1.5 rounded-full flex-shrink-0', getDotClass(toast.type)]"></span>
        <div class="flex-1 min-w-0">
          <p class="text-sm text-ink font-medium">{{ toast.message }}</p>
          <p v-if="toast.detail" class="mono text-xs text-ink-muted mt-0.5 break-all">{{ toast.detail }}</p>
        </div>
        <button
          @click="removeToast(toast.id)"
          class="text-ink-muted hover:text-ink text-lg leading-none px-1"
          aria-label="关闭"
        >×</button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const toasts = ref([])

const getBorderClass = (type) => {
  switch (type) {
    case 'success': return 'border-ok/30'
    case 'error': return 'border-danger/30'
    case 'warning': return 'border-warn/30'
    default: return 'border-line'
  }
}

const getDotClass = (type) => {
  switch (type) {
    case 'success': return 'bg-ok'
    case 'error': return 'bg-danger'
    case 'warning': return 'bg-warn'
    default: return 'bg-accent'
  }
}

const addToast = (message, type = 'info', detail = '') => {
  const id = Date.now()
  toasts.value.push({ id, message, type, detail })
  setTimeout(() => removeToast(id), 4000)
}

const removeToast = (id) => {
  const index = toasts.value.findIndex(t => t.id === id)
  if (index > -1) toasts.value.splice(index, 1)
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
  transition: opacity 0.15s ease, transform 0.15s ease;
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>
