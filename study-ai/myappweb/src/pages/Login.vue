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
        <el-form-item class="login-button-group">
          <el-button type="primary" @click="handleLogin" class="login-button">登录</el-button>
          <el-button type="text" @click="goToRegister">没有账号？去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '../api/auth';

const router = useRouter();
const loginForm = ref({
  email: '',
  password: ''
});

const loginRules = ref({
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
});

const handleLogin = async () => {
  try {
    const response = await login(loginForm.value.email, loginForm.value.password);
    // 假设登录成功后返回token
    localStorage.setItem('token', response.token);
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error) {
    ElMessage.error('登录失败，请检查邮箱和密码');
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
</style>