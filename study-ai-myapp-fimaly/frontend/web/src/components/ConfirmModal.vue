<template>
  <div 
    v-if="visible" 
    class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" 
    @click.self="handleCancel"
  >
    <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
      <!-- Header -->
      <div class="flex items-center mb-4">
        <div :class="['w-12 h-12 rounded-full flex items-center justify-center mr-4', iconBgClass]">
          <svg xmlns="http://www.w3.org/2000/svg" :class="['h-6 w-6', iconClass]" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path :d="iconPath" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" />
          </svg>
        </div>
        <div>
          <h3 style="font-size: 18px; font-weight: 600; color: #111827;">{{ title }}</h3>
          <p style="font-size: 14px; color: #6b7280;">{{ subtitle }}</p>
        </div>
      </div>

      <!-- Body -->
      <div class="mb-6">
        <p style="color: #374151;">{{ message }}</p>
      </div>

      <!-- Footer -->
      <div class="flex justify-end space-x-3">
        <button 
          @click="handleCancel" 
          class="btn-secondary"
        >
          {{ cancelText }}
        </button>
        <button 
          @click="handleConfirm" 
          :class="confirmButtonClass"
        >
          {{ confirmText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'ConfirmModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '确认操作'
    },
    subtitle: {
      type: String,
      default: ''
    },
    message: {
      type: String,
      default: '确定要执行此操作吗？'
    },
    type: {
      type: String,
      default: 'confirm',
      validator: (value) => ['confirm', 'delete', 'warning'].includes(value)
    },
    confirmText: {
      type: String,
      default: '确定'
    },
    cancelText: {
      type: String,
      default: '取消'
    }
  },
  emits: ['confirm', 'cancel'],
  setup(props, { emit }) {
    const handleConfirm = () => {
      emit('confirm')
    }

    const handleCancel = () => {
      emit('cancel')
    }

    const iconBgClass = computed(() => {
      const iconMap = {
        'confirm': 'bg-gradient-to-br from-blue-100 to-blue-200',
        'delete': 'bg-gradient-to-br from-red-100 to-red-200',
        'warning': 'bg-gradient-to-br from-yellow-100 to-yellow-200'
      }
      return iconMap[props.type] || iconMap['confirm']
    })

    const iconClass = computed(() => {
      const iconMap = {
        'confirm': 'text-blue-600',
        'delete': 'text-red-600',
        'warning': 'text-yellow-600'
      }
      return iconMap[props.type] || iconMap['confirm']
    })

    const iconPath = computed(() => {
      const iconMap = {
        'confirm': 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
        'delete': 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z',
        'warning': 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z'
      }
      return iconMap[props.type] || iconMap['confirm']
    })

    const confirmButtonClass = computed(() => {
      const buttonMap = {
        'confirm': 'btn-primary',
        'delete': 'btn-danger',
        'warning': 'btn-warning'
      }
      return buttonMap[props.type] || buttonMap['confirm']
    })

    return {
      handleConfirm,
      handleCancel,
      iconBgClass,
      iconClass,
      iconPath,
      confirmButtonClass
    }
  }
}
</script>
