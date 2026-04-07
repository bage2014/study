<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">AI关系分析</h1>

    <!-- 家族选择器 -->
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">选择家族</label>
      <select v-model="selectedFamilyId" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
        <option value="">请选择家族</option>
        <option v-for="family in families" :key="family.id" :value="family.id">
          {{ family.name }}
        </option>
      </select>
    </div>

    <!-- 分析按钮 -->
    <div class="mb-4">
      <button 
        @click="analyzeRelationships" 
        :disabled="!selectedFamilyId || aiRelationshipStore.loading"
        class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md disabled:bg-gray-400"
      >
        {{ aiRelationshipStore.loading ? '分析中...' : '分析关系' }}
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="aiRelationshipStore.loading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-green-500"></div>
    </div>

    <!-- 错误信息 -->
    <div v-else-if="aiRelationshipStore.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ aiRelationshipStore.error }}
    </div>

    <!-- 分析结果 -->
    <div v-else-if="aiRelationshipStore.predictedRelationships.length > 0" class="mt-4">
      <h2 class="text-lg font-semibold mb-2">分析结果</h2>
      <div class="space-y-4">
        <div v-for="(relationship, index) in aiRelationshipStore.predictedRelationships" :key="index" class="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
          <div class="flex items-center">
            <div class="font-medium">{{ relationship.member1.name }}</div>
            <div class="mx-4 px-2 py-1 bg-green-100 text-green-800 rounded-md text-sm">
              {{ getRelationshipTypeText(relationship.relationshipType) }}
            </div>
            <div class="font-medium">{{ relationship.member2.name }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 无分析结果提示 -->
    <div v-else-if="selectedFamilyId" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
      <p class="text-gray-500">点击"分析关系"按钮开始分析家族关系</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAiRelationshipStore } from '../stores/aiRelationship'
import { useFamilyStore } from '../stores/family'

const aiRelationshipStore = useAiRelationshipStore()
const familyStore = useFamilyStore()

const selectedFamilyId = ref('')

const families = computed(() => familyStore.families)

onMounted(async () => {
  await familyStore.fetchFamilies()
})

const analyzeRelationships = async () => {
  if (selectedFamilyId.value) {
    await aiRelationshipStore.analyzeRelationships(selectedFamilyId.value)
  }
}

const getRelationshipTypeText = (type) => {
  const typeMap = {
    'parent': '父母',
    'child': '子女',
    'sibling': '兄弟姐妹',
    'spouse': '配偶',
    'relative': '亲戚'
  }
  return typeMap[type] || type
}
</script>
