<template>
  <div class="min-h-screen bg-base">
    <header class="sticky top-0 z-40 bg-surface border-b border-line">
      <div class="max-w-6xl mx-auto px-6 h-14 flex items-center justify-between">
        <div class="flex items-center gap-8">
          <router-link to="/" class="flex items-center gap-2 text-ink font-semibold tracking-tight">
            <span class="w-5 h-5 rounded-sm bg-accent inline-flex items-center justify-center">
              <span class="w-2 h-2 bg-white rounded-[1px]"></span>
            </span>
            <span>AI Pipeline</span>
          </router-link>
          <nav class="flex items-center gap-1">
            <router-link
              v-for="item in navItems"
              :key="item.to"
              :to="item.to"
              :class="[
                'px-3 py-1.5 rounded text-sm transition-colors',
                isActive(item)
                  ? 'text-ink font-medium bg-base'
                  : 'text-ink-secondary hover:text-ink'
              ]"
            >
              {{ item.label }}
            </router-link>
          </nav>
        </div>
        <div v-if="projectStore.currentProject" class="flex items-center gap-2 text-sm">
          <span class="text-ink-muted text-2xs uppercase tracking-wide">Project</span>
          <span class="mono text-ink">{{ projectStore.currentProject.name }}</span>
        </div>
      </div>
    </header>
    <main class="max-w-6xl mx-auto px-6 py-8">
      <router-view />
    </main>
    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, provide } from 'vue'
import { useRoute } from 'vue-router'
import Toast from './components/Toast.vue'
import { projectStore } from './stores/projectStore'

const route = useRoute()
const toastRef = ref(null)

const navItems = [
  { to: '/', label: '录入需求', match: (p) => p === '/' },
  { to: '/pipelines', label: '流水线', match: (p) => p.startsWith('/pipelines') },
  { to: '/projects', label: '项目', match: (p) => p.startsWith('/projects') },
]

const isActive = (item) => item.match(route.path)

const showToast = {
  success: (msg, detail) => toastRef.value?.success(msg, detail),
  error: (msg, detail) => toastRef.value?.error(msg, detail),
  warning: (msg, detail) => toastRef.value?.warning(msg, detail),
  info: (msg, detail) => toastRef.value?.info(msg, detail)
}

provide('showToast', showToast)
defineExpose({ showToast })
</script>
