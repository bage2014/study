# 用户列表应用项目

这是一个完整的多模块用户列表应用，包含后端服务、Web前端应用和Android移动应用，实现了用户数据的获取和展示功能。

## 项目结构

```
├── android-app/              # Android移动应用模块
├── backend/                  # 原始Node.js后端服务
├── frontend/                 # 原始Web前端应用
├── spring-boot-backend/      # Spring Boot后端服务
├── vue-frontend/             # Vue 3前端应用
├── .gitignore                # 根目录Git忽略文件
└── package.json              # 项目根配置文件
```

## 模块说明

### 1. Spring Boot后端服务 (spring-boot-backend/)

**技术栈：**
- Java 17
- Spring Boot 3.2.0
- Maven
- RESTful API

**功能：**
- 提供用户列表数据接口
- 支持跨域请求
- 实现RESTful API设计

**核心文件：**
- `src/main/java/com/example/backend/SpringBootBackendApplication.java` - 应用主类
- `src/main/java/com/example/backend/controller/UserController.java` - API控制器
- `src/main/java/com/example/backend/model/User.java` - 用户数据模型
- `src/main/resources/application.properties` - 应用配置文件
- `pom.xml` - Maven依赖配置

### 2. Vue 3前端应用 (vue-frontend/)

**技术栈：**
- Vue 3
- Vite
- JavaScript
- Fetch API

**功能：**
- 展示用户列表
- 响应式布局
- 与Spring Boot后端通信

**核心文件：**
- `src/App.vue` - 主应用组件
- `src/main.js` - 应用入口文件
- `vite.config.js` - Vite配置文件
- `package.json` - 项目依赖配置

### 3. Android移动应用 (android-app/)

**技术栈：**
- Java 8
- Android SDK (API 34)
- RecyclerView
- Retrofit + Gson
- MVVM架构

**功能：**
- 移动设备上展示用户列表
- 与Spring Boot后端通信
- 响应式列表布局

**核心文件：**
- `app/src/main/java/com/example/userlistapp/MainActivity.java` - 主活动
- `app/src/main/java/com/example/userlistapp/adapter/UserAdapter.java` - 列表适配器
- `app/src/main/java/com/example/userlistapp/api/RetrofitClient.java` - 网络客户端
- `app/src/main/java/com/example/userlistapp/api/UserApi.java` - API接口定义
- `app/src/main/java/com/example/userlistapp/model/User.java` - 用户数据模型
- `app/src/main/res/layout/activity_main.xml` - 主布局
- `app/src/main/res/layout/user_item.xml` - 列表项布局

### 4. 原始Node.js后端 (backend/)

**技术栈：**
- Node.js
- Express
- CORS

**功能：**
- 提供用户列表数据接口
- 支持跨域请求

**核心文件：**
- `server.js` - 服务器入口文件
- `package.json` - 项目依赖配置

### 5. 原始Web前端 (frontend/)

**技术栈：**
- HTML5
- CSS3
- JavaScript
- Fetch API

**功能：**
- 展示用户列表
- 与Node.js后端通信

**核心文件：**
- `index.html` - 主页面
- `package.json` - 项目依赖配置

## 启动指南

### Spring Boot后端服务

```bash
cd spring-boot-backend
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

### Vue 3前端应用

```bash
cd vue-frontend
npm install
npm run dev
```

应用将在 `http://localhost:5173` 启动。

### Android移动应用

1. 使用Android Studio打开 `android-app` 目录
2. 连接Android设备或启动模拟器
3. 点击运行按钮启动应用

### 原始Node.js后端

```bash
cd backend
npm install
npm start
```

服务将在 `http://localhost:5001` 启动。

### 原始Web前端

```bash
cd frontend
npm install
npm start
```

应用将在 `http://localhost:8081` 启动。

## API接口说明

### Spring Boot后端 API

- `GET /api/users` - 获取用户列表

### Node.js后端 API

- `GET /api/users` - 获取用户列表

## 数据结构

用户数据结构：

```json
{
  "id": 1,
  "name": "用户名",
  "email": "user@example.com"
}
```

## 开发注意事项

1. 确保各模块的.gitignore文件已正确配置
2. Spring Boot后端默认端口为8080，Vue前端默认端口为5173
3. Android应用需要确保网络权限已在AndroidManifest.xml中配置
4. 各模块之间通过RESTful API进行通信，需要确保网络连接正常
5. 开发时可以同时启动Spring Boot后端和Vue前端，进行前后端联调

## 版本信息

- Spring Boot: 3.2.0
- Vue: 3.x
- Android SDK: API 34
- Java: 17 (后端) / 8 (Android)