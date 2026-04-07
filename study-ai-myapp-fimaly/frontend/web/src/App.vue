<template>
  <div class="min-h-screen bg-gray-50">
    <router-view />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useUserStore } from './stores/user';

const userStore = useUserStore();
const isAuthenticated = computed(() => userStore.isAuthenticated);

onMounted(async () => {
  if (isAuthenticated.value && !userStore.user) {
    await userStore.fetchCurrentUser();
  }
});
</script>

<style>
@import "tailwindcss";

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>