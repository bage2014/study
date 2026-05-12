<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { relationshipAPI, memberAPI } from '@/api'
import type { RelationshipRequest } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()
const relationships = ref<any[]>([])
const members = ref<any[]>([])

const showCreateModal = ref(false)

const form = ref<RelationshipRequest>({
  member1Id: 0,
  member2Id: 0,
  relationshipType: '',
  familyId: 0
})

const relationshipOptions = [
  { value: 'PARENT_CHILD', label: '父母-子女' },
  { value: 'SPOUSE', label: '夫妻' },
  { value: 'SIBLING', label: '兄弟姐妹' },
  { value: 'GRANDPARENT_GRANDCHILD', label: '祖孙' },
  { value: 'UNCLE_NEPHEW', label: '叔侄' },
  { value: 'AUNT_NIECE', label: '姑侄' }
]

async function loadRelationships() {
  if (familyStore.currentFamily) {
    relationships.value = await relationshipAPI.getByFamily(familyStore.currentFamily.id)
  }
}

async function loadMembers() {
  if (familyStore.currentFamily) {
    members.value = await memberAPI.getByFamily(familyStore.currentFamily.id)
  }
}

async function createRelationship() {
  form.value.familyId = familyStore.currentFamily?.id || 0
  await relationshipAPI.create(form.value)
  showCreateModal.value = false
  resetForm()
  await loadRelationships()
}

function resetForm() {
  form.value = {
    member1Id: 0,
    member2Id: 0,
    relationshipType: '',
    familyId: 0
  }
}

async function deleteRelationship(id: number) {
  await relationshipAPI.delete(id)
  await loadRelationships()
}

function getMemberName(id: number) {
  const member = members.value.find(m => m.id === id)
  return member?.name || '未知'
}

function getRelationshipLabel(type: string) {
  const option = relationshipOptions.find(o => o.value === type)
  return option?.label || type
}

watch(() => familyStore.currentFamily, async () => {
  await loadRelationships()
  await loadMembers()
})

onMounted(async () => {
  await loadRelationships()
  await loadMembers()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>关系管理</h1>
        <el-button type="primary" @click="showCreateModal = true">
          <el-icon>Plus</el-icon>
          添加关系
        </el-button>
      </div>

      <div class="card">
        <el-table :data="relationships" border>
          <el-table-column label="成员1">
            <template #default="scope">
              {{ getMemberName(scope.row.member1Id) }}
            </template>
          </el-table-column>
          <el-table-column prop="relationshipType" label="关系">
            <template #default="scope">
              {{ getRelationshipLabel(scope.row.relationshipType) }}
            </template>
          </el-table-column>
          <el-table-column label="成员2">
            <template #default="scope">
              {{ getMemberName(scope.row.member2Id) }}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button 
                size="small" 
                type="danger" 
                @click="deleteRelationship(scope.row.id)"
              >
                <el-icon>Delete</el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog title="添加关系" :visible="showCreateModal" @close="showCreateModal = false">
        <el-form :model="form" label-width="100px">
          <el-form-item label="成员1" required>
            <el-select v-model="form.member1Id">
              <el-option v-for="member in members" :key="member.id" :label="member.name" :value="member.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="关系类型" required>
            <el-select v-model="form.relationshipType">
              <el-option v-for="opt in relationshipOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="成员2" required>
            <el-select v-model="form.member2Id">
              <el-option v-for="member in members" :key="member.id" :label="member.name" :value="member.id" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateModal = false">取消</el-button>
          <el-button type="primary" @click="createRelationship">保存</el-button>
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