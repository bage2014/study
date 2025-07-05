<template>
  <div class="register-container">
    <el-card class="register-card">
      <div class="register-header">
        <h2 class="register-title">用户注册</h2>
      </div>

      <el-tabs v-model="activeTab" type="card" class="register-tabs" @tab-click="handleTabClick">
        <!-- 第一步：邮箱验证 -->
        <el-tab-pane label="邮箱验证" name="emailVerify">
          <el-form ref="emailForm" :model="emailForm" :rules="emailRules" class="register-form" size="default">
            <!-- 邮箱输入框 -->
            <el-form-item prop="email">
              <el-input v-model="emailForm.email" placeholder="请输入邮箱">
                <template #prefix><el-icon><Mail class="input-icon" /></el-icon></template>
              </el-input>
            </el-form-item>

            <!-- 验证码输入框 -->
            <el-form-item prop="captcha">
              <div class="captcha-group">
                <el-input v-model="emailForm.captcha" placeholder="请输入验证码">
                  <template #prefix><el-icon><Key class="input-icon" /></el-icon></template>
                </el-input>
                <div class="captcha-image-container">
                  <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-image" alt="验证码">
                  <el-button type="text" @click="refreshCaptcha" class="refresh-button">刷新</el-button>
                </div>
              </div>
            </el-form-item>

            <el-form-item class="button-group">
              <el-button type="primary" @click="sendVerificationEmail" :loading="sendingEmail" class="send-button">
                发送邮件
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 第二步：账号注册 -->
        <el-tab-pane label="账号注册" name="accountRegister" :disabled="!emailVerified">
          <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form" size="default">
            <!-- 邮箱显示 -->
            <el-form-item>
              <el-input v-model="registerForm.email" placeholder="邮箱" disabled>
                <template #prefix><el-icon><Mail class="input-icon" /></el-icon></template>
              </el-input>
            </el-form-item>

            <!-- 邮箱验证码 -->
            <el-form-item prop="emailCode">
              <el-input v-model="registerForm.emailCode" placeholder="请输入邮箱验证码">
                <template #prefix><el-icon><Message class="input-icon" /></el-icon></template>
              </el-input>
            </el-form-item>

            <!-- 密码输入 -->
            <el-form-item prop="password">
              <el-input type="password" v-model="registerForm.password" placeholder="请输入密码" show-password>
                <template #prefix><el-icon><Lock class="input-icon" /></el-icon></template>
              </el-input>
            </el-form-item>

            <!-- 确认密码 -->
            <el-form-item prop="confirmPassword">
              <el-input type="password" v-model="registerForm.confirmPassword" placeholder="请确认密码" show-password>
                <template #prefix><el-icon><Lock class="input-icon" /></el-icon></template>
              </el-input>
            </el-form-item>

            <!-- 新验证码 -->
            <el-form-item prop="newCaptcha">
              <div class="captcha-group">
                <el-input v-model="registerForm.newCaptcha" placeholder="请输入验证码">
                  <template #prefix><el-icon><Key class="input-icon" /></el-icon></template>
                </el-input>
                <div class="captcha-image-container">
                  <img :src="registerCaptchaUrl" @click="refreshRegisterCaptcha" class="captcha-image" alt="验证码">
                  <el-button type="text" @click="refreshRegisterCaptcha" class="refresh-button">刷新</el-button>
                </div>
              </div>
            </el-form-item>

            <el-form-item class="button-group">
              <el-button type="primary" @click="handleRegister" :loading="registerLoading" class="register-button">
                注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div class="login-link">
        <span>已有账号？</span>
        <el-button type="text" @click="goToLogin" class="login-button">立即登录</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElForm } from 'element-plus';
import { register, sendVerificationCode } from '../api/auth';
import API_BASE_URL from '../api/config';
import { User, Key, Lock, Message } from '@element-plus/icons-vue';

const router = useRouter();

// 选项卡状态
const activeTab = ref('emailVerify');
const emailVerified = ref(false);

// 邮箱验证表单数据
const emailForm = reactive({
  email: '',
  captcha: ''
});

// 注册表单数据
const registerForm = reactive({
  email: '',
  emailCode: '',
  password: '',
  confirmPassword: '',
  newCaptcha: ''
});

// 加载状态
const sendingEmail = ref(false);
const registerLoading = ref(false);

// 验证码图片URL
const captchaUrl = ref('');
const registerCaptchaUrl = ref('');

// 邮箱验证表单规则
const emailRules = ref({
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
});

// 注册表单规则
const registerRules = ref({
  emailCode: [
    { required: true, message: '请输入邮箱验证码', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  newCaptcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
});

// 刷新验证码
const refreshCaptcha = () => {
  captchaUrl.value = `${API_BASE_URL}captcha?timestamp=${Date.now()}`;
};

// 刷新注册验证码
const refreshRegisterCaptcha = () => {
  registerCaptchaUrl.value = `${API_BASE_URL}captcha?timestamp=${Date.now()}`;
};

// 发送验证邮件
const sendVerificationEmail = async () => {
  const emailFormRef = ref<InstanceType<typeof ElForm>>();
  emailFormRef.value?.validate(async (valid) => {
    if (valid) {
      sendingEmail.value = true;
      try {
        await sendVerificationCode(emailForm.email, emailForm.captcha);
        ElMessage.success('验证码已发送至您的邮箱，请查收');
        // 切换到注册选项卡并携带邮箱信息
        registerForm.email = emailForm.email;
        emailVerified.value = true;
        activeTab.value = 'accountRegister';
        refreshRegisterCaptcha();
      } catch (error) {
        ElMessage.error('发送失败，请检查邮箱和验证码');
        refreshCaptcha();
      } finally {
        sendingEmail.value = false;
      }
    }
  });
};

// 处理注册
const handleRegister = async () => {
  const registerFormRef = ref<InstanceType<typeof ElForm>>();
  registerFormRef.value?.validate(async (valid) => {
    if (valid) {
      registerLoading.value = true;
      try {
        await register(
          registerForm.email,
          registerForm.password,
          registerForm.emailCode,
          registerForm.newCaptcha
        );
        ElMessage.success('注册成功，请登录');
        router.push('/login');
      } catch (error) {
        ElMessage.error('注册失败，请检查信息是否正确');
        refreshRegisterCaptcha();
      } finally {
        registerLoading.value = false;
      }
    }
  });
};

// 切换选项卡处理
const handleTabClick = (tab: any) => {
  if (tab.name === 'accountRegister' && !emailVerified.value) {
    activeTab.value = 'emailVerify';
    ElMessage.warning('请先完成邮箱验证');
  }
};

// 前往登录页面
const goToLogin = () => {
  router.push('/login');
};

// 页面挂载时初始化
onMounted(() => {
  refreshCaptcha();
});
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.register-card {
  width: 500px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.register-header {
  margin-bottom: 25px;
  text-align: center;
}

.register-title {
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

.button-group {
  margin-top: 15px;
}

.send-button,
.register-button {
  width: 100%;
}

.login-link {
  margin-top: 20px;
  text-align: center;
  color: #606266;
}

.register-tabs {
  margin-bottom: 20px;
}
</style>