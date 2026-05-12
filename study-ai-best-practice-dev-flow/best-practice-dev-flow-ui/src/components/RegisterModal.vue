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
        <div class="auth-icon register-icon">
          <UserAddOutlined />
        </div>
        <h2 class="auth-title">创建账户</h2>
        <p class="auth-subtitle">注册成为会员</p>
      </div>
      
      <a-form :model="formData" layout="vertical" class="form-content">
        <a-form-item :validate-status="usernameStatus" :help="usernameHelp">
          <a-input
            v-model:value="formData.username"
            placeholder="用户名"
            class="auth-input"
            @blur="validateUsername"
          >
            <template #prefix>
              <UserOutlined class="input-icon" />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item :validate-status="emailStatus" :help="emailHelp">
          <a-input
            v-model:value="formData.email"
            placeholder="邮箱地址"
            class="auth-input"
            @blur="validateEmail"
          >
            <template #prefix>
              <MailOutlined class="input-icon" />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item :validate-status="passwordStatus" :help="passwordHelp">
          <a-input-password
            v-model:value="formData.password"
            placeholder="密码（至少6位）"
            class="auth-input"
            @blur="validatePassword"
          >
            <template #prefix>
              <LockOutlined class="input-icon" />
            </template>
          </a-input-password>
        </a-form-item>
        
        <a-form-item :validate-status="confirmStatus" :help="confirmHelp">
          <a-input-password
            v-model:value="formData.confirmPassword"
            placeholder="确认密码"
            class="auth-input"
            @blur="validateConfirmPassword"
          >
            <template #prefix>
              <LockOutlined class="input-icon" />
            </template>
          </a-input-password>
        </a-form-item>
        
        <a-form-item>
          <a-checkbox v-model:checked="agreeTerms">
            我已阅读并同意
            <a href="#" @click.prevent>服务条款</a>
            和
            <a href="#" @click.prevent>隐私政策</a>
          </a-checkbox>
        </a-form-item>
        
        <a-form-item>
          <a-button
            type="primary"
            block
            @click="handleRegister"
            :loading="loading"
            class="submit-btn"
          >
            注册
          </a-button>
        </a-form-item>
        
        <a-divider>
          <span class="divider-text">或</span>
        </a-divider>
        
        <a-form-item>
          <a-button
            block
            @click="handleLogin"
            class="login-btn"
          >
            <template #icon>
              <LoginOutlined />
            </template>
            返回登录
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { 
  UserAddOutlined, 
  UserOutlined, 
  MailOutlined, 
  LockOutlined,
  LoginOutlined
} from '@ant-design/icons-vue'
import { userApi } from '../api/user'

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(['update:visible', 'register-success', 'show-login'])

const loading = ref(false)
const agreeTerms = ref(false)
const usernameStatus = ref('')
const emailStatus = ref('')
const passwordStatus = ref('')
const confirmStatus = ref('')
const usernameHelp = ref('')
const emailHelp = ref('')
const passwordHelp = ref('')
const confirmHelp = ref('')

const formData = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateUsername = () => {
  if (!formData.username) {
    usernameStatus.value = 'error'
    usernameHelp.value = '请输入用户名'
    return false
  }
  if (formData.username.length < 3 || formData.username.length > 20) {
    usernameStatus.value = 'error'
    usernameHelp.value = '用户名长度应为3-20位'
    return false
  }
  if (!/^[a-zA-Z0-9_]+$/.test(formData.username)) {
    usernameStatus.value = 'error'
    usernameHelp.value = '用户名只能包含字母、数字和下划线'
    return false
  }
  usernameStatus.value = ''
  usernameHelp.value = ''
  return true
}

const validateEmail = () => {
  if (!formData.email) {
    emailStatus.value = 'error'
    emailHelp.value = '请输入邮箱地址'
    return false
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(formData.email)) {
    emailStatus.value = 'error'
    emailHelp.value = '请输入有效的邮箱地址'
    return false
  }
  emailStatus.value = ''
  emailHelp.value = ''
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

const validateConfirmPassword = () => {
  if (!formData.confirmPassword) {
    confirmStatus.value = 'error'
    confirmHelp.value = '请确认密码'
    return false
  }
  if (formData.password !== formData.confirmPassword) {
    confirmStatus.value = 'error'
    confirmHelp.value = '两次输入的密码不一致'
    return false
  }
  confirmStatus.value = ''
  confirmHelp.value = ''
  return true
}

const handleRegister = async () => {
  const isUsernameValid = validateUsername()
  const isEmailValid = validateEmail()
  const isPasswordValid = validatePassword()
  const isConfirmValid = validateConfirmPassword()
  
  if (!isUsernameValid || !isEmailValid || !isPasswordValid || !isConfirmValid) {
    return
  }
  
  if (!agreeTerms.value) {
    alert('请先同意服务条款和隐私政策')
    return
  }
  
  loading.value = true
  try {
    const result = await userApi.register(formData.username, formData.email, formData.password)
    emit('register-success', result)
    emit('update:visible', false)
    alert(`注册成功！欢迎, ${formData.username}`)
  } catch (error) {
    alert(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}

const handleLogin = () => {
  emit('update:visible', false)
  emit('show-login')
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
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.register-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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
  margin-bottom: 8px;
}

.auth-input {
  border-radius: 8px;
  height: 44px;
}

.input-icon {
  color: #8c8c8c;
}

.submit-btn {
  height: 44px;
  border-radius: 8px;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

.login-btn {
  height: 44px;
  border-radius: 8px;
  border: 1px solid #d9d9d9;
  color: #f5576c;
  font-size: 16px;
}

.login-btn:hover {
  border-color: #f5576c;
  background: rgba(245, 87, 108, 0.05);
}
</style>