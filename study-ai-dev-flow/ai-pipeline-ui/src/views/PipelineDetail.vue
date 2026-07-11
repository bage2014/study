<template>
  <div>
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold text-gray-800">流水线详情</h2>
      <div class="flex gap-2">
        <button
          v-if="pipeline.status === 'RUNNING'"
          @click="handleCancel"
          class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors"
        >
          取消流水线
        </button>
        <router-link to="/pipelines" class="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors">
          返回列表
        </router-link>
      </div>
    </div>

    <div v-if="loading" class="text-center py-10">
      <div class="spinner-border" role="status"></div>
      <p class="mt-2 text-gray-500">加载中...</p>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <div class="lg:col-span-2 space-y-6">
        <div class="bg-white rounded-lg shadow-md p-4">
          <div class="flex items-center mb-4">
            <span
              :class="[
                'px-3 py-1 rounded-full text-sm font-medium',
                statusClass(pipeline.status)
              ]"
            >
              {{ pipeline.status }}
            </span>
            <span class="ml-4 text-gray-600">当前阶段: {{ pipeline.currentStage }}</span>
          </div>
          <div class="border-t pt-4">
            <h4 class="font-semibold text-gray-700 mb-2">需求信息</h4>
            <pre class="bg-gray-50 p-3 rounded text-sm">{{ getRequirementMd() }}</pre>
          </div>
          <div v-if="pipeline.resultJson" class="border-t pt-4 mt-4">
            <h4 class="font-semibold text-gray-700 mb-2">执行结果</h4>
            <p class="text-gray-600">{{ getResultMessage() }}</p>
          </div>
          <div v-if="pipeline.errorMessage" class="border-t pt-4 mt-4">
            <h4 class="font-semibold text-red-600 mb-2">错误信息</h4>
            <p class="text-red-600">{{ pipeline.errorMessage }}</p>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow-md p-4">
          <h4 class="font-semibold text-gray-700 mb-4">执行阶段</h4>
          <div class="space-y-3">
            <div
              v-for="stage in stages"
              :key="stage.stageName"
              class="relative pl-8"
            >
              <div
                :class="[
                  'absolute left-0 top-1 w-4 h-4 rounded-full border-2',
                  stage.status === 'COMPLETED' ? 'bg-green-500 border-green-500' :
                  stage.status === 'RUNNING' ? 'bg-yellow-500 border-yellow-500' :
                  stage.status === 'FAILED' ? 'bg-red-500 border-red-500' :
                  'bg-white border-gray-300'
                ]"
              ></div>
              <div class="flex justify-between items-center">
                <span class="font-medium text-gray-700">{{ stage.stageName }}</span>
                <span
                  :class="[
                    'text-sm',
                    stage.status === 'COMPLETED' ? 'text-green-600' :
                    stage.status === 'RUNNING' ? 'text-yellow-600' :
                    stage.status === 'FAILED' ? 'text-red-600' :
                    'text-gray-400'
                  ]"
                >
                  {{ stage.status || 'PENDING' }}
                </span>
              </div>
              <div v-if="stage.durationMs" class="text-xs text-gray-500 mt-1">
                耗时: {{ (stage.durationMs / 1000).toFixed(1) }}s
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow-md p-4">
          <h4 class="font-semibold text-gray-700 mb-4">生成的代码</h4>
          <div v-if="files.length === 0" class="text-center py-8 text-gray-500">
            暂无生成的代码文件
          </div>
          <div v-else>
            <div class="flex gap-2 mb-4 overflow-x-auto pb-2">
              <button
                v-for="file in files"
                :key="file.name"
                @click="selectFile(file.name)"
                :class="[
                  'px-3 py-2 rounded text-sm whitespace-nowrap',
                  selectedFile === file.name
                    ? 'bg-indigo-600 text-white'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                ]"
              >
                {{ file.name }}
              </button>
            </div>
            <pre class="bg-gray-900 text-green-400 p-4 rounded-lg overflow-x-auto text-sm font-mono">{{ fileContent }}</pre>
          </div>
        </div>
      </div>

      <div class="space-y-6">
        <div class="bg-white rounded-lg shadow-md p-4">
          <h4 class="font-semibold text-gray-700 mb-3">基本信息</h4>
          <div class="space-y-2 text-sm">
            <div class="flex justify-between">
              <span class="text-gray-500">流水线ID</span>
              <span class="text-gray-700 font-mono text-xs">{{ pipeline.pipelineId }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">创建时间</span>
              <span class="text-gray-700">{{ pipeline.createdAt }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">更新时间</span>
              <span class="text-gray-700">{{ pipeline.updatedAt }}</span>
            </div>
          </div>
        </div>

        <div v-if="pipeline.status === 'COMPLETED'" class="bg-green-50 rounded-lg shadow-md p-4">
          <h4 class="font-semibold text-green-700 mb-3">🎉 部署成功</h4>
          <p class="text-green-600 text-sm">应用已部署到 http://localhost:8081</p>
          <a href="http://localhost:8081/api/health" target="_blank" class="block mt-3 text-green-600 hover:underline">
            访问健康检查端点
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { pipelineApi } from '../api'

const route = useRoute()
const router = useRouter()
const pipelineId = route.params.id

const pipeline = ref({})
const stages = ref([])
const files = ref([])
const fileContent = ref('')
const selectedFile = ref('')
const loading = ref(true)
let refreshInterval = null

const statusClass = (status) => {
  switch (status) {
    case 'RUNNING':
      return 'bg-yellow-100 text-yellow-700'
    case 'COMPLETED':
      return 'bg-green-100 text-green-700'
    case 'FAILED':
      return 'bg-red-100 text-red-700'
    case 'CANCELLED':
      return 'bg-gray-100 text-gray-700'
    default:
      return 'bg-blue-100 text-blue-700'
  }
}

const getRequirementMd = () => {
  try {
    const input = JSON.parse(pipeline.value.inputJson)
    return input.requirementMd || '未提供需求描述'
  } catch {
    return '未提供需求描述'
  }
}

const getResultMessage = () => {
  try {
    const result = JSON.parse(pipeline.value.resultJson)
    return result.message || ''
  } catch {
    return ''
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const [pipelineRes, stagesRes, filesRes] = await Promise.all([
      pipelineApi.getPipeline(pipelineId),
      pipelineApi.getStages(pipelineId),
      pipelineApi.getGeneratedFiles(pipelineId)
    ])
    pipeline.value = pipelineRes.data
    stages.value = stagesRes.data
    files.value = filesRes.data
    if (files.value.length > 0 && !selectedFile.value) {
      selectedFile.value = files.value[0].name
      await loadFileContent(selectedFile.value)
    }
  } catch (e) {
    console.error('加载数据失败:', e)
  } finally {
    loading.value = false
  }
}

const loadFileContent = async (fileName) => {
  try {
    const response = await pipelineApi.getFileContent(pipelineId, fileName)
    fileContent.value = response.data
  } catch (e) {
    fileContent.value = '无法加载文件内容'
  }
}

const selectFile = async (fileName) => {
  selectedFile.value = fileName
  await loadFileContent(fileName)
}

const handleCancel = async () => {
  if (confirm('确定要取消这条流水线吗？')) {
    try {
      await pipelineApi.cancelPipeline(pipelineId)
      router.push('/pipelines')
    } catch (e) {
      alert('取消失败')
    }
  }
}

onMounted(() => {
  loadData()
  refreshInterval = setInterval(() => {
    if (pipeline.value.status === 'RUNNING') {
      loadData()
    }
  }, 3000)
})

onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>
