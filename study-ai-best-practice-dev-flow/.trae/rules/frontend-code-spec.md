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

#### 2.1 统一请求封装

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json'
  }
});
```

#### 2.2 统一请求头

所有请求必须携带以下统一请求头：

| 请求头 | 说明 | 值示例 |
|--------|------|--------|
| `Content-Type` | 请求内容类型 | `application/json;charset=UTF-8` |
| `Accept` | 接受类型 | `application/json` |
| `Authorization` | 认证令牌（登录后） | `Bearer xxx.token.xxx` |
| `X-Request-Id` | 请求唯一标识 | `uuid` |
| `X-Client-Type` | 客户端类型 | `web` |
| `X-Client-Version` | 客户端版本 | `1.0.0` |

#### 2.3 请求拦截器

```javascript
import { v4 as uuidv4 } from 'uuid';

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    config.headers['X-Request-Id'] = uuidv4();
    config.headers['X-Client-Type'] = 'web';
    config.headers['X-Client-Version'] = '1.0.0';
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
```

#### 2.4 响应拦截器

```javascript
api.interceptors.response.use(
  (response) => {
    const { data } = response;
    
    if (data.code === 200) {
      return data.data;
    } else {
      console.error('API Error:', data.message);
      return Promise.reject(new Error(data.message));
    }
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response;
      
      switch (status) {
        case 401:
          console.error('未授权，请重新登录');
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
          break;
        case 403:
          console.error('无权限访问');
          break;
        case 404:
          console.error('资源不存在');
          break;
        case 500:
          console.error('服务器内部错误');
          break;
        default:
          console.error('请求失败:', data?.message || error.message);
      }
    } else if (error.request) {
      console.error('请求超时或网络异常');
    } else {
      console.error('请求配置错误:', error.message);
    }
    
    return Promise.reject(error);
  }
);
```

#### 2.5 API 模块示例

```javascript
export const userAPI = {
  login: (data) => api.post('/users/login', data),
  logout: () => api.post('/users/logout'),
  getUsers: (params) => api.get('/users', { params }),
  getUserById: (id) => api.get(`/users/${id}`),
  createUser: (data) => api.post('/users', data),
  updateUser: (id, data) => api.put(`/users/${id}`, data),
  deleteUser: (id) => api.delete(`/users/${id}`)
};
```

#### 2.6 响应格式

后端统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1715487600000
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | number | 状态码，200 表示成功 |
| `message` | string | 提示信息 |
| `data` | any | 响应数据 |
| `timestamp` | number | 时间戳 |

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