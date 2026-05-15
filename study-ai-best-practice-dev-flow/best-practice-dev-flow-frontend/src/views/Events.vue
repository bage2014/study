<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { eventAPI } from '@/api'
import type { HistoricalEventRequest } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()
const events = ref<any[]>([])

const showCreateModal = ref(false)
const showUpdateModal = ref(false)

const form = ref<HistoricalEventRequest>({
  title: '',
  description: '',
  eventDate: '',
  location: '',
  familyId: 0
})

const editingId = ref<number | null>(null)

async function loadEvents() {
  if (familyStore.currentFamily) {
    events.value = await eventAPI.getByFamily(familyStore.currentFamily.id) as unknown as any[]
  }
}

async function createEvent() {
  form.value.familyId = familyStore.currentFamily?.id || 0
  await eventAPI.create(form.value)
  showCreateModal.value = false
  resetForm()
  await loadEvents()
}

function resetForm() {
  form.value = {
    title: '',
    description: '',
    eventDate: '',
    location: '',
    familyId: 0
  }
}

async function openUpdateModal(event: any) {
  editingId.value = event.id
  form.value = {
    title: event.title,
    description: event.description || '',
    eventDate: event.eventDate || '',
    location: event.location || '',
    familyId: event.familyId
  }
  showUpdateModal.value = true
}

async function updateEvent() {
  if (editingId.value) {
    await eventAPI.update(editingId.value, form.value)
    showUpdateModal.value = false
    editingId.value = null
    resetForm()
    await loadEvents()
  }
}

async function deleteEvent(id: number) {
  await eventAPI.delete(id)
  await loadEvents()
}

watch(() => familyStore.currentFamily, async () => {
  await loadEvents()
})

onMounted(async () => {
  await loadEvents()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>历史事件</h1>
        <el-button type="primary" @click="showCreateModal = true">
          <el-icon>Plus</el-icon>
          添加事件
        </el-button>
      </div>

      <div class="card">
        <el-table :data="events" border>
          <el-table-column prop="title" label="事件标题" />
          <el-table-column prop="description" label="描述" />
          <el-table-column prop="eventDate" label="事件日期" />
          <el-table-column prop="location" label="地点" />
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
                @click="deleteEvent(scope.row.id)"
              >
                <el-icon>Delete</el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="添加事件" :visible="showCreateModal" @close="showCreateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="事件标题" required>
            <el-input v-model="form.title" />
          </el-form-item>
          <el-form-item label="描述">
            <el-textarea v-model="form.description" />
          </el-form-item>
          <el-form-item label="事件日期">
            <el-date-picker v-model="form.eventDate" type="date" />
          </el-form-item>
          <el-form-item label="地点">
            <el-input v-model="form.location" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" @click="createEvent">保存</el-button>
        </template>
      </el-dialog>

      <el-dialog title="编辑事件" :visible="showUpdateModal" @close="showUpdateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="事件标题" required>
            <el-input v-model="form.title" />
          </el-form-item>
          <el-form-item label="描述">
            <el-textarea v-model="form.description" />
          </el-form-item>
          <el-form-item label="事件日期">
            <el-date-picker v-model="form.eventDate" type="date" />
          </el-form-item>
          <el-form-item label="地点">
            <el-input v-model="form.location" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpdateModal = false">取消</el-button>
          <el-button type="primary" @click="updateEvent">保存</el-button>
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