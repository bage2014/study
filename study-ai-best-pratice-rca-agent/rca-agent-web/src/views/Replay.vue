<template>
  <div class="replay-page">
    <el-card class="mb-16">
      <template #header>
        <div class="card-header">
          <span>RCA 回放</span>
          <el-button type="primary" :loading="loading" @click="loadHistory">刷新历史</el-button>
        </div>
      </template>

      <el-form :inline="true">
        <el-form-item label="选择历史记录">
          <el-select v-model="selectedId" placeholder="选择一条已录制的分析" filterable style="width: 320px">
            <el-option
              v-for="h in historyList"
              :key="h.analysisId"
              :label="`${h.analysisId} · ${h.appId} · ${h.scene} · ${h.resultLevel}`"
              :value="h.analysisId"
              :disabled="!h.recorded"
            >
              <span>{{ h.analysisId }}</span>
              <el-tag size="small" :type="h.recorded ? 'success' : 'info'" style="margin-left: 8px">
                {{ h.recorded ? '可回放' : '未录制' }}
              </el-tag>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="replaying" :disabled="!selectedId" @click="onReplay">
            执行回放
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row v-if="original || replayed" :gutter="16">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>原始结果</span>
              <el-tag v-if="original" :type="levelType(original.resultLevel)">{{ original.resultLevel }}</el-tag>
            </div>
          </template>
          <div v-if="original">
            <p>分析ID：{{ original.analysisId }}</p>
            <p>结论数：{{ original.conclusions?.length }}</p>
            <el-divider />
            <el-timeline>
              <el-timeline-item
                v-for="c in original.conclusions"
                :key="c.hypothesisId"
                :timestamp="`置信度 ${(c.confidence * 100).toFixed(1)}%`"
                :type="c.confirmed ? 'success' : 'primary'"
              >
                <strong>{{ c.title }}</strong>
                <p>{{ c.targetType }}:{{ c.symptom }}</p>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>回放结果</span>
              <el-tag v-if="replayed" :type="levelType(replayed.resultLevel)">{{ replayed.resultLevel }}</el-tag>
            </div>
          </template>
          <div v-if="replayed">
            <p>分析ID：{{ replayed.analysisId }}</p>
            <p>结论数：{{ replayed.conclusions?.length }}</p>
            <el-divider />
            <el-timeline>
              <el-timeline-item
                v-for="c in replayed.conclusions"
                :key="c.hypothesisId"
                :timestamp="`置信度 ${(c.confidence * 100).toFixed(1)}%`"
                :type="c.confirmed ? 'success' : 'primary'"
              >
                <strong>{{ c.title }}</strong>
                <p>{{ c.targetType }}:{{ c.symptom }}</p>
              </el-timeline-item>
            </el-timeline>
          </div>
          <el-empty v-else description="选择历史记录并点击回放" />
        </el-card>
      </el-col>
    </el-row>

    <el-card v-if="original && replayed" class="mb-16">
      <template #header>
        <div class="card-header">
          <span>一致性对比</span>
          <el-tag :type="isConsistent ? 'success' : 'danger'">
            {{ isConsistent ? '一致' : '不一致' }}
          </el-tag>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="结果等级">
          {{ original.resultLevel }} → {{ replayed.resultLevel }}
        </el-descriptions-item>
        <el-descriptions-item label="结论数">
          {{ original.conclusions?.length }} → {{ replayed.conclusions?.length }}
        </el-descriptions-item>
        <el-descriptions-item label="证据数">
          {{ original.evidences?.length }} → {{ replayed.evidences?.length }}
        </el-descriptions-item>
        <el-descriptions-item label="假设数">
          {{ original.hypotheses?.length }} → {{ replayed.hypotheses?.length }}
        </el-descriptions-item>
        <el-descriptions-item label="Top1 假设" :span="2">
          {{ top1Info(original) }} → {{ top1Info(replayed) }}
        </el-descriptions-item>
        <el-descriptions-item label="Top1 置信度" :span="2">
          {{ top1Confidence(original) }} → {{ top1Confidence(replayed) }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getHistory, getHistoryDetail, replay } from '@/api/rca'

const loading = ref(false)
const replaying = ref(false)
const historyList = ref([])
const selectedId = ref('')
const original = ref(null)
const replayed = ref(null)

const isConsistent = computed(() => {
  if (!original.value || !replayed.value) return false
  const o = original.value, r = replayed.value
  if (o.resultLevel !== r.resultLevel) return false
  if ((o.conclusions?.length || 0) !== (r.conclusions?.length || 0)) return false
  for (let i = 0; i < (o.conclusions?.length || 0); i++) {
    const oc = o.conclusions[i], rc = r.conclusions[i]
    if (oc.hypothesisId !== rc.hypothesisId) return false
    if (oc.confidence !== rc.confidence) return false
  }
  return true
})

function levelType(level) {
  return { CONFIRMED: 'success', SUSPECTED: 'warning', INCONCLUSIVE: 'info' }[level] || 'info'
}

function top1Info(resp) {
  const c = resp?.conclusions?.[0]
  return c ? `${c.hypothesisId} ${c.targetType}:${c.symptom}` : '-'
}

function top1Confidence(resp) {
  const c = resp?.conclusions?.[0]
  return c ? (c.confidence * 100).toFixed(1) + '%' : '-'
}

async function loadHistory() {
  loading.value = true
  try {
    const res = await getHistory(0, 100)
    historyList.value = res.items.filter(h => h.recorded)
  } catch (e) {
    ElMessage.error('加载历史失败：' + e.message)
  } finally {
    loading.value = false
  }
}

async function onReplay() {
  replaying.value = true
  original.value = null
  replayed.value = null
  try {
    const detail = await getHistoryDetail(selectedId.value)
    original.value = detail.response
    const result = await replay(selectedId.value)
    replayed.value = result
    ElMessage.success(isConsistent.value ? '回放成功，结果一致' : '回放完成，结果存在差异')
  } catch (e) {
    ElMessage.error('回放失败：' + (e.response?.data?.message || e.message))
  } finally {
    replaying.value = false
  }
}

onMounted(loadHistory)
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
