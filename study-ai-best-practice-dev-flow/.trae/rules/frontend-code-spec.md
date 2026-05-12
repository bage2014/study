# 前端编码规范

## 一、目录结构

```
frontend/src/
├── components/     # 组件目录
│   ├── layout/     # 布局组件
│   └── common/     # 通用组件
├── views/          # 页面视图
├── api/            # API 接口
├── stores/         # 状态管理 (Pinia)
├── router/         # 路由配置
├── utils/          # 工具函数
└── styles/         # 全局样式
```

## 二、命名规范

| 类型 | 规则 | 示例 |
|------|------|------|
| 组件名 | 大驼峰 + `.vue` | `UserTable.vue` |
| 变量名 | 小驼峰 | `userName` |
| 方法名 | 小驼峰 | `handleLogin` |
| 文件 | 大驼峰或小写短横线 | `user-management.vue` |

## 三、编码规范

### 1. 组件规范
- 使用 Composition API
- 脚本部分使用 `<script setup>`
- 模板部分使用 PascalCase 组件名

```vue
<script setup>
import { ref, reactive } from 'vue';

const form = reactive({
  username: '',
  password: ''
});

const handleSubmit = () => {
  // 提交逻辑
};
</script>

<template>
  <form @submit.prevent="handleSubmit">
    <input v-model="form.username" />
    <input v-model="form.password" type="password" />
  </form>
</template>
```

### 2. API 规范
- 统一管理 API 接口
- 使用 axios 封装

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const userAPI = {
  login: (data) => api.post('/users/login', data),
  getUsers: () => api.get('/users')
};
```

### 3. 路由规范
- 使用 Vue Router
- 路由配置包含 meta 信息

```javascript
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/users',
    name: 'UserManagement',
    component: () => import('../views/UserManagement.vue'),
    meta: { requiresAuth: true }
  }
];
```

## 四、状态管理

- 使用 Pinia
- 模块化管理状态

```javascript
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token')
  }),
  actions: {
    login(user, token) {
      this.user = user;
      this.token = token;
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));
    },
    logout() {
      this.user = null;
      this.token = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  }
});
```

## 五、Playwright 测试规范

### 测试文件结构
```
frontend/tests/
├── login.spec.js
├── user-management.spec.js
└── [功能].spec.js
```

### 测试用例编写规范

```javascript
import { test, expect } from '@playwright/test';

test.describe('登录功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
  });

  test('登录成功', async ({ page }) => {
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    await expect(page.locator('.user-info')).toBeVisible();
  });
});
```

### 测试执行命令

| 命令 | 描述 |
|------|------|
| `npm test` | 运行所有测试 |
| `npx playwright test login.spec.js` | 运行指定测试 |
| `npx playwright show-report` | 查看报告 |