<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import http from '../components/http/http.service';

const { t } = useI18n();

// 定义应用版本接口
interface AppVersion {
  id: number;
  version: string;
  releaseDate: string;
  releaseNotes: string;
  downloadUrl: string;
  forceUpdate: boolean;
}

// 定义API响应接口 - 更新以匹配实际的API响应结构
interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
  total: null;
  page: null;
  size: null;
}

interface PaginationData {
  versions: AppVersion[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

// 定义文件上传响应接口
interface UploadResponseData {
  fileId: string;
  originalFileName: string;
  fileName: string;
}

// 定义搜索和分页状态
const keyword = ref('');
const currentPage = ref(0);
const pageSize = ref(10);
const totalElements = ref(0);
const totalPages = ref(0);
const appVersions = ref<AppVersion[]>([]);
const isLoading = ref(false);

// 文件上传相关状态
const selectedFile = ref<File | null>(null);
const fileName = ref('');
const isUploading = ref(false);
const uploadMessage = ref('');
const uploadSuccess = ref(false);

// 查询应用版本列表 - 修改以正确处理嵌套的数据结构
const searchAppVersions = async () => {
  isLoading.value = true;
  try {
    const response = await http.get<ApiResponse<PaginationData>>('/app/versions', {
      params: {
        keyword: keyword.value,
        page: currentPage.value,
        size: pageSize.value
      }
    });
    
    // 处理响应数据 - 正确访问嵌套的数据结构
    if (response && response.data) {
      appVersions.value = response.data.versions || [];
      totalElements.value = response.data.totalElements || 0;
      totalPages.value = response.data.totalPages || 0;
      currentPage.value = response.data.currentPage || 0;
      pageSize.value = response.data.pageSize || 10;
    } else {
      appVersions.value = [];
      totalElements.value = 0;
      totalPages.value = 0;
      currentPage.value = 0;
      pageSize.value = 10;
    }
  } catch (error) {
    console.error('Failed to fetch app versions:', error);
    // 发生错误时重置数据
    appVersions.value = [];
    totalElements.value = 0;
    totalPages.value = 0;
    currentPage.value = 0;
    pageSize.value = 10;
  } finally {
    isLoading.value = false;
  }
};

// 处理文件选择
const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    const file = input.files[0];
    // 检查文件类型是否为.apk
    selectedFile.value = file;
    fileName.value = file.name;
    uploadMessage.value = '';
    
  }
};

// 上传文件
const uploadFile = async () => {
  if (!selectedFile.value) {
    uploadMessage.value = t('message.selectFileFirst');
    uploadSuccess.value = false;
    return;
  }

  isUploading.value = true;
  uploadMessage.value = '';
  
  try {
    // 创建FormData对象
    const formData = new FormData();
    formData.append('file', selectedFile.value);
    
    // 发送文件上传请求 - 修改类型定义以匹配实际响应格式
    const response = await http.post<ApiResponse<UploadResponseData>>('/app/upload', formData, { 
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    
    // 处理上传成功 - 修改以正确获取message
    uploadMessage.value = response.message || t('message.fileUploadSuccess');
    uploadSuccess.value = true;
    
    // 清空选择的文件
    selectedFile.value = null;
    fileName.value = '';
    
    // 重新加载应用版本列表
    searchAppVersions();
  } catch (error) {
    console.error('Failed to upload file:', error);
    uploadMessage.value = t('message.fileUploadFailed');
    uploadSuccess.value = false;
  } finally {
    isUploading.value = false;
  }
};

// 切换页码
const changePage = (page: number) => {
  currentPage.value = page;
  searchAppVersions();
};

// 切换每页显示数量
const changeSize = (size: number) => {
  pageSize.value = size;
  currentPage.value = 0; // 重置到第一页
  searchAppVersions();
};

// 初始加载数据
onMounted(() => {
  searchAppVersions();
});
</script>

<template>
  <div class="app-list-container">
    <h1>{{ t('menu.appList') }}</h1>
    
    <!-- 文件上传区域 -->
    <div class="upload-section">
      <div class="file-upload-container">
        <input
          type="file"
          id="fileUpload"
          @change="handleFileChange"
          style="display: none;"
        />
        <label for="fileUpload" class="browse-button">
          {{ t('button.browse') }}
        </label>
        <span class="file-name">
          {{ fileName || t('placeholder.noFileSelected') }}
        </span>
        <button 
          class="upload-button"
          @click="uploadFile"
          :disabled="isUploading"
        >
          {{ isUploading ? t('button.searching') : t('button.upload') }}
        </button>
      </div>
      
      <!-- 上传消息提示 -->
      <div v-if="uploadMessage" :class="['upload-message', uploadSuccess ? 'success' : 'error']">
        {{ uploadMessage }}
      </div>
    </div>
    
    <!-- 搜索和分页控制 -->
    <div class="search-controls">
      <div class="search-input-group">
        <input
          type="text"
          v-model="keyword"
          :placeholder="t('placeholder.searchAppVersion')"
          @keyup.enter="searchAppVersions()"
        />
        <button @click="searchAppVersions()">
          {{ t('button.search') }}
        </button>
      </div>
      
      <div class="page-size-selector">
        <label for="pageSize">{{ t('label.pageSize') }}:</label>
        <select id="pageSize" v-model="pageSize" @change="changeSize(pageSize)">
          <option value="5">5</option>
          <option value="10">10</option>
          <option value="20">20</option>
          <option value="50">50</option>
        </select>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="isLoading" class="loading">
      <span>{{ t('message.loading') }}...</span>
    </div>
    
    <!-- 应用版本列表 -->
    <div v-else-if="appVersions.length > 0" class="app-versions-grid">
      <div v-for="version in appVersions" :key="version.id" class="app-version-card">
        <div class="app-version-header">
          <div class="version-info">
            <h3>{{ version.version }}</h3>
            <span v-if="version.forceUpdate" class="force-update-badge">
              {{ t('label.forceUpdate') }}
            </span>
          </div>
          <span class="release-date">{{ version.releaseDate }}</span>
        </div>
        <div class="release-notes">
          <p>{{ version.releaseNotes }}</p>
        </div>
        <div class="app-version-footer">
          <a v-if="version.downloadUrl" :href="version.downloadUrl" target="_blank">
            {{ t('button.download') }}
          </a>
          <span v-else>{{ t('message.noDownloadUrl') }}</span>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-state">
      <p>{{ t('message.noAppVersionsFound') }}</p>
    </div>
    
    <!-- 分页控件 -->
    <div v-if="totalElements > 0" class="pagination">
      <button
        :disabled="currentPage <= 0"
        @click="changePage(currentPage - 1)"
      >
        {{ t('button.prevPage') }}
      </button>
      
      <span class="page-info">
        {{ currentPage + 1 }} / {{ totalPages }}
      </span>
      
      <button
        :disabled="currentPage >= totalPages - 1"
        @click="changePage(currentPage + 1)"
      >
        {{ t('button.nextPage') }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.app-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}

/* 文件上传区域样式 */
.upload-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background-color: var(--bg-light);
  border-radius: var(--radius);
  border: 1px solid var(--border-color);
}

.file-upload-container {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.browse-button,
.upload-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition);
}

.browse-button:hover,
.upload-button:hover:not(:disabled) {
  background-color: var(--primary-dark);
}

.upload-button:disabled {
  background-color: var(--bg-light);
  cursor: not-allowed;
  opacity: 0.6;
}

.file-name {
  flex: 1;
  min-width: 200px;
  padding: 0.75rem;
  background-color: var(--bg-color);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  color: var(--text-color);
}

.upload-message {
  padding: 0.75rem;
  border-radius: var(--radius);
  font-size: 0.9rem;
}

.upload-message.success {
  background-color: #f6ffed;
  color: #389e0d;
  border: 1px solid #b7eb8f;
}

.upload-message.error {
  background-color: #fff2f0;
  color: #cf1322;
  border: 1px solid #ffccc7;
}

.search-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.search-input-group {
  display: flex;
  gap: 0.5rem;
  flex: 1;
  min-width: 300px;
}

.search-input-group input {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-size: 1rem;
}

.search-input-group button,
.pagination button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition);
}

.search-input-group button:hover,
.pagination button:hover:not(:disabled) {
  background-color: var(--primary-dark);
}

.pagination button:disabled {
  background-color: var(--bg-light);
  cursor: not-allowed;
  opacity: 0.6;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.page-size-selector select {
  padding: 0.5rem;
  border-radius: var(--radius);
  border: 1px solid var(--border-color);
  background-color: var(--bg-color);
  color: var(--text-color);
}

.app-versions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.app-version-card {
  background-color: var(--bg-color);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  padding: 1.5rem;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.app-version-card:hover {
  box-shadow: var(--shadow-hover);
  transform: translateY(-2px);
}

.app-version-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.version-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.version-info h3 {
  margin: 0;
  font-size: 1.25rem;
  color: var(--text-color);
}

.force-update-badge {
  background-color: #ff4d4f;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.release-date {
  font-size: 0.9rem;
  color: var(--text-light);
}

.release-notes {
  margin-bottom: 1rem;
  line-height: 1.6;
  color: var(--text-color);
}

.app-version-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid var(--border-color);
  padding-top: 1rem;
}

.app-version-footer a {
  color: var(--primary-color);
  font-weight: 500;
  text-decoration: none;
}

.app-version-footer a:hover {
  text-decoration: underline;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
}

.page-info {
  font-size: 1rem;
  color: var(--text-color);
}

.loading,
.empty-state {
  text-align: center;
  padding: 4rem;
  color: var(--text-light);
}
</style>