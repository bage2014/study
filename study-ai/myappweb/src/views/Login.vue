<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

const handleLogin = () => {
  if (!username.value || !password.value) {
    error.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  error.value = ''

  // 模拟登录API调用
  setTimeout(() => {
    // 这里应该是实际的登录逻辑
    localStorage.setItem('token', 'fake-token')
    loading.value = false

    // 重定向到之前想要访问的页面，或者首页
    const redirect = router.currentRoute.value.query.redirect || '/'
    router.push(redirect as string)
  }, 1000)
}
</script>

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
          >
        </div>
        <button type="submit" :disabled="loading">
          <span v-if="loading">登录中...</span>
          <span v-else>{{ t('button.login') }}</span>
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
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
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background-color: var(--bg-color);
  color: var(--text-color);
}

.error-message {
  margin-bottom: 1rem;
  padding: 0.75rem;
  background-color: rgba(255, 99, 71, 0.1);
  color: #ff6347;
  border-radius: var(--radius);
}

button {
  width: 100%;
  padding: 0.75rem;
  font-weight: 500;
}
</style>