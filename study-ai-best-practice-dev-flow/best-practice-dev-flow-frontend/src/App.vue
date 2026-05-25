<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from './stores/user'
import Sidebar from './components/Sidebar.vue'

const router = useRouter()
const userStore = useUserStore()

router.beforeEach((to, _from, next) => {
  const requiresAuth = to.meta.requiresAuth !== false
  const isLoggedIn = userStore.isLoggedIn

  if (requiresAuth && !isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && isLoggedIn) {
    next('/family')
  } else {
    next()
  }
})

function shouldShowSidebar() {
  const publicPaths = ['/login', '/register']
  const currentPath = window.location.pathname
  return !publicPaths.includes(currentPath)
}
</script>

<template>
  <div class="app-container">
    <Sidebar v-if="shouldShowSidebar()" />
    <main class="main-content" :class="{ 'main-content-mobile': shouldShowSidebar() }">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  margin-left: 220px;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content-mobile {
  margin-left: 0;
}

@media screen and (max-width: 768px) {
  .main-content {
    margin-left: 0;
    min-height: 100vh;
  }
  
  .main-content-mobile {
    margin-left: 0;
  }
}

@media screen and (min-width: 769px) and (max-width: 1024px) {
  .main-content {
    margin-left: 200px;
  }
}
</style>