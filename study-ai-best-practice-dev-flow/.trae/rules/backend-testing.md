# 后端测试规则
# 强制测试先行，确保单元测试覆盖率不低于 85%

## 规则说明

本规则强制后端开发遵循测试先行的开发模式，确保代码质量和可维护性。

## 测试要求

### 1. 测试覆盖率门槛

| 类型 | 最低覆盖率 |
|------|------------|
| 后端代码总体覆盖率 | ≥ 85% |
| 核心业务逻辑覆盖率 | ≥ 90% |
| Controller 层覆盖率 | ≥ 80% |
| Service 层覆盖率 | ≥ 85% |

### 2. 测试编写时机

- **必须先写测试**：在编写业务代码之前，必须先编写对应的单元测试
- **测试必须通过**：代码提交前，所有单元测试必须通过
- **覆盖率必须达标**：覆盖率不达标不允许合并代码

### 3. 测试文件命名规范

```
测试类位置：src/test/java/com/bage/study/ai/best/practice/dev/flow/
测试类命名：{被测类名}Test.java
测试方法命名：test{被测方法名}_{场景描述}()
```

### 4. 测试类结构规范

```java
package com.bage.study.ai.best.practice.dev.flow.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testGetUserById_Success() {
        // Arrange - 准备测试数据
        Long userId = 1L;

        // Act - 执行被测方法
        User user = userService.getUserById(userId);

        // Assert - 验证结果
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long userId = 999L;

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }
}
```

### 5. 测试数据管理

- 使用 `@BeforeEach` 或 `@BeforeAll` 准备测试数据
- 使用 `@AfterEach` 或 `@AfterAll` 清理测试数据
- 避免在测试中使用真实的生产数据

### 6. Mock 使用规范

- 外部依赖必须使用 Mock（如数据库、第三方 API）
- 使用 `@MockBean` 注解创建 Mock 对象
- 使用 `@InjectMocks` 注入被测对象

```java
@MockBean
private UserRepository userRepository;

@InjectMocks
private UserService userService;
```

### 7. 运行测试命令

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成覆盖率报告
mvn test jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html
```

### 8. 覆盖率报告

测试运行后，在 `target/site/jacoco/` 目录生成覆盖率报告。

### 9. 测试文档要求

每个功能模块必须创建对应的测试文档，存放在功能模块目录下：

```
docs/features/{功能}/
└── {功能}-测试文档-{日期}.md
```

**测试文档内容要求：**
- 测试概述和范围
- 单元测试用例清单
- 接口测试用例清单
- UI 测试用例清单（如有）
- 测试数据说明
- 测试执行记录
- 缺陷记录
- 测试覆盖率统计

### 10. 测试命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 单元测试 | UT-{模块}-{编号} | UT-LOGIN-001 |
| 集成测试 | IT-{模块}-{编号} | IT-USER-001 |
| UI 测试 | UI-{模块}-{编号} | UI-TRACK-001 |

## 违规处理

- 覆盖率低于 85%：拒绝代码提交
- 测试失败：拒绝代码提交
- 未编写测试：拒绝代码提交（核心业务逻辑）
- 未创建测试文档：拒绝代码合并