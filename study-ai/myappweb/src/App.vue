<script setup lang="ts">
// 原有导入保持不变
import { RouterLink, RouterView } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ref } from 'vue'
import { applyTheme, Theme } from './utils/theme'

const { t, locale } = useI18n()
const currentTheme = ref<Theme>(localStorage.getItem('theme') as Theme || 'light')

// 切换语言
const toggleLanguage = () => {
  locale.value = locale.value === 'zh' ? 'en' : 'zh'
  localStorage.setItem('locale', locale.value)
}

// 切换主题
const changeTheme = (theme: Theme) => {
  currentTheme.value = theme
  applyTheme(theme)
}

// 检查是否登录
const isLoggedIn = ref(!!localStorage.getItem('token'))

// 退出登录
const logout = () => {
  localStorage.removeItem('token')
  isLoggedIn.value = false
}
</script>

<template>
  <div class="container">
    <header class="header">
      <div class="logo">
        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="var(--primary-color)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 2L2 7l10 5 10-5-10-5z"></path>
          <path d="M2 17l10 5 10-5"></path>
          <path d="M2 12l10 5 10-5"></path>
        </svg>
        <h1>{{ t('title.appName') }}</h1>
      </div>
      <nav class="menu">
        <ul>
          <!-- 添加首页菜单项 -->
          <li v-if="isLoggedIn"><RouterLink to="/home">{{ t('menu.home') }}</RouterLink></li>
          <li v-if="isLoggedIn"><RouterLink to="/app-list">{{ t('menu.appList') }}</RouterLink></li>
          <li v-if="isLoggedIn"><RouterLink to="/map-trajectory">{{ t('menu.mapTrajectory') }}</RouterLink></li>
          <li v-if="isLoggedIn"><RouterLink to="/tv-list">{{ t('menu.tvList') }}</RouterLink></li>
          <li v-if="isLoggedIn"><a href="#" @click.prevent="logout">{{ t('button.logout') }}</a></li>
          <li><a href="#" @click.prevent="toggleLanguage">{{ t('button.changeLanguage') }}</a></li>
          <li>
            <select v-model="currentTheme" @change="changeTheme(currentTheme)">
              <option value="light">Light</option>
              <option value="dark">Dark</option>
              <option value="blue">Blue</option>
              <option value="green">Green</option>
            </select>
          </li>
        </ul>
      </nav>
    </header>

    <main class="content">
      <RouterView />
    </main>

    <footer class="footer">
      <p>&copy; {{ new Date().getFullYear() }} {{ t('title.appName') }} - All Rights Reserved</p>
    </footer>
  </div>
</template>

<!-- 样式部分保持不变 -->
<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background-color: var(--bg-color);
  box-shadow: var(--shadow);
}

.logo {
  display: flex;
  align-items: center;
}

.logo h1 {
  margin-left: 0.5rem;
  font-size: 1.5rem;
  color: var(--text-color);
}

.menu ul {
  display: flex;
  list-style: none;
}

.menu li {
  margin-left: 1.5rem;
}

.menu a {
  color: var(--text-color);
  font-weight: 500;
  padding: 0.5rem 0.75rem;
  border-radius: var(--radius);
  transition: var(--transition);
}

.menu a:hover,
.menu a.router-link-active {
  color: var(--primary-color);
  background-color: var(--bg-light);
}

.content {
  padding: 2rem 1rem;
  min-height: calc(100vh - 180px);
}

.footer {
  padding: 1.5rem;
  text-align: center;
  color: var(--text-light);
  background-color: var(--bg-light);
}

select {
  padding: 0.5rem;
  border-radius: var(--radius);
  border: 1px solid var(--border-color);
  background-color: var(--bg-color);
  color: var(--text-color);
  cursor: pointer;
}
</style>
