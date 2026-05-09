<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="图片导入解析" />
    
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <!-- 页面说明 -->
      <div class="mb-6 bg-white rounded-xl shadow-lg p-6 animate-slide-up">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-rose-100 to-rose-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-rose-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h2 class="text-lg font-semibold text-gray-900">图片导入解析家族关系</h2>
        </div>
        <p class="text-gray-600">上传家族关系图图片，AI将自动识别家族成员及其关系</p>
      </div>

      <!-- 上传区域 -->
      <div v-if="!parseResult" class="bg-white rounded-xl shadow-lg p-6 animate-slide-in-left">
        <div 
          class="upload-area border-2 border-dashed border-gray-300 rounded-xl p-12 text-center cursor-pointer transition-all duration-300 hover:border-green-500 hover:bg-green-50 hover:shadow-md"
          :class="{ 'border-green-500 bg-green-50 shadow-md': isDragOver }"
          @drop.prevent="handleDrop"
          @dragover.prevent="isDragOver = true"
          @dragleave="isDragOver = false"
          @click="triggerFileInput"
        >
          <input 
            ref="fileInput"
            type="file"
            accept="image/png, image/jpeg"
            class="hidden"
            @change="handleFileSelect"
          />
          <div class="w-20 h-20 mx-auto mb-4 rounded-full bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center transition-transform duration-300 hover:scale-110">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
            </svg>
          </div>
          <p class="text-lg font-semibold text-gray-900 mb-2">点击或拖拽上传图片</p>
          <p class="text-sm text-gray-500">支持 PNG、JPG 格式，最大 10MB</p>
        </div>

        <!-- 选择家族 -->
        <div class="mt-6">
          <label class="block text-sm font-medium text-gray-700 mb-2">选择目标家族</label>
          <select v-model="selectedFamilyId" 
                  class="w-full md:w-1/2 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
            <option value="">请选择家族</option>
            <option v-for="family in families" :key="family.id" :value="family.id">
              {{ family.name }}
            </option>
          </select>
        </div>

        <!-- 上传按钮 -->
        <div class="mt-6">
          <button 
            class="px-6 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
            :disabled="!selectedFile || !selectedFamilyId || isLoading"
            @click="parseImage"
          >
            <span v-if="isLoading" class="flex items-center">
              <svg class="animate-spin -ml-1 mr-2 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              解析中...
            </span>
            <span v-else>开始解析</span>
          </button>
        </div>
      </div>

      <!-- 解析结果 -->
      <div v-else class="space-y-6">
        <!-- 返回按钮 -->
        <button class="flex items-center text-gray-600 hover:text-gray-900 transition-colors" @click="reset">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
          返回上传
        </button>

        <!-- 结果概览 -->
        <div class="bg-white rounded-lg shadow p-6">
          <h3 class="text-lg font-semibold mb-4">解析结果</h3>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div class="bg-green-50 rounded-lg p-4 text-center">
              <span class="text-2xl font-bold text-green-600">{{ parseResult.totalMembers }}</span>
              <p class="text-sm text-gray-600 mt-1">识别成员</p>
            </div>
            <div class="bg-blue-50 rounded-lg p-4 text-center">
              <span class="text-2xl font-bold text-blue-600">{{ parseResult.totalRelationships }}</span>
              <p class="text-sm text-gray-600 mt-1">识别关系</p>
            </div>
          </div>
        </div>

        <!-- 成员列表 -->
        <div class="bg-white rounded-lg shadow p-6">
          <h4 class="text-lg font-semibold mb-4">识别的成员</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div v-for="member in parseResult.members" :key="member.id"
                 class="border rounded-lg p-4 flex items-center justify-between"
                 :class="member.exists ? 'border-gray-200 bg-gray-50' : 'border-green-200 bg-green-50'">
              <div>
                <span class="font-medium">{{ member.name }}</span>
                <span class="text-sm text-gray-500 ml-2">{{ member.gender === 'male' ? '男' : member.gender === 'female' ? '女' : '未知' }}</span>
                <span v-if="member.birthYear" class="text-sm text-gray-500 ml-2">{{ member.birthYear }}年</span>
              </div>
              <span v-if="member.exists" class="px-2 py-1 text-xs bg-gray-200 text-gray-700 rounded">已存在</span>
              <span v-else class="px-2 py-1 text-xs bg-green-200 text-green-700 rounded">新增</span>
            </div>
          </div>
        </div>

        <!-- 关系列表 -->
        <div class="bg-white rounded-lg shadow p-6">
          <h4 class="text-lg font-semibold mb-4">识别的关系</h4>
          <div class="space-y-3">
            <div v-for="(relation, index) in parseResult.relationships" :key="index"
                 class="border rounded-lg p-4 flex items-center justify-between"
                 :class="relation.exists ? 'border-gray-200 bg-gray-50' : 'border-blue-200 bg-blue-50'">
              <div class="flex items-center space-x-3">
                <span class="font-medium">{{ relation.member1Name }}</span>
                <span class="text-gray-400">→</span>
                <span class="text-blue-600">{{ getRelationshipLabel(relation.relationshipType) }}</span>
                <span class="text-gray-400">→</span>
                <span class="font-medium">{{ relation.member2Name }}</span>
              </div>
              <span v-if="relation.exists" class="px-2 py-1 text-xs bg-gray-200 text-gray-700 rounded">已存在</span>
              <span v-else class="px-2 py-1 text-xs bg-blue-200 text-blue-700 rounded">新增</span>
            </div>
          </div>
        </div>

        <!-- 保存按钮 -->
        <div class="flex justify-center">
          <button 
            class="px-8 py-3 bg-green-500 text-white rounded-md hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
            :disabled="isSaving"
            @click="saveResult"
          >
            <span v-if="isSaving" class="flex items-center">
              <svg class="animate-spin -ml-1 mr-2 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              保存中...
            </span>
            <span v-else>保存到家族</span>
          </button>
        </div>
      </div>

      <!-- 成功提示 -->
      <div v-if="showSuccess" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-xl p-8 text-center max-w-sm mx-4">
          <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">保存成功</h3>
          <p class="text-gray-600 mb-4">家族关系已成功更新</p>
          <button 
            class="px-6 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition-colors"
            @click="showSuccess = false; reset()"
          >
            确定
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Header from '../components/Header.vue';
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
  const maxSize = 10 * 1024 * 1024;
  const validTypes = ['image/png', 'image/jpeg'];
  
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
    const result = await imageParseStore.parseImage(selectedFile.value, selectedFamilyId.value);
    parseResult.value = result;
  } catch (error) {
    console.error('图片解析失败:', error);
    alert('图片解析失败，请重试');
  } finally {
    isLoading.value = false;
  }
};

const getRelationshipLabel = (type) => {
  const labels = {
    'father': '父子',
    'mother': '母子',
    'son': '儿子',
    'daughter': '女儿',
    'brother': '兄弟',
    'sister': '姐妹',
    'husband': '丈夫',
    'wife': '妻子',
    'grandfather': '祖父',
    'grandmother': '祖母',
    'grandson': '孙子',
    'granddaughter': '孙女'
  };
  return labels[type] || type;
};

const saveResult = async () => {
  if (!parseResult.value || !selectedFamilyId.value) return;
  
  isSaving.value = true;
  
  try {
    await imageParseStore.saveParseResult(parseResult.value, selectedFamilyId.value);
    showSuccess.value = true;
  } catch (error) {
    console.error('保存失败:', error);
    alert('保存失败，请重试');
  } finally {
    isSaving.value = false;
  }
};

const reset = () => {
  parseResult.value = null;
  selectedFile.value = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};
</script>
