---
name: "personal-frontend-coding-standard"
description: "前端编码规范技能。提供前端开发的编码标准和最佳实践指导。"
---

# 前端编码规范技能

## 功能描述

提供前端开发的编码标准和最佳实践指导，帮助开发者编写高质量、可维护的前端代码。

## 何时使用

在以下情况调用此技能：
- 需要编写前端代码时
- 需要审查前端代码质量时
- 需要学习前端编码规范时
- 需要培训团队成员时

## 核心功能

- **命名规范**：提供变量、函数、组件的命名标准
- **代码结构**：指导代码组织和文件结构
- **样式规范**：规范 CSS/SCSS 的编写
- **组件设计**：指导 React/Vue 组件的设计
- **状态管理**：规范状态管理的使用
- **性能优化**：提供性能优化的最佳实践

## 输入参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| framework | String | 否 | 使用的框架（React/Vue/Angular等），默认React |
| language | String | 否 | 目标语言（TypeScript/JavaScript），默认TypeScript |
| focusArea | String | 否 | 关注领域（naming/structure/style/component/state/performance） |

## 输出格式

```json
{
  "status": "SUCCESS",
  "framework": "React",
  "language": "TypeScript",
  "focusArea": "naming",
  "rules": [
    {
      "category": "命名规范",
      "rules": [
        {
          "rule": "组件名使用 PascalCase",
          "example": "const UserProfile = () => {};",
          "description": "组件名应采用大驼峰命名法"
        }
      ]
    }
  ],
  "bestPractices": ["最佳实践1", "最佳实践2"]
}
```

## 编码规范分类

### 1. 命名规范

| 元素类型 | 规范 | 示例 |
|----------|------|------|
| 组件名 | PascalCase | `UserProfile`, `OrderList` |
| 函数名 | camelCase | `getUser()`, `handleSubmit()` |
| 变量名 | camelCase | `userName`, `isLoading` |
| 常量名 | UPPER_SNAKE_CASE | `MAX_RETRY`, `API_BASE_URL` |
| 文件/目录名 | kebab-case | `user-profile.tsx`, `components/` |
| Hook名 | use + camelCase | `useUser()`, `useLocalStorage()` |

### 2. 代码结构

```
src/
├── components/          # 通用组件
│   ├── common/         # 公共基础组件
│   │   └── Button.tsx
│   ├── layout/         # 布局组件
│   │   └── Header.tsx
│   └── pages/          # 页面级组件
│       └── HomePage.tsx
├── hooks/              # 自定义 Hooks
│   └── useUser.ts
├── services/           # API 服务
│   └── api.ts
├── store/              # 状态管理
│   └── store.ts
├── types/              # 类型定义
│   └── index.ts
├── utils/              # 工具函数
│   └── format.ts
├── styles/             # 全局样式
│   └── global.css
└── App.tsx             # 主应用组件
```

### 3. 组件设计

| 原则 | 说明 |
|------|------|
| 单一职责 | 每个组件只负责一个功能 |
| 可复用性 | 设计可复用的通用组件 |
| 无状态优先 | 能用函数组件就不用类组件 |
| 受控组件 | 表单组件优先使用受控模式 |
| Props 验证 | 使用 TypeScript 或 PropTypes |

### 4. 样式规范

| 原则 | 说明 |
|------|------|
| BEM 命名 | 使用 BEM 命名规范 |
| 模块化 | 使用 CSS Modules 或 styled-components |
| 响应式 | 使用媒体查询或 CSS-in-JS |
| 避免 !important | 优先使用选择器优先级 |
| 变量管理 | 使用 CSS 变量或 SCSS 变量 |

### 5. 状态管理

| 原则 | 说明 |
|------|------|
| 最小化状态 | 只保留必要的状态 |
| 状态提升 | 共享状态提升到公共父组件 |
| Context 使用 | 全局状态使用 Context 或状态库 |
| 异步状态 | 管理异步操作的加载和错误状态 |
| 状态持久化 | 必要时使用 localStorage |

### 6. 性能优化

| 原则 | 说明 |
|------|------|
| 懒加载 | 使用 React.lazy 和 Suspense |
| 虚拟滚动 | 大数据列表使用虚拟滚动 |
| 防抖节流 | 高频事件使用防抖节流 |
| 图片优化 | 使用合适的图片格式和尺寸 |
| 缓存策略 | 缓存 API 响应 |

## 最佳实践指南

### React 前端开发最佳实践

```typescript
// 组件示例
interface UserProfileProps {
  userId: string;
}

const UserProfile = ({ userId }: UserProfileProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await api.getUser(userId);
        setUser(data);
      } catch (err) {
        setError('Failed to fetch user');
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [userId]);

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorDisplay message={error} />;
  if (!user) return null;

  return (
    <div className="user-profile">
      <Avatar src={user.avatar} />
      <h2>{user.name}</h2>
      <p>{user.email}</p>
    </div>
  );
};

export default UserProfile;
```

### TypeScript 最佳实践

```typescript
// 类型定义
interface User {
  id: string;
  name: string;
  email: string;
  avatar?: string;
}

// 联合类型
type Status = 'loading' | 'success' | 'error';

// 泛型工具类型
type PartialUser = Partial<User>;
type UserId = Pick<User, 'id'>;
type UserWithoutId = Omit<User, 'id'>;
```

### 代码审查检查清单

| 检查项 | 说明 | 状态 |
|--------|------|------|
| 命名规范 | 变量、函数、组件命名是否符合规范 | ✅/❌ |
| 代码格式 | 缩进、空格、换行是否符合规范 | ✅/❌ |
| 类型安全 | TypeScript 类型是否正确 | ✅/❌ |
| 组件设计 | 组件是否遵循单一职责 | ✅/❌ |
| 状态管理 | 状态使用是否合理 | ✅/❌ |
| 性能优化 | 是否考虑性能问题 | ✅/❌ |
| 错误处理 | 是否正确处理异常情况 | ✅/❌ |
| 可测试性 | 代码是否易于测试 | ✅/❌ |

## 核心组件

| 组件 | 职责 | 描述 |
|------|------|------|
| CodeStyleChecker | 代码风格检查器 | 检查代码是否符合命名规范 |
| ComponentAnalyzer | 组件分析器 | 分析组件设计和结构 |
| TypeScriptValidator | TypeScript 验证器 | 验证类型定义 |
| PerformanceAnalyzer | 性能分析器 | 检测性能问题 |

## 配置要求

无需额外配置，基于规则引擎进行代码分析。

## 扩展指南

### 添加新的编码规则

1. 在规则引擎中添加新规则
2. 定义规则的检查逻辑
3. 更新代码审查检查清单

### 添加新框架支持

1. 创建对应框架的规则定义
2. 添加框架特定的最佳实践
3. 更新技能文档