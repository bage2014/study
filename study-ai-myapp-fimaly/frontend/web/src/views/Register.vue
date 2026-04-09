<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
      <h1 class="text-2xl font-bold text-center text-gray-800 mb-6">注册</h1>
      <form @submit.prevent="register" class="space-y-4">
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
        <div>
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1">确认密码</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="form.confirmPassword" 
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
          <p v-if="form.password && form.confirmPassword && form.password !== form.confirmPassword" class="text-red-600 text-sm mt-1">
            两次输入的密码不一致
          </p>
        </div>
        <div>
          <label for="nickname" class="block text-sm font-medium text-gray-700 mb-1">昵称</label>
          <input 
            type="text" 
            id="nickname" 
            v-model="form.nickname" 
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
        <button type="submit" class="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50" :disabled="form.password && form.confirmPassword && form.password !== form.confirmPassword">
          注册
        </button>
        <div class="text-center mt-4">
          <span class="text-gray-600">已有账号?</span>
          <router-link to="/login" class="text-blue-600 hover:text-blue-500 ml-1">立即登录</router-link>
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
  password: '',
  confirmPassword: '',
  nickname: ''
});

const register = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    alert('两次输入的密码不一致');
    return;
  }
  
  try {
    await userStore.register({
      email: form.value.email,
      password: form.value.password,
      nickname: form.value.nickname
    });
    router.push('/login');
  } catch (error) {
    console.error('注册失败:', error);
  }
};
</script>

<style scoped>
@import "tailwindcss";

.primary-dark {
  @apply bg-blue-700;
}
</style>