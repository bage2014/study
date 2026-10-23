<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载项目" subtext="请稍候" />

    <template v-else>
      <header class="flex items-end justify-between">
        <div>
          <h1 class="text-2xl font-semibold tracking-tight text-ink">项目</h1>
          <p class="text-ink-secondary mt-1">管理开发项目和代码仓库</p>
        </div>
        <div class="flex items-center gap-2">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索项目"
            class="input w-56"
            @keyup.enter="handleSearch"
          />
          <button @click="showCreateProject = true" class="btn-primary">新建项目</button>
        </div>
      </header>

      <div class="grid grid-cols-4 gap-px bg-line border border-line rounded-lg overflow-hidden">
        <div class="bg-surface p-4"><p class="mono text-xl text-ink">{{ stats.total }}</p><p class="text-2xs text-ink-muted mt-0.5 uppercase tracking-wide">总项目</p></div>
        <div class="bg-surface p-4"><p class="mono text-xl text-ok">{{ stats.active }}</p><p class="text-2xs text-ink-muted mt-0.5 uppercase tracking-wide">活跃</p></div>
        <div class="bg-surface p-4"><p class="mono text-xl text-accent">{{ stats.pipelineCount }}</p><p class="text-2xs text-ink-muted mt-0.5 uppercase tracking-wide">流水线执行</p></div>
        <div class="bg-surface p-4"><p class="mono text-xl text-ink">{{ stats.successRate }}</p><p class="text-2xs text-ink-muted mt-0.5 uppercase tracking-wide">成功率</p></div>
      </div>

      <div class="card overflow-hidden">
        <table class="w-full text-sm">
          <thead>
            <tr class="border-b border-line text-left text-2xs font-medium uppercase tracking-wide text-ink-muted">
              <th class="px-5 py-3">名称</th>
              <th class="px-5 py-3">类型</th>
              <th class="px-5 py-3">来源</th>
              <th class="px-5 py-3">状态</th>
              <th class="px-5 py-3">更新时间</th>
              <th class="px-5 py-3"></th>
            </tr>
          </thead>
          <tbody class="divide-y divide-line">
            <tr
              v-for="project in paginatedProjects"
              :key="project.id"
              @click="goToProjectDetail(project.id)"
              class="hover:bg-base cursor-pointer transition-colors"
            >
              <td class="px-5 py-3">
                <p class="text-ink font-medium">{{ project.name }}</p>
                <p class="text-xs text-ink-muted mt-0.5 line-clamp-1">{{ project.description }}</p>
              </td>
              <td class="px-5 py-3 mono text-xs text-ink-secondary">{{ project.projectType }}</td>
              <td class="px-5 py-3">
                <span :class="['tag', project.projectSource === 'GITHUB' ? 'bg-accent-soft text-accent' : 'bg-base text-ink-secondary']">
                  {{ project.projectSource === 'GITHUB' ? 'GitHub' : '本地' }}
                </span>
              </td>
              <td class="px-5 py-3">
                <span :class="['tag', statusTagClass(project.status)]">{{ statusText(project.status) }}</span>
              </td>
              <td class="px-5 py-3 text-xs text-ink-muted">{{ formatTime(project.updatedAt) }}</td>
              <td class="px-5 py-3 text-right">
                <button @click.stop="startPipeline(project)" class="text-accent hover:underline text-sm">启动流水线</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="totalElements > pageSize" class="flex items-center justify-center gap-4">
        <button @click="currentPage = Math.max(0, currentPage - 1)" :disabled="currentPage === 0" class="btn-secondary">←</button>
        <span class="mono text-xs text-ink-muted">{{ currentPage + 1 }} / {{ totalPages }}</span>
        <button @click="currentPage = Math.min(totalPages - 1, currentPage + 1)" :disabled="currentPage === totalPages - 1" class="btn-secondary">→</button>
      </div>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">最近活动</h2>
        <ul class="divide-y divide-line">
          <li
            v-for="activity in recentActivities"
            :key="activity.id"
            @click="goToPipelineDetail(activity.pipelineId)"
            class="flex items-center justify-between px-5 py-3 hover:bg-base cursor-pointer transition-colors"
          >
            <div class="flex items-center gap-3 min-w-0">
              <span :class="['w-1.5 h-1.5 rounded-full flex-shrink-0', activityDotClass(activity.status)]"></span>
              <p class="text-sm text-ink truncate">{{ activity.message }}</p>
            </div>
            <div class="flex items-center gap-3 flex-shrink-0">
              <span class="text-xs text-ink-muted">{{ activity.time }}</span>
              <span :class="['tag', activityTagClass(activity.status)]">{{ activity.statusText }}</span>
            </div>
          </li>
        </ul>
      </section>
    </template>

    <div v-if="showCreateProject" class="fixed inset-0 bg-ink/40 flex items-center justify-center z-50" @click.self="showCreateProject = false">
      <div class="card w-full max-w-lg mx-4">
        <div class="px-5 py-4 border-b border-line flex items-center justify-between">
          <h2 class="font-medium text-ink">新建项目</h2>
          <button @click="showCreateProject = false" class="text-ink-muted hover:text-ink text-xl leading-none">×</button>
        </div>
        <div class="p-5 space-y-4">
          <div>
            <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">项目名称</label>
            <input v-model="newProject.projectName" type="text" class="input" placeholder="my-project" />
          </div>
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">来源</label>
              <select v-model="newProject.projectSource" class="input">
                <option value="LOCAL">本地项目</option>
                <option value="GITHUB">GitHub 仓库</option>
              </select>
            </div>
            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">类型</label>
              <select v-model="newProject.projectType" class="input">
                <option value="SPRING_BOOT">Spring Boot</option>
                <option value="VUE">Vue</option>
                <option value="REACT">React</option>
                <option value="NODE">Node.js</option>
              </select>
            </div>
          </div>
          <div v-if="newProject.projectSource === 'LOCAL'">
            <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">本地路径</label>
            <input v-model="newProject.localPath" type="text" class="input mono" placeholder="/Users/username/projects/my-project" />
            <p class="text-xs text-ink-muted mt-1">留空则自动创建到 /tmp/project-name</p>
          </div>
          <template v-if="newProject.projectSource === 'GITHUB'">
            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">仓库地址</label>
              <input v-model="newProject.repoUrl" type="text" class="input mono" placeholder="https://github.com/username/repo.git" />
            </div>
            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">Token（可选）</label>
                <input v-model="newProject.githubToken" type="password" class="input" placeholder="私有仓库" />
              </div>
              <div>
                <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">分支</label>
                <input v-model="newProject.branch" type="text" class="input mono" placeholder="main" />
              </div>
            </div>
          </template>
          <div>
            <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">描述</label>
            <textarea v-model="newProject.description" rows="3" class="input resize-none" placeholder="项目用途说明"></textarea>
          </div>
        </div>
        <div class="px-5 py-4 border-t border-line flex justify-end gap-2">
          <button @click="showCreateProject = false" class="btn-secondary">取消</button>
          <button @click="createProject" class="btn-primary">创建</button>
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

const stats = ref({ total: 0, active: 0, pipelineCount: 0, successRate: '0%' })
const recentActivities = ref([])
const showCreateProject = ref(false)
const newProject = ref({
  projectName: '', projectType: 'SPRING_BOOT', projectSource: 'LOCAL',
  description: '', localPath: '', repoUrl: '', githubToken: '', branch: 'main'
})

const paginatedProjects = computed(() => projects.value)

const statusText = (s) => ({ active: '运行中', maintenance: '维护中', stopped: '已停止' }[s] || s)
const statusTagClass = (s) => ({
  active: 'bg-ok-soft text-ok', maintenance: 'bg-warn-soft text-warn', stopped: 'bg-base text-ink-muted'
}[s] || 'bg-base text-ink-secondary')

const activityDotClass = (s) => ({ success: 'bg-ok', running: 'bg-warn', failed: 'bg-danger' }[s] || 'bg-line-strong')
const activityTagClass = (s) => ({
  success: 'bg-ok-soft text-ok', running: 'bg-warn-soft text-warn', failed: 'bg-danger-soft text-danger'
}[s] || 'bg-base text-ink-secondary')

const formatTime = (t) => {
  if (!t) return '—'
  const diff = Date.now() - (typeof t === 'number' ? t : new Date(t).getTime())
  const d = Math.floor(diff / 86400000)
  const h = Math.floor(diff / 3600000)
  const m = Math.floor(diff / 60000)
  if (d > 0) return `${d} 天前`
  if (h > 0) return `${h} 小时前`
  if (m > 0) return `${m} 分钟前`
  return '刚刚'
}

const loadProjects = async () => {
  try {
    const params = new URLSearchParams()
    params.set('page', currentPage.value)
    params.set('size', pageSize.value)
    if (searchKeyword.value) params.set('keyword', searchKeyword.value)
    const res = await projectApi.getAllProjects(params)

    projects.value = res.data.content.map(p => ({
      id: p.id, name: p.projectName, description: p.description,
      status: p.status, projectType: p.projectType, projectSource: p.projectSource,
      localPath: p.localPath, updatedAt: p.updatedAt
    }))
    totalElements.value = res.data.totalElements
    totalPages.value = res.data.totalPages
    projectStore.setProjects(projects.value)

    stats.value = {
      total: totalElements.value,
      active: projects.value.filter(p => p.status === 'active').length,
      pipelineCount: projects.value.reduce((s, p) => s + (p.pipelineCount || 0), 0),
      successRate: projects.value.length > 0
        ? Math.round(projects.value.reduce((s, p) => s + (p.successRate || 0), 0) / projects.value.length) + '%'
        : '0%'
    }
  } catch (error) {
    console.error('Failed to load projects:', error)
    projects.value = [
      { id: 1, name: 'demo-backend', description: 'Spring Boot 后端服务', status: 'active', projectType: 'SPRING_BOOT', localPath: '/tmp/demo-backend', updatedAt: Date.now() },
      { id: 2, name: 'demo-frontend', description: 'Vue 3 前端应用', status: 'active', projectType: 'VUE', localPath: '/tmp/demo-frontend', updatedAt: Date.now() },
    ]
    totalElements.value = projects.value.length
    totalPages.value = 1
    stats.value = { total: 2, active: 2, pipelineCount: 0, successRate: '—' }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { currentPage.value = 0; loadProjects() }

const loadRecentActivities = async () => {
  try {
    const pipeRes = await pipelineApi.getAllPipelines()
    recentActivities.value = pipeRes.data.slice(0, 5).map(p => {
      const title = p.requirementTitle || `流水线 ${p.pipelineId.substring(0, 8)}`
      const s = p.status === 'COMPLETED' ? 'success' : p.status === 'RUNNING' ? 'running' : 'failed'
      return {
        id: p.pipelineId, pipelineId: p.pipelineId,
        message: `${title} ${p.status === 'COMPLETED' ? '执行完成' : p.status === 'RUNNING' ? '执行中' : '执行失败'}`,
        time: formatTime(p.createdAt),
        status: s,
        statusText: p.status === 'COMPLETED' ? '成功' : p.status === 'RUNNING' ? '进行中' : '失败'
      }
    })
  } catch (error) {
    console.error('Failed to load activities:', error)
    recentActivities.value = []
  }
}

const createProject = async () => {
  if (!newProject.value.projectName) return showToast?.warning('请输入项目名称')
  if (newProject.value.projectSource === 'GITHUB' && !newProject.value.repoUrl) {
    return showToast?.warning('请输入 GitHub 仓库地址')
  }
  try {
    await projectApi.createProject(newProject.value)
    showCreateProject.value = false
    newProject.value = {
      projectName: '', projectType: 'SPRING_BOOT', projectSource: 'LOCAL',
      description: '', localPath: '', repoUrl: '', githubToken: '', branch: 'main'
    }
    await loadProjects()
    showToast?.success('项目创建成功')
  } catch (error) {
    console.error('Failed to create project:', error)
    showToast?.error(error.response?.data?.error || error.message || '创建失败')
  }
}

const goToProjectDetail = (id) => router.push(`/projects/${id}`)
const startPipeline = (project) => { projectStore.setCurrentProject(project); router.push('/') }
const goToPipelineDetail = (id) => router.push(`/pipelines/${id}`)

onMounted(() => { loadProjects(); loadRecentActivities() })
</script>
