<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="成员大事件记录" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">

    <!-- 成员选择器 -->
    <div class="bg-white p-6 rounded-xl shadow-lg mb-6 animate-slide-up">
      <div class="flex items-center mb-4">
        <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-yellow-100 to-yellow-200 flex items-center justify-center mr-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <h2 class="text-lg font-semibold text-gray-900">选择成员</h2>
      </div>
      <select v-model="selectedMemberId" @change="fetchMilestones" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
        <option value="">请选择成员</option>
        <option v-for="member in members" :key="member.id" :value="member.id">
          {{ member.name }}
        </option>
      </select>
    </div>

    <!-- 加载状态 -->
    <div v-if="milestoneStore.loading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-green-500"></div>
    </div>

    <!-- 错误信息 -->
    <div v-else-if="milestoneStore.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ milestoneStore.error }}
    </div>

    <!-- 大事件列表 -->
    <div v-else-if="milestones.length > 0" class="space-y-4">
      <div v-for="(milestone, index) in milestones" :key="milestone.id" class="bg-white border border-gray-200 rounded-xl p-5 shadow-sm hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1 animate-fade-in" :style="{ animationDelay: `${index * 30}ms` }">
        <div class="flex justify-between items-start">
          <div class="flex items-center">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-yellow-100 to-yellow-200 flex items-center justify-center mr-3 flex-shrink-0">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h2 class="text-lg font-semibold text-gray-900">{{ milestone.title }}</h2>
          </div>
          <div class="flex space-x-2">
            <button @click="editMilestone(milestone)" class="text-blue-500 hover:text-blue-600 p-2 hover:bg-blue-50 rounded-lg transition-all duration-200">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </button>
            <button @click="deleteMilestone(milestone.id)" class="text-red-500 hover:text-red-600 p-2 hover:bg-red-50 rounded-lg transition-all duration-200">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>
        <div class="mt-2 text-sm text-gray-500">
          <span>创建时间：{{ formatDate(milestone.createdAt) }}</span>
          <span class="mx-2">|</span>
          <span>更新时间：{{ formatDate(milestone.updatedAt) }}</span>
        </div>
        <div class="mt-4 text-gray-700" v-html="milestone.content"></div>
      </div>
    </div>

    <!-- 无大事件提示 -->
    <div v-else-if="selectedMemberId" class="bg-gray-50 border border-gray-200 rounded-lg p-8 text-center">
      <p class="text-gray-500">该成员暂无大事件记录</p>
    </div>

    <!-- 添加大事件按钮 -->
    <div v-if="selectedMemberId" class="mt-6">
      <button @click="showAddModal = true" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md">
        添加大事件
      </button>
    </div>

    <!-- 添加/编辑大事件模态框 -->
    <div v-if="showAddModal || showEditModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
        <h2 class="text-xl font-bold mb-4">{{ showAddModal ? '添加大事件' : '编辑大事件' }}</h2>
        
        <form @submit.prevent="saveMilestone">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">标题</label>
            <input v-model="formData.title" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500" required>
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">内容</label>
            <div class="border border-gray-300 rounded-md">
              <!-- 简单的富文本编辑器 -->
              <div class="p-2 border-b border-gray-200 flex space-x-2">
                <button type="button" @click="formatText('bold')" class="text-gray-600 hover:text-gray-900">
                  <strong>B</strong>
                </button>
                <button type="button" @click="formatText('italic')" class="text-gray-600 hover:text-gray-900">
                  <em>I</em>
                </button>
                <button type="button" @click="formatText('underline')" class="text-gray-600 hover:text-gray-900">
                  <u>U</u>
                </button>
                <button type="button" @click="insertImage" class="text-gray-600 hover:text-gray-900">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                </button>
              </div>
              <div contenteditable="true" v-html="formData.content" @input="updateContent" class="p-3 min-h-[200px] focus:outline-none"></div>
            </div>
          </div>
          
          <div class="mb-4">
            <label class="flex items-center">
              <input v-model="formData.isPublic" type="checkbox" class="mr-2">
              <span class="text-sm text-gray-700">公开</span>
            </label>
          </div>
          
          <div class="flex justify-end space-x-2">
            <button type="button" @click="showAddModal = false; showEditModal = false; resetForm()" class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md">
              保存
            </button>
          </div>
        </form>
      </div>
    </div>
  </main>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useMilestoneStore } from '../stores/milestone'
import { useMemberStore } from '../stores/member'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'

const milestoneStore = useMilestoneStore()
const memberStore = useMemberStore()
const userStore = useUserStore()

const selectedMemberId = ref('')
const showAddModal = ref(false)
const showEditModal = ref(false)
const currentMilestoneId = ref(null)

const formData = ref({
  title: '',
  content: '',
  isPublic: true
})

const members = computed(() => memberStore.members)
const milestones = computed(() => milestoneStore.milestones)

onMounted(async () => {
  if (!userStore.user) {
    await userStore.fetchCurrentUser()
  }
  await memberStore.fetchMembers()
})

const fetchMilestones = async () => {
  if (selectedMemberId.value) {
    await milestoneStore.fetchMilestonesByMemberId(selectedMemberId.value)
  }
}

const formatText = (format) => {
  document.execCommand(format, false, null)
}

const insertImage = () => {
  const imageUrl = prompt('请输入图片URL:')
  if (imageUrl) {
    document.execCommand('insertImage', false, imageUrl)
  }
}

const updateContent = (event) => {
  formData.value.content = event.target.innerHTML
}

const resetForm = () => {
  formData.value = {
    title: '',
    content: '',
    isPublic: true
  }
  currentMilestoneId.value = null
}

const saveMilestone = async () => {
  const milestoneData = {
    ...formData.value,
    member: {
      id: selectedMemberId.value
    },
    creatorId: userStore.user.id
  }

  if (showAddModal.value) {
    await milestoneStore.createMilestone(milestoneData)
  } else if (showEditModal.value) {
    await milestoneStore.updateMilestone(currentMilestoneId.value, milestoneData)
  }

  showAddModal.value = false
  showEditModal.value = false
  resetForm()
}

const editMilestone = async (milestone) => {
  currentMilestoneId.value = milestone.id
  formData.value = {
    title: milestone.title,
    content: milestone.content,
    isPublic: milestone.isPublic
  }
  showEditModal.value = true
}

const deleteMilestone = async (id) => {
  if (confirm('确定要删除这个大事件吗？')) {
    await milestoneStore.deleteMilestone(id)
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>
