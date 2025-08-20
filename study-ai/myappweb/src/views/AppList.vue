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

// 定义API响应接口
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
// 添加上传文件所需的三个字段
const version = ref('');
const forceUpdate = ref(false);
const releaseNotes = ref('');
// 添加弹窗状态控制变量
const showUploadModal = ref(false);

// 查询应用版本列表
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
    
    // 处理响应数据
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
  
  if (!version.value.trim()) {
    uploadMessage.value = '请输入版本号';
    uploadSuccess.value = false;
    return;
  }

  isUploading.value = true;
  uploadMessage.value = '';
  
  try {
    // 创建FormData对象并添加所有字段
    const formData = new FormData();
    formData.append('file', selectedFile.value);
    formData.append('version', version.value.trim());
    formData.append('forceUpdate', String(forceUpdate.value));
    if (releaseNotes.value.trim()) {
      formData.append('releaseNotes', releaseNotes.value.trim());
    }
    
    // 发送文件上传请求
    const response = await http.post<ApiResponse<UploadResponseData>>('/app/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    
    // 处理上传成功
    uploadMessage.value = response.message || t('message.fileUploadSuccess');
    uploadSuccess.value = true;
    
    // 清空选择的文件和表单字段
    selectedFile.value = null;
    fileName.value = '';
    version.value = '';
    forceUpdate.value = false;
    releaseNotes.value = '';
    
    // 重新加载应用版本列表
    searchAppVersions();
    
    // 上传成功后延迟关闭弹窗
    setTimeout(() => {
      showUploadModal.value = false;
      uploadMessage.value = '';
    }, 1500);
  } catch (error) {
    console.error('Failed to upload file:', error);
    uploadMessage.value = t('message.fileUploadFailed');
    uploadSuccess.value = false;
  } finally {
    isUploading.value = false;
  }
};

// 关闭弹窗并重置表单
const closeUploadModal = () => {
  // 清空选择的文件和表单字段
  selectedFile.value = null;
  fileName.value = '';
  version.value = '';
  forceUpdate.value = false;
  releaseNotes.value = '';
  uploadMessage.value = '';
  uploadSuccess.value = false;
  
  // 关闭弹窗
  showUploadModal.value = false;
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
    
    <!-- 搜索和分页控制 - 调整布局，将上传按钮放在右侧 -->
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
      
      <button 
        class="upload-button primary-action"
        @click="showUploadModal = true"
        :disabled="isLoading"
      >
        {{ t('button.uploadNewVersion') }}
      </button>
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
    
    <!-- 分页控件 - 添加分页大小选择器 -->
    <div v-if="totalElements > 0" class="pagination-container">
      <div class="pagination-controls">
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
    
    <!-- 文件上传弹窗 -->
    <div v-if="showUploadModal" class="modal-overlay">
      <div class="upload-modal">
        <div class="modal-header">
          <h2>{{ t('menu.uploadAppVersion') }}</h2>
          <button class="close-button" @click="closeUploadModal">×</button>
        </div>
        
        <div class="modal-body">
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
          </div>
          
          <!-- 上传表单字段 -->
          <div class="upload-form-fields">
            <div class="form-field">
              <label for="version">版本号 <span class="required">*</span></label>
              <input
                type="text"
                id="version"
                v-model="version"
                placeholder="请输入版本号"
                :disabled="isUploading"
              />
            </div>
          </div>
          
          <div class="form-field full-width">
            <label for="releaseNotes">更新说明</label>
            <textarea
              id="releaseNotes"
              v-model="releaseNotes"
              placeholder="请输入更新说明（可选）"
              :disabled="isUploading"
              rows="3"
            ></textarea>
          </div>
          
          <!-- 单独放置的强制更新复选框 -->
          <div class="force-update-section">
            <div class="form-field checkbox-field standalone">
              <input
                type="checkbox"
                id="forceUpdate"
                v-model="forceUpdate"
                :disabled="isUploading"
              />
              <label for="forceUpdate">强制更新</label>
            </div>
          </div>
          
          <!-- 上传消息提示 -->
          <div v-if="uploadMessage" :class="['upload-message', uploadSuccess ? 'success' : 'error']">
            {{ uploadMessage }}
          </div>
        </div>
        
        <div class="modal-footer">
          <button 
            class="cancel-button"
            @click="closeUploadModal"
            :disabled="isUploading"
          >
            {{ t('button.cancel') }}
          </button>
          <button 
            class="upload-button"
            @click="uploadFile"
            :disabled="isUploading || !selectedFile || !version.trim()"
          >
            {{ isUploading ? t('button.uploading') : t('button.upload') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.app-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}

/* 搜索控制区域样式 - 调整布局 */
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

/* 主页面上传按钮样式 */
.upload-button.primary-action {
  margin: 0;
  padding: 0.75rem 2rem;
  font-size: 1rem;
}

/* 分页容器样式 - 新添加 */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 文件上传弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.upload-modal {
  background-color: white;
  border-radius: var(--radius);
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h2 {
  margin: 0;
  color: var(--text-color);
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-light);
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius);
  transition: var(--transition);
}

.close-button:hover {
  background-color: var(--bg-light);
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid var(--border-color);
  background-color: var(--bg-light);
}

.cancel-button {
  padding: 0.75rem 1.5rem;
  background-color: white;
  color: var(--text-color);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition);
}

.cancel-button:hover:not(:disabled) {
  background-color: var(--bg-light);
}

.required {
  color: #ff4d4f;
}

/* 文件上传区域样式 */
.file-upload-container {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.browse-button, .upload-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition);
}

.browse-button:hover, .upload-button:hover:not(:disabled) {
  background-color: var(--primary-dark);
}

.upload-button:disabled, .cancel-button:disabled {
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

/* 上传表单字段样式 */
.upload-form-fields {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.form-field {  
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  flex: 1;
  min-width: 200px;
}

.form-field.full-width {
  width: 100%;
  margin-bottom: 1.5rem;
}

/* 强制更新部分样式 */
.force-update-section {
  margin-bottom: 1.5rem;
}

.checkbox-field.standalone {
  flex-direction: row;
  align-items: center;
  min-width: auto;
  flex: 0;
}

.checkbox-field.standalone input[type="checkbox"] {
  width: auto;
  margin-right: 0.5rem;
  transform: scale(1.2); /* 稍微放大复选框以提高可见性 */
}

.checkbox-field.standalone label {
  font-weight: 500;
  color: var(--text-color);
  font-size: 1rem;
}

.form-field label {
  font-weight: 500;
  color: var(--text-color);
}

.form-field input[type="text"], .form-field textarea {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background-color: var(--bg-color);
  color: var(--text-color);
  font-size: 1rem;
}

.form-field textarea {
  resize: vertical;
  min-height: 80px;
}

.checkbox-field {
  flex-direction: row;
  align-items: center;
  min-width: auto;
  flex: 0;
}

.checkbox-field input[type="checkbox"] {
  width: auto;
  margin-right: 0.5rem;
}

.upload-message {
  padding: 0.75rem;
  border-radius: var(--radius);
  font-size: 0.9rem;
  margin-top: 1rem;
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

.search-input-group button, .pagination button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition);
}

.search-input-group button:hover, .pagination button:hover:not(:disabled) {
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

.loading, .empty-state {
  text-align: center;
  padding: 4rem;
  color: var(--text-light);
}
</style>