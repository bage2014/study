<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useFamilyStore } from '@/stores/family'
import type { FamilyRequest } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import Sidebar from '@/components/Sidebar.vue'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'

const familyStore = useFamilyStore()
const families = ref<any[]>([])

const showCreateModal = ref(false)
const showUpdateModal = ref(false)

const form = ref<FamilyRequest>({
  name: '',
  description: ''
})

const editingId = ref<number | null>(null)
const loading = ref(false)

async function loadFamilies() {
  try {
    loading.value = true
    await familyStore.loadFamilies()
    families.value = familyStore.families
  } catch (error) {
    ElMessage.error('加载家族列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function createFamily() {
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入家族名称')
    return
  }
  try {
    loading.value = true
    await familyStore.createFamily(form.value)
    ElMessage.success('家族创建成功')
    showCreateModal.value = false
    form.value = { name: '', description: '' }
    await loadFamilies()
  } catch (error) {
    ElMessage.error('创建家族失败')
    console.error(error)
  } finally {
    loading.value = false
  }
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
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入家族名称')
    return
  }
  if (!editingId.value) return
  try {
    loading.value = true
    await familyStore.updateFamily(editingId.value, form.value)
    ElMessage.success('家族更新成功')
    showUpdateModal.value = false
    editingId.value = null
    form.value = { name: '', description: '' }
    await loadFamilies()
  } catch (error) {
    ElMessage.error('更新家族失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function deleteFamily(id: number) {
  try {
    await ElMessageBox.confirm('确定要删除这个家族吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    loading.value = true
    await familyStore.deleteFamily(id)
    ElMessage.success('家族删除成功')
    await loadFamilies()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除家族失败')
      console.error(error)
    }
  } finally {
    loading.value = false
  }
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
        <el-button type="primary" :loading="loading" @click="showCreateModal = true">
          <Plus />
          创建家族
        </el-button>
      </div>

      <div class="card">
        <el-table :data="families" border v-loading="loading">
          <el-table-column prop="name" label="家族名称" />
          <el-table-column prop="description" label="描述" />
          <el-table-column prop="memberCount" label="成员数量" />
          <el-table-column prop="creatorName" label="创建者" />
          <el-table-column prop="createdAt" label="创建时间" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button 
                size="small" 
                @click="openUpdateModal(scope.row)"
              >
                <Edit />
                编辑
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="deleteFamily(scope.row.id)"
              >
                <Delete />
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="创建家族" v-model="showCreateModal" width="500px">
        <el-form :model="form" label-width="80px">
          <el-form-item label="家族名称" required>
            <el-input v-model="form.name" placeholder="请输入家族名称" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input 
              v-model="form.description" 
              type="textarea" 
              :rows="4" 
              placeholder="请输入家族描述" 
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" :loading="loading" @click="createFamily">创建</el-button>
        </template>
      </el-dialog>

      <el-dialog title="编辑家族" v-model="showUpdateModal" width="500px">
        <el-form :model="form" label-width="80px">
          <el-form-item label="家族名称" required>
            <el-input v-model="form.name" placeholder="请输入家族名称" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input 
              v-model="form.description" 
              type="textarea" 
              :rows="4" 
              placeholder="请输入家族描述" 
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpdateModal = false">取消</el-button>
          <el-button type="primary" :loading="loading" @click="updateFamily">保存</el-button>
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