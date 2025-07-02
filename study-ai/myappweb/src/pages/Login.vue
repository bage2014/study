<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">用户登录</h2>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="email">
          <el-input v-model="loginForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha-group">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码"></el-input>
            <div class="captcha-image-container">
              <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-image" alt="验证码">
              <el-button type="text" @click="refreshCaptcha" class="refresh-button">刷新</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item class="login-button-group">
          <el-button type="primary" @click="handleLogin" class="login-button">登录</el-button>
          <el-button type="text" @click="goToRegister">没有账号？去注册</el-button>
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

const router = useRouter();
const loginForm = ref({
  email: '',
  password: '',
  captcha: ''
});

const captchaUrl = ref('');

const loginRules = ref({
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 4, max: 4, message: '验证码长度为4位', trigger: 'blur' }
  ]
});

const refreshCaptcha = () => {
  captchaUrl.value = `${API_BASE_URL}captcha?timestamp=${Date.now()}`;
};

onMounted(() => {
  refreshCaptcha();
});

const handleLogin = async () => {
  try {
    const response = await login(
      loginForm.value.email,
      loginForm.value.password,
      loginForm.value.captcha
    );
    localStorage.setItem('token', response.token);
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error) {
    ElMessage.error('登录失败，请检查邮箱、密码或验证码');
    refreshCaptcha();
  }
};

const goToRegister = () => {
  router.push('/register');
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
}

.login-form {
  margin-top: 20px;
}

.login-button-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.login-button {
  width: 60%;
}

.captcha-group {
  display: flex;
  gap: 10px;
}

.captcha-image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  margin-bottom: 5px;
}

.refresh-button {
  padding: 0;
  font-size: 12px;
}
</style>