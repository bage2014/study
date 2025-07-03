<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <div class="logo-container">
          <el-avatar shape="circle" size="large" class="logo-avatar">
            <User />
          </el-avatar>
          <h2 class="login-title">用户登录</h2>
        </div>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" size="default">
        <el-form-item>
          <el-radio-group v-model="loginType" class="login-type">
            <el-radio label="email">邮箱登录</el-radio>
            <el-radio label="phone">手机登录</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="loginType === 'email'" prop="email">
          <el-input v-model="loginForm.email" placeholder="请输入邮箱">
            <template #prefix><el-icon name="mail" class="input-icon" /></template>
          </el-input>
        </el-form-item>

        <el-form-item v-if="loginType === 'phone'" prop="phone">
          <el-input v-model="loginForm.phone" placeholder="请输入手机号">
            <template #prefix><el-icon name="phone" class="input-icon" /></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password>
            <template #prefix><Lock class="input-icon" /></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha-group">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码">
              <template #prefix><Key class="input-icon" /></template>
            </el-input>
            <div class="captcha-image-container">
              <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-image" alt="验证码">
              <el-button type="text" @click="refreshCaptcha" class="refresh-button">刷新</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item class="remember-me">
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          <el-button type="text" @click="handleForgotPassword" class="forgot-password">忘记密码？</el-button>
        </el-form-item>
        <el-form-item class="login-button-group">
          <el-button type="primary" @click="handleLogin" class="login-button" :loading="loginLoading">
            <template #loading><Loading /></template>
            登录
          </el-button>
        </el-form-item>
    
        <div class="register-link">
          <span>没有账号？</span>
          <el-button type="text" @click="goToRegister" class="register-button">立即注册</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElLoading } from 'element-plus';
import { login } from '../api/auth';
import API_BASE_URL from '../api/config';
import { User, Lock, Key, Loading } from '@element-plus/icons-vue';

const router = useRouter();
const loginType = ref('email');
const loginForm = ref({
  email: '',
  phone: '',
  password: '',
  captcha: '',
  rememberMe: false
});

const loginLoading = ref(false);

const captchaUrl = ref('');

const loginRules = ref({
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
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
  loginLoading.value = true;
  try {
    const params = loginType.value === 'email' ? {
  email: loginForm.value.email,
  password: loginForm.value.password,
  captcha: loginForm.value.captcha
} : {
  phone: loginForm.value.phone,
  password: loginForm.value.password,
  captcha: loginForm.value.captcha
};

const identifier = loginType.value === 'email' ? loginForm.value.email : loginForm.value.phone;
const response = await login(identifier, loginForm.value.password, loginForm.value.captcha);
    localStorage.setItem('token', response.token);
    if (loginForm.value.rememberMe) {
      localStorage.setItem('rememberedEmail', loginForm.value.email);
    } else {
      localStorage.removeItem('rememberedEmail');
    }
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error) {
    ElMessage.error('登录失败，请检查邮箱、密码或验证码');
    refreshCaptcha();
  } finally {
    loginLoading.value = false;
  }
};

const handleForgotPassword = () => {
  ElMessage({ message: '忘记密码功能即将上线，敬请期待！', type: 'info' });
};

const handleKeyup = (e: KeyboardEvent) => {
  if (e.key === 'Enter') {
    handleLogin();
  }
};

onMounted(() => {
  refreshCaptcha();
  const rememberedEmail = localStorage.getItem('rememberedEmail');
  if (rememberedEmail) {
    loginForm.value.email = rememberedEmail;
    loginForm.value.rememberMe = true;
  }
  window.addEventListener('keyup', handleKeyup);
});

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
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  transition: all 0.3s ease;
}

.login-card {
  width: 420px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.login-card:hover {
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

.login-header {
  margin-bottom: 25px;
}

.logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-avatar {
  background: linear-gradient(135deg, #409eff 0%, #69b1ff 100%);
  margin-bottom: 15px;
  transition: transform 0.3s ease;
}

.logo-avatar:hover {
  transform: scale(1.05);
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.login-type {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.login-form {
  margin-top: 15px;
}

.el-form-item {
  margin-bottom: 20px;
}

.input-icon {
  color: #c0c4cc;
  transition: color 0.2s;
}

.el-input:focus-within .input-icon {
  color: #409eff;
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.forgot-password {
  color: #409eff;
  padding: 0;
  height: auto;
}

.forgot-password:hover {
  color: #66b1ff;
}

.login-button-group {
  margin-bottom: 25px;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.other-login-methods {
  margin-bottom: 20px;
}

.divider-text {
  position: relative;
  text-align: center;
  margin-bottom: 15px;
  color: #909399;
  font-size: 14px;
}

.divider-text::before,
.divider-text::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 35%;
  height: 1px;
  background-color: #e4e7ed;
}

.divider-text::before {
  left: 0;
}

.divider-text::after {
  right: 0;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.social-buttons .el-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.social-buttons .el-button:hover {
  transform: translateY(-3px);
}

.register-link {
  text-align: center;
  color: #606266;
  font-size: 14px;
}

.register-button {
  color: #409eff;
  padding: 0;
  height: auto;
  font-size: 14px;
}

.register-button:hover {
  color: #66b1ff;
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
  border-radius: 4px;
  transition: all 0.2s;
}

.captcha-image:hover {
  opacity: 0.9;
}

.refresh-button {
  padding: 0;
  font-size: 12px;
  color: #409eff;
}

/* 动画效果 */
.el-input {
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.el-input:focus-within {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.el-button--primary {
  background: linear-gradient(135deg, #409eff 0%, #69b1ff 100%);
  border: none;
}

.el-button--primary:hover {
  background: linear-gradient(135deg, #3a8ee6 0%, #5da8f5 100%);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .login-card {
    width: 90%;
    padding: 20px;
  }

  .social-buttons {
    gap: 10px;
  }
}
</style>