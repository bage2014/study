# 用户列表应用项目

这是一个完整的多模块用户列表应用，包含后端服务、Web前端应用和Android移动应用，实现了用户数据的获取和展示功能。

## 项目结构

```
├── flutter_app/              # Flutter移动应用模块
├── spring-boot-backend/      # Spring Boot后端服务
├── vue-frontend/             # Vue 3前端应用
├── .gitignore                # 根目录Git忽略文件
└── README.md                 # 项目结构说明文档
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

### 3. Flutter移动应用 (flutter_app/)

**技术栈：**
- Dart
- Flutter 3.0+
- HTTP Package
- FutureBuilder
- Material Design

**功能：**
- 移动设备上展示用户列表
- 与Spring Boot后端通信
- 响应式列表布局
- 加载状态和错误处理
- 支持Android和iOS平台

**核心文件：**
- `lib/main.dart` - 应用入口和主界面
- `lib/models/user.dart` - 用户数据模型
- `lib/api/api_service.dart` - API服务类
- `android/app/src/main/AndroidManifest.xml` - Android权限配置

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

### Flutter移动应用

1. 确保已安装Flutter SDK
2. 使用VS Code或Android Studio打开 `flutter_app` 目录
3. 连接Android设备或启动模拟器
4. 运行 `flutter pub get` 安装依赖
5. 点击运行按钮或执行 `flutter run` 启动应用

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
- Flutter: 3.0+
- Dart: 3.8.1+