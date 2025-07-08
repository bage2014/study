<template>
  <div>
    <h1>视频列表</h1>
    <el-row justify="end">
      <el-col>
        <el-input v-model="keyword" placeholder="请输入搜索关键词" @keyup.enter="fetchData()" style="width: 100px;" />
      </el-col>
      <el-col>
        <el-button @click="fetchData()">搜索</el-button>
      </el-col>
    </el-row>
    <el-table :data="videoList" style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column label="Logo">
        <template #default="{ row }">
          <img :src="row.logo.trim()" alt="Logo" style="width: 50px; height: 50px;">
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button @click="openDetail(row)">查看详情</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="action" label="播放">
        <template #default="{ row }">
          // 修复：传递正确的参数
          <el-button @click="playVideo(row.url, row.name)">播放</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="fetchData"
      :current-page="number + 1"
      :page-size="size"
      :total="totalElements"
      layout="prev, pager, next"
    />
    <el-dialog v-model="dialogVisible" title="视频详情" width="80%" height="60%" center>
      <div class="detail-container">
        <video ref="videoPlayer" controls :src="currentVideo.url.trim()"></video>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { request } from '@/api/request'; // 新增导入

interface VideoItem {
  id: number;
  name: string;
  url: string;
  // 其他视频相关字段
}

// 新增响应接口定义
interface VideoListResponse {
  content: VideoItem[];
  totalElements: number;
}

import API_BASE_URL from '../api/config';
import Hls from 'hls.js';
import { useRouter } from 'vue-router';

const videoList = ref([]);
const pageable = ref({});
const totalPages = ref(0);
const totalElements = ref(0);
const last = ref(false);
const size = ref(0);
const number = ref(0);
const dialogVisible = ref(false);
const currentVideo = ref({});
const videoPlayer = ref(null);
const keyword = ref('');

const fetchData = async (page = 1) => {
  try {
    // 修复：使用request<T>替换fetch
    const data = await request<VideoListResponse>('/m3u/query', {
      method: 'GET',
      params: {
        size: 5,
        keyword: keyword.value,
        page: page - 1
      }
    });
    videoList.value = data.content;
    totalElements.value = data.totalElements;
    size.value = data.size;
    number.value = data.number;
  } catch (error) {
    console.error('数据获取失败:', error);
  }
};

const openDetail = (row) => {
  currentVideo.value = {
    ...row,
    logo: row.logo.trim(),
    url: row.url.trim()
  };
  dialogVisible.value = true;
  if (videoPlayer.value && Hls.isSupported()) {
    const video = videoPlayer.value;
    const hls = new Hls();
    hls.loadSource(currentVideo.value.url.trim());
    hls.attachMedia(video);
    hls.on(Hls.Events.MANIFEST_PARSED, () => {
      video.play();
    });
  } else if (videoPlayer.value && videoPlayer.value.canPlayType('application/vnd.apple.mpegurl')) {
    videoPlayer.value.src = currentVideo.value.url.trim();
    videoPlayer.value.addEventListener('loadedmetadata', () => {
      videoPlayer.value.play();
    });
  }
};

const router = useRouter();

// 修复：添加缺失的loading变量定义
const loading = ref(false);

const playVideo = (url, name = '') => {
  const newWindow = window.open('', '_blank');
  if (newWindow) {
    newWindow.location.href = `/video-player?url=${encodeURIComponent(url)}&name=${encodeURIComponent(name || '视频播放')}`;
  }
};

// 移除未使用的fetchVideos函数
const fetchVideos = async () => {
  try {
    loading.value = true;
    // 替换fetch为request<T>
    const data = await request<VideoListResponse>('/m3u/query', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    videoList.value = data.content;
    total.value = data.totalElements;
  } catch (error) {
    console.error('获取视频列表失败:', error);
    ElMessage.error('获取视频列表失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.detail-container {
  padding: 10px;
  margin: 0 auto;
  text-align: center;
}
.el-dialog__header {
  background-color: #f0f2f5;
}
.el-dialog__title {
  font-weight: bold;
}
</style>