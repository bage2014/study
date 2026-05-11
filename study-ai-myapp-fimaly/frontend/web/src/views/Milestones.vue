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
      <Select
        v-model="selectedMemberId"
        :options="memberOptions"
        placeholder="请选择成员"
        @change="fetchMilestones"
      />
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
    <div v-if="showAddModal || showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showAddModal = false; showEditModal = false; resetForm()">
      <div class="bg-white rounded-xl shadow-2xl max-w-2xl w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-indigo-100 to-indigo-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">{{ showAddModal ? '添加大事件' : '编辑大事件' }}</h3>
        </div>
        
        <form @submit.prevent="saveMilestone">
          <div class="space-y-4">
            <div>
              <label class="form-label">标题</label>
              <input v-model="formData.title" type="text" class="input-field" required>
            </div>
            
            <div>
              <label class="form-label">内容</label>
              <div class="border border-gray-200 rounded-xl overflow-hidden">
                <div class="p-2 border-b border-gray-100 flex space-x-2">
                  <button type="button" @click="formatText('bold')" class="text-gray-600 hover:text-gray-900 hover:bg-gray-100 p-1.5 rounded cursor-pointer transition-colors">
                    <strong>B</strong>
                  </button>
                  <button type="button" @click="formatText('italic')" class="text-gray-600 hover:text-gray-900 hover:bg-gray-100 p-1.5 rounded cursor-pointer transition-colors">
                    <em>I</em>
                  </button>
                  <button type="button" @click="formatText('underline')" class="text-gray-600 hover:text-gray-900 hover:bg-gray-100 p-1.5 rounded cursor-pointer transition-colors">
                    <u>U</u>
                  </button>
                  <button type="button" @click="insertImage" class="text-gray-600 hover:text-gray-900 hover:bg-gray-100 p-1.5 rounded cursor-pointer transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                  </button>
                </div>
                <div contenteditable="true" v-html="formData.content" @input="updateContent" class="p-4 min-h-[200px] focus:outline-none focus:ring-2 focus:ring-green-500 transition-all"></div>
              </div>
            </div>
            
            <div>
              <label class="flex items-center cursor-pointer">
                <input v-model="formData.isPublic" type="checkbox" class="w-4 h-4 text-green-500 border-gray-300 rounded focus:ring-green-500">
                <span class="ml-2 text-sm text-gray-700">公开</span>
              </label>
            </div>
          </div>
          
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showAddModal = false; showEditModal = false; resetForm()" class="btn-secondary">
              取消
            </button>
            <button type="submit" class="btn-primary">
              保存
            </button>
          </div>
        </form>
      </div>
    </div>
  </main>

  <!-- Delete Confirm Modal -->
  <ConfirmModal 
    :visible="showDeleteConfirm"
    title="确认删除"
    message="确定要删除这个大事件吗？"
    type="delete"
    @confirm="confirmDelete"
    @cancel="showDeleteConfirm = false"
  />

</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useMilestoneStore } from '../stores/milestone'
import { useMemberStore } from '../stores/member'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import Select from '../components/Select.vue'

const milestoneStore = useMilestoneStore()
const memberStore = useMemberStore()
const userStore = useUserStore()

const selectedMemberId = ref('')
const showAddModal = ref(false)
const showEditModal = ref(false)
const showDeleteConfirm = ref(false)
const deletingMilestoneId = ref(null)
const currentMilestoneId = ref(null)

const showToastMsg = (message, type = 'info') => {
  window.showToastMessage(message, type)
}

const formData = ref({
  title: '',
  content: '',
  isPublic: true
})

const members = computed(() => memberStore.members)
const memberOptions = computed(() => {
  return members.value.map(member => ({
    value: member.id,
    label: member.name
  }))
})

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

const deleteMilestone = (id) => {
  deletingMilestoneId.value = id
  showDeleteConfirm.value = true
}

const confirmDelete = async () => {
  try {
    await milestoneStore.deleteMilestone(deletingMilestoneId.value)
    showToastMsg('大事件删除成功', 'success')
  } catch (error) {
    showToastMsg('删除失败: ' + (error.response?.data?.message || error.message), 'error')
  } finally {
    showDeleteConfirm.value = false
    deletingMilestoneId.value = null
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>
