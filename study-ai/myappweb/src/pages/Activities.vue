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

<script setup lang="ts">
// 修复：添加 computed 导入
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { request } from '@/api/request';

interface ActivityItem {
  id: number;
  time: string;
  description: string;
  creator: string;
}

// 新增响应接口定义
interface ActivitiesResponse {
  content: ActivityItem[];
  totalElements: number;
}

const activities = ref<ActivityItem[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const fetchActivities = async () => {
  try {
    // 替换fetch为request<T>
    const data = await request<ActivitiesResponse>('/activities', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    activities.value = data.content;
    total.value = data.totalElements;
  } catch (error) {
    console.error('获取个人动态信息失败:', error);
    ElMessage.error('获取个人动态信息失败，请稍后重试');
  }
};

// 计算属性现在可以正常工作
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