<script setup>
import { ref, onMounted } from 'vue'
import { fetchSessions, fetchSession, createSession } from '../api/grayflow.js'

const sessions = ref([])
const loading = ref(false)
const error = ref(null)

const showModal = ref(false)
const newCount = ref(10)
const creating = ref(false)

const expandedSessionId = ref(null)
const expandedRecords = ref([])
const expandedDiffId = ref(null)
const loadingRecords = ref(false)

async function loadSessions() {
  loading.value = true
  error.value = null
  try {
    const res = await fetchSessions()
    sessions.value = Array.isArray(res) ? res : []
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function toggleSession(sessionId) {
  if (expandedSessionId.value === sessionId) {
    expandedSessionId.value = null
    expandedRecords.value = []
    expandedDiffId.value = null
    return
  }
  expandedSessionId.value = sessionId
  expandedRecords.value = []
  expandedDiffId.value = null
  loadingRecords.value = true
  try {
    const res = await fetchSession(sessionId)
    expandedRecords.value = res?.records ?? []
  } catch (e) {
    error.value = e.message
  } finally {
    loadingRecords.value = false
  }
}

function toggleDiff(recordId) {
  expandedDiffId.value = expandedDiffId.value === recordId ? null : recordId
}

function formatDiff(diffPatch) {
  if (!diffPatch) return '无差异'
  try {
    return JSON.stringify(JSON.parse(diffPatch), null, 2)
  } catch {
    return diffPatch
  }
}

async function onCreateSession() {
  const count = Number(newCount.value)
  if (count < 1 || count > 100) return
  creating.value = true
  error.value = null
  try {
    await createSession(count)
    showModal.value = false
    newCount.value = 10
    await loadSessions()
  } catch (e) {
    error.value = e.message
  } finally {
    creating.value = false
  }
}

function formatTime(instant) {
  if (!instant) return ''
  return new Date(instant).toLocaleString('zh-CN')
}

onMounted(loadSessions)
</script>

<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">回放记录</h2>
      <button class="btn" @click="showModal = true">新建回放</button>
    </div>

    <div v-if="error" role="alert" class="error-msg">{{ error }}</div>
    <div v-if="loading" class="hint">加载中...</div>

    <template v-else>
      <div class="table-wrap">
        <table class="table">
          <thead>
            <tr>
              <th>会话ID</th>
              <th>状态</th>
              <th>总数</th>
              <th>已完成</th>
              <th>创建时间</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!sessions.length">
              <td colspan="6" class="hint">暂无回放会话，点击「新建回放」开始</td>
            </tr>
            <template v-for="s in sessions" :key="s.sessionId">
              <tr class="session-row" @click="toggleSession(s.sessionId)">
                <td class="mono" :title="s.sessionId">{{ s.sessionId ? s.sessionId.slice(0, 8) + '...' : '—' }}</td>
                <td>
                  <span :class="['badge', s.status === 'COMPLETED' ? 'badge-green' : 'badge-yellow']">
                    {{ s.status }}
                  </span>
                </td>
                <td>{{ s.totalCount }}</td>
                <td>{{ s.completedCount }}</td>
                <td>{{ formatTime(s.createdAt) }}</td>
                <td class="expand-icon">{{ expandedSessionId === s.sessionId ? '▲' : '▼' }}</td>
              </tr>
              <!-- 展开 records -->
              <tr v-if="expandedSessionId === s.sessionId">
                <td colspan="6" class="expanded-cell">
                  <div v-if="loadingRecords" class="hint">加载中...</div>
                  <div v-else-if="!expandedRecords.length" class="hint">无回放记录</div>
                  <div v-else>
                    <div v-for="r in expandedRecords" :key="r.id" class="record-row">
                      <span class="match-icon">{{ r.matchResult === 'MATCH' ? '✅' : '❌' }}</span>
                      <span class="record-method">{{ r.httpMethod }} {{ r.requestUri }}</span>
                      <span v-if="r.chainMatchResult" :class="['badge', r.chainMatchResult === 'MATCH' ? 'badge-green' : 'badge-red']" style="font-size:11px;">
                        链路 {{ r.chainMatchResult }}
                      </span>
                      <span v-if="r.errorMsg" class="record-error">{{ r.errorMsg }}</span>
                      <button class="btn-sm" @click.stop="toggleDiff(r.id)">
                        {{ expandedDiffId === r.id ? '收起 Diff' : '查看 Diff' }}
                      </button>
                      <pre v-if="expandedDiffId === r.id" class="diff-pre">{{ formatDiff(r.diffPatch) }}</pre>
                    </div>
                  </div>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </template>

    <!-- 新建回放 Modal -->
    <div v-if="showModal" class="modal-backdrop" @click.self="showModal = false">
      <div role="dialog" aria-label="新建回放会话" class="modal">
        <div class="modal-header">
          <span>新建回放会话</span>
          <button class="close-btn" @click="showModal = false">✕</button>
        </div>
        <div class="modal-body">
          <label class="form-label">回放最近 N 条请求（1 - 100）</label>
          <input v-model.number="newCount" type="number" min="1" max="100" class="form-input" />
          <div class="modal-actions">
            <button class="btn-outline" @click="showModal = false">取消</button>
            <button class="btn" :disabled="creating" @click="onCreateSession">
              {{ creating ? '创建中...' : '确认' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-title { font-size: 20px; font-weight: 600; }
.error-msg { color: #e53e3e; margin-bottom: 12px; font-size: 14px; }
.hint { color: #999; padding: 16px; font-size: 14px; }
.table-wrap { background: white; border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,0.08); overflow: hidden; }
.table { width: 100%; border-collapse: collapse; }
.table th { background: #f7f7f7; padding: 12px 16px; text-align: left; font-size: 13px; color: #666; border-bottom: 1px solid #eee; }
.table td { padding: 12px 16px; font-size: 13px; border-bottom: 1px solid #f5f5f5; }
.session-row { cursor: pointer; }
.session-row:hover { background: #fafafa; }
.mono { font-family: monospace; }
.expand-icon { color: #999; text-align: right; }
.badge { display: inline-block; padding: 2px 8px; border-radius: 12px; font-size: 12px; font-weight: 500; }
.badge-green { background: #f0fff4; color: #38a169; }
.badge-yellow { background: #fffbf0; color: #d69e2e; }
.badge-red { background: #fff5f5; color: #e53e3e; }
.expanded-cell { padding: 0 !important; background: #fafafa; }
.record-row { display: flex; align-items: center; gap: 10px; padding: 10px 20px; border-bottom: 1px solid #eee; flex-wrap: wrap; font-size: 13px; }
.match-icon { font-size: 16px; }
.record-method { font-family: monospace; font-size: 12px; color: #555; }
.record-error { color: #e53e3e; font-size: 12px; }
.diff-pre { background: #1e1e2e; color: #a6e3a1; padding: 14px 16px; border-radius: 6px; font-size: 12px; overflow-x: auto; width: 100%; margin-top: 8px; white-space: pre-wrap; word-break: break-all; }
.btn { padding: 8px 16px; background: #1a1a2e; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
.btn:hover { background: #2d2d52; }
.btn:disabled { opacity: 0.5; cursor: not-allowed; }
.btn-outline { padding: 8px 16px; background: white; color: #1a1a2e; border: 1px solid #1a1a2e; border-radius: 4px; cursor: pointer; font-size: 14px; }
.btn-sm { padding: 4px 10px; background: white; border: 1px solid #ddd; border-radius: 4px; cursor: pointer; font-size: 12px; white-space: nowrap; }
.btn-sm:hover { background: #f5f5f5; }
.modal-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: white; border-radius: 8px; width: 420px; box-shadow: 0 8px 32px rgba(0,0,0,0.2); }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-bottom: 1px solid #eee; font-weight: 600; font-size: 15px; }
.close-btn { background: none; border: none; cursor: pointer; font-size: 20px; color: #999; }
.close-btn:hover { color: #333; }
.modal-body { padding: 20px; }
.form-label { display: block; font-size: 14px; margin-bottom: 8px; color: #555; }
.form-input { width: 100%; padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
.modal-actions { display: flex; justify-content: flex-end; gap: 8px; margin-top: 20px; }
</style>