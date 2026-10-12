---
name: "common-technical-writer"
description: "技术文档专家技能，生成标准化 README、API 文档、技术教程，支持中英文互译（参考自 shubhamsabooapps@technical-writer）"
trigger: "需要写项目 README、接口文档、技术文档、或翻译技术文档时"
disable-when: "只有纯代码改动，不需要文档"
category: "common"
tags: ["documentation", "readme", "api-doc", "technical-writing", "translation"]
requires: ["common-api-doc", "common-spec-driven"]
---

# 技术文档专家技能

## 功能描述

生成符合行业标准的技术文档，覆盖项目 README、API 接口文档、技术教程/指南，以及中英文技术文档互译。

## 触发条件

- 需要写项目 README 时
- 接口文档或技术文档需要补充时
- 需要中英文技术文档互译时

## 何时使用

- 新项目创建 README
- API 接口文档编写
- 功能完成后补充文档
- 技术方案/架构文档
- 技术文档中译英或英译中

## 核心功能

- **标准 README 生成**：包含安装/使用/贡献/许可证等标准模块
- **RESTful API 文档**：请求/响应/错误码完整覆盖
- **技术教程/用户指南**：分步操作、FAQ、延伸阅读
- **中英文互译**：技术术语保留、代码块不翻译
- **反模式检查**：避免营销软文、不一致、漏示例等

### 1. 标准 README 生成

标准结构：

```markdown
# 项目名

> 一句话描述项目做什么

## 功能特性
- 特性 1
- 特性 2

## 快速开始
### 环境要求
### 安装
### 运行

## 使用指南
### 基础用法
### 进阶用法

## API 文档
（链接或嵌入）

## 架构设计
（可选，复杂项目需要）

## 贡献指南
## 许可证
```

### 2. RESTful API 文档

标准结构：

```markdown
## API 名称

**Endpoint:** `POST /api/v1/resource`

**描述:** 做什么的

**请求体:**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|

**请求示例:**
```json
{
  "key": "value"
}
```

**成功响应 (200):**
```json
{
  "code": 0,
  "data": {}
}
```

**错误响应:**
| 状态码 | 说明 |
|--------|------|
| 400 | 参数错误 |
| 401 | 未授权 |
```

### 3. 技术教程/用户指南

结构模板：

```markdown
# 教程标题

## 适用场景
## 前置条件
## 步骤 1: ...
## 步骤 2: ...
## 常见问题 FAQ
## 延伸阅读
```

### 4. 中英文文档互译

**翻译原则：**
- 技术术语保持一致性（如 Redis、Docker 不翻译）
- 代码块、变量名、命令不翻译
- 代码注释和自然语言内容翻译
- 保留原文的 Markdown 格式
- 中文用简体，英文用美式

## 写作规范

### 代码示例

- 每个代码块必须有语言标记
- 短代码用 inline code，长代码用 fenced block
- 关键部分加注释

### 表格和列表

- API 参数用表格（清晰）
- 特性列表用 bullet list（易扫读）
- 操作步骤用 numbered list（有序）

### 链接和引用

- 内部链接用相对路径
- 外部链接用完整 URL
- 参考文献集中放在末尾

## 避免的反模式

- ❌ README 写得像营销软文
- ❌ API 文档不写示例请求/响应
- ❌ 中英文混用（同一文档要么全中要么全英）
- ❌ 代码块不标语言
- ❌ 文档和代码不一致（写完后要同步检查）

## 与现有 skill 的关系

- **common-api-doc**：API 文档专用，本 skill 是通用技术文档的上层覆盖
- **common-coding**：文档中的代码示例参考编码规范
- **common-spec-driven**：Spec 文档需要本 skill 的写作能力

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| docType | String | 是 | 文档类型：readme/api/tutorial/translation |
| target | String | 否 | 要覆盖的文件/模块/接口 |
| language | String | 否 | 输出语言：zh/en，默认跟随输入 |

## 输出格式

按 docType 生成对应的 Markdown 文档，示例：

```markdown
# UserService

> 用户服务模块，提供用户注册/登录/管理等 API

## 功能特性
- JWT 鉴权
- 用户 CRUD
- 角色权限

## 快速开始

### 环境要求
- Java 17+
- MySQL 8.0+

### 安装
```bash
./gradlew build
```

### 运行
```bash
./gradlew bootRun
```

## API 文档

### POST /api/v1/users
注册新用户...

## 贡献指南

1. Fork 本仓库
2. 创建特性分支
3. 提交 PR
```

## 参考来源

文章中提到的 `shubhamsabooapps@technical-writer`（GitHub 地址未公开）
