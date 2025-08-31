<template>
  <div class="profile-container">
    <div class="container">
      <div class="profile-header">
        <h1>{{ $t('common.profile') }}</h1>
      </div>
      
      <div class="profile-content">
        <div class="card">
          <h3>User Information</h3>
          <div class="profile-info">
            <div class="info-item">
              <label>Username:</label>
              <span>{{ user.username }}</span>
            </div>
            <div class="info-item">
              <label>User ID:</label>
              <span>{{ user.id }}</span>
            </div>
            <div class="info-item">
              <label>Permissions:</label>
              <div class="permissions">
                <span v-for="permission in user.permissions" :key="permission" class="permission-tag">
                  {{ permission }}
                </span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="card">
          <h3>Settings</h3>
          <div class="settings-section">
            <div class="setting-item">
              <label>Theme:</label>
              <ThemeSwitcher />
            </div>
            <div class="setting-item">
              <label>Language:</label>
              <LanguageSwitcher />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import ThemeSwitcher from '@/components/common/ThemeSwitcher.vue'
import LanguageSwitcher from '@/components/common/LanguageSwitcher.vue'

const authStore = useAuthStore()
const { user } = storeToRefs(authStore)
</script>

<style scoped>
.profile-container {
  flex: 1;
  padding: 2rem 0;
}

.profile-header {
  margin-bottom: 2rem;
}

.profile-header h1 {
  color: var(--primary-color);
}

.profile-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.profile-info {
  margin-top: 1rem;
}

.info-item {
  margin-bottom: 1rem;
}

.info-item label {
  font-weight: bold;
  display: block;
  margin-bottom: 0.25rem;
}

.permissions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.permission-tag {
  background-color: var(--primary-color);
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
}

.settings-section {
  margin-top: 1rem;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding: 0.75rem;
  background-color: var(--background-color);
  border-radius: 0.25rem;
}

.setting-item label {
  font-weight: bold;
}
</style>