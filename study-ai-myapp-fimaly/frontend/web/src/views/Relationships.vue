<template>
  <div class="min-h-screen bg-gray-50">
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <h1 class="text-xl font-bold text-gray-900">家族关系管理</h1>
          </div>
          <div class="flex items-center">
            <button @click="navigateTo('/home')" class="ml-4 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100">
              返回首页
            </button>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-1">
          <div class="bg-white p-6 rounded-lg shadow">
            <h2 class="text-lg font-medium text-gray-900 mb-4">添加关系</h2>
            <form @submit.prevent="handleCreateRelationship" class="space-y-4">
              <div>
                <label for="familyId" class="block text-sm font-medium text-gray-700 mb-1">选择家族</label>
                <select 
                  id="familyId" 
                  v-model="form.familyId" 
                  class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                >
                  <option value="">请选择家族</option>
                  <option v-for="family in families" :key="family.id" :value="family.id">{{ family.name }}</option>
                </select>
              </div>
              <div>
                <label for="member1Id" class="block text-sm font-medium text-gray-700 mb-1">成员1</label>
                <div class="flex space-x-2">
                  <select 
                    id="member1Id" 
                    v-model="form.member1Id" 
                    class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value="">请选择成员</option>
                    <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
                  </select>
                  <button 
                    type="button"
                    @click="searchMember(1)"
                    class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
                  >
                    搜索
                  </button>
                </div>
                <p v-if="selectedMember1" class="mt-1 text-sm text-gray-600">
                  已选择: {{ selectedMember1.name }} (ID: {{ selectedMember1.id }})
                </p>
              </div>
              <div>
                <label for="member2Id" class="block text-sm font-medium text-gray-700 mb-1">成员2</label>
                <div class="flex space-x-2">
                  <select 
                    id="member2Id" 
                    v-model="form.member2Id" 
                    class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value="">请选择成员</option>
                    <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
                  </select>
                  <button 
                    type="button"
                    @click="searchMember(2)"
                    class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
                  >
                    搜索
                  </button>
                </div>
                <p v-if="selectedMember2" class="mt-1 text-sm text-gray-600">
                  已选择: {{ selectedMember2.name }} (ID: {{ selectedMember2.id }})
                </p>
              </div>
              <div>
                <label for="relationshipType" class="block text-sm font-medium text-gray-700 mb-1">关系类型</label>
                <select 
                  id="relationshipType" 
                  v-model="form.relationshipType" 
                  class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                >
                  <option value="">请选择关系类型</option>
                  <option value="FATHER">父亲</option>
                  <option value="MOTHER">母亲</option>
                  <option value="SON">儿子</option>
                  <option value="DAUGHTER">女儿</option>
                  <option value="HUSBAND">丈夫</option>
                  <option value="WIFE">妻子</option>
                  <option value="BROTHER">兄弟</option>
                  <option value="SISTER">姐妹</option>
                  <option value="GRANDFATHER">祖父</option>
                  <option value="GRANDMOTHER">祖母</option>
                  <option value="GRANDSON">孙子</option>
                  <option value="GRANDDAUGHTER">孙女</option>
                  <option value="UNCLE">叔叔</option>
                  <option value="AUNT">阿姨</option>
                  <option value="COUSIN">堂表亲</option>
                </select>
              </div>
              <button 
                type="submit" 
                :disabled="relationshipStore.loading"
                class="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 disabled:opacity-50"
              >
                {{ relationshipStore.loading ? '添加中...' : '添加关系' }}
              </button>
            </form>
          </div>
        </div>

        <div class="lg:col-span-2">
          <div class="bg-white p-6 rounded-lg shadow">
            <h2 class="text-lg font-medium text-gray-900 mb-4">关系列表</h2>
            
            <div class="mb-4">
              <label for="searchMemberId" class="block text-sm font-medium text-gray-700 mb-1">按成员ID筛选</label>
              <div class="flex space-x-2">
                <input 
                  type="number" 
                  id="searchMemberId" 
                  v-model="searchMemberId" 
                  placeholder="输入成员ID"
                  class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <button 
                  @click="handleSearch"
                  class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  搜索
                </button>
                <button 
                  @click="handleReset"
                  class="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300"
                >
                  重置
                </button>
              </div>
            </div>

            <div v-if="relationshipStore.loading" class="flex justify-center py-8">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
            <div v-else-if="filteredRelationships.length === 0" class="text-center py-8">
              <p class="text-gray-600">暂无关系数据</p>
            </div>
            <div v-else class="space-y-4">
              <div 
                v-for="relationship in filteredRelationships" 
                :key="relationship.id" 
                class="border rounded-md p-4 hover:shadow-md"
              >
                <div class="flex justify-between items-start">
                  <div class="flex-1">
                    <div class="flex items-center space-x-4">
                      <div class="flex items-center space-x-2">
                        <span class="text-sm font-medium text-gray-700">成员1:</span>
                        <span class="text-sm text-gray-900">{{ getMemberName(relationship.member1Id) }}</span>
                      </div>
                      <div class="flex items-center space-x-2">
                        <span class="text-sm font-medium text-gray-700">成员2:</span>
                        <span class="text-sm text-gray-900">{{ getMemberName(relationship.member2Id) }}</span>
                      </div>
                      <div class="flex items-center space-x-2">
                        <span class="text-sm font-medium text-gray-700">关系:</span>
                        <span class="px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-800">
                          {{ getRelationshipTypeText(relationship.relationshipType) }}
                        </span>
                      </div>
                    </div>
                  </div>
                  <button 
                    @click="handleDelete(relationship.id)"
                    class="ml-4 px-3 py-1 text-sm bg-red-100 text-red-800 rounded-md hover:bg-red-200"
                  >
                    删除
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { useRelationshipStore } from '../stores/relationship'
import { useMemberStore } from '../stores/member'
import { useFamilyStore } from '../stores/family'
import { useRouter, useRoute } from 'vue-router'
import { ref, computed, onMounted, watch } from 'vue'

export default {
  name: 'Relationships',
  setup() {
    const relationshipStore = useRelationshipStore()
    const memberStore = useMemberStore()
    const familyStore = useFamilyStore()
    const router = useRouter()
    const route = useRoute()

    const form = ref({
      member1Id: '',
      member2Id: '',
      relationshipType: '',
      familyId: ''
    })

    // 从URL参数获取familyId
    const familyId = route.query.familyId
    if (familyId) {
      form.value.familyId = parseInt(familyId)
    }

    const searchMemberId = ref('')
    const currentSearchTarget = ref(null)

    const navigateTo = (path) => {
      router.push(path)
    }

    const searchMember = (target) => {
      currentSearchTarget.value = target
      router.push({
        path: '/member-search',
        query: {
          from: 'relationships',
          memberId: target
        }
      })
    }

    const selectedMember1 = computed(() => {
      if (!form.value.member1Id) return null
      return memberStore.members.find(m => m.id === form.value.member1Id)
    })

    const selectedMember2 = computed(() => {
      if (!form.value.member2Id) return null
      return memberStore.members.find(m => m.id === form.value.member2Id)
    })

    const familyMembers = computed(() => {
      if (!form.value.familyId) return []
      return memberStore.members.filter(member => member.familyId === form.value.familyId)
    })

    const families = computed(() => {
      return familyStore.families
    })

    watch(() => route.query.selectedMemberId, (newVal) => {
      if (newVal) {
        const target = route.query.memberId
        if (target === '1') {
          form.value.member1Id = parseInt(newVal)
        } else if (target === '2') {
          form.value.member2Id = parseInt(newVal)
        }
        router.replace({ query: {} })
      }
    }, { immediate: true })

    const handleCreateRelationship = async () => {
      try {
        await relationshipStore.createRelationship(form.value)
        alert('关系添加成功')
        form.value = {
          member1Id: '',
          member2Id: '',
          relationshipType: ''
        }
        await loadRelationships()
      } catch (error) {
        alert('关系添加失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const handleDelete = async (id) => {
      if (confirm('确定要删除这个关系吗？')) {
        try {
          await relationshipStore.deleteRelationship(id)
          alert('关系删除成功')
          await loadRelationships()
        } catch (error) {
          alert('关系删除失败: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    const handleSearch = async () => {
      // 搜索功能已通过filteredRelationships计算属性实现
    }

    const handleReset = () => {
      searchMemberId.value = ''
    }

    const loadRelationships = async () => {
      await relationshipStore.fetchRelationships()
    }

    const loadMembers = async () => {
      await memberStore.fetchMembers()
    }

    const loadFamilies = async () => {
      await familyStore.fetchFamilies()
    }

    const getRelationshipTypeText = (type) => {
      const typeMap = {
        'FATHER': '父亲',
        'MOTHER': '母亲',
        'SON': '儿子',
        'DAUGHTER': '女儿',
        'HUSBAND': '丈夫',
        'WIFE': '妻子',
        'BROTHER': '兄弟',
        'SISTER': '姐妹',
        'GRANDFATHER': '祖父',
        'GRANDMOTHER': '祖母',
        'GRANDSON': '孙子',
        'GRANDDAUGHTER': '孙女',
        'UNCLE': '叔叔',
        'AUNT': '阿姨',
        'COUSIN': '堂表亲'
      }
      return typeMap[type] || type
    }

    const getMemberName = (memberId) => {
      const member = memberStore.members.find(m => m.id === memberId)
      return member ? member.name : memberId
    }

    const filteredRelationships = computed(() => {
      if (!searchMemberId.value) {
        return relationshipStore.relationships
      }
      return relationshipStore.relationships.filter(rel => 
        rel.member1Id == searchMemberId.value || rel.member2Id == searchMemberId.value
      )
    })

    onMounted(async () => {
      await loadFamilies()
      await loadMembers()
      await loadRelationships()
    })

    return {
      form,
      searchMemberId,
      currentSearchTarget,
      relationshipStore,
      memberStore,
      familyStore,
      members: memberStore.members,
      families,
      familyMembers,
      filteredRelationships,
      selectedMember1,
      selectedMember2,
      navigateTo,
      searchMember,
      handleCreateRelationship,
      handleDelete,
      handleSearch,
      handleReset,
      getRelationshipTypeText,
      getMemberName
    }
  }
}
</script>
