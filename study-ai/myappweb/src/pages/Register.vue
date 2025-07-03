<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
        <el-form-item>
          <el-radio-group v-model="registerType" class="register-type">
            <el-radio label="email">邮箱注册</el-radio>
            <el-radio label="phone">手机注册</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="registerType === 'email'" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱">
            <template #prefix><IconMail class="input-icon" /></template>
          </el-input>
        </el-form-item>

        <el-form-item v-if="registerType === 'phone'" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号">
            <template #prefix><IconPhone class="input-icon" /></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input type="password" v-model="registerForm.confirmPassword" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha-group">
            <el-input v-model="registerForm.captcha" placeholder="请输入验证码"></el-input>
            <div class="captcha-image-container">
              <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-image" alt="验证码">
              <el-button type="text" @click="refreshCaptcha" class="refresh-button">刷新</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item class="register-button-group">
          <el-button type="primary" @click="handleRegister" class="register-button">注册</el-button>
          <el-button type="text" @click="goToLogin">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { IconMail, IconPhone, IconKey } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { register } from '../api/auth';
import API_BASE_URL from '../api/config';

const router = useRouter();
const registerType = ref('email');
const registerForm = ref({
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  captcha: ''
});

const captchaUrl = ref('');

const registerRules = ref({
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
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule: any, value: string, callback: any) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      }, trigger: 'blur'
    }
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

const handleRegister = async () => {
  try {
    const params = registerType.value === 'email' ? {
  email: registerForm.value.email,
  password: registerForm.value.password,
  captcha: registerForm.value.captcha
} : {
  phone: registerForm.value.phone,
  password: registerForm.value.password,
  captcha: registerForm.value.captcha
};

await register(
  registerType.value === 'email' ? registerForm.value.email : registerForm.value.phone,
  registerForm.value.password,
  registerForm.value.captcha
);
    ElMessage.success('注册成功，请登录');
    router.push('/login');
  } catch (error) {
    ElMessage.error('注册失败，请检查信息或验证码');
    refreshCaptcha();
  }
};

const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.register-card {
  width: 400px;
  padding: 20px;
}

.register-title {
  text-align: center;
  margin-bottom: 20px;
}

.register-type {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.input-icon {
  color: #c0c4cc;
}

.register-form {
  margin-top: 20px;
}

.register-button-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.register-button {
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