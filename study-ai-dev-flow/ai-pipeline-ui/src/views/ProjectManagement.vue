<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载项目列表中..." subtext="请稍候" />
    
    <template v-else>
      <div class="flex justify-between items-center">
        <div>
          <h3 class="text-xl font-bold text-gray-800">我的项目</h3>
          <p class="text-sm text-gray-500 mt-1">管理您的开发项目和代码仓库</p>
        </div>
        <div class="flex items-center space-x-3">
          <div class="relative">
            <input 
              v-model="searchKeyword"
              type="text"
              placeholder="搜索项目..."
              class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent w-64"
              @keyup.enter="handleSearch"
            />
            <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">🔍</span>
          </div>
          <button 
            @click="showCreateProject = true" 
            class="bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-all shadow-md hover:shadow-lg flex items-center space-x-2"
          >
            <span>+</span>
            <span>新建项目</span>
          </button>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div 
          v-for="project in paginatedProjects" 
          :key="project.id"
          class="bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 overflow-hidden group cursor-pointer"
          @click="goToProjectDetail(project.id)"
        >
          <div :class="['p-6', project.status === 'active' ? 'bg-gradient-to-br from-indigo-50 to-purple-50' : 'bg-gray-50']">
            <div class="flex items-start justify-between mb-4">
              <div :class="['w-12 h-12 rounded-xl flex items-center justify-center text-2xl', project.iconBgClass]">
                {{ project.icon }}
              </div>
              <span :class="[
                'px-3 py-1 rounded-full text-xs font-medium',
                project.status === 'active' ? 'bg-green-100 text-green-700' :
                project.status === 'maintenance' ? 'bg-yellow-100 text-yellow-700' :
                'bg-gray-100 text-gray-600'
              ]">
                {{ statusText(project.status) }}
              </span>
            </div>
            <h4 class="text-lg font-semibold text-gray-800 mb-1 group-hover:text-indigo-600 transition-colors">
              {{ project.name }}
            </h4>
            <p class="text-sm text-gray-500 mb-4 line-clamp-2">{{ project.description }}</p>
            <div class="flex items-center justify-between text-xs text-gray-400">
              <div class="flex items-center space-x-2">
                <span>{{ project.projectType }}</span>
                <span :class="[
                  'px-2 py-0.5 rounded-full',
                  project.projectSource === 'GITHUB' ? 'bg-blue-100 text-blue-600' : 'bg-gray-100 text-gray-500'
                ]">
                  {{ project.projectSource === 'GITHUB' ? 'GitHub' : '本地' }}
                </span>
              </div>
              <span>{{ formatTime(project.updatedAt) }}</span>
            </div>
          </div>
          <div class="px-6 py-3 bg-white border-t border-gray-100 flex items-center justify-between">
            <button 
              @click.stop="goToProjectDetail(project.id)"
              class="text-sm text-indigo-600 hover:text-indigo-800 font-medium"
            >
              查看详情
            </button>
            <button 
              @click.stop="startPipeline(project)"
              class="text-sm text-green-600 hover:text-green-800 font-medium flex items-center space-x-1"
            >
              <span>🚀</span>
              <span>启动流水线</span>
            </button>
          </div>
        </div>
      </div>

      <div v-if="totalElements > pageSize" class="flex items-center justify-center space-x-4 pt-4">
        <button 
          @click="currentPage = Math.max(0, currentPage - 1)"
          :disabled="currentPage === 0"
          :class="[
            'px-4 py-2 rounded-lg transition-colors',
            currentPage === 0 
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed' 
              : 'bg-white border border-gray-200 text-gray-700 hover:bg-gray-50'
          ]"
        >
          ←
        </button>
        <span class="text-sm text-gray-600">
          第 {{ currentPage + 1 }} / {{ totalPages }} 页
        </span>
        <button 
          @click="currentPage = Math.min(totalPages - 1, currentPage + 1)"
          :disabled="currentPage === totalPages - 1"
          :class="[
            'px-4 py-2 rounded-lg transition-colors',
            currentPage === totalPages - 1 
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed' 
              : 'bg-white border border-gray-200 text-gray-700 hover:bg-gray-50'
          ]"
        >
          →
        </button>
      </div>

      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-gray-800">{{ stats.total }}</p>
              <p class="text-sm text-gray-500 mt-1">总项目数</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center">
              <span class="text-indigo-600 text-lg">📦</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-green-600">{{ stats.active }}</p>
              <p class="text-sm text-gray-500 mt-1">活跃项目</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center">
              <span class="text-green-600 text-lg">✓</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-blue-600">{{ stats.pipelineCount }}</p>
              <p class="text-sm text-gray-500 mt-1">流水线执行</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center">
              <span class="text-blue-600 text-lg">🔄</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-purple-600">{{ stats.successRate }}</p>
              <p class="text-sm text-gray-500 mt-1">成功率</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-purple-100 flex items-center justify-center">
              <span class="text-purple-600 text-lg">📊</span>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-sm p-6">
        <h4 class="font-semibold text-gray-800 mb-4">最近活动</h4>
        <div class="space-y-3">
          <div 
            v-for="activity in recentActivities" 
            :key="activity.id"
            class="flex items-center justify-between p-3 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer"
            @click="goToPipelineDetail(activity.pipelineId)"
          >
            <div class="flex items-center space-x-3">
              <span :class="['w-8 h-8 rounded-full flex items-center justify-center text-sm', activity.iconBgClass]">
                {{ activity.icon }}
              </span>
              <div>
                <p class="text-sm text-gray-800">{{ activity.message }}</p>
                <p class="text-xs text-gray-500">{{ activity.time }}</p>
              </div>
            </div>
            <div class="flex items-center space-x-2">
              <span :class="[
                'text-xs px-2 py-1 rounded-full',
                activity.status === 'success' ? 'bg-green-100 text-green-700' :
                activity.status === 'running' ? 'bg-yellow-100 text-yellow-700' :
                'bg-gray-100 text-gray-600'
              ]">
                {{ activity.statusText }}
              </span>
              <span class="text-gray-400 text-sm">→</span>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-if="showCreateProject" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl w-full max-w-lg p-6 mx-4">
        <h4 class="text-lg font-semibold text-gray-800 mb-4">新建项目</h4>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">项目名称</label>
            <input 
              v-model="newProject.projectName" 
              type="text" 
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              placeholder="请输入项目名称"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">项目来源</label>
            <select 
              v-model="newProject.projectSource" 
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
            >
              <option value="LOCAL">本地项目</option>
              <option value="GITHUB">GitHub 仓库</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">项目类型</label>
            <select 
              v-model="newProject.projectType" 
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
            >
              <option value="SPRING_BOOT">Spring Boot</option>
              <option value="VUE">Vue</option>
              <option value="REACT">React</option>
              <option value="NODE">Node.js</option>
            </select>
          </div>
          <div v-if="newProject.projectSource === 'LOCAL'">
            <label class="block text-sm font-medium text-gray-700 mb-1">本地路径</label>
            <input 
              v-model="newProject.localPath" 
              type="text" 
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              placeholder="例如: /Users/username/projects/my-project"
            />
            <p class="text-xs text-gray-500 mt-1">留空则自动创建到 /tmp/project-name</p>
          </div>
          <div v-if="newProject.projectSource === 'GITHUB'">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">GitHub 仓库地址</label>
              <input 
                v-model="newProject.repoUrl" 
                type="text" 
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="例如: https://github.com/username/repo.git"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">GitHub Token (可选)</label>
              <input 
                v-model="newProject.githubToken" 
                type="password" 
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="用于访问私有仓库"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">分支名称</label>
              <input 
                v-model="newProject.branch" 
                type="text" 
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="main"
                value="main"
              />
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
            <textarea 
              v-model="newProject.description" 
              rows="3"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 resize-none"
              placeholder="请输入项目描述..."
            ></textarea>
          </div>
        </div>
        <div class="mt-6 flex gap-3">
          <button 
            @click="showCreateProject = false" 
            class="flex-1 bg-gray-100 text-gray-700 px-4 py-2.5 rounded-lg hover:bg-gray-200 transition-colors font-medium"
          >
            取消
          </button>
          <button 
            @click="createProject" 
            class="flex-1 bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-colors font-medium"
          >
            创建
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { projectApi, pipelineApi } from '../api'
import Loading from '../components/Loading.vue'
import { projectStore } from '../stores/projectStore'

const router = useRouter()
const showToast = inject('showToast')

const loading = ref(true)
const projects = ref([])
const currentPage = ref(0)
const pageSize = ref(6)
const totalElements = ref(0)
const totalPages = ref(0)
const searchKeyword = ref('')

const stats = ref({
  total: 0,
  active: 0,
  pipelineCount: 0,
  successRate: '0%'
})

const recentActivities = ref([])
const showCreateProject = ref(false)
const newProject = ref({
  projectName: '',
  projectType: 'SPRING_BOOT',
  projectSource: 'LOCAL',
  description: '',
  localPath: '',
  repoUrl: '',
  githubToken: '',
  branch: 'main'
})

const paginatedProjects = computed(() => {
  return projects.value
})

const statusText = (status) => {
  switch (status) {
    case 'active': return '运行中'
    case 'maintenance': return '维护中'
    case 'stopped': return '已停止'
    default: return status
  }
}

const formatTime = (timestamp) => {
  if (!timestamp) return '未知'
  const diff = Date.now() - (typeof timestamp === 'number' ? timestamp : new Date(timestamp).getTime())
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const minutes = Math.floor(diff / (1000 * 60))
  
  if (days > 0) return `${days}天前`
  if (hours > 0) return `${hours}小时前`
  if (minutes > 0) return `${minutes}分钟前`
  return '刚刚'
}

const getProjectIcon = (type) => {
  switch (type) {
    case 'SPRING_BOOT': return '☕'
    case 'VUE': return '💚'
    case 'REACT': return '⚛️'
    case 'NODE': return '🟢'
    default: return '📦'
  }
}

const getProjectIconBg = (type) => {
  switch (type) {
    case 'SPRING_BOOT': return 'bg-orange-100'
    case 'VUE': return 'bg-green-100'
    case 'REACT': return 'bg-blue-100'
    case 'NODE': return 'bg-green-100'
    default: return 'bg-gray-100'
  }
}

const loadProjects = async () => {
  try {
    const params = new URLSearchParams()
    params.set('page', currentPage.value)
    params.set('size', pageSize.value)
    if (searchKeyword.value) {
      params.set('keyword', searchKeyword.value)
    }
    const res = await projectApi.getAllProjects(params)
    
    projects.value = res.data.content.map(p => ({
      id: p.id,
      name: p.projectName,
      description: p.description,
      icon: getProjectIcon(p.projectType),
      iconBgClass: getProjectIconBg(p.projectType),
      status: p.status,
      projectType: p.projectType,
      projectSource: p.projectSource,
      localPath: p.localPath,
      updatedAt: p.updatedAt
    }))
    totalElements.value = res.data.totalElements
    totalPages.value = res.data.totalPages
    
    projectStore.setProjects(projects.value)
    
    stats.value = {
      total: totalElements.value,
      active: projects.value.filter(p => p.status === 'active').length,
      pipelineCount: projects.value.reduce((sum, p) => sum + (p.pipelineCount || 0), 0),
      successRate: projects.value.length > 0 
        ? Math.round(projects.value.reduce((sum, p) => sum + (p.successRate || 0), 0) / projects.value.length) + '%'
        : '0%'
    }
  } catch (error) {
    console.error('Failed to load projects:', error)
    projects.value = [
      { id: 1, name: 'demo-backend', description: 'Spring Boot 后端服务', icon: '☕', iconBgClass: 'bg-orange-100', status: 'active', projectType: 'SPRING_BOOT', localPath: '/tmp/demo-backend', updatedAt: Date.now() },
      { id: 2, name: 'demo-frontend', description: 'Vue 3 前端应用', icon: '💚', iconBgClass: 'bg-green-100', status: 'active', projectType: 'VUE', localPath: '/tmp/demo-frontend', updatedAt: Date.now() },
      { id: 3, name: 'api-gateway', description: 'API 网关服务', icon: '🚪', iconBgClass: 'bg-blue-100', status: 'maintenance', projectType: 'SPRING_BOOT', localPath: '/tmp/api-gateway', updatedAt: Date.now() },
      { id: 4, name: 'data-service', description: '数据服务模块', icon: '📊', iconBgClass: 'bg-purple-100', status: 'stopped', projectType: 'SPRING_BOOT', localPath: '/tmp/data-service', updatedAt: Date.now() },
      { id: 5, name: 'ai-pipeline-ui', description: 'AI 流水线前端界面', icon: '🎨', iconBgClass: 'bg-pink-100', status: 'active', projectType: 'VUE', localPath: '/tmp/ai-pipeline-ui', updatedAt: Date.now() },
    ]
    totalElements.value = projects.value.length
    totalPages.value = 1
    stats.value = { total: 5, active: 3, pipelineCount: 12, successRate: '92%' }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 0
  loadProjects()
}

const loadRecentActivities = async () => {
  try {
    const pipeRes = await pipelineApi.getAllPipelines()
    const pipelines = pipeRes.data.slice(0, 5)
    
    recentActivities.value = pipelines.map(p => ({
      id: p.pipelineId,
      pipelineId: p.pipelineId,
      icon: p.status === 'COMPLETED' ? '✓' : p.status === 'RUNNING' ? '●' : '✗',
      iconBgClass: p.status === 'COMPLETED' ? 'bg-green-100' : p.status === 'RUNNING' ? 'bg-yellow-100' : 'bg-red-100',
      message: `流水线 ${p.pipelineId.substring(0, 8)} ${p.status === 'COMPLETED' ? '执行完成' : p.status === 'RUNNING' ? '执行中' : '执行失败'}`,
      time: formatTime(p.createdAt),
      status: p.status === 'COMPLETED' ? 'success' : p.status === 'RUNNING' ? 'running' : 'failed',
      statusText: p.status === 'COMPLETED' ? '成功' : p.status === 'RUNNING' ? '进行中' : '失败'
    }))
  } catch (error) {
    console.error('Failed to load activities:', error)
    recentActivities.value = [
      { id: 1, pipelineId: 'test-pipeline-1', icon: '✓', iconBgClass: 'bg-green-100', message: 'demo-backend 流水线执行完成', time: '2小时前', status: 'success', statusText: '成功' },
      { id: 2, pipelineId: 'test-pipeline-2', icon: '●', iconBgClass: 'bg-yellow-100', message: 'demo-backend 用户管理功能开发中', time: '5小时前', status: 'running', statusText: '进行中' },
      { id: 3, pipelineId: 'test-pipeline-3', icon: '+', iconBgClass: 'bg-blue-100', message: '新建需求：数据统计报表', time: '1天前', status: 'info', statusText: '新建' },
    ]
  }
}

const createProject = async () => {
  if (!newProject.value.projectName) {
    showToast?.warning('请输入项目名称')
    return
  }
  
  if (newProject.value.projectSource === 'GITHUB' && !newProject.value.repoUrl) {
    showToast?.warning('请输入GitHub仓库地址')
    return
  }
  
  try {
    await projectApi.createProject(newProject.value)
    showCreateProject.value = false
    newProject.value = { 
      projectName: '', 
      projectType: 'SPRING_BOOT', 
      projectSource: 'LOCAL',
      description: '',
      localPath: '',
      repoUrl: '',
      githubToken: '',
      branch: 'main'
    }
    await loadProjects()
    showToast?.success('项目创建成功')
  } catch (error) {
    console.error('Failed to create project:', error)
    showToast?.error('创建失败，请重试')
  }
}

const goToProjectDetail = (id) => {
  router.push(`/projects/${id}`)
}

const startPipeline = (project) => {
  projectStore.setCurrentProject(project)
  router.push('/')
}

const goToPipelineDetail = (pipelineId) => {
  router.push(`/pipelines/${pipelineId}`)
}

onMounted(() => {
  loadProjects()
  loadRecentActivities()
})
</script>
