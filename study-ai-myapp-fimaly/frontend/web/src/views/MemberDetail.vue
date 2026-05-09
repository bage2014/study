<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="成员详情">
      <template #actions>
        <button @click="editMember" class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5">
          编辑
        </button>
      </template>
    </Header>

    <!-- Main Content -->
    <main class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div v-if="memberStore.loading" class="flex justify-center py-16">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
      </div>
      <div v-else-if="!memberStore.currentMember" class="bg-white p-8 rounded-xl shadow-lg text-center py-16 animate-slide-up">
        <div class="w-16 h-16 mx-auto bg-gray-100 rounded-full flex items-center justify-center mb-4">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <p class="text-gray-600">成员不存在</p>
      </div>
      <div v-else class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up">
        <!-- Member Profile -->
        <div class="bg-gradient-to-r from-green-500 to-green-600 p-8 text-white">
          <div class="flex flex-col md:flex-row items-center md:items-start">
            <div class="w-32 h-32 mb-4 md:mb-0 md:mr-8 relative">
              <img v-if="memberStore.currentMember.photo" :src="memberStore.currentMember.photo" :alt="memberStore.currentMember.name" class="w-32 h-32 rounded-full object-cover ring-4 ring-white/30 shadow-xl">
              <div v-else class="w-32 h-32 rounded-full bg-gradient-to-br from-white/20 to-white/10 flex items-center justify-center ring-4 ring-white/30">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
            </div>
            <div class="flex-1">
              <h2 class="text-3xl font-bold">{{ memberStore.currentMember.name }}</h2>
              <div class="mt-3 space-y-2">
                <div class="flex items-center">
                  <span class="w-24 text-sm font-medium text-white/80">性别:</span>
                  <span class="text-sm px-3 py-1 bg-white/20 rounded-full">{{ memberStore.currentMember.gender === 'male' ? '男' : '女' }}</span>
                </div>
                <div class="flex items-center">
                  <span class="w-24 text-sm font-medium text-white/80">出生日期:</span>
                  <span class="text-sm">{{ memberStore.currentMember.birthDate || '未知' }}</span>
                </div>
                <div class="flex items-center">
                  <span class="w-24 text-sm font-medium text-white/80">去世日期:</span>
                  <span class="text-sm">{{ memberStore.currentMember.deathDate || '在世' }}</span>
                </div>
                <div class="flex items-center">
                  <span class="w-24 text-sm font-medium text-white/80">家族:</span>
                  <span class="text-sm">{{ currentFamily?.name }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Member Details -->
        <div class="p-6 border-b border-gray-100">
          <div class="flex items-center mb-4">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-100 to-blue-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">详细信息</h3>
          </div>
          <div class="bg-gray-50 p-5 rounded-xl">
            <p class="text-gray-700 leading-relaxed">{{ memberStore.currentMember.details || '暂无详细信息' }}</p>
          </div>
        </div>

        <!-- Relationships -->
        <div class="p-6 border-b border-gray-100">
          <div class="flex items-center mb-4">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">家族关系</h3>
          </div>
          <div v-if="relationships.length === 0" class="bg-gray-50 p-5 rounded-xl text-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-300 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <p class="text-gray-500">暂无关系信息</p>
          </div>
          <div v-else class="space-y-3">
            <div v-for="(relationship, index) in relationships" :key="relationship.id" class="flex items-center p-4 bg-gray-50 rounded-xl hover:bg-green-50 transition-all duration-200 transform hover:-translate-x-1 cursor-pointer animate-fade-in" :style="{ animationDelay: `${index * 50}ms` }">
              <div class="w-12 h-12 mr-4 relative">
                <img v-if="relationship.relatedMember.photo" :src="relationship.relatedMember.photo" :alt="relationship.relatedMember.name" class="w-12 h-12 rounded-full object-cover ring-2 ring-green-300">
                <div v-else class="w-12 h-12 rounded-full bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
              </div>
              <div class="flex-1">
                <div class="font-medium text-gray-900">{{ relationship.relatedMember.name }}</div>
                <div class="text-sm text-green-600">{{ relationship.relationshipType }}</div>
              </div>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </div>
          </div>
        </div>

        <!-- Related Events -->
        <div class="p-6">
          <div class="flex items-center mb-4">
            <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-yellow-100 to-yellow-200 flex items-center justify-center mr-3">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-900">相关事件</h3>
          </div>
          <div v-if="events.length === 0" class="bg-gray-50 p-5 rounded-xl text-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-300 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <p class="text-gray-500">暂无相关事件</p>
          </div>
          <div v-else class="space-y-3">
            <div v-for="(event, index) in events" :key="event.id" class="p-4 bg-gray-50 rounded-xl hover:shadow-md transition-all duration-200 cursor-pointer animate-fade-in" :style="{ animationDelay: `${index * 50}ms` }">
              <div class="flex justify-between items-start">
                <h4 class="font-medium text-gray-900">{{ event.name }}</h4>
                <span class="text-sm px-2 py-1 bg-yellow-100 text-yellow-700 rounded-full">{{ event.eventDate }}</span>
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
              <input type="text" id="name" v-model="editForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="gender" class="block text-sm font-medium text-gray-700">性别</label>
              <select id="gender" v-model="editForm.gender" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                <option value="male">男</option>
                <option value="female">女</option>
              </select>
            </div>
            <div>
              <label for="birthDate" class="block text-sm font-medium text-gray-700">出生日期</label>
              <input type="date" id="birthDate" v-model="editForm.birthDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="deathDate" class="block text-sm font-medium text-gray-700">去世日期</label>
              <input type="date" id="deathDate" v-model="editForm.deathDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="details" class="block text-sm font-medium text-gray-700">详细信息</label>
              <textarea id="details" v-model="editForm.details" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showEditModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500">
              取消
            </button>
            <button type="submit" :disabled="memberStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50">
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
import Header from '../components/Header.vue'

export default {
  name: 'MemberDetail',
  components: {
    Header
  },
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