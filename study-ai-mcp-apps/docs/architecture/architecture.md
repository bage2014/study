# MCP Apps Todo List 架构文档

## 概述

本项目是一个基于 SEP-1865 MCP Apps 规范的待办事项管理应用，实现了交互式用户界面与 MCP Server 的双向通信。

## 架构设计

### 整体架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        用户浏览器                               │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              浏览器预览 Host (React)                     │   │
│  │  ┌─────────────────┐  ┌─────────────────────────────┐  │   │
│  │  │  MCPHost 组件   │  │   UIResourceRenderer        │  │   │
│  │  │  (工具调用)      │  │   (iframe 渲染 UI)          │  │   │
│  │  └────────┬────────┘  └─────────────┬───────────────┘  │   │
│  │           │                         │                  │   │
│  │           ▼                         ▼                  │   │
│  │  ┌─────────────────┐  ┌─────────────────────────────┐  │   │
│  │  │   fetch 请求    │  │    postMessage 通信         │  │   │
│  │  └────────┬────────┘  └─────────────┬───────────────┘  │   │
│  └───────────┼─────────────────────────┼──────────────────┘   │
└──────────────┼─────────────────────────┼───────────────────────┘
               │                         │
               ▼                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                     MCP Server (Express)                       │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              McpServer (@modelcontextprotocol/sdk)        │  │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────┐ │  │
│  │  │listTodos│ │ addTodo │ │toggleTodo││deleteTodo││ getUI│ │  │
│  │  └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘ └───┬──┘ │  │
│  │       │           │           │           │           │    │  │
│  │       ▼           ▼           ▼           ▼           ▼    │  │
│  │  ┌───────────────────────────────────────────────────────┐  │  │
│  │  │                  TodoStore (内存存储)                  │  │  │
│  │  └───────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### 组件职责

| 组件 | 职责 | 技术栈 |
|------|------|--------|
| MCPHost | MCP 客户端组件，管理连接和工具调用 | React + @modelcontextprotocol/sdk |
| UIResourceRenderer | UI 资源渲染器，通过 iframe 展示 HTML | @mcp-ui/client |
| McpServer | MCP Server 核心，注册和处理工具调用 | @modelcontextprotocol/sdk |
| TodoStore | 待办事项数据存储，提供 CRUD 操作 | TypeScript |

### 数据流

#### 工具调用流程

1. 用户在浏览器预览 Host 中点击工具按钮
2. MCPHost 组件通过 `fetch` 发送 JSON-RPC 请求到 `/mcp` 端点
3. MCP Server 接收请求，调用对应的工具函数
4. TodoStore 执行数据操作
5. MCP Server 返回 SSE 格式响应
6. MCPHost 解析响应，更新 UI

#### UI 交互流程

1. 用户点击 "Open Todo UI" 按钮
2. MCPHost 调用 `getUI` 工具获取 HTML UI 资源
3. UIResourceRenderer 在 iframe 中渲染 HTML
4. 用户在 iframe 中操作（添加/切换/删除待办事项）
5. iframe 通过 `postMessage` 发送消息到宿主
6. MCPHost 监听消息，调用相应工具
7. 工具执行成功后，iframe 自动刷新待办事项列表

## 技术选型

### 前端框架

- **React 18**: 用于构建浏览器预览 Host 的用户界面
- **Vite 5**: 快速的前端构建工具，支持热更新
- **TypeScript**: 类型安全的编程语言

### MCP 协议

- **@modelcontextprotocol/sdk**: MCP 协议核心 SDK，提供 Server 和 Client 实现
- **@mcp-ui/server**: MCP UI 资源服务器端支持
- **@mcp-ui/client**: MCP UI 资源客户端渲染

### 后端框架

- **Express**: 轻量级 Node.js Web 框架，用于构建 MCP Server
- **Zod**: TypeScript 输入验证库

## 目录结构

```
src/
├── components/              # React 组件
│   └── MCPHost.tsx         # MCP 客户端核心组件
├── server/                  # MCP Server 代码
│   ├── index.ts            # Server 入口，注册工具和路由
│   └── todoStore.ts        # 待办事项数据存储
├── App.tsx                 # 主应用组件
└── main.tsx               # React 应用入口

docs/
├── api/                    # API 文档
│   └── openapi.yaml        # OpenAPI 规范
├── architecture/           # 架构文档
│   └── architecture.md     # 架构说明
└── guides/                 # 使用指南
    └── getting-started.md  # 快速开始指南
```

## 关键设计决策

### 无状态模式

MCP Server 采用无状态模式，为每个请求创建新的 `McpServer` 实例和 `StreamableHTTPServerTransport`。这简化了会话管理，避免了连接复用问题。

### iframe 通信

UI 资源通过 iframe 渲染，使用 `postMessage` API 实现双向通信。这种方式隔离了 UI 资源和宿主环境，提高了安全性。

### SSE 响应

MCP Server 使用 Server-Sent Events (SSE) 格式返回响应，支持流式传输，符合 MCP Streamable HTTP Transport 规范。

## 扩展建议

### 数据库持久化

当前 TodoStore 使用内存存储，重启后数据会丢失。建议添加数据库支持：

- SQLite: 轻量级嵌入式数据库
- PostgreSQL: 生产环境数据库

### 用户认证

添加用户认证功能，支持多用户使用：

- JWT 令牌认证
- OAuth 2.0 集成

### 实时同步

使用 WebSocket 实现实时同步，多个客户端可以共享待办事项列表：

- Socket.IO
- WebSocket API