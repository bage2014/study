<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 via-blue-50 to-indigo-100 animate-gradient-flow">
    <div class="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md animate-scale-in">
      <!-- Logo区域 -->
      <div class="text-center mb-6">
        <div class="w-16 h-16 mx-auto bg-green-100 rounded-full flex items-center justify-center mb-4">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
        </div>
        <h1 class="text-2xl font-bold text-gray-800">家庭族谱</h1>
        <p class="text-gray-500 text-sm mt-1">创建账号，开启家族故事之旅</p>
      </div>

      <form @submit.prevent="register" class="space-y-5">
        <!-- 邮箱输入 -->
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 mb-1.5">邮箱</label>
          <input 
            type="email" 
            id="email" 
            v-model="form.email" 
            @input="validateEmail"
            :class="[
              'w-full px-4 py-2.5 border rounded-lg transition-all duration-200',
              emailError ? 'border-red-400 focus:ring-2 focus:ring-red-200' : 'border-gray-300 focus:ring-2 focus:ring-blue-200 focus:border-blue-400'
            ]"
            placeholder="请输入邮箱地址"
            autofocus
          />
          <p v-if="emailError" class="mt-1 text-sm text-red-500 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {{ emailError }}
          </p>
        </div>

        <!-- 密码输入 -->
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1.5">密码</label>
          <div class="relative">
            <input 
              :type="showPassword ? 'text' : 'password'" 
              id="password" 
              v-model="form.password" 
              @input="validatePassword"
              :class="[
                'w-full px-4 py-2.5 border rounded-lg transition-all duration-200 pr-10',
                passwordError ? 'border-red-400 focus:ring-2 focus:ring-red-200' : 'border-gray-300 focus:ring-2 focus:ring-blue-200 focus:border-blue-400'
              ]"
              placeholder="请输入密码"
            />
            <button 
              type="button" 
              @click="showPassword = !showPassword"
              class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
            >
              <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
              </svg>
            </button>
          </div>
          <!-- 密码强度提示 -->
          <div v-if="form.password && !passwordError" class="mt-1.5">
            <div class="flex items-center justify-between mb-1">
              <span class="text-xs text-gray-500">密码强度</span>
              <span :class="passwordStrengthColor" class="text-xs font-medium">{{ passwordStrengthText }}</span>
            </div>
            <div class="flex space-x-1">
              <div 
                v-for="i in 4" 
                :key="i"
                :class="[
                  'h-1.5 flex-1 rounded-full transition-all duration-300',
                  i <= passwordStrength ? passwordStrengthColorClass : 'bg-gray-200'
                ]"
              ></div>
            </div>
          </div>
          <p v-if="passwordError" class="mt-1 text-sm text-red-500 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {{ passwordError }}
          </p>
        </div>

        <!-- 确认密码 -->
        <div>
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1.5">确认密码</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="form.confirmPassword" 
            @input="validateConfirmPassword"
            :class="[
              'w-full px-4 py-2.5 border rounded-lg transition-all duration-200',
              confirmPasswordError ? 'border-red-400 focus:ring-2 focus:ring-red-200' : 'border-gray-300 focus:ring-2 focus:ring-blue-200 focus:border-blue-400'
            ]"
            placeholder="请再次输入密码"
          />
          <p v-if="confirmPasswordError" class="mt-1 text-sm text-red-500 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {{ confirmPasswordError }}
          </p>
          <p v-if="form.confirmPassword && !confirmPasswordError && form.password === form.confirmPassword" class="mt-1 text-sm text-green-500 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
            </svg>
            两次输入的密码一致
          </p>
        </div>

        <!-- 昵称输入 -->
        <div>
          <label for="nickname" class="block text-sm font-medium text-gray-700 mb-1.5">昵称</label>
          <input 
            type="text" 
            id="nickname" 
            v-model="form.nickname" 
            @input="validateNickname"
            :class="[
              'w-full px-4 py-2.5 border rounded-lg transition-all duration-200',
              nicknameError ? 'border-red-400 focus:ring-2 focus:ring-red-200' : 'border-gray-300 focus:ring-2 focus:ring-blue-200 focus:border-blue-400'
            ]"
            placeholder="请输入昵称"
          />
          <p v-if="nicknameError" class="mt-1 text-sm text-red-500 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {{ nicknameError }}
          </p>
          <p v-if="form.nickname && !nicknameError" class="mt-1 text-xs text-gray-500">
            昵称长度：{{ form.nickname.length }}/20
          </p>
        </div>

        <!-- 注册按钮 -->
        <button 
          type="submit" 
          :disabled="!isFormValid || loading"
          :class="[
            'w-full py-2.5 rounded-lg font-medium transition-all duration-200 btn-active',
            isFormValid && !loading 
              ? 'bg-green-500 hover:bg-green-600 text-white shadow-md hover:shadow-lg' 
              : 'bg-gray-300 text-gray-500 cursor-not-allowed'
          ]"
        >
          <span v-if="loading" class="flex items-center justify-center">
            <svg class="animate-spin -ml-1 mr-2 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            注册中...
          </span>
          <span v-else>注册</span>
        </button>

        <!-- 登录链接 -->
        <div class="text-center pt-2">
          <span class="text-gray-500">已有账号?</span>
          <router-link 
            to="/login" 
            class="text-blue-600 hover:text-blue-700 font-medium ml-1 transition-colors"
          >
            立即登录
          </router-link>
        </div>
      </form>

      <!-- 分割线 -->
      <div class="mt-6 relative">
        <div class="absolute inset-0 flex items-center">
          <div class="w-full border-t border-gray-200"></div>
        </div>
        <div class="relative flex justify-center text-sm">
          <span class="px-4 bg-white text-gray-500">其他注册方式</span>
        </div>
      </div>

      <!-- 社交登录（预留） -->
      <div class="mt-4 flex justify-center space-x-4">
        <button class="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors">
          <svg class="w-5 h-5 text-gray-600" viewBox="0 0 24 24">
            <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
            <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
            <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
            <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
          </svg>
        </button>
        <button class="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors">
          <svg class="w-5 h-5 text-gray-600" viewBox="0 0 24 24">
            <path d="M20.283 10.356h-8.327v3.451h4.792c-.446 2.193-2.313 3.453-4.792 3.453a5.27 5.27 0 01-5.279-5.28 5.27 5.27 0 015.279-5.279c1.259 0 2.397.447 3.29 1.178l2.6-2.599c-1.584-1.381-3.615-2.233-5.89-2.233a8.908 8.908 0 00-8.934 8.934 8.907 8.907 0 008.934 8.934c4.467 0 8.529-3.249 8.529-8.934 0-.528-.081-1.097-.202-1.625z"/>
          </svg>
        </button>
      </div>

      <!-- 服务条款 -->
      <p class="mt-4 text-xs text-gray-400 text-center">
        注册即表示您同意我们的
        <a href="#" class="text-blue-500 hover:text-blue-600">服务条款</a>
        和
        <a href="#" class="text-blue-500 hover:text-blue-600">隐私政策</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
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

const showPassword = ref(false);
const loading = ref(false);
const emailError = ref('');
const passwordError = ref('');
const confirmPasswordError = ref('');
const nicknameError = ref('');

// 验证邮箱
const validateEmail = () => {
  const email = form.value.email;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
  if (!email) {
    emailError.value = '';
  } else if (!emailRegex.test(email)) {
    emailError.value = '请输入有效的邮箱地址';
  } else {
    emailError.value = '';
  }
};

// 验证密码
const validatePassword = () => {
  const password = form.value.password;
  
  if (!password) {
    passwordError.value = '';
  } else if (password.length < 6) {
    passwordError.value = '密码至少需要6个字符';
  } else {
    passwordError.value = '';
  }
  // 同步验证确认密码
  validateConfirmPassword();
};

// 验证确认密码
const validateConfirmPassword = () => {
  const password = form.value.password;
  const confirmPassword = form.value.confirmPassword;
  
  if (!confirmPassword) {
    confirmPasswordError.value = '';
  } else if (password !== confirmPassword) {
    confirmPasswordError.value = '两次输入的密码不一致';
  } else {
    confirmPasswordError.value = '';
  }
};

// 验证昵称
const validateNickname = () => {
  const nickname = form.value.nickname;
  
  if (!nickname) {
    nicknameError.value = '';
  } else if (nickname.length < 2) {
    nicknameError.value = '昵称至少需要2个字符';
  } else if (nickname.length > 20) {
    nicknameError.value = '昵称最多20个字符';
  } else if (!/^[\u4e00-\u9fa5a-zA-Z0-9_]+$/.test(nickname)) {
    nicknameError.value = '昵称只能包含中文、英文、数字和下划线';
  } else {
    nicknameError.value = '';
  }
};

// 密码强度
const passwordStrength = computed(() => {
  const password = form.value.password;
  let strength = 0;
  
  if (password.length >= 6) strength++;
  if (password.length >= 8) strength++;
  if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
  if (/[0-9]/.test(password)) strength++;
  if (/[^a-zA-Z0-9]/.test(password)) strength++;
  
  return Math.min(strength, 4);
});

const passwordStrengthText = computed(() => {
  const texts = ['', '弱', '中', '强', '非常强'];
  return texts[passwordStrength.value];
});

const passwordStrengthColor = computed(() => {
  const colors = ['', 'text-red-500', 'text-yellow-500', 'text-green-500', 'text-green-600'];
  return colors[passwordStrength.value];
});

const passwordStrengthColorClass = computed(() => {
  const colors = ['', 'bg-red-500', 'bg-yellow-500', 'bg-green-500', 'bg-green-600'];
  return colors[passwordStrength.value];
});

// 表单是否有效
const isFormValid = computed(() => {
  return form.value.email && 
         form.value.password && 
         form.value.confirmPassword && 
         form.value.nickname &&
         !emailError.value && 
         !passwordError.value && 
         !confirmPasswordError.value && 
         !nicknameError.value &&
         form.value.password === form.value.confirmPassword;
});

// 注册
const register = async () => {
  if (!isFormValid.value) return;
  
  loading.value = true;
  
  try {
    await userStore.register({
      email: form.value.email,
      password: form.value.password,
      nickname: form.value.nickname
    });
    userStore.showToast('注册成功，正在登录...', 'success');
    router.push('/home');
  } catch (error) {
    userStore.showToast(userStore.error || '注册失败，请稍后重试', 'error');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.4s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.btn-active:active {
  transform: scale(0.98);
}
</style>