---
name: "personal-project-runner"
description: "项目启动管理技能，支持Java Spring Boot和Vue项目的启动、重启、关闭操作，保持原有端口不变"
trigger: "需要启动、重启或关闭项目时"
disable-when: "项目已停止且无需启动时"
category: "personal"
tags: ["runner", "start", "restart", "stop", "java", "vue", "spring", "port"]
---

# personal-project-runner

## 功能描述

项目启动管理技能，支持 Java Spring Boot 和 Vue 项目的启动、重启、关闭操作。自动检测项目类型，保持原有端口不变，支持后台运行和日志查看。

## 触发条件

- 需要启动项目时
- 需要重启项目时（已启动则先停止再启动）
- 需要关闭项目时
- 需要查看项目运行状态时

## 何时使用

- 开发过程中需要频繁启动/重启项目时
- 项目启动失败需要排查时
- 需要保持端口不变的重启时
- 需要后台运行项目时

## 何时不使用

- 项目已正常运行且无需操作时
- 需要修改端口配置时（请先修改配置文件）
- 仅需要查看日志时（可直接查看日志文件）

## 核心功能

### 1. 项目类型检测

- 自动识别 Java Spring Boot 项目（检测 pom.xml、build.gradle）
- 自动识别 Vue 项目（检测 package.json、vue.config.js）
- 支持自定义项目类型配置

### 2. 端口检测与管理

- 自动检测项目配置的端口号
- 启动前检查端口是否被占用
- 保持原有端口不变，不自动修改配置

### 3. 启动操作

- 若项目未启动：直接启动
- 若项目已启动：先停止再启动，提示"已重新启动"

### 4. 关闭操作

- 安全停止正在运行的进程
- 支持按端口号停止
- 支持按进程名停止

### 5. 状态监控

- 查看项目运行状态
- 查看端口占用情况
- 查看进程信息

## 支持的项目类型

### Java Spring Boot

| 特征 | 说明 |
|------|------|
| 配置文件 | `application.yml`、`application.properties` |
| 构建工具 | Maven（pom.xml）、Gradle（build.gradle） |
| 默认端口 | 8080 |
| 启动命令 | `mvn spring-boot:run`、`java -jar xxx.jar` |

### Vue

| 特征 | 说明 |
|------|------|
| 配置文件 | `package.json`、`vue.config.js`、`vite.config.js` |
| 构建工具 | npm、yarn、pnpm |
| 默认端口 | 8080（Vue CLI）、5173（Vite） |
| 启动命令 | `npm run serve`、`npm run dev`、`pnpm dev` |

## 操作流程

### 启动流程

```
检测项目类型 → 读取端口配置 → 检查端口占用 → 启动项目 → 验证启动成功
```

### 重启流程

```
检测运行状态 → 若已运行则停止 → 等待进程退出 → 启动项目 → 提示"已重新启动"
```

### 关闭流程

```
检测运行状态 → 若已运行则停止 → 等待进程退出 → 提示"已关闭"
```

## 输入参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| action | String | 是 | 操作类型：start/restart/stop/status |
| projectPath | String | 否 | 项目路径，默认当前目录 |
| projectType | String | 否 | 项目类型：java/vue，自动检测时留空 |
| port | Integer | 否 | 端口号，自动检测时留空 |
| mode | String | 否 | 运行模式：foreground/background，默认 background |
| buildTool | String | 否 | 构建工具：maven/gradle/npm/yarn/pnpm |

## 输出格式

```json
{
  "action": "restart",
  "projectType": "java",
  "port": 8080,
  "status": "success",
  "message": "项目已重新启动，端口：8080",
  "processId": 12345,
  "logs": "/path/to/logs",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

## 状态码定义

| 状态码 | 说明 |
|--------|------|
| `running` | 项目正在运行 |
| `stopped` | 项目已停止 |
| `starting` | 项目启动中 |
| `stopping` | 项目停止中 |
| `not_found` | 未检测到项目类型 |
| `port_conflict` | 端口冲突 |
| `build_failed` | 构建失败 |

## 端口检测规则

### Java Spring Boot 端口读取

按优先级读取：
1. `application.yml` 中的 `server.port`
2. `application.properties` 中的 `server.port`
3. 默认值：8080

### Vue 端口读取

按优先级读取：
1. `vue.config.js` 中的 `devServer.port`
2. `vite.config.js` 中的 `server.port`
3. `package.json` scripts 中的端口参数
4. 默认值：Vue CLI 8080，Vite 5173

## 进程管理

### 查找进程

```bash
# 按端口查找
lsof -i :8080 -t

# 按进程名查找
pgrep -f "spring-boot"
pgrep -f "node"
```

### 停止进程

```bash
# 优雅停止（优先）
kill -TERM <pid>

# 强制停止（超时后）
kill -9 <pid>
```

## 使用流程

### 启动项目

```bash
# 自动检测项目类型和端口
skills-run start

# 指定项目路径
skills-run start --project-path /path/to/project

# 指定项目类型
skills-run start --project-type java
skills-run start --project-type vue
```

### 重启项目

```bash
# 自动检测并重启
skills-run restart

# 指定项目路径和类型
skills-run restart --project-path /path/to/project --project-type java
```

### 关闭项目

```bash
# 自动检测并关闭
skills-run stop

# 指定端口关闭
skills-run stop --port 8080
```

### 查看状态

```bash
# 查看当前目录项目状态
skills-run status

# 查看指定端口状态
skills-run status --port 8080
```

## 后台运行配置

### Java Spring Boot

```bash
# 后台运行并输出日志
nohup mvn spring-boot:run > logs/console.log 2>&1 &

# 或使用 jar 包
nohup java -jar target/app.jar > logs/console.log 2>&1 &
```

### Vue

```bash
# 后台运行
nohup npm run serve > logs/console.log 2>&1 &

# 或使用 pm2
pm2 start npm -- run serve
```

## 日志管理

### 日志目录结构

```
project-root/
├── logs/
│   ├── console.log       # 控制台日志
│   ├── spring.log        # Spring Boot 日志
│   └── error.log         # 错误日志
```

### 查看日志

```bash
# 实时查看日志
tail -f logs/console.log

# 查看最后100行
tail -n 100 logs/console.log

# 搜索错误
grep -i error logs/console.log
```

## 最佳实践

1. **端口优先**：启动前检查端口占用，避免启动失败
2. **优雅停止**：先尝试 `kill -TERM`，超时后再强制停止
3. **后台运行**：开发环境建议后台运行，避免终端关闭导致进程终止
4. **日志输出**：确保日志输出到文件，方便排查问题
5. **环境检查**：启动前验证 JDK/Node 版本是否符合项目要求

## 配置要求

无需额外配置，自动检测项目类型和端口。

## 扩展指南

### 添加新项目类型

1. 在项目类型检测逻辑中添加新类型识别
2. 添加对应的端口读取规则
3. 添加启动/停止命令
4. 更新文档说明

### 自定义启动命令

在项目根目录创建 `.runner.json` 配置文件：

```json
{
  "projectType": "java",
  "port": 8080,
  "buildTool": "maven",
  "startCommand": "mvn spring-boot:run",
  "stopPattern": "spring-boot"
}
```

## 常见问题

### Q: 端口被占用怎么办？

A: 先停止占用端口的进程，或修改项目配置文件中的端口号。本技能不会自动修改端口配置。

### Q: 如何确认项目是否启动成功？

A: 检查进程是否存在，访问 http://localhost:port 确认服务响应。

### Q: 后台运行后如何停止？

A: 使用 `skills-run stop` 或手动 `kill` 进程。

### Q: 日志文件在哪里？

A: 默认在项目根目录的 `logs/console.log`。

参考经验：启动/重启项目时，先识别并结束旧进程（按端口/进程名），再确认运行环境，最后启动项目。
