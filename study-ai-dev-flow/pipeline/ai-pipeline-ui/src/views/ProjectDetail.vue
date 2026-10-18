<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载项目详情" subtext="请稍候" />

    <template v-else>
      <header class="flex items-start justify-between">
        <div class="flex items-start gap-3 min-w-0">
          <button @click="goBack" class="btn-ghost px-2 flex-shrink-0">←</button>
          <div class="min-w-0">
            <h1 class="text-2xl font-semibold tracking-tight text-ink">{{ project.name }}</h1>
            <p class="text-ink-secondary mt-1 text-sm">{{ project.description }}</p>
          </div>
        </div>
        <button @click="showCreateRequirement = true" class="btn-primary flex-shrink-0">新建需求</button>
      </header>

      <div class="grid grid-cols-2 md:grid-cols-4 gap-px bg-line border border-line rounded-lg overflow-hidden">
        <div class="bg-surface p-5">
          <p class="mono text-2xl text-ink">{{ stats.total }}</p>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mt-1">需求总数</p>
        </div>
        <div class="bg-surface p-5">
          <p class="mono text-2xl text-ok">{{ stats.completed }}</p>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mt-1">已完成</p>
        </div>
        <div class="bg-surface p-5">
          <p class="mono text-2xl text-warn">{{ stats.inProgress }}</p>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mt-1">执行中</p>
        </div>
        <div class="bg-surface p-5">
          <p class="mono text-2xl text-ink-secondary">{{ stats.pending }}</p>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mt-1">待处理</p>
        </div>
      </div>

      <section class="card">
        <div class="px-5 py-3 border-b border-line flex items-center justify-between">
          <h2 class="text-sm font-medium text-ink">需求列表</h2>
          <div class="flex gap-1">
            <button
              v-for="f in [
                { key: 'all', label: '全部' },
                { key: 'completed', label: '已完成' },
                { key: 'in_progress', label: '执行中' },
                { key: 'pending', label: '待处理' }
              ]"
              :key="f.key"
              @click="currentFilter = f.key"
              :class="[
                'px-2.5 py-1 rounded text-xs font-medium transition-colors',
                currentFilter === f.key
                  ? 'bg-accent-soft text-accent'
                  : 'text-ink-secondary hover:bg-base'
              ]"
            >{{ f.label }}</button>
          </div>
        </div>

        <div v-if="requirements.length === 0" class="px-5 py-12 text-center">
          <p class="text-ink-muted text-sm">暂无需求</p>
          <button @click="showCreateRequirement = true" class="btn-primary mt-4 inline-flex">新建第一条需求</button>
        </div>

        <ul v-else class="divide-y divide-line">
          <li
            v-for="req in requirements"
            :key="req.id"
            @click="viewRequirement(req)"
            class="px-5 py-4 flex items-center justify-between hover:bg-base cursor-pointer transition-colors"
          >
            <div class="min-w-0">
              <p class="text-sm font-medium text-ink">{{ req.title }}</p>
              <p class="text-xs text-ink-muted mt-0.5">{{ formatTime(req.createdAt) }}</p>
            </div>
            <div class="flex items-center gap-3 flex-shrink-0">
              <span :class="['tag', statusClass(req.status)]">{{ statusText(req.status) }}</span>
              <button
                @click.stop="startPipeline(req)"
                :disabled="startingPipelineId === req.id"
                class="btn-secondary text-xs"
              >
                {{ startingPipelineId === req.id ? '启动中…' : '启动' }}
              </button>
            </div>
          </li>
        </ul>
      </section>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">项目信息</h2>
        <dl class="px-5 py-4 grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-2 text-sm">
          <div class="flex justify-between"><dt class="text-ink-muted">项目类型</dt><dd class="text-ink">{{ project.projectType }}</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">数据库</dt><dd class="text-ink">{{ project.database }}</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">代码仓库</dt><dd class="mono text-xs text-ink">{{ project.repoUrl || 'GitHub' }}</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">最后更新</dt><dd class="text-ink">{{ formatTime(project.updatedAt) }}</dd></div>
        </dl>
      </section>

      <section class="card">
        <div class="px-5 py-3 border-b border-line flex items-center justify-between">
          <h2 class="text-sm font-medium text-ink">运行控制</h2>
          <span :class="['tag', projectStatus.running ? 'bg-ok-soft text-ok' : 'bg-base text-ink-muted']">
            {{ projectStatus.running ? '运行中' : '已停止' }}
          </span>
        </div>
        <dl class="px-5 py-4 grid grid-cols-1 md:grid-cols-3 gap-x-8 gap-y-2 text-sm">
          <div class="flex justify-between"><dt class="text-ink-muted">进程 PID</dt><dd class="mono text-xs text-ink">{{ projectStatus.pid || '—' }}</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">监听端口</dt><dd class="mono text-xs text-ink">{{ projectStatus.port || '—' }}</dd></div>
          <div class="flex justify-between">
            <dt class="text-ink-muted">状态</dt>
            <dd :class="projectStatus.running ? 'text-ok' : 'text-ink-secondary'" class="font-medium">{{ projectStatus.status }}</dd>
          </div>
        </dl>
        <div class="px-5 py-4 border-t border-line flex flex-wrap gap-2">
          <button
            @click="handleStartProject"
            :disabled="projectActionLoading !== '' || projectStatus.running"
            class="btn-primary"
          >{{ projectActionLoading === 'start' ? '启动中…' : '启动' }}</button>
          <button
            @click="handleStopProject"
            :disabled="projectActionLoading !== '' || !projectStatus.running"
            class="btn-secondary text-danger border-danger/30 hover:bg-danger-soft"
          >{{ projectActionLoading === 'stop' ? '停止中…' : '停止' }}</button>
          <button
            @click="handleRestartProject"
            :disabled="projectActionLoading !== ''"
            class="btn-secondary"
          >{{ projectActionLoading === 'restart' ? '重启中…' : '重启' }}</button>
          <button
            @click="loadProjectStatus"
            :disabled="projectActionLoading !== ''"
            class="btn-ghost"
          >刷新状态</button>
        </div>
      </section>

      <div v-if="showCreateRequirement" class="fixed inset-0 bg-ink/40 flex items-center justify-center z-50 px-4">
        <div class="card w-full max-w-md p-6">
          <h3 class="text-sm font-medium text-ink">新建需求</h3>
          <div class="mt-4 space-y-4">
            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1.5">需求标题</label>
              <input
                v-model="newRequirement.title"
                type="text"
                class="input"
                placeholder="请输入需求标题"
              />
            </div>
            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1.5">需求描述</label>
              <textarea
                v-model="newRequirement.description"
                rows="4"
                class="input resize-none"
                placeholder="请详细描述需求…"
              ></textarea>
            </div>
          </div>
          <div class="mt-6 flex gap-2 justify-end">
            <button @click="showCreateRequirement = false" class="btn-secondary">取消</button>
            <button @click="createRequirement" class="btn-primary">创建</button>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { projectApi, requirementApi, pipelineApi } from '../api'
import Loading from '../components/Loading.vue'

const router = useRouter()
const route = useRoute()
const showToast = inject('showToast')

const loading = ref(true)
const project = ref({
  id: null,
  projectName: '',
  description: '',
  projectType: '',
  database: '',
  updatedAt: ''
})

const stats = ref({
  total: 0,
  completed: 0,
  inProgress: 0,
  pending: 0
})

const currentFilter = ref('all')
const requirements = ref([])
const showCreateRequirement = ref(false)
const newRequirement = ref({
  title: '',
  description: ''
})
const startingPipelineId = ref(null)

const projectStatus = ref({ status: 'UNKNOWN', running: false, pid: null, port: null })
const projectActionLoading = ref('')
const pollingTimer = ref(null)

const statusText = (status) => {
  switch (status) {
    case 'completed': return '已完成'
    case 'in_progress': return '执行中'
    case 'pending': return '待处理'
    default: return status
  }
}

const statusClass = (status) => {
  switch (status) {
    case 'completed': return 'bg-ok-soft text-ok'
    case 'in_progress': return 'bg-warn-soft text-warn'
    case 'pending': return 'bg-base text-ink-secondary'
    default: return 'bg-base text-ink-secondary'
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

const loadProjectDetail = async () => {
  try {
    const projectId = route.params.id
    const [projectRes, reqStatsRes, reqRes] = await Promise.all([
      projectApi.getProject(projectId),
      requirementApi.getProjectRequirementStats(projectId),
      requirementApi.getRequirementsByProject(projectId)
    ])

    project.value = projectRes.data
    project.value.name = projectRes.data.projectName

    stats.value = reqStatsRes.data

    if (currentFilter.value === 'all') {
      requirements.value = reqRes.data
    } else {
      requirements.value = reqRes.data.filter(r => r.status === currentFilter.value)
    }
  } catch (error) {
    console.error('Failed to load project detail:', error)
  } finally {
    loading.value = false
  }
}

const loadRequirements = async () => {
  try {
    const projectId = route.params.id
    if (currentFilter.value === 'all') {
      const res = await requirementApi.getRequirementsByProject(projectId)
      requirements.value = res.data
    } else {
      const res = await requirementApi.getRequirementsByStatus(projectId, currentFilter.value)
      requirements.value = res.data
    }
  } catch (error) {
    console.error('Failed to load requirements:', error)
  }
}

const createRequirement = async () => {
  if (!newRequirement.value.title) {
    showToast?.warning('请输入需求标题')
    return
  }

  try {
    await requirementApi.createRequirement({
      projectId: route.params.id,
      title: newRequirement.value.title,
      description: newRequirement.value.description
    })
    showCreateRequirement.value = false
    newRequirement.value = { title: '', description: '' }
    await loadProjectDetail()
    showToast?.success('需求创建成功')
  } catch (error) {
    console.error('Failed to create requirement:', error)
    showToast?.error('创建失败，请重试')
  }
}

const goBack = () => {
  router.push('/projects')
}

const viewRequirement = (req) => {
  router.push(`/pipelines/${req.pipelineId || 'demo'}`)
}

const startPipeline = async (req) => {
  try {
    startingPipelineId.value = req.id

    const projectId = route.params.id
    const projectInfo = await projectApi.getProject(projectId)

    const res = await pipelineApi.startPipeline({
      requirementMd: '# ' + req.title + '\n\n' + (req.description || ''),
      projectId: projectId,
      projectLocalPath: projectInfo.data.localPath
    })

    const pipelineId = res.data.pipelineId

    await requirementApi.updateRequirement(req.id, {
      pipelineId: pipelineId,
      status: 'in_progress'
    })

    router.push(`/pipelines/${pipelineId}`)
  } catch (error) {
    console.error('Failed to start pipeline:', error)
    showToast?.error('启动失败，请重试')
  } finally {
    startingPipelineId.value = null
  }
}

watch(currentFilter, () => {
  loadRequirements()
})

const loadProjectStatus = async () => {
  try {
    const res = await projectApi.getProjectStatus(route.params.id)
    projectStatus.value = res.data
    if (projectStatus.value.running && !pollingTimer.value) {
      pollingTimer.value = setInterval(loadProjectStatus, 5000)
    } else if (!projectStatus.value.running && pollingTimer.value) {
      clearInterval(pollingTimer.value)
      pollingTimer.value = null
    }
  } catch (e) {
    projectStatus.value = { status: 'UNKNOWN', running: false }
  }
}

const handleStartProject = async () => {
  projectActionLoading.value = 'start'
  try {
    const res = await projectApi.startProject(route.params.id)
    if (res.data.success) {
      showToast?.success('项目启动中，端口: ' + res.data.port)
      await loadProjectStatus()
    } else {
      showToast?.warning(res.data.message || '启动失败')
    }
  } catch (e) {
    console.error('Failed to start project:', e)
    showToast?.error('启动失败，请重试')
  } finally {
    projectActionLoading.value = ''
  }
}

const handleStopProject = async () => {
  projectActionLoading.value = 'stop'
  try {
    const res = await projectApi.stopProject(route.params.id)
    if (res.data.success) {
      showToast?.success('项目已停止')
      await loadProjectStatus()
    } else {
      showToast?.warning(res.data.message || '停止失败')
    }
  } catch (e) {
    console.error('Failed to stop project:', e)
    showToast?.error('停止失败，请重试')
  } finally {
    projectActionLoading.value = ''
  }
}

const handleRestartProject = async () => {
  projectActionLoading.value = 'restart'
  try {
    const res = await projectApi.restartProject(route.params.id)
    if (res.data.success) {
      showToast?.success('项目重启中，端口: ' + res.data.port)
      await loadProjectStatus()
    } else {
      showToast?.warning(res.data.message || '重启失败')
    }
  } catch (e) {
    console.error('Failed to restart project:', e)
    showToast?.error('重启失败，请重试')
  } finally {
    projectActionLoading.value = ''
  }
}

onMounted(() => {
  loadProjectDetail()
  loadProjectStatus()
})

onUnmounted(() => {
  if (pollingTimer.value) {
    clearInterval(pollingTimer.value)
    pollingTimer.value = null
  }
})
</script>
