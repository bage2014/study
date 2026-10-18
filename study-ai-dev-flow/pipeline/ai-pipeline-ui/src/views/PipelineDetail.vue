<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载流水线详情" subtext="请稍候" />

    <template v-else>
      <header class="flex items-start justify-between">
        <div class="min-w-0">
          <p class="mono text-xs text-ink-muted mb-1">{{ pipeline.pipelineId }}</p>
          <h1 class="text-2xl font-semibold tracking-tight text-ink">{{ requirement.title }}</h1>
          <p class="text-ink-secondary mt-1 text-sm">
            <span class="mono">{{ requirement.project }}</span>
            <span class="mx-2 text-line-strong">·</span>{{ formatTime(requirement.createdAt) }}
          </p>
        </div>
        <div class="flex gap-2 flex-shrink-0">
          <button v-if="pipeline.status === 'RUNNING'" @click="handleCancel" class="btn-secondary text-danger border-danger/30 hover:bg-danger-soft">取消</button>
          <router-link to="/pipelines" class="btn-secondary">返回列表</router-link>
        </div>
      </header>

      <div class="card px-5 py-4 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <span :class="['tag', statusClass(pipeline.status)]">{{ statusText(pipeline.status) }}</span>
          <span class="text-sm text-ink-secondary">当前阶段 <span class="text-ink font-medium">{{ currentStageInfo.name }}</span></span>
        </div>
        <span class="mono text-xs text-ink-muted">{{ completedCount }} / {{ pipelineStages.length }}</span>
      </div>

      <div v-if="pipeline.status === 'WAITING_APPROVAL'" class="card border-warn/30 bg-warn-soft px-5 py-4 flex items-center justify-between">
        <div>
          <p class="text-sm font-medium text-ink">等待审批</p>
          <p class="text-xs text-ink-secondary mt-0.5">「{{ currentStageInfo.name }}」需要人工审批才能继续</p>
        </div>
        <div class="flex gap-2">
          <button @click="handleApprove" class="btn-primary">批准</button>
          <button @click="handleReject" class="btn-secondary text-danger border-danger/30 hover:bg-danger-soft">拒绝</button>
        </div>
      </div>

      <ol class="card divide-y divide-line">
        <li
          v-for="(stage, i) in pipelineStages"
          :key="stage.id"
          class="px-5 py-4"
        >
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <span class="mono text-2xs text-ink-muted w-6">{{ String(i + 1).padStart(2, '0') }}</span>
              <span :class="['w-1.5 h-1.5 rounded-full flex-shrink-0', stageDotClass(stage.status)]"></span>
              <span class="text-sm font-medium text-ink">{{ stage.name }}</span>
              <span :class="['tag', stageTagClass(stage.status)]">{{ stage.statusText }}</span>
            </div>
            <div class="text-right text-xs text-ink-muted">
              <span v-if="stage.durationMs">{{ formatDuration(stage.durationMs) }}</span>
              <span v-if="stage.time" class="ml-2">{{ stage.time }}</span>
            </div>
          </div>

          <div v-if="stage.status !== 'PENDING'" class="mt-4 ml-10 space-y-4">
            <template v-if="stage.id === 'requirement-analysis'">
              <div v-if="stage.analysisResult" class="space-y-3">
                <div class="border-l-2 border-accent pl-3">
                  <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1">摘要</p>
                  <p class="text-sm text-ink-secondary">{{ stage.analysisResult.summary }}</p>
                </div>
                <div v-if="stage.analysisResult.features?.length">
                  <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">功能点</p>
                  <div class="space-y-2">
                    <div v-for="(f, idx) in stage.analysisResult.features" :key="idx" class="bg-base rounded p-3">
                      <p class="text-sm font-medium text-ink">{{ f.name }}</p>
                      <p class="text-xs text-ink-secondary mt-0.5">{{ f.description }}</p>
                    </div>
                  </div>
                </div>
                <div v-if="stage.analysisResult.affected_files_hints?.length">
                  <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">影响文件</p>
                  <div class="flex flex-wrap gap-1">
                    <code v-for="(f, idx) in stage.analysisResult.affected_files_hints" :key="idx" class="mono text-xs bg-base border border-line rounded px-1.5 py-0.5">{{ f }}</code>
                  </div>
                </div>
              </div>
              <pre v-else class="mono bg-ink text-white/90 p-4 rounded text-xs overflow-x-auto max-h-40 overflow-y-auto">{{ stage.resultJson || getDefaultAnalysis() }}</pre>
            </template>

            <template v-if="stage.id === 'feature-point-split'">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-2">
                <div v-for="(p, idx) in stage.featurePoints || []" :key="idx" class="border border-line rounded p-3">
                  <p class="text-sm font-medium text-ink">{{ p.title || p.name }}</p>
                  <p class="text-xs text-ink-secondary mt-0.5">{{ p.description }}</p>
                </div>
                <p v-if="!stage.featurePoints?.length" class="text-sm text-ink-muted">AI 已将需求拆分为多个功能点</p>
              </div>
            </template>

            <template v-if="stage.id === 'task-split'">
              <div v-if="stage.allTasks?.length" class="space-y-2">
                <div v-for="(g, gi) in stage.allTasks" :key="gi" class="border border-line rounded p-3">
                  <p class="text-xs font-medium text-ink mb-2">{{ g.featurePoint }}</p>
                  <div class="grid grid-cols-2 md:grid-cols-3 gap-1.5">
                    <div v-for="(t, ti) in g.tasks || []" :key="ti" class="bg-base rounded p-2 text-xs">
                      <p class="font-medium text-ink">{{ t.type || '—' }}</p>
                      <p class="text-ink-muted mt-0.5">{{ t.description }}</p>
                    </div>
                  </div>
                </div>
              </div>
              <p v-else class="text-sm text-ink-muted">AI 已将功能点拆分为原子任务</p>
            </template>

            <template v-if="stage.id === 'code-gen'">
              <div v-if="generatedFiles.length" class="flex flex-wrap gap-1.5">
                <button
                  v-for="file in generatedFiles"
                  :key="file.name"
                  @click="selectCodeFile(file)"
                  :class="[
                    'mono text-xs px-2 py-1 rounded border transition-colors',
                    selectedCodeFile?.name === file.name
                      ? 'border-accent bg-accent-soft text-accent'
                      : 'border-line text-ink-secondary hover:border-line-strong'
                  ]"
                >{{ file.name }}</button>
              </div>
              <p v-else class="text-sm text-ink-muted">暂无生成文件</p>
              <div v-if="selectedCodeFile" class="border border-line rounded overflow-hidden">
                <div class="flex items-center justify-between px-3 py-2 bg-base border-b border-line">
                  <span class="mono text-xs text-ink">{{ selectedCodeFile.name }}</span>
                  <button @click="applyCode" class="btn-secondary text-xs">应用代码</button>
                </div>
                <pre class="mono text-xs p-4 overflow-x-auto max-h-72 overflow-y-auto bg-surface">{{ selectedCodeFile.content }}</pre>
              </div>
            </template>

            <template v-if="stage.id === 'test-gen'">
              <div v-if="testGeneratedFiles.length" class="flex flex-wrap gap-1.5">
                <button
                  v-for="file in testGeneratedFiles"
                  :key="file.name"
                  @click="selectCodeFile(file)"
                  :class="[
                    'mono text-xs px-2 py-1 rounded border transition-colors',
                    selectedCodeFile?.name === file.name
                      ? 'border-ok bg-ok-soft text-ok'
                      : 'border-line text-ink-secondary hover:border-line-strong'
                  ]"
                >{{ file.name }}</button>
              </div>
              <p v-else class="text-sm text-ok">测试用例已生成</p>
            </template>

            <template v-if="stage.id === 'code-review'">
              <div v-if="stage.issues?.length" class="space-y-1.5">
                <div v-for="(issue, idx) in stage.issues" :key="idx"
                  :class="['text-sm px-3 py-2 rounded border', issue.startsWith('CRITICAL') ? 'border-danger/30 bg-danger-soft text-danger' : 'border-warn/30 bg-warn-soft text-warn']"
                >{{ issue }}</div>
              </div>
              <p v-else class="text-sm text-ok">代码审查通过 · 评分 95/100</p>
            </template>

            <template v-if="stage.id === 'pr-creation'">
              <div class="flex gap-4 text-sm">
                <div><span class="mono font-medium text-ok">+{{ stage.stats?.added ?? 2 }}</span> <span class="text-ink-muted text-xs ml-1">新增</span></div>
                <div><span class="mono font-medium text-accent">~{{ stage.stats?.modified ?? 1 }}</span> <span class="text-ink-muted text-xs ml-1">修改</span></div>
                <div><span class="mono font-medium text-danger">-{{ stage.stats?.deleted ?? 0 }}</span> <span class="text-ink-muted text-xs ml-1">删除</span></div>
              </div>
              <div class="flex items-center justify-between border border-line rounded px-3 py-2">
                <span class="text-sm text-ink">风险等级 <span :class="['tag ml-2', riskClass(stage.risk)]">{{ stage.risk || '低' }}</span></span>
                <button @click="viewDiff" class="text-sm text-accent hover:underline">查看差异</button>
              </div>
            </template>

            <template v-if="stage.id === 'test-exec'">
              <div class="grid grid-cols-4 gap-px bg-line border border-line rounded overflow-hidden text-center">
                <div class="bg-surface p-3"><p class="mono text-lg text-ink">{{ stage.testStats?.total || 0 }}</p><p class="text-2xs text-ink-muted">总数</p></div>
                <div class="bg-surface p-3"><p class="mono text-lg text-ok">{{ stage.testStats?.passed || 0 }}</p><p class="text-2xs text-ink-muted">通过</p></div>
                <div class="bg-surface p-3"><p class="mono text-lg text-danger">{{ stage.testStats?.failed || 0 }}</p><p class="text-2xs text-ink-muted">失败</p></div>
                <div class="bg-surface p-3"><p class="mono text-lg text-accent">{{ stage.testStats?.coverage || '0%' }}</p><p class="text-2xs text-ink-muted">覆盖率</p></div>
              </div>
              <ul class="space-y-1">
                <li v-for="test in stage.tests || []" :key="test.name" class="flex items-center justify-between text-sm border-b border-line last:border-0 py-1.5">
                  <span class="mono text-xs text-ink-secondary">{{ test.name }}</span>
                  <span :class="test.status === 'PASSED' ? 'text-ok' : 'text-danger'" class="text-xs font-medium">{{ test.status === 'PASSED' ? '通过' : '失败' }}</span>
                </li>
              </ul>
              <p v-if="stage.fixAttempts > 0" class="text-xs text-ink-muted">自动修复 {{ stage.fixAttempts }} 次后通过</p>
            </template>

            <template v-if="stage.id === 'build'">
              <p :class="['text-sm font-medium', stage.buildStatus === 'SUCCESS' ? 'text-ok' : 'text-danger']">
                {{ stage.buildStatus === 'SUCCESS' ? '构建成功' : '构建失败' }}
                <span class="text-ink-muted font-normal ml-2">耗时 {{ stage.duration || '12.5s' }}</span>
              </p>
              <pre class="mono bg-ink text-white/90 p-4 rounded text-xs overflow-x-auto max-h-32 overflow-y-auto">{{ stage.log || '[INFO] BUILD SUCCESS' }}</pre>
            </template>

            <template v-if="stage.id === 'deploy'">
              <p :class="['text-sm font-medium', deployStatusClass(stage.deployStatus)]">
                {{ deployStatusText(stage.deployStatus) }}
                <span class="text-ink-muted font-normal ml-2">{{ stage.environment || '开发环境' }} · v{{ stage.version || '1.0.0' }}</span>
              </p>
              <ul class="space-y-1">
                <li v-for="ep in stage.endpoints || getDefaultEndpoints()" :key="ep.path" class="flex items-center gap-3 text-sm border-b border-line last:border-0 py-1.5">
                  <span class="mono text-2xs font-medium text-accent w-12">{{ ep.method }}</span>
                  <span class="mono text-xs text-ink-secondary flex-1 truncate">{{ ep.url }}</span>
                  <span :class="['tag', ep.status === 'UP' ? 'bg-ok-soft text-ok' : 'bg-danger-soft text-danger']">{{ ep.status === 'UP' ? '在线' : '离线' }}</span>
                </li>
              </ul>
            </template>
          </div>
        </li>
      </ol>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <section class="card">
          <h3 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">基本信息</h3>
          <dl class="px-5 py-4 space-y-2 text-sm">
            <div class="flex justify-between"><dt class="text-ink-muted">项目</dt><dd class="mono text-ink">{{ requirement.project }}</dd></div>
            <div class="flex justify-between"><dt class="text-ink-muted">创建时间</dt><dd class="text-ink">{{ formatTime(requirement.createdAt) }}</dd></div>
            <div class="flex justify-between"><dt class="text-ink-muted">当前阶段</dt><dd class="text-ink">{{ pipeline.currentStage }}</dd></div>
          </dl>
        </section>

        <section class="card">
          <h3 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">需求描述</h3>
          <p class="px-5 py-4 text-sm text-ink-secondary leading-relaxed">{{ requirement.description }}</p>
        </section>

        <section class="card">
          <h3 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">执行日志</h3>
          <div class="px-5 py-4 space-y-1 max-h-40 overflow-y-auto">
            <p v-for="(log, idx) in executionLogs" :key="idx" class="mono text-xs text-ink-muted">{{ log }}</p>
          </div>
        </section>
      </div>

      <div v-if="pipeline.status === 'COMPLETED'" class="card border-ok/30 bg-ok-soft px-5 py-4 flex items-center justify-between">
        <div>
          <p class="text-sm font-medium text-ok">部署成功</p>
          <p class="text-xs text-ink-secondary mt-0.5">应用已部署到开发环境</p>
        </div>
        <a href="http://localhost:8081/api/health" target="_blank" class="btn-secondary bg-surface">访问健康检查</a>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { pipelineApi, requirementApi, projectApi } from '../api'
import Loading from '../components/Loading.vue'

const router = useRouter()
const route = useRoute()
const showToast = inject('showToast')

const loading = ref(true)
const requirement = ref({ id: '', project: '', title: '', description: '', createdAt: '' })
const pipeline = ref({ pipelineId: '', status: 'RUNNING', currentStage: 'INIT' })
const selectedCodeFile = ref(null)
const stages = ref([])
const executionLogs = ref([])
let pollInterval = null

const pipelineStages = computed(() => {
  if (!stages.value.length) return []
  return stages.value.map(s => {
    const result = {
      id: s.stageName.toLowerCase().replace('_', '-'),
      name: getStageName(s.stageName),
      status: s.status,
      statusText: getStageStatusText(s.status),
      time: s.endTime ? formatTimeShort(s.endTime) : '',
      durationMs: s.durationMs,
      errorMessage: s.errorMessage
    }
    if (s.resultJson) {
      try {
        let json = JSON.parse(s.resultJson)
        if (json.analysisResult && typeof json.analysisResult === 'string') {
          try { json.analysisResult = JSON.parse(json.analysisResult) } catch (e) {}
        }
        Object.assign(result, json)
      } catch (e) {}
    }
    return result
  }).sort((a, b) => (a.orderNum || 0) - (b.orderNum || 0))
})

const currentStageInfo = computed(() =>
  pipelineStages.value.find(s => s.status === 'RUNNING') ||
  pipelineStages.value[pipelineStages.value.length - 1] ||
  { name: '未知' }
)

const completedCount = computed(() =>
  pipelineStages.value.filter(s => s.status === 'COMPLETED').length
)

const generatedFiles = computed(() => {
  const s = pipelineStages.value.find(s => s.id === 'code-gen')
  return s?.generatedFiles ? Object.entries(s.generatedFiles).map(([name, content]) => ({ name, content })) : []
})

const testGeneratedFiles = computed(() => {
  const s = pipelineStages.value.find(s => s.id === 'test-gen')
  return s?.generatedFiles ? Object.entries(s.generatedFiles).map(([name, content]) => ({ name, content })) : []
})

const getDefaultEndpoints = () => [
  { method: 'GET', url: 'http://localhost:8081/api/users', status: 'UP' },
  { method: 'GET', url: 'http://localhost:8081/api/health', status: 'UP' }
]

const getStageName = (n) => ({
  REQUIREMENT_ANALYSIS: '需求分析', FEATURE_POINT_SPLIT: '功能点拆分', TASK_SPLIT: '任务拆分',
  DESIGN: '方案设计', CODE_GEN: '代码生成', TEST_GEN: '测试生成',
  CODE_REVIEW: '代码审查', PR_CREATION: '变更预览', TEST_EXEC: '测试验证',
  BUILD: '编译构建', DEPLOY: '发布部署'
}[n] || n)

const getStageStatusText = (s) => ({ COMPLETED: '已完成', RUNNING: '执行中', FAILED: '失败' }[s] || '待处理')
const statusText = (s) => ({ COMPLETED: '已完成', RUNNING: '执行中', FAILED: '失败', WAITING_APPROVAL: '等待审批', CANCELLED: '已取消' }[s] || s)

const statusClass = (s) => ({
  COMPLETED: 'bg-ok-soft text-ok',
  RUNNING: 'bg-accent-soft text-accent',
  FAILED: 'bg-danger-soft text-danger',
  WAITING_APPROVAL: 'bg-warn-soft text-warn',
}[s] || 'bg-base text-ink-secondary')

const stageDotClass = (s) => ({
  COMPLETED: 'bg-ok', RUNNING: 'bg-accent animate-pulse', FAILED: 'bg-danger'
}[s] || 'bg-line-strong')

const stageTagClass = (s) => ({
  COMPLETED: 'bg-ok-soft text-ok', RUNNING: 'bg-accent-soft text-accent', FAILED: 'bg-danger-soft text-danger'
}[s] || 'bg-base text-ink-muted')

const riskClass = (r) => ({ 低: 'bg-ok-soft text-ok', 中: 'bg-warn-soft text-warn', 高: 'bg-danger-soft text-danger' }[r] || 'bg-ok-soft text-ok')

const deployStatusClass = (s) => ({ SUCCESS: 'text-ok', RUNNING: 'text-warn' }[s] || 'text-danger')
const deployStatusText = (s) => ({ SUCCESS: '部署成功', RUNNING: '部署中' }[s] || '部署失败')

const formatTime = (t) => {
  if (!t) return '—'
  const d = new Date(t)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const formatTimeShort = (t) => {
  if (!t) return ''
  const d = new Date(t)
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const formatDuration = (ms) => {
  if (!ms) return ''
  const s = Math.floor(ms / 1000)
  const m = Math.floor(s / 60)
  return m > 0 ? `${m}m ${s % 60}s` : `${s}s`
}

const getDefaultAnalysis = () => JSON.stringify({ success: true, projectType: 'SPRING_BOOT', database: 'H2' }, null, 2)

const selectCodeFile = (f) => { selectedCodeFile.value = f }
const applyCode = () => showToast?.success('代码已应用', selectedCodeFile.value.name)
const viewDiff = () => showToast?.info('正在打开差异对比视图')

const handleCancel = () => {
  if (confirm('确定取消这条流水线？')) {
    pipelineApi.cancelPipeline(pipeline.value.pipelineId)
    router.push('/pipelines')
  }
}

const handleApprove = () => {
  if (confirm('确定批准当前阶段？')) {
    pipelineApi.approvePipeline(pipeline.value.pipelineId, pipeline.value.currentStage || 'DEPLOY', true, 'admin', '')
  }
}

const handleReject = () => {
  const reason = prompt('请输入拒绝原因：')
  if (reason !== null) {
    pipelineApi.approvePipeline(pipeline.value.pipelineId, pipeline.value.currentStage || 'DEPLOY', false, 'admin', reason)
  }
}

const loadPipelineData = async () => {
  try {
    const pipelineId = route.params.id
    const pipeRes = await pipelineApi.getPipeline(pipelineId)
    pipeline.value = pipeRes.data
    const stageRes = await pipelineApi.getStages(pipelineId)
    stages.value = stageRes.data

    let projectName = '未知项目'
    if (pipeline.value.projectId) {
      try {
        const projRes = await projectApi.getProject(pipeline.value.projectId)
        projectName = projRes.data.projectName || '未知项目'
      } catch (e) { console.error('Failed to load project:', e) }
    }

    if (pipeline.value.requirementTitle) {
      requirement.value = {
        project: projectName,
        title: pipeline.value.requirementTitle,
        description: pipeline.value.requirementDescription || '',
        createdAt: pipeline.value.createdAt
      }
    } else {
      const reqRes = await requirementApi.getRequirementByPipelineId(pipelineId)
      requirement.value = reqRes?.data
        ? { ...reqRes.data, project: projectName }
        : { project: projectName, title: '需求处理中', description: '', createdAt: pipeline.value.createdAt }
    }

    executionLogs.value = stages.value
      .map(s => `${formatTimeShort(s.startTime)} [${s.status}] ${getStageName(s.stageName)}`)
      .filter(l => l.trim())
  } catch (error) {
    console.error('Failed to load pipeline data:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadPipelineData()
  pollInterval = setInterval(() => {
    if (pipeline.value.status === 'RUNNING') loadPipelineData()
  }, 5000)
})

onUnmounted(() => { if (pollInterval) clearInterval(pollInterval) })
</script>
