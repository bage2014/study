<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <h2 class="text-2xl font-bold text-gray-900">用户管理</h2>
      <button
        @click="showForm = !showForm"
        class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
      >
        {{ showForm ? '取消' : '新增用户' }}
      </button>
    </div>

    <div v-if="showForm" class="bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-medium text-gray-900 mb-4">
        {{ editingUser ? '编辑用户' : '新增用户' }}
      </h3>
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">用户名</label>
          <input
            v-model="form.username"
            type="text"
            required
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 border px-3 py-2"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">邮箱</label>
          <input
            v-model="form.email"
            type="email"
            required
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 border px-3 py-2"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">手机号</label>
          <input
            v-model="form.phone"
            type="text"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 border px-3 py-2"
          />
        </div>
        <div class="flex space-x-2">
          <button
            type="submit"
            class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
          >
            {{ editingUser ? '更新' : '创建' }}
          </button>
          <button
            type="button"
            @click="resetForm"
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 transition"
          >
            重置
          </button>
        </div>
      </form>
    </div>

    <div class="bg-white rounded-lg shadow overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">用户名</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">邮箱</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">手机号</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="user in users" :key="user.id">
            <td class="px-6 py-4 text-sm text-gray-900">{{ user.id }}</td>
            <td class="px-6 py-4 text-sm text-gray-900">{{ user.username }}</td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ user.email }}</td>
            <td class="px-6 py-4 text-sm text-gray-500">{{ user.phone || '-' }}</td>
            <td class="px-6 py-4 text-sm space-x-2">
              <button
                @click="handleEdit(user)"
                class="text-blue-600 hover:text-blue-800"
              >
                编辑
              </button>
              <button
                @click="handleDelete(user.id)"
                class="text-red-600 hover:text-red-800"
              >
                删除
              </button>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="5" class="px-6 py-4 text-center text-gray-500">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { userApi } from '@/services/api'
import type { User } from '@/types'

const users = ref<User[]>([])
const showForm = ref(false)
const editingUser = ref<User | null>(null)

const form = reactive({
  username: '',
  email: '',
  phone: '',
})

async function loadUsers() {
  try {
    const { data } = await userApi.findAll()
    users.value = data
  } catch {
    console.log('后端未启动或无数据')
  }
}

async function handleSubmit() {
  try {
    if (editingUser.value) {
      await userApi.update(editingUser.value.id, { ...form })
    } else {
      await userApi.create({ ...form })
    }
    resetForm()
    await loadUsers()
  } catch (err: any) {
    const msg = err.response?.data?.message || '操作失败'
    alert(msg)
  }
}

function handleEdit(user: User) {
  editingUser.value = user
  form.username = user.username
  form.email = user.email
  form.phone = user.phone || ''
  showForm.value = true
}

async function handleDelete(id: number) {
  if (!confirm('确认删除该用户？')) return
  try {
    await userApi.delete(id)
    await loadUsers()
  } catch (err: any) {
    const msg = err.response?.data?.message || '删除失败'
    alert(msg)
  }
}

function resetForm() {
  editingUser.value = null
  form.username = ''
  form.email = ''
  form.phone = ''
  showForm.value = false
}

onMounted(loadUsers)
</script>
