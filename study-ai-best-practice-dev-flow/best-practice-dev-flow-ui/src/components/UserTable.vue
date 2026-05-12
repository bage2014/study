<template>
  <a-card title="用户管理" :bordered="false">
    <div style="display: flex; justify-content: flex-end; margin-bottom: 16px;">
      <a-button type="primary" @click="showAddModal = true">
        <template #icon>
          <PlusOutlined />
        </template>
        添加用户
      </a-button>
    </div>
    
    <a-table :columns="columns" :data-source="users" :row-key="'id'" :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" @click="handleEdit(record)">编辑</a-button>
            <a-button size="small" danger @click="handleDelete(record)">删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal
      v-model:open="showAddModal"
      :title="editingUser ? '编辑用户' : '添加用户'"
      :footer="null"
      width="400px"
    >
      <a-form :model="formData" layout="vertical">
        <a-form-item label="用户名" :required="true">
          <a-input
            v-model:value="formData.username"
            placeholder="请输入用户名"
          />
        </a-form-item>
        <a-form-item label="邮箱" :required="true">
          <a-input
            v-model:value="formData.email"
            placeholder="请输入邮箱"
          />
        </a-form-item>
        <a-form-item label="密码" v-if="!editingUser" :required="true">
          <a-input-password
            v-model:value="formData.password"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item label="新密码" v-else>
          <a-input-password
            v-model:value="formData.password"
            placeholder="留空则不修改密码"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button @click="showAddModal = false">取消</a-button>
            <a-button
              type="primary"
              @click="handleSubmit"
              :loading="submitLoading"
            >
              {{ editingUser ? '保存' : '添加' }}
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { userApi } from '../api/user'

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
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '邮箱', dataIndex: 'email', key: 'email' },
  { title: '操作', key: 'action' }
]

const loadUsers = async () => {
  loading.value = true
  try {
    users.value = await userApi.getAllUsers()
  } catch (error) {
    alert(error.message)
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
  if (!confirm(`确定要删除用户 ${record.username} 吗？`)) {
    return
  }
  
  try {
    await userApi.deleteUser(record.id)
    await loadUsers()
    alert('删除成功')
  } catch (error) {
    alert(error.message)
  }
}

const handleSubmit = async () => {
  if (!formData.username || !formData.email) {
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
    } else {
      if (!formData.password) {
        alert('请输入密码')
        submitLoading.value = false
        return
      }
      await userApi.register(formData.username, formData.email, formData.password)
    }
    
    showAddModal.value = false
    await loadUsers()
    alert(editingUser.value ? '更新成功' : '添加成功')
  } catch (error) {
    alert(error.message)
  } finally {
    submitLoading.value = false
  }
}

const resetForm = () => {
  editingUser.value = null
  formData.username = ''
  formData.email = ''
  formData.password = ''
}

const openModal = () => {
  resetForm()
  showAddModal.value = true
}

defineExpose({ openModal })

onMounted(() => {
  loadUsers()
})
</script>