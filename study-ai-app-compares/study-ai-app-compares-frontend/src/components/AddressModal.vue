<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h2>{{ address ? '编辑地址' : '添加收货地址' }}</h2>
        <button class="close-btn" @click="$emit('close')">×</button>
      </div>
      
      <form class="address-form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>收货人姓名 *</label>
          <input
            v-model="form.receiverName"
            type="text"
            class="form-input"
            placeholder="请输入收货人姓名"
            required
          />
        </div>
        
        <div class="form-group">
          <label>手机号码 *</label>
          <input
            v-model="form.phoneNumber"
            type="tel"
            class="form-input"
            placeholder="请输入手机号码"
            required
          />
        </div>
        
        <div class="form-group">
          <label>省市区 *</label>
          <input
            v-model="form.provinceCityDistrict"
            type="text"
            class="form-input"
            placeholder="如：北京市朝阳区"
            required
          />
        </div>
        
        <div class="form-group">
          <label>详细地址 *</label>
          <textarea
            v-model="form.detailAddress"
            class="form-textarea"
            placeholder="请输入详细地址"
            required
          ></textarea>
        </div>
        
        <div class="form-group checkbox-group">
          <label class="checkbox-label">
            <input
              v-model="form.isDefault"
              type="checkbox"
              value="true"
            />
            <span>设为默认地址</span>
          </label>
        </div>
        
        <div class="form-actions">
          <button type="button" class="btn btn-cancel" @click="$emit('close')">取消</button>
          <button type="submit" class="btn btn-submit">保存</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  address: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'save'])

const form = ref({
  receiverName: '',
  phoneNumber: '',
  provinceCityDistrict: '',
  detailAddress: '',
  isDefault: false
})

onMounted(() => {
  if (props.address) {
    form.value = {
      receiverName: props.address.receiverName,
      phoneNumber: props.address.phoneNumber,
      provinceCityDistrict: props.address.provinceCityDistrict,
      detailAddress: props.address.detailAddress,
      isDefault: props.address.isDefault || false
    }
  }
})

const handleSubmit = () => {
  emit('save', {
    receiverName: form.value.receiverName,
    phoneNumber: form.value.phoneNumber,
    provinceCityDistrict: form.value.provinceCityDistrict,
    detailAddress: form.value.detailAddress,
    isDefault: form.value.isDefault
  })
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-content {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 500px;
  overflow: hidden;
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  border-bottom: 1px solid #eee;
}

.modal-header h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-btn {
  width: 30px;
  height: 30px;
  border: none;
  background: #f5f5f5;
  border-radius: 50%;
  font-size: 24px;
  line-height: 26px;
  cursor: pointer;
  color: #666;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #eee;
  color: #333;
}

.address-form {
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

.form-input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
}

.form-textarea {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  min-height: 80px;
  resize: vertical;
  transition: border-color 0.3s;
}

.form-textarea:focus {
  outline: none;
  border-color: #667eea;
}

.checkbox-group {
  margin-top: 5px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.checkbox-label span {
  font-weight: normal;
  color: #666;
}

.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-cancel:hover {
  background: #eee;
}

.btn-submit {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-submit:hover {
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}
</style>