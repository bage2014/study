<script setup>
import { ref, reactive, onMounted } from 'vue'
import { fetchLogs, fetchTrace } from '../api/grayflow.js'

const logs = ref([])
const total = ref(0)
const page = ref(0)
const PAGE_SIZE = 20

const loading = ref(false)
const error = ref(null)
const filter = reactive({ logType: '' })

const traceIdInput = ref('')
const showTraceModal = ref(false)
const traceLoading = ref(false)
const traceData = ref([])

async function loadLogs() {
  loading.value = true
  error.value = null
  try {
    const res = await fetchLogs({ logType: filter.logType || undefined, page: page.value, size: PAGE_SIZE })
    logs.value = res.content ?? []
    total.value = res.totalElements ?? 0
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

function onFilter() {
  page.value = 0
  loadLogs()
}

function prevPage() {
  if (page.value > 0) { page.value--; loadLogs() }
}

function nextPage() {
  if ((page.value + 1) * PAGE_SIZE < total.value) { page.value++; loadLogs() }
}

const totalPages = () => Math.max(1, Math.ceil(total.value / PAGE_SIZE))

async function queryTrace() {
  const id = traceIdInput.value.trim()
  if (!id) return
  showTraceModal.value = true
  traceLoading.value = true
  traceData.value = []
  error.value = null
  try {
    traceData.value = await fetchTrace(id)
  } catch (e) {
    error.value = e.message
    showTraceModal.value = false
  } finally {
    traceLoading.value = false
  }
}

function openTrace(traceId) {
  traceIdInput.value = traceId
  queryTrace()
}

function formatTime(instant) {
  if (!instant) return ''
  return new Date(instant).toLocaleString('zh-CN')
}

onMounted(loadLogs)
</script>

<template>
  <div>
    <h2 class="page-title">日志记录</h2>

    <div class="toolbar">
      <select v-model="filter.logType" class="select">
        <option value="">ALL</option>
        <option value="CONTROLLER">CONTROLLER</option>
        <option value="PROXY">PROXY</option>
      </select>
      <button class="btn" @click="onFilter">查询</button>
      <span class="sep">|</span>
      <input
        v-model="traceIdInput"
        class="input"
        placeholder="输入 traceId 查调用链"
        @keyup.enter="queryTrace"
      />
      <button class="btn-outline" @click="queryTrace">查链路</button>
    </div>

    <div v-if="error" role="alert" class="error-msg">{{ error }}</div>
    <div v-if="loading" class="hint">加载中...</div>

    <template v-else>
      <div class="table-wrap">
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>类型</th>
              <th>TraceId</th>
              <th>类名.方法名</th>
              <th>耗时(ms)</th>
              <th>时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!logs.length">
              <td colspan="6" class="hint">暂无数据</td>
            </tr>
            <tr v-for="log in logs" :key="log.id">
              <td>{{ log.id }}</td>
              <td>
                <span :class="['badge', log.logType === 'CONTROLLER' ? 'badge-blue' : 'badge-gray']">
                  {{ log.logType }}
                </span>
              </td>
              <td class="trace-cell" @click="openTrace(log.traceId)" :title="log.traceId">
                {{ log.traceId ? log.traceId.slice(0, 8) + '...' : '—' }}
              </td>
              <td>{{ log.className }}.{{ log.methodName }}</td>
              <td>{{ log.durationMs ?? '—' }}</td>
              <td>{{ formatTime(log.createdAt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="pagination">
        <button class="btn-sm" :disabled="page === 0" @click="prevPage">上一页</button>
        <span>第 {{ page + 1 }} 页 / 共 {{ totalPages() }} 页（{{ total }} 条）</span>
        <button class="btn-sm" :disabled="(page + 1) * PAGE_SIZE >= total" @click="nextPage">下一页</button>
      </div>
    </template>

    <!-- 调用链 Modal -->
    <div v-if="showTraceModal" class="modal-backdrop" @click.self="showTraceModal = false">
      <div role="dialog" aria-label="调用链详情" class="modal">
        <div class="modal-header">
          <span>调用链：{{ traceIdInput }}</span>
          <button class="close-btn" @click="showTraceModal = false">✕</button>
        </div>
        <div class="modal-body">
          <div v-if="traceLoading" class="hint">加载中...</div>
          <div v-else-if="!traceData.length" class="hint">无数据</div>
          <div v-else>
            <div v-for="node in traceData" :key="node.id" class="trace-node">
              <div class="trace-node-header">
                <span :class="['badge', node.logType === 'CONTROLLER' ? 'badge-blue' : 'badge-gray']">
                  {{ node.logType }}
                </span>
                <span class="node-index">#{{ node.callIndex }}</span>
                <span class="node-method">{{ node.className }}.{{ node.methodName }}</span>
                <span class="node-duration">{{ node.durationMs }}ms</span>
              </div>
              <div class="node-detail"><b>参数：</b>{{ node.args || '—' }}</div>
              <div class="node-detail"><b>结果：</b>{{ node.resultSummary || '—' }}</div>
              <div v-if="node.errorMsg" class="node-error"><b>错误：</b>{{ node.errorMsg }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 16px; }
.toolbar { display: flex; align-items: center; gap: 8px; margin-bottom: 16px; flex-wrap: wrap; }
.select { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; background: white; }
.input { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; width: 280px; }
.sep { color: #ddd; margin: 0 4px; }
.btn { padding: 8px 16px; background: #1a1a2e; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
.btn:hover { background: #2d2d52; }
.btn-outline { padding: 8px 16px; background: white; color: #1a1a2e; border: 1px solid #1a1a2e; border-radius: 4px; cursor: pointer; font-size: 14px; }
.error-msg { color: #e53e3e; margin-bottom: 12px; font-size: 14px; }
.hint { color: #999; padding: 16px 0; font-size: 14px; }
.table-wrap { background: white; border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,0.08); overflow: hidden; }
.table { width: 100%; border-collapse: collapse; }
.table th { background: #f7f7f7; padding: 12px 16px; text-align: left; font-size: 13px; color: #666; border-bottom: 1px solid #eee; }
.table td { padding: 12px 16px; font-size: 13px; border-bottom: 1px solid #f5f5f5; }
.table tbody tr:hover { background: #fafafa; }
.trace-cell { color: #4a9eff; cursor: pointer; font-family: monospace; }
.trace-cell:hover { text-decoration: underline; }
.badge { display: inline-block; padding: 2px 8px; border-radius: 12px; font-size: 12px; font-weight: 500; }
.badge-blue { background: #ebf5ff; color: #1a72e8; }
.badge-gray { background: #f0f0f0; color: #666; }
.pagination { display: flex; align-items: center; gap: 12px; justify-content: center; margin-top: 16px; font-size: 14px; color: #555; }
.btn-sm { padding: 6px 14px; background: white; border: 1px solid #ddd; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-sm:disabled { opacity: 0.4; cursor: not-allowed; }
.modal-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: white; border-radius: 8px; width: 720px; max-height: 80vh; display: flex; flex-direction: column; box-shadow: 0 8px 32px rgba(0,0,0,0.2); }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-bottom: 1px solid #eee; font-weight: 600; font-size: 15px; }
.close-btn { background: none; border: none; cursor: pointer; font-size: 20px; color: #999; line-height: 1; }
.close-btn:hover { color: #333; }
.modal-body { overflow-y: auto; padding: 16px 20px; }
.trace-node { background: #f9f9f9; border-radius: 6px; padding: 12px 16px; margin-bottom: 10px; border-left: 3px solid #4a9eff; }
.trace-node-header { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; }
.node-index { font-size: 12px; color: #999; }
.node-method { font-weight: 500; font-size: 14px; }
.node-duration { margin-left: auto; font-size: 12px; color: #999; }
.node-detail { font-size: 12px; color: #555; margin-top: 3px; word-break: break-all; }
.node-error { font-size: 12px; color: #e53e3e; margin-top: 3px; }
</style>