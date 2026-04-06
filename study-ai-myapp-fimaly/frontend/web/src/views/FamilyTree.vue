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
            <h1 class="text-xl font-bold text-gray-900">家族树</h1>
          </div>
          <div class="flex items-center space-x-3">
            <button @click="openCreateFamilyModal" class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
              新建家族
            </button>
            <button v-if="selectedFamily" @click="openFamilyDetailModal" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 shadow-md hover:shadow-lg transition-all duration-200">
              查看明细
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Family Selector -->
      <div class="bg-white p-6 rounded-lg shadow mb-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">选择家族</h2>
        <div v-if="familyStore.loading" class="flex justify-center py-8">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="familyStore.families.length === 0" class="text-center py-8">
          <p class="text-gray-600">暂无家族数据</p>
          <button @click="openCreateFamilyModal" class="mt-4 px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
            创建第一个家族
          </button>
        </div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div 
            v-for="family in familyStore.families" 
            :key="family.id" 
            @click="selectFamily(family)"
            :class="['border rounded-md p-4 cursor-pointer hover:shadow-md transition-all', selectedFamily?.id === family.id ? 'border-primary bg-blue-50' : 'border-gray-200']"
          >
            <div class="flex items-center">
              <div class="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center mr-3">
                <span class="text-lg font-bold text-gray-600">{{ family.name.charAt(0) }}</span>
              </div>
              <div class="flex-1">
                <h3 class="font-medium text-gray-900">{{ family.name }}</h3>
                <p class="text-sm text-gray-600">{{ family.description || '无描述' }}</p>
              </div>
              <div v-if="selectedFamily?.id === family.id" class="text-primary">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Selected Family Content -->
      <div v-if="selectedFamily" class="space-y-6">
        <!-- Family Info -->
        <div class="bg-white p-6 rounded-lg shadow">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="text-2xl font-bold text-gray-900">{{ selectedFamily.name }}</h2>
              <p class="text-gray-600 mt-1">{{ selectedFamily.description || '无描述' }}</p>
              <p class="text-sm text-gray-500 mt-2">创建于: {{ formatDate(selectedFamily.createdAt) }}</p>
            </div>
            <div class="flex space-x-2">
              <button @click="openAddMemberModal" class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
                添加成员
              </button>
              <button @click="openAddRelationshipModal" class="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 shadow-md hover:shadow-lg transition-all duration-200">
                添加关系
              </button>
            </div>
          </div>
        </div>

        <!-- Members List -->
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-medium text-gray-900 mb-4">家族成员</h3>
          <div v-if="memberStore.loading" class="flex justify-center py-8">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
          </div>
          <div v-else-if="familyMembers.length === 0" class="text-center py-8">
            <p class="text-gray-600">暂无成员数据</p>
          </div>
          <div v-else class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">姓名</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">性别</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">出生日期</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="member in familyMembers" :key="member.id">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="w-8 h-8 rounded-full bg-gray-200 flex items-center justify-center mr-2">
                        <span class="text-sm font-bold text-gray-600">{{ member.name.charAt(0) }}</span>
                      </div>
                      <span class="text-sm font-medium text-gray-900">{{ member.name }}</span>
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
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button @click="editMember(member)" class="text-primary hover:text-blue-700 mr-3">编辑</button>
                    <button @click="deleteMember(member.id)" class="text-red-600 hover:text-red-800">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Relationships List -->
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-medium text-gray-900 mb-4">关联关系</h3>
          <div v-if="relationshipStore.loading" class="flex justify-center py-8">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
          </div>
          <div v-else-if="familyRelationships.length === 0" class="text-center py-8">
            <p class="text-gray-600">暂无关联关系</p>
          </div>
          <div v-else class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">成员1</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">关系</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">成员2</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="relationship in familyRelationships" :key="relationship.id">
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {{ getMemberName(relationship.member1Id) }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-purple-100 text-purple-800">
                      {{ relationship.relationshipType }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {{ getMemberName(relationship.member2Id) }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button @click="deleteRelationship(relationship.id)" class="text-red-600 hover:text-red-800">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <!-- Create Family Modal -->
    <div v-if="showCreateFamilyModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">新建家族</h3>
        <form @submit.prevent="handleCreateFamily">
          <div class="space-y-4">
            <div>
              <label for="familyName" class="block text-sm font-medium text-gray-700">家族名称</label>
              <input type="text" id="familyName" v-model="familyForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="familyDescription" class="block text-sm font-medium text-gray-700">描述</label>
              <textarea id="familyDescription" v-model="familyForm.description" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showCreateFamilyModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-md text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 hover:shadow-lg disabled:opacity-50 transition-all duration-200">
              {{ familyStore.loading ? '创建中...' : '创建' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Family Detail Modal -->
    <div v-if="showFamilyDetailModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">家族详情</h3>
        <form @submit.prevent="handleUpdateFamily">
          <div class="space-y-4">
            <div>
              <label for="editFamilyName" class="block text-sm font-medium text-gray-700">家族名称</label>
              <input type="text" id="editFamilyName" v-model="familyForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="editFamilyDescription" class="block text-sm font-medium text-gray-700">描述</label>
              <textarea id="editFamilyDescription" v-model="familyForm.description" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="handleDeleteFamily" class="bg-red-600 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white hover:bg-red-700">
              删除家族
            </button>
            <button type="button" @click="showFamilyDetailModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-md text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 hover:shadow-lg disabled:opacity-50 transition-all duration-200">
              {{ familyStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Add Member Modal -->
    <div v-if="showAddMemberModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">{{ editingMember ? '编辑成员' : '添加成员' }}</h3>
        <form @submit.prevent="handleAddMember">
          <div class="space-y-4">
            <div>
              <label for="memberName" class="block text-sm font-medium text-gray-700">姓名</label>
              <input type="text" id="memberName" v-model="memberForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="memberGender" class="block text-sm font-medium text-gray-700">性别</label>
              <select id="memberGender" v-model="memberForm.gender" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                <option value="">请选择</option>
                <option value="male">男</option>
                <option value="female">女</option>
              </select>
            </div>
            <div>
              <label for="memberBirthDate" class="block text-sm font-medium text-gray-700">出生日期</label>
              <input type="date" id="memberBirthDate" v-model="memberForm.birthDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="memberDeathDate" class="block text-sm font-medium text-gray-700">去世日期</label>
              <input type="date" id="memberDeathDate" v-model="memberForm.deathDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
            </div>
            <div>
              <label for="memberDetails" class="block text-sm font-medium text-gray-700">详细信息</label>
              <textarea id="memberDetails" v-model="memberForm.details" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showAddMemberModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" :disabled="memberStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-md text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 hover:shadow-lg disabled:opacity-50 transition-all duration-200">
              {{ memberStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Add Relationship Modal -->
    <div v-if="showAddRelationshipModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">添加关联关系</h3>
        <form @submit.prevent="handleAddRelationship">
          <div class="space-y-4">
            <div>
              <label for="member1" class="block text-sm font-medium text-gray-700">成员1</label>
              <select id="member1" v-model="relationshipForm.member1Id" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
                <option value="">请选择成员</option>
                <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
              </select>
            </div>
            <div>
              <label for="relationshipType" class="block text-sm font-medium text-gray-700">关系类型</label>
              <select id="relationshipType" v-model="relationshipForm.relationshipType" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
                <option value="">请选择关系类型</option>
                <option value="配偶">配偶</option>
                <option value="父子">父子</option>
                <option value="母子">母子</option>
                <option value="父女">父女</option>
                <option value="母女">母女</option>
                <option value="兄弟">兄弟</option>
                <option value="姐妹">姐妹</option>
                <option value="兄妹">兄妹</option>
                <option value="姐弟">姐弟</option>
              </select>
            </div>
            <div>
              <label for="member2" class="block text-sm font-medium text-gray-700">成员2</label>
              <select id="member2" v-model="relationshipForm.member2Id" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
                <option value="">请选择成员</option>
                <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
              </select>
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showAddRelationshipModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" :disabled="relationshipStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-md text-sm font-medium rounded-md text-white bg-green-500 hover:bg-green-600 hover:shadow-lg disabled:opacity-50 transition-all duration-200">
              {{ relationshipStore.loading ? '添加中...' : '添加' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useMemberStore } from '../stores/member'
import { useRelationshipStore } from '../stores/relationship'
import { useRouter } from 'vue-router'

export default {
  name: 'FamilyTree',
  setup() {
    const router = useRouter()
    const familyStore = useFamilyStore()
    const memberStore = useMemberStore()
    const relationshipStore = useRelationshipStore()

    const selectedFamily = ref(null)
    const showCreateFamilyModal = ref(false)
    const showFamilyDetailModal = ref(false)
    const showAddMemberModal = ref(false)
    const showAddRelationshipModal = ref(false)
    const editingMember = ref(null)

    const familyForm = ref({
      name: '',
      description: ''
    })

    const memberForm = ref({
      name: '',
      gender: '',
      birthDate: '',
      deathDate: '',
      details: ''
    })

    const relationshipForm = ref({
      member1Id: '',
      member2Id: '',
      relationshipType: ''
    })

    const familyMembers = computed(() => {
      if (!selectedFamily.value) return []
      return memberStore.members.filter(member => member.familyId === selectedFamily.value.id)
    })

    const familyRelationships = computed(() => {
      if (!selectedFamily.value) return []
      return relationshipStore.relationships.filter(rel => {
        const member1 = familyMembers.value.find(m => m.id === rel.member1Id)
        const member2 = familyMembers.value.find(m => m.id === rel.member2Id)
        return member1 && member2
      })
    })

    const navigateTo = (path) => {
      router.push(path)
    }

    const formatDate = (date) => {
      if (!date) return '未知'
      return new Date(date).toLocaleDateString('zh-CN')
    }

    const selectFamily = (family) => {
      selectedFamily.value = family
      memberStore.fetchMembers(family.id)
      relationshipStore.fetchRelationships()
    }

    const openCreateFamilyModal = () => {
      familyForm.value = { name: '', description: '' }
      showCreateFamilyModal.value = true
    }

    const openFamilyDetailModal = () => {
      if (!selectedFamily.value) return
      familyForm.value = {
        name: selectedFamily.value.name,
        description: selectedFamily.value.description || ''
      }
      showFamilyDetailModal.value = true
    }

    const openAddMemberModal = () => {
      editingMember.value = null
      memberForm.value = { name: '', gender: '', birthDate: '', deathDate: '', details: '' }
      showAddMemberModal.value = true
    }

    const openAddRelationshipModal = () => {
      relationshipForm.value = { member1Id: '', member2Id: '', relationshipType: '' }
      showAddRelationshipModal.value = true
    }

    const handleCreateFamily = async () => {
      try {
        await familyStore.createFamily(familyForm.value)
        showCreateFamilyModal.value = false
        familyForm.value = { name: '', description: '' }
      } catch (error) {
        alert('创建家族失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const handleUpdateFamily = async () => {
      try {
        await familyStore.updateFamily(selectedFamily.value.id, familyForm.value)
        showFamilyDetailModal.value = false
        selectedFamily.value = { ...selectedFamily.value, ...familyForm.value }
      } catch (error) {
        alert('更新家族失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const handleDeleteFamily = async () => {
      if (!confirm('确定要删除这个家族吗？此操作不可恢复。')) return
      try {
        await familyStore.deleteFamily(selectedFamily.value.id)
        showFamilyDetailModal.value = false
        selectedFamily.value = null
      } catch (error) {
        alert('删除家族失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const handleAddMember = async () => {
      try {
        if (editingMember.value) {
          await memberStore.updateMember(editingMember.value.id, memberForm.value)
        } else {
          await memberStore.createMember({
            ...memberForm.value,
            familyId: selectedFamily.value.id
          })
        }
        showAddMemberModal.value = false
        editingMember.value = null
        memberForm.value = { name: '', gender: '', birthDate: '', deathDate: '', details: '' }
      } catch (error) {
        alert('保存成员失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const editMember = (member) => {
      editingMember.value = member
      memberForm.value = {
        name: member.name,
        gender: member.gender,
        birthDate: member.birthDate || '',
        deathDate: member.deathDate || '',
        details: member.details || ''
      }
      showAddMemberModal.value = true
    }

    const deleteMember = async (memberId) => {
      if (!confirm('确定要删除这个成员吗？')) return
      try {
        await memberStore.deleteMember(memberId)
      } catch (error) {
        alert('删除成员失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const handleAddRelationship = async () => {
      try {
        await relationshipStore.createRelationship({
          ...relationshipForm.value,
          familyId: selectedFamily.value.id
        })
        showAddRelationshipModal.value = false
        relationshipForm.value = { member1Id: '', member2Id: '', relationshipType: '' }
      } catch (error) {
        alert('添加关系失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const deleteRelationship = async (relationshipId) => {
      if (!confirm('确定要删除这个关系吗？')) return
      try {
        await relationshipStore.deleteRelationship(relationshipId)
      } catch (error) {
        alert('删除关系失败: ' + (error.response?.data?.message || error.message))
      }
    }

    const getMemberName = (memberId) => {
      const member = familyMembers.value.find(m => m.id === memberId)
      return member ? member.name : '未知'
    }

    onMounted(() => {
      familyStore.fetchFamilies()
    })

    return {
      familyStore,
      memberStore,
      relationshipStore,
      selectedFamily,
      showCreateFamilyModal,
      showFamilyDetailModal,
      showAddMemberModal,
      showAddRelationshipModal,
      editingMember,
      familyForm,
      memberForm,
      relationshipForm,
      familyMembers,
      familyRelationships,
      navigateTo,
      formatDate,
      selectFamily,
      openCreateFamilyModal,
      openFamilyDetailModal,
      openAddMemberModal,
      openAddRelationshipModal,
      handleCreateFamily,
      handleUpdateFamily,
      handleDeleteFamily,
      handleAddMember,
      editMember,
      deleteMember,
      handleAddRelationship,
      deleteRelationship,
      getMemberName
    }
  }
}
</script>