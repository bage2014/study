# MCP Apps 项目规范

## 1. 核心架构原则

### 1.1 后端驱动UI渲染（MVP模式）

本项目采用**Model-View-Presenter**架构，核心原则是：

- **Model（数据层）**：后端存储和管理所有业务数据
- **View（视图层）**：前端仅负责渲染，不包含业务逻辑
- **Presenter（展示层）**：后端生成完整的HTML页面，通过MCP协议返回

```
┌─────────────────────────────────────────────────────────────┐
│                        前端浏览器                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                MCP Host (React)                     │   │
│  │  ┌───────────────────────────────────────────────┐  │   │
│  │  │           UIResourceRenderer (iframe)          │  │   │
│  │  │  ┌─────────────────────────────────────────┐   │  │   │
│  │  │  │  后端生成的完整HTML页面                  │   │  │   │
│  │  │  │  包含: HTML + CSS + JavaScript          │   │  │   │
│  │  │  └─────────────────────────────────────────┘   │  │   │
│  │  └───────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                    MCP JSON-RPC                              │
│                          ▼                                  │
│┌────────────────────────────────────────────────────────────┐│
││                      MCP Server                            ││
││  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     ││
││  │   Tools      │  │   Stores     │  │   UI         │     ││
││  │  (业务逻辑)  │  │  (数据存储)  │  │  (页面生成)  │     ││
││  └──────────────┘  └──────────────┘  └──────────────┘     ││
│└────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
```

### 1.2 数据流向

```
用户操作 → iframe → postMessage → MCP Host → MCP Tool Call → 后端处理 → 返回结果
                                                              │
                                                              ▼
                                                     更新数据/生成新UI
                                                              │
                                                              ▼
                                                     返回UI Resource → 重新渲染iframe
```

## 2. 目录结构规范

```
src/
├── components/           # 前端组件（仅Host）
│   └── MCPHost.tsx      # MCP客户端Host，负责连接和渲染
├── server/              # 后端服务
│   ├── tools/           # MCP工具定义
│   │   ├── todoTools.ts
│   │   ├── kanbanTools.ts
│   │   └── familyTools.ts  # 家族管理工具
│   ├── ui/              # UI页面生成器
│   │   ├── todoListHtml.ts
│   │   ├── kanbanBoardHtml.ts
│   │   ├── familyTreeHtml.ts    # 家族树页面
│   │   ├── familyManageHtml.ts  # 家族管理页面
│   │   ├── memberManageHtml.ts  # 成员管理页面
│   │   └── historyHtml.ts       # 历史记录页面
│   ├── stores/          # 数据存储层（可选）
│   │   ├── todoStore.ts
│   │   ├── kanbanStore.ts
│   │   ├── familyStore.ts
│   │   ├── memberStore.ts
│   │   └── relationshipStore.ts
│   └── index.ts         # 服务器入口
├── shared/              # 共享类型定义
│   └── types.ts
├── App.tsx              # 前端入口
└── main.tsx             # React挂载点
```

## 3. MCP工具开发规范

### 3.1 工具命名规范

| 工具类型 | 命名格式 | 示例 |
|---------|---------|------|
| UI获取 | `get{PageName}UI` | `getFamilyTreeUI`, `getMemberManageUI` |
| 数据查询 | `list{Entity}`, `get{Entity}` | `listFamilies`, `getMemberById` |
| 数据创建 | `create{Entity}` | `createFamily`, `createMember` |
| 数据更新 | `update{Entity}` | `updateFamily`, `updateMember` |
| 数据删除 | `delete{Entity}` | `deleteFamily`, `deleteMember` |

### 3.2 工具定义模板

```typescript
import { z } from 'zod';

export const getFamilyTreeUITool = {
  name: 'getFamilyTreeUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    outputSchema: z.object({
      content: z.array(z.object({
        type: z.string(),
        resource: z.object({
          uri: z.string(),
          mimeType: z.string(),
          text: z.string(),
        }),
      })),
    }),
    title: '获取家族树UI',
    description: '获取家族树可视化展示页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const html = generateFamilyTreeHtml(familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/tree',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};
```

### 3.3 参数传递规范

- **GET请求**：使用查询参数（query parameters）
- **POST/PUT请求**：使用请求体（request body）
- **禁止**：将参数直接放在URL路径中

正确示例：
- GET: `/members/family?familyId=1`
- POST: `/members` 配合请求体 `{ "familyId": 1, "name": "张三" }`

## 4. UI页面生成规范

### 4.1 HTML模板结构

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>页面标题</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    /* 自定义样式 */
  </style>
</head>
<body class="bg-gray-100 min-h-screen p-4">
  <!-- 页面内容 -->
  
  <script>
    // 页面逻辑
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
    
    // 页面初始化
    document.addEventListener('DOMContentLoaded', () => {
      // 加载数据
    });
  </script>
</body>
</html>
```

### 4.2 样式规范

使用 Tailwind CSS，遵循以下规范：

- **主色调**：绿色系列 (`#10B981` - `#059669`)
- **辅助色**：蓝色系列 (`#3B82F6` - `#2563EB`)
- **成功色**：`#10B981`
- **警告色**：`#F59E0B`
- **错误色**：`#EF4444`

按钮规范：
- 主要按钮：`bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md shadow`
- 次要按钮：`bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-md`
- 危险按钮：`bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md`

卡片规范：
- 基础卡片：`bg-white p-6 rounded-lg shadow`
- 悬停效果：`hover:shadow-lg transition-all duration-200`

### 4.3 JavaScript规范

1. **防抖机制**：防止重复请求导致界面闪烁
```javascript
var isLoading = false;
async function loadData() {
  if (isLoading) return;
  isLoading = true;
  try {
    // 数据请求逻辑
  } finally {
    isLoading = false;
  }
}
```

2. **渲染优化**：使用innerHTML一次性渲染，避免频繁DOM操作
```javascript
function renderList(data) {
  var container = document.getElementById('container');
  container.innerHTML = data.map(item => `
    <div class="card">${item.name}</div>
  `).join('');
}
```

3. **错误处理**：完善的错误捕获和用户提示
```javascript
try {
  var result = await mcpCallTool('getFamily', {});
} catch (error) {
  console.error('加载失败:', error);
  showToast('加载失败，请重试');
}
```

## 5. 数据存储规范

### 5.1 Store类结构

```typescript
class FamilyStore {
  private families: Family[] = [];
  
  getAll(): Family[];
  getById(id: string): Family | undefined;
  create(data: Partial<Family>): Family;
  update(id: string, data: Partial<Family>): Family | undefined;
  delete(id: string): boolean;
}
```

### 5.2 初始化数据

每个Store在构造函数中初始化示例数据，便于开发测试：

```typescript
constructor() {
  this.families.push({
    id: 'family-1',
    name: '张氏家族',
    description: '张氏族谱',
    createdAt: '2024-01-01',
    createdBy: 'user-1',
  });
}
```

### 5.3 数据模型规范

| 字段类型 | 命名格式 | 示例 |
|---------|---------|------|
| 主键 | `id` | `string` |
| 创建时间 | `createdAt` | `YYYY-MM-DD` |
| 创建人 | `createdBy` | `string (userId)` |
| 外键 | `{entity}Id` | `familyId`, `memberId` |

## 6. 前端Host规范

### 6.1 MCPHost组件职责

1. **连接管理**：维护与MCP Server的连接
2. **工具调用**：处理iframe发送的工具调用请求
3. **UI渲染**：渲染MCP返回的UI资源
4. **消息传递**：处理iframe与父页面的通信

### 6.2 工具调用分离

将工具调用分为两类：
- **callToolDirectly**：纯数据调用，不触发UI更新
- **handleToolCall**：带UI状态更新的调用

```typescript
const callToolDirectly = async (toolName: string, params: Record<string, unknown>) => {
  // 直接调用，不更新UI状态
};

const handleToolCall = async (toolName: string, params: Record<string, unknown>) => {
  // 调用并更新UI状态
};
```

### 6.3 消息监听

```typescript
useEffect(() => {
  const handleMessage = async (event: MessageEvent) => {
    if (event.data && event.data.type === 'tool') {
      const { messageId, payload } = event.data;
      try {
        const response = await callToolDirectly(payload.toolName, payload.params);
        event.source?.postMessage({ messageId, response }, '*');
      } catch (err) {
        event.source?.postMessage({ messageId, error: (err as Error).message }, '*');
      }
    }
  };
  window.addEventListener('message', handleMessage);
  return () => window.removeEventListener('message', handleMessage);
}, []);
```

## 7. 部署规范

### 7.1 环境配置

| 环境 | 端口 | 说明 |
|------|------|------|
| 开发 | 3000 (Server), 5173 (Host) | 本地开发 |
| 测试 | 3000 (Server), 5173 (Host) | 测试环境 |
| 生产 | 自定义 | 生产部署 |

### 7.2 启动命令

```bash
# 开发模式（同时启动Server和Host）
npm run dev

# 仅启动Server
npm run server

# 仅启动Host
npm run host

# 构建生产版本
npm run build
```

### 7.3 CORS配置

确保Server支持CORS，允许Host访问：

```typescript
app.use(cors());
```

## 8. 代码质量规范

### 8.1 TypeScript规范

- 使用严格模式 (`"strict": true`)
- 明确类型定义，避免`any`
- 使用Zod进行参数验证
- 接口命名：`I`前缀或直接命名

### 8.2 代码风格

- 使用4空格缩进
- 使用单引号
- 分号结尾
- 文件末尾空行
- 函数注释：JSDoc格式

### 8.3 安全规范

- 输入验证：所有用户输入必须验证
- SQL注入防护：使用参数化查询
- XSS防护：输出编码
- 敏感数据：避免日志中记录密码等敏感信息

## 9. 功能模块清单

### 9.1 用户管理

| 功能 | 状态 |
|------|------|
| 注册 | ✅ |
| 登录 | ✅ |
| 密码重置 | ✅ |
| 个人信息维护 | ✅ |

### 9.2 家族管理

| 功能 | 状态 |
|------|------|
| 新建家族 | ✅ |
| 编辑家族 | ✅ |
| 删除家族 | ✅ |
| 查看家族 | ✅ |

### 9.3 成员管理

| 功能 | 状态 |
|------|------|
| 查看成员列表 | ✅ |
| 新建成员 | ✅ |
| 编辑成员 | ✅ |
| 删除成员 | ✅ |
| 搜索成员 | ✅ |

### 9.4 关系管理

| 功能 | 状态 |
|------|------|
| 添加关系 | ✅ |
| 删除关系 | ✅ |
| 关系类型选择 | ✅ |

### 9.5 家族树

| 功能 | 状态 |
|------|------|
| 可视化展示 | ✅ |
| 成员节点显示 | ✅ |
| 关系连线 | ✅ |

### 9.6 AI功能

| 功能 | 状态 |
|------|------|
| AI关系分析 | 📋 |
| 家族故事生成 | 📋 |
| 图片导入识别 | 📋 |

## 10. 文档规范

### 10.1 文档结构

```
docs/
├── api/           # API文档
│   └── openapi.yaml
├── architecture/  # 架构文档
│   └── architecture.md
├── guides/        # 使用指南
│   ├── getting-started.md
│   └── project-standard.md  # 本文件
└── CHANGELOG.md   # 变更日志
```

### 10.2 文档更新

- 每次功能变更必须更新CHANGELOG
- 新功能需要添加API文档
- 架构变更需要更新架构文档