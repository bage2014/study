<template>
  <div class="history-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>RCA 分析历史</span>
          <el-button type="primary" :loading="loading" @click="load">刷新</el-button>
        </div>
      </template>

      <el-table :data="items" border stripe v-loading="loading" @row-click="onRowClick">
        <el-table-column prop="analysisId" label="分析ID" width="160" />
        <el-table-column prop="appId" label="应用" width="140" />
        <el-table-column prop="scene" label="场景" width="140" />
        <el-table-column label="结果等级" width="130">
          <template #default="{ row }">
            <el-tag :type="levelType(row.resultLevel)" size="small">{{ row.resultLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="已录制" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.recorded" type="success" size="small">是</el-tag>
            <el-tag v-else type="info" size="small">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click.stop="showDetail(row)">详情</el-button>
            <el-button size="small" type="danger" link @click.stop="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="total > 0"
        class="mb-16"
        style="margin-top: 16px; justify-content: flex-end"
        layout="total, prev, pager, next"
        :total="total"
        :page-size="size"
        :current-page="page + 1"
        @current-change="onPageChange"
      />
    </el-card>

    <el-dialog v-model="detailVisible" title="分析详情" width="90%" top="5vh">
      <div v-if="detail">
        <el-descriptions :column="3" border size="small" class="mb-16">
          <el-descriptions-item label="分析ID">{{ detail.analysisId }}</el-descriptions-item>
          <el-descriptions-item label="应用">{{ detail.request?.appId }}</el-descriptions-item>
          <el-descriptions-item label="场景">{{ detail.request?.scene }}</el-descriptions-item>
          <el-descriptions-item label="结果等级">
            <el-tag :type="levelType(detail.response?.resultLevel)">{{ detail.response?.resultLevel }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="LLM">{{ detail.response?.llmUsed ? '已调用' : '未调用' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatTime(detail.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="告警描述" :span="3">{{ detail.request?.alarmDescription }}</el-descriptions-item>
        </el-descriptions>

        <el-tabs v-model="activeTab">
          <el-tab-pane label="结论" name="conclusions">
            <el-timeline v-if="detail.response?.conclusions?.length">
              <el-timeline-item
                v-for="c in detail.response.conclusions"
                :key="c.hypothesisId"
                :timestamp="`置信度 ${(c.confidence * 100).toFixed(1)}%`"
                :type="c.confirmed ? 'success' : 'primary'"
              >
                <strong>{{ c.title }}</strong>
                <p>{{ c.targetType }}:{{ c.symptom }} · {{ c.targetName }}</p>
                <ul v-if="c.causalChain?.length">
                  <li v-for="(item, idx) in c.causalChain" :key="idx">{{ item }}</li>
                </ul>
              </el-timeline-item>
            </el-timeline>
          </el-tab-pane>
          <el-tab-pane label="阶段轨迹" name="stages">
            <el-table :data="detail.response?.stages" border size="small">
              <el-table-column prop="stage" label="阶段" width="140" />
              <el-table-column prop="status" label="状态" width="100" />
              <el-table-column prop="note" label="备注" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="假设" name="hypotheses">
            <el-table :data="detail.response?.hypotheses" border size="small">
              <el-table-column prop="id" label="ID" width="70" />
              <el-table-column label="目标" width="160">
                <template #default="{ row }">{{ row.targetType }}:{{ row.targetName }}</template>
              </el-table-column>
              <el-table-column prop="symptom" label="症状" width="170" />
              <el-table-column label="置信度" width="100">
                <template #default="{ row }">{{ (row.confidence * 100).toFixed(1) }}%</template>
              </el-table-column>
              <el-table-column prop="desc" label="描述" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="证据" name="evidences">
            <el-table :data="detail.response?.evidences" border size="small">
              <el-table-column prop="id" label="ID" width="70" />
              <el-table-column prop="toolName" label="工具" width="170" />
              <el-table-column prop="targetName" label="目标" />
              <el-table-column label="当前值" width="110">
                <template #default="{ row }">{{ row.value }}</template>
              </el-table-column>
              <el-table-column prop="verdict" label="裁决" width="100" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="录制摘要" name="recording">
            <div v-if="detail.recording">
              <p>录制ID：{{ detail.recording.recordingId }}</p>
              <p>交互数：{{ detail.recording.interactions?.length }}</p>
              <el-table :data="detail.recording.interactions" border size="small" max-height="400">
                <el-table-column label="类型" width="80">
                  <template #default="{ row }">{{ row.type }}</template>
                </el-table-column>
                <el-table-column prop="name" label="名称" width="200" />
                <el-table-column label="耗时(ms)" width="100">
                  <template #default="{ row }">{{ row.durationMs }}</template>
                </el-table-column>
                <el-table-column label="成功" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.success ? 'success' : 'danger'" size="small">{{ row.success ? '是' : '否' }}</el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <el-empty v-else description="该分析未录制" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getHistory, getHistoryDetail, deleteHistory } from '@/api/rca'

const loading = ref(false)
const items = ref([])
const total = ref(0)
const page = ref(0)
const size = ref(10)
const detailVisible = ref(false)
const detail = ref(null)
const activeTab = ref('conclusions')

function levelType(level) {
  return { CONFIRMED: 'success', SUSPECTED: 'warning', INCONCLUSIVE: 'info' }[level] || 'info'
}

function formatTime(t) {
  return t ? String(t).replace('T', ' ') : '-'
}

async function load() {
  loading.value = true
  try {
    const res = await getHistory(page.value, size.value)
    items.value = res.items
    total.value = res.total
  } catch (e) {
    ElMessage.error('加载历史失败：' + e.message)
  } finally {
    loading.value = false
  }
}

async function showDetail(row) {
  try {
    detail.value = await getHistoryDetail(row.analysisId)
    detailVisible.value = true
  } catch (e) {
    ElMessage.error('加载详情失败：' + e.message)
  }
}

function onRowClick(row) {
  showDetail(row)
}

function onPageChange(p) {
  page.value = p - 1
  load()
}

async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除分析 ${row.analysisId}？`, '提示', { type: 'warning' })
    await deleteHistory(row.analysisId)
    ElMessage.success('已删除')
    load()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败：' + e.message)
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
</style>
