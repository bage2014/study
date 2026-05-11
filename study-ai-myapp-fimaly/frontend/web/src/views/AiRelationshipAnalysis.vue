<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="AI关系预测" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">

      <div class="mb-6 bg-white rounded-xl shadow-lg p-6 animate-slide-up">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-cyan-100 to-cyan-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
          </div>
          <h2 class="text-lg font-semibold text-gray-900">智能关系预测</h2>
        </div>
        <p class="text-gray-600 mb-4">基于家族成员的年龄、姓名、性别等多维度信息，AI将分析并预测可能的家族关系。</p>

        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-1.5">选择家族</label>
          <Select
            v-model="selectedFamilyId"
            :options="familyOptions"
            placeholder="请选择家族"
          />
        </div>

        <button
          @click="analyzeRelationships"
          :disabled="!selectedFamilyId || aiRelationshipStore.loading"
          class="bg-green-500 hover:bg-green-600 text-white px-6 py-3 rounded-xl disabled:bg-gray-400 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5"
        >
          {{ aiRelationshipStore.loading ? '分析中...' : '开始分析' }}
        </button>
      </div>

      <div v-if="aiRelationshipStore.loading" class="flex justify-center items-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        <span class="ml-4 text-gray-600">AI正在分析家族关系...</span>
      </div>

      <div v-else-if="aiRelationshipStore.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ aiRelationshipStore.error }}
      </div>

      <div v-if="aiRelationshipStore.predictedRelationships.length > 0" class="space-y-6">
        <div class="bg-white rounded-lg shadow p-6">
          <h3 class="text-lg font-semibold mb-4">预测关系结果</h3>
          <p class="text-gray-600 mb-4">共发现 {{ aiRelationshipStore.predictedRelationships.length }} 种可能的关系</p>

          <div class="space-y-4">
            <div v-for="(rel, index) in aiRelationshipStore.predictedRelationships" :key="index"
                 class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
              <div class="flex items-center justify-between mb-2">
                <div class="flex items-center">
                  <span class="font-medium text-lg">{{ rel.member1?.name || '未知' }}</span>
                  <span class="mx-3 text-gray-400">↔</span>
                  <span class="font-medium text-lg">{{ rel.member2?.name || '未知' }}</span>
                </div>
                <span :class="getConfidenceClass(rel.confidence)"
                      class="px-3 py-1 rounded-full text-sm font-medium">
                  {{ (rel.confidence * 100).toFixed(0) }}% 置信度
                </span>
              </div>

              <div class="flex items-center mb-2">
                <span class="px-3 py-1 bg-blue-100 text-blue-800 rounded-md text-sm font-medium">
                  {{ getRelationshipTypeText(rel.relationshipType) }}
                </span>
              </div>

              <div v-if="rel.reasoning" class="mt-2 text-sm text-gray-600">
                <span class="font-medium">分析依据：</span>{{ rel.reasoning }}
              </div>
            </div>
          </div>
        </div>

        <div v-if="aiRelationshipStore.completionSuggestions && aiRelationshipStore.completionSuggestions.length > 0"
             class="bg-yellow-50 border border-yellow-200 rounded-lg shadow p-6">
          <h3 class="text-lg font-semibold mb-4 text-yellow-800">📋 数据补全建议</h3>
          <ul class="space-y-2">
            <li v-for="(suggestion, index) in aiRelationshipStore.completionSuggestions" :key="index"
                class="flex items-start">
              <span class="text-yellow-600 mr-2">•</span>
              <span class="text-gray-700">{{ suggestion }}</span>
            </li>
          </ul>
        </div>
      </div>

      <div v-else-if="selectedFamilyId && !aiRelationshipStore.loading" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
        <p class="text-gray-500">点击"开始分析"按钮，AI将预测家族成员之间的关系</p>
      </div>

      <div v-else-if="!selectedFamilyId" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
        <p class="text-gray-500">请先选择一个家族开始AI关系预测</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAiRelationshipStore } from '../stores/aiRelationship'
import { useFamilyStore } from '../stores/family'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'
import Select from '../components/Select.vue'

const aiRelationshipStore = useAiRelationshipStore()
const familyStore = useFamilyStore()
const userStore = useUserStore()

const selectedFamilyId = ref('')

const families = computed(() => familyStore.families)

const familyOptions = computed(() => {
  return families.value.map(family => ({
    value: family.id,
    label: family.name
  }))
})

onMounted(async () => {
  if (!userStore.user) {
    await userStore.fetchCurrentUser()
  }
  await familyStore.fetchFamilies()
})

const analyzeRelationships = async () => {
  if (selectedFamilyId.value) {
    const result = await aiRelationshipStore.predictRelationships(selectedFamilyId.value)
    if (result) {
      await aiRelationshipStore.getCompletionSuggestions(selectedFamilyId.value)
    }
  }
}

const getRelationshipTypeText = (type) => {
  const typeMap = {
    'parent_child': '父子/母子',
    'siblings': '兄弟姐妹',
    'spouse': '配偶',
    'grandparent_grandchild': '祖孙',
    'granduncle_grandniece': '叔侄/姑侄',
    'cousin': '表/堂兄弟姐妹',
    'in_laws': '姻亲'
  }
  return typeMap[type] || type || '未知关系'
}

const getConfidenceClass = (confidence) => {
  if (confidence >= 0.8) return 'bg-green-100 text-green-800'
  if (confidence >= 0.6) return 'bg-yellow-100 text-yellow-800'
  return 'bg-gray-100 text-gray-800'
}
</script>
