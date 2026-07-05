# 家庭族谱应用 - PRD文档

## 1. 项目概述

### 1.1 产品定位
家庭族谱是一款基于MCP Apps规范的家族关系管理应用，旨在帮助用户记录、管理和传承家族历史与文化。通过可视化的家族树展示、成员信息管理、历史事件记录等功能，打造一个便捷的家族记忆平台。

### 1.2 目标用户
- 家族文化爱好者
- 家族管理者（族长、家谱修订者）
- 普通家庭成员
- 寻根问祖爱好者

### 1.3 产品愿景
成为最易用的家族关系管理平台，让每个家庭都能轻松记录和传承家族历史。

---

## 2. 竞品分析

### 2.1 主要竞品对比

| 功能维度 | 族记 | 云族谱 | 时光家谱 | FamilyRoots | 本项目 |
|---------|------|--------|----------|-------------|--------|
| **基础功能** | | | | | |
| 家族树展示 | ✅ 三种视图 | ✅ 全景图 | ✅ | ✅ 高级可视化 | ✅ 基础树 |
| 成员管理 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 关系管理 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 称谓自动计算 | ✅ | ❌ | ❌ | ❌ | ❌ |
| **协作功能** | | | | | |
| 多人协作编辑 | ✅ | ✅ | ❌ | ✅ 实时 | ❌ |
| 邀请家人 | ✅ | ✅ | ❌ | ✅ | ❌ |
| 家族社区 | ❌ | ✅ | ❌ | ✅ | ❌ |
| **内容功能** | | | | | |
| 相册功能 | ✅ | ✅ | ❌ | ✅ | ❌ |
| 历史事件记录 | ✅ | ❌ | ✅ 时间轴 | ✅ 故事 | ✅ 基础 |
| 纪念堂/祭祖 | ❌ | ✅ | ✅ | ❌ | ❌ |
| 迁徙记录 | ❌ | ❌ | ✅ | ❌ | ❌ |
| **导出功能** | | | | | |
| 导出图片 | ✅ | ✅ | ❌ | ✅ PDF | ❌ |
| 打印成册 | ✅ | ❌ | ❌ | ✅ | ❌ |
| GEDCOM导入 | ❌ | ✅ | ❌ | 规划中 | ❌ |
| **高级功能** | | | | | |
| AI照片工具 | ❌ | ❌ | ❌ | ✅ | ❌ |
| DNA集成 | ❌ | ❌ | ❌ | ✅ | ❌ |
| 统计分析 | ❌ | ❌ | ❌ | ✅ | ❌ |
| 权限管理 | ✅ | ✅ | ❌ | ✅ | ❌ |

### 2.2 竞品核心优势分析

**族记**：
- 三种家族树视图（父系、母系、夫妻）
- 称谓自动计算功能
- 多人协作编辑
- 相册功能

**云族谱**：
- 云端存储
- 家族社区互动
- 族谱百科和修谱课程
- GEDCOM导入

**时光家谱**：
- 时间轴展示家族变迁
- 迁徙过程记录
- 祠堂检索功能

**FamilyRoots**：
- 实时协作编辑
- AI照片工具（着色、修复）
- DNA集成
- 高级统计分析

### 2.3 市场机会

基于竞品分析，本项目在以下方面存在差异化机会：

1. **轻量化Web应用**：竞品多为移动端App，Web端体验不佳
2. **MCP协议集成**：支持AI Agent直接调用，智能化潜力大
3. **实时协作**：结合Web技术实现轻量级协作
4. **AI增强**：利用大模型提供智能提示、自动补全

---

## 3. 功能需求清单

### 3.1 已有功能

#### 3.1.1 用户模块

| 功能点 | 功能描述 | 状态 | 所属文件 |
|--------|----------|------|----------|
| 用户注册 | 支持邮箱、密码、昵称注册 | ✅ | [familyStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/familyStore.ts) |
| 用户登录 | 支持邮箱密码登录 | ✅ | [familyTools.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| 会话管理 | 维护用户登录状态 | ✅ | [familyTools.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |

#### 3.1.2 家族模块

| 功能点 | 功能描述 | 状态 | 所属文件 |
|--------|----------|------|----------|
| 创建家族 | 创建新家族，设置名称和描述 | ✅ | [familyStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/familyStore.ts) |
| 编辑家族 | 修改家族名称和描述 | ✅ | [familyStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/familyStore.ts) |
| 删除家族 | 删除指定家族 | ✅ | [familyStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/familyStore.ts) |
| 家族列表 | 获取所有家族列表 | ✅ | [familyTools.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |

#### 3.1.3 成员模块

| 功能点 | 功能描述 | 状态 | 所属文件 |
|--------|----------|------|----------|
| 添加成员 | 添加家族成员，记录姓名、性别、出生日期等 | ✅ | [memberStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/memberStore.ts) |
| 编辑成员 | 修改成员信息 | ✅ | [memberStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/memberStore.ts) |
| 删除成员 | 删除指定成员 | ✅ | [memberStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/memberStore.ts) |
| 成员列表 | 获取家族成员列表 | ✅ | [memberStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/memberStore.ts) |
| 搜索成员 | 按姓名、电话、邮箱搜索成员 | ✅ | [memberStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/memberStore.ts) |

#### 3.1.4 关系模块

| 功能点 | 功能描述 | 状态 | 所属文件 |
|--------|----------|------|----------|
| 创建关系 | 创建成员间的关系（父子、夫妻、兄弟姐妹等） | ✅ | [relationshipStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/relationshipStore.ts) |
| 删除关系 | 删除指定关系 | ✅ | [relationshipStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/relationshipStore.ts) |
| 关系列表 | 获取家族关系列表 | ✅ | [relationshipStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/relationshipStore.ts) |

#### 3.1.5 家族树模块

| 功能点 | 功能描述 | 状态 | 所属文件 |
|--------|----------|------|----------|
| 家族树展示 | 可视化展示家族成员关系 | ✅ | [familyTreeHtml.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/ui/familyTreeHtml.ts) |

#### 3.1.6 历史记录模块

| 功能点 | 功能描述 | 状态 | 所属文件 |
|--------|----------|------|----------|
| 添加事件 | 添加家族事件、成员大事件、操作日志 | ✅ | [historyStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/historyStore.ts) |
| 删除事件 | 删除指定历史事件 | ✅ | [historyStore.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/historyStore.ts) |
| 事件列表 | 获取家族历史事件列表 | ✅ | [historyTools.ts](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |

### 3.2 缺失功能

#### 3.2.1 用户体验增强

| 功能点 | 功能描述 | 优先级 | 预估工时 |
|--------|----------|--------|----------|
| 个人资料编辑 | 编辑用户昵称、头像、手机号 | P2 | 2h |
| 密码修改 | 修改登录密码 | P2 | 2h |
| 头像上传 | 支持上传用户头像 | P3 | 4h |

#### 3.2.2 协作功能

| 功能点 | 功能描述 | 优先级 | 预估工时 |
|--------|----------|--------|----------|
| 邀请家人 | 通过链接/二维码邀请家人加入家族 | P1 | 8h |
| 权限管理 | 为家族成员设置不同权限（管理员、编辑、查看） | P1 | 10h |
| 协作编辑 | 支持多人同时编辑家族信息 | P2 | 16h |
| 操作日志 | 记录所有操作历史，支持追溯 | P2 | 6h |

#### 3.2.3 内容功能

| 功能点 | 功能描述 | 优先级 | 预估工时 |
|--------|----------|--------|----------|
| 相册功能 | 上传和管理家族照片，支持分享 | P1 | 12h |
| 纪念堂 | 记录逝者信息，提供网上缅怀功能 | P2 | 8h |
| 家族动态 | 发布和查看家族动态 | P2 | 8h |
| 故事记录 | 记录家族故事和个人事迹 | P3 | 6h |

#### 3.2.4 家族树增强

| 功能点 | 功能描述 | 优先级 | 预估工时 |
|--------|----------|--------|----------|
| 多种视图 | 支持父系、母系、夫妻三种视图 | P1 | 10h |
| 称谓自动计算 | 根据关系自动计算称谓 | P1 | 8h |
| 族谱导出 | 导出为图片或PDF | P2 | 8h |
| 打印功能 | 支持打印族谱 | P3 | 6h |

#### 3.2.5 数据管理

| 功能点 | 功能描述 | 优先级 | 预估工时 |
|--------|----------|--------|----------|
| GEDCOM导入 | 支持导入GEDCOM格式文件 | P2 | 12h |
| GEDCOM导出 | 支持导出为GEDCOM格式 | P2 | 8h |
| 数据备份 | 支持数据备份和恢复 | P3 | 6h |

#### 3.2.6 高级功能

| 功能点 | 功能描述 | 优先级 | 预估工时 |
|--------|----------|--------|----------|
| 统计分析 | 家族人口统计、年龄分布、性别比例等 | P2 | 8h |
| 迁徙记录 | 记录家族迁徙历史 | P3 | 6h |
| AI智能提示 | 利用AI提供关系建议和信息补全 | P3 | 10h |
| 移动端适配 | 适配手机和平板设备 | P2 | 16h |

---

## 4. 功能优先级路线图

### 4.1 第一阶段：核心功能完善（1-2周）

| 功能 | 说明 |
|------|------|
| 称谓自动计算 | 根据成员关系自动计算称谓 |
| 多种视图 | 父系、母系、夫妻三种家族树视图 |
| 权限管理 | 家族成员权限设置 |
| 邀请家人 | 通过链接邀请家人 |

### 4.2 第二阶段：内容增强（2-3周）

| 功能 | 说明 |
|------|------|
| 相册功能 | 照片上传、管理、分享 |
| 家族动态 | 发布和查看动态 |
| 族谱导出 | 导出为图片/PDF |
| 统计分析 | 家族数据统计 |

### 4.3 第三阶段：高级功能（3-4周）

| 功能 | 说明 |
|------|------|
| GEDCOM导入导出 | 兼容标准族谱格式 |
| 纪念堂 | 网上缅怀功能 |
| AI智能提示 | 智能辅助功能 |
| 移动端适配 | 响应式设计 |

---

## 5. 非功能需求

### 5.1 性能需求
- 页面加载时间 < 2秒
- 家族树渲染支持500+成员
- 并发用户支持100+

### 5.2 安全需求
- 用户数据加密存储
- 敏感信息传输加密
- 权限控制严格验证

### 5.3 兼容性需求
- 支持主流浏览器（Chrome、Firefox、Safari、Edge）
- 支持移动端访问

---

## 6. 数据模型

### 6.1 用户模型

```typescript
interface User {
  id: string;
  email: string;
  password: string;
  nickname: string;
  avatar: string | null;
  phone: string | null;
  createdAt: string;
}
```

### 6.2 家族模型

```typescript
interface Family {
  id: string;
  name: string;
  description: string;
  avatar: string | null;
  createdAt: string;
  createdBy: string;
  adminId: string;
}
```

### 6.3 成员模型

```typescript
interface Member {
  id: string;
  familyId: string;
  name: string;
  gender: 'male' | 'female';
  birthDate: string | null;
  deathDate: string | null;
  photo: string | null;
  details: string | null;
  phone: string | null;
  email: string | null;
  createdAt: string;
}
```

### 6.4 关系模型

```typescript
interface Relationship {
  id: string;
  memberId1: string;
  memberId2: string;
  relationshipType: 'father' | 'mother' | 'husband' | 'wife' | 'son' | 'daughter' | 'brother' | 'sister' | ...;
  createdAt: string;
}
```

### 6.5 历史事件模型

```typescript
interface HistoryEvent {
  id: string;
  familyId: string;
  type: 'event' | 'milestone' | 'log';
  title: string;
  description: string;
  date: string;
  relatedMemberId: string | null;
  operator: string | null;
}
```

---

## 7. 页面结构

### 7.1 当前页面

| 页面 | 路径 | 功能 | 状态 |
|------|------|------|------|
| 登录页 | ui://family/login | 用户登录/注册 | ✅ |
| 首页 | ui://family/home | 功能入口、统计信息 | ✅ |
| 家族管理 | ui://family/manage | 家族增删改查 | ✅ |
| 成员管理 | ui://family/members | 成员增删改查 | ✅ |
| 关系管理 | ui://family/relationships | 关系管理 | ✅ |
| 家族树 | ui://family/tree | 家族树可视化 | ✅ |
| 历史记录 | ui://family/history | 历史事件记录 | ✅ |

### 7.2 待添加页面

| 页面 | 路径 | 功能 | 优先级 |
|------|------|------|--------|
| 个人中心 | ui://family/profile | 用户个人资料 | P2 |
| 相册 | ui://family/album | 家族相册 | P1 |
| 纪念堂 | ui://family/memorial | 逝者缅怀 | P2 |
| 家族动态 | ui://family/feed | 动态发布查看 | P2 |
| 统计分析 | ui://family/stats | 数据分析 | P2 |

---

## 8. API接口清单

### 8.1 已有接口

| 接口名 | 功能 | 所属工具 |
|--------|------|----------|
| login | 用户登录 | [loginTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| register | 用户注册 | [registerTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| listFamilies | 获取家族列表 | [listFamiliesTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| createFamily | 创建家族 | [createFamilyTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| updateFamily | 更新家族 | [updateFamilyTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| deleteFamily | 删除家族 | [deleteFamilyTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| listMembers | 获取成员列表 | [listMembersTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| createMember | 创建成员 | [createMemberTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| updateMember | 更新成员 | [updateMemberTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| deleteMember | 删除成员 | [deleteMemberTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| searchMembers | 搜索成员 | [searchMembersTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| listRelationships | 获取关系列表 | [listRelationshipsTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| createRelationship | 创建关系 | [createRelationshipTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| deleteRelationship | 删除关系 | [deleteRelationshipTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| listEvents | 获取历史事件 | [listEventsTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| createEvent | 创建历史事件 | [createEventTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |
| deleteEvent | 删除历史事件 | [deleteEventTool](file:///Users/bage/bage/github/study/study-ai-mcp-apps/src/server/tools/familyTools.ts) |

### 8.2 待添加接口

| 接口名 | 功能 | 优先级 |
|--------|------|--------|
| updateProfile | 更新用户资料 | P2 |
| changePassword | 修改密码 | P2 |
| uploadAvatar | 上传头像 | P3 |
| inviteMember | 邀请成员 | P1 |
| setPermissions | 设置权限 | P1 |
| listAlbums | 获取相册列表 | P1 |
| uploadPhoto | 上传照片 | P1 |
| deletePhoto | 删除照片 | P1 |
| listMemorials | 获取纪念堂列表 | P2 |
| createMemorial | 创建纪念堂 | P2 |
| listFeed | 获取家族动态 | P2 |
| createFeed | 发布动态 | P2 |
| getStatistics | 获取统计数据 | P2 |
| importGedcom | 导入GEDCOM | P2 |
| exportGedcom | 导出GEDCOM | P2 |
| exportTree | 导出家族树 | P2 |
| getRelationshipLabel | 获取关系称谓 | P1 |

---

## 9. 总结

### 9.1 项目现状

本项目已实现家庭族谱的核心功能，包括：
- 用户登录/注册
- 家族管理（CRUD）
- 成员管理（CRUD + 搜索）
- 关系管理（CRUD）
- 家族树可视化
- 历史记录管理

### 9.2 核心差距

与市场竞品相比，本项目主要缺少：

1. **协作能力** - 多人协作编辑、邀请机制、权限管理
2. **内容丰富度** - 相册、动态、故事记录
3. **家族树增强** - 多视图、称谓计算、导出功能
4. **数据互通** - GEDCOM格式支持

### 9.3 建议优先实现

根据优先级路线图，建议优先实现：
1. 称谓自动计算（提升用户体验）
2. 多种家族树视图（增强可视化）
3. 邀请家人和权限管理（支持协作）
4. 相册功能（丰富内容）
