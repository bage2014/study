<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <AppHeader />
    <main class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/layout/AppHeader.vue'
</script>
