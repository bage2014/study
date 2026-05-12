<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Sidebar from '@/components/Sidebar.vue'

interface TrackPoint {
  id: number
  latitude: number
  longitude: number
  name: string
  description: string
  createdAt: string
}

const trackPoints = ref<TrackPoint[]>([])
const showCreateModal = ref(false)
const showUpdateModal = ref(false)

const form = ref({
  name: '',
  latitude: '',
  longitude: '',
  description: ''
})

const editingId = ref<number | null>(null)

async function loadTrackPoints() {
  try {
    const response = await fetch('/api/trackpoints', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    const data = await response.json()
    if (data.code === 200) {
      trackPoints.value = data.data
    }
  } catch (e) {
    console.error('Load track points error:', e)
  }
}

async function createTrackPoint() {
  try {
    const response = await fetch('/api/trackpoints', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: form.value.name,
        latitude: parseFloat(form.value.latitude),
        longitude: parseFloat(form.value.longitude),
        description: form.value.description
      })
    })
    const data = await response.json()
    if (data.code === 200) {
      showCreateModal.value = false
      resetForm()
      await loadTrackPoints()
    }
  } catch (e) {
    console.error('Create track point error:', e)
  }
}

function resetForm() {
  form.value = {
    name: '',
    latitude: '',
    longitude: '',
    description: ''
  }
}

async function openUpdateModal(point: TrackPoint) {
  editingId.value = point.id
  form.value = {
    name: point.name,
    latitude: String(point.latitude),
    longitude: String(point.longitude),
    description: point.description
  }
  showUpdateModal.value = true
}

async function updateTrackPoint() {
  if (!editingId.value) return
  
  try {
    const response = await fetch(`/api/trackpoints/${editingId.value}`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: form.value.name,
        latitude: parseFloat(form.value.latitude),
        longitude: parseFloat(form.value.longitude),
        description: form.value.description
      })
    })
    const data = await response.json()
    if (data.code === 200) {
      showUpdateModal.value = false
      editingId.value = null
      resetForm()
      await loadTrackPoints()
    }
  } catch (e) {
    console.error('Update track point error:', e)
  }
}

async function deleteTrackPoint(id: number) {
  try {
    await fetch(`/api/trackpoints/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    await loadTrackPoints()
  } catch (e) {
    console.error('Delete track point error:', e)
  }
}

onMounted(async () => {
  await loadTrackPoints()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>轨迹点管理</h1>
        <el-button type="primary" @click="showCreateModal = true">
          <el-icon>Plus</el-icon>
          添加轨迹点
        </el-button>
      </div>

      <div class="card">
        <el-table :data="trackPoints" border>
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="latitude" label="纬度" />
          <el-table-column prop="longitude" label="经度" />
          <el-table-column prop="description" label="描述" />
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
                @click="deleteTrackPoint(scope.row.id)"
              >
                <el-icon>Delete</el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="添加轨迹点" :visible="showCreateModal" @close="showCreateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="名称" required>
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="纬度" required>
            <el-input v-model="form.latitude" type="number" />
          </el-form-item>
          <el-form-item label="经度" required>
            <el-input v-model="form.longitude" type="number" />
          </el-form-item>
          <el-form-item label="描述">
            <el-textarea v-model="form.description" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" @click="createTrackPoint">保存</el-button>
        </template>
      </el-dialog>

      <el-dialog title="编辑轨迹点" :visible="showUpdateModal" @close="showUpdateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="名称" required>
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="纬度" required>
            <el-input v-model="form.latitude" type="number" />
          </el-form-item>
          <el-form-item label="经度" required>
            <el-input v-model="form.longitude" type="number" />
          </el-form-item>
          <el-form-item label="描述">
            <el-textarea v-model="form.description" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpdateModal = false">取消</el-button>
          <el-button type="primary" @click="updateTrackPoint">保存</el-button>
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