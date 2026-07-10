<template>
  <div>
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:20px">
      <h2>商品列表</h2>
      <el-button type="primary" @click="openCreate">
        <el-icon><Plus /></el-icon> 新增商品
      </el-button>
    </div>

    <el-table :data="products" stripe v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editForm.id ? '编辑商品' : '新增商品'" width="480px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="editForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="editForm.stock" :min="0" />
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
import { productApi } from '../api'

const products = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editForm = ref({ id: null, name: '', description: '', price: 0, stock: 0 })

async function load() {
  loading.value = true
  try {
    const { data } = await productApi.list()
    products.value = data
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editForm.value = { id: null, name: '', description: '', price: 0, stock: 0 }
  dialogVisible.value = true
}

function openEdit(row) {
  editForm.value = { ...row }
  dialogVisible.value = true
}

async function save() {
  try {
    if (editForm.value.id) {
      await productApi.update(editForm.value.id, editForm.value)
    } else {
      await productApi.create(editForm.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

async function remove(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await productApi.remove(id)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>