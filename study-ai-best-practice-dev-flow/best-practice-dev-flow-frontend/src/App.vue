<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useUserStore } from './stores/user'

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
</script>

<template>
  <router-view />
</template>