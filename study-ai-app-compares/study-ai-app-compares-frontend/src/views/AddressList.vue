<template>
  <div class="address-list-container">
    <header class="page-header">
      <div class="header-content">
        <h1 class="page-title">📍 收货地址管理</h1>
        <button class="back-btn" @click="$router.push('/')">← 返回首页</button>
      </div>
    </header>

    <main class="page-content">
      <div class="action-bar">
        <button class="add-btn" @click="showModal = true">+ 添加新地址</button>
      </div>

      <div v-if="addressStore.loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>

      <div v-else-if="addressStore.addresses.length === 0" class="empty-state">
        <div class="empty-icon">📭</div>
        <h3>暂无收货地址</h3>
        <p>点击上方按钮添加您的第一个收货地址</p>
      </div>

      <div v-else class="address-list">
        <div 
          v-for="address in addressStore.addresses" 
          :key="address.id"
          class="address-card"
          :class="{ 'default-card': address.isDefault }"
        >
          <div class="card-header">
            <span class="receiver-name">{{ address.receiverName }}</span>
            <span class="phone-number">{{ address.phoneNumber }}</span>
            <span v-if="address.isDefault" class="default-badge">默认</span>
          </div>
          <div class="card-body">
            <p class="full-address">
              {{ address.provinceCityDistrict }} {{ address.detailAddress }}
            </p>
          </div>
          <div class="card-actions">
            <button class="action-btn edit-btn" @click="editAddress(address)">编辑</button>
            <button class="action-btn default-btn" v-if="!address.isDefault" @click="setDefault(address.id)">设为默认</button>
            <button class="action-btn delete-btn" @click="deleteAddress(address.id)">删除</button>
          </div>
        </div>
      </div>
    </main>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ editingAddress ? '编辑地址' : '添加地址' }}</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveAddress">
            <div class="form-group">
              <label>收货人姓名</label>
              <input 
                v-model="formData.receiverName" 
                type="text" 
                placeholder="请输入收货人姓名"
                required
              />
            </div>
            <div class="form-group">
              <label>手机号码</label>
              <input 
                v-model="formData.phoneNumber" 
                type="tel" 
                placeholder="请输入手机号码"
                required
              />
            </div>
            <div class="form-group">
              <label>省市区</label>
              <input 
                v-model="formData.provinceCityDistrict" 
                type="text" 
                placeholder="请输入省市区，如：北京市朝阳区"
                required
              />
            </div>
            <div class="form-group">
              <label>详细地址</label>
              <input 
                v-model="formData.detailAddress" 
                type="text" 
                placeholder="请输入详细地址"
                required
              />
            </div>
            <div class="form-group checkbox-group">
              <input 
                v-model="formData.isDefault" 
                type="checkbox" 
                id="isDefault"
              />
              <label for="isDefault">设为默认地址</label>
            </div>
            <div class="form-actions">
              <button type="button" class="cancel-btn" @click="closeModal">取消</button>
              <button type="submit" class="submit-btn">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useAddressStore } from '../stores/address'

const addressStore = useAddressStore()
const showModal = ref(false)
const editingAddress = ref(null)

const formData = reactive({
  receiverName: '',
  phoneNumber: '',
  provinceCityDistrict: '',
  detailAddress: '',
  isDefault: false
})

const editAddress = (address) => {
  editingAddress.value = address
  formData.receiverName = address.receiverName
  formData.phoneNumber = address.phoneNumber
  formData.provinceCityDistrict = address.provinceCityDistrict
  formData.detailAddress = address.detailAddress
  formData.isDefault = address.isDefault
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  editingAddress.value = null
  formData.receiverName = ''
  formData.phoneNumber = ''
  formData.provinceCityDistrict = ''
  formData.detailAddress = ''
  formData.isDefault = false
}

const saveAddress = async () => {
  try {
    if (editingAddress.value) {
      await addressStore.updateAddress(editingAddress.value.id, { ...formData })
    } else {
      await addressStore.createAddress({ ...formData })
    }
    closeModal()
  } catch (error) {
    alert('保存失败: ' + error.message)
  }
}

const setDefault = async (id) => {
  try {
    const address = addressStore.getAddressById(id)
    await addressStore.updateAddress(id, {
      ...address,
      isDefault: true
    })
  } catch (error) {
    alert('设置默认地址失败: ' + error.message)
  }
}

const deleteAddress = async (id) => {
  if (!confirm('确定要删除这个地址吗？')) {
    return
  }
  try {
    await addressStore.deleteAddress(id)
  } catch (error) {
    alert('删除失败: ' + error.message)
  }
}
</script>

<style scoped>
.address-list-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.page-header {
  padding: 40px 20px;
  text-align: center;
}

.header-content {
  color: white;
}

.page-title {
  font-size: 2rem;
  margin-bottom: 20px;
  font-weight: 700;
}

.back-btn {
  padding: 10px 20px;
  background: rgba(255,255,255,0.2);
  color: white;
  border: 1px solid rgba(255,255,255,0.3);
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  background: rgba(255,255,255,0.3);
}

.page-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

.action-bar {
  margin-bottom: 30px;
}

.add-btn {
  padding: 12px 30px;
  background: white;
  color: #667eea;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.15);
}

.loading-container {
  text-align: center;
  padding: 60px;
  background: white;
  border-radius: 16px;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  background: white;
  border-radius: 16px;
  padding: 80px 40px;
  text-align: center;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.empty-state h3 {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 10px;
}

.empty-state p {
  color: #888;
}

.address-list {
  display: grid;
  gap: 20px;
}

.address-card {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.08);
  border: 2px solid transparent;
  transition: transform 0.3s, box-shadow 0.3s;
}

.address-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.12);
}

.default-card {
  border-color: #667eea;
  background: linear-gradient(135deg, #f8f9ff 0%, #ffffff 100%);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.receiver-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.phone-number {
  font-size: 1rem;
  color: #666;
}

.default-badge {
  background: #667eea;
  color: white;
  padding: 4px 12px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
}

.card-body {
  margin-bottom: 20px;
}

.full-address {
  font-size: 1rem;
  color: #555;
  line-height: 1.6;
}

.card-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.edit-btn {
  background: #f0f0f0;
  color: #667eea;
}

.edit-btn:hover {
  background: #667eea;
  color: white;
}

.default-btn {
  background: #fff3cd;
  color: #856404;
}

.default-btn:hover {
  background: #ffeeba;
}

.delete-btn {
  background: #f8d7da;
  color: #721c24;
}

.delete-btn:hover {
  background: #f5c6cb;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.close-btn {
  font-size: 24px;
  color: #999;
  border: none;
  background: none;
  cursor: pointer;
  line-height: 1;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 25px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
}

.checkbox-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
  transform: scale(1.2);
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 30px;
}

.cancel-btn {
  padding: 12px 25px;
  background: #f0f0f0;
  color: #666;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn:hover {
  background: #e0e0e0;
}

.submit-btn {
  padding: 12px 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}

@media (max-width: 768px) {
  .card-actions {
    flex-wrap: wrap;
  }
  
  .action-btn {
    flex: 1;
    min-width: 80px;
  }
}
</style>