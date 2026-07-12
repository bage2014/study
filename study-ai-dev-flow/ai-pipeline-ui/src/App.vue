<template>
  <div class="min-h-screen bg-gray-50">
    <nav class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-lg">
      <div class="container mx-auto px-4 py-3">
        <div class="flex justify-between items-center">
          <router-link to="/" class="text-xl font-bold hover:text-gray-100 transition-colors">
            AI Pipeline Platform
          </router-link>
          <div class="flex items-center space-x-6">
            <router-link 
              to="/" 
              :class="[
                'px-3 py-1.5 rounded-md transition-colors',
                $route.path === '/' ? 'bg-white/20' : 'hover:text-gray-200'
              ]"
            >
              录入需求
            </router-link>
            <router-link 
              to="/pipelines" 
              :class="[
                'px-3 py-1.5 rounded-md transition-colors',
                $route.path.startsWith('/pipelines') ? 'bg-white/20' : 'hover:text-gray-200'
              ]"
            >
              流水线列表
            </router-link>
            <router-link 
              to="/projects" 
              :class="[
                'px-3 py-1.5 rounded-md transition-colors',
                $route.path.startsWith('/projects') ? 'bg-white/20' : 'hover:text-gray-200'
              ]"
            >
              项目管理
            </router-link>
            <div v-if="projectStore.currentProject" class="ml-4 pl-4 border-l border-white/30">
              <div class="flex items-center space-x-2">
                <span class="text-xs bg-white/20 px-2 py-0.5 rounded">当前项目</span>
                <span class="text-sm font-medium truncate max-w-32">{{ projectStore.currentProject.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>
    <main class="container mx-auto px-4 py-6">
      <router-view />
    </main>
    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, provide } from 'vue'
import Toast from './components/Toast.vue'
import { projectStore } from './stores/projectStore'

const toastRef = ref(null)

const showToast = {
  success: (msg, detail) => toastRef.value?.success(msg, detail),
  error: (msg, detail) => toastRef.value?.error(msg, detail),
  warning: (msg, detail) => toastRef.value?.warning(msg, detail),
  info: (msg, detail) => toastRef.value?.info(msg, detail)
}

provide('showToast', showToast)
defineExpose({ showToast })
</script>
