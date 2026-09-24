<template>
  <div class="recording-page">
    <el-card class="mb-16">
      <template #header>
        <div class="card-header">
          <span>录制列表</span>
          <el-button type="primary" :loading="loading" @click="load">刷新</el-button>
        </div>
      </template>
      <el-table :data="recordings" border stripe v-loading="loading" @row-click="selectRecording">
        <el-table-column prop="recordingId" label="录制ID" width="200" />
        <el-table-column prop="analysisId" label="关联分析ID" width="160" />
        <el-table-column prop="interactionCount" label="交互数" width="120" />
        <el-table-column label="创建时间" width="180">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card v-if="detail">
      <template #header>
        <div class="card-header">
          <span>交互明细 · {{ detail.recordingId }}</span>
          <el-radio-group v-model="filterType" size="small">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="TOOL">TOOL</el-radio-button>
            <el-radio-button label="LLM">LLM</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-table :data="filteredInteractions" border stripe size="small">
        <el-table-column label="#" width="60">
          <template #default="{ $index }">{{ $index + 1 }}</template>
        </el-table-column>
        <el-table-column label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.type === 'TOOL' ? 'primary' : 'warning'" size="small">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="180" />
        <el-table-column label="耗时(ms)" width="100">
          <template #default="{ row }">{{ row.durationMs }}</template>
        </el-table-column>
        <el-table-column label="成功" width="80">
          <template #default="{ row }">
            <el-tag :type="row.success ? 'success' : 'danger'" size="small">{{ row.success ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="时间" width="180">
          <template #default="{ row }">{{ formatTime(row.timestamp) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="expanded = expanded === row ? null : row">
              {{ expanded === row ? '收起' : '查看' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-collapse-transition>
        <div v-if="expanded" class="interaction-detail">
          <el-row :gutter="16">
            <el-col :span="12">
              <h4>入参 (Input)</h4>
              <pre class="json-box">{{ prettyJson(expanded.inputJson) }}</pre>
            </el-col>
            <el-col :span="12">
              <h4>出参 (Output)</h4>
              <pre class="json-box">{{ prettyJson(expanded.outputJson) }}</pre>
            </el-col>
          </el-row>
        </div>
      </el-collapse-transition>
    </el-card>

    <el-empty v-if="!detail" description="点击上方录制查看交互明细" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRecordings, getRecordingDetail } from '@/api/rca'

const loading = ref(false)
const recordings = ref([])
const detail = ref(null)
const filterType = ref('')
const expanded = ref(null)

const filteredInteractions = computed(() => {
  if (!detail.value?.interactions) return []
  if (!filterType.value) return detail.value.interactions
  return detail.value.interactions.filter(i => i.type === filterType.value)
})

function formatTime(t) {
  return t ? String(t).replace('T', ' ') : '-'
}

function prettyJson(str) {
  if (!str) return ''
  if (str === 'null') return 'null'
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch {
    return str
  }
}

async function load() {
  loading.value = true
  try {
    recordings.value = await getRecordings()
  } catch (e) {
    ElMessage.error('加载录制列表失败：' + e.message)
  } finally {
    loading.value = false
  }
}

async function selectRecording(row) {
  try {
    detail.value = await getRecordingDetail(row.recordingId)
    expanded.value = null
  } catch (e) {
    ElMessage.error('加载录制详情失败：' + e.message)
  }
}

onMounted(load)
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.interaction-detail {
  margin-top: 16px;
}
.json-box {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  max-height: 500px;
  overflow: auto;
  font-size: 12px;
  line-height: 1.5;
}
</style>
