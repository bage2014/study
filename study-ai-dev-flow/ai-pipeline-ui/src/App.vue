<template>
  <div class="min-h-screen bg-gray-50">
    <nav class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white">
      <div class="container mx-auto px-4 py-3">
        <div class="flex justify-between items-center">
          <router-link to="/" class="text-xl font-bold">AI Pipeline Platform</router-link>
          <div class="space-x-4">
            <router-link to="/" class="hover:text-gray-200">录入需求</router-link>
            <router-link to="/pipelines" class="hover:text-gray-200">流水线列表</router-link>
          </div>
        </div>
      </div>
    </nav>
    <main class="container mx-auto px-4 py-6">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
</script>
