<template>
  <a-card title="用户管理" :bordered="false" class="user-table-card">
    <div class="table-header">
      <a-button type="primary" @click="openAddModal">
        <template #icon>
          <PlusOutlined />
        </template>
        添加用户
      </a-button>
    </div>
    
    <a-table 
      :columns="columns" 
      :data-source="users" 
      :row-key="'id'" 
      :loading="loading"
      :pagination="{ pageSize: 10 }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space size="small">
            <a-button size="small" type="link" @click="handleEdit(record)">编辑</a-button>
            <a-button size="small" type="link" danger @click="handleDelete(record)">删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal
      v-model:open="showAddModal"
      :title="editingUser ? '编辑用户' : '添加用户'"
      @cancel="resetForm"
      width="450px"
    >
      <a-form :model="formData" layout="vertical">
        <a-form-item label="用户名" :required="true">
          <a-input
            v-model:value="formData.username"
            placeholder="请输入用户名（3-20位）"
          />
        </a-form-item>
        <a-form-item label="邮箱" :required="true">
          <a-input
            v-model:value="formData.email"
            placeholder="请输入邮箱地址"
          />
        </a-form-item>
        <a-form-item label="密码" v-if="!editingUser" :required="true">
          <a-input-password
            v-model:value="formData.password"
            placeholder="请输入密码（至少6位）"
          />
        </a-form-item>
        <a-form-item label="新密码" v-else>
          <a-input-password
            v-model:value="formData.password"
            placeholder="留空则不修改密码"
          />
        </a-form-item>
      </a-form>
      
      <template #footer>
        <a-space>
          <a-button @click="resetForm">取消</a-button>
          <a-button
            type="primary"
            @click="handleSubmit"
            :loading="submitLoading"
          >
            {{ editingUser ? '保存' : '添加' }}
          </a-button>
        </a-space>
      </template>
    </a-modal>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { userApi } from '../api/user'
import { message } from 'ant-design-vue'

const users = ref([])
const loading = ref(false)
const showAddModal = ref(false)
const editingUser = ref(null)
const submitLoading = ref(false)

const formData = reactive({
  username: '',
  email: '',
  password: ''
})

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '邮箱', dataIndex: 'email', key: 'email' },
  { title: '操作', key: 'action', width: 120 }
]

const loadUsers = async () => {
  loading.value = true
  try {
    console.log('Loading users...')
    const token = localStorage.getItem('token')
    console.log('Current token:', token ? 'exists' : 'not found')
    users.value = await userApi.getAllUsers()
    console.log('Users loaded:', users.value)
  } catch (error) {
    console.error('Load users error:', error)
    message.error(error.message || '获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleEdit = (record) => {
  editingUser.value = record
  formData.username = record.username
  formData.email = record.email
  formData.password = ''
  showAddModal.value = true
}

const handleDelete = async (record) => {
  try {
    await userApi.deleteUser(record.id)
    await loadUsers()
    message.success('删除成功')
  } catch (error) {
    message.error(error.message || '删除失败')
  }
}

const handleSubmit = async () => {
  if (!formData.username || !formData.email) {
    message.warning('请填写完整信息')
    return
  }
  
  if (!editingUser.value && !formData.password) {
    message.warning('请输入密码')
    return
  }
  
  submitLoading.value = true
  try {
    const data = {
      username: formData.username,
      email: formData.email
    }
    if (formData.password) {
      data.password = formData.password
    }
    
    if (editingUser.value) {
      await userApi.updateUser(editingUser.value.id, data)
      message.success('更新成功')
    } else {
      await userApi.register(formData.username, formData.email, formData.password)
      message.success('添加成功')
    }
    
    resetForm()
    await loadUsers()
  } catch (error) {
    message.error(error.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

const resetForm = () => {
  editingUser.value = null
  formData.username = ''
  formData.email = ''
  formData.password = ''
  showAddModal.value = false
}

const openAddModal = () => {
  resetForm()
  showAddModal.value = true
}

defineExpose({ openModal: openAddModal, loadUsers })

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-table-card {
  border-radius: 12px;
}

.table-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
}
</style>