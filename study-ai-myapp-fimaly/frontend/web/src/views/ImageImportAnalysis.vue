<template>
  <div class="image-import-container">
    <div class="header">
      <h2>图片导入解析家族关系</h2>
      <p class="description">上传家族关系图图片，AI将自动识别家族成员及其关系</p>
    </div>

    <!-- 上传区域 -->
    <div v-if="!parseResult" class="upload-section">
      <div 
        class="upload-area"
        :class="{ 'drag-over': isDragOver }"
        @drop.prevent="handleDrop"
        @dragover.prevent="isDragOver = true"
        @dragleave="isDragOver = false"
        @click="triggerFileInput"
      >
        <input 
          ref="fileInput"
          type="file"
          accept="image/png, image/jpeg"
          class="file-input"
          @change="handleFileSelect"
        />
        <div class="upload-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
            <polyline points="17 8 12 3 7 8"></polyline>
            <line x1="12" y1="3" x2="12" y2="15"></line>
          </svg>
        </div>
        <p class="upload-text">点击或拖拽上传图片</p>
        <p class="upload-hint">支持 PNG、JPG 格式，最大 10MB</p>
      </div>

      <!-- 选择家族 -->
      <div class="family-select">
        <label>选择目标家族:</label>
        <select v-model="selectedFamilyId" class="family-select-input">
          <option value="">请选择家族</option>
          <option v-for="family in families" :key="family.id" :value="family.id">
            {{ family.name }}
          </option>
        </select>
      </div>

      <!-- 上传按钮 -->
      <button 
        class="parse-btn"
        :disabled="!selectedFile || !selectedFamilyId || isLoading"
        @click="parseImage"
      >
        <span v-if="isLoading">解析中...</span>
        <span v-else>开始解析</span>
      </button>
    </div>

    <!-- 解析结果 -->
    <div v-else class="result-section">
      <!-- 返回按钮 -->
      <button class="back-btn" @click="reset">
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M19 12H5"></path>
          <polyline points="12 19 5 12 12 5"></polyline>
        </svg>
        返回上传
      </button>

      <!-- 结果概览 -->
      <div class="result-summary">
        <h3>解析结果</h3>
        <div class="summary-stats">
          <div class="stat-item">
            <span class="stat-value">{{ parseResult.totalMembers }}</span>
            <span class="stat-label">成员</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ parseResult.totalRelationships }}</span>
            <span class="stat-label">关系</span>
          </div>
        </div>
      </div>

      <!-- 成员列表 -->
      <div class="members-panel">
        <h4>识别的成员</h4>
        <div class="members-list">
          <div 
            v-for="member in parseResult.members" 
            :key="member.id"
            class="member-card"
            :class="{ 'exists': member.exists }"
          >
            <div class="member-info">
              <span class="member-name">{{ member.name }}</span>
              <span class="member-gender">{{ member.gender === 'male' ? '男' : member.gender === 'female' ? '女' : '未知' }}</span>
              <span v-if="member.birthYear" class="member-birth">{{ member.birthYear }}年</span>
            </div>
            <div class="member-status">
              <span v-if="member.exists" class="status-tag exists-tag">已存在</span>
              <span v-else class="status-tag new-tag">新增</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 关系列表 -->
      <div class="relationships-panel">
        <h4>识别的关系</h4>
        <div class="relationships-list">
          <div 
            v-for="(relation, index) in parseResult.relationships" 
            :key="index"
            class="relationship-card"
            :class="{ 'exists': relation.exists }"
          >
            <div class="relationship-content">
              <span class="relation-name">{{ relation.member1Name }}</span>
              <span class="relation-arrow">→</span>
              <span class="relation-type">{{ getRelationshipLabel(relation.relationshipType) }}</span>
              <span class="relation-arrow">→</span>
              <span class="relation-name">{{ relation.member2Name }}</span>
            </div>
            <div class="relationship-status">
              <span v-if="relation.exists" class="status-tag exists-tag">已存在</span>
              <span v-else class="status-tag new-tag">新增</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 保存按钮 -->
      <button 
        class="save-btn"
        :disabled="isSaving"
        @click="saveResult"
      >
        <span v-if="isSaving">保存中...</span>
        <span v-else>保存到家族</span>
      </button>
    </div>

    <!-- 成功提示 -->
    <div v-if="showSuccess" class="success-modal">
      <div class="success-content">
        <div class="success-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="20 6 9 17 4 12"></polyline>
          </svg>
        </div>
        <h3>保存成功</h3>
        <p>家族关系已成功更新</p>
        <button class="confirm-btn" @click="showSuccess = false; reset()">确定</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useFamilyStore } from '../stores/family';
import { useImageParseStore } from '../stores/imageParse';

const familyStore = useFamilyStore();
const imageParseStore = useImageParseStore();

const fileInput = ref(null);
const isDragOver = ref(false);
const selectedFile = ref(null);
const selectedFamilyId = ref('');
const isLoading = ref(false);
const isSaving = ref(false);
const parseResult = ref(null);
const showSuccess = ref(false);
const families = ref([]);

onMounted(() => {
  loadFamilies();
});

const loadFamilies = async () => {
  try {
    await familyStore.fetchFamilies();
    families.value = familyStore.families;
  } catch (error) {
    console.error('加载家族列表失败:', error);
  }
};

const triggerFileInput = () => {
  fileInput.value?.click();
};

const handleFileSelect = (event) => {
  const file = event.target.files?.[0];
  if (file) {
    validateFile(file);
  }
};

const handleDrop = (event) => {
  isDragOver.value = false;
  const file = event.dataTransfer.files?.[0];
  if (file) {
    validateFile(file);
  }
};

const validateFile = (file) => {
  const validTypes = ['image/png', 'image/jpeg'];
  const maxSize = 10 * 1024 * 1024; // 10MB

  if (!validTypes.includes(file.type)) {
    alert('请上传PNG或JPG格式的图片');
    return;
  }

  if (file.size > maxSize) {
    alert('图片大小不能超过10MB');
    return;
  }

  selectedFile.value = file;
};

const parseImage = async () => {
  if (!selectedFile.value || !selectedFamilyId.value) return;

  isLoading.value = true;
  try {
    const result = await imageParseStore.uploadAndParse(
      selectedFile.value,
      parseInt(selectedFamilyId.value)
    );
    parseResult.value = result;
  } catch (error) {
    console.error('解析图片失败:', error);
    alert('解析图片失败: ' + error.message);
  } finally {
    isLoading.value = false;
  }
};

const saveResult = async () => {
  if (!parseResult.value || !selectedFamilyId.value) return;

  isSaving.value = true;
  try {
    await imageParseStore.saveParseResult(
      parseInt(selectedFamilyId.value),
      parseResult.value.members,
      parseResult.value.relationships
    );
    showSuccess.value = true;
  } catch (error) {
    console.error('保存失败:', error);
    alert('保存失败: ' + error.message);
  } finally {
    isSaving.value = false;
  }
};

const reset = () => {
  selectedFile.value = null;
  parseResult.value = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

const getRelationshipLabel = (type) => {
  const labels = {
    'parent': '父母',
    'child': '子女',
    'sibling': '兄弟姐妹',
    'spouse': '配偶',
    'grandparent': '祖孙',
    'grandchild': '孙辈',
    'relative': '亲戚'
  };
  return labels[type] || type;
};
</script>

<style scoped>
.image-import-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 8px;
}

.description {
  color: #666;
  font-size: 14px;
}

.upload-section {
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.upload-area {
  border: 2px dashed #ddd;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-area:hover,
.upload-area.drag-over {
  border-color: #4080ff;
  background: #f5f8ff;
}

.file-input {
  display: none;
}

.upload-icon {
  color: #4080ff;
  margin-bottom: 16px;
}

.upload-text {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.upload-hint {
  font-size: 12px;
  color: #999;
}

.family-select {
  margin-top: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.family-select label {
  font-size: 14px;
  color: #666;
}

.family-select-input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.parse-btn,
.save-btn {
  width: 100%;
  margin-top: 24px;
  padding: 14px;
  background: #4080ff;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.parse-btn:hover:not(:disabled),
.save-btn:hover:not(:disabled) {
  background: #3070ee;
}

.parse-btn:disabled,
.save-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.result-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: #f5f5f5;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.back-btn:hover {
  background: #eee;
}

.result-summary {
  margin-bottom: 24px;
}

.result-summary h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
}

.summary-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 32px;
  font-weight: bold;
  color: #4080ff;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.members-panel,
.relationships-panel {
  margin-bottom: 24px;
}

.members-panel h4,
.relationships-panel h4 {
  font-size: 16px;
  color: #333;
  margin-bottom: 12px;
}

.members-list,
.relationships-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.member-card,
.relationship-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
  border-left: 4px solid #4080ff;
}

.member-card.exists,
.relationship-card.exists {
  border-left-color: #999;
  background: #f0f0f0;
}

.member-info {
  display: flex;
  gap: 16px;
  align-items: center;
}

.member-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.member-gender,
.member-birth {
  font-size: 14px;
  color: #666;
  padding: 4px 8px;
  background: #fff;
  border-radius: 4px;
}

.relationship-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.relation-name {
  font-size: 14px;
  color: #333;
}

.relation-arrow {
  color: #999;
}

.relation-type {
  font-size: 14px;
  color: #4080ff;
  font-weight: 500;
  padding: 4px 12px;
  background: #e8f0ff;
  border-radius: 12px;
}

.status-tag {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 12px;
}

.exists-tag {
  background: #e8e8e8;
  color: #666;
}

.new-tag {
  background: #e8f0ff;
  color: #4080ff;
}

.success-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.success-content {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  text-align: center;
  width: 360px;
}

.success-icon {
  color: #52c41a;
  margin-bottom: 16px;
}

.success-content h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 8px;
}

.success-content p {
  color: #666;
  margin-bottom: 24px;
}

.confirm-btn {
  padding: 12px 32px;
  background: #4080ff;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.confirm-btn:hover {
  background: #3070ee;
}
</style>