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
- **单元测试**：覆盖核心业务逻辑，所有controller接口必须编写单元测试
- **集成测试**：验证接口功能，确保各组件协同工作
- **测试要求**：
  - 每个controller方法都必须有对应的测试用例
  - 测试用例必须包含成功场景和失败场景
  - 使用 `@SpringBootTest` 进行集成测试
  - 使用 Mockito 进行单元测试

### 9. Controller 测试规范

#### 9.1 测试类结构
```java
package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.[ModelName];
import com.familytree.service.[ServiceName];
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class [ControllerName]Test {
    @Mock
    private [ServiceName] [serviceName];
    
    @InjectMocks
    private [ControllerName] [controllerName];
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    // 测试方法...
}
```

#### 9.2 测试方法模板

##### 成功场景测试
```java
@Test
void test[MethodName]Success() {
    // Arrange
    // 准备测试数据和mock行为
    
    // Act
    // 调用被测试的方法
    
    // Assert
    // 验证返回结果和mock调用
    assertNotNull(response);
    assertEquals(200, response.getCode());
    assertEquals("Success", response.getMessage());
    assertNotNull(response.getData());
    verify([serviceName], times(1)).[methodName](...);
}
```

##### 失败场景测试
```java
@Test
void test[MethodName]Failure() {
    // Arrange
    // 准备测试数据和mock异常行为
    
    // Act
    // 调用被测试的方法
    
    // Assert
    // 验证返回结果和mock调用
    assertNotNull(response);
    assertEquals(400, response.getCode());
    assertEquals(errorMessage, response.getMessage());
    assertNull(response.getData());
    verify([serviceName], times(1)).[methodName](...);
}
```

#### 9.3 测试覆盖范围
每个controller的测试应覆盖以下场景：
- 所有HTTP方法（GET、POST、PUT、DELETE）
- 成功场景
- 失败场景（异常处理）
- 边界情况

#### 9.4 测试执行命令
```bash
cd backend && mvn test
```

#### 9.5 测试结果验证
- 所有测试应通过（Failures: 0, Errors: 0）
- 代码覆盖率应达到合理水平

#### 9.6 示例测试类
以AuthControllerTest为例：
```java
package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.model.User;
import com.familytree.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthService authService;
    
    @InjectMocks
    private AuthController authController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testLoginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        
        String token = "test-token";
        when(authService.login(request)).thenReturn(token);
        
        // Act
        ApiResponse<Map<String, Object>> response = authController.login(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(token, response.getData().get("token"));
        verify(authService, times(1)).login(request);
    }
    
    @Test
    void testLoginFailure() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        
        String errorMessage = "Invalid email or password";
        when(authService.login(request)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Map<String, Object>> response = authController.login(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(authService, times(1)).login(request);
    }
    
    // 其他测试方法...
}
```

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