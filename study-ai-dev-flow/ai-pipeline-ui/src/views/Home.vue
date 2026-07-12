<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载项目列表中..." subtext="请稍候" />
    
    <template v-else>
      <div class="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-xl shadow-lg p-8 text-white">
        <div class="flex items-center justify-between">
          <div>
            <h2 class="text-2xl font-bold mb-2">智能开发流水线</h2>
            <p class="text-indigo-100">选择项目并输入需求，AI 将自动分析代码逻辑并完成从方案设计到代码实现的全过程</p>
          </div>
          <div class="w-20 h-20 rounded-full bg-white/20 flex items-center justify-center">
            <span class="text-4xl">🤖</span>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <div class="flex items-center justify-between mb-6">
          <h3 class="font-semibold text-gray-800">选择项目 & 输入需求</h3>
          <div v-if="selectedProject" class="flex items-center space-x-2 text-sm">
            <span class="text-gray-500">已选择:</span>
            <span class="bg-indigo-100 text-indigo-700 px-3 py-1 rounded-full font-medium">
              {{ selectedProject.name }}
            </span>
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div class="lg:col-span-1">
            <label class="block text-sm font-medium text-gray-700 mb-3">选择项目</label>
            <div class="space-y-2">
              <div 
                v-for="project in projects" 
                :key="project.id"
                @click="selectProject(project)"
                :class="[
                  'flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all',
                  selectedProject?.id === project.id 
                    ? 'border-indigo-500 bg-indigo-50' 
                    : 'border-gray-200 hover:border-indigo-300 hover:bg-gray-50'
                ]"
              >
                <div :class="['w-10 h-10 rounded-lg flex items-center justify-center text-xl', project.iconBgClass]">
                  {{ project.icon }}
                </div>
                <div class="ml-3 flex-1">
                  <p class="text-sm font-medium text-gray-800">{{ project.name }}</p>
                  <p class="text-xs text-gray-500">{{ project.description }}</p>
                </div>
                <div v-if="selectedProject?.id === project.id" class="text-indigo-600">
                  <span>✓</span>
                </div>
              </div>
            </div>
          </div>

          <div class="lg:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-3">业务需求描述</label>
            <div class="bg-gray-50 rounded-lg p-4 border-2 transition-colors" :class="selectedProject ? 'border-indigo-200' : 'border-gray-200'">
              <textarea 
                v-model="form.requirement"
                rows="6"
                :disabled="!selectedProject"
                :class="[
                  'w-full bg-transparent resize-none focus:outline-none',
                  selectedProject ? 'text-gray-700' : 'text-gray-400'
                ]"
                :placeholder="selectedProject ? '请详细描述您的业务需求，例如：\n添加用户注册功能，支持手机号验证和密码加密存储...' : '请先选择一个项目'"
              ></textarea>
            </div>
            
            <div v-if="selectedProject" class="mt-4 bg-blue-50 rounded-lg p-3">
              <div class="flex items-center space-x-2">
                <span class="text-blue-600 text-lg">🔍</span>
                <div>
                  <p class="text-sm font-medium text-blue-800">AI 代码分析</p>
                  <p class="text-xs text-blue-600">AI 将自动分析项目 {{ selectedProject.name }} 的现有代码结构，识别技术栈和数据库配置，无需手动选择。</p>
                </div>
              </div>
            </div>

            <div class="mt-4 flex justify-end">
              <button 
                @click="submitRequirement"
                :disabled="!selectedProject || !form.requirement || isSubmitting"
                :class="[
                  'px-6 py-3 rounded-lg font-medium transition-all flex items-center space-x-2',
                  selectedProject && form.requirement && !isSubmitting
                    ? 'bg-indigo-600 text-white hover:bg-indigo-700 shadow-md hover:shadow-lg'
                    : 'bg-gray-200 text-gray-400 cursor-not-allowed'
                ]"
              >
                <span v-if="isSubmitting">⏳</span>
                <span>{{ isSubmitting ? 'AI 正在分析并启动流水线...' : '提交需求，启动AI流水线' }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 rounded-lg bg-indigo-100 flex items-center justify-center">
              <span class="text-indigo-600 text-xl">🔍</span>
            </div>
            <div>
              <h4 class="font-medium text-gray-800">代码分析</h4>
              <p class="text-xs text-gray-500 mt-1">AI 自动分析项目代码结构，识别技术栈、框架、数据库配置。</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 rounded-lg bg-blue-100 flex items-center justify-center">
              <span class="text-blue-600 text-xl">📋</span>
            </div>
            <div>
              <h4 class="font-medium text-gray-800">需求分析</h4>
              <p class="text-xs text-gray-500 mt-1">基于代码分析结果，AI 理解业务需求并生成结构化需求文档。</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 rounded-lg bg-purple-100 flex items-center justify-center">
              <span class="text-purple-600 text-xl">📐</span>
            </div>
            <div>
              <h4 class="font-medium text-gray-800">方案设计</h4>
              <p class="text-xs text-gray-500 mt-1">AI 自动生成技术方案、架构设计和 API 设计，与现有代码无缝集成。</p>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 rounded-lg bg-green-100 flex items-center justify-center">
              <span class="text-green-600 text-xl">💻</span>
            </div>
            <div>
              <h4 class="font-medium text-gray-800">代码实现</h4>
              <p class="text-xs text-gray-500 mt-1">根据方案设计，AI 生成符合项目规范的代码实现和测试用例。</p>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-sm p-6">
        <h4 class="font-semibold text-gray-800 mb-4">最近提交的需求</h4>
        <div class="space-y-3">
          <div 
            v-for="item in recentRequirements" 
            :key="item.id"
            @click="viewPipeline(item.id)"
            class="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:border-indigo-300 hover:bg-gray-50 transition-all cursor-pointer"
          >
            <div>
              <p class="text-sm font-medium text-gray-800">{{ item.projectName }} - {{ item.requirementTitle }}</p>
              <p class="text-xs text-gray-500 mt-1">{{ item.createdAt }}</p>
            </div>
            <span :class="[
              'text-xs px-3 py-1.5 rounded-full font-medium',
              item.status === '执行中' ? 'bg-yellow-100 text-yellow-700' :
              item.status === '已完成' ? 'bg-green-100 text-green-700' :
              'bg-gray-100 text-gray-700'
            ]">
              {{ item.status }}
            </span>
          </div>
        </div>
      </div>
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
const form = ref({
  requirement: ''
})
const isSubmitting = ref(false)

const recentRequirements = ref([
  { id: 'a9b79feb', projectName: 'demo-backend', requirementTitle: '添加健康检查端点', status: '已完成', createdAt: '2026-07-11 23:20' },
  { id: 'b8c68dee', projectName: 'demo-backend', requirementTitle: '用户管理功能', status: '执行中', createdAt: '2026-07-11 20:15' },
  { id: 'c7d57cfc', projectName: 'demo-frontend', requirementTitle: '登录页面优化', status: '待处理', createdAt: '2026-07-10 18:30' },
])

const selectProject = (project) => {
  selectedProject.value = project
  projectStore.setCurrentProject(project)
}

const submitRequirement = async () => {
  if (!selectedProject.value) {
    showToast?.warning('请先选择一个项目')
    return
  }
  
  if (!form.value.requirement) {
    showToast?.warning('请填写需求描述')
    return
  }
  
  isSubmitting.value = true
  
  try {
    const reqRes = await requirementApi.createRequirement({
      projectId: selectedProject.value.id,
      title: form.value.requirement.substring(0, 100),
      description: form.value.requirement
    })
    
    const pipeRes = await pipelineApi.startPipeline({
      requirementMd: form.value.requirement,
      projectId: selectedProject.value.id,
      projectLocalPath: selectedProject.value.localPath || '/tmp/demo-backend'
    })
    
    const pipelineId = pipeRes.data.pipelineId || pipeRes.data.id
    form.value.requirement = ''
    isSubmitting.value = false
    showToast?.success('流水线已启动', `ID: ${pipelineId.substring(0, 8)}`)
    router.push(`/pipelines/${pipelineId}`)
  } catch (error) {
    isSubmitting.value = false
    console.error('Failed to submit requirement:', error)
    showToast?.error('提交失败，请重试')
  }
}

const viewPipeline = (id) => {
  router.push(`/pipelines/${id}`)
}

const loadProjects = async () => {
  try {
    const res = await projectApi.getAllProjects()
    projects.value = res.data.map(p => ({
      id: p.id,
      name: p.projectName,
      description: p.description,
      icon: getProjectIcon(p.projectType),
      iconBgClass: getProjectIconBg(p.projectType),
      localPath: p.localPath
    }))
    
    if (projects.value.length === 0) {
      projects.value = [
        { id: 1, name: 'demo-backend', description: 'Spring Boot 后端服务', icon: '☕', iconBgClass: 'bg-orange-100', localPath: '/tmp/demo-backend' },
        { id: 2, name: 'demo-frontend', description: 'Vue 3 前端应用', icon: '💚', iconBgClass: 'bg-green-100', localPath: '/tmp/demo-frontend' },
      ]
    }
    
    if (!selectedProject.value && projects.value.length > 0) {
      selectedProject.value = projects.value[0]
      projectStore.setCurrentProject(projects.value[0])
    }
  } catch (error) {
    console.error('Failed to load projects:', error)
    projects.value = [
      { id: 1, name: 'demo-backend', description: 'Spring Boot 后端服务', icon: '☕', iconBgClass: 'bg-orange-100', localPath: '/tmp/demo-backend' },
      { id: 2, name: 'demo-frontend', description: 'Vue 3 前端应用', icon: '💚', iconBgClass: 'bg-green-100', localPath: '/tmp/demo-frontend' },
      { id: 3, name: 'api-gateway', description: 'API 网关服务', icon: '🚪', iconBgClass: 'bg-blue-100', localPath: '/tmp/api-gateway' },
      { id: 4, name: 'data-service', description: '数据服务模块', icon: '📊', iconBgClass: 'bg-purple-100', localPath: '/tmp/data-service' },
      { id: 5, name: 'ai-pipeline-ui', description: 'AI 流水线前端界面', icon: '🎨', iconBgClass: 'bg-pink-100', localPath: '/tmp/ai-pipeline-ui' },
    ]
    
    if (!selectedProject.value) {
      selectedProject.value = projects.value[0]
      projectStore.setCurrentProject(projects.value[0])
    }
  } finally {
    loading.value = false
  }
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

onMounted(() => {
  loadProjects()
})
</script>
