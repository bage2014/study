<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const { t } = useI18n()
const router = useRouter()

// 定义应用版本接口
interface AppVersion {
  id: number
  version: string
  releaseDate: string
  releaseNotes: string
  downloadUrl: string
  forceUpdate: boolean
}

// 定义API响应接口
interface ApiResponse<T> {
  code: number
  message: string
  data: T
  total?: number
  page?: number
  size?: number
}

interface PaginationData {
  versions: AppVersion[]
  totalElements: number
  totalPages: number
  currentPage: number
  pageSize: number
}

// 定义文件上传响应接口
interface UploadResponseData {
  fileId: string
  originalFileName: string
  fileName: string
}

// 定义搜索和分页状态
const keyword = ref('')
const currentPage = ref(0)
const pageSize = ref(10)
const totalElements = ref(0)
const totalPages = ref(0)
const appVersions = ref<AppVersion[]>([])
const isLoading = ref(false)
const error = ref('')

// 文件上传相关状态
const selectedFile = ref<File | null>(null)
const fileName = ref('')
const isUploading = ref(false)
const uploadMessage = ref('')
const uploadSuccess = ref(false)
const version = ref('')
const forceUpdate = ref(false)
const releaseNotes = ref('')
const showUploadModal = ref(false)

// 计算属性
const hasSearchKeyword = computed(() => keyword.value.trim() !== '')
const canUpload = computed(() => selectedFile.value && version.value.trim() && !isUploading.value)
const uploadButtonText = computed(() => isUploading.value ? t('button.uploading') : t('button.upload'))

// 查询应用版本列表
const searchAppVersions = async () => {
  isLoading.value = true
  error.value = ''
  
  try {
    const response = await request.get<ApiResponse<PaginationData>>('/app/versions', {
      params: {
        keyword: keyword.value.trim(),
        page: currentPage.value,
        size: pageSize.value
      }
    })
    
    if (response && response.data) {
      appVersions.value = response.data.data.versions || []
      totalElements.value = response.data.data.totalElements || 0
      totalPages.value = response.data.data.totalPages || 0
      currentPage.value = response.data.data.currentPage || 0
      pageSize.value = response.data.data.pageSize || 10
    } else {
      appVersions.value = []
      totalElements.value = 0
      totalPages.value = 0
      currentPage.value = 0
      pageSize.value = 10
    }
  } catch (err: any) {
    console.error('Failed to fetch app versions:', err)
    error.value = err.response?.data?.message || t('message.fetchFailed')
    appVersions.value = []
    totalElements.value = 0
    totalPages.value = 0
    currentPage.value = 0
    pageSize.value = 10
  } finally {
    isLoading.value = false
  }
}

// 处理文件选择
const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (input.files && input.files.length > 0) {
    const file = input.files[0]
    
    // 验证文件类型（可选）
    const allowedTypes = ['application/octet-stream', 'application/zip', 'application/x-zip-compressed']
    if (!allowedTypes.includes(file.type) && !file.name.endsWith('.apk') && !file.name.endsWith('.ipa')) {
      uploadMessage.value = t('message.invalidFileType')
      uploadSuccess.value = false
      return
    }
    
    // 验证文件大小（可选，限制为50MB）
    const maxSize = 50 * 1024 * 1024 // 50MB
    if (file.size > maxSize) {
      uploadMessage.value = t('message.fileTooLarge')
      uploadSuccess.value = false
      return
    }
    
    selectedFile.value = file
    fileName.value = file.name
    uploadMessage.value = ''
  }
}

// 上传文件
const uploadFile = async () => {
  if (!selectedFile.value) {
    uploadMessage.value = t('message.selectFileFirst')
    uploadSuccess.value = false
    return
  }
  
  if (!version.value.trim()) {
    uploadMessage.value = t('message.versionRequired')
    uploadSuccess.value = false
    return
  }

  isUploading.value = true
  uploadMessage.value = ''
  
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('version', version.value.trim())
    formData.append('forceUpdate', String(forceUpdate.value))
    
    if (releaseNotes.value.trim()) {
      formData.append('releaseNotes', releaseNotes.value.trim())
    }
    
    const response = await request.post<ApiResponse<UploadResponseData>>('/app/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    uploadMessage.value = response.data.message || t('message.fileUploadSuccess')
    uploadSuccess.value = true
    
    // 重置表单
    resetUploadForm()
    
    // 重新加载列表
    await searchAppVersions()
    
    // 延迟关闭弹窗
    setTimeout(() => {
      showUploadModal.value = false
      uploadMessage.value = ''
    }, 1500)
  } catch (err: any) {
    console.error('Failed to upload file:', err)
    uploadMessage.value = err.response?.data?.message || t('message.fileUploadFailed')
    uploadSuccess.value = false
  } finally {
    isUploading.value = false
  }
}

// 重置上传表单
const resetUploadForm = () => {
  selectedFile.value = null
  fileName.value = ''
  version.value = ''
  forceUpdate.value = false
  releaseNotes.value = ''
  uploadMessage.value = ''
  uploadSuccess.value = false
}

// 关闭弹窗
const closeUploadModal = () => {
  resetUploadForm()
  showUploadModal.value = false
}

// 切换页码
const changePage = (page: number) => {
  currentPage.value = page
  searchAppVersions()
}

// 切换每页显示数量
const changeSize = (size: number) => {
  pageSize.value = size
  currentPage.value = 0
  searchAppVersions()
}

// 清空搜索
const clearSearch = () => {
  keyword.value = ''
  currentPage.value = 0
  searchAppVersions()
}

// 格式化日期
const formatDate = (dateString: string) => {
  try {
    return new Date(dateString).toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch {
    return dateString
  }
}

// 下载文件
const downloadFile = (url: string, version: string) => {
  if (!url) return
  
  const link = document.createElement('a')
  link.href = url
  link.download = `app-v${version}.apk`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 删除版本（可选功能）
const deleteVersion = async (id: number) => {
  if (!confirm(t('message.confirmDelete'))) return
  
  try {
    await request.delete(`/app/versions/${id}`)
    await searchAppVersions()
  } catch (err: any) {
    console.error('Failed to delete version:', err)
    error.value = err.response?.data?.message || t('message.deleteFailed')
  }
}

// 初始加载数据
onMounted(() => {
  searchAppVersions()
})
</script>

<template>
  <div class="app-list-container">
    <!-- 头部 -->
    <div class="page-header">
      <h1>{{ t('menu.appList') }}</h1>
      <div class="header-actions">
        <button 
          class="primary-button"
          @click="showUploadModal = true"
          :disabled="isLoading"
        >
          <span class="button-icon">+</span>
          {{ t('button.uploadNewVersion') }}
        </button>
      </div>
    </div>
    
    <!-- 搜索和过滤 -->
    <div class="search-section">
      <div class="search-input-wrapper">
        <input
          type="text"
          v-model="keyword"
          :placeholder="t('placeholder.searchAppVersion')"
          @keyup.enter="searchAppVersions()"
          class="search-input"
        />
        <button 
          v-if="hasSearchKeyword"
          @click="clearSearch"
          class="clear-button"
          title="{{ t('button.clear') }}"
        >
          ×
        </button>
      </div>
      <button @click="searchAppVersions()" class="search-button">
        {{ t('button.search') }}
      </button>
    </div>
    
    <!-- 错误提示 -->
    <div v-if="error" class="error-alert">
      <span class="error-icon">⚠️</span>
      <span class="error-message">{{ error }}</span>
      <button @click="error = ''" class="close-error">×</button>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="isLoading" class="loading-container">
      <div class="loading-spinner"></div>
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
          <div class="version-actions">
            <button 
              v-if="version.downloadUrl"
              @click="downloadFile(version.downloadUrl, version.version)"
              class="icon-button download-button"
              :title="t('button.download')"
            >
              ↓
            </button>
            <button 
              @click="deleteVersion(version.id)"
              class="icon-button delete-button"
              :title="t('button.delete')"
            >
              🗑
            </button>
          </div>
        </div>
        
        <div class="version-meta">
          <span class="release-date">
            📅 {{ formatDate(version.releaseDate) }}
          </span>
        </div>
        
        <div class="release-notes" v-if="version.releaseNotes">
          <h4>{{ t('label.releaseNotes') }}:</h4>
          <p>{{ version.releaseNotes }}</p>
        </div>
        
        <div class="app-version-footer">
          <a 
            v-if="version.downloadUrl" 
            :href="version.downloadUrl" 
            target="_blank"
            class="download-link"
          >
            {{ t('button.download') }}
          </a>
          <span v-else class="no-download">{{ t('message.noDownloadUrl') }}</span>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-state">
      <div class="empty-icon">📱</div>
      <h3>{{ t('message.noAppVersionsFound') }}</h3>
      <p>{{ t('message.uploadFirstVersion') }}</p>
      <button @click="showUploadModal = true" class="primary-button">
        {{ t('button.uploadFirstVersion') }}
      </button>
    </div>
    
    <!-- 分页控件 -->
    <div v-if="totalElements > 0" class="pagination-container">
      <div class="pagination-info">
        <span>{{ t('label.total') }}: {{ totalElements }}</span>
        <span>{{ t('label.showing') }}: {{ currentPage * pageSize + 1 }}-{{ Math.min((currentPage + 1) * pageSize, totalElements) }}</span>
      </div>
      
      <div class="pagination-controls">
        <button
          :disabled="currentPage <= 0"
          @click="changePage(currentPage - 1)"
          class="pagination-button"
        >
          {{ t('button.prevPage') }}
        </button>
        
        <div class="page-numbers">
          <button
            v-for="page in Math.min(totalPages, 5)"
            :key="page - 1"
            @click="changePage(page - 1)"
            :class="['page-number', { active: currentPage === page - 1 }]"
          >
            {{ page }}
          </button>
        </div>
        
        <button
          :disabled="currentPage >= totalPages - 1"
          @click="changePage(currentPage + 1)"
          class="pagination-button"
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
    <div v-if="showUploadModal" class="modal-overlay" @click.self="closeUploadModal">
      <div class="upload-modal">
        <div class="modal-header">
          <h2>{{ t('menu.uploadAppVersion') }}</h2>
          <button class="close-button" @click="closeUploadModal">×</button>
        </div>
        
        <div class="modal-body">
          <!-- 文件上传区域 -->
          <div class="upload-section">
            <div class="file-upload-area">
              <input
                type="file"
                id="fileUpload"
                @change="handleFileChange"
                accept=".apk,.ipa,application/octet-stream"
                style="display: none;"
              />
              <label for="fileUpload" class="file-upload-label">
                <div class="upload-icon">📁</div>
                <div class="upload-text">
                  <span class="upload-main-text">{{ t('label.selectFile') }}</span>
                  <span class="upload-sub-text">{{ t('label.fileSizeLimit') }}</span>
                </div>
              </label>
            </div>
            
            <div v-if="fileName" class="selected-file">
              <span class="file-name">{{ fileName }}</span>
              <button @click="selectedFile = null; fileName = ''" class="remove-file">×</button>
            </div>
          </div>
          
          <!-- 上传表单字段 -->
          <div class="form-section">
            <div class="form-group">
              <label for="version">
                {{ t('label.versionNumber') }} <span class="required">*</span>
              </label>
              <input
                type="text"
                id="version"
                v-model="version"
                :placeholder="t('placeholder.versionNumber')"
                :disabled="isUploading"
                class="form-input"
              />
            </div>
            
            <div class="form-group">
              <label for="releaseNotes">{{ t('label.releaseNotes') }}</label>
              <textarea
                id="releaseNotes"
                v-model="releaseNotes"
                :placeholder="t('placeholder.releaseNotes')"
                :disabled="isUploading"
                class="form-textarea"
                rows="4"
              ></textarea>
            </div>
            
            <div class="form-group checkbox-group">
              <input
                type="checkbox"
                id="forceUpdate"
                v-model="forceUpdate"
                :disabled="isUploading"
                class="form-checkbox"
              />
              <label for="forceUpdate">{{ t('label.forceUpdate') }}</label>
            </div>
          </div>
          
          <!-- 上传消息提示 -->
          <div v-if="uploadMessage" :class="['upload-message', uploadSuccess ? 'success' : 'error']">
            <span class="message-icon">{{ uploadSuccess ? '✓' : '⚠️' }}</span>
            <span class="message-text">{{ uploadMessage }}</span>
          </div>
        </div>
        
        <div class="modal-footer">
          <button 
            class="secondary-button"
            @click="closeUploadModal"
            :disabled="isUploading"
          >
            {{ t('button.cancel') }}
          </button>
          <button 
            class="primary-button"
            @click="uploadFile"
            :disabled="!canUpload"
          >
            {{ uploadButtonText }}
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
  padding: 1.5rem;
  min-height: 100vh;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid var(--border-color, #e0e0e0);
}

.page-header h1 {
  margin: 0;
  color: var(--text-primary, #333);
  font-size: 1.75rem;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 1rem;
}

/* 搜索区域 */
.search-section {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.search-input-wrapper {
  position: relative;
  flex: 1;
  min-width: 300px;
}

.search-input {
  width: 100%;
  padding: 0.75rem 2.5rem 0.75rem 1rem;
  border: 2px solid var(--border-color, #e0e0e0);
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: var(--primary-color, #1E90FF);
  box-shadow: 0 0 0 3px rgba(30, 144, 255, 0.1);
}

.clear-button {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  font-size: 1.2rem;
  color: var(--text-secondary, #666);
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.clear-button:hover {
  background-color: var(--bg-hover, #f5f5f5);
  color: var(--text-primary, #333);
}

.search-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color, #1E90FF);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-button:hover {
  background-color: var(--primary-dark, #1876CC);
  transform: translateY(-1px);
}

/* 错误提示 */
.error-alert {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  background-color: #fee;
  border: 1px solid #fcc;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  color: #c33;
}

.error-icon {
  font-size: 1.2rem;
}

.error-message {
  flex: 1;
}

.close-error {
  background: none;
  border: none;
  font-size: 1.2rem;
  color: #c33;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.close-error:hover {
  background-color: #fcc;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  gap: 1rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(30, 144, 255, 0.3);
  border-top-color: var(--primary-color, #1E90FF);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 应用版本网格 */
.app-versions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.app-version-card {
  background-color: var(--card-bg, #fff);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
}

.app-version-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
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
  flex: 1;
}

.version-info h3 {
  margin: 0;
  font-size: 1.25rem;
  color: var(--text-primary, #333);
  font-weight: 600;
}

.force-update-badge {
  background-color: #ff4d4f;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
  white-space: nowrap;
}

.version-actions {
  display: flex;
  gap: 0.5rem;
}

.icon-button {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.download-button {
  background-color: #e6f7ff;
  color: #1890ff;
}

.download-button:hover {
  background-color: #1890ff;
  color: white;
}

.delete-button {
  background-color: #fff1f0;
  color: #ff4d4f;
}

.delete-button:hover {
  background-color: #ff4d4f;
  color: white;
}

.version-meta {
  margin-bottom: 1rem;
}

.release-date {
  font-size: 0.875rem;
  color: var(--text-secondary, #666);
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.release-notes {
  margin-bottom: 1rem;
}

.release-notes h4 {
  margin: 0 0 0.5rem 0;
  font-size: 0.875rem;
  color: var(--text-secondary, #666);
  font-weight: 500;
}

.release-notes p {
  margin: 0;
  font-size: 0.875rem;
  line-height: 1.5;
  color: var(--text-primary, #333);
  white-space: pre-wrap;
}

.app-version-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color, #e0e0e0);
}

.download-link {
  color: var(--primary-color, #1E90FF);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s ease;
}

.download-link:hover {
  text-decoration: underline;
}

.no-download {
  color: var(--text-secondary, #666);
  font-size: 0.875rem;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--text-secondary, #666);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-state h3 {
  margin: 0 0 0.5rem 0;
  font-size: 1.25rem;
  color: var(--text-primary, #333);
}

.empty-state p {
  margin: 0 0 1.5rem 0;
  font-size: 1rem;
}

/* 分页控件 */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 1rem;
  background-color: var(--card-bg, #fff);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination-info {
  display: flex;
  gap: 1rem;
  font-size: 0.875rem;
  color: var(--text-secondary, #666);
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.pagination-button {
  padding: 0.5rem 1rem;
  background-color: var(--card-bg, #fff);
  color: var(--text-primary, #333);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s ease;
}

.pagination-button:hover:not(:disabled) {
  background-color: var(--bg-hover, #f5f5f5);
  border-color: var(--primary-color, #1E90FF);
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 0.25rem;
}

.page-number {
  min-width: 32px;
  height: 32px;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 6px;
  background-color: var(--card-bg, #fff);
  color: var(--text-primary, #333);
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
}

.page-number:hover {
  background-color: var(--bg-hover, #f5f5f5);
}

.page-number.active {
  background-color: var(--primary-color, #1E90FF);
  color: white;
  border-color: var(--primary-color, #1E90FF);
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.page-size-selector select {
  padding: 0.5rem;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 6px;
  background-color: var(--card-bg, #fff);
  color: var(--text-primary, #333);
  cursor: pointer;
}

/* 模态弹窗 */
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
  backdrop-filter: blur(4px);
}

.upload-modal {
  background-color: var(--card-bg, #fff);
  border-radius: 12px;
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  animation: modalSlideIn 0.3s ease;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color, #e0e0e0);
}

.modal-header h2 {
  margin: 0;
  color: var(--text-primary, #333);
  font-size: 1.25rem;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-secondary, #666);
  padding: 0.5rem;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.close-button:hover {
  background-color: var(--bg-hover, #f5f5f5);
  color: var(--text-primary, #333);
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid var(--border-color, #e0e0e0);
  background-color: var(--bg-light, #f9f9f9);
}

/* 上传区域 */
.upload-section {
  margin-bottom: 1.5rem;
}

.file-upload-area {
  border: 2px dashed var(--border-color, #e0e0e0);
  border-radius: 8px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.file-upload-area:hover {
  border-color: var(--primary-color, #1E90FF);
  background-color: rgba(30, 144, 255, 0.05);
}

.file-upload-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  cursor: pointer;
}

.upload-icon {
  font-size: 3rem;
  opacity: 0.6;
}

.upload-text {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.upload-main-text {
  font-weight: 500;
  color: var(--text-primary, #333);
}

.upload-sub-text {
  font-size: 0.875rem;
  color: var(--text-secondary, #666);
}

.selected-file {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem;
  background-color: var(--bg-light, #f9f9f9);
  border-radius: 6px;
  margin-top: 1rem;
}

.file-name {
  flex: 1;
  font-size: 0.875rem;
  color: var(--text-primary, #333);
  word-break: break-all;
}

.remove-file {
  background: none;
  border: none;
  font-size: 1.2rem;
  color: var(--text-secondary, #666);
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.remove-file:hover {
  background-color: var(--bg-hover, #f5f5f5);
  color: #ff4d4f;
}

/* 表单区域 */
.form-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 500;
  color: var(--text-primary, #333);
  font-size: 0.875rem;
}

.required {
  color: #ff4d4f;
}

.form-input {
  padding: 0.75rem;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 6px;
  font-size: 1rem;
  transition: all 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-color, #1E90FF);
  box-shadow: 0 0 0 3px rgba(30, 144, 255, 0.1);
}

.form-textarea {
  padding: 0.75rem;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 6px;
  font-size: 1rem;
  resize: vertical;
  min-height: 100px;
  transition: all 0.2s ease;
  font-family: inherit;
}

.form-textarea:focus {
  outline: none;
  border-color: var(--primary-color, #1E90FF);
  box-shadow: 0 0 0 3px rgba(30, 144, 255, 0.1);
}

.checkbox-group {
  flex-direction: row;
  align-items: center;
  gap: 0.5rem;
}

.form-checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

/* 上传消息 */
.upload-message {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem;
  border-radius: 6px;
  font-size: 0.875rem;
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

.message-icon {
  font-size: 1rem;
}

/* 按钮样式 */
.primary-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color, #1E90FF);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.primary-button:hover:not(:disabled) {
  background-color: var(--primary-dark, #1876CC);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(30, 144, 255, 0.3);
}

.primary-button:disabled {
  background-color: var(--bg-light, #f9f9f9);
  color: var(--text-secondary, #666);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.secondary-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--card-bg, #fff);
  color: var(--text-primary, #333);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.secondary-button:hover:not(:disabled) {
  background-color: var(--bg-hover, #f5f5f5);
  border-color: var(--primary-color, #1E90FF);
}

.secondary-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.button-icon {
  font-size: 1.2rem;
  font-weight: bold;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-list-container {
    padding: 1rem;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .search-section {
    flex-direction: column;
  }
  
  .search-input-wrapper {
    min-width: 100%;
  }
  
  .app-versions-grid {
    grid-template-columns: 1fr;
  }
  
  .pagination-container {
    flex-direction: column;
    gap: 1rem;
  }
  
  .pagination-info {
    order: 3;
    width: 100%;
    justify-content: center;
  }
  
  .upload-modal {
    margin: 1rem;
    max-width: calc(100% - 2rem);
  }
}

@media (max-width: 480px) {
  .page-header h1 {
    font-size: 1.5rem;
  }
  
  .app-version-card {
    padding: 1rem;
  }
  
  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 1rem;
  }
  
  .primary-button,
  .secondary-button {
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
  }
}
</style>