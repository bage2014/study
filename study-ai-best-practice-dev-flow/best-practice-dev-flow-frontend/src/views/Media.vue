<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { mediaAPI } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()
const mediaList = ref<any[]>([])

const showUploadModal = ref(false)
const showPreviewModal = ref(false)

const uploadForm = ref({
  type: 'PHOTO',
  url: '',
  description: ''
})

const previewMedia = ref<any>(null)

const mediaTypeOptions = [
  { value: 'PHOTO', label: '照片' },
  { value: 'VIDEO', label: '视频' },
  { value: 'DOCUMENT', label: '文档' }
]

async function loadMedia() {
  if (familyStore.currentFamily) {
    mediaList.value = await mediaAPI.getByFamily(familyStore.currentFamily.id)
  }
}

async function uploadMedia() {
  if (familyStore.currentFamily) {
    await mediaAPI.upload({
      familyId: familyStore.currentFamily.id,
      type: uploadForm.value.type,
      url: uploadForm.value.url,
      description: uploadForm.value.description
    })
    showUploadModal.value = false
    resetForm()
    await loadMedia()
  }
}

function resetForm() {
  uploadForm.value = {
    type: 'PHOTO',
    url: '',
    description: ''
  }
}

async function deleteMedia(id: number) {
  await mediaAPI.delete(id)
  await loadMedia()
}

function preview(id: number) {
  const media = mediaList.value.find(m => m.id === id)
  if (media) {
    previewMedia.value = media
    showPreviewModal.value = true
  }
}

watch(() => familyStore.currentFamily, async () => {
  await loadMedia()
})

onMounted(async () => {
  await loadMedia()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>多媒体库</h1>
        <el-button type="primary" @click="showUploadModal = true">
          <el-icon>Plus</el-icon>
          上传媒体
        </el-button>
      </div>

      <div class="card">
        <div v-if="mediaList.length === 0" class="empty-state">
          <el-icon size="64" class="empty-icon">Picture</el-icon>
          <p>暂无媒体文件</p>
        </div>
        
        <div v-else class="media-grid">
          <div 
            v-for="media in mediaList" 
            :key="media.id" 
            class="media-card"
            @click="preview(media.id)"
          >
            <div class="media-thumbnail">
              <el-icon v-if="media.type === 'PHOTO'" size="48">Image</el-icon>
              <el-icon v-else-if="media.type === 'VIDEO'" size="48">Video</el-icon>
              <el-icon v-else size="48">FileText</el-icon>
            </div>
            <div class="media-info">
              <span class="media-type">{{ media.type === 'PHOTO' ? '照片' : media.type === 'VIDEO' ? '视频' : '文档' }}</span>
              <p class="media-desc">{{ media.description || '无描述' }}</p>
            </div>
            <div class="media-actions">
              <el-button size="small" @click.stop="preview(media.id)">
                <el-icon>ZoomIn</el-icon>
              </el-button>
              <el-button size="small" type="danger" @click.stop="deleteMedia(media.id)">
                <el-icon>Delete</el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <el-dialog title="上传媒体" :visible="showUploadModal" @close="showUploadModal = false">
        <el-form :model="uploadForm" label-width="100px">
          <el-form-item label="类型" required>
            <el-select v-model="uploadForm.type">
              <el-option v-for="opt in mediaTypeOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="URL" required>
            <el-input v-model="uploadForm.url" placeholder="请输入媒体URL" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="uploadForm.description" placeholder="请输入描述" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUploadModal = false">取消</el-button>
          <el-button type="primary" @click="uploadMedia">上传</el-button>
        </template>
      </el-dialog>

      <el-dialog title="预览" :visible="showPreviewModal" @close="showPreviewModal = false">
        <div v-if="previewMedia" class="preview-content">
          <div class="preview-type">
            {{ previewMedia.type === 'PHOTO' ? '照片' : previewMedia.type === 'VIDEO' ? '视频' : '文档' }}
          </div>
          <div class="preview-url">
            <a :href="previewMedia.url" target="_blank">{{ previewMedia.url }}</a>
          </div>
          <div class="preview-desc">
            {{ previewMedia.description || '无描述' }}
          </div>
        </div>
      </el-dialog>
    </main>
  </div>
</template>

<style scoped>
.page-layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f5f5;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  margin: 0;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.empty-state {
  text-align: center;
  padding: 100px 0;
}

.empty-icon {
  color: #ccc;
  margin-bottom: 20px;
}

.media-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.media-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.media-card:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.media-thumbnail {
  text-align: center;
  color: #409eff;
  margin-bottom: 12px;
}

.media-info {
  margin-bottom: 12px;
}

.media-type {
  display: inline-block;
  background: #f0f5ff;
  color: #409eff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-bottom: 8px;
}

.media-desc {
  margin: 0;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.media-actions {
  display: flex;
  gap: 8px;
}

.preview-content {
  padding: 20px;
}

.preview-type {
  color: #409eff;
  font-size: 14px;
  margin-bottom: 12px;
}

.preview-url {
  word-break: break-all;
  margin-bottom: 12px;
}

.preview-url a {
  color: #409eff;
}

.preview-desc {
  color: #666;
}
</style>