package com.bage.ai.pipeline.agent.config;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MockChatLanguageModel implements ChatLanguageModel {

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages) {
        String userContent = messages.stream()
                .filter(m -> m instanceof UserMessage)
                .map(m -> ((UserMessage) m).text())
                .findFirst()
                .orElse("");

        String systemContent = messages.stream()
                .filter(m -> m instanceof SystemMessage)
                .map(m -> ((SystemMessage) m).text())
                .findFirst()
                .orElse("");

        String response = generateMockResponse(userContent, systemContent);
        log.info("Mock AI response generated for request type: {}", detectRequestType(systemContent));
        return Response.from(AiMessage.from(response));
    }

    private String detectRequestType(String systemContent) {
        if (systemContent.contains("decompose") && systemContent.contains("feature points")) {
            return "feature-point-split";
        }
        if (systemContent.contains("split a feature") || systemContent.contains("atomic implementation tasks")) {
            return "task-split";
        }
        if (systemContent.contains("generate code") || systemContent.contains("implement") || systemContent.contains("writeFile")) {
            return "code-generation";
        }
        if (systemContent.contains("code reviewer") || systemContent.contains("identify issues")) {
            return "code-review";
        }
        if (systemContent.contains("generate.*test") || systemContent.contains("JUnit") || systemContent.contains("pytest")) {
            return "test-generation";
        }
        if (systemContent.contains("requirement") || systemContent.contains("需求")) {
            return "requirement-analysis";
        }
        return "unknown";
    }

    private String detectRequirementType(String userContent) {
        if (userContent.contains("接口文档") || userContent.contains("API文档") || userContent.contains("API文档生成") || userContent.contains("生成接口文档")) {
            return "api-doc";
        }
        if (userContent.contains("用户管理") || userContent.contains("用户") || userContent.contains("User")) {
            return "user-management";
        }
        if (userContent.contains("健康检查") || userContent.contains("health") || userContent.contains("监控")) {
            return "health-check";
        }
        if (userContent.contains("数据统计") || userContent.contains("报表") || userContent.contains("统计")) {
            return "data-report";
        }
        if (userContent.contains("登录") || userContent.contains("认证") || userContent.contains("auth")) {
            return "auth";
        }
        if (userContent.contains("订单") || userContent.contains("order")) {
            return "order";
        }
        if (userContent.contains("产品") || userContent.contains("Product") || userContent.contains("CRUD")) {
            return "product-management";
        }
        return "generic";
    }

    private String generateMockResponse(String userContent, String systemContent) {
        String requestType = detectRequestType(systemContent);
        String requirementType = detectRequirementType(userContent);

        return switch (requestType) {
            case "requirement-analysis" -> generateMockRequirementAnalysis(userContent, requirementType);
            case "feature-point-split" -> generateMockFeaturePointSplit(userContent, requirementType);
            case "task-split" -> generateMockTaskSplit(userContent, requirementType);
            case "code-generation" -> generateMockCodeGeneration(userContent, requirementType);
            case "code-review" -> generateMockCodeReview();
            case "test-generation" -> generateMockTestGeneration(userContent, requirementType);
            default -> generateMockDefault();
        };
    }

    private String generateMockRequirementAnalysis(String userContent, String requirementType) {
        return switch (requirementType) {
            case "api-doc" -> """
                    {
                      "projectType": "SPRING_BOOT",
                      "buildTool": "MAVEN",
                      "techStack": ["Java 21", "Spring Boot 3.3", "JPA", "H2"],
                      "parsedRequirement": {
                        "title": "生成API接口文档",
                        "description": "为demo-backend项目生成完整的API接口文档，包含所有RESTful接口的详细说明",
                        "scope": ["生成接口文档", "API文档输出"],
                        "constraints": ["使用OpenAPI 3.0规范", "输出为Markdown格式"],
                        "acceptanceCriteria": ["生成完整的API文档", "包含所有接口的请求/响应示例", "包含参数说明"]
                      }
                    }
                    """;
            case "user-management" -> """
                    {
                      "projectType": "SPRING_BOOT",
                      "buildTool": "MAVEN",
                      "techStack": ["Java 21", "Spring Boot 3.3", "JPA", "H2"],
                      "parsedRequirement": {
                        "title": "用户管理模块",
                        "description": "实现用户的CRUD管理功能，包括用户列表查询、新增、编辑、删除",
                        "scope": ["用户列表", "用户创建", "用户编辑", "用户删除"],
                        "constraints": [],
                        "acceptanceCriteria": ["GET /api/users 返回用户列表", "POST /api/users 创建用户", "PUT /api/users/{id} 更新用户", "DELETE /api/users/{id} 删除用户"]
                      }
                    }
                    """;
            case "data-report" -> """
                    {
                      "projectType": "SPRING_BOOT",
                      "buildTool": "MAVEN",
                      "techStack": ["Java 21", "Spring Boot 3.3", "JPA", "H2"],
                      "parsedRequirement": {
                        "title": "数据统计报表",
                        "description": "实现数据统计功能，生成各类业务报表",
                        "scope": ["数据统计", "报表生成"],
                        "constraints": [],
                        "acceptanceCriteria": ["GET /api/reports 获取报表列表", "GET /api/reports/{id} 获取报表详情"]
                      }
                    }
                    """;
            case "auth" -> """
                    {
                      "projectType": "SPRING_BOOT",
                      "buildTool": "MAVEN",
                      "techStack": ["Java 21", "Spring Boot 3.3", "JWT", "H2"],
                      "parsedRequirement": {
                        "title": "用户认证登录",
                        "description": "实现用户登录认证功能，支持JWT令牌生成和验证",
                        "scope": ["用户登录", "JWT认证"],
                        "constraints": [],
                        "acceptanceCriteria": ["POST /api/auth/login 用户登录", "返回JWT令牌", "令牌验证"]
                      }
                    }
                    """;
            case "order" -> """
                    {
                      "projectType": "SPRING_BOOT",
                      "buildTool": "MAVEN",
                      "techStack": ["Java 21", "Spring Boot 3.3", "JPA", "H2"],
                      "parsedRequirement": {
                        "title": "订单管理模块",
                        "description": "实现订单的创建、查询、支付等功能",
                        "scope": ["订单创建", "订单查询", "订单支付"],
                        "constraints": [],
                        "acceptanceCriteria": ["POST /api/orders 创建订单", "GET /api/orders 查询订单列表", "GET /api/orders/{id} 查询订单详情"]
                      }
                    }
                    """;
            default -> """
                    {
                      "projectType": "SPRING_BOOT",
                      "buildTool": "MAVEN",
                      "techStack": ["Java 21", "Spring Boot 3.3", "JPA", "H2"],
                      "parsedRequirement": {
                        "title": "添加健康检查端点",
                        "description": "为demo-backend添加一个健康检查API端点，用于监控服务状态",
                        "scope": ["新增端点"],
                        "constraints": [],
                        "acceptanceCriteria": ["GET /api/health 返回200状态码", "返回JSON格式的健康状态信息"]
                      }
                    }
                    """;
        };
    }

    private String generateMockFeaturePointSplit(String userContent, String requirementType) {
        return switch (requirementType) {
            case "api-doc" -> """
                    [
                      {
                        "id": "fp-1",
                        "title": "API文档生成器",
                        "description": "创建ApiDocController，提供API文档生成和查询接口",
                        "acceptanceCriteria": ["GET /api/docs 获取API文档", "生成OpenAPI格式文档"],
                        "scope": "backend"
                      },
                      {
                        "id": "fp-2",
                        "title": "接口元数据管理",
                        "description": "管理接口元数据，支持动态生成文档",
                        "acceptanceCriteria": ["支持CRUD操作", "支持版本管理"],
                        "scope": "backend"
                      }
                    ]
                    """;
            case "user-management" -> """
                    [
                      {
                        "id": "fp-1",
                        "title": "用户控制器",
                        "description": "创建UserController，提供用户管理API端点",
                        "acceptanceCriteria": ["GET /api/users 返回用户列表", "POST /api/users 创建用户", "PUT /api/users/{id} 更新用户", "DELETE /api/users/{id} 删除用户"],
                        "scope": "backend"
                      },
                      {
                        "id": "fp-2",
                        "title": "用户服务",
                        "description": "创建UserService，实现用户业务逻辑",
                        "acceptanceCriteria": ["实现用户CRUD", "实现用户查询"],
                        "scope": "backend"
                      },
                      {
                        "id": "fp-3",
                        "title": "用户实体",
                        "description": "创建User实体类和Repository",
                        "acceptanceCriteria": ["定义用户表结构", "实现数据访问"],
                        "scope": "backend"
                      }
                    ]
                    """;
            case "data-report" -> """
                    [
                      {
                        "id": "fp-1",
                        "title": "报表控制器",
                        "description": "创建ReportController，提供报表API端点",
                        "acceptanceCriteria": ["GET /api/reports 返回报表列表", "GET /api/reports/{id} 返回报表详情"],
                        "scope": "backend"
                      },
                      {
                        "id": "fp-2",
                        "title": "报表服务",
                        "description": "创建ReportService，实现报表生成逻辑",
                        "acceptanceCriteria": ["实现数据统计", "生成报表数据"],
                        "scope": "backend"
                      }
                    ]
                    """;
            case "auth" -> """
                    [
                      {
                        "id": "fp-1",
                        "title": "认证控制器",
                        "description": "创建AuthController，提供登录API端点",
                        "acceptanceCriteria": ["POST /api/auth/login 用户登录", "返回JWT令牌"],
                        "scope": "backend"
                      },
                      {
                        "id": "fp-2",
                        "title": "JWT服务",
                        "description": "创建JwtService，实现令牌生成和验证",
                        "acceptanceCriteria": ["生成JWT令牌", "验证令牌有效性"],
                        "scope": "backend"
                      }
                    ]
                    """;
            case "order" -> """
                    [
                      {
                        "id": "fp-1",
                        "title": "订单控制器",
                        "description": "创建OrderController，提供订单API端点",
                        "acceptanceCriteria": ["POST /api/orders 创建订单", "GET /api/orders 查询订单列表"],
                        "scope": "backend"
                      },
                      {
                        "id": "fp-2",
                        "title": "订单服务",
                        "description": "创建OrderService，实现订单业务逻辑",
                        "acceptanceCriteria": ["创建订单", "查询订单", "订单支付"],
                        "scope": "backend"
                      }
                    ]
                    """;
            default -> """
                    [
                      {
                        "id": "fp-1",
                        "title": "健康检查控制器",
                        "description": "创建HealthController，提供健康检查API端点",
                        "acceptanceCriteria": ["GET /api/health 返回200状态码", "返回JSON格式的健康状态信息"],
                        "scope": "backend"
                      }
                    ]
                    """;
        };
    }

    private String generateMockTaskSplit(String userContent, String requirementType) {
        return switch (requirementType) {
            case "api-doc" -> """
                    [
                      {
                        "id": "fp-1-task-1",
                        "featurePointId": "fp-1",
                        "title": "创建ApiDocController",
                        "description": "创建ApiDocController类，提供API文档生成端点",
                        "targetFiles": ["src/main/java/com/bage/demo/controller/ApiDocController.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-2-task-1",
                        "featurePointId": "fp-2",
                        "title": "创建ApiDocService",
                        "description": "创建ApiDocService类，实现文档生成逻辑",
                        "targetFiles": ["src/main/java/com/bage/demo/service/ApiDocService.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-2-task-2",
                        "featurePointId": "fp-2",
                        "title": "创建ApiDoc实体",
                        "description": "创建ApiDoc实体类和Repository",
                        "targetFiles": ["src/main/java/com/bage/demo/entity/ApiDoc.java", "src/main/java/com/bage/demo/repository/ApiDocRepository.java"],
                        "type": "BACKEND"
                      }
                    ]
                    """;
            case "user-management" -> """
                    [
                      {
                        "id": "fp-1-task-1",
                        "featurePointId": "fp-1",
                        "title": "创建UserController",
                        "description": "创建UserController类，提供用户管理API端点",
                        "targetFiles": ["src/main/java/com/bage/demo/controller/UserController.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-2-task-1",
                        "featurePointId": "fp-2",
                        "title": "创建UserService",
                        "description": "创建UserService接口和实现类",
                        "targetFiles": ["src/main/java/com/bage/demo/service/UserService.java", "src/main/java/com/bage/demo/service/impl/UserServiceImpl.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-3-task-1",
                        "featurePointId": "fp-3",
                        "title": "创建User实体",
                        "description": "创建User实体类和UserRepository",
                        "targetFiles": ["src/main/java/com/bage/demo/entity/User.java", "src/main/java/com/bage/demo/repository/UserRepository.java"],
                        "type": "BACKEND"
                      }
                    ]
                    """;
            case "data-report" -> """
                    [
                      {
                        "id": "fp-1-task-1",
                        "featurePointId": "fp-1",
                        "title": "创建ReportController",
                        "description": "创建ReportController类，提供报表API端点",
                        "targetFiles": ["src/main/java/com/bage/demo/controller/ReportController.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-2-task-1",
                        "featurePointId": "fp-2",
                        "title": "创建ReportService",
                        "description": "创建ReportService接口和实现类",
                        "targetFiles": ["src/main/java/com/bage/demo/service/ReportService.java", "src/main/java/com/bage/demo/service/impl/ReportServiceImpl.java"],
                        "type": "BACKEND"
                      }
                    ]
                    """;
            case "auth" -> """
                    [
                      {
                        "id": "fp-1-task-1",
                        "featurePointId": "fp-1",
                        "title": "创建AuthController",
                        "description": "创建AuthController类，提供登录API端点",
                        "targetFiles": ["src/main/java/com/bage/demo/controller/AuthController.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-2-task-1",
                        "featurePointId": "fp-2",
                        "title": "创建JwtService",
                        "description": "创建JwtService类，实现JWT令牌生成和验证",
                        "targetFiles": ["src/main/java/com/bage/demo/service/JwtService.java"],
                        "type": "BACKEND"
                      }
                    ]
                    """;
            case "order" -> """
                    [
                      {
                        "id": "fp-1-task-1",
                        "featurePointId": "fp-1",
                        "title": "创建OrderController",
                        "description": "创建OrderController类，提供订单API端点",
                        "targetFiles": ["src/main/java/com/bage/demo/controller/OrderController.java"],
                        "type": "BACKEND"
                      },
                      {
                        "id": "fp-2-task-1",
                        "featurePointId": "fp-2",
                        "title": "创建OrderService",
                        "description": "创建OrderService接口和实现类",
                        "targetFiles": ["src/main/java/com/bage/demo/service/OrderService.java", "src/main/java/com/bage/demo/service/impl/OrderServiceImpl.java"],
                        "type": "BACKEND"
                      }
                    ]
                    """;
            default -> """
                    [
                      {
                        "id": "fp-1-task-1",
                        "featurePointId": "fp-1",
                        "title": "创建HealthController",
                        "description": "在demo-backend中创建HealthController类，添加GET /api/health端点",
                        "targetFiles": ["src/main/java/com/bage/demo/controller/HealthController.java"],
                        "type": "BACKEND"
                      }
                    ]
                    """;
        };
    }

    private String generateMockCodeGeneration(String userContent, String requirementType) {
        return switch (requirementType) {
            case "api-doc" -> """
                    {
                      "success": true,
                      "filePath": "src/main/java/com/bage/demo/controller/ApiDocController.java",
                      "code": "package com.bage.demo.controller;\\\\n\\\\nimport io.swagger.v3.oas.annotations.Operation;\\\\nimport io.swagger.v3.oas.annotations.tags.Tag;\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.GetMapping;\\\\nimport org.springframework.web.bind.annotation.RequestMapping;\\\\nimport org.springframework.web.bind.annotation.RestController;\\\\nimport java.util.List;\\\\nimport java.util.Map;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/docs\\\")\\\\n@Tag(name = \\\"API文档\\\", description = \\\"API文档生成和管理接口\\\")\\\\npublic class ApiDocController {\\\\n\\\\n    @GetMapping\\\\n    @Operation(summary = \\\"获取API文档\\\", description = \\\"获取完整的API接口文档\\\")\\\\n    public ResponseEntity<Map<String, Object>> getApiDocs() {\\\\n        return ResponseEntity.ok(Map.of(\\\\n            \\\"openapi\\\", \\\"3.0.1\\\",\\\\n            \\\"info\\\", Map.of(\\\\n                \\\"title\\\", \\\"Demo Backend API\\\",\\\\n                \\\"version\\\", \\\"1.0.0\\\",\\\\n                \\\"description\\\", \\\"演示后端服务API文档\\\"\\\\n            ),\\\\n            \\\"paths\\\", Map.of(\\\\n                \\\"/api/users\\\", Map.of(\\\\n                    \\\"get\\\", Map.of(\\\\n                        \\\"summary\\\", \\\"获取用户列表\\\",\\\\n                        \\\"description\\\", \\\"获取所有用户信息列表\\\",\\\\n                        \\\"responses\\\", Map.of(\\\\n                            \\\"200\\\", Map.of(\\\\n                                \\\"description\\\", \\\"成功获取用户列表\\\",\\\\n                                \\\"content\\\", Map.of(\\\\n                                    \\\"application/json\\\", Map.of(\\\\n                                        \\\"example\\\", \\\"[{\\\\\\\"id\\\\\\\":1,\\\\\\\"username\\\\\\\":\\\\\\\"admin\\\\\\\"}]\\\"\\\\n                                    )\\\\n                                )\\\\n                            )\\\\n                        )\\\\n                    ),\\\\n                    \\\"post\\\", Map.of(\\\\n                        \\\"summary\\\", \\\"创建用户\\\",\\\\n                        \\\"description\\\", \\\"创建新用户\\\",\\\\n                        \\\"responses\\\", Map.of(\\\\n                            \\\"201\\\", Map.of(\\\\n                                \\\"description\\\", \\\"用户创建成功\\\"\\\\n                            )\\\\n                        )\\\\n                    )\\\\n                ),\\\\n                \\\"/api/health\\\", Map.of(\\\\n                    \\\"get\\\", Map.of(\\\\n                        \\\"summary\\\", \\\"健康检查\\\",\\\\n                        \\\"description\\\", \\\"检查服务健康状态\\\"\\\\n                    )\\\\n                )\\\\n            )\\\\n        ));\\\\n    }\\\\n}",
                      "summary": "成功生成API文档控制器代码"
                    }
                    """;
            case "user-management" -> """
                    {
                      "success": true,
                      "filePath": "src/main/java/com/bage/demo/controller/UserController.java",
                      "code": "package com.bage.demo.controller;\\\\n\\\\nimport com.bage.demo.entity.User;\\\\nimport com.bage.demo.service.UserService;\\\\nimport org.springframework.http.HttpStatus;\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.*;\\\\nimport java.util.List;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/users\\\")\\\\npublic class UserController {\\\\n\\\\n    private final UserService userService;\\\\n\\\\n    public UserController(UserService userService) {\\\\n        this.userService = userService;\\\\n    }\\\\n\\\\n    @GetMapping\\\\n    public ResponseEntity<List<User>> getAllUsers() {\\\\n        return ResponseEntity.ok(userService.getAllUsers());\\\\n    }\\\\n\\\\n    @GetMapping(\\\"/{id}\\\")\\\\n    public ResponseEntity<User> getUserById(@PathVariable Long id) {\\\\n        return userService.getUserById(id)\\\\n                .map(ResponseEntity::ok)\\\\n                .orElse(ResponseEntity.notFound().build());\\\\n    }\\\\n\\\\n    @PostMapping\\\\n    public ResponseEntity<User> createUser(@RequestBody User user) {\\\\n        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));\\\\n    }\\\\n\\\\n    @PutMapping(\\\"/{id}\\\")\\\\n    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {\\\\n        return ResponseEntity.ok(userService.updateUser(id, user));\\\\n    }\\\\n\\\\n    @DeleteMapping(\\\"/{id}\\\")\\\\n    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {\\\\n        userService.deleteUser(id);\\\\n        return ResponseEntity.noContent().build();\\\\n    }\\\\n}",
                      "summary": "成功生成用户管理控制器代码"
                    }
                    """;
            case "data-report" -> """
                    {
                      "success": true,
                      "filePath": "src/main/java/com/bage/demo/controller/ReportController.java",
                      "code": "package com.bage.demo.controller;\\\\n\\\\nimport com.bage.demo.service.ReportService;\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.*;\\\\nimport java.util.List;\\\\nimport java.util.Map;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/reports\\\")\\\\npublic class ReportController {\\\\n\\\\n    private final ReportService reportService;\\\\n\\\\n    public ReportController(ReportService reportService) {\\\\n        this.reportService = reportService;\\\\n    }\\\\n\\\\n    @GetMapping\\\\n    public ResponseEntity<List<Map<String, Object>>> getReports() {\\\\n        return ResponseEntity.ok(reportService.getReports());\\\\n    }\\\\n\\\\n    @GetMapping(\\\"/{id}\\\")\\\\n    public ResponseEntity<Map<String, Object>> getReportById(@PathVariable Long id) {\\\\n        return reportService.getReportById(id)\\\\n                .map(ResponseEntity::ok)\\\\n                .orElse(ResponseEntity.notFound().build());\\\\n    }\\\\n}",
                      "summary": "成功生成报表控制器代码"
                    }
                    """;
            case "auth" -> """
                    {
                      "success": true,
                      "filePath": "src/main/java/com/bage/demo/controller/AuthController.java",
                      "code": "package com.bage.demo.controller;\\\\n\\\\nimport com.bage.demo.service.JwtService;\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.*;\\\\nimport java.util.Map;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/auth\\\")\\\\npublic class AuthController {\\\\n\\\\n    private final JwtService jwtService;\\\\n\\\\n    public AuthController(JwtService jwtService) {\\\\n        this.jwtService = jwtService;\\\\n    }\\\\n\\\\n    @PostMapping(\\\"/login\\\")\\\\n    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {\\\\n        String username = credentials.get(\\\"username\\\");\\\\n        String password = credentials.get(\\\"password\\\");\\\\n        String token = jwtService.generateToken(username);\\\\n        return ResponseEntity.ok(Map.of(\\\\n            \\\"token\\\", token,\\\\n            \\\"tokenType\\\", \\\"Bearer\\\",\\\\n            \\\"expiresIn\\\", 86400\\\\n        ));\\\\n    }\\\\n}",
                      "summary": "成功生成认证控制器代码"
                    }
                    """;
            case "order" -> """
                    {
                      "success": true,
                      "filePath": "src/main/java/com/bage/demo/controller/OrderController.java",
                      "code": "package com.bage.demo.controller;\\\\n\\\\nimport com.bage.demo.entity.Order;\\\\nimport com.bage.demo.service.OrderService;\\\\nimport org.springframework.http.HttpStatus;\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.*;\\\\nimport java.util.List;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/orders\\\")\\\\npublic class OrderController {\\\\n\\\\n    private final OrderService orderService;\\\\n\\\\n    public OrderController(OrderService orderService) {\\\\n        this.orderService = orderService;\\\\n    }\\\\n\\\\n    @GetMapping\\\\n    public ResponseEntity<List<Order>> getOrders() {\\\\n        return ResponseEntity.ok(orderService.getOrders());\\\\n    }\\\\n\\\\n    @GetMapping(\\\"/{id}\\\")\\\\n    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {\\\\n        return orderService.getOrderById(id)\\\\n                .map(ResponseEntity::ok)\\\\n                .orElse(ResponseEntity.notFound().build());\\\\n    }\\\\n\\\\n    @PostMapping\\\\n    public ResponseEntity<Order> createOrder(@RequestBody Order order) {\\\\n        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));\\\\n    }\\\\n}",
                      "summary": "成功生成订单控制器代码"
                    }
                    """;
            default -> """
                    {
                      "success": true,
                      "filePath": "src/main/java/com/bage/demo/controller/HealthController.java",
                      "code": "package com.bage.demo.controller;\\\\n\\\\nimport org.springframework.http.ResponseEntity;\\\\nimport org.springframework.web.bind.annotation.GetMapping;\\\\nimport org.springframework.web.bind.annotation.RequestMapping;\\\\nimport org.springframework.web.bind.annotation.RestController;\\\\nimport java.time.LocalDateTime;\\\\nimport java.util.Map;\\\\n\\\\n@RestController\\\\n@RequestMapping(\\\"/api/health\\\")\\\\npublic class HealthController {\\\\n\\\\n    @GetMapping\\\\n    public ResponseEntity<Map<String, Object>> health() {\\\\n        return ResponseEntity.ok(Map.of(\\\\n            \\\"status\\\", \\\"UP\\\",\\\\n            \\\"timestamp\\\", LocalDateTime.now().toString(),\\\\n            \\\"service\\\", \\\"demo-backend\\\"\\\\n        ));\\\\n    }\\\\n}",
                      "summary": "成功生成健康检查控制器代码"
                    }
                    """;
        };
    }

    private String generateMockCodeReview() {
        return """
                {
                  "approved": true,
                  "score": 95,
                  "issues": [],
                  "suggestions": ["代码结构良好，符合Spring Boot最佳实践"]
                }
                """;
    }

    private String generateMockTestGeneration(String userContent, String requirementType) {
        return switch (requirementType) {
            case "api-doc" -> """
                    {
                      "success": true,
                      "testFilePath": "src/test/java/com/bage/demo/controller/ApiDocControllerTest.java",
                      "testCode": "package com.bage.demo.controller;\\\\n\\\\nimport org.junit.jupiter.api.Test;\\\\nimport org.springframework.beans.factory.annotation.Autowired;\\\\nimport org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\\\\nimport org.springframework.test.web.servlet.MockMvc;\\\\nimport static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;\\\\n\\\\n@WebMvcTest(ApiDocController.class)\\\\npublic class ApiDocControllerTest {\\\\n\\\\n    @Autowired\\\\n    private MockMvc mockMvc;\\\\n\\\\n    @Test\\\\n    void getApiDocs_shouldReturnOpenApiDocument() throws Exception {\\\\n        mockMvc.perform(get(\\\"/api/docs\\\"))\\\\n                .andExpect(status().isOk())\\\\n                .andExpect(jsonPath(\\\"$.openapi\\\").value(\\\"3.0.1\\\"));\\\\n    }\\\\n}",
                      "summary": "成功生成API文档控制器测试代码"
                    }
                    """;
            case "user-management" -> """
                    {
                      "success": true,
                      "testFilePath": "src/test/java/com/bage/demo/controller/UserControllerTest.java",
                      "testCode": "package com.bage.demo.controller;\\\\n\\\\nimport org.junit.jupiter.api.Test;\\\\nimport org.springframework.beans.factory.annotation.Autowired;\\\\nimport org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\\\\nimport org.springframework.test.web.servlet.MockMvc;\\\\nimport static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\\\\n\\\\n@WebMvcTest(UserController.class)\\\\npublic class UserControllerTest {\\\\n\\\\n    @Autowired\\\\n    private MockMvc mockMvc;\\\\n\\\\n    @Test\\\\n    void getAllUsers_shouldReturnUsers() throws Exception {\\\\n        mockMvc.perform(get(\\\"/api/users\\\"))\\\\n                .andExpect(status().isOk());\\\\n    }\\\\n}",
                      "summary": "成功生成用户控制器测试代码"
                    }
                    """;
            default -> """
                    {
                      "success": true,
                      "testFilePath": "src/test/java/com/bage/demo/controller/HealthControllerTest.java",
                      "testCode": "package com.bage.demo.controller;\\\\n\\\\nimport org.junit.jupiter.api.Test;\\\\nimport org.springframework.beans.factory.annotation.Autowired;\\\\nimport org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\\\\nimport org.springframework.test.web.servlet.MockMvc;\\\\nimport static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\\\\nimport static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;\\\\n\\\\n@WebMvcTest(HealthController.class)\\\\npublic class HealthControllerTest {\\\\n\\\\n    @Autowired\\\\n    private MockMvc mockMvc;\\\\n\\\\n    @Test\\\\n    void health_shouldReturnUpStatus() throws Exception {\\\\n        mockMvc.perform(get(\\\"/api/health\\\"))\\\\n                .andExpect(status().isOk())\\\\n                .andExpect(jsonPath(\\\"$.status\\\").value(\\\"UP\\\"));\\\\n    }\\\\n}",
                      "summary": "成功生成单元测试代码"
                    }
                    """;
        };
    }

    private String generateMockDefault() {
        return "{\"success\": true, \"message\": \"Mock response generated\"}";
    }
}
