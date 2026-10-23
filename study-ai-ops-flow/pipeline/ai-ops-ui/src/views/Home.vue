<template>
  <div class="space-y-8">
    <Loading v-if="loading" text="加载项目列表" subtext="请稍候" />

    <template v-else>
      <section>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">录入需求</h1>
        <p class="text-ink-secondary mt-1 max-w-2xl">
          选择项目并描述需求。AI 将分析现有代码，完成从方案设计到代码实现的全过程。
        </p>
      </section>

      <section class="card">
        <div class="px-5 py-4 border-b border-line flex items-center justify-between">
          <h2 class="font-medium text-ink">项目与需求</h2>
          <span v-if="selectedProject" class="tag bg-accent-soft text-accent">
            {{ selectedProject.name }}
          </span>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 divide-y lg:divide-y-0 lg:divide-x divide-line">
          <div class="p-5">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-3">项目</p>
            <ul class="space-y-1">
              <li
                v-for="project in projects"
                :key="project.id"
                @click="selectProject(project)"
                :class="[
                  'flex items-center gap-3 px-3 py-2.5 rounded cursor-pointer transition-colors border',
                  selectedProject?.id === project.id
                    ? 'border-accent bg-accent-soft'
                    : 'border-transparent hover:bg-base'
                ]"
              >
                <span :class="['w-1.5 h-1.5 rounded-full flex-shrink-0', selectedProject?.id === project.id ? 'bg-accent' : 'bg-line-strong']"></span>
                <div class="flex-1 min-w-0">
                  <p class="text-sm font-medium text-ink truncate">{{ project.name }}</p>
                  <p class="text-xs text-ink-muted truncate">{{ project.description }}</p>
                </div>
              </li>
            </ul>
          </div>

          <div class="lg:col-span-2 p-5 space-y-4">
            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">需求标题</label>
              <input
                v-model="form.title"
                :disabled="!selectedProject"
                class="input"
                :placeholder="selectedProject ? '例如：用户注册功能' : '请先选择项目'"
              />
            </div>

            <div>
              <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">需求描述</label>
              <textarea
                v-model="form.requirement"
                rows="7"
                :disabled="!selectedProject"
                class="input resize-none"
                :placeholder="selectedProject ? '详细描述业务需求，例如：添加用户注册功能，支持手机号验证和密码加密存储' : '请先选择项目'"
              ></textarea>
            </div>

            <div v-if="selectedProject" class="flex items-start gap-2 text-xs text-ink-secondary bg-base border border-line rounded px-3 py-2">
              <span class="text-accent mt-px">›</span>
              <p>AI 将自动分析 <span class="mono text-ink">{{ selectedProject.name }}</span> 的代码结构、技术栈与数据库配置。</p>
            </div>

            <div class="flex justify-end pt-2">
              <button
                @click="submitRequirement"
                :disabled="!selectedProject || !form.title || !form.requirement || isSubmitting"
                class="btn-primary"
              >
                <span v-if="isSubmitting" class="w-3 h-3 border border-white/40 border-t-white rounded-full animate-spin"></span>
                {{ isSubmitting ? '启动中' : '启动流水线' }}
              </button>
            </div>
          </div>
        </div>
      </section>

      <section>
        <h2 class="text-sm font-medium text-ink mb-3">流水线阶段</h2>
        <ol class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-px bg-line border border-line rounded-lg overflow-hidden">
          <li v-for="(stage, i) in stages" :key="stage.name" class="bg-surface p-4">
            <p class="mono text-2xs text-ink-muted">{{ String(i + 1).padStart(2, '0') }}</p>
            <p class="text-sm font-medium text-ink mt-1">{{ stage.name }}</p>
            <p class="text-xs text-ink-secondary mt-1 leading-relaxed">{{ stage.desc }}</p>
          </li>
        </ol>
      </section>

      <section class="card">
        <div class="px-5 py-4 border-b border-line">
          <h2 class="font-medium text-ink">最近需求</h2>
        </div>
        <ul class="divide-y divide-line">
          <li
            v-for="item in recentRequirements"
            :key="item.id"
            @click="viewPipeline(item.id)"
            class="flex items-center justify-between px-5 py-3 hover:bg-base cursor-pointer transition-colors"
          >
            <div class="min-w-0">
              <p class="text-sm text-ink truncate">
                <span class="mono text-ink-muted mr-2">{{ item.projectName }}</span>{{ item.requirementTitle }}
              </p>
              <p class="text-xs text-ink-muted mt-0.5">{{ item.createdAt }}</p>
            </div>
            <span :class="['tag flex-shrink-0 ml-4', statusTagClass(item.status)]">{{ item.status }}</span>
          </li>
        </ul>
      </section>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { projectApi, pipelineApi, requirementApi } from '../api'
import Loading from '../components/Loading.vue'
import { projectStore } from '../stores/projectStore'

const router = useRouter()
const showToast = inject('showToast')

const loading = ref(true)
const projects = ref([])
const selectedProject = ref(projectStore.currentProject || null)
const form = ref({ title: '', requirement: '' })
const isSubmitting = ref(false)

const stages = [
  { name: '代码分析', desc: '扫描项目结构，识别技术栈与数据库配置' },
  { name: '需求分析', desc: '理解业务需求，生成结构化需求文档' },
  { name: '方案设计', desc: '生成技术方案、架构设计与 API 契约' },
  { name: '代码实现', desc: '按方案生成符合项目规范的代码与测试' },
]

const recentRequirements = ref([
  { id: 'a9b79feb', projectName: 'demo-backend', requirementTitle: '添加健康检查端点', status: '已完成', createdAt: '2026-07-11 23:20' },
  { id: 'b8c68dee', projectName: 'demo-backend', requirementTitle: '用户管理功能', status: '执行中', createdAt: '2026-07-11 20:15' },
  { id: 'c7d57cfc', projectName: 'demo-frontend', requirementTitle: '登录页面优化', status: '待处理', createdAt: '2026-07-10 18:30' },
])

const statusTagClass = (status) => {
  switch (status) {
    case '执行中': return 'bg-warn-soft text-warn'
    case '已完成': return 'bg-ok-soft text-ok'
    default: return 'bg-base text-ink-secondary'
  }
}

const selectProject = (project) => {
  selectedProject.value = project
  projectStore.setCurrentProject(project)
}

const submitRequirement = async () => {
  if (!selectedProject.value) return showToast?.warning('请先选择项目')
  if (!form.value.title) return showToast?.warning('请填写需求标题')
  if (!form.value.requirement) return showToast?.warning('请填写需求描述')

  isSubmitting.value = true
  try {
    await requirementApi.createRequirement({
      projectId: selectedProject.value.id,
      title: form.value.title,
      description: form.value.requirement
    })
    const pipeRes = await pipelineApi.startPipeline({
      requirementMd: '# ' + form.value.title + '\n\n' + form.value.requirement,
      projectId: selectedProject.value.id,
      projectLocalPath: selectedProject.value.localPath || '/tmp/demo-backend'
    })
    const pipelineId = pipeRes.data.pipelineId || pipeRes.data.id
    if (!pipelineId) throw new Error('流水线ID未返回')

    form.value.title = ''
    form.value.requirement = ''
    showToast?.success('流水线已启动', pipelineId.substring(0, 8))
    router.push(`/pipelines/${pipelineId}`)
  } catch (error) {
    console.error('Failed to submit requirement:', error)
    showToast?.error(error.message || '提交失败，请重试')
  } finally {
    isSubmitting.value = false
  }
}

const viewPipeline = (id) => router.push(`/pipelines/${id}`)

const loadProjects = async () => {
  try {
    const res = await projectApi.getAllProjects({ page: 0, size: 10 })
    const projectList = res.data.content || res.data
    projects.value = projectList.map(p => ({
      id: p.id,
      name: p.projectName,
      description: p.description,
      localPath: p.localPath
    }))
    if (projects.value.length === 0) throw new Error('empty')
  } catch (error) {
    console.error('Failed to load projects:', error)
    projects.value = [
      { id: 1, name: 'demo-backend', description: 'Spring Boot 后端服务', localPath: '/Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-backend' },
      { id: 2, name: 'demo-frontend', description: 'Vue 3 前端应用', localPath: '/Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-frontend' },
    ]
  } finally {
    if (!selectedProject.value && projects.value.length > 0) {
      selectedProject.value = projects.value[0]
      projectStore.setCurrentProject(projects.value[0])
    }
    loading.value = false
  }
}

onMounted(loadProjects)
</script>
