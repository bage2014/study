<template>
  <div id="app">
    <Header v-if="showHeader" />
    <div class="app-content">
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from '@/components/layout/Header.vue'
import { useThemeStore } from '@/stores/theme'

const route = useRoute()
const themeStore = useThemeStore()

const showHeader = computed(() => !['/login', '/register'].includes(route.path))

themeStore.setTheme(themeStore.currentTheme)
</script>

<style>
.app-content {
  display: flex;
  flex: 1;
}

.main-content {
  flex: 1;
  padding: 2rem;
  background-color: var(--background-color);
}
</style>
