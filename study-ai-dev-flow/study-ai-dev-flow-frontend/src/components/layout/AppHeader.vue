<template>
  <nav class="bg-white shadow-sm border-b border-gray-200">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16 items-center">
        <div class="flex items-center">
          <router-link to="/" class="text-xl font-bold text-gray-900">
            Study AI Dev Flow
          </router-link>
        </div>
        <div class="flex space-x-4">
          <router-link
            to="/"
            class="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            active-class="text-gray-900 bg-gray-100"
          >
            首页
          </router-link>
          <router-link
            to="/samples"
            class="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            active-class="text-gray-900 bg-gray-100"
          >
            示例
          </router-link>
          <router-link
            to="/users"
            class="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            active-class="text-gray-900 bg-gray-100"
          >
            用户
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>
