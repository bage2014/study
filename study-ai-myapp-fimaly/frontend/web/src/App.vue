<template>
  <div class="min-h-screen bg-gray-50">
    <router-view />
    <Toast 
      :visible="toastVisible" 
      :message="toastMessage" 
      :type="toastType" 
    />
    <Loading 
      :visible="loadingVisible" 
      :text="loadingText" 
    />
  </div>
</template>

<script setup>import { computed, onMounted, provide } from 'vue';
import { useUserStore } from './stores/user';
import Toast from './components/Toast.vue';
import Loading from './components/Loading.vue';
const userStore = useUserStore();
const isAuthenticated = computed(() => userStore.isAuthenticated);
// Toast状态
const toastVisible = computed(() => userStore.toastVisible);
const toastMessage = computed(() => userStore.toastMessage);
const toastType = computed(() => userStore.toastType);
// Loading状态
const loadingVisible = computed(() => userStore.loadingVisible);
const loadingText = computed(() => userStore.loadingText);
// 提供全局方法
provide('showToast', (message, type = 'info') => {
 userStore.showToast(message, type);
});
provide('hideToast', () => {
 userStore.hideToast();
});
provide('showLoading', (text = '加载中...') => {
 userStore.showLoading(text);
});
provide('hideLoading', () => {
 userStore.hideLoading();
});
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

/* 全局动画类 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.animate-fade-in {
  animation: fadeIn 0.3s ease-out;
}

.animate-slide-in {
  animation: slideIn 0.3s ease-out;
}

.animate-pulse-slow {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

/* 按钮点击效果 */
.btn-active:active {
  transform: scale(0.98);
}

/* 卡片悬停效果 */
.card-hover {
  transition: all 0.3s ease;
}

.card-hover:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

/* 输入框聚焦效果 */
input:focus,
textarea:focus,
select:focus {
  outline: none;
  ring: 2px;
  ring-color: #3b82f6;
}
</style>