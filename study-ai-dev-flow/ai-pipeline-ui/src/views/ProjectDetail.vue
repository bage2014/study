<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载项目详情..." subtext="请稍候" />
    
    <template v-else>
      <div class="flex items-center justify-between">
        <div class="flex items-center">
          <button @click="goBack" class="mr-4 p-2 rounded-lg hover:bg-gray-100 transition-colors">
            <span class="text-xl text-gray-600">←</span>
          </button>
          <div>
            <h2 class="text-xl font-bold text-gray-800">{{ project.name }}</h2>
            <p class="text-sm text-gray-500">{{ project.description }}</p>
          </div>
        </div>
        <button 
          @click="showCreateRequirement = true" 
          class="bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-all font-medium shadow-md"
        >
          + 新建需求
        </button>
      </div>

      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-gray-800">{{ stats.total }}</p>
              <p class="text-sm text-gray-500 mt-1">需求总数</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center">
              <span class="text-indigo-600 text-lg">📋</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-green-600">{{ stats.completed }}</p>
              <p class="text-sm text-gray-500 mt-1">已完成</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center">
              <span class="text-green-600 text-lg">✓</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-yellow-600">{{ stats.inProgress }}</p>
              <p class="text-sm text-gray-500 mt-1">执行中</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-yellow-100 flex items-center justify-center">
              <span class="text-yellow-600 text-lg">●</span>
            </div>
          </div>
        </div>
        <div class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-2xl font-bold text-gray-600">{{ stats.pending }}</p>
              <p class="text-sm text-gray-500 mt-1">待处理</p>
            </div>
            <div class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center">
              <span class="text-gray-600 text-lg">⏳</span>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <div class="flex items-center justify-between mb-4">
          <h3 class="font-semibold text-gray-800">需求列表</h3>
          <div class="flex gap-2">
            <button 
              @click="currentFilter = 'all'"
              :class="[
                'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
                currentFilter === 'all' ? 'bg-indigo-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              ]"
            >
              全部
            </button>
            <button 
              @click="currentFilter = 'completed'"
              :class="[
                'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
                currentFilter === 'completed' ? 'bg-green-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              ]"
            >
              已完成
            </button>
            <button 
              @click="currentFilter = 'in_progress'"
              :class="[
                'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
                currentFilter === 'in_progress' ? 'bg-yellow-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              ]"
            >
              执行中
            </button>
            <button 
              @click="currentFilter = 'pending'"
              :class="[
                'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
                currentFilter === 'pending' ? 'bg-gray-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              ]"
            >
              待处理
            </button>
          </div>
        </div>
        
        <div class="space-y-3">
          <div 
            v-for="req in requirements" 
            :key="req.id"
            @click="viewRequirement(req)"
            class="flex items-center justify-between p-4 border border-gray-200 rounded-xl hover:border-indigo-300 hover:bg-gray-50 transition-all cursor-pointer"
          >
            <div>
              <p class="text-sm font-medium text-gray-800">{{ req.title }}</p>
              <p class="text-xs text-gray-500 mt-1">{{ formatTime(req.createdAt) }} · {{ req.status }}</p>
            </div>
            <div class="flex items-center space-x-3">
              <span :class="[
                'text-xs px-3 py-1.5 rounded-full font-medium',
                req.status === 'completed' ? 'bg-green-100 text-green-700' :
                req.status === 'in_progress' ? 'bg-yellow-100 text-yellow-700' :
                'bg-gray-100 text-gray-700'
              ]">
                {{ statusText(req.status) }}
              </span>
              <button 
                @click.stop="startPipeline(req)"
                :disabled="startingPipelineId === req.id"
                class="text-indigo-600 hover:text-indigo-800 text-sm font-medium flex items-center space-x-1 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="startingPipelineId === req.id">⏳</span>
                <span v-else>🚀</span>
                <span>{{ startingPipelineId === req.id ? '启动中...' : '启动' }}</span>
              </button>
            </div>
          </div>
          
          <div v-if="requirements.length === 0" class="text-center py-8">
            <div class="text-gray-400 text-4xl mb-4">📭</div>
            <p class="text-gray-500">暂无需求，请点击上方按钮创建</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <h3 class="font-semibold text-gray-800 mb-4">项目信息</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <span class="text-gray-500">项目类型</span>
            <span class="text-gray-700 font-medium">{{ project.projectType }}</span>
          </div>
          <div class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <span class="text-gray-500">数据库</span>
            <span class="text-gray-700 font-medium">{{ project.database }}</span>
          </div>
          <div class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <span class="text-gray-500">代码仓库</span>
            <span class="text-gray-700 font-medium">{{ project.repoUrl || 'GitHub' }}</span>
          </div>
          <div class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <span class="text-gray-500">最后更新</span>
            <span class="text-gray-700 font-medium">{{ formatTime(project.updatedAt) }}</span>
          </div>
        </div>
      </div>

      <div v-if="showCreateRequirement" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
        <div class="bg-white rounded-xl shadow-xl w-full max-w-md p-6 mx-4">
          <h4 class="text-lg font-semibold text-gray-800 mb-4">新建需求</h4>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">需求标题</label>
              <input 
                v-model="newRequirement.title" 
                type="text" 
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="请输入需求标题"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">需求描述</label>
              <textarea 
                v-model="newRequirement.description" 
                rows="4"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 resize-none"
                placeholder="请详细描述需求..."
              ></textarea>
            </div>
          </div>
          <div class="mt-6 flex gap-3">
            <button 
              @click="showCreateRequirement = false" 
              class="flex-1 bg-gray-100 text-gray-700 px-4 py-2.5 rounded-lg hover:bg-gray-200 transition-colors font-medium"
            >
              取消
            </button>
            <button 
              @click="createRequirement" 
              class="flex-1 bg-indigo-600 text-white px-4 py-2.5 rounded-lg hover:bg-indigo-700 transition-colors font-medium"
            >
              创建
            </button>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, inject } from 'vue'
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

const statusText = (status) => {
  switch (status) {
    case 'completed': return '已完成'
    case 'in_progress': return '执行中'
    case 'pending': return '待处理'
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

onMounted(() => {
  loadProjectDetail()
})
</script>
