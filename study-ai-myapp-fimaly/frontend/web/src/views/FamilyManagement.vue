<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="家族管理">
      <template #actions>
        <button @click="openCreateFamilyModal" class="btn-primary">
          新建家族
        </button>
        <button v-if="selectedFamily" @click="openFamilyDetailModal" class="btn-primary">
          查看明细
        </button>
      </template>
    </Header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <!-- Family Selector -->
      <div class="bg-white p-6 rounded-lg shadow mb-6 animate-slide-up">
        <h2 class="text-lg font-medium text-gray-900 mb-4">选择家族</h2>
        <div v-if="familyStore.loading" class="flex justify-center py-8">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="familyStore.families.length === 0" class="text-center py-8">
          <p class="text-gray-600">暂无家族数据</p>
          <button @click="openCreateFamilyModal" class="mt-4 btn-primary">
            创建第一个家族
          </button>
        </div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div 
            v-for="(family, index) in familyStore.families" 
            :key="family.id" 
            @click="selectFamily(family)"
            :class="['border rounded-lg p-5 cursor-pointer transition-all duration-300 transform', selectedFamily?.id === family.id ? 'border-green-500 bg-green-50 shadow-lg scale-105' : 'border-gray-200 hover:shadow-lg hover:-translate-y-1 hover:border-green-300']"
            :style="{ animationDelay: `${index * 50}ms` }"
            class="animate-fade-in"
          >
            <div class="flex items-center">
              <div :class="['w-14 h-14 rounded-xl flex items-center justify-center mr-4 transition-all duration-300', selectedFamily?.id === family.id ? 'bg-gradient-to-br from-green-400 to-green-600' : 'bg-gradient-to-br from-green-100 to-green-200']">
                <span :class="['text-xl font-bold', selectedFamily?.id === family.id ? 'text-white' : 'text-green-600']">{{ family.name.charAt(0) }}</span>
              </div>
              <div class="flex-1">
                <h3 :class="['font-semibold text-lg', selectedFamily?.id === family.id ? 'text-green-700' : 'text-gray-900']">{{ family.name }}</h3>
                <p class="text-sm text-gray-600 mt-1">{{ family.description || '无描述' }}</p>
              </div>
              <div v-if="selectedFamily?.id === family.id" class="text-green-600 transform scale-110">
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
              <p class="text-sm text-gray-500 mt-1">管理员: {{ getAdministratorName(selectedFamily.administratorId) }}</p>
            </div>
            <div class="flex space-x-2">
              <button @click="openUpdateAdministratorModal" class="btn-primary">
                更改管理员
              </button>
              <button @click="openAddMemberModal" class="btn-primary">
                添加成员
              </button>
              <button @click="openAddRelationshipModal" class="btn-purple">
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
                    <button @click="editMember(member)" class="btn-text mr-3">编辑</button>
                    <button @click="deleteMember(member.id)" class="btn-text-danger">删除</button>
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
                    <button @click="deleteRelationship(relationship.id)" class="btn-text-danger">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <!-- Create Family Modal -->
    <div v-if="showCreateFamilyModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showCreateFamilyModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">新建家族</h3>
        </div>
        <form @submit.prevent="handleCreateFamily">
          <div class="space-y-4">
            <div>
              <label for="familyName" class="block text-sm font-medium text-gray-700 mb-1.5">家族名称</label>
              <input type="text" id="familyName" v-model="familyForm.name" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="familyDescription" class="block text-sm font-medium text-gray-700 mb-1.5">描述</label>
              <textarea id="familyDescription" v-model="familyForm.description" rows="3" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showCreateFamilyModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="btn-primary">
              {{ familyStore.loading ? '创建中...' : '创建' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Family Detail Modal -->
    <div v-if="showFamilyDetailModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showFamilyDetailModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-purple-100 to-purple-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">家族详情</h3>
        </div>
        <form @submit.prevent="handleUpdateFamily">
          <div class="space-y-4">
            <div>
              <label for="editFamilyName" class="block text-sm font-medium text-gray-700 mb-1.5">家族名称</label>
              <input type="text" id="editFamilyName" v-model="familyForm.name" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="editFamilyDescription" class="block text-sm font-medium text-gray-700 mb-1.5">描述</label>
              <textarea id="editFamilyDescription" v-model="familyForm.description" rows="3" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="handleDeleteFamily" class="btn-danger">
              删除家族
            </button>
            <button type="button" @click="showFamilyDetailModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="familyStore.loading" class="btn-primary">
              {{ familyStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Add Member Modal -->
    <div v-if="showAddMemberModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showAddMemberModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-100 to-blue-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">{{ editingMember ? '编辑成员' : '添加成员' }}</h3>
        </div>
        <form @submit.prevent="handleAddMember">
          <div class="space-y-4">
            <div>
              <label for="memberName" class="block text-sm font-medium text-gray-700 mb-1.5">姓名</label>
              <input type="text" id="memberName" v-model="memberForm.name" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="memberGender" class="block text-sm font-medium text-gray-700 mb-1.5">性别</label>
              <select id="memberGender" v-model="memberForm.gender" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
                <option value="">请选择</option>
                <option value="male">男</option>
                <option value="female">女</option>
              </select>
            </div>
            <div>
              <label for="memberBirthDate" class="block text-sm font-medium text-gray-700 mb-1.5">出生日期</label>
              <input type="date" id="memberBirthDate" v-model="memberForm.birthDate" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="memberDeathDate" class="block text-sm font-medium text-gray-700 mb-1.5">去世日期</label>
              <input type="date" id="memberDeathDate" v-model="memberForm.deathDate" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="memberDetails" class="block text-sm font-medium text-gray-700 mb-1.5">详细信息</label>
              <textarea id="memberDetails" v-model="memberForm.details" rows="3" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"></textarea>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showAddMemberModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="memberStore.loading" class="btn-primary">
              {{ memberStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Add Relationship Modal -->
    <div v-if="showAddRelationshipModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showAddRelationshipModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-amber-100 to-amber-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-amber-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">添加关联关系</h3>
        </div>
        <form @submit.prevent="handleAddRelationship">
          <div class="space-y-4">
            <div>
              <label for="member1" class="block text-sm font-medium text-gray-700 mb-1.5">成员1</label>
              <select id="member1" v-model="relationshipForm.member1Id" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
                <option value="">请选择成员</option>
                <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
              </select>
            </div>
            <div>
              <label for="relationshipType" class="block text-sm font-medium text-gray-700 mb-1.5">关系类型</label>
              <select id="relationshipType" v-model="relationshipForm.relationshipType" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
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
              <label for="member2" class="block text-sm font-medium text-gray-700 mb-1.5">成员2</label>
              <select id="member2" v-model="relationshipForm.member2Id" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
                <option value="">请选择成员</option>
                <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
              </select>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showAddRelationshipModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="relationshipStore.loading" class="btn-primary">
              {{ relationshipStore.loading ? '添加中...' : '添加' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Update Administrator Modal -->
    <div v-if="showUpdateAdministratorModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showUpdateAdministratorModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-cyan-100 to-cyan-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">更改管理员</h3>
        </div>
        <form @submit.prevent="handleUpdateAdministrator">
          <div class="space-y-4">
            <div>
              <label for="newAdministrator" class="block text-sm font-medium text-gray-700 mb-1.5">新管理员</label>
              <select id="newAdministrator" v-model="administratorForm.administratorId" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
                <option value="">请选择成员</option>
                <option v-for="member in familyMembers" :key="member.id" :value="member.id">{{ member.name }}</option>
              </select>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showUpdateAdministratorModal = false" class="btn-secondary">
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
      :title="deleteConfirmTitle"
      :message="deleteConfirmMessage"
      type="delete"
      @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false"
    />

    <!-- Message Modal -->
    <Modal 
      :visible="showMessageModal" 
      :title="messageModalTitle" 
      :icon="messageModalIcon"
      @close="showMessageModal = false"
    >
      <p class="text-gray-700">{{ messageModalContent }}</p>
      <template #footer>
        <button @click="showMessageModal = false" class="btn-primary w-full">
          确定
        </button>
      </template>
    </Modal>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import Header from '../components/Header.vue'
import Modal from '../components/Modal.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import { useFamilyStore } from '../stores/family'
import { useMemberStore } from '../stores/member'
import { useRelationshipStore } from '../stores/relationship'
import { useRouter } from 'vue-router'

export default {
  name: 'FamilyManagement',
  components: {
    Header,
    Modal,
    ConfirmModal
  },
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
    const showUpdateAdministratorModal = ref(false)
    const showDeleteConfirm = ref(false)
    const showMessageModal = ref(false)
    const editingMember = ref(null)
    const deletingType = ref('')
    const deletingId = ref(null)
    const deleteConfirmTitle = ref('确认删除')
    const deleteConfirmMessage = ref('确定要删除吗？')
    const messageModalTitle = ref('')
    const messageModalContent = ref('')
    const messageModalIcon = ref('info')

    const showMessage = (title, content, icon = 'info') => {
      messageModalTitle.value = title
      messageModalContent.value = content
      messageModalIcon.value = icon
      showMessageModal.value = true
    }

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
      relationshipType: '',
      member2Id: ''
    })

    const administratorForm = ref({
      administratorId: ''
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

    const openUpdateAdministratorModal = () => {
      if (selectedFamily.value) {
        administratorForm.value = {
          administratorId: selectedFamily.value.administratorId
        }
        showUpdateAdministratorModal.value = true
      }
    }

    const handleUpdateAdministrator = async () => {
      if (!selectedFamily.value || !administratorForm.value.administratorId) return

      try {
        await familyStore.updateAdministrator(selectedFamily.value.id, administratorForm.value.administratorId)
        showUpdateAdministratorModal.value = false
        ElMessage.success('管理员更新成功')
      } catch (error) {
        ElMessage.error('管理员更新失败: ' + (error.message || '未知错误'))
      }
    }

    const getAdministratorName = (administratorId) => {
      if (!administratorId) return '无'
      const member = memberStore.members.find(m => m.id === administratorId)
      return member ? member.name : '未知'
    }

    const handleCreateFamily = async () => {
      try {
        await familyStore.createFamily(familyForm.value)
        showCreateFamilyModal.value = false
        familyForm.value = { name: '', description: '' }
        showMessage('操作成功', '家族创建成功', 'success')
      } catch (error) {
        showMessage('操作失败', '创建家族失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const handleUpdateFamily = async () => {
      try {
        await familyStore.updateFamily(selectedFamily.value.id, familyForm.value)
        showFamilyDetailModal.value = false
        selectedFamily.value = { ...selectedFamily.value, ...familyForm.value }
        showMessage('操作成功', '家族更新成功', 'success')
      } catch (error) {
        showMessage('操作失败', '更新家族失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const handleDeleteFamily = () => {
      deletingType.value = 'family'
      deletingId.value = selectedFamily.value.id
      deleteConfirmTitle.value = '确认删除家族'
      deleteConfirmMessage.value = '确定要删除这个家族吗？此操作不可恢复。'
      showDeleteConfirm.value = true
    }

    const handleAddMember = async () => {
      try {
        if (editingMember.value) {
          await memberStore.updateMember(editingMember.value.id, memberForm.value)
          showMessage('操作成功', '成员更新成功', 'success')
        } else {
          await memberStore.createMember({
            ...memberForm.value,
            familyId: selectedFamily.value.id
          })
          showMessage('操作成功', '成员添加成功', 'success')
        }
        showAddMemberModal.value = false
        editingMember.value = null
        memberForm.value = { name: '', gender: '', birthDate: '', deathDate: '', details: '' }
      } catch (error) {
        showMessage('操作失败', '保存成员失败: ' + (error.response?.data?.message || error.message), 'error')
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

    const deleteMember = (memberId) => {
      deletingType.value = 'member'
      deletingId.value = memberId
      deleteConfirmTitle.value = '确认删除成员'
      deleteConfirmMessage.value = '确定要删除这个成员吗？'
      showDeleteConfirm.value = true
    }

    const handleAddRelationship = async () => {
      try {
        await relationshipStore.createRelationship({
          ...relationshipForm.value,
          familyId: selectedFamily.value.id
        })
        showAddRelationshipModal.value = false
        relationshipForm.value = { member1Id: '', member2Id: '', relationshipType: '' }
        showMessage('操作成功', '关系添加成功', 'success')
      } catch (error) {
        showMessage('操作失败', '添加关系失败: ' + (error.response?.data?.message || error.message), 'error')
      }
    }

    const deleteRelationship = (relationshipId) => {
      deletingType.value = 'relationship'
      deletingId.value = relationshipId
      deleteConfirmTitle.value = '确认删除关系'
      deleteConfirmMessage.value = '确定要删除这个关系吗？'
      showDeleteConfirm.value = true
    }

    const confirmDelete = async () => {
      try {
        if (deletingType.value === 'family') {
          await familyStore.deleteFamily(deletingId.value)
          showFamilyDetailModal.value = false
          selectedFamily.value = null
          showMessage('操作成功', '家族删除成功', 'success')
        } else if (deletingType.value === 'member') {
          await memberStore.deleteMember(deletingId.value)
          showMessage('操作成功', '成员删除成功', 'success')
        } else if (deletingType.value === 'relationship') {
          await relationshipStore.deleteRelationship(deletingId.value)
          showMessage('操作成功', '关系删除成功', 'success')
        }
      } catch (error) {
        showMessage('操作失败', '删除失败: ' + (error.response?.data?.message || error.message), 'error')
      } finally {
        showDeleteConfirm.value = false
        deletingType.value = ''
        deletingId.value = null
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
      showUpdateAdministratorModal,
      showDeleteConfirm,
      showMessageModal,
      deleteConfirmTitle,
      deleteConfirmMessage,
      messageModalTitle,
      messageModalContent,
      messageModalIcon,
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
      openUpdateAdministratorModal,
      handleUpdateAdministrator,
      getAdministratorName,
      handleCreateFamily,
      handleUpdateFamily,
      handleDeleteFamily,
      handleAddMember,
      editMember,
      deleteMember,
      handleAddRelationship,
      deleteRelationship,
      confirmDelete,
      getMemberName,
      administratorForm
    }
  }
}
</script>