# 后端项目规范

## 技术栈
- **框架**: Spring Boot 3.1.10+
- **JDK**: 21
- **数据库**: H2 (开发环境) / MySQL (生产环境)
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **构建工具**: Maven

## 项目结构
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/familytree/
│   │   │   ├── config/         # 配置类
│   │   │   ├── controller/     # 控制器
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── exception/      # 异常处理
│   │   │   ├── model/          # 实体类
│   │   │   ├── repository/     # 数据访问层
│   │   │   ├── service/        # 业务逻辑层
│   │   │   ├── utils/          # 工具类
│   │   │   └── FamilyTreeApplication.java  # 应用入口
│   │   └── resources/
│   │       └── application.properties  # 配置文件
│   └── test/                   # 测试代码
└── pom.xml                     # Maven配置
```

## 编码规范

### 命名规范
- **包名**: 小写字母，使用点分隔，如 `com.familytree.controller`
- **类名**: 首字母大写，驼峰命名，如 `AuthController`
- **方法名**: 首字母小写，驼峰命名，如 `login`
- **变量名**: 首字母小写，驼峰命名，如 `userService`
- **常量名**: 全大写，下划线分隔，如 `JWT_SECRET`

### 代码风格
- 使用 4 空格缩进
- 方法体之间空一行
- 类成员之间空一行
- 注释清晰，说明方法功能和参数含义

## 核心规范

### 1. 响应格式
所有控制器方法必须返回统一的 `ApiResponse` 对象：

```java
public class ApiResponse<T> {
    private int code;           // 状态码，200 表示成功
    private String message;      // 消息
    private T data;              // 数据
    
    // 构造方法、getter、setter
    
    // 静态方法
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(400, message, null);
    }
}
```

### 2. 控制器规范
- 使用 `@RestController` 注解
- 统一使用 `@RequestMapping` 定义基础路径，如 `/api/auth`
- 方法级使用 `@GetMapping`、`@PostMapping` 等
- 参数验证使用 `@Valid` 注解
- 异常处理由全局异常处理器统一处理

### 3. 服务层规范
- 服务接口定义业务逻辑
- 服务实现类使用 `@Service` 注解
- 事务管理使用 `@Transactional` 注解
- 业务逻辑处理应包含必要的参数校验

### 4. 数据访问层规范
- 使用 Spring Data JPA
- 仓库接口继承 `JpaRepository`
- 复杂查询使用 `@Query` 注解或 `Specification`

### 5. 安全规范
- 使用 JWT 进行身份认证
- 密码使用 BCrypt 加密
- 权限控制使用 Spring Security
- 敏感信息使用环境变量或配置文件管理

### 6. 异常处理
- 全局异常处理器捕获并统一处理异常
- 业务异常应继承 `RuntimeException`
- 异常信息应清晰明确

### 7. 配置管理
- 使用 `application.properties` 或 `application.yml`
- 不同环境使用不同配置文件，如 `application-dev.properties`
- 敏感配置使用环境变量注入

### 8. 测试规范
- 单元测试覆盖核心业务逻辑
- 集成测试验证接口功能
- 使用 `@SpringBootTest` 进行集成测试

## 开发流程
1. 需求分析与设计
2. 数据库设计
3. 实体类与仓库层开发
4. 服务层业务逻辑实现
5. 控制器接口开发
6. 测试与调试
7. 代码审查
8. 部署与监控

## 最佳实践
- 遵循 SOLID 原则
- 采用分层架构，职责明确
- 使用依赖注入，减少耦合
- 充分利用 Spring Boot 自动配置
- 定期代码审查与重构
- 编写清晰的文档