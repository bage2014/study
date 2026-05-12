<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useFamilyStore } from '@/stores/family'
import type { FamilyRequest } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()
const families = ref<any[]>([])

const showCreateModal = ref(false)
const showUpdateModal = ref(false)

const form = ref<FamilyRequest>({
  name: '',
  description: ''
})

const editingId = ref<number | null>(null)

async function loadFamilies() {
  await familyStore.loadFamilies()
  families.value = familyStore.families
}

async function createFamily() {
  await familyStore.createFamily(form.value)
  showCreateModal.value = false
  form.value = { name: '', description: '' }
  await loadFamilies()
}

async function openUpdateModal(family: any) {
  editingId.value = family.id
  form.value = {
    name: family.name,
    description: family.description
  }
  showUpdateModal.value = true
}

async function updateFamily() {
  if (editingId.value) {
    await familyStore.updateFamily(editingId.value, form.value)
    showUpdateModal.value = false
    editingId.value = null
    form.value = { name: '', description: '' }
    await loadFamilies()
  }
}

async function deleteFamily(id: number) {
  await familyStore.deleteFamily(id)
  await loadFamilies()
}

onMounted(async () => {
  await loadFamilies()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>家族管理</h1>
        <el-button type="primary" @click="showCreateModal = true">
          <el-icon>Plus</el-icon>
          创建家族
        </el-button>
      </div>

      <div class="card">
        <el-table :data="families" border>
          <el-table-column prop="name" label="家族名称" />
          <el-table-column prop="description" label="描述" />
          <el-table-column prop="memberCount" label="成员数量" />
          <el-table-column prop="creatorName" label="创建者" />
          <el-table-column prop="createdAt" label="创建时间" />
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
                @click="deleteFamily(scope.row.id)"
              >
                <el-icon>Delete</el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="创建家族" :visible="showCreateModal" @close="showCreateModal = false">
        <el-form :model="form" label-width="80px">
          <el-form-item label="家族名称" required>
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="描述">
            <el-textarea v-model="form.description" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" @click="createFamily">创建</el-button>
        </template>
      </el-dialog>

      <el-dialog title="编辑家族" :visible="showUpdateModal" @close="showUpdateModal = false">
        <el-form :model="form" label-width="80px">
          <el-form-item label="家族名称" required>
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="描述">
            <el-textarea v-model="form.description" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpdateModal = false">取消</el-button>
          <el-button type="primary" @click="updateFamily">保存</el-button>
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

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}
</style>