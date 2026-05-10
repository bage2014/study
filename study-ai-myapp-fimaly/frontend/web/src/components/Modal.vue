<template>
  <div 
    v-if="visible" 
    class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" 
    @click.self="handleClose"
  >
    <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in" :class="{ 'max-w-lg': size === 'lg', 'max-w-xl': size === 'xl' }">
      <!-- Header -->
      <div v-if="title || showClose" class="flex items-center justify-between mb-4">
        <div v-if="title" class="flex items-center">
          <div v-if="icon" :class="['w-10 h-10 rounded-lg flex items-center justify-center mr-3', iconBgClass]">
            <svg xmlns="http://www.w3.org/2000/svg" :class="['h-5 w-5', iconClass]" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path :d="iconPath" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" />
            </svg>
          </div>
          <h3 style="font-size: 18px; font-weight: 600; color: #111827;">{{ title }}</h3>
        </div>
        <button 
          v-if="showClose" 
          @click="handleClose" 
          style="color: #9ca3af; transition: color 0.2s;"
          onmouseover="this.style.color='#4b5563'"
          onmouseout="this.style.color='#9ca3af'"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Body -->
      <div class="modal-body">
        <slot></slot>
      </div>

      <!-- Footer -->
      <div v-if="$slots.footer" class="mt-6">
        <slot name="footer"></slot>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'Modal',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    },
    showClose: {
      type: Boolean,
      default: true
    },
    size: {
      type: String,
      default: 'md',
      validator: (value) => ['sm', 'md', 'lg', 'xl'].includes(value)
    },
    icon: {
      type: String,
      default: ''
    }
  },
  emits: ['close'],
  setup(props, { emit }) {
    const handleClose = () => {
      emit('close')
    }

    const iconBgClass = computed(() => {
      const iconMap = {
        'add': 'bg-gradient-to-br from-green-100 to-green-200',
        'edit': 'bg-gradient-to-br from-blue-100 to-blue-200',
        'delete': 'bg-gradient-to-br from-red-100 to-red-200',
        'info': 'bg-gradient-to-br from-blue-100 to-blue-200',
        'warning': 'bg-gradient-to-br from-yellow-100 to-yellow-200',
        'success': 'bg-gradient-to-br from-green-100 to-green-200',
        'error': 'bg-gradient-to-br from-red-100 to-red-200'
      }
      return iconMap[props.icon] || 'bg-gradient-to-br from-gray-100 to-gray-200'
    })

    const iconClass = computed(() => {
      const iconMap = {
        'add': 'text-green-600',
        'edit': 'text-blue-600',
        'delete': 'text-red-600',
        'info': 'text-blue-600',
        'warning': 'text-yellow-600',
        'success': 'text-green-600',
        'error': 'text-red-600'
      }
      return iconMap[props.icon] || 'text-gray-600'
    })

    const iconPath = computed(() => {
      const iconMap = {
        'add': 'M12 4v16m8-8H4',
        'edit': 'M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z',
        'delete': 'M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16',
        'info': 'M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z',
        'warning': 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z',
        'success': 'M5 13l4 4L19 7',
        'error': 'M6 18L18 6M6 6l12 12'
      }
      return iconMap[props.icon] || 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z'
    })

    return {
      handleClose,
      iconBgClass,
      iconClass,
      iconPath
    }
  }
}
</script>

<style scoped>
.modal-body {
  color: #374151;
}
</style>
