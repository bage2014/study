<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="AI家族故事" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

      <div class="mb-6 bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-semibold mb-4">智能故事生成</h2>
        <p class="text-gray-600 mb-4">基于家族成员信息，AI将为您创作独特的家族故事，记录家族的历史与传承。</p>

        <div class="grid md:grid-cols-2 gap-4 mb-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">选择家族</label>
            <select v-model="selectedFamilyId"
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="">请选择家族</option>
              <option v-for="family in families" :key="family.id" :value="family.id">
                {{ family.name }}
              </option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">故事类型</label>
            <select v-model="selectedStoryType"
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="migration">迁徙故事</option>
              <option value="biography">人物传记</option>
              <option value="legend">传奇故事</option>
              <option value="default">综合故事</option>
            </select>
          </div>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-1">关键词（可选）</label>
          <input v-model="keywords" type="text"
                 placeholder="输入关键词，用逗号分隔，如：传承、孝道、勤劳"
                 class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
        </div>

        <button
          @click="generateStory"
          :disabled="!selectedFamilyId || familyStoryStore.loading"
          class="bg-green-500 hover:bg-green-600 text-white px-6 py-2 rounded-md disabled:bg-gray-400 transition-colors"
        >
          {{ familyStoryStore.loading ? '生成中...' : '生成故事' }}
        </button>
      </div>

      <div v-if="familyStoryStore.loading" class="flex justify-center items-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        <span class="ml-4 text-gray-600">AI正在创作家族故事...</span>
      </div>

      <div v-else-if="familyStoryStore.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ familyStoryStore.error }}
      </div>

      <div v-if="familyStoryStore.currentStory" class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-xl font-semibold">{{ familyStoryStore.currentStory.title || '家族故事' }}</h3>
          <span class="text-sm text-gray-500">
            {{ getStoryTypeText(familyStoryStore.currentStory.storyType) }} |
            {{ familyStoryStore.currentStory.generationMethod === 'ai' ? 'AI生成' : '规则生成' }}
          </span>
        </div>

        <div class="prose max-w-none">
          <div v-html="formatContent(familyStoryStore.currentStory.content)"></div>
        </div>

        <div class="mt-6 pt-4 border-t border-gray-200">
          <button @click="copyStory"
                  class="text-green-600 hover:text-green-700 text-sm font-medium">
            复制故事内容
          </button>
        </div>
      </div>

      <div v-else-if="selectedFamilyId && !familyStoryStore.loading" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
        <p class="text-gray-500">点击"生成故事"按钮，AI将为您创作独特的家族故事</p>
      </div>

      <div v-else-if="!selectedFamilyId" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
        <p class="text-gray-500">请先选择一个家族开始生成家族故事</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStoryStore } from '../stores/familyStory'
import { useFamilyStore } from '../stores/family'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'

const familyStoryStore = useFamilyStoryStore()
const familyStore = useFamilyStore()
const userStore = useUserStore()

const selectedFamilyId = ref('')
const selectedStoryType = ref('migration')
const keywords = ref('')

const families = computed(() => familyStore.families)

onMounted(async () => {
  if (!userStore.user) {
    await userStore.fetchCurrentUser()
  }
  await familyStore.fetchFamilies()
  await familyStoryStore.getStoryTypes()
})

const generateStory = async () => {
  if (selectedFamilyId.value) {
    const keywordList = keywords.value ? keywords.value.split(',').map(k => k.trim()).filter(k => k) : []
    await familyStoryStore.generateFamilyStory(selectedFamilyId.value, selectedStoryType.value, keywordList)
  }
}

const getStoryTypeText = (type) => {
  const typeMap = {
    'migration': '迁徙故事',
    'biography': '人物传记',
    'legend': '传奇故事',
    'default': '综合故事'
  }
  return typeMap[type] || type || '未知类型'
}

const formatContent = (content) => {
  if (!content) return ''
  return content.replace(/\n/g, '<br>').replace(/#+\s/g, (match) => {
    const level = match.trim().length
    return `<h${level} class="text-lg font-semibold mt-4 mb-2">`.replace('h2', 'h3').replace('h1', 'h2')
  })
}

const copyStory = () => {
  if (familyStoryStore.currentStory?.content) {
    navigator.clipboard.writeText(familyStoryStore.currentStory.content)
  }
}
</script>
