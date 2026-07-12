<template>
  <div class="flex h-screen bg-gray-50">
    <aside class="w-64 bg-white border-r border-gray-200 flex flex-col">
      <div class="p-4 border-b border-gray-200">
        <h1 class="text-xl font-bold text-indigo-600">AI Pipeline</h1>
        <p class="text-xs text-gray-500 mt-1">智能软件开发流水线</p>
      </div>
      
      <nav class="flex-1 p-4 space-y-2">
        <router-link 
          v-for="menu in menuItems" 
          :key="menu.path"
          :to="menu.path"
          :class="[
            'flex items-center px-4 py-3 rounded-lg transition-all',
            $route.path.startsWith(menu.path) 
              ? 'bg-indigo-50 text-indigo-700 font-medium' 
              : 'text-gray-600 hover:bg-gray-50'
          ]"
        >
          <span class="mr-3 text-xl">{{ menu.icon }}</span>
          <span>{{ menu.label }}</span>
          <span 
            v-if="menu.badge" 
            :class="[
              'ml-auto text-xs px-2 py-0.5 rounded-full',
              menu.badgeClass
            ]"
          >
            {{ menu.badge }}
          </span>
        </router-link>
      </nav>

      <div class="p-4 border-t border-gray-200">
        <div class="flex items-center">
          <div class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center">
            <span class="text-indigo-600 font-bold text-sm">AI</span>
          </div>
          <div class="ml-3">
            <p class="text-sm font-medium text-gray-700">AI Assistant</p>
            <p class="text-xs text-gray-500">在线</p>
          </div>
        </div>
      </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden">
      <header class="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
        <div>
          <h2 class="text-lg font-semibold text-gray-800">{{ currentPageTitle }}</h2>
          <p class="text-sm text-gray-500">{{ currentPageDesc }}</p>
        </div>
        <div class="flex items-center space-x-4">
          <button class="p-2 rounded-lg hover:bg-gray-100 transition-colors">
            <span class="text-gray-500 text-xl">🔔</span>
          </button>
          <button class="p-2 rounded-lg hover:bg-gray-100 transition-colors">
            <span class="text-gray-500 text-xl">⚙️</span>
          </button>
        </div>
      </header>

      <div class="flex-1 overflow-auto p-6">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const menuItems = [
  { path: '/projects', label: '项目管理', icon: '📁', badge: '5', badgeClass: 'bg-blue-100 text-blue-700' },
  { path: '/', label: '选择项目', icon: '✏️' },
  { path: '/design', label: '方案设计', icon: '🎨' },
  { path: '/coding', label: '编码实现', icon: '💻' },
  { path: '/preview', label: '变更预览', icon: '👁️' },
  { path: '/build', label: '编译结果', icon: '🔨' },
  { path: '/deploy', label: '发布情况', icon: '🚀' },
  { path: '/pipelines', label: '流水线列表', icon: '📋', badge: '1', badgeClass: 'bg-green-100 text-green-700' },
]

const pageTitles = {
  '/': { title: '选择项目', desc: '选择项目并输入需求，AI自动分析代码逻辑' },
  '/projects': { title: '项目管理', desc: '管理您的开发项目和代码仓库' },
  '/projects/:id': { title: '项目详情', desc: '查看项目的需求汇总和代码结构' },
  '/pipelines': { title: '流水线列表', desc: '查看所有流水线的执行状态' },
  '/pipelines/:id': { title: '流水线详情', desc: '查看需求的完整开发流程' },
  '/design': { title: '方案设计', desc: 'AI 自动生成技术方案和架构设计' },
  '/coding': { title: '编码实现', desc: 'AI 自动生成代码实现' },
  '/preview': { title: '变更预览', desc: '预览代码变更和影响范围' },
  '/build': { title: '编译结果', desc: '查看构建和测试结果' },
  '/deploy': { title: '发布情况', desc: '查看部署状态和访问地址' },
}

const currentPageTitle = computed(() => {
  const matched = Object.keys(pageTitles).find(key => {
    if (key.includes(':')) {
      const pattern = new RegExp(`^${key.replace(':id', '.*')}$`)
      return pattern.test(route.path)
    }
    return route.path === key
  })
  return (pageTitles[matched] || pageTitles['/']).title
})

const currentPageDesc = computed(() => {
  const matched = Object.keys(pageTitles).find(key => {
    if (key.includes(':')) {
      const pattern = new RegExp(`^${key.replace(':id', '.*')}$`)
      return pattern.test(route.path)
    }
    return route.path === key
  })
  return (pageTitles[matched] || pageTitles['/']).desc
})
</script>
