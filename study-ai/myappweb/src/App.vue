<script setup lang="ts">
// 原有导入保持不变
import { RouterLink, RouterView } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ref } from 'vue'
import { applyTheme } from './utils/theme'
import type { Theme } from './utils/theme'

const { t, locale } = useI18n()
const currentTheme = ref<Theme>(localStorage.getItem('theme') as Theme || 'light')
const openSubmenu = ref<string | null>(null)

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

// 切换子菜单显示状态
const toggleSubmenu = (menuKey: string) => {
  openSubmenu.value = openSubmenu.value === menuKey ? null : menuKey
}

// 点击页面其他地方关闭子菜单
const closeSubmenuOnClickOutside = () => {
  openSubmenu.value = null
}
</script>

<template>
  <div class="container" @click="closeSubmenuOnClickOutside">
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
          <!-- 首页菜单 -->
          <li ><RouterLink to="/home">{{ t('menu.home') }}</RouterLink></li>
          
          <!-- 应用管理二级菜单 -->
          <!-- <li  class="dropdown" @click.stop="toggleSubmenu('app')">
            <a href="#" class="dropdown-toggle">
              {{ t('menu.appManagement') }}
              <span class="dropdown-arrow">{{ openSubmenu === 'app' ? '▼' : '▶' }}</span>
            </a>
            <ul class="dropdown-menu" v-if="openSubmenu === 'app'">
              <li><RouterLink to="/app-list">{{ t('menu.appList') }}</RouterLink></li>
            </ul>
          </li> -->
          
          <!-- 数据可视化二级菜单 -->
          <!-- <li  class="dropdown" @click.stop="toggleSubmenu('data')">
            <a href="#" class="dropdown-toggle">
              {{ t('menu.dataVisualization') }}
              <span class="dropdown-arrow">{{ openSubmenu === 'data' ? '▼' : '▶' }}</span>
            </a>
            <ul class="dropdown-menu" v-if="openSubmenu === 'data'">
              <li><RouterLink to="/map-trajectory">{{ t('menu.mapTrajectory') }}</RouterLink></li>
            </ul>
          </li> -->
          
          <!-- 媒体中心二级菜单 -->
          <!-- <li  class="dropdown" @click.stop="toggleSubmenu('media')">
            <a href="#" class="dropdown-toggle">
              {{ t('menu.mediaCenter') }}
              <span class="dropdown-arrow">{{ openSubmenu === 'media' ? '▼' : '▶' }}</span>
            </a>
            <ul class="dropdown-menu" v-if="openSubmenu === 'media'">
              <li><RouterLink to="/tv-list">{{ t('menu.tvList') }}</RouterLink></li>
            </ul>
          </li> -->
          
          <!-- 消息页面菜单 -->
          <!-- <li><RouterLink to="/messages">{{ t('menu.messages') }}</RouterLink></li> -->
          
          <!-- 系统设置二级菜单 -->
          <li class="dropdown" @click.stop="toggleSubmenu('system')">
            <a href="#" class="dropdown-toggle">
              {{ t('menu.system') }}
              <span class="dropdown-arrow">{{ openSubmenu === 'system' ? '▼' : '▶' }}</span>
            </a>
            <ul class="dropdown-menu" v-if="openSubmenu === 'system'">
              <li><a href="#" @click.prevent="logout">{{ t('button.logout') }}</a></li>
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

<!-- 样式部分 -->
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
  padding: 0;
  margin: 0;
}

.menu li {
  margin-left: 1.5rem;
  position: relative;
}

.menu a {
  color: var(--text-color);
  font-weight: 500;
  padding: 0.5rem 0.75rem;
  border-radius: var(--radius);
  transition: var(--transition);
  display: inline-block;
  text-decoration: none;
}

.menu a:hover,
.menu a.router-link-active {
  color: var(--primary-color);
  background-color: var(--bg-light);
}

/* 二级菜单样式 */
.dropdown {
  position: relative;
}

.dropdown-toggle {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.dropdown-arrow {
  font-size: 0.75rem;
  transition: transform 0.2s ease;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: var(--bg-color);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 0.5rem 0;
  margin: 0.25rem 0 0;
  min-width: 150px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.dropdown-menu li {
  margin: 0;
  width: 100%;
}

.dropdown-menu a {
  display: block;
  padding: 0.5rem 1rem;
  width: 100%;
  text-align: left;
  box-sizing: border-box;
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
