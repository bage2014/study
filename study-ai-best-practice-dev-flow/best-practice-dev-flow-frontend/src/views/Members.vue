<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { memberAPI } from '@/api'
import type { MemberRequest } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()
const members = ref<any[]>([])

const showCreateModal = ref(false)
const showUpdateModal = ref(false)

const form = ref<MemberRequest>({
  name: '',
  gender: '',
  birthDate: '',
  deathDate: '',
  occupation: '',
  education: '',
  phone: '',
  email: '',
  familyId: 0
})

const editingId = ref<number | null>(null)
const searchKeyword = ref('')

const genderOptions = [
  { value: 'MALE', label: '男' },
  { value: 'FEMALE', label: '女' }
]

async function loadMembers() {
  if (familyStore.currentFamily) {
    members.value = await memberAPI.getByFamily(familyStore.currentFamily.id) as unknown as any[]
  }
}

async function searchMembers() {
  if (searchKeyword.value.trim()) {
    members.value = await memberAPI.search(searchKeyword.value) as unknown as any[]
  } else {
    await loadMembers()
  }
}

async function createMember() {
  form.value.familyId = familyStore.currentFamily?.id || 0
  await memberAPI.create(form.value)
  showCreateModal.value = false
  resetForm()
  await loadMembers()
}

function resetForm() {
  form.value = {
    name: '',
    gender: '',
    birthDate: '',
    deathDate: '',
    occupation: '',
    education: '',
    phone: '',
    email: '',
    familyId: 0
  }
}

async function openUpdateModal(member: any) {
  editingId.value = member.id
  form.value = {
    name: member.name,
    gender: member.gender,
    birthDate: member.birthDate || '',
    deathDate: member.deathDate || '',
    occupation: member.occupation || '',
    education: member.education || '',
    phone: member.phone || '',
    email: member.email || '',
    familyId: member.familyId
  }
  showUpdateModal.value = true
}

async function updateMember() {
  if (editingId.value) {
    await memberAPI.update(editingId.value, form.value)
    showUpdateModal.value = false
    editingId.value = null
    resetForm()
    await loadMembers()
  }
}

async function deleteMember(id: number) {
  await memberAPI.delete(id)
  await loadMembers()
}

watch(() => familyStore.currentFamily, async () => {
  await loadMembers()
})

onMounted(async () => {
  await loadMembers()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>成员管理</h1>
        <el-button type="primary" @click="showCreateModal = true">
          <el-icon>Plus</el-icon>
          添加成员
        </el-button>
      </div>

      <div class="search-bar">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索成员姓名"
          @keyup.enter="searchMembers"
        >
          <template #append>
            <el-button @click="searchMembers">
              <el-icon>Search</el-icon>
            </el-button>
          </template>
        </el-input>
      </div>

      <div class="card">
        <el-table :data="members" border>
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="gender" label="性别">
            <template #default="scope">
              {{ scope.row.gender === 'MALE' ? '男' : '女' }}
            </template>
          </el-table-column>
          <el-table-column prop="birthDate" label="出生日期" />
          <el-table-column prop="occupation" label="职业" />
          <el-table-column prop="education" label="学历" />
          <el-table-column prop="phone" label="电话" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button 
                size="small" 
                @click="openUpdateModal(scope.row)"
              >
                <el-icon>Edit</el-icon>
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="deleteMember(scope.row.id)"
              >
                <el-icon>Delete</el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="添加成员" :visible="showCreateModal" @close="showCreateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="姓名" required>
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender">
              <el-option v-for="opt in genderOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker v-model="form.birthDate" type="date" />
          </el-form-item>
          <el-form-item label="去世日期">
            <el-date-picker v-model="form.deathDate" type="date" />
          </el-form-item>
          <el-form-item label="职业">
            <el-input v-model="form.occupation" />
          </el-form-item>
          <el-form-item label="学历">
            <el-input v-model="form.education" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" type="email" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" @click="createMember">保存</el-button>
        </template>
      </el-dialog>

      <el-dialog title="编辑成员" :visible="showUpdateModal" @close="showUpdateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="姓名" required>
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender">
              <el-option v-for="opt in genderOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker v-model="form.birthDate" type="date" />
          </el-form-item>
          <el-form-item label="去世日期">
            <el-date-picker v-model="form.deathDate" type="date" />
          </el-form-item>
          <el-form-item label="职业">
            <el-input v-model="form.occupation" />
          </el-form-item>
          <el-form-item label="学历">
            <el-input v-model="form.education" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" type="email" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpdateModal = false">取消</el-button>
          <el-button type="primary" @click="updateMember">保存</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<style scoped>
.page-layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f5f5;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  margin: 0;
}

.search-bar {
  margin-bottom: 20px;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}
</style>