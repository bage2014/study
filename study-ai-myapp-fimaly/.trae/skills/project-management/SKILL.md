# 项目进度管理

## 1. 功能描述

项目进度管理功能用于跟踪家庭族谱APP的开发进度，记录已完成的功能和待完成的任务。通过进度文件，团队成员可以清晰了解项目的当前状态和下一步计划。

## 2. 进度文件结构

进度文件位于项目根目录下的 `.trae/progress.json`，采用JSON格式存储项目进度信息。

### 2.1 进度文件示例

```json
{
  "projectName": "家庭族谱APP",
  "lastUpdated": "2026-04-06T12:00:00Z",
  "completedFeatures": [
    {
      "id": "user-management",
      "name": "用户管理",
      "description": "注册、登录、密码重置功能",
      "completedAt": "2026-04-01T10:00:00Z"
    },
    {
      "id": "family-management",
      "name": "家族管理",
      "description": "新建家族、编辑家族、查看家族、删除家族功能",
      "completedAt": "2026-04-03T14:00:00Z"
    },
    {
      "id": "member-management",
      "name": "成员管理",
      "description": "查看家族成员、新建成员、编辑成员、删除成员功能",
      "completedAt": "2026-04-04T09:00:00Z"
    },
    {
      "id": "relationship-management",
      "name": "关系管理",
      "description": "添加关系、删除关系功能",
      "completedAt": "2026-04-04T11:00:00Z"
    },
    {
      "id": "family-tree-visualization",
      "name": "家族树可视化",
      "description": "家族关系图显示功能",
      "completedAt": "2026-04-05T15:00:00Z"
    },
    {
      "id": "historical-records",
      "name": "历史记录",
      "description": "记录家族重要事件功能",
      "completedAt": "2026-04-05T16:00:00Z"
    },
    {
      "id": "media-library",
      "name": "多媒体库",
      "description": "上传、管理家族照片功能",
      "completedAt": "2026-04-05T17:00:00Z"
    }
  ],
  "pendingTasks": [
    {
      "id": "ai-relationship-analysis",
      "name": "AI关系分析",
      "description": "基于现有数据自动推测和补全家族关系",
      "priority": "high",
      "dueDate": "2026-04-15"
    },
    {
      "id": "family-story-generation",
      "name": "家族故事生成",
      "description": "基于家族成员信息和历史事件，自动生成家族故事",
      "priority": "medium",
      "dueDate": "2026-04-20"
    },
    {
      "id": "cross-generation-communication",
      "name": "跨代沟通平台",
      "description": "为不同年龄层的家族成员提供适合的沟通方式",
      "priority": "medium",
      "dueDate": "2026-04-25"
    },
    {
      "id": "family-health-records",
      "name": "家族健康档案",
      "description": "记录家族健康历史，提供健康风险评估",
      "priority": "low",
      "dueDate": "2026-04-30"
    },
    {
      "id": "android-app",
      "name": "Android应用",
      "description": "开发Android端应用",
      "priority": "high",
      "dueDate": "2026-05-10"
    }
  ],
  "overallProgress": 70
}
```

## 3. 进度管理功能

### 3.1 读取进度

通过读取 `frontend/web/public/progress.json` 文件，获取项目的当前进度信息，包括已完成的功能和待完成的任务。

### 3.2 更新进度

**重要：更新进度时需要同步更新两个文件，确保进度信息的一致性。**

#### 3.2.1 需要同步更新的文件

1. **进度文件**：`frontend/web/public/progress.json`
   - 记录项目的已完成功能和待完成任务
   - 包含功能ID、名称、描述、完成时间、优先级等详细信息
   - 前端页面直接读取此文件展示进度

2. **PRD文档**：`docs/prd/prd.md`
   - 在第2章"核心功能"的"功能完成情况"章节中更新
   - 包括"已完成功能"表格和"待完成功能"表格
   - 更新整体进度统计

#### 3.2.2 更新流程

当完成新功能或任务时，需要按以下步骤同步更新：

**步骤1：更新进度文件**
- 在 `frontend/web/public/progress.json` 文件中：
  - 将新完成的功能添加到 `completedFeatures` 数组
  - 或将待完成任务从 `pendingTasks` 数组移到 `completedFeatures` 数组
  - 更新 `lastUpdated` 字段为当前时间
  - 重新计算 `overallProgress`（已完成功能数 / 总功能数 * 100）

**步骤2：更新PRD文档**
- 在 `docs/prd/prd.md` 文件的第2.2章节中：
  - 在"已完成功能"表格中添加新完成的功能行
  - 或从"待完成功能"表格中移除已完成的任务行
  - 更新"整体进度"统计信息

**步骤3：验证一致性**
- 确保两个文件中的功能ID、名称、描述保持一致
- 确保整体进度百分比一致
- 确保已完成功能和待完成任务的数量一致

#### 3.2.3 更新示例

**示例：完成"数据导出导入"功能**

**更新 progress.json：**
```json
{
  "completedFeatures": [
    // ... 现有已完成功能 ...
    {
      "id": "data-export-import",
      "name": "数据导出导入",
      "description": "支持家族数据的导出和导入功能，方便数据迁移和备份",
      "completedAt": "2026-04-15T10:00:00Z"
    }
  ],
  "pendingTasks": [
    // 移除 data-export-import 任务
    // ... 其他待完成任务 ...
  ],
  "overallProgress": 60
}
```

**更新 prd.md：**
在"已完成功能"表格中添加：
```
| data-export-import | 数据导出导入 | 支持家族数据的导出和导入功能，方便数据迁移和备份 | 2026-04-15 | ✅ 已完成 |
```

在"待完成功能"表格中移除对应行，并更新整体进度统计。

### 3.3 进度统计

根据已完成功能和待完成任务的数量，计算项目的整体完成进度。

## 4. 代码实现

### 4.1 前端实现

在前端项目中，添加一个进度管理工具，用于读取和更新进度文件。

### 4.2 后端实现

在后端项目中，添加一个进度管理API，用于获取和更新项目进度。

## 5. 使用方法

1. **查看进度**：通过读取 `frontend/web/public/progress.json` 文件，查看项目的当前进度。
2. **更新进度**：当完成新功能时，**必须同时更新以下两个文件**：
   - 更新 `frontend/web/public/progress.json` 文件，添加新的已完成功能
   - 更新 `docs/prd/prd.md` 文件，在功能完成情况表格中添加新功能
3. **添加任务**：当有新的任务时，在 `frontend/web/public/progress.json` 文件的 `pendingTasks` 数组中添加新的任务，同时在 `docs/prd/prd.md` 的待完成功能表格中添加对应行。
4. **标记任务完成**：当任务完成时，将任务从 `pendingTasks` 数组移动到 `completedFeatures` 数组中，并更新 `completedAt` 字段。同时更新PRD文档中的对应表格。

**注意事项：**
- 更新进度时必须同步更新两个文件，确保信息一致性
- 更新后需要验证两个文件中的功能数量和进度百分比是否一致
- 建议使用版本控制系统（如Git）管理进度文件，确保进度信息的历史记录

## 6. 最佳实践

- **同步更新**：更新进度时必须同步更新 `frontend/web/public/progress.json` 和 `docs/prd/prd.md` 两个文件，确保进度信息的一致性。
- **定期更新**：定期更新进度文件，确保进度信息的准确性。
- **明确标识**：为每个功能和任务设置明确的ID和描述，便于跟踪和管理。
- **优先级管理**：为待完成任务设置优先级和截止日期，便于规划和执行。
- **版本控制**：使用版本控制系统（如Git）管理进度文件，确保进度信息的历史记录。
- **验证一致性**：更新后验证两个文件中的功能数量、进度百分比是否一致，避免信息不一致。
- **文档维护**：保持PRD文档的更新，确保文档与实际进度保持同步。