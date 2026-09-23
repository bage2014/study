<template>
  <div class="health-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>服务健康检查</span>
          <el-button type="primary" :loading="loading" @click="load">刷新</el-button>
        </div>
      </template>

      <el-result
        v-if="data"
        :icon="data.status === 'UP' ? 'success' : 'error'"
        :title="data.status === 'UP' ? '服务运行中' : '服务异常'"
        :sub-title="`服务名：${data.service}`"
      >
        <template #extra>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="status">{{ data.status }}</el-descriptions-item>
            <el-descriptions-item label="service">{{ data.service }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </el-result>

      <el-empty v-else-if="!loading" description="点击刷新检查后端服务健康状态" />
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { health } from '@/api/rca'

const data = ref(null)
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    data.value = await health()
  } catch (e) {
    ElMessage.error('健康检查失败：' + (e.message || '后端服务不可达'))
    data.value = null
  } finally {
    loading.value = false
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
