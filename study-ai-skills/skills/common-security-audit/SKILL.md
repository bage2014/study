---
name: "common-security-audit"
description: "安全审计技能，提供安全漏洞扫描、OWASP Top 10 检查、渗透测试指导和安全加固方案"
trigger: "需要进行安全审计、漏洞扫描或安全检查时"
disable-when: "已有完善的安全审计流程或第三方安全团队负责时"
category: "common"
tags: ["security", "audit", "vulnerability", "owasp", "penetration-testing"]
requires: ["common-code-review"]
---

# common-security-audit

## 功能描述

安全审计技能，用于系统性地检查和评估应用程序的安全性。覆盖 OWASP Top 10 漏洞、配置安全、依赖安全、数据安全等多个维度，提供自动化扫描工具和手动检查指南。

## 何时使用

在以下情况调用此技能：
- **代码审查**：检查代码中的安全隐患（SQL 注入、XSS、CSRF 等）
- **上线前审计**：新功能/模块上线前的安全检查
- **依赖更新**：检查第三方依赖的已知漏洞
- **合规要求**：满足安全合规标准（如 GDPR、PCI DSS）
- **渗透测试**：模拟攻击者进行安全测试
- **安全加固**：修复已知漏洞后的验证

## 核心功能

### 1. OWASP Top 10 检查清单

| 风险等级 | 漏洞类型 | 检查项 | 工具/方法 |
|----------|----------|--------|-----------|
| **P0** | A01:2021 - Broken Access Control | 权限绕过、水平/垂直越权 | 手动测试 + 自动化脚本 |
| **P0** | A02:2021 - Cryptographic Failures | 敏感数据明文存储、弱加密算法 | 代码扫描 + 配置检查 |
| **P0** | A03:2021 - Injection | SQL 注入、命令注入、LDAP 注入 | SAST 工具 + 手工测试 |
| **P1** | A04:2021 - Insecure Design | 架构设计缺陷、业务逻辑漏洞 | 威胁建模 + 设计评审 |
| **P1** | A05:2021 - Security Misconfiguration | 默认密码、调试接口暴露、错误信息泄露 | 配置扫描 + 渗透测试 |
| **P1** | A06:2021 - Vulnerable Components | 依赖库已知漏洞 | SCA 工具（Snyk/Dependabot） |
| **P2** | A07:2021 - Authentication Failures | 弱密码策略、会话固定、暴力破解 | 渗透测试 + 日志分析 |
| **P2** | A08:2021 - Software Integrity Failures | CI/CD 管道篡改、未签名组件 | 供应链安全检查 |
| **P2** | A09:2021 - Logging & Monitoring Failures | 日志缺失、告警失效 | 日志审计 + 监控检查 |
| **P2** | A10:2021 - SSRF | 服务端请求伪造 | 代码审计 + 网络隔离检查 |

### 2. 自动化扫描工具推荐

#### Java/Spring Boot 项目

```bash
# 1. 依赖漏洞扫描（SCA）
mvn org.owasp:dependency-check-maven:check

# 2. 静态代码分析（SAST）
# SonarQube + Security Plugin
# SpotBugs + FindSecBugs
mvn spotbugs:check

# 3. API 安全测试
# OWASP ZAP 主动扫描
zap.sh -cmd -quickurl http://localhost:8080 -quickout report.html
```

#### Vue/前端项目

```bash
# 1. 依赖漏洞扫描
npm audit --audit-level=high

# 2. XSS 检测
# eslint-plugin-security
npm install --save-dev eslint-plugin-security

# 3. CSP 头检查
curl -I https://your-app.com | grep Content-Security-Policy
```

### 3. 手动检查清单

#### 输入验证

- [ ] 所有用户输入是否经过白名单验证？
- [ ] 文件上传是否限制类型、大小、扩展名？
- [ ] URL 参数是否经过编码/转义？
- [ ] JSON/XML 解析是否禁用外部实体（XXE）？

#### 认证授权

- [ ] 密码是否使用强哈希算法（BCrypt/Argon2）？
- [ ] 会话 Token 是否随机且不可预测？
- [ ] 权限检查是否在服务端执行（非仅前端）？
- [ ] 敏感操作是否需要二次确认？

#### 数据安全

- [ ] 敏感数据（密码、Token、PII）是否加密存储？
- [ ] 数据库连接是否使用参数化查询？
- [ ] 日志中是否脱敏敏感信息？
- [ ] HTTPS 是否强制启用（HSTS）？

#### 配置安全

- [ ] 生产环境是否禁用调试模式？
- [ ] 默认账户/密码是否已修改？
- [ ] 错误页面是否隐藏堆栈信息？
- [ ] CORS 策略是否最小化允许源？

### 4. 渗透测试流程

```
1. 信息收集 → 端口扫描、服务识别、目录枚举
   ↓
2. 漏洞探测 → 自动化工具扫描 + 手工测试
   ↓
3. 漏洞利用 → 验证漏洞可被利用（PoC）
   ↓
4. 影响评估 → 数据泄露范围、业务影响
   ↓
5. 修复建议 → 优先级排序 + 具体修复方案
   ↓
6. 复测验证 → 确认漏洞已修复
```

### 5. 安全加固方案

#### 高优先级（P0）

1. **SQL 注入防护**
   ```java
   //  错误：拼接 SQL
   String sql = "SELECT * FROM users WHERE name = '" + name + "'";
   
   // ✅ 正确：参数化查询
   @Query("SELECT u FROM User u WHERE u.name = :name")
   List<User> findByName(@Param("name") String name);
   ```

2. **XSS 防护**
   ```javascript
   // ❌ 错误：直接渲染用户输入
   element.innerHTML = userInput;
   
   // ✅ 正确：转义后渲染
   element.textContent = userInput;
   // 或使用框架内置转义（Vue/Angular/React 默认转义）
   ```

3. **CSRF 防护**
   ```java
   // Spring Security 自动启用 CSRF
   @Configuration
   public class SecurityConfig extends WebSecurityConfigurerAdapter {
       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
       }
   }
   ```

#### 中优先级（P1）

4. **敏感数据加密**
   ```java
   // 使用 Jasypt 加密配置文件中的敏感信息
   spring.datasource.password=ENC(encrypted_password_here)
   ```

5. **依赖漏洞修复**
   ```bash
   # 定期运行依赖检查
   mvn dependency-check:update
   mvn dependency-check:check
   
   # 升级有漏洞的依赖
   mvn versions:display-dependency-updates
   ```

#### 低优先级（P2）

6. **安全头配置**
   ```nginx
   # Nginx 安全头
   add_header X-Content-Type-Options "nosniff";
   add_header X-Frame-Options "DENY";
   add_header X-XSS-Protection "1; mode=block";
   add_header Content-Security-Policy "default-src 'self'";
   add_header Strict-Transport-Security "max-age=31536000; includeSubDomains";
   ```

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| projectType | String | 是 | 项目类型（backend/frontend/fullstack） |
| framework | String | 否 | 使用的框架（Spring Boot/Vue/React等） |
| scanLevel | String | 否 | 扫描级别（quick/full/penetration），默认 quick |
| outputFormat | String | 否 | 输出格式（json/html/markdown），默认 markdown |

## 输出格式

```markdown
# 安全审计报告

## 执行摘要
- 扫描时间：2026-10-31
- 扫描范围：[列出模块]
- 总体风险等级：[高/中/低]

## 漏洞统计
| 风险等级 | 数量 | 占比 |
|----------|------|------|
| P0（严重） | X | X% |
| P1（高危） | X | X% |
| P2（中危） | X | X% |
| P3（低危） | X | X% |

## 详细发现

### [漏洞名称] - P0/P1/P2

**描述**：[漏洞描述]

**影响**：[潜在影响]

**位置**：[文件路径/URL]

**复现步骤**：
1. ...
2. ...

**修复建议**：
- [具体修复方案]
- [代码示例]

**参考**：
- OWASP: [链接]
- CVE: [编号]

## 修复优先级

1. [P0 漏洞列表]
2. [P1 漏洞列表]
3. [P2 漏洞列表]

## 附录

- 扫描工具版本
- 完整扫描日志
```

## 使用流程

### 快速扫描（Quick Scan）

1. 运行依赖漏洞扫描（SCA）
2. 运行静态代码分析（SAST）
3. 检查 OWASP Top 10 高风险项
4. 生成简要报告

### 全面审计（Full Audit）

1. 执行快速扫描所有步骤
2. 手动检查配置安全
3. 渗透测试关键接口
4. 检查日志和监控
5. 生成详细报告 + 修复方案

### 渗透测试（Penetration Test）

1. 信息收集（端口、服务、目录）
2. 漏洞探测（自动 + 手工）
3. 漏洞利用验证（PoC）
4. 影响评估
5. 修复建议 + 复测

## 失败案例

| 错误场景 | 错误信息 | 解决方案 |
|----------|----------|----------|
| 依赖扫描超时 | `Dependency check timeout` | 增加 Maven 超时设置或离线模式 |
| SAST 误报过多 | 大量 false positive | 配置规则白名单，聚焦 P0/P1 |
| 渗透测试阻塞 | WAF/防火墙拦截 | 申请测试白名单或使用 staging 环境 |
| 敏感数据泄露 | 扫描结果包含密钥 | 立即撤销密钥，清理扫描日志 |

## 最佳实践

1. **左移安全**：在 CI/CD 流水线集成安全扫描
2. **定期扫描**：每周运行依赖检查，每月全面审计
3. **漏洞管理**：建立漏洞跟踪系统（Jira/GitHub Issues）
4. **安全培训**：团队成员定期进行安全意识培训
5. **应急响应**：制定安全事件应急预案

## 配置要求

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| SECURITY_SCAN_LEVEL | 扫描级别 | quick |
| OWASP_ZAP_HOME | OWASP ZAP 安装路径 | /opt/zap |
| SONAR_HOST_URL | SonarQube 地址 | http://localhost:9000 |

### Maven 插件配置

```xml
<!-- pom.xml -->
<build>
    <plugins>
        <!-- OWASP Dependency Check -->
        <plugin>
            <groupId>org.owasp</groupId>
            <artifactId>dependency-check-maven</artifactId>
            <version>8.4.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>check</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        
        <!-- SpotBugs + FindSecBugs -->
        <plugin>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs-maven-plugin</artifactId>
            <version>4.7.3.0</version>
            <dependencies>
                <dependency>
                    <groupId>com.h3xstream.findsecbugs</groupId>
                    <artifactId>findsecbugs-plugin</artifactId>
                    <version>1.12.0</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

## 扩展指南

### 添加自定义安全规则

1. 创建 `.security-rules.yml` 文件
2. 定义项目特定的安全检查项
3. 集成到 CI/CD 流水线

### 集成第三方安全工具

- **Snyk**：依赖漏洞扫描 + 容器安全
- **SonarQube**：代码质量 + 安全扫描
- **Burp Suite**：专业渗透测试工具
- **Nessus**：基础设施漏洞扫描

## 触发条件

- **前置条件**：项目可编译/可运行
- **环境要求**：Java 21+, Node.js 18+, Maven 3.8+
- **数据准备**：无特殊要求
- **触发方式**：API 调用 / CLI 执行 / CI/CD 触发
