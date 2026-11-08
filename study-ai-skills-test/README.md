# 用户管理系统

前后端分离的用户 CRUD 管理系统。

## 项目结构

```
.
├── backend/          # Spring Boot 后端
│   ├── src/
│   │   ├── main/java/com/bage/study/userbackend/
│   │   │   ├── controller/    # REST API 控制器
│   │   │   ├── entity/        # JPA 实体
│   │   │   ├── repository/    # 数据访问层
│   │   │   └── service/       # 业务逻辑层
│   │   └── main/resources/
│   │       └── application.yml
│   └── pom.xml
│
└── frontend/         # Vue 3 前端
    ├── src/
    │   ├── api/         # API 请求
    │   ├── components/  # Vue 组件
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 技术栈

### 后端
- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- H2 内存数据库
- Lombok

### 前端
- Vue 3
- Element Plus
- Axios
- Vite

## 启动方式

### 1. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

H2 数据库控制台：`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: (空)

### 2. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users | 获取所有用户 |
| GET | /api/users/{id} | 获取单个用户 |
| POST | /api/users | 创建用户 |
| PUT | /api/users/{id} | 更新用户 |
| DELETE | /api/users/{id} | 删除用户 |

## 功能说明

- ✅ 用户列表展示
- ✅ 新增用户
- ✅ 编辑用户
- ✅ 删除用户
- ✅ 表单验证
- ✅ 跨域支持
