<template>
  <div class="auth-container">
    <div class="auth-card card">
      <h2>{{ $t('common.login') }}</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">{{ $t('auth.username') }}</label>
          <input
            type="text"
            id="username"
            v-model="formData.username"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">{{ $t('auth.password') }}</label>
          <input
            type="password"
            id="password"
            v-model="formData.password"
            required
          />
        </div>
        <div class="form-group" v-if="showCaptcha">
          <label for="captcha">{{ $t('auth.captcha') }}</label>
          <div class="captcha-container">
            <input
              type="text"
              id="captcha"
              v-model="formData.captcha"
              required
              maxlength="4"
            />
            <img 
              :src="captchaImageUrl" 
              class="captcha-image" 
              @click="refreshCaptcha"
              alt="验证码"
            />
          </div>
        </div>
        <button type="submit" class="btn" :disabled="loading">
          {{ loading ? 'Logging in...' : $t('common.login') }}
        </button>
      </form>
      <div class="auth-links">
        <router-link to="/register">{{ $t('common.register') }}</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import request from '@/utils/request'
import { getFullUrl } from '@/utils/request'

const { t } = useI18n()
const router = useRouter()
const authStore = useAuthStore()

const formData = ref({
  username: 'zhangsan@qq.com',
  password: 'zhangsan123',
  captcha: '',
  requestId: ''
})

const loading = ref(false)
const captchaTimestamp = ref(0) // 用于刷新验证码图片的时间戳
const showCaptcha = ref(false) // 控制验证码显示

// 生成简单的UUID
function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

// 计算验证码图片URL
const captchaImageUrl = computed(() => {
  var url =  `/captcha?requestId=${formData.value.requestId}&t=${captchaTimestamp.value}`
  return getFullUrl(url);
})

// 刷新验证码
function refreshCaptcha() {
  // 生成新的requestId
  formData.value.requestId = generateUUID()
  // 更新时间戳以强制刷新图片
  captchaTimestamp.value = Date.now()
}

// 组件挂载时生成请求ID
onMounted(() => {
  formData.value.requestId = generateUUID()
})

async function handleLogin() {
  loading.value = true
  try {
    // 发送登录请求
    const response = await request.post('/login', {
      username: formData.value.username,
      password: formData.value.password,
      captcha: formData.value.captcha.toUpperCase(),
      requestId: formData.value.requestId
    })
    
    if (response.code === 200) {
      // 更新auth store
      authStore.login(response.data.user, response.data.userToken)
      
      alert(t('auth.loginSuccess'))
      router.push('/home')
    } else {
      alert(response.message || t('auth.loginFailed'))
      // 检查是否需要显示验证码
      if (response.data?.user?.loginAttempts > 0) {
        showCaptcha.value = true
      }
      refreshCaptcha()
    }
  } catch (error) {
    console.error('Login error:', error)
    alert(t('auth.loginFailed'))
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--background-color);
}

.auth-card {
  width: 100%;
  max-width: 400px;
}

.auth-card h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: var(--primary-color);
}

.auth-links {
  text-align: center;
  margin-top: 1rem;
}

.auth-links a {
  color: var(--primary-color);
  text-decoration: none;
}

.auth-links a:hover {
  text-decoration: underline;
}

.captcha-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-image {
  height: 38px;
  border-radius: 4px;
  cursor: pointer;
  border: 1px solid #ddd;
  background-color: #f9f9f9;
}

.captcha-image:hover {
  border-color: #999;
}
</style>