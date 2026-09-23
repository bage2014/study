<template>
  <div class="analyze-page">
    <el-card class="mb-16">
      <template #header>
        <div class="card-header">
          <span>RCA 分析请求</span>
          <el-tag type="info" size="small">六阶段管道：Plan → BroadScan → BroadAnalyze → FocusedScan → FocusedAnalyze → GlobalReact</el-tag>
        </div>
      </template>

      <el-form :model="form" label-width="120px" label-position="right">
        <el-form-item label="应用标识">
          <el-input v-model="form.appId" placeholder="如 order-service" />
        </el-form-item>
        <el-form-item label="告警描述">
          <el-input
            v-model="form.alarmDescription"
            type="textarea"
            :rows="3"
            placeholder="描述告警现象，如：下单接口大量超时，P99 延迟飙升，疑似慢查询"
          />
        </el-form-item>
        <el-form-item label="场景">
          <el-select v-model="form.scene" placeholder="选择 Mock 场景">
            <el-option
              v-for="opt in sceneOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="启用 LLM">
          <el-switch v-model="form.enableLlm" />
          <span class="form-tip">关闭时全链路走确定性规则，失败软降级</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onAnalyze">执行分析</el-button>
          <el-button :loading="loading" @click="onAnalyzeMock">Mock 场景一键分析</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="result" class="mb-16">
      <template #header>
        <div class="card-header">
          <span>分析结果</span>
          <div>
            <el-tag :type="levelTagType" size="large">{{ result.resultLevel }}</el-tag>
            <el-tag size="large" type="info" style="margin-left: 8px">
              LLM: {{ result.llmUsed ? '已调用' : '未调用' }}
            </el-tag>
          </div>
        </div>
      </template>

      <el-descriptions :column="3" border size="small" class="mb-16">
        <el-descriptions-item label="分析ID">{{ result.analysisId }}</el-descriptions-item>
        <el-descriptions-item label="应用">{{ result.appId }}</el-descriptions-item>
        <el-descriptions-item label="场景">{{ result.scene }}</el-descriptions-item>
        <el-descriptions-item label="分析时间">{{ formatTime(result.analysisTime) }}</el-descriptions-item>
        <el-descriptions-item label="告警时间">{{ formatTime(result.alarmTime) }}</el-descriptions-item>
        <el-descriptions-item label="告警描述" :span="3">{{ result.alarmDescription }}</el-descriptions-item>
      </el-descriptions>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="结论与建议" name="conclusions">
          <el-timeline v-if="result.conclusions?.length">
            <el-timeline-item
              v-for="c in result.conclusions"
              :key="c.hypothesisId"
              :timestamp="`置信度 ${(c.confidence * 100).toFixed(1)}%`"
              :type="c.confirmed ? 'success' : 'primary'"
            >
              <el-card shadow="hover">
                <div class="conclusion-title">
                  <el-tag size="small">{{ c.rank }}</el-tag>
                  <strong>{{ c.title }}</strong>
                  <el-tag size="small" type="warning" style="margin-left: 8px">{{ c.targetType }}:{{ c.symptom }}</el-tag>
                </div>
                <p class="conclusion-target">目标：{{ c.targetName }}</p>
                <p v-if="c.rationale">解读：{{ c.rationale }}</p>
                <p v-if="c.counterfactual">反事实：{{ c.counterfactual }}</p>
                <div v-if="c.causalChain?.length">
                  <div class="chain-label">因果链：</div>
                  <ul>
                    <li v-for="(item, idx) in c.causalChain" :key="idx">{{ item }}</li>
                  </ul>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>

          <el-divider content-position="left">处置建议</el-divider>
          <el-row :gutter="16" v-if="result.suggestions">
            <el-col :span="8" v-for="block in suggestionBlocks" :key="block.key">
              <el-card shadow="never" class="suggestion-card">
                <template #header>
                  <span>{{ block.label }}</span>
                </template>
                <ul>
                  <li v-for="(s, idx) in result.suggestions[block.key]" :key="idx">{{ s }}</li>
                </ul>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="阶段轨迹" name="stages">
          <el-table :data="result.stages" border stripe size="small">
            <el-table-column prop="stage" label="阶段" width="140" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'SUCCESS' ? 'success' : 'danger'" size="small">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="假设全景" name="hypotheses">
          <el-table :data="result.hypotheses" border stripe size="small">
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column label="目标" width="160">
              <template #default="{ row }">{{ row.targetType }}:{{ row.targetName }}</template>
            </el-table-column>
            <el-table-column prop="symptom" label="症状" width="170" />
            <el-table-column label="先验" width="90">
              <template #default="{ row }">{{ (row.priorConfidence * 100).toFixed(0) }}%</template>
            </el-table-column>
            <el-table-column label="置信度" width="100">
              <template #default="{ row }">
                <el-tag :type="row.confidence >= 0.6 ? 'success' : row.confidence >= 0.35 ? 'warning' : 'info'" size="small">
                  {{ (row.confidence * 100).toFixed(1) }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="desc" label="因果描述" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="时间线" name="timeline">
          <el-timeline>
            <el-timeline-item
              v-for="(t, idx) in result.timeline"
              :key="idx"
              :timestamp="formatTime(t.time)"
              :type="kindType(t.kind)"
            >
              <strong>{{ t.kind }}</strong> · {{ t.source }}
              <p>{{ t.description }}</p>
              <p class="muted">关联目标：{{ t.relatedTarget }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>

        <el-tab-pane label="证据" name="evidences">
          <el-table :data="result.evidences" border stripe size="small">
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="hypothesisId" label="假设" width="90" />
            <el-table-column prop="phase" label="阶段" width="100" />
            <el-table-column prop="toolName" label="工具" width="170" />
            <el-table-column prop="targetName" label="目标" />
            <el-table-column label="当前值" width="110">
              <template #default="{ row }">{{ row.value }}</template>
            </el-table-column>
            <el-table-column label="偏离" width="100">
              <template #default="{ row }">{{ row.deviationRatio }}x</template>
            </el-table-column>
            <el-table-column prop="verdict" label="裁决" width="100">
              <template #default="{ row }">
                <el-tag :type="verdictType(row.verdict)" size="small">{{ row.verdict }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-empty v-if="!result" description="提交请求后在此展示分析结果" />
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { analyze, analyzeMock } from '@/api/rca'

const sceneOptions = [
  { value: 'default', label: 'default（无异常）' },
  { value: 'db_slow_query', label: 'db_slow_query（慢查询）' },
  { value: 'mq_lag', label: 'mq_lag（MQ 堆积）' },
  { value: 'app_resource', label: 'app_resource（资源耗尽）' },
  { value: 'supplier_failure', label: 'supplier_failure（供应商故障）' },
  { value: 'high_error', label: 'high_error（高错误率）' },
  { value: 'redis', label: 'redis（Redis 异常）' }
]

const form = reactive({
  appId: 'order-service',
  alarmDescription: '',
  scene: 'default',
  enableLlm: false
})

const loading = ref(false)
const result = ref(null)
const activeTab = ref('conclusions')

const levelTagType = {
  CONFIRMED: 'success',
  SUSPECTED: 'warning',
  INCONCLUSIVE: 'info'
}

const suggestionBlocks = [
  { key: 'mitigation', label: '止血' },
  { key: 'remediation', label: '根治' },
  { key: 'prevention', label: '预防' }
]

function formatTime(t) {
  if (!t) return '-'
  return String(t).replace('T', ' ')
}

function kindType(kind) {
  return { ALERT: 'danger', CHANGE: 'warning', SIGNAL: 'primary' }[kind] || ''
}

function verdictType(v) {
  return { SUPPORT: 'success', REFUTE: 'danger', NEUTRAL: 'info' }[v] || ''
}

async function onAnalyze() {
  if (!form.alarmDescription?.trim()) {
    ElMessage.warning('请填写告警描述')
    return
  }
  loading.value = true
  try {
    result.value = await analyze({ ...form })
    ElMessage.success('分析完成')
  } catch (e) {
    ElMessage.error('分析失败：' + (e.response?.data?.message || e.message))
  } finally {
    loading.value = false
  }
}

async function onAnalyzeMock() {
  loading.value = true
  try {
    result.value = await analyzeMock(form.scene, form.enableLlm)
    ElMessage.success('Mock 场景分析完成')
  } catch (e) {
    ElMessage.error('分析失败：' + (e.response?.data?.message || e.message))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.form-tip {
  margin-left: 12px;
  color: #909399;
  font-size: 12px;
}
.conclusion-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}
.conclusion-target {
  color: #606266;
}
.chain-label {
  font-weight: 600;
  margin: 8px 0 4px;
}
.suggestion-card {
  height: 100%;
}
.muted {
  color: #909399;
  font-size: 12px;
}
</style>
