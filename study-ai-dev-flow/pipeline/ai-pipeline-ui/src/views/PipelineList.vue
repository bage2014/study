<template>
  <div class="space-y-6">
    <header class="flex items-end justify-between">
      <div>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">流水线</h1>
        <p class="text-ink-secondary mt-1">所有 AI 流水线的执行记录</p>
      </div>
      <router-link to="/" class="btn-primary">录入需求</router-link>
    </header>

    <Loading v-if="loading" text="加载流水线" subtext="请稍候" />

    <template v-else>
      <div v-if="pipelines.length === 0" class="card p-16 text-center">
        <p class="text-ink-muted text-sm">暂无流水线记录</p>
        <router-link to="/" class="btn-primary mt-4 inline-flex">录入第一条需求</router-link>
      </div>

      <div v-else class="card overflow-hidden">
        <table class="w-full text-sm">
          <thead>
            <tr class="border-b border-line text-left text-2xs font-medium uppercase tracking-wide text-ink-muted">
              <th class="px-5 py-3">ID</th>
              <th class="px-5 py-3">需求</th>
              <th class="px-5 py-3">当前阶段</th>
              <th class="px-5 py-3">状态</th>
              <th class="px-5 py-3">创建时间</th>
              <th class="px-5 py-3"></th>
            </tr>
          </thead>
          <tbody class="divide-y divide-line">
            <tr
              v-for="pipeline in pipelines"
              :key="pipeline.pipelineId"
              @click="goToDetail(pipeline.pipelineId)"
              class="hover:bg-base cursor-pointer transition-colors"
            >
              <td class="px-5 py-3 mono text-ink-secondary">{{ pipeline.pipelineId.substring(0, 8) }}</td>
              <td class="px-5 py-3 text-ink">{{ pipeline.requirementTitle || '需求处理中' }}</td>
              <td class="px-5 py-3 text-ink-secondary">{{ pipeline.currentStage }}</td>
              <td class="px-5 py-3">
                <span :class="['tag', statusClass(pipeline.status)]">{{ statusText(pipeline.status) }}</span>
              </td>
              <td class="px-5 py-3 text-ink-muted text-xs">{{ formatTime(pipeline.createdAt) }}</td>
              <td class="px-5 py-3 text-right text-ink-muted">→</td>
            </tr>
          </tbody>
        </table>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { pipelineApi } from '../api'
import Loading from '../components/Loading.vue'

const router = useRouter()
const showToast = inject('showToast')

const pipelines = ref([])
const loading = ref(true)

const statusClass = (status) => {
  switch (status) {
    case 'RUNNING': return 'bg-warn-soft text-warn'
    case 'COMPLETED': return 'bg-ok-soft text-ok'
    case 'FAILED': return 'bg-danger-soft text-danger'
    case 'CANCELLED': return 'bg-base text-ink-muted'
    case 'WAITING_APPROVAL': return 'bg-accent-soft text-accent'
    default: return 'bg-base text-ink-secondary'
  }
}

const statusText = (status) => {
  switch (status) {
    case 'RUNNING': return '执行中'
    case 'COMPLETED': return '已完成'
    case 'FAILED': return '失败'
    case 'CANCELLED': return '已取消'
    case 'WAITING_APPROVAL': return '等待审批'
    default: return status
  }
}

const formatTime = (timestamp) => {
  if (!timestamp) return '—'
  const diff = Date.now() - (typeof timestamp === 'number' ? timestamp : new Date(timestamp).getTime())
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const minutes = Math.floor(diff / (1000 * 60))
  if (days > 0) return `${days} 天前`
  if (hours > 0) return `${hours} 小时前`
  if (minutes > 0) return `${minutes} 分钟前`
  return '刚刚'
}

const goToDetail = (pipelineId) => router.push(`/pipelines/${pipelineId}`)

const loadPipelines = async () => {
  loading.value = true
  try {
    const response = await pipelineApi.getAllPipelines()
    pipelines.value = response.data
  } catch (e) {
    console.error('加载流水线失败:', e)
    showToast?.error('加载流水线列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadPipelines)
</script>
