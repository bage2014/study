<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <h2 class="login-title">用户登录</h2>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" size="default">
        <!-- 账号输入框 -->
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入账号">
            <template #prefix><el-icon><User class="input-icon" /></el-icon></template>
          </el-input>
        </el-form-item>

        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password>
            <template #prefix><el-icon><Lock class="input-icon" /></el-icon></template>
          </el-input>
        </el-form-item>

        <!-- 验证码输入框 -->
        <el-form-item prop="captcha">
          <div class="captcha-group">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码">
              <template #prefix><el-icon><Key class="input-icon" /></el-icon></template>
            </el-input>
            <div class="captcha-image-container">
              <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-image" alt="验证码">
              <el-button type="text" @click="refreshCaptcha" class="refresh-button">刷新</el-button>
            </div>
          </div>
        </el-form-item>

        <el-form-item class="login-button-group">
          <el-button type="primary" @click="handleLogin" class="login-button" :loading="loginLoading">
            <template #loading><Loading /></template>
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '../api/auth';
import API_BASE_URL from '../api/config';
import { User, Lock, Key, Loading } from '@element-plus/icons-vue';

const router = useRouter();
const loginForm = ref({
  username: '',
  password: '',
  captcha: ''
});

const loginLoading = ref(false);
const captchaUrl = ref('');

// 表单验证规则
const loginRules = ref({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
});

// 刷新验证码
const refreshCaptcha = () => {
  captchaUrl.value = `${API_BASE_URL}captcha?timestamp=${Date.now()}`;
};

// 登录处理
const handleLogin = async () => {
  loginLoading.value = true;
  try {
    const response = await login(
      loginForm.value.username,
      loginForm.value.password,
      loginForm.value.captcha
    );
    localStorage.setItem('token', response.token);
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error) {
    ElMessage.error('登录失败，请检查账号、密码或验证码');
    refreshCaptcha();
  } finally {
    loginLoading.value = false;
  }
};

// 页面挂载时初始化
onMounted(() => {
  refreshCaptcha();
});
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-card {
  width: 420px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.login-header {
  margin-bottom: 25px;
  text-align: center;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.captcha-group {
  display: flex;
  gap: 10px;
}

.captcha-image-container {
  display: flex;
  align-items: center;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}

.login-button-group {
  margin-top: 15px;
}

.login-button {
  width: 100%;
}
</style>