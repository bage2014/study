<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <button @click="navigateTo('/members')" class="mr-4 text-gray-600 hover:text-gray-900">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h1 class="text-xl font-bold text-gray-900">成员详情</h1>
          </div>
          <div class="flex items-center">
            <button @click="editMember" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
              编辑
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div v-if="memberStore.loading" class="flex justify-center py-16">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
      <div v-else-if="!memberStore.currentMember" class="bg-white p-6 rounded-lg shadow text-center py-16">
        <p class="text-gray-600">成员不存在</p>
      </div>
      <div v-else class="bg-white p-6 rounded-lg shadow">
        <!-- Member Profile -->
        <div class="flex flex-col md:flex-row items-center md:items-start mb-8">
          <div class="w-32 h-32 mb-4 md:mb-0 md:mr-8">
            <img v-if="memberStore.currentMember.photo" :src="memberStore.currentMember.photo" :alt="memberStore.currentMember.name" class="w-32 h-32 rounded-full object-cover">
            <div v-else class="w-32 h-32 rounded-full bg-gray-200 flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
          </div>
          <div class="flex-1">
            <h2 class="text-2xl font-bold text-gray-900">{{ memberStore.currentMember.name }}</h2>
            <div class="mt-2 space-y-2">
              <div class="flex items-center">
                <span class="w-24 text-sm font-medium text-gray-500">性别:</span>
                <span class="text-sm text-gray-900">{{ memberStore.currentMember.gender === 'male' ? '男' : '女' }}</span>
              </div>
              <div class="flex items-center">
                <span class="w-24 text-sm font-medium text-gray-500">出生日期:</span>
                <span class="text-sm text-gray-900">{{ memberStore.currentMember.birthDate || '未知' }}</span>
              </div>
              <div class="flex items-center">
                <span class="w-24 text-sm font-medium text-gray-500">去世日期:</span>
                <span class="text-sm text-gray-900">{{ memberStore.currentMember.deathDate || '在世' }}</span>
              </div>
              <div class="flex items-center">
                <span class="w-24 text-sm font-medium text-gray-500">家族:</span>
                <span class="text-sm text-gray-900">{{ currentFamily?.name }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Member Details -->
        <div class="mb-8">
          <h3 class="text-lg font-medium text-gray-900 mb-4">详细信息</h3>
          <div class="bg-gray-50 p-4 rounded-md">
            <p class="text-gray-700">{{ memberStore.currentMember.details || '暂无详细信息' }}</p>
          </div>
        </div>

        <!-- Relationships -->
        <div class="mb-8">
          <h3 class="text-lg font-medium text-gray-900 mb-4">家族关系</h3>
          <div v-if="relationships.length === 0" class="bg-gray-50 p-4 rounded-md">
            <p class="text-gray-700">暂无关系信息</p>
          </div>
          <div v-else class="space-y-4">
            <div v-for="relationship in relationships" :key="relationship.id" class="flex items-center p-4 bg-gray-50 rounded-md">
              <div class="w-10 h-10 mr-4">
                <img v-if="relationship.relatedMember.photo" :src="relationship.relatedMember.photo" :alt="relationship.relatedMember.name" class="w-10 h-10 rounded-full object-cover">
                <div v-else class="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
              </div>
              <div>
                <div class="text-sm font-medium text-gray-900">{{ relationship.relatedMember.name }}</div>
                <div class="text-sm text-gray-500">{{ relationship.relationshipType }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Related Events -->
        <div>
          <h3 class="text-lg font-medium text-gray-900 mb-4">相关事件</h3>
          <div v-if="events.length === 0" class="bg-gray-50 p-4 rounded-md">
            <p class="text-gray-700">暂无相关事件</p>
          </div>
          <div v-else class="space-y-4">
            <div v-for="event in events" :key="event.id" class="p-4 bg-gray-50 rounded-md">
              <div class="flex justify-between items-start">
                <h4 class="font-medium text-gray-900">{{ event.name }}</h4>
                <span class="text-sm text-gray-500">{{ event.eventDate }}</span>
              </div>
              <p class="mt-2 text-sm text-gray-700">{{ event.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Edit Member Modal -->
    <div v-if="showEditModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">编辑成员信息</h3>
        <form @submit.prevent="handleUpdate">
          <div class="space-y-4">
            <div>
              <label for="name" class="block text-sm font-medium text-gray-700">姓名</label>
              <input type="text" id="name" v-model="editForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="gender" class="block text-sm font-medium text-gray-700">性别</label>
              <select id="gender" v-model="editForm.gender" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
                <option value="male">男</option>
                <option value="female">女</option>
              </select>
            </div>
            <div>
              <label for="birthDate" class="block text-sm font-medium text-gray-700">出生日期</label>
              <input type="date" id="birthDate" v-model="editForm.birthDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="deathDate" class="block text-sm font-medium text-gray-700">去世日期</label>
              <input type="date" id="deathDate" v-model="editForm.deathDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="details" class="block text-sm font-medium text-gray-700">详细信息</label>
              <textarea id="details" v-model="editForm.details" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showEditModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" :disabled="memberStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-50">
              {{ memberStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useMemberStore } from '../stores/member'
import { useFamilyStore } from '../stores/family'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'MemberDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const memberStore = useMemberStore()
    const familyStore = useFamilyStore()
    const showEditModal = ref(false)
    const currentFamily = ref(null)
    const relationships = ref([])
    const events = ref([])
    const editForm = ref({})

    const navigateTo = (path) => {
      router.push(path)
    }

    const fetchMemberDetails = async () => {
      const memberId = route.params.id
      if (memberId) {
        await memberStore.fetchMember(memberId)
        if (memberStore.currentMember) {
          // 这里可以添加获取家族信息、关系信息和事件信息的逻辑
          // 暂时使用模拟数据
          relationships.value = [
            {
              id: 1,
              relatedMember: {
                id: 2,
                name: '李四',
                photo: null
              },
              relationshipType: '配偶'
            },
            {
              id: 2,
              relatedMember: {
                id: 3,
                name: '张小明',
                photo: null
              },
              relationshipType: '父子'
            }
          ]
          events.value = [
            {
              id: 1,
              name: '张三出生',
              description: '张三出生于1920年1月1日',
              eventDate: '1920-01-01'
            },
            {
              id: 2,
              name: '张三结婚',
              description: '张三与李四结婚',
              eventDate: '1940-01-01'
            }
          ]
        }
      }
    }

    const editMember = () => {
      if (memberStore.currentMember) {
        editForm.value = {
          name: memberStore.currentMember.name,
          gender: memberStore.currentMember.gender,
          birthDate: memberStore.currentMember.birthDate,
          deathDate: memberStore.currentMember.deathDate,
          details: memberStore.currentMember.details
        }
        showEditModal.value = true
      }
    }

    const handleUpdate = async () => {
      try {
        await memberStore.updateMember(memberStore.currentMember.id, editForm.value)
        alert('成员信息更新成功')
        showEditModal.value = false
      } catch (error) {
        alert('更新失败: ' + (error.response?.data?.message || error.message))
      }
    }

    onMounted(() => {
      fetchMemberDetails()
    })

    return {
      memberStore,
      showEditModal,
      currentFamily,
      relationships,
      events,
      editForm,
      navigateTo,
      editMember,
      handleUpdate
    }
  }
}
</script>