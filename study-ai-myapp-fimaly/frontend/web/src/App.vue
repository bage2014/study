<template>
  <div class="app">
    <router-view v-if="isAuthenticated" />
    <router-view v-else name="auth" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useUserStore } from './stores/user';

const userStore = useUserStore();
const isAuthenticated = computed(() => userStore.isAuthenticated);
</script>

<style>
@reference tailwind;

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>