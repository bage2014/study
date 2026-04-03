<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <button @click="navigateTo('/home')" class="mr-4 text-gray-600 hover:text-gray-900">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h1 class="text-xl font-bold text-gray-900">成员管理</h1>
          </div>
          <div class="flex items-center">
            <button @click="openAddMemberModal" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
              添加成员
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white p-6 rounded-lg shadow">
        <!-- Family Selector -->
        <div class="mb-6">
          <label for="family" class="block text-sm font-medium text-gray-700 mb-2">选择家族</label>
          <div class="flex space-x-2">
            <select id="family" v-model="selectedFamilyId" @change="fetchMembers" class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary">
              <option value="">请选择家族</option>
              <option v-for="family in familyStore.families" :key="family.id" :value="family.id">{{ family.name }}</option>
            </select>
            <button 
              @click="viewOrEditFamily" 
              :disabled="!selectedFamilyId"
              class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
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
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
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
                  去世日期
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="member in familyMembers" :key="member.id">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <img v-if="member.photo" :src="member.photo" :alt="member.name" class="h-10 w-10 rounded-full">
                      <div v-else class="h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ member.name }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                    {{ member.gender === 'male' ? '男' : '女' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ member.birthDate || '未知' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ member.deathDate || '在世' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button @click="navigateTo(`/member/${member.id}`)" class="text-primary hover:text-blue-700 mr-3">
                    查看
                  </button>
                  <button @click="editMember(member)" class="text-green-600 hover:text-green-800 mr-3">
                    编辑
                  </button>
                  <button @click="deleteMember(member.id)" class="text-red-600 hover:text-red-800">
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
    <div v-if="showModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">{{ editingMember ? '编辑成员' : '添加成员' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label for="name" class="block text-sm font-medium text-gray-700">姓名</label>
              <input type="text" id="name" v-model="form.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="gender" class="block text-sm font-medium text-gray-700">性别</label>
              <select id="gender" v-model="form.gender" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
                <option value="">请选择</option>
                <option value="male">男</option>
                <option value="female">女</option>
              </select>
            </div>
            <div>
              <label for="birthDate" class="block text-sm font-medium text-gray-700">出生日期</label>
              <input type="date" id="birthDate" v-model="form.birthDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="deathDate" class="block text-sm font-medium text-gray-700">去世日期</label>
              <input type="date" id="deathDate" v-model="form.deathDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700">手机号</label>
              <input type="tel" id="phone" v-model="form.phone" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700">邮箱</label>
              <input type="email" id="email" v-model="form.email" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="details" class="block text-sm font-medium text-gray-700">详细信息</label>
              <textarea id="details" v-model="form.details" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" :disabled="memberStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-50">
              {{ memberStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Family Edit Modal -->
    <div v-if="showFamilyModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">编辑家族</h3>
        <form @submit.prevent="handleFamilySubmit">
          <div class="space-y-4">
            <div>
              <label for="familyName" class="block text-sm font-medium text-gray-700">家族名称</label>
              <input type="text" id="familyName" v-model="familyForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="familyDescription" class="block text-sm font-medium text-gray-700">家族描述</label>
              <textarea id="familyDescription" v-model="familyForm.description" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showFamilyModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-50">
              {{ familyStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useMemberStore } from '../stores/member'
import { useRouter } from 'vue-router'

export default {
  name: 'Members',
  setup() {
    const familyStore = useFamilyStore()
    const memberStore = useMemberStore()
    const router = useRouter()
    const selectedFamilyId = ref('')
    const showModal = ref(false)
    const showFamilyModal = ref(false)
    const editingMember = ref(null)
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

    const selectedFamily = computed(() => {
      if (!selectedFamilyId.value) return null
      return familyStore.families.find(f => f.id === selectedFamilyId.value)
    })

    const formatDate = (dateString) => {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    const openAddMemberModal = () => {
      if (!selectedFamilyId.value) {
        alert('请先选择家族')
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

    const deleteMember = async (memberId) => {
      if (confirm('确定要删除这个成员吗？')) {
        try {
          await memberStore.deleteMember(memberId)
          alert('成员删除成功')
        } catch (error) {
          alert('成员删除失败: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    const handleSubmit = async () => {
      try {
        if (editingMember.value) {
          // 编辑成员
          await memberStore.updateMember(editingMember.value.id, form.value)
          alert('成员更新成功')
        } else {
          // 添加成员
          await memberStore.createMember({
            ...form.value,
            familyId: selectedFamilyId.value
          })
          alert('成员添加成功')
        }
        showModal.value = false
        await fetchMembers()
      } catch (error) {
        alert('操作失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const handleFamilySubmit = async () => {
      try {
        await familyStore.updateFamily(selectedFamilyId.value, familyForm.value)
        alert('家族更新成功')
        showFamilyModal.value = false
        await familyStore.fetchFamilies()
      } catch (error) {
        alert('家族更新失败: ' + (error.response?.data?.message || error.message))
      }
    }

    onMounted(async () => {
      await familyStore.fetchFamilies()
    })

    return {
      familyStore,
      memberStore,
      familyMembers,
      selectedFamily,
      selectedFamilyId,
      showModal,
      showFamilyModal,
      editingMember,
      form,
      familyForm,
      navigateTo,
      fetchMembers,
      formatDate,
      openAddMemberModal,
      viewOrEditFamily,
      editMember,
      deleteMember,
      handleSubmit,
      handleFamilySubmit
    }
  }
}
</script>