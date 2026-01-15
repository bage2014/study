<script setup>
import { ref, onMounted, computed } from 'vue'

// API配置
const API_URL = 'http://localhost:8080/api/family-tree'

// 状态管理
const persons = ref([])
const relationships = ref([])
const loading = ref(true)
const error = ref(null)
const activeTab = ref('graph') // 'graph', 'persons', 'relationships'

// 表单数据
const newPerson = ref({
  name: '',
  gender: '男',
  birthDate: '',
  deathDate: '',
  description: ''
})

const newRelationship = ref({
  person1Id: '',
  person2Id: '',
  type: '',
  description: ''
})

// 加载数据
async function fetchData() {
  try {
    loading.value = true
    const [personsRes, relationshipsRes] = await Promise.all([
      fetch(`${API_URL}/persons`),
      fetch(`${API_URL}/relationships`)
    ])
    
    if (!personsRes.ok || !relationshipsRes.ok) {
      throw new Error('Failed to fetch data')
    }
    
    persons.value = await personsRes.json()
    relationships.value = await relationshipsRes.json()
    error.value = null
  } catch (err) {
    error.value = err.message
    persons.value = []
    relationships.value = []
  } finally {
    loading.value = false
  }
}

// 添加人员
async function addPerson() {
  try {
    const response = await fetch(`${API_URL}/persons`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newPerson.value)
    })
    
    if (!response.ok) {
      throw new Error('Failed to add person')
    }
    
    const addedPerson = await response.json()
    persons.value.push(addedPerson)
    
    // 重置表单
    newPerson.value = {
      name: '',
      gender: '男',
      birthDate: '',
      deathDate: '',
      description: ''
    }
  } catch (err) {
    error.value = err.message
  }
}

// 添加关系
async function addRelationship() {
  try {
    const response = await fetch(`${API_URL}/relationships`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...newRelationship.value,
        person1Id: Number(newRelationship.value.person1Id),
        person2Id: Number(newRelationship.value.person2Id)
      })
    })
    
    if (!response.ok) {
      throw new Error('Failed to add relationship')
    }
    
    const addedRelationship = await response.json()
    relationships.value.push(addedRelationship)
    
    // 重置表单
    newRelationship.value = {
      person1Id: '',
      person2Id: '',
      type: '',
      description: ''
    }
  } catch (err) {
    error.value = err.message
  }
}

// 删除人员
async function deletePerson(id) {
  try {
    const response = await fetch(`${API_URL}/persons/${id}`, {
      method: 'DELETE'
    })
    
    if (!response.ok) {
      throw new Error('Failed to delete person')
    }
    
    persons.value = persons.value.filter(p => p.id !== id)
    relationships.value = relationships.value.filter(r => r.person1Id !== id && r.person2Id !== id)
  } catch (err) {
    error.value = err.message
  }
}

// 删除关系
async function deleteRelationship(id) {
  try {
    const response = await fetch(`${API_URL}/relationships/${id}`, {
      method: 'DELETE'
    })
    
    if (!response.ok) {
      throw new Error('Failed to delete relationship')
    }
    
    relationships.value = relationships.value.filter(r => r.id !== id)
  } catch (err) {
    error.value = err.message
  }
}

// 格式化日期
function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

// 获取人员名称
function getPersonName(id) {
  const person = persons.value.find(p => p.id === id)
  return person ? person.name : `未知(${id})`
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="container">
    <h1>家庭族谱应用</h1>
    <div class="hello-message">Hello World! 这是一个基于Vue 3的家庭族谱应用</div>
    
    <div v-if="error" class="error">{{ error }}</div>
    
    <div v-else-if="loading" class="loading">
      加载数据中...
    </div>
    
    <div v-else>
      <!-- 标签页 -->
      <div class="tabs">
        <button 
          class="tab-button" 
          :class="{ active: activeTab === 'graph' }" 
          @click="activeTab = 'graph'"
        >
          关系图
        </button>
        <button 
          class="tab-button" 
          :class="{ active: activeTab === 'persons' }" 
          @click="activeTab = 'persons'"
        >
          人员管理
        </button>
        <button 
          class="tab-button" 
          :class="{ active: activeTab === 'relationships' }" 
          @click="activeTab = 'relationships'"
        >
          关系管理
        </button>
      </div>
      
      <!-- 关系图标签页 -->
      <div v-if="activeTab === 'graph'" class="graph-tab">
        <h2>家庭关系图</h2>
        <div class="graph-container">
          <div class="graph-info">
            <p>共有 {{ persons.length }} 位家庭成员</p>
            <p>共有 {{ relationships.length }} 条关系记录</p>
          </div>
          <!-- 美化的关系图显示 -->
          <div class="beautified-graph">
            <!-- 中心人员展示 -->
            <div class="graph-center">
              <div class="graph-title">家庭关系</div>
              <div class="graph-visualization">
                <!-- 人员节点 -->
                <div class="nodes-container">
                  <div 
                    v-for="person in persons" 
                    :key="person.id" 
                    class="person-node"
                    :class="{ 'male': person.gender === '男', 'female': person.gender === '女' }"
                  >
                    <div class="person-avatar">
                      {{ person.name.charAt(0) }}
                    </div>
                    <div class="person-name">{{ person.name }}</div>
                    <div class="person-details">
                      <span class="birth-date">{{ formatDate(person.birthDate) }}</span>
                      <span class="status">{{ person.deathDate ? '已故' : '在世' }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 关系连接 -->
            <div class="relationships-section">
              <h3>关系列表</h3>
              <div class="relationships-grid">
                <div 
                  v-for="relationship in relationships" 
                  :key="relationship.id" 
                  class="relationship-card"
                >
                  <div class="relationship-content">
                    <div class="person-pair">
                      <div class="person-info">
                        <div class="person-avatar small">
                          {{ getPersonName(relationship.person1Id).charAt(0) }}
                        </div>
                        <div class="person-name">{{ getPersonName(relationship.person1Id) }}</div>
                      </div>
                      
                      <div class="relationship-connector">
                        <div class="connector-line"></div>
                        <div class="relationship-type-badge">{{ relationship.type }}</div>
                      </div>
                      
                      <div class="person-info">
                        <div class="person-avatar small">
                          {{ getPersonName(relationship.person2Id).charAt(0) }}
                        </div>
                        <div class="person-name">{{ getPersonName(relationship.person2Id) }}</div>
                      </div>
                    </div>
                    
                    <div class="relationship-description">
                      {{ relationship.description }}
                    </div>
                  </div>
                  
                  <button 
                    class="delete-btn small" 
                    @click="deleteRelationship(relationship.id)"
                    title="删除关系"
                  >
                    ×
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 人员管理标签页 -->
      <div v-if="activeTab === 'persons'" class="persons-tab">
        <h2>人员管理</h2>
        
        <!-- 添加人员表单 -->
        <div class="add-form">
          <h3>添加新成员</h3>
          <form @submit.prevent="addPerson">
            <div class="form-group">
              <label>姓名:</label>
              <input type="text" v-model="newPerson.name" required>
            </div>
            <div class="form-group">
              <label>性别:</label>
              <select v-model="newPerson.gender">
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
            </div>
            <div class="form-group">
              <label>出生日期:</label>
              <input type="date" v-model="newPerson.birthDate">
            </div>
            <div class="form-group">
              <label>去世日期:</label>
              <input type="date" v-model="newPerson.deathDate">
            </div>
            <div class="form-group">
              <label>描述:</label>
              <textarea v-model="newPerson.description"></textarea>
            </div>
            <button type="submit" class="submit-btn">添加人员</button>
          </form>
        </div>
        
        <!-- 人员列表 -->
        <div class="persons-list">
          <h3>人员列表</h3>
          <div class="list-container">
            <div 
              v-for="person in persons" 
              :key="person.id" 
              class="person-item"
            >
              <div class="person-info">
                <div class="person-name">{{ person.name }}</div>
                <div class="person-details">
                  <span>{{ person.gender }}</span>
                  <span>{{ formatDate(person.birthDate) }}</span>
                  <span>{{ person.deathDate ? formatDate(person.deathDate) : '在世' }}</span>
                </div>
                <div class="person-description">{{ person.description }}</div>
              </div>
              <button 
                class="delete-btn" 
                @click="deletePerson(person.id)"
              >
                删除
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 关系管理标签页 -->
      <div v-if="activeTab === 'relationships'" class="relationships-tab">
        <h2>关系管理</h2>
        
        <!-- 添加关系表单 -->
        <div class="add-form">
          <h3>添加新关系</h3>
          <form @submit.prevent="addRelationship">
            <div class="form-group">
              <label>人员1:</label>
              <select v-model="newRelationship.person1Id" required>
                <option value="">选择人员</option>
                <option 
                  v-for="person in persons" 
                  :key="person.id" 
                  :value="person.id"
                >
                  {{ person.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>关系类型:</label>
              <input type="text" v-model="newRelationship.type" required placeholder="如：父亲、母亲、儿子等">
            </div>
            <div class="form-group">
              <label>人员2:</label>
              <select v-model="newRelationship.person2Id" required>
                <option value="">选择人员</option>
                <option 
                  v-for="person in persons" 
                  :key="person.id" 
                  :value="person.id"
                >
                  {{ person.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>描述:</label>
              <textarea v-model="newRelationship.description"></textarea>
            </div>
            <button type="submit" class="submit-btn">添加关系</button>
          </form>
        </div>
        
        <!-- 关系列表 -->
        <div class="relationships-list">
          <h3>关系列表</h3>
          <div class="list-container">
            <div 
              v-for="relationship in relationships" 
              :key="relationship.id" 
              class="relationship-item"
            >
              <div class="relationship-info">
                <div class="relationship-pair">
                  <span>{{ getPersonName(relationship.person1Id) }}</span>
                  <span class="relationship-type">{{ relationship.type }}</span>
                  <span>{{ getPersonName(relationship.person2Id) }}</span>
                </div>
                <div class="relationship-description">{{ relationship.description }}</div>
              </div>
              <button 
                class="delete-btn" 
                @click="deleteRelationship(relationship.id)"
              >
                删除
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 10px;
}

h2 {
  color: #34495e;
  margin-bottom: 20px;
  border-bottom: 2px solid #3498db;
  padding-bottom: 10px;
}

h3 {
  color: #7f8c8d;
  margin-bottom: 15px;
}

.hello-message {
  text-align: center;
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.tabs {
  display: flex;
  margin-bottom: 30px;
  border-bottom: 1px solid #ddd;
}

.tab-button {
  padding: 12px 24px;
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  margin-right: 10px;
  color: #7f8c8d;
}

.tab-button.active {
  color: #3498db;
  border-bottom: 3px solid #3498db;
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

/* 表单样式 */
.add-form {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #2c3e50;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-group textarea {
  height: 80px;
  resize: vertical;
}

.submit-btn {
  background-color: #3498db;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-btn:hover {
  background-color: #2980b9;
}

/* 列表样式 */
.list-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.person-item,
.relationship-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
}

.person-item:last-child,
.relationship-item:last-child {
  border-bottom: none;
}

.person-info,
.relationship-info {
  flex: 1;
}

.person-name {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 5px;
  color: #2c3e50;
}

.person-details {
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 5px;
}

.person-details span {
  margin-right: 15px;
}

.person-description {
  color: #95a5a6;
  font-size: 14px;
  font-style: italic;
}

.relationship-pair {
  display: flex;
  align-items: center;
  font-size: 16px;
  margin-bottom: 5px;
}

.relationship-type {
  background-color: #3498db;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  margin: 0 10px;
  font-size: 14px;
}

.relationship-description {
  color: #95a5a6;
  font-size: 14px;
  font-style: italic;
}

.delete-btn {
  background-color: #e74c3c;
  color: white;
  padding: 5px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.delete-btn:hover {
  background-color: #c0392b;
}

/* 关系图样式 */
.graph-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.graph-info {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

/* 美化的关系图样式 */
.beautified-graph {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.graph-center {
  text-align: center;
}

.graph-title {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 20px;
}

.graph-visualization {
  background-color: #f0f4f8;
  border-radius: 12px;
  padding: 30px;
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nodes-container {
  display: flex;
  flex-wrap: wrap;
  gap: 25px;
  justify-content: center;
  align-items: center;
}

.person-node {
  background-color: white;
  border-radius: 12px;
  padding: 15px;
  width: 150px;
  text-align: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.person-node:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.person-node.male {
  border-top: 4px solid #3498db;
}

.person-node.female {
  border-top: 4px solid #e91e63;
}

.person-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  margin: 0 auto 10px;
  background-color: #3498db;
  color: white;
}

.person-node.male .person-avatar {
  background-color: #3498db;
}

.person-node.female .person-avatar {
  background-color: #e91e63;
}

.person-avatar.small {
  width: 40px;
  height: 40px;
  font-size: 16px;
  margin: 0 auto 8px;
}

.person-name {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 8px;
  color: #2c3e50;
}

.person-details {
  font-size: 12px;
  color: #7f8c8d;
}

.birth-date {
  display: block;
  margin-bottom: 4px;
}

.status {
  padding: 2px 8px;
  border-radius: 12px;
  background-color: #e7f3ff;
  color: #3498db;
  font-size: 10px;
}

/* 关系部分 */
.relationships-section {
  margin-top: 20px;
}

.relationships-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.relationship-card {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
  border-left: 4px solid #3498db;
}

.relationship-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.person-pair {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.person-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.relationship-connector {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  position: relative;
}

.connector-line {
  width: 100%;
  height: 2px;
  background-color: #e0e0e0;
  position: relative;
}

.relationship-type-badge {
  padding: 4px 12px;
  border-radius: 16px;
  background-color: #3498db;
  color: white;
  font-size: 12px;
  font-weight: bold;
  position: relative;
  z-index: 1;
  box-shadow: 0 2px 8px rgba(52, 152, 219, 0.3);
}

.relationship-description {
  font-size: 14px;
  color: #7f8c8d;
  font-style: italic;
  text-align: center;
  padding: 10px 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.relationship-card .delete-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 6px 10px;
  font-size: 12px;
}

.delete-btn.small {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  line-height: 1;
}

@media (max-width: 768px) {
  .simple-graph {
    grid-template-columns: 1fr;
  }
  
  .beautified-graph {
    gap: 20px;
  }
  
  .graph-visualization {
    padding: 20px;
  }
  
  .nodes-container {
    gap: 15px;
  }
  
  .person-node {
    width: 120px;
    padding: 12px;
  }
  
  .person-avatar {
    width: 50px;
    height: 50px;
    font-size: 20px;
  }
  
  .relationships-grid {
    grid-template-columns: 1fr;
  }
  
  .person-pair {
    flex-direction: column;
    gap: 15px;
  }
  
  .relationship-connector {
    flex-direction: row;
    gap: 15px;
  }
  
  .connector-line {
    width: 2px;
    height: 40px;
  }
  
  .person-item,
  .relationship-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .delete-btn {
    margin-top: 10px;
  }
}
</style>
