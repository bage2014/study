<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
      <h1 class="text-2xl font-bold text-center text-gray-800 mb-6">登录</h1>
      <!-- 错误提示 -->
      <div v-if="error" class="bg-red-50 border border-red-200 text-red-600 p-3 rounded-md mb-4">
        {{ error }}
      </div>
      <form @submit.prevent="login" class="space-y-4">
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
          <input 
            type="email" 
            id="email" 
            v-model="form.email" 
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="form.password" 
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input id="remember-me" name="remember-me" type="checkbox" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded">
            <label for="remember-me" class="ml-2 block text-sm text-gray-700">记住我</label>
          </div>
          <a href="#" class="text-sm text-blue-600 hover:text-blue-500">忘记密码?</a>
        </div>
        <button type="submit" class="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
          登录
        </button>
        <div class="text-center mt-4">
          <span class="text-gray-600">还没有账号?</span>
          <router-link to="/register" class="text-blue-600 hover:text-blue-500 ml-1">立即注册</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';

const router = useRouter();
const userStore = useUserStore();

const form = ref({
  email: '',
  password: ''
});

const error = ref('');

const login = async () => {
  error.value = '';
  try {
    await userStore.login(form.value);
    router.push('/');
  } catch (err) {
    // 从错误对象中提取后端返回的message
    if (err.response && err.response.data && err.response.data.message) {
      error.value = err.response.data.message;
    } else if (err.message) {
      error.value = err.message;
    } else {
      error.value = '登录失败，请稍后重试';
    }
    console.error('登录失败:', err);
  }
};
</script>

<style scoped>
@import "tailwindcss";

.primary-dark {
  @apply bg-blue-700;
}
</style>