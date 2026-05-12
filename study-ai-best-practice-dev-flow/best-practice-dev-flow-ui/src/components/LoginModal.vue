<template>
  <a-modal
    :open="visible"
    @update:open="$emit('update:visible', $event)"
    title=""
    :footer="null"
    width="420px"
    class="auth-modal"
    :closable="false"
  >
    <div class="auth-form">
      <div class="auth-header">
        <div class="auth-icon">
          <LoginOutlined />
        </div>
        <h2 class="auth-title">欢迎回来</h2>
        <p class="auth-subtitle">登录您的账户</p>
      </div>
      
      <a-form :model="formData" layout="vertical" class="form-content">
        <a-form-item :validate-status="usernameStatus" :help="usernameHelp">
          <a-input
            v-model:value="formData.username"
            placeholder="用户名或邮箱"
            class="auth-input"
            @blur="validateUsername"
          >
            <template #prefix>
              <UserOutlined class="input-icon" />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item :validate-status="passwordStatus" :help="passwordHelp">
          <a-input-password
            v-model:value="formData.password"
            placeholder="密码"
            class="auth-input"
            @blur="validatePassword"
          >
            <template #prefix>
              <LockOutlined class="input-icon" />
            </template>
          </a-input-password>
        </a-form-item>
        
        <div class="form-options">
          <a-checkbox v-model:checked="rememberMe">记住我</a-checkbox>
          <a href="#" class="forgot-link" @click.prevent>忘记密码？</a>
        </div>
        
        <a-form-item>
          <a-button
            type="primary"
            block
            @click="handleLogin"
            :loading="loading"
            class="submit-btn"
          >
            登录
          </a-button>
        </a-form-item>
        
        <a-divider>
          <span class="divider-text">或</span>
        </a-divider>
        
        <a-form-item>
          <a-button
            block
            @click="handleRegister"
            class="register-btn"
          >
            <template #icon>
              <UserAddOutlined />
            </template>
            创建账户
          </a-button>
        </a-form-item>
      </a-form>
      
      <div class="default-users">
        <p class="default-users-title">默认测试账户：</p>
        <div class="user-list">
          <div class="user-item">
            <span class="user-label">管理员：</span>
            <span class="user-value">admin / admin123</span>
          </div>
          <div class="user-item">
            <span class="user-label">用户1：</span>
            <span class="user-value">user1 / user123</span>
          </div>
          <div class="user-item">
            <span class="user-label">用户2：</span>
            <span class="user-value">user2 / user234</span>
          </div>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { 
  LoginOutlined, 
  UserOutlined, 
  LockOutlined,
  UserAddOutlined
} from '@ant-design/icons-vue'
import { userApi } from '../api/user'

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(['update:visible', 'login-success', 'show-register'])

const loading = ref(false)
const rememberMe = ref(false)
const usernameStatus = ref('')
const passwordStatus = ref('')
const usernameHelp = ref('')
const passwordHelp = ref('')

const formData = reactive({
  username: '',
  password: ''
})

const validateUsername = () => {
  if (!formData.username) {
    usernameStatus.value = 'error'
    usernameHelp.value = '请输入用户名'
    return false
  }
  usernameStatus.value = ''
  usernameHelp.value = ''
  return true
}

const validatePassword = () => {
  if (!formData.password) {
    passwordStatus.value = 'error'
    passwordHelp.value = '请输入密码'
    return false
  }
  if (formData.password.length < 6) {
    passwordStatus.value = 'error'
    passwordHelp.value = '密码长度至少6位'
    return false
  }
  passwordStatus.value = ''
  passwordHelp.value = ''
  return true
}

const handleLogin = async () => {
  const isUsernameValid = validateUsername()
  const isPasswordValid = validatePassword()
  
  if (!isUsernameValid || !isPasswordValid) {
    return
  }
  
  loading.value = true
  try {
    const result = await userApi.login(formData.username, formData.password)
    emit('login-success', result)
    emit('update:visible', false)
  } catch (error) {
    passwordStatus.value = 'error'
    passwordHelp.value = error.message || '登录失败，请检查用户名或密码'
  } finally {
    loading.value = false
  }
}

const handleRegister = () => {
  emit('update:visible', false)
  emit('show-register')
}
</script>

<style scoped>
.auth-modal :deep(.ant-modal-content) {
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}

.auth-form {
  padding: 8px;
}

.auth-header {
  text-align: center;
  margin-bottom: 28px;
}

.auth-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.auth-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #1f1f1f;
}

.auth-subtitle {
  font-size: 14px;
  color: #8c8c8c;
  margin: 0;
}

.form-content {
  margin-bottom: 20px;
}

.auth-input {
  border-radius: 8px;
  height: 44px;
}

.input-icon {
  color: #8c8c8c;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.forgot-link {
  color: #667eea;
  font-size: 14px;
}

.forgot-link:hover {
  color: #764ba2;
}

.submit-btn {
  height: 44px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-size: 16px;
  font-weight: 500;
}

.submit-btn:hover {
  opacity: 0.9;
}

.divider-text {
  color: #8c8c8c;
  font-size: 14px;
}

.register-btn {
  height: 44px;
  border-radius: 8px;
  border: 1px solid #d9d9d9;
  color: #667eea;
  font-size: 16px;
}

.register-btn:hover {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.default-users {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.default-users-title {
  font-size: 13px;
  color: #8c8c8c;
  margin: 0 0 12px;
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.user-item {
  display: flex;
  font-size: 13px;
}

.user-label {
  color: #8c8c8c;
  margin-right: 8px;
}

.user-value {
  color: #1f1f1f;
  font-family: monospace;
}
</style>