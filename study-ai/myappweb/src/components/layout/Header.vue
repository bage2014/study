<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <div class="logo" @click="goToHome">
          <h1>{{ $t('common.appName') }}</h1>
        </div>
        <div class="header-actions">
          <ThemeSwitcher />
          <LanguageSwitcher />
          <button v-if="isAuthenticated" @click="logout" class="btn btn-secondary">
            {{ $t('common.logout') }}
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import ThemeSwitcher from '@/components/common/ThemeSwitcher.vue'
import LanguageSwitcher from '@/components/common/LanguageSwitcher.vue'

const router = useRouter()
const authStore = useAuthStore()
const { isAuthenticated } = storeToRefs(authStore)
const { logout } = authStore

const goToHome = () => {
  router.push('/home')
}
</script>

<style scoped>
.header {
  background-color: var(--surface-color);
  border-bottom: 1px solid var(--border-color);
  padding: 1rem 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo h1 {
  color: var(--primary-color);
  font-size: 1.5rem;
}

.logo {
  cursor: pointer;
}
</style>