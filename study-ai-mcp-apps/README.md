# MCP Apps Todo List

基于 SEP-1865 MCP Apps 规范的待办事项管理应用，实现交互式用户界面与 MCP Server 的双向通信。

## 项目简介

本项目是一个完整的 MCP Apps 实现示例，包含：
- MCP Server：提供待办事项管理的工具接口
- 浏览器预览 Host：展示交互式 UI 并与 Server 通信
- 支持添加、查询、更新、删除待办事项

## 技术栈

- **框架**: React 18 + Express
- **构建工具**: Vite 5
- **语言**: TypeScript
- **MCP SDK**: @modelcontextprotocol/sdk
- **MCP UI**: @mcp-ui/server, @mcp-ui/client

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动项目

```bash
npm run dev
```

该命令会同时启动：
- MCP Server: http://localhost:3000/mcp
- 浏览器预览 Host: http://localhost:5173

### 其他命令

```bash
# 仅启动 MCP Server
npm run server

# 仅启动浏览器预览 Host
npm run host

# 构建项目
npm run build

# 生产环境启动
npm start
```

## 项目结构

```
.
├── src/
│   ├── components/          # React 组件
│   │   └── MCPHost.tsx     # MCP 客户端组件
│   ├── server/             # MCP Server 代码
│   │   ├── index.ts        # Server 入口
│   │   └── todoStore.ts    # 待办事项存储
│   ├── App.tsx             # 主应用组件
│   └── main.tsx            # React 入口
├── public/                 # 静态资源
├── index.html              # HTML 模板
├── package.json            # 依赖配置
├── tsconfig.json           # TypeScript 配置
├── vite.config.ts          # Vite 配置
├── .gitignore              # Git 忽略规则
├── .traeignore             # Trae 技能系统忽略规则
└── README.md               # 项目说明文档
```

## API 接口

### MCP Server Endpoint

- **POST/GET** `http://localhost:3000/mcp`

### 可用工具

| 工具名称 | 描述 | 参数 |
|----------|------|------|
| `listTodos` | 获取所有待办事项 | 无 |
| `addTodo` | 添加新待办事项 | `text`: string |
| `toggleTodo` | 切换待办事项状态 | `id`: number |
| `deleteTodo` | 删除待办事项 | `id`: number |
| `getUI` | 获取交互式 UI 资源 | 无 |

## 开发指南

### 修改待办事项存储

编辑 `src/server/todoStore.ts` 文件来自定义数据存储逻辑。

### 修改 UI 界面

编辑 `src/server/index.ts` 中的 HTML 模板来自定义待办事项管理界面。

### 自定义工具

在 `src/server/index.ts` 中使用 `server.registerTool()` 方法注册新工具。

## 部署说明

### 构建生产版本

```bash
npm run build
```

### 启动生产服务器

```bash
npm start
```

## MCP Apps 规范

本项目遵循 SEP-1865 MCP Apps 规范：
- 使用 `ui://` URI 方案声明 UI 资源
- 通过 MCP JSON-RPC 协议进行双向通信
- 支持 Streamable HTTP Transport
- 使用 postMessage 实现 iframe 与宿主通信

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## 许可证

MIT License