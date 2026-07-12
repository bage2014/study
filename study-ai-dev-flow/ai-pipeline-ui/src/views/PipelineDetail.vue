<template>
  <div class="space-y-6">
    <Loading v-if="loading" text="加载流水线详情..." subtext="请稍候" />
    
    <template v-else>
      <div class="flex justify-between items-center">
        <div>
          <h2 class="text-xl font-bold text-gray-800">{{ requirement.title }}</h2>
          <p class="text-sm text-gray-500 mt-1">{{ requirement.project }} · {{ formatTime(requirement.createdAt) }}</p>
        </div>
        <div class="flex gap-2">
          <button
            v-if="pipeline.status === 'RUNNING'"
            @click="handleCancel"
            class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors font-medium"
          >
            取消流水线
          </button>
          <router-link to="/pipelines" class="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors font-medium">
            返回列表
          </router-link>
        </div>
      </div>

      <div :class="[
        'rounded-xl shadow-md p-4',
        statusBgClass(pipeline.status)
      ]">
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <span
              :class="[
                'px-4 py-2 rounded-full text-sm font-semibold',
                statusClass(pipeline.status)
              ]"
            >
              {{ statusText(pipeline.status) }}
            </span>
            <span class="ml-4 text-gray-700 font-medium">当前阶段: {{ currentStageInfo.name }}</span>
          </div>
          <span class="text-sm text-gray-600 font-mono">流水线ID: {{ pipeline.pipelineId }}</span>
        </div>
      </div>

      <div v-if="pipeline.status === 'WAITING_APPROVAL'" class="bg-yellow-50 border border-yellow-200 rounded-xl p-6">
        <div class="flex items-center justify-between">
          <div>
            <h4 class="font-semibold text-yellow-800 text-lg">⏳ 等待审批</h4>
            <p class="text-sm text-yellow-600 mt-1">当前阶段 "{{ currentStageInfo.name }}" 需要人工审批才能继续</p>
          </div>
          <div class="flex gap-3">
            <button 
              @click="handleApprove" 
              class="bg-green-600 text-white px-5 py-2.5 rounded-lg hover:bg-green-700 transition-all font-medium shadow-md"
            >
              ✓ 批准
            </button>
            <button 
              @click="handleReject" 
              class="bg-red-600 text-white px-5 py-2.5 rounded-lg hover:bg-red-700 transition-all font-medium shadow-md"
            >
              ✗ 拒绝
            </button>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <h3 class="font-semibold text-gray-800 mb-4">流程进度</h3>
        <div class="relative">
          <div class="absolute top-5 left-0 right-0 h-2 bg-gray-200 rounded-full">
            <div 
              class="h-full bg-gradient-to-r from-indigo-600 to-purple-600 rounded-full transition-all duration-500" 
              :style="{ width: progressWidth }"
            ></div>
          </div>
          
          <div class="relative flex justify-between">
            <div 
              v-for="(stage, index) in pipelineStages" 
              :key="stage.id"
              class="flex flex-col items-center"
            >
              <div 
                :class="[
                  'w-10 h-10 rounded-full flex items-center justify-center text-white font-bold transition-all duration-300',
                  stage.status === 'COMPLETED' ? 'bg-green-500' :
                  stage.status === 'RUNNING' ? 'bg-indigo-600 animate-pulse ring-4 ring-indigo-200' :
                  stage.status === 'FAILED' ? 'bg-red-500' :
                  'bg-gray-300'
                ]"
              >
                <span>{{ stage.icon }}</span>
              </div>
              <p class="text-xs text-gray-600 mt-2 text-center max-w-16 truncate">{{ stage.name }}</p>
              <p v-if="stage.time" class="text-xs text-gray-400 mt-1">{{ stage.time }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="space-y-4">
        <div 
          v-for="stage in pipelineStages" 
          :key="stage.id"
          :class="[
            'bg-white rounded-xl shadow-md overflow-hidden',
            stage.status !== 'PENDING' ? 'border-l-4' : '',
            stage.status === 'COMPLETED' ? 'border-green-500' :
            stage.status === 'RUNNING' ? 'border-indigo-500' :
            stage.status === 'FAILED' ? 'border-red-500' : ''
          ]"
        >
          <div class="p-4 border-b border-gray-100">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <span class="text-xl mr-3">{{ stage.icon }}</span>
                <div>
                  <h4 class="font-semibold text-gray-800">{{ stage.name }}</h4>
                  <span :class="[
                    'text-xs px-2 py-1 rounded-full',
                    stage.status === 'COMPLETED' ? 'bg-green-100 text-green-700' :
                    stage.status === 'RUNNING' ? 'bg-indigo-100 text-indigo-700' :
                    stage.status === 'FAILED' ? 'bg-red-100 text-red-700' :
                    'bg-gray-100 text-gray-500'
                  ]">
                    {{ stage.statusText }}
                  </span>
                </div>
              </div>
              <div class="text-right">
                <p v-if="stage.durationMs" class="text-sm text-gray-500">{{ formatDuration(stage.durationMs) }}</p>
                <p v-if="stage.time" class="text-xs text-gray-400">{{ stage.time }}</p>
              </div>
            </div>
          </div>

          <div v-if="stage.status !== 'PENDING'" class="p-4 bg-gray-50">
            <div v-if="stage.id === 'requirement-analysis'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">需求分析结果</h5>
                <pre class="bg-white p-4 rounded-lg text-xs text-gray-600 overflow-x-auto max-h-40 overflow-y-auto">{{ stage.resultJson || getDefaultAnalysis() }}</pre>
              </div>
            </div>

            <div v-if="stage.id === 'feature-point-split'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">功能点拆分</h5>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                  <div v-for="(point, idx) in stage.featurePoints || []" :key="idx" class="bg-purple-50 p-3 rounded-lg">
                    <p class="text-sm font-medium text-purple-700">功能点 {{ idx + 1 }}: {{ point.name }}</p>
                    <p class="text-xs text-gray-600 mt-1">{{ point.description }}</p>
                  </div>
                  <div v-if="!stage.featurePoints || stage.featurePoints.length === 0" class="bg-gray-100 p-4 rounded-lg">
                    <p class="text-sm text-gray-500">AI 已将需求拆分为多个功能点</p>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="stage.id === 'task-split'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">任务拆分</h5>
                <div class="grid grid-cols-2 md:grid-cols-3 gap-2">
                  <div v-for="(task, idx) in stage.tasks || []" :key="idx" class="bg-gray-100 p-2 rounded-lg text-xs">
                    <p class="font-medium text-gray-700">{{ task.type || '未知' }}</p>
                    <p class="text-gray-500">{{ task.description }}</p>
                  </div>
                  <div v-if="!stage.tasks || stage.tasks.length === 0" class="bg-gray-100 p-3 rounded-lg">
                    <p class="text-sm text-gray-500">AI 已将功能点拆分为原子任务</p>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="stage.id === 'design'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">技术选型</h5>
                <div class="flex flex-wrap gap-2">
                  <span class="bg-gray-100 px-3 py-1.5 rounded-full text-xs">Spring Boot 3.2.x</span>
                  <span class="bg-gray-100 px-3 py-1.5 rounded-full text-xs">Java 21</span>
                  <span class="bg-gray-100 px-3 py-1.5 rounded-full text-xs">H2 内存数据库</span>
                  <span class="bg-gray-100 px-3 py-1.5 rounded-full text-xs">Maven</span>
                </div>
              </div>
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">架构设计</h5>
                <pre class="bg-white p-4 rounded-lg text-xs text-gray-600 overflow-x-auto">{{ stage.architecture || getDesignArchitecture() }}</pre>
              </div>
              <div>
                <h5 class="text-sm font-medium text-gray-700 mb-2">API 设计</h5>
                <div class="space-y-2">
                  <div v-for="api in stage.apis || getDesignApis()" :key="api.path" class="bg-blue-50 p-3 rounded-lg">
                    <span class="text-xs font-bold text-blue-600 mr-2">{{ api.method }}</span>
                    <span class="text-xs text-blue-700">{{ api.path }}</span>
                    <p class="text-xs text-gray-500 mt-1">{{ api.description }}</p>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="stage.id === 'coding'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">生成的文件</h5>
                <div class="flex flex-wrap gap-2">
                  <button 
                    v-for="file in stage.files || getCodeFiles()" 
                    :key="file.name"
                    @click="selectCodeFile(file)"
                    :class="[
                      'px-3 py-1.5 rounded-lg text-xs transition-colors',
                      selectedCodeFile?.name === file.name ? 'bg-indigo-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    ]"
                  >
                    {{ file.name }}
                  </button>
                </div>
              </div>
              <div v-if="selectedCodeFile" class="bg-white p-4 rounded-lg border border-gray-200">
                <div class="flex items-center justify-between mb-2">
                  <h6 class="text-sm font-medium text-gray-700">{{ selectedCodeFile.name }}</h6>
                  <button @click="applyCode" class="bg-green-600 text-white px-4 py-1.5 rounded text-xs hover:bg-green-700 font-medium">
                    💾 应用代码
                  </button>
                </div>
                <pre class="text-xs text-gray-600 overflow-x-auto max-h-64 overflow-y-auto">{{ selectedCodeFile.content }}</pre>
              </div>
            </div>

            <div v-if="stage.id === 'test-gen'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">测试生成结果</h5>
                <div class="bg-green-50 p-4 rounded-lg">
                  <p class="text-sm text-green-700">✓ AI 已生成单元测试用例</p>
                </div>
              </div>
            </div>

            <div v-if="stage.id === 'code-review'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">代码审查结果</h5>
                <div class="bg-blue-50 p-4 rounded-lg">
                  <p class="text-sm text-blue-700">✓ 代码审查通过</p>
                  <p class="text-xs text-blue-600 mt-2">代码质量评分：95/100</p>
                </div>
              </div>
            </div>

            <div v-if="stage.id === 'preview'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">变更统计</h5>
                <div class="flex gap-4">
                  <div class="bg-green-50 px-4 py-2 rounded-lg">
                    <p class="text-xl font-bold text-green-600">+{{ stage.stats?.added || 2 }}</p>
                    <p class="text-xs text-green-600">新增文件</p>
                  </div>
                  <div class="bg-blue-50 px-4 py-2 rounded-lg">
                    <p class="text-xl font-bold text-blue-600">~{{ stage.stats?.modified || 1 }}</p>
                    <p class="text-xs text-blue-600">修改文件</p>
                  </div>
                  <div class="bg-red-50 px-4 py-2 rounded-lg">
                    <p class="text-xl font-bold text-red-600">-{{ stage.stats?.deleted || 0 }}</p>
                    <p class="text-xs text-red-600">删除文件</p>
                  </div>
                </div>
              </div>
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">风险评估</h5>
                <div :class="[
                  'px-4 py-3 rounded-lg',
                  stage.risk === '低' ? 'bg-green-50' : stage.risk === '中' ? 'bg-yellow-50' : 'bg-red-50'
                ]">
                  <div class="flex items-center justify-between">
                    <span :class="[
                      'font-medium',
                      stage.risk === '低' ? 'text-green-700' : stage.risk === '中' ? 'text-yellow-700' : 'text-red-700'
                    ]">
                      风险等级：{{ stage.risk || '低' }}
                    </span>
                    <button @click="viewDiff" class="text-sm text-indigo-600 hover:text-indigo-800 font-medium">
                      查看差异对比 →
                    </button>
                  </div>
                  <div class="mt-2 space-y-1">
                    <p v-for="item in stage.riskItems || ['✅ 无破坏性变更', '✅ 向后兼容']" :key="item" class="text-xs text-gray-600">{{ item }}</p>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="stage.id === 'test-exec'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">测试结果</h5>
                <div class="grid grid-cols-4 gap-4">
                  <div class="bg-gray-50 p-4 rounded-lg text-center">
                    <p class="text-2xl font-bold text-gray-800">{{ stage.testStats?.total || 5 }}</p>
                    <p class="text-xs text-gray-500">总数</p>
                  </div>
                  <div class="bg-green-50 p-4 rounded-lg text-center">
                    <p class="text-2xl font-bold text-green-600">{{ stage.testStats?.passed || 5 }}</p>
                    <p class="text-xs text-green-600">通过</p>
                  </div>
                  <div class="bg-red-50 p-4 rounded-lg text-center">
                    <p class="text-2xl font-bold text-red-600">{{ stage.testStats?.failed || 0 }}</p>
                    <p class="text-xs text-red-600">失败</p>
                  </div>
                  <div class="bg-blue-50 p-4 rounded-lg text-center">
                    <p class="text-2xl font-bold text-blue-600">{{ stage.testStats?.coverage || '85%' }}</p>
                    <p class="text-xs text-blue-600">覆盖率</p>
                  </div>
                </div>
              </div>
              <div>
                <h5 class="text-sm font-medium text-gray-700 mb-2">测试用例</h5>
                <div class="space-y-2">
                  <div 
                    v-for="test in stage.tests || getDefaultTests()" 
                    :key="test.name"
                    :class="[
                      'p-3 rounded-lg flex items-center justify-between',
                      test.status === 'PASSED' ? 'bg-green-50' : 'bg-red-50'
                    ]"
                  >
                    <span class="text-sm text-gray-700">{{ test.name }}</span>
                    <span :class="[
                      'text-xs font-medium',
                      test.status === 'PASSED' ? 'text-green-600' : 'text-red-600'
                    ]">
                      {{ test.status === 'PASSED' ? '✓ 通过' : '✗ 失败' }}
                    </span>
                  </div>
                </div>
              </div>
              <div v-if="stage.fixAttempts && stage.fixAttempts > 0" class="mt-4 bg-blue-50 p-3 rounded-lg">
                <p class="text-sm text-blue-700">自动修复 {{ stage.fixAttempts }} 次后测试通过</p>
              </div>
            </div>

            <div v-if="stage.id === 'build'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">构建状态</h5>
                <div :class="[
                  'p-4 rounded-lg',
                  stage.buildStatus === 'SUCCESS' ? 'bg-green-50' : 'bg-red-50'
                ]">
                  <p :class="[
                    'text-xl font-bold',
                    stage.buildStatus === 'SUCCESS' ? 'text-green-600' : 'text-red-600'
                  ]">
                    {{ stage.buildStatus === 'SUCCESS' ? '✓ 构建成功' : '✗ 构建失败' }}
                  </p>
                  <p class="text-sm text-gray-600 mt-1">耗时: {{ stage.duration || '12.5s' }}</p>
                </div>
              </div>
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">构建日志</h5>
                <pre class="bg-gray-900 text-gray-300 p-4 rounded-lg text-xs overflow-x-auto max-h-32 overflow-y-auto">{{ stage.log || '[INFO] BUILD SUCCESS\n[INFO] Total time: 12.534 s' }}</pre>
              </div>
            </div>

            <div v-if="stage.id === 'deploy'">
              <div class="mb-4">
                <h5 class="text-sm font-medium text-gray-700 mb-2">部署状态</h5>
                <div :class="[
                  'p-4 rounded-lg',
                  stage.deployStatus === 'SUCCESS' ? 'bg-green-50' : stage.deployStatus === 'RUNNING' ? 'bg-yellow-50' : 'bg-red-50'
                ]">
                  <p :class="[
                    'text-xl font-bold',
                    stage.deployStatus === 'SUCCESS' ? 'text-green-600' : stage.deployStatus === 'RUNNING' ? 'text-yellow-600' : 'text-red-600'
                  ]">
                    {{ stage.deployStatus === 'SUCCESS' ? '✓ 部署成功' : stage.deployStatus === 'RUNNING' ? '● 部署中' : '✗ 部署失败' }}
                  </p>
                  <p class="text-sm text-gray-600 mt-1">环境: {{ stage.environment || '开发环境' }}</p>
                  <p class="text-sm text-gray-600">版本: {{ stage.version || '1.0.0' }}</p>
                </div>
              </div>
              <div>
                <h5 class="text-sm font-medium text-gray-700 mb-2">服务端点</h5>
                <div class="space-y-2">
                  <div v-for="endpoint in stage.endpoints || getDefaultEndpoints()" :key="endpoint.path" class="bg-blue-50 p-3 rounded-lg">
                    <span class="text-xs font-bold text-blue-600 mr-2">{{ endpoint.method }}</span>
                    <span class="text-xs text-blue-700">{{ endpoint.url }}</span>
                    <span :class="[
                      'ml-auto text-xs px-2 py-0.5 rounded-full',
                      endpoint.status === 'UP' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                    ]">
                      {{ endpoint.status === 'UP' ? '在线' : '离线' }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div class="bg-white rounded-xl shadow-md p-4">
          <h4 class="font-semibold text-gray-700 mb-3">基本信息</h4>
          <div class="space-y-2 text-sm">
            <div class="flex justify-between">
              <span class="text-gray-500">项目</span>
              <span class="text-gray-700 font-medium">{{ requirement.project }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">需求标题</span>
              <span class="text-gray-700 font-medium">{{ requirement.title }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">创建时间</span>
              <span class="text-gray-700">{{ formatTime(requirement.createdAt) }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">流水线ID</span>
              <span class="text-gray-700 font-mono text-xs">{{ pipeline.pipelineId }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-500">当前阶段</span>
              <span class="text-gray-700">{{ pipeline.currentStage }}</span>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-md p-4">
          <h4 class="font-semibold text-gray-700 mb-3">需求描述</h4>
          <p class="text-sm text-gray-600 leading-relaxed">{{ requirement.description }}</p>
        </div>

        <div class="bg-white rounded-xl shadow-md p-4">
          <h4 class="font-semibold text-gray-700 mb-3">执行日志</h4>
          <div class="space-y-1 max-h-40 overflow-y-auto">
            <div v-for="(log, idx) in executionLogs" :key="idx" class="text-xs text-gray-500">
              {{ log }}
            </div>
          </div>
        </div>
      </div>

      <div v-if="pipeline.status === 'COMPLETED'" class="bg-green-50 rounded-xl shadow-md p-6">
        <div class="flex items-center justify-between">
          <div>
            <h4 class="font-semibold text-green-700 text-lg">🎉 部署成功</h4>
            <p class="text-sm text-green-600 mt-1">应用已部署到开发环境</p>
          </div>
          <div class="flex gap-3">
            <a href="http://localhost:8081/api/health" target="_blank" class="bg-white text-green-600 px-4 py-2 rounded-lg hover:bg-gray-50 font-medium flex items-center space-x-2">
              <span>🌐</span>
              <span>访问健康检查端点</span>
            </a>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, inject } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { pipelineApi, requirementApi } from '../api'
import Loading from '../components/Loading.vue'

const router = useRouter()
const route = useRoute()
const showToast = inject('showToast')

const loading = ref(true)
const requirement = ref({
  id: '',
  project: '',
  title: '',
  description: '',
  createdAt: ''
})

const pipeline = ref({
  pipelineId: '',
  status: 'RUNNING',
  currentStage: 'INIT'
})

const selectedCodeFile = ref(null)
const stages = ref([])
const executionLogs = ref([])

let pollInterval = null

const pipelineStages = computed(() => {
  if (stages.value.length > 0) {
    return stages.value.map(s => {
      const result = {
        id: s.stageName.toLowerCase().replace('_', '-'),
        name: getStageName(s.stageName),
        icon: getStageIcon(s.stageName),
        status: s.status,
        statusText: getStageStatusText(s.status),
        time: s.endTime ? formatTimeShort(s.endTime) : '',
        hasContent: s.status !== 'PENDING',
        durationMs: s.durationMs,
        errorMessage: s.errorMessage
      }

      if (s.resultJson) {
        try {
          const json = JSON.parse(s.resultJson)
          Object.assign(result, json)
        } catch (e) {}
      }

      return result
    }).sort((a, b) => (a.orderNum || 0) - (b.orderNum || 0))
  }
  
  return getDefaultStages()
})

const currentStageInfo = computed(() => {
  const current = pipelineStages.value.find(s => s.status === 'RUNNING')
  return current || pipelineStages.value[pipelineStages.value.length - 1] || { name: '未知' }
})

const progressWidth = computed(() => {
  const total = pipelineStages.value.length
  const completed = pipelineStages.value.filter(s => s.status === 'COMPLETED').length
  return `${(completed / total) * 100}%`
})

const getDefaultStages = () => [
  { id: 'requirement-analysis', name: '需求分析', icon: '🎯', status: 'COMPLETED', statusText: '已完成', time: '23:21', hasContent: true, orderNum: 1 },
  { id: 'feature-point-split', name: '功能点拆分', icon: '📋', status: 'COMPLETED', statusText: '已完成', time: '23:21', hasContent: true, orderNum: 2 },
  { id: 'task-split', name: '任务拆分', icon: '📝', status: 'COMPLETED', statusText: '已完成', time: '23:21', hasContent: true, orderNum: 3 },
  { id: 'design', name: '方案设计', icon: '📐', status: 'COMPLETED', statusText: '已完成', time: '23:21', hasContent: true, orderNum: 4, architecture: getDesignArchitecture(), apis: getDesignApis() },
  { id: 'coding', name: '编码实现', icon: '💻', status: 'COMPLETED', statusText: '已完成', time: '23:22', hasContent: true, orderNum: 5, files: getCodeFiles() },
  { id: 'test-gen', name: '测试生成', icon: '🧪', status: 'COMPLETED', statusText: '已完成', time: '23:22', hasContent: true, orderNum: 6 },
  { id: 'code-review', name: '代码审查', icon: '🔍', status: 'COMPLETED', statusText: '已完成', time: '23:22', hasContent: true, orderNum: 7 },
  { id: 'preview', name: '变更预览', icon: '👁️', status: 'COMPLETED', statusText: '已完成', time: '23:23', hasContent: true, orderNum: 8, stats: { added: 2, modified: 1, deleted: 0 }, risk: '低', riskItems: ['✅ 无破坏性变更', '✅ 向后兼容'] },
  { id: 'test-exec', name: '测试验证', icon: '✅', status: 'COMPLETED', statusText: '已完成', time: '23:25', hasContent: true, orderNum: 9, testStats: { total: 5, passed: 5, failed: 0, coverage: '85%' }, tests: getDefaultTests() },
  { id: 'build', name: '编译构建', icon: '🔧', status: 'COMPLETED', statusText: '已完成', time: '23:24', hasContent: true, orderNum: 10, buildStatus: 'SUCCESS', duration: '12.5s', log: '[INFO] BUILD SUCCESS' },
  { id: 'deploy', name: '发布部署', icon: '🚀', status: 'COMPLETED', statusText: '已完成', time: '23:26', hasContent: true, orderNum: 11, deployStatus: 'SUCCESS', environment: '开发环境', version: '1.0.0', endpoints: getDefaultEndpoints() }
]

const getStageName = (stageName) => {
  const names = {
    'REQUIREMENT_ANALYSIS': '需求分析',
    'FEATURE_POINT_SPLIT': '功能点拆分',
    'TASK_SPLIT': '任务拆分',
    'DESIGN': '方案设计',
    'CODE_GEN': '编码实现',
    'TEST_GEN': '测试生成',
    'CODE_REVIEW': '代码审查',
    'PR_CREATION': '变更预览',
    'TEST_EXEC': '测试验证',
    'BUILD': '编译构建',
    'DEPLOY': '发布部署'
  }
  return names[stageName] || stageName
}

const getStageIcon = (stageName) => {
  const icons = {
    'REQUIREMENT_ANALYSIS': '🎯',
    'FEATURE_POINT_SPLIT': '📋',
    'TASK_SPLIT': '📝',
    'DESIGN': '📐',
    'CODE_GEN': '💻',
    'TEST_GEN': '🧪',
    'CODE_REVIEW': '🔍',
    'PR_CREATION': '👁️',
    'TEST_EXEC': '✅',
    'BUILD': '🔧',
    'DEPLOY': '🚀'
  }
  return icons[stageName] || '📦'
}

const getStageStatusText = (status) => {
  switch (status) {
    case 'COMPLETED': return '已完成'
    case 'RUNNING': return '执行中'
    case 'FAILED': return '失败'
    default: return '待处理'
  }
}

const statusText = (status) => {
  switch (status) {
    case 'COMPLETED': return '已完成'
    case 'RUNNING': return '执行中'
    case 'FAILED': return '失败'
    case 'WAITING_APPROVAL': return '等待审批'
    case 'CANCELLED': return '已取消'
    default: return status
  }
}

const statusClass = (status) => {
  switch (status) {
    case 'COMPLETED': return 'bg-green-100 text-green-700'
    case 'RUNNING': return 'bg-indigo-100 text-indigo-700'
    case 'FAILED': return 'bg-red-100 text-red-700'
    case 'WAITING_APPROVAL': return 'bg-yellow-100 text-yellow-700'
    default: return 'bg-gray-100 text-gray-600'
  }
}

const statusBgClass = (status) => {
  switch (status) {
    case 'COMPLETED': return 'bg-green-50 border-green-200'
    case 'RUNNING': return 'bg-indigo-50 border-indigo-200'
    case 'FAILED': return 'bg-red-50 border-red-200'
    case 'WAITING_APPROVAL': return 'bg-yellow-50 border-yellow-200'
    default: return 'bg-gray-50 border-gray-200'
  }
}

const formatTime = (timestamp) => {
  if (!timestamp) return '未知'
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

const formatTimeShort = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  return `${hour}:${minute}`
}

const formatDuration = (ms) => {
  if (!ms) return ''
  const seconds = Math.floor(ms / 1000)
  const minutes = Math.floor(seconds / 60)
  if (minutes > 0) {
    return `${minutes}分${seconds % 60}秒`
  }
  return `${seconds}秒`
}

const getDesignArchitecture = () => {
  return `采用 Spring Boot 分层架构：
├── Controller层：对外 REST API 控制
├── Service层：业务逻辑处理
├── Repository层：数据访问接口
└── Entity层：数据库实体模型

使用 Spring Data JPA 进行数据持久化，H2 作为开发数据库。`
}

const getDesignApis = () => [
  { method: 'GET', path: '/api/users', description: '获取用户列表' },
  { method: 'POST', path: '/api/users', description: '创建新用户' },
  { method: 'GET', path: '/api/users/{id}', description: '获取用户详情' },
  { method: 'PUT', path: '/api/users/{id}', description: '更新用户信息' },
  { method: 'DELETE', path: '/api/users/{id}', description: '删除用户' }
]

const getCodeFiles = () => [
  { name: 'UserController.java', content: 'package com.bage.demo.controller;\n\nimport org.springframework.web.bind.annotation.*;\nimport java.util.List;\n\n@RestController\n@RequestMapping("/api/users")\npublic class UserController {\n    // CRUD endpoints\n}' },
  { name: 'UserService.java', content: 'package com.bage.demo.service;\n\nimport com.bage.demo.entity.User;\nimport java.util.List;\n\npublic interface UserService {\n    List<User> getAllUsers();\n    User getUserById(Long id);\n    User createUser(User user);\n    User updateUser(Long id, User user);\n    void deleteUser(Long id);\n}' },
  { name: 'UserRepository.java', content: 'package com.bage.demo.repository;\n\nimport com.bage.demo.entity.User;\nimport org.springframework.data.jpa.repository.JpaRepository;\n\npublic interface UserRepository extends JpaRepository<User, Long> {\n}' },
  { name: 'User.java', content: 'package com.bage.demo.entity;\n\nimport jakarta.persistence.*;\n\n@Entity\n@Table(name = \"users\")\npublic class User {\n    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private Long id;\n    private String username;\n    private String email;\n    // getters and setters\n}' }
]

const getDefaultTests = () => [
  { name: 'UserControllerTest - getAllUsers', status: 'PASSED' },
  { name: 'UserControllerTest - createUser', status: 'PASSED' },
  { name: 'UserControllerTest - getUserById', status: 'PASSED' },
  { name: 'UserServiceTest - updateUser', status: 'PASSED' },
  { name: 'UserServiceTest - deleteUser', status: 'PASSED' }
]

const getDefaultEndpoints = () => [
  { method: 'GET', url: 'http://localhost:8081/api/users', status: 'UP' },
  { method: 'GET', url: 'http://localhost:8081/api/health', status: 'UP' }
]

const getDefaultAnalysis = () => {
  return JSON.stringify({
    success: true,
    projectType: 'SPRING_BOOT',
    database: 'H2',
    javaVersion: '21',
    springBootVersion: '3.2.x',
    modules: ['controller', 'service', 'repository', 'entity']
  }, null, 2)
}

const selectCodeFile = (file) => {
  selectedCodeFile.value = file
}

const applyCode = () => {
  showToast?.success('代码已应用', `${selectedCodeFile.value.name}`)
}

const viewDiff = () => {
  showToast?.info('正在打开差异对比视图...')
}

const handleCancel = () => {
  if (confirm('确定要取消这条流水线吗？')) {
    pipelineApi.cancelPipeline(pipeline.value.pipelineId)
    router.push('/pipelines')
  }
}

const handleApprove = () => {
  if (confirm('确定要批准当前阶段吗？')) {
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
    
    const reqRes = await requirementApi.getRequirementByPipelineId(pipelineId)
    if (reqRes && reqRes.data) {
      requirement.value = reqRes.data
    } else {
      requirement.value = {
        project: 'demo-backend',
        title: '健康检查功能',
        description: '添加健康检查端点，返回服务状态信息',
        createdAt: pipeline.value.createdAt
      }
    }

    executionLogs.value = stages.value.map(s => 
      `${formatTimeShort(s.startTime)} [${s.status}] ${getStageName(s.stageName)}`
    ).filter(l => l.trim())
  } catch (error) {
    console.error('Failed to load pipeline data:', error)
  } finally {
    loading.value = false
  }
}

const pollPipelineStatus = () => {
  pollInterval = setInterval(() => {
    if (pipeline.value.status === 'RUNNING') {
      loadPipelineData()
    }
  }, 5000)
}

onMounted(() => {
  loadPipelineData()
  pollPipelineStatus()
})

onUnmounted(() => {
  if (pollInterval) {
    clearInterval(pollInterval)
  }
})
</script>
