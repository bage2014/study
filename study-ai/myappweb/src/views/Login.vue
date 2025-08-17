<script setup lang="ts">
// 原有导入保持不变
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import http from '../components/http'
import { AuthStorage } from '../utils/authStorage'

const { t } = useI18n()
const router = useRouter()
const username = ref('zhangsan@qq.com')
const password = ref('zhangsan123')
const captcha = ref('')
const captchaImage = ref('')
const loading = ref(false)
const error = ref('')
const captchaLoading = ref(false)

// 生成请求ID
const generateRequestId = () => {
  return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
}

// 获取验证码
const fetchCaptcha = async () => {
  try {
    captchaLoading.value = true
    const requestId = generateRequestId()
    const imageUrl = http.rebuildURL('/captcha');
    captchaImage.value = `${imageUrl}?requestId=${requestId}`;
    captcha.value = '' // 清空验证码输入
  } catch (err) {
    console.error('获取验证码失败:', err)
    ElMessage.error('获取验证码失败，请稍后重试')
  } finally {
    captchaLoading.value = false
  }
}

// 处理登录
const handleLogin = async () => {
  // 表单验证部分保持不变
  if (!username.value.trim()) {
    error.value = '请输入用户名'
    return
  }

  if (!password.value) {
    error.value = '请输入密码'
    return
  }

  if (!captcha.value.trim()) {
    error.value = '请输入验证码'
    return
  }

  loading.value = true
  error.value = ''

  try {
    // 实际调用登录API
    const response = await http.post('/login', {
      username: username.value.trim(),
      password: password.value,
      captcha: captcha.value.trim()
    })

    // 登录成功处理
    if (response && response.code === 200) {
      // 保存用户信息和认证信息
      if (response.data && response.data.userToken) {
        // 使用统一的存储方法
        AuthStorage.saveLoginResponse(response.data)
        
        ElMessage.success(response.message || '登录成功')
        
        // 重定向到首页
        router.push('/home')
      } else {
        throw new Error('登录响应数据不完整')
      }
    } else {
      throw new Error(response?.message || '登录失败')
    }
  } catch (err) {
    console.error('登录失败:', err)
    error.value = err instanceof Error ? err.message : '登录失败，请检查您的网络连接或稍后重试'
    ElMessage.error(error.value)
    
    // 登录失败后刷新验证码
    await fetchCaptcha()
  } finally {
    loading.value = false
  }
}

// 页面加载时获取验证码
onMounted(() => {
  fetchCaptcha()
})
</script>

<!-- 模板和样式部分保持不变 -->
<template>
  <div class="login-page">
    <div class="login-container card">
      <h2>{{ t('button.login') }}</h2>
      <div v-if="error" class="error-message">{{ error }}</div>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="username"
            placeholder="请输入用户名"
            :disabled="loading"
            autocomplete="username"
          >
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="password"
            placeholder="请输入密码"
            :disabled="loading"
            autocomplete="current-password"
          >
        </div>
        <div class="form-group">
          <label for="captcha">验证码</label>
          <div class="captcha-container">
            <input
              type="text"
              id="captcha"
              v-model="captcha"
              placeholder="请输入验证码"
              :disabled="loading || captchaLoading"
              class="captcha-input"
              maxlength="6"
              autocomplete="one-time-code"
            >
            <div class="captcha-image-container">
              <img
                :src="captchaImage"
                alt="验证码"
                v-if="captchaImage"
                @click="fetchCaptcha"
                :class="{ 'captcha-loading': captchaLoading }"
              >
              <div v-else class="captcha-placeholder" @click="fetchCaptcha">
                <span v-if="captchaLoading">加载中...</span>
                <span v-else>点击获取验证码</span>
              </div>
            </div>
          </div>
        </div>
        <el-button
          type="primary"
          :loading="loading"
          native-type="submit"
          style="width: 100%"
        >
          {{ t('button.login') }}
        </el-button>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* 样式部分保持不变 */
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--bg-light);
  padding: 2rem;
}

.login-container {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: var(--bg-color);
}

h2 {
  margin-bottom: 1.5rem;
  text-align: center;
  color: var(--text-color);
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--text-color);
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  background-color: var(--bg-color);
  color: var(--text-color);
  font-size: 14px;
  transition: border-color 0.3s;
}

input:focus {
  outline: none;
  border-color: var(--primary-color);
}

.error-message {
  margin-bottom: 1rem;
  padding: 0.75rem;
  background-color: rgba(255, 99, 71, 0.1);
  color: #ff6347;
  border-radius: 4px;
}

/* 验证码样式 */
.captcha-container {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.captcha-input {
  flex: 1;
}

.captcha-image-container {
  flex-shrink: 0;
  width: 120px;
  height: 40px;
  border-radius: 4px;
  overflow: hidden;
}

.captcha-image-container img {
  width: 100%;
  height: 100%;
  cursor: pointer;
  object-fit: cover;
  border-radius: 4px;
}

.captcha-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--bg-light);
  color: var(--text-light);
  font-size: 12px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid var(--border-color);
}

.captcha-loading {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>