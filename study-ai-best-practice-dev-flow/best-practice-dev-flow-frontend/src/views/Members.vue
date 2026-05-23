<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { memberAPI } from '@/api'
import type { MemberRequest } from '@/api'
import Sidebar from '@/components/Sidebar.vue'
import { Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

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
    try {
      const response = await memberAPI.getByFamily(familyStore.currentFamily.id)
      members.value = response as unknown as any[]
    } catch (error) {
      console.error('加载成员列表失败', error)
    }
  }
}

async function searchMembers() {
  if (searchKeyword.value.trim()) {
    try {
      const response = await memberAPI.search(searchKeyword.value)
      members.value = response as unknown as any[]
    } catch (error) {
      console.error('搜索成员失败', error)
    }
  } else {
    await loadMembers()
  }
}

function openCreateModal() {
  if (!familyStore.currentFamily) {
    ElMessage.warning('请先选择一个家族')
    return
  }
  showCreateModal.value = true
  resetForm()
}

async function createMember() {
  if (!familyStore.currentFamily) {
    ElMessage.warning('请先选择一个家族')
    return
  }
  
  if (!form.value.name || !form.value.name.trim()) {
    ElMessage.warning('请输入成员姓名')
    return
  }
  
  form.value.familyId = familyStore.currentFamily.id
  
  try {
    await memberAPI.create(form.value)
    showCreateModal.value = false
    resetForm()
    await loadMembers()
    ElMessage.success('成员添加成功')
  } catch (error: any) {
    console.error('创建成员失败', error)
    ElMessage.error(error.message || '创建成员失败')
  }
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
    familyId: familyStore.currentFamily?.id || 0
  }
  editingId.value = null
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
  if (!editingId.value) return
  
  if (!form.value.name || !form.value.name.trim()) {
    ElMessage.warning('请输入成员姓名')
    return
  }
  
  try {
    await memberAPI.update(editingId.value, form.value)
    showUpdateModal.value = false
    resetForm()
    await loadMembers()
    ElMessage.success('成员信息更新成功')
  } catch (error: any) {
    console.error('更新成员失败', error)
    ElMessage.error(error.message || '更新成员失败')
  }
}

async function deleteMember(id: number) {
  try {
    await memberAPI.delete(id)
    await loadMembers()
    ElMessage.success('成员删除成功')
  } catch (error: any) {
    console.error('删除成员失败', error)
    ElMessage.error(error.message || '删除成员失败')
  }
}

watch(() => familyStore.currentFamily, async () => {
  await loadMembers()
})

onMounted(async () => {
  await familyStore.loadFamilies()
  await loadMembers()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>成员管理</h1>
        <el-button type="primary" @click="openCreateModal">
          <Plus />
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
              <Search />
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
                <Edit />
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="deleteMember(scope.row.id)"
              >
                <Delete />
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="添加成员" v-model="showCreateModal">
        <el-form :model="form" label-width="100px">
          <el-form-item label="姓名" required>
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" placeholder="请选择性别">
              <el-option v-for="opt in genderOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker v-model="form.birthDate" type="date" placeholder="请选择日期" />
          </el-form-item>
          <el-form-item label="去世日期">
            <el-date-picker v-model="form.deathDate" type="date" placeholder="请选择日期" />
          </el-form-item>
          <el-form-item label="职业">
            <el-input v-model="form.occupation" placeholder="请输入职业" />
          </el-form-item>
          <el-form-item label="学历">
            <el-input v-model="form.education" placeholder="请输入学历" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" type="email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" @click="createMember">保存</el-button>
        </template>
      </el-dialog>

      <el-dialog title="编辑成员" v-model="showUpdateModal">
        <el-form :model="form" label-width="100px">
          <el-form-item label="姓名" required>
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" placeholder="请选择性别">
              <el-option v-for="opt in genderOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker v-model="form.birthDate" type="date" placeholder="请选择日期" />
          </el-form-item>
          <el-form-item label="去世日期">
            <el-date-picker v-model="form.deathDate" type="date" placeholder="请选择日期" />
          </el-form-item>
          <el-form-item label="职业">
            <el-input v-model="form.occupation" placeholder="请输入职业" />
          </el-form-item>
          <el-form-item label="学历">
            <el-input v-model="form.education" placeholder="请输入学历" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" type="email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpdateModal = false">取消</el-button>
          <el-button type="primary" @click="updateMember">保存</el-button>
        </template>
