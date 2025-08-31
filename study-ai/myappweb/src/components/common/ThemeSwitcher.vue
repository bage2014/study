<template>
  <button @click="toggleTheme" class="theme-switcher">
    {{ currentTheme === 'light' ? '🌙' : '☀️' }}
  </button>
</template>

<script setup>
import { useThemeStore } from '@/stores/theme'
import { storeToRefs } from 'pinia'

const themeStore = useThemeStore()
const { currentTheme } = storeToRefs(themeStore)
const { toggleTheme } = themeStore
</script>

<style scoped>
.theme-switcher {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.5rem;
  transition: background-color 0.3s;
}

.theme-switcher:hover {
  background-color: var(--surface-color);
}
</style>