<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchLogCount, fetchSessions } from '../api/grayflow.js'

const router = useRouter()
const logCount = ref('—')
const sessionCount = ref('—')

onMounted(async () => {
  try {
    const [logsResp, sessions] = await Promise.all([
      fetchLogCount(),
      fetchSessions()
    ])
    logCount.value = logsResp.totalElements ?? '—'
    sessionCount.value = Array.isArray(sessions) ? sessions.length : '—'
  } catch {
    // 保持 — 展示，不阻断页面
  }
})
</script>

<template>
  <div>
    <h2 class="page-title">概览</h2>
    <div class="cards">
      <div class="card">
        <div class="card-value">{{ logCount }}</div>
        <div class="card-label">日志总条数</div>
      </div>
      <div class="card">
        <div class="card-value">{{ sessionCount }}</div>
        <div class="card-label">回放会话总数</div>
      </div>
    </div>
    <div class="actions">
      <button class="btn" @click="router.push('/logs')">查看日志</button>
      <button class="btn" @click="router.push('/replay')">查看回放</button>
    </div>
  </div>
</template>

<style scoped>
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 24px; }
.cards { display: flex; gap: 16px; margin-bottom: 24px; flex-wrap: wrap; }
.card { background: white; border-radius: 8px; padding: 24px 32px; box-shadow: 0 1px 4px rgba(0,0,0,0.08); min-width: 160px; }
.card-value { font-size: 36px; font-weight: 700; color: #1a1a2e; }
.card-label { font-size: 14px; color: #666; margin-top: 6px; }
.actions { display: flex; gap: 12px; }
.btn { padding: 10px 24px; background: #1a1a2e; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; }
.btn:hover { background: #2d2d52; }
</style>