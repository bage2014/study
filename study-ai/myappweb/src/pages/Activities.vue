<template>
  <div class="activities-container">
    <el-row :gutter="20">
        <el-col :span="24" v-for="activity in currentActivities" :key="activity.id">
          <div class="activity-item" @click="handleViewDetails(activity.id)">
            <div class="time-top-left">{{ activity.time }}</div>
            <p><strong></strong> {{ activity.description }}</p>
            <div class="creator-bottom-right">{{ activity.creator }}</div>
          </div>
        </el-col>
    </el-row>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import API_BASE_URL from '../api/config';

const activities = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const currentActivities = computed(() => {
  return activities.value;
});

const handleViewDetails = (id) => {
  ElMessage.info(`查看活动 ID ${id} 的详情`);
  // 这里可以添加跳转到详情页的逻辑
};

const handleCurrentChange = async (val) => {
  currentPage.value = val;
  await fetchActivities();
};

const fetchActivities = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}activities?page=${currentPage.value - 1}&size=${pageSize.value}`);
    if (!response.ok) {
      throw new Error('网络响应失败');
    }
    const data = await response.json();
    activities.value = data.content;
    total.value = data.totalElements;
  } catch (error) {
    console.error('获取个人动态信息失败:', error);
    ElMessage.error('获取个人动态信息失败，请稍后重试');
  }
};

onMounted(async () => {
  await fetchActivities();
});
</script>

<style scoped>
.activities-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.float-right {
  float: right;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
.custom-card {
  display: none;
}
.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.time-top-left {
  position: static;
  color: #666;
  font-size: 12px;
}
.creator-bottom-right {
  position: static;
  color: #666;
  font-size: 12px;
}
</style>