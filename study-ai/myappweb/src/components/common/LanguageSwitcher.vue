<template>
  <select v-model="currentLocale" @change="changeLanguage" class="language-switcher">
    <option value="zh">中文</option>
    <option value="en">English</option>
  </select>
</template>

<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { locale } = useI18n()
const currentLocale = ref(locale.value)

function changeLanguage() {
  locale.value = currentLocale.value
  localStorage.setItem('locale', currentLocale.value)
}
</script>

<style scoped>
.language-switcher {
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  border-radius: 0.25rem;
  background-color: var(--surface-color);
  color: var(--text-color);
  cursor: pointer;
}
</style>