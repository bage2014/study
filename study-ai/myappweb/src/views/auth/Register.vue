<template>
  <div class="auth-container">
    <div class="auth-card card">
      <h2>{{ $t('common.register') }}</h2>
      <form @submit.prevent="handleRegister">
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
        <div class="form-group">
          <label for="confirmPassword">{{ $t('auth.confirmPassword') }}</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="formData.confirmPassword"
            required
          />
        </div>
        <button type="submit" class="btn" :disabled="loading">
          {{ loading ? 'Registering...' : $t('common.register') }}
        </button>
      </form>
      <div class="auth-links">
        <router-link to="/login">{{ $t('common.login') }}</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const router = useRouter()

const formData = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const loading = ref(false)

async function handleRegister() {
  if (formData.value.password !== formData.value.confirmPassword) {
    alert('Passwords do not match')
    return
  }
  
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    alert('Registration successful! Please login.')
    router.push('/login')
  } catch (error) {
    alert('Registration failed')
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
</style>