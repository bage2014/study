<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

// 定义接口保持不变
interface ChannelUrl {
  title: string
  url: string
}

interface Channel {
  title: string
  logo: string
  channelUrls: ChannelUrl[]
}

interface TvSearchResponse {
  code: number
  message: string
  data: { 
    channels: Channel[]
    totalElements: number
    totalPages: number
    currentPage: number
    pageSize: number
  }
}

const { t } = useI18n()
const router = useRouter()
const channels = ref<Channel[]>([])
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(9)
const total = ref(0)
const loading = ref(false)

// 其他函数保持不变
const searchTvChannels = async () => {
  loading.value = true
  try {
    const response: TvSearchResponse = await request({
      url: '/tv/search',
      method: 'GET',
      params: { 
        keyword: keyword.value,
        page: currentPage.value - 1,
        size: pageSize.value 
      }
    })
    
    if (response && response.code === 200 && response.data) {
      channels.value = response.data.channels
      total.value = response.data.totalElements
    } else {
      console.error(response?.message || '获取频道列表失败')
    }
  } catch (error) {
    console.error('获取TV频道失败:', error)
  } finally {
    loading.value = false
  }
}

const changePage = (page: number) => {
  currentPage.value = page
  searchTvChannels()
}

const changeSize = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  searchTvChannels()
}

// 添加导航到播放器页面的函数
const navigateToPlayer = (channelTitle: string, url: string) => {
  router.push({
    name: 'tv-player',
    query: { 
      title: channelTitle,
      url: url 
    }
  })
}

onMounted(() => {
  searchTvChannels()
})
</script>

<template>
  <div class="tv-list-page">
    <!-- 头部和搜索部分保持不变 -->
    <div class="tv-list-header">
      <h2>{{ t('menu.tvList') }}</h2>
      <div class="search-container">
        <input 
          type="text"
          v-model="keyword"
          placeholder="搜索关键词"
          @keyup.enter="searchTvChannels"
        >
        <button @click="searchTvChannels" :disabled="loading">
          {{ loading ? '搜索中...' : '搜索' }}
        </button>
      </div>
    </div>
    
    <!-- 频道列表部分保持不变，仅修改URL点击事件 -->
    <div class="tv-channels-grid">
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>
      
      <div v-else-if="channels.length === 0" class="empty-state">
        {{ t('message.noChannelsFound') }}
      </div>
      
      <div v-else class="channels-container">
        <div v-for="(channel, index) in channels" :key="index" class="channel-card">
          <div class="channel-logo">
            <img :src="channel.logo" :alt="channel.title" onerror="this.src='data:image/svg+xml;charset=UTF-8,%3csvg xmlns%3d%22http%3a%2f%2fwww.w3.org%2f2000%2fsvg%22 width%3d%22150%22 height%3d%22150%22 viewBox%3d%220 0 150 150%22 fill%3d%22none%22%3e%3crect width%3d%22150%22 height%3d%22150%22 fill%3d%22%23f0f0f0%22%3e%3c%2frect%3e%3cpath d%3d%22M101.579 30.8241L75 57.1398L48.4211 30.8241H101.579Z%22 stroke%3d%22%23ccc%22 stroke-width%3d%222%22%3e%3c%2fpath%3e%3cpath d%3d%22M48.4211 30.8241V119.176L75 92.8602L101.579 119.176V30.8241H48.4211Z%22 stroke%3d%22%23ccc%22 stroke-width%3d%222%22%3e%3c%2fpath%3e%3c%2fsvg%3e'">
          </div>
          <div class="channel-info">
            <h3>{{ channel.title }}</h3>
            <div class="channel-urls">
              <div v-for="(url, urlIndex) in channel.channelUrls" :key="urlIndex" class="url-item">
                <!-- 修改这里，使用@click代替href -->
                <a href="#" @click.prevent="navigateToPlayer(channel.title, url.url)">
                  {{ url.title }}
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页部分保持不变 -->
    <div v-if="total > 0" class="pagination-container">
      <ElPagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="changeSize"
        @current-change="changePage"
      />
    </div>
  </div>
</template>

<!-- 样式部分保持不变 -->
<style scoped>
.tv-list-page {
  padding: 2rem;
}

.tv-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.tv-list-header h2 {
  color: var(--text-color);
  margin: 0;
}

.search-container {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.search-container input {
  padding: 0.5rem 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  min-width: 200px;
}

.search-container button {
  padding: 0.5rem 1rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-container button:hover:not(:disabled) {
  background-color: var(--primary-dark);
}

.search-container button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.tv-channels-grid {
  margin-bottom: 2rem;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  color: var(--text-light);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--border-color);
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 4rem;
  color: var(--text-light);
}

.channels-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.channel-card {
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: box-shadow 0.3s;
}

.channel-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.channel-logo {
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--bg-light);
  overflow: hidden;
}

.channel-logo img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.channel-info {
  padding: 1rem;
}

.channel-info h3 {
  margin: 0 0 1rem 0;
  color: var(--text-color);
  font-size: 1.1rem;
}

.channel-urls {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.url-item a {
  color: var(--primary-color);
  text-decoration: none;
  display: inline-block;
  padding: 0.25rem 0;
}

.url-item a:hover {
  color: var(--primary-dark);
  text-decoration: underline;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .tv-list-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-container {
    justify-content: center;
  }
  
  .pagination-container {
    justify-content: center;
  }
}
</style>