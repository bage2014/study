---
name: "common-backend-unit-test"
description: "提供后端单元测试指导和生成能力，支持JUnit 5、Mockito等框架"
trigger: "用户需要编写后端单元测试时"
disable-when: "项目已完成测试覆盖或使用其他测试框架"
category: "backend"
tags: ["testing", "junit", "mockito", "unit-test"]
---

# 后端单元测试技能

## 功能描述

提供后端服务的单元测试能力，帮助开发者编写高质量的单元测试，确保代码的正确性和可维护性。

## 何时使用

在以下情况调用此技能：
- 需要为后端代码编写单元测试时
- 需要评估测试覆盖率时
- 需要优化现有测试时
- 需要生成测试报告时

## 核心功能

- **测试生成**：根据代码生成单元测试用例
- **测试审查**：检查测试代码质量和覆盖率
- **Mock生成**：生成Mock对象和桩代码
- **测试优化**：提供测试优化建议
- **报告生成**：生成测试覆盖率报告

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| language | String | 是 | 目标编程语言（Java/Python/Go等） |
| framework | String | 否 | 使用的测试框架（JUnit/Pytest/Ginkgo等） |
| targetCode | String | 是 | 目标代码或文件路径 |
| testType | String | 否 | 测试类型（单元测试/集成测试） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "language": "Java",
  "framework": "JUnit 5",
  "testCode": "生成的测试代码",
  "testCases": [
    {
      "name": "测试用例名称",
      "description": "测试描述",
      "expectedResult": "期望结果"
    }
  ],
  "coverage": {
    "lineCoverage": 85.5,
    "branchCoverage": 78.2,
    "methodCoverage": 90.0
  },
  "suggestions": ["优化建议1", "优化建议2"]
}
```

## 单元测试流程

```
需求分析 → 测试设计 → 测试编写 → 测试执行 → 覆盖率分析 → 优化改进 → 报告生成
```

### 详细步骤

1. **需求分析**：理解被测代码的功能和行为
2. **测试设计**：设计测试用例，覆盖正常和异常场景
3. **测试编写**：编写测试代码，使用合适的断言
4. **测试执行**：运行测试，验证代码正确性
5. **覆盖率分析**：分析测试覆盖率，识别未覆盖的代码
6. **优化改进**：根据覆盖率结果补充测试用例
7. **报告生成**：生成测试报告和文档

## 测试质量检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 覆盖完整 | 是否覆盖主要业务场景 | ✅/❌ |
| 边界测试 | 是否测试边界条件 | ✅/❌ |
| 异常测试 | 是否测试异常情况 | ✅/❌ |
| 断言明确 | 断言是否清晰明确 | ✅/❌ |
| 测试独立 | 测试用例是否相互独立 | ✅/❌ |
| Mock正确 | Mock对象是否正确配置 | ✅/❌ |
| 命名规范 | 测试方法命名是否规范 | ✅/❌ |
| 文档完整 | 是否有必要的注释说明 | ✅/❌ |

## 最佳实践指南

### JUnit 5 测试规范

```java
@DisplayName("用户服务测试")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("创建用户 - 成功")
    void createUser_Success() {
        // given
        UserCreateRequest request = new UserCreateRequest("test@example.com");
        User expectedUser = new User("1", "test@example.com");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        
        // when
        User result = userService.createUser(request);
        
        // then
        assertThat(result.getId()).isEqualTo("1");
        verify(userRepository, times(1)).save(any(User.class));
    }
}
```

### 测试命名规范

```
方法名_场景_预期结果

例如：
- createUser_withValidEmail_returnsUser
- createUser_withNullEmail_throwsException
- getUserById_existingId_returnsUser
- getUserById_nonExistingId_returnsNull
```

### Mock 使用原则

1. **隔离外部依赖**：使用 Mock 隔离数据库、网络调用等外部依赖
2. **验证交互**：验证方法被正确调用
3. **避免过度Mock**：只Mock必要的外部依赖，不要Mock被测代码本身
4. **保持简单**：Mock配置应简洁明了

## 测试覆盖率目标

| 指标 | 目标值 | 说明 |
|------|--------|------|
| 行覆盖率 | ≥80% | 代码行被测试覆盖的比例 |
| 分支覆盖率 | ≥70% | 条件分支被测试覆盖的比例 |
| 方法覆盖率 | ≥85% | 方法被测试覆盖的比例 |

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| TestGenerator | 测试生成器 | 根据代码生成测试用例 |
| MockGenerator | Mock生成器 | 生成Mock对象和配置 |
| CoverageAnalyzer | 覆盖率分析器 | 分析测试覆盖率 |
| TestReporter | 测试报告器 | 生成测试报告 |

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| TEST_FRAMEWORK | 默认测试框架 | junit5 |
| COVERAGE_THRESHOLD | 覆盖率阈值 | 80 |

### 配置文件

```yaml
testing:
  framework: junit5
  coverage:
    line-threshold: 80
    branch-threshold: 70
    method-threshold: 85
  report:
    format: html
    output-dir: target/site/jacoco
```

## 扩展指南

### 添加新测试框架支持

1. 创建对应框架的测试生成器
2. 定义该框架的测试模板和规范
3. 添加覆盖率分析集成

### 添加自定义测试规则

1. 在规则引擎中添加新规则
2. 定义规则的检查逻辑和修复建议
3. 更新测试质量检查清单
## 触发条件

