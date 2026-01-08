<script setup>
import { ref, onMounted } from 'vue'

const users = ref([])
const loading = ref(true)
const error = ref(null)

const API_URL = 'http://localhost:8080/api/users'

async function fetchUsers() {
  try {
    loading.value = true
    const response = await fetch(API_URL)
    if (!response.ok) {
      throw new Error('Failed to fetch users')
    }
    users.value = await response.json()
    error.value = null
  } catch (err) {
    error.value = err.message
    users.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<template>
  <div class="container">
    <h1>用户列表</h1>
    <div class="hello-message">Hello World! 这是一个基于Vue 3的用户列表应用</div>
    
    <div v-if="error" class="error">{{ error }}</div>
    
    <div v-else-if="loading" class="loading">
      加载用户列表中...
    </div>
    
    <div v-else class="user-list">
      <div 
        v-for="user in users" 
        :key="user.id" 
        class="user-item"
      >
        <div class="user-info">
          <div class="user-name">{{ user.name }}</div>
          <div class="user-email">{{ user.email }}</div>
        </div>
        <div class="user-id">ID: {{ user.id }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
}

.hello-message {
  text-align: center;
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.user-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.user-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
}

.user-item:last-child {
  border-bottom: none;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 5px;
}

.user-email {
  color: #666;
  font-size: 14px;
}

.user-id {
  background-color: #3498db;
  color: white;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.error {
  text-align: center;
  padding: 20px;
  color: #e74c3c;
  background-color: #ffebee;
  border-radius: 4px;
  margin: 20px 0;
}
</style>
