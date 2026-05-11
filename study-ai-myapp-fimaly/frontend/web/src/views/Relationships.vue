<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="家族关系管理"></Header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-1 animate-slide-in-left">
          <div class="bg-white p-6 rounded-xl shadow-lg">
            <div class="flex items-center mb-4">
              <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center mr-3">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                </svg>
              </div>
              <h2 class="text-lg font-semibold text-gray-900">添加关系</h2>
            </div>
            <form @submit.prevent="handleCreateRelationship" class="space-y-4">
              <div>
                <label for="familyId" class="block text-sm font-medium text-gray-700 mb-1">选择家族</label>
                <Select
                  id="familyId"
                  v-model="form.familyId"
                  :options="familyOptions"
                  placeholder="请选择家族"
                  required
                />
              </div>
              <div>
                <label for="member1Id" class="block text-sm font-medium text-gray-700 mb-1">成员1</label>
                <div class="flex space-x-2">
                  <div class="flex-1">
                    <Select
                      id="member1Id"
                      v-model="form.member1Id"
                      :options="member1Options"
                      placeholder="请选择成员"
                      required
                    />
                  </div>
                  <button 
                    type="button"
                    @click="searchMember(1)"
                    class="btn-primary btn-sm"
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
                  <div class="flex-1">
                    <Select
                      id="member2Id"
                      v-model="form.member2Id"
                      :options="member2Options"
                      placeholder="请选择成员"
                      required
                    />
                  </div>
                  <button 
                    type="button"
                    @click="searchMember(2)"
                    class="btn-primary btn-sm"
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
                <Select
                  id="relationshipType"
                  v-model="form.relationshipType"
                  :options="relationshipTypeOptions"
                  placeholder="请选择关系类型"
                  required
                />
              </div>
              <button 
                type="submit" 
                :disabled="relationshipStore.loading"
                class="btn-primary w-full"
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
                  class="input-field flex-1"
                />
                <button 
                  @click="handleSearch"
                  class="btn-primary btn-sm"
                >
                  搜索
                </button>
                <button 
                  @click="handleReset"
                  class="btn-secondary btn-sm"
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

    <!-- Delete Confirm Modal -->
    <ConfirmModal 
      :visible="showDeleteConfirm"
      title="确认删除"
      message="确定要删除这个关系吗？"
      type="delete"
      @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false"
    />

  </div>
</template>

<script>
import { useRelationshipStore } from '../stores/relationship'
import { useMemberStore } from '../stores/member'
import { useFamilyStore } from '../stores/family'
import { useRouter, useRoute } from 'vue-router'
import { ref, computed, onMounted, watch } from 'vue'
import Header from '../components/Header.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import Select from '../components/Select.vue'

export default {
  name: 'Relationships',
  components: {
    Header,
    Select,
    ConfirmModal
  },
  setup() {
    const relationshipStore = useRelationshipStore()
    const memberStore = useMemberStore()
    const familyStore = useFamilyStore()
    const router = useRouter()
    const route = useRoute()

    const showDeleteConfirm = ref(false)
    const deletingRelationshipId = ref(null)

    const showToastMsg = (message, type = 'info') => {
      window.showToastMessage(message, type)
    }

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

    const familyOptions = computed(() => {
      return families.value.map(family => ({
        value: family.id,
        label: family.name
      }))
    })

    const member1Options = computed(() => {
      return familyMembers.value.map(member => ({
        value: member.id,
        label: member.name
      }))
    })

    const member2Options = computed(() => {
      return familyMembers.value.map(member => ({
        value: member.id,
        label: member.name
      }))
    })

    const relationshipTypeOptions = [
      { value: 'FATHER', label: '父亲' },
      { value: 'MOTHER', label: '母亲' },
      { value: 'SON', label: '儿子' },
      { value: 'DAUGHTER', label: '女儿' },
      { value: 'HUSBAND', label: '丈夫' },
      { value: 'WIFE', label: '妻子' },
      { value: 'BROTHER', label: '兄弟' },
      { value: 'SISTER', label: '姐妹' },
      { value: 'GRANDFATHER', label: '祖父' },
      { value: 'GRANDMOTHER', label: '祖母' },
      { value: 'GRANDSON', label: '孙子' },
      { value: 'GRANDDAUGHTER', label: '孙女' },
      { value: 'UNCLE', label: '叔叔' },
      { value: 'AUNT', label: '阿姨' },
      { value: 'COUSIN', label: '堂表亲' }
    ]

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
        showToastMsg('关系添加成功', 'success')
        form.value = {
          member1Id: '',
          member2Id: '',
          relationshipType: ''
        }
        await loadRelationships()
      } catch (error) {
        showToastMsg('关系添加失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const handleDelete = (id) => {
      deletingRelationshipId.value = id
      showDeleteConfirm.value = true
    }

    const confirmDelete = async () => {
      try {
        await relationshipStore.deleteRelationship(deletingRelationshipId.value)
        showToastMsg('关系删除成功', 'success')
        await loadRelationships()
      } catch (error) {
        showToastMsg('关系删除失败: ' + (error.response?.data?.message || error.message), 'error')
      } finally {
        showDeleteConfirm.value = false
        deletingRelationshipId.value = null
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
      showDeleteConfirm,
      showMessageModal,
      messageModalTitle,
      messageModalContent,
      messageModalIcon,
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
      confirmDelete,
      handleSearch,
      handleReset,
      getRelationshipTypeText,
      getMemberName
    }
  }
}
</script>
