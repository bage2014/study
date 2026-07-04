---
name: "mcp-apps-project-standard"
description: "MCP Apps项目规范 - 后端驱动UI渲染架构。所有页面由后端生成HTML通过MCP协议返回渲染。Invoke when developing or maintaining MCP Apps projects, especially when creating new pages, tools, or stores."
---

# MCP Apps 项目规范

## 核心架构原则

### 后端驱动UI渲染（MVP模式）

- **Model（数据层）**：后端存储和管理所有业务数据
- **View（视图层）**：前端仅负责渲染，不包含业务逻辑
- **Presenter（展示层）**：后端生成完整的HTML页面，通过MCP协议返回

### 数据流向

```
用户操作 → iframe → postMessage → MCP Host → MCP Tool Call → 后端处理 → 返回结果
                                                              │
                                                              ▼
                                                     更新数据/生成新UI
                                                              │
                                                              ▼
                                                     返回UI Resource → 重新渲染iframe
```

## 目录结构

```
src/
├── components/           # 前端组件（仅Host）
│   └── MCPHost.tsx      # MCP客户端Host
├── server/              # 后端服务
│   ├── tools/           # MCP工具定义
│   ├── ui/              # UI页面生成器（HTML模板）
│   ├── stores/          # 数据存储层
│   └── index.ts         # 服务器入口
├── shared/              # 共享类型定义
├── App.tsx              # 前端入口
└── main.tsx             # React挂载点
```

## 工具命名规范

| 工具类型 | 命名格式 | 示例 |
|---------|---------|------|
| UI获取 | `get{PageName}UI` | `getFamilyTreeUI`, `getMemberManageUI` |
| 数据查询 | `list{Entity}`, `get{Entity}` | `listFamilies`, `getMemberById` |
| 数据创建 | `create{Entity}` | `createFamily`, `createMember` |
| 数据更新 | `update{Entity}` | `updateFamily`, `updateMember` |
| 数据删除 | `delete{Entity}` | `deleteFamily`, `deleteMember` |

## UI页面生成规范

### HTML模板结构

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>页面标题</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>/* 自定义样式 */</style>
</head>
<body class="bg-gray-100 min-h-screen p-4">
  <!-- 页面内容 -->
  <script>
    function mcpCallTool(toolName, params) {
      return new Promise((resolve, reject) => {
        const messageId = 'msg-' + Date.now() + '-' + Math.random();
        const handler = (event) => {
          if (event.data && event.data.messageId === messageId) {
            window.removeEventListener('message', handler);
            if (event.data.error) {
              reject(new Error(event.data.error));
            } else {
              resolve(event.data.response);
            }
          }
        };
        window.addEventListener('message', handler);
        window.parent.postMessage({
          messageId: messageId,
          type: 'tool',
          payload: { toolName, params }
        }, '*');
      });
    }
  </script>
</body>
</html>
```

### 样式规范

- **主色调**：绿色系列 (`#10B981`)
- **辅助色**：蓝色系列 (`#3B82F6`)
- **成功色**：`#10B981`
- **警告色**：`#F59E0B`
- **错误色**：`#EF4444`

### JavaScript规范

1. **防抖机制**：防止重复请求导致界面闪烁
2. **innerHTML渲染**：使用innerHTML一次性渲染，优化性能
3. **错误处理**：完善的错误捕获和用户提示

## 开发工作流

1. 创建数据存储（Store）
2. 创建MCP工具（Tools）
3. 创建UI页面（HTML）
4. 注册工具到Server
5. 更新前端菜单
6. 测试验证

## 质量保证

- 使用严格模式TypeScript
- 使用Zod进行参数验证
- 完善的错误处理
- 防抖机制防止闪烁
- 输入验证防止XSS