<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="成员管理">
      <template #actions>
        <button @click="openAddMemberModal" class="btn-primary">
          添加成员
        </button>
      </template>
    </Header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div class="bg-white p-6 rounded-lg shadow animate-slide-up">
        <!-- Family Selector -->
        <div class="mb-6">
          <label for="family" class="block text-sm font-medium text-gray-700 mb-2">选择家族</label>
          <div class="flex space-x-2">
            <Select
              id="family"
              v-model="selectedFamilyId"
              :options="familyOptions"
              placeholder="请选择家族"
              @change="fetchMembers"
              class="flex-1"
            />
            <button 
              @click="viewOrEditFamily" 
              :disabled="!selectedFamilyId"
              class="btn-primary"
            >
              查看/编辑家族
            </button>
          </div>
          
          <!-- Current Family Info -->
          <div v-if="selectedFamily" class="mt-4 p-4 bg-blue-50 rounded-md">
            <h3 class="text-sm font-medium text-gray-900 mb-2">当前家族信息</h3>
            <div class="grid grid-cols-2 gap-2 text-sm">
              <div><span class="font-medium">家族名称:</span> {{ selectedFamily.name }}</div>
              <div><span class="font-medium">创建时间:</span> {{ formatDate(selectedFamily.createdAt) }}</div>
              <div class="col-span-2"><span class="font-medium">描述:</span> {{ selectedFamily.description || '无描述' }}</div>
            </div>
          </div>
        </div>

        <!-- Members List -->
        <div v-if="memberStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="familyMembers.length === 0" class="text-center py-16">
          <p class="text-gray-600">暂无成员数据</p>
        </div>
        <div v-else class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  姓名
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  性别
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  出生日期
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="member in familyMembers" :key="member.id" class="hover:bg-green-50 transition-colors duration-150 cursor-pointer hover:shadow-sm">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10 transition-transform duration-200 hover:scale-110">
                      <img v-if="member.photo" :src="member.photo" :alt="member.name" class="h-10 w-10 rounded-full object-cover ring-2 ring-gray-200 hover:ring-green-400 transition-all duration-200">
                      <div v-else class="h-10 w-10 rounded-full bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900 flex items-center">
                        {{ member.name }}
                        <span v-if="hasPassedAway(member.deathDate)" class="ml-2 px-2 py-0.5 text-xs bg-gray-100 text-gray-500 rounded-full">已去世</span>
                      </div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800 transform hover:scale-105 transition-transform duration-150">
                    {{ member.gender === 'male' ? '男' : '女' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ member.birthDate || '未知' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button @click="navigateTo(`/member/${member.id}`)" class="btn-text mr-3 inline-flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                    查看
                  </button>
                  <button @click="editMember(member)" class="btn-text mr-3 inline-flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                    编辑
                  </button>
                  <button @click="deleteMember(member.id)" class="btn-text-danger inline-flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                    删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <!-- Add/Edit Member Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">{{ editingMember ? '编辑成员' : '添加成员' }}</h3>
        </div>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label for="name" class="block text-sm font-medium text-gray-700 mb-1.5">姓名</label>
              <input type="text" id="name" v-model="form.name" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="gender" class="block text-sm font-medium text-gray-700 mb-1.5">性别</label>
              <Select
                id="gender"
                v-model="form.gender"
                :options="genderOptions"
                placeholder="请选择"
                required
              />
            </div>
            <div>
              <label for="birthDate" class="block text-sm font-medium text-gray-700 mb-1.5">出生日期</label>
              <input type="date" id="birthDate" v-model="form.birthDate" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="deathDate" class="block text-sm font-medium text-gray-700 mb-1.5">去世日期</label>
              <input type="date" id="deathDate" v-model="form.deathDate" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700 mb-1.5">手机号</label>
              <input type="tel" id="phone" v-model="form.phone" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700 mb-1.5">邮箱</label>
              <input type="email" id="email" v-model="form.email" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="details" class="block text-sm font-medium text-gray-700 mb-1.5">详细信息</label>
              <textarea id="details" v-model="form.details" rows="3" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="memberStore.loading" class="btn-primary">
              {{ memberStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Family Edit Modal -->
    <div v-if="showFamilyModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showFamilyModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">编辑家族</h3>
        </div>
        <form @submit.prevent="handleFamilySubmit">
          <div class="space-y-4">
            <div>
              <label for="familyName" class="block text-sm font-medium text-gray-700 mb-1.5">家族名称</label>
              <input type="text" id="familyName" v-model="familyForm.name" required class="input-field">
            </div>
            <div>
              <label for="familyDescription" class="block text-sm font-medium text-gray-700 mb-1.5">家族描述</label>
              <textarea id="familyDescription" v-model="familyForm.description" rows="3" class="input-field"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showFamilyModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="btn-primary">
              {{ familyStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Delete Confirm Modal -->
    <ConfirmModal 
      :visible="showDeleteConfirm"
      title="确认删除"
      message="确定要删除该成员吗？此操作不可撤销。"
      type="delete"
      @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false"
    />
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useMemberStore } from '../stores/member'
import { useRouter } from 'vue-router'
import Header from '../components/Header.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import Select from '../components/Select.vue'

export default {
  name: 'Members',
  components: {
    Header,
    Select,
    ConfirmModal
  },
  setup() {
    const familyStore = useFamilyStore()
    const memberStore = useMemberStore()
    const router = useRouter()
    const selectedFamilyId = ref('')
    const showModal = ref(false)
    const showFamilyModal = ref(false)
    const showDeleteConfirm = ref(false)
    const editingMember = ref(null)
    const deletingMemberId = ref(null)

    const form = ref({
      name: '',
      gender: '',
      birthDate: '',
      deathDate: '',
      phone: '',
      email: '',
      details: ''
    })
    const familyForm = ref({
      name: '',
      description: ''
    })

    const showToastMsg = (message, type = 'info') => {
      window.showToastMessage(message, type)
    }

    const navigateTo = (path) => {
      router.push(path)
    }

    const fetchMembers = async () => {
      if (selectedFamilyId.value) {
        await memberStore.fetchMembersByFamilyId(selectedFamilyId.value)
      }
    }

    const familyMembers = computed(() => {
      return memberStore.getMembersByFamilyId(selectedFamilyId.value)
    })

    const familyOptions = computed(() => {
      return familyStore.families.map(family => ({
        value: family.id,
        label: family.name
      }))
    })

    const genderOptions = [
      { value: 'male', label: '男' },
      { value: 'female', label: '女' }
    ]

    const selectedFamily = computed(() => {
      if (!selectedFamilyId.value) return null
      return familyStore.families.find(f => f.id === selectedFamilyId.value)
    })

    const formatDate = (dateString) => {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    const hasPassedAway = (deathDate) => {
      if (!deathDate) return false
      const death = new Date(deathDate)
      const today = new Date()
      return death <= today
    }

    const openAddMemberModal = () => {
      if (!selectedFamilyId.value) {
        showToastMsg('请先选择家族', 'warning')
        return
      }
      editingMember.value = null
      form.value = {
        name: '',
        gender: '',
        birthDate: '',
        deathDate: '',
        phone: '',
        email: '',
        details: ''
      }
      showModal.value = true
    }

    const viewOrEditFamily = () => {
      if (!selectedFamily.value) return
      familyForm.value = {
        name: selectedFamily.value.name,
        description: selectedFamily.value.description || ''
      }
      showFamilyModal.value = true
    }

    const editMember = (member) => {
      editingMember.value = member
      form.value = {
        name: member.name,
        gender: member.gender,
        birthDate: member.birthDate,
        deathDate: member.deathDate,
        phone: member.phone || '',
        email: member.email || '',
        details: member.details
      }
      showModal.value = true
    }

    const deleteMember = (memberId) => {
      deletingMemberId.value = memberId
      showDeleteConfirm.value = true
    }

    const confirmDelete = async () => {
      try {
        await memberStore.deleteMember(deletingMemberId.value)
        showToastMsg('成员删除成功', 'success')
      } catch (error) {
        showToastMsg('成员删除失败: ' + (error.response?.data?.message || error.message), 'error')
      } finally {
        showDeleteConfirm.value = false
        deletingMemberId.value = null
      }
    }

    const handleSubmit = async () => {
      try {
        if (editingMember.value) {
          // 编辑成员
          await memberStore.updateMember(editingMember.value.id, form.value)
          showToastMsg('成员更新成功', 'success')
        } else {
          // 添加成员
          await memberStore.createMember({
            ...form.value,
            familyId: selectedFamilyId.value
          })
          showToastMsg('成员添加成功', 'success')
        }
        showModal.value = false
        await fetchMembers()
      } catch (error) {
        showToastMsg('操作失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const handleFamilySubmit = async () => {
      try {
        await familyStore.updateFamily(selectedFamilyId.value, familyForm.value)
        showToastMsg('家族更新成功', 'success')
        showFamilyModal.value = false
        await familyStore.fetchFamilies()
      } catch (error) {
        showToastMsg('家族更新失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    onMounted(async () => {
      await familyStore.fetchFamilies()
      if (familyStore.families.length > 0) {
        selectedFamilyId.value = familyStore.families[0].id
        await fetchMembers()
      }
    })

    return {
      familyStore,
      memberStore,
      familyMembers,
      familyOptions,
      selectedFamily,
      selectedFamilyId,
      showModal,
      showFamilyModal,
      showDeleteConfirm,
      editingMember,
      form,
      familyForm,
      navigateTo,
      fetchMembers,
      formatDate,
      hasPassedAway,
      openAddMemberModal,
      viewOrEditFamily,
      editMember,
      deleteMember,
      confirmDelete,
      handleSubmit,
      handleFamilySubmit,
      showToastMsg
    }
  }
}
</script>