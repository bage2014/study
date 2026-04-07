<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">家族故事生成</h1>

    <!-- 故事类型选择 -->
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">选择故事类型</label>
      <select v-model="storyType" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
        <option value="family">家族故事</option>
        <option value="member">成员故事</option>
      </select>
    </div>

    <!-- 家族选择器 -->
    <div v-if="storyType === 'family'" class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">选择家族</label>
      <select v-model="selectedFamilyId" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
        <option value="">请选择家族</option>
        <option v-for="family in families" :key="family.id" :value="family.id">
          {{ family.name }}
        </option>
      </select>
    </div>

    <!-- 成员选择器 -->
    <div v-else class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">选择成员</label>
      <select v-model="selectedMemberId" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
        <option value="">请选择成员</option>
        <option v-for="member in members" :key="member.id" :value="member.id">
          {{ member.name }}
        </option>
      </select>
    </div>

    <!-- 生成按钮 -->
    <div class="mb-4">
      <button 
        @click="generateStory" 
        :disabled="(!selectedFamilyId && storyType === 'family') || (!selectedMemberId && storyType === 'member') || familyStoryStore.loading"
        class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md disabled:bg-gray-400"
      >
        {{ familyStoryStore.loading ? '生成中...' : '生成故事' }}
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="familyStoryStore.loading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-green-500"></div>
    </div>

    <!-- 错误信息 -->
    <div v-else-if="familyStoryStore.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ familyStoryStore.error }}
    </div>

    <!-- 故事内容 -->
    <div v-else-if="familyStoryStore.familyStory || familyStoryStore.memberStory" class="mt-4">
      <h2 class="text-lg font-semibold mb-2">{{ storyType === 'family' ? '家族故事' : '成员故事' }}</h2>
      <div class="bg-white border border-gray-200 rounded-lg p-6 shadow-sm whitespace-pre-line">
        {{ storyType === 'family' ? familyStoryStore.familyStory : familyStoryStore.memberStory }}
      </div>
    </div>

    <!-- 提示信息 -->
    <div v-else class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
      <p class="text-gray-500">请选择故事类型和对应的家族或成员，然后点击"生成故事"按钮</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStoryStore } from '../stores/familyStory'
import { useFamilyStore } from '../stores/family'
import { useMemberStore } from '../stores/member'

const familyStoryStore = useFamilyStoryStore()
const familyStore = useFamilyStore()
const memberStore = useMemberStore()

const storyType = ref('family')
const selectedFamilyId = ref('')
const selectedMemberId = ref('')

const families = computed(() => familyStore.families)
const members = computed(() => memberStore.members)

onMounted(async () => {
  await familyStore.fetchFamilies()
  await memberStore.fetchMembers()
})

const generateStory = async () => {
  if (storyType.value === 'family' && selectedFamilyId.value) {
    await familyStoryStore.generateFamilyStory(selectedFamilyId.value)
  } else if (storyType.value === 'member' && selectedMemberId.value) {
    await familyStoryStore.generateMemberStory(selectedMemberId.value)
  }
}
</script>
