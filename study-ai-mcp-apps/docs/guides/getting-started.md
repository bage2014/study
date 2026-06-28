# 快速开始指南

## 概述

本指南将帮助您快速启动和使用 MCP Apps Todo List 应用。

## 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0

## 安装步骤

### 1. 克隆项目

```bash
git clone https://github.com/your-repo/mcp-apps-todo-list.git
cd mcp-apps-todo-list
```

### 2. 安装依赖

```bash
npm install
```

## 启动应用

### 方式一：同时启动 Server 和 Host（推荐）

```bash
npm run dev
```

该命令会启动两个服务：
- MCP Server: http://localhost:3000/mcp
- 浏览器预览 Host: http://localhost:5173

### 方式二：分别启动

```bash
# 启动 MCP Server（终端1）
npm run server

# 启动浏览器预览 Host（终端2）
npm run host
```

## 使用方法

### 访问浏览器预览 Host

打开浏览器，访问 http://localhost:5173

### 使用工具

1. **Open Todo UI**: 打开交互式待办事项管理界面
2. **List Todos**: 获取所有待办事项列表
3. **Add Todo**: 添加新待办事项（需要输入 text 参数）
4. **Toggle Todo**: 切换待办事项状态（需要输入 id 参数）
5. **Delete Todo**: 删除待办事项（需要输入 id 参数）

### 使用交互式 UI

1. 点击 "Open Todo UI" 按钮
2. 在 UI Preview 区域会显示待办事项管理界面
3. 在输入框中输入待办事项内容，点击 "Add" 按钮添加
4. 点击复选框切换完成状态
5. 点击 "Delete" 按钮删除待办事项

## 测试 API

### 使用 curl 测试

```bash
# 获取待办事项列表
curl -X POST http://localhost:3000/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"listTodos","arguments":{}}}'

# 添加待办事项
curl -X POST http://localhost:3000/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"addTodo","arguments":{"text":"Buy milk"}}}'

# 切换待办事项状态
curl -X POST http://localhost:3000/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"toggleTodo","arguments":{"id":1}}}'

# 删除待办事项
curl -X POST http://localhost:3000/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"deleteTodo","arguments":{"id":1}}}'

# 获取 UI 资源
curl -X POST http://localhost:3000/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"getUI","arguments":{}}}'
```

## 开发指南

### 修改待办事项存储

编辑 `src/server/todoStore.ts` 文件来自定义数据存储逻辑。

```typescript
// 示例：修改存储实现
class TodoStore {
  // 添加新方法或修改现有方法
}
```

### 修改 UI 界面

编辑 `src/server/index.ts` 中的 HTML 模板来自定义待办事项管理界面。

```typescript
// 找到 getUI 工具中的 HTML 模板
// 修改样式、布局或交互逻辑
```

### 添加新工具

在 `src/server/index.ts` 中注册新工具：

```typescript
server.registerTool({
  name: 'myTool',
  title: '我的工具',
  description: '描述工具功能',
  inputSchema: z.object({
    // 输入参数
  }),
  handler: async (args) => {
    // 工具逻辑
    return {
      content: [{ type: 'text', text: '结果' }],
    };
  },
});
```

## 构建生产版本

```bash
npm run build
```

构建产物会生成在 `dist/` 目录中。

## 部署说明

### 本地部署

```bash
npm start
```

### 生产部署

1. 将 `dist/` 目录部署到静态服务器
2. 启动 MCP Server：`node dist/server/index.js`
3. 配置反向代理，将 `/mcp` 请求转发到 MCP Server

## 常见问题

### Q: 端口被占用怎么办？

修改 `src/server/index.ts` 中的端口号：

```typescript
const PORT = 3001; // 修改为其他端口
```

### Q: 待办事项数据丢失？

当前使用内存存储，重启服务器后数据会丢失。如需持久化，建议添加数据库支持。

### Q: UI 界面无法正常交互？

确保浏览器预览 Host 和 MCP Server 都已启动，并且网络连接正常。

## 参考文档

- [SEP-1865 MCP Apps 规范](https://modelcontextprotocol.io/seps/1865-mcp-apps-interactive-user-interfaces-for-mcp)
- [MCP SDK 文档](https://modelcontextprotocol.io/docs)
- [React 文档](https://react.dev/)
- [Vite 文档](https://vitejs.dev/)