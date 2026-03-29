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
          <select id="family" v-model="selectedFamilyId" @change="fetchMembers" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary">
            <option value="">请选择家族</option>
            <option v-for="family in families" :key="family.id" :value="family.id">{{ family.name }}</option>
          </select>
        </div>

        <!-- Members List -->
        <div v-if="loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
        <div v-else-if="members.length === 0" class="text-center py-16">
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
              <tr v-for="member in members" :key="member.id">
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
              <label for="details" class="block text-sm font-medium text-gray-700">详细信息</label>
              <textarea id="details" v-model="form.details" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              保存
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'

export default {
  name: 'Members',
  setup() {
    const familyStore = useFamilyStore()
    const selectedFamilyId = ref('')
    const loading = ref(false)
    const showModal = ref(false)
    const editingMember = ref(null)
    const form = ref({
      name: '',
      gender: '',
      birthDate: '',
      deathDate: '',
      details: ''
    })

    const navigateTo = (path) => {
      window.location.href = path
    }

    const fetchMembers = async () => {
      if (selectedFamilyId.value) {
        loading.value = true
        await familyStore.fetchFamilyMembers(selectedFamilyId.value)
        loading.value = false
      }
    }

    const openAddMemberModal = () => {
      editingMember.value = null
      form.value = {
        name: '',
        gender: '',
        birthDate: '',
        deathDate: '',
        details: ''
      }
      showModal.value = true
    }

    const editMember = (member) => {
      editingMember.value = member
      form.value = {
        name: member.name,
        gender: member.gender,
        birthDate: member.birthDate,
        deathDate: member.deathDate,
        details: member.details
      }
      showModal.value = true
    }

    const deleteMember = (memberId) => {
      if (confirm('确定要删除这个成员吗？')) {
        // 这里应该调用删除成员的API
        console.log('删除成员:', memberId)
      }
    }

    const handleSubmit = async () => {
      if (editingMember.value) {
        // 编辑成员
        console.log('编辑成员:', form.value)
      } else {
        // 添加成员
        await familyStore.addFamilyMember(selectedFamilyId.value, form.value)
      }
      showModal.value = false
      await fetchMembers()
    }

    onMounted(async () => {
      await familyStore.fetchFamilies()
    })

    return {
      families: familyStore.families,
      members: familyStore.members,
      selectedFamilyId,
      loading,
      showModal,
      editingMember,
      form,
      navigateTo,
      fetchMembers,
      openAddMemberModal,
      editMember,
      deleteMember,
      handleSubmit
    }
  }
}
</script>