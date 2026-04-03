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
            <h1 class="text-xl font-bold text-gray-900">成员搜索</h1>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white p-6 rounded-lg shadow">
        <!-- Search Form -->
        <div class="mb-6">
          <h2 class="text-lg font-medium text-gray-900 mb-4">搜索成员</h2>
          <form @submit.prevent="handleSearch" class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">手机号</label>
              <input 
                type="tel" 
                id="phone" 
                v-model="searchForm.phone" 
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary"
                placeholder="输入手机号"
              />
            </div>
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
              <input 
                type="email" 
                id="email" 
                v-model="searchForm.email" 
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary"
                placeholder="输入邮箱"
              />
            </div>
            <div class="flex items-end">
              <button 
                type="submit" 
                :disabled="memberStore.loading"
                class="w-full px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
              >
                {{ memberStore.loading ? '搜索中...' : '搜索' }}
              </button>
            </div>
          </form>
        </div>

        <!-- Search Results -->
        <div v-if="memberStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
        <div v-else-if="searchResults.length === 0" class="text-center py-16">
          <p class="text-gray-600">暂无搜索结果</p>
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
                  手机号
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  邮箱
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  家族
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="member in searchResults" :key="member.id">
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
                  {{ member.phone || '未知' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ member.email || '未知' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ getFamilyName(member.familyId) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button @click="navigateTo(`/member/${member.id}`)" class="text-primary hover:text-blue-700 mr-3">
                    查看
                  </button>
                  <button @click="selectMember(member)" class="text-green-600 hover:text-green-800">
                    选择
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useMemberStore } from '../stores/member'
import { useFamilyStore } from '../stores/family'
import { useRouter } from 'vue-router'

export default {
  name: 'MemberSearch',
  setup() {
    const memberStore = useMemberStore()
    const familyStore = useFamilyStore()
    const router = useRouter()
    const searchForm = ref({
      phone: '',
      email: ''
    })

    const navigateTo = (path) => {
      router.push(path)
    }

    const handleSearch = async () => {
      try {
        await memberStore.searchMembers(searchForm.value)
      } catch (error) {
        alert('搜索失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const selectMember = (member) => {
      // 如果是从关联关系页面跳转过来的，返回时携带选中的成员信息
      const from = router.currentRoute.value.query.from
      if (from === 'relationships') {
        const memberId = router.currentRoute.value.query.memberId
        router.push({
          path: '/relationships',
          query: {
            selectedMemberId: member.id,
            memberId: memberId
          }
        })
      } else {
        // 否则跳转到成员详情页
        navigateTo(`/member/${member.id}`)
      }
    }

    const getFamilyName = (familyId) => {
      const family = familyStore.families.find(f => f.id === familyId)
      return family ? family.name : '未知'
    }

    const searchResults = computed(() => {
      return memberStore.members
    })

    onMounted(async () => {
      await familyStore.fetchFamilies()
    })

    return {
      memberStore,
      familyStore,
      searchForm,
      searchResults,
      navigateTo,
      handleSearch,
      selectMember,
      getFamilyName
    }
  }
}
</script>