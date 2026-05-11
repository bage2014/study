<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="AI家族故事" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">

      <div class="mb-6 bg-white rounded-xl shadow-lg p-6 animate-slide-up">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
          </div>
          <h2 class="text-lg font-semibold text-gray-900">智能故事生成</h2>
        </div>
        <p class="text-gray-600 mb-4">基于家族成员信息，AI将为您创作独特的家族故事，记录家族的历史与传承。</p>

        <div class="grid md:grid-cols-2 gap-4 mb-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">选择家族</label>
            <Select
              v-model="selectedFamilyId"
              :options="familyOptions"
              placeholder="请选择家族"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1.5">故事类型</label>
            <Select
              v-model="selectedStoryType"
              :options="storyTypeOptions"
              placeholder="请选择故事类型"
            />
          </div>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-1.5">关键词（可选）</label>
          <input v-model="keywords" type="text"
                 placeholder="输入关键词，用逗号分隔，如：传承、孝道、勤劳"
                 class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
        </div>

        <button
          @click="generateStory"
          :disabled="!selectedFamilyId || familyStoryStore.loading"
          class="bg-green-500 hover:bg-green-600 text-white px-6 py-3 rounded-xl disabled:bg-gray-400 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5"
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
import Select from '../components/Select.vue'

const familyStoryStore = useFamilyStoryStore()
const familyStore = useFamilyStore()
const userStore = useUserStore()

const selectedFamilyId = ref('')
const selectedStoryType = ref('migration')
const keywords = ref('')

const families = computed(() => familyStore.families)

const familyOptions = computed(() => {
  return families.value.map(family => ({
    value: family.id,
    label: family.name
  }))
})

const storyTypeOptions = [
  { value: 'migration', label: '迁徙故事' },
  { value: 'biography', label: '人物传记' },
  { value: 'legend', label: '传奇故事' },
  { value: 'default', label: '综合故事' }
]

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
