<template>
  <div>
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:20px">
      <h2>图书列表</h2>
      <el-button type="primary" @click="openCreate">
        <el-icon><Plus /></el-icon> 新增图书
      </el-button>
    </div>

    <el-table :data="books" stripe v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" width="160" />
      <el-table-column prop="isbn" label="ISBN" width="180" show-overflow-tooltip />
      <el-table-column prop="price" label="价格" width="120" :formatter="formatPrice" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editForm.id ? '编辑图书' : '新增图书'" width="480px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="书名">
          <el-input v-model="editForm.title" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="editForm.author" />
        </el-form-item>
        <el-form-item label="ISBN">
          <el-input v-model="editForm.isbn" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="editForm.price" :min="0" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { bookApi } from '../api'

const books = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editForm = ref({ id: null, title: '', author: '', isbn: '', price: 0 })

function formatPrice(row) {
  return `¥${row.price}`
}

async function load() {
  loading.value = true
  try {
    const { data } = await bookApi.list()
    books.value = data
  } catch (e) {
    // 后端不可用时保持空列表
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editForm.value = { id: null, title: '', author: '', isbn: '', price: 0 }
  dialogVisible.value = true
}

function openEdit(row) {
  editForm.value = { ...row }
  dialogVisible.value = true
}

async function save() {
  try {
    if (editForm.value.id) {
      await bookApi.update(editForm.value.id, editForm.value)
    } else {
      await bookApi.create(editForm.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  }
}

async function remove(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await bookApi.remove(id)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>
