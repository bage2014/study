# DESIGN_track_v1.0 - 用户轨迹设计文档

## 1. 设计概述

### 1.1 功能描述

用户历史轨迹功能设计，基于百度地图实现轨迹记录和展示。

### 1.2 文档信息

| 字段 | 值 |
|------|------|
| 文档版本 | v1.0 |
| 创建日期 | 2026-05-12 |
| 状态 | 已评审 |

---

## 2. 技术方案

### 2.1 技术栈

| 组件 | 技术选型 |
|------|----------|
| 后端框架 | Spring Boot 3.1.10 |
| 数据库 | H2 / MySQL |
| 前端框架 | Vue 3.4+ |
| 地图服务 | 百度地图API |

### 2.2 架构设计

```
前端页面 → 百度地图 → TrackPointController → TrackPointService → TrackPointRepository → 数据库
```

---

## 3. 数据库设计

### 3.1 轨迹点表 (track_points)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 轨迹点ID |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 用户ID |
| latitude | DOUBLE | NOT NULL | 纬度 |
| longitude | DOUBLE | NOT NULL | 经度 |
| name | VARCHAR(100) | | 位置名称 |
| description | VARCHAR(500) | | 位置描述 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

### 3.2 索引设计

| 索引名 | 字段 | 类型 |
|--------|------|------|
| idx_user_id | user_id | 普通索引 |
| idx_created_at | created_at | 普通索引 |

---

## 4. API 设计

### 4.1 接口列表

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/trackpoints | 获取轨迹点列表 |
| GET | /api/trackpoints/{id} | 获取单个轨迹点 |
| POST | /api/trackpoints | 创建轨迹点 |
| PUT | /api/trackpoints/{id} | 更新轨迹点 |
| DELETE | /api/trackpoints/{id} | 删除轨迹点 |

### 4.2 接口详情

#### POST /api/trackpoints

**请求体**：

```json
{
  "latitude": 39.9042,
  "longitude": 116.4074,
  "name": "天安门",
  "description": "首都北京标志性建筑"
}
```

**响应**：

```json
{
  "id": 1,
  "userId": 1,
  "latitude": 39.9042,
  "longitude": 116.4074,
  "name": "天安门",
  "description": "首都北京标志性建筑",
  "createdAt": "2026-05-12T10:00:00"
}
```

---

## 5. 前端设计

### 5.1 页面结构

| 元素 | 描述 |
|------|------|
| 地图容器 | 百度地图显示区域 |
| 轨迹列表 | 侧边栏显示轨迹点列表 |
| 踩点按钮 | 添加轨迹点功能 |
| 删除按钮 | 删除选中轨迹点 |

### 5.2 交互流程

```
点击地图 → 弹出信息弹窗 → 输入名称 → 保存 → 更新地图显示
```

---

## 6. 安全评审

| 检查项 | 状态 | 说明 |
|--------|------|------|
| 用户权限 | ✅ | 只能访问自己的轨迹 |
| 输入验证 | ✅ | 经纬度范围校验 |

---

## 7. 性能评审

| 指标 | 目标值 |
|------|--------|
| 地图加载时间 | ≤ 2秒 |
| 轨迹渲染时间 | ≤ 1秒（100个点） |

---

## 8. 项目结构

```
backend/src/main/java/.../
├── controller/
│   └── TrackPointController.java
├── service/
│   ├── TrackPointService.java
│   └── impl/
│       └── TrackPointServiceImpl.java
├── repository/
│   └── TrackPointRepository.java
├── entity/
│   └── TrackPoint.java
└── dto/
    ├── TrackPointRequest.java
    └── TrackPointResponse.java

frontend/src/
├── components/
│   └── TrackMap.vue
└── api/
    └── trackpoint.js
```

---

## 9. 评审意见

| 评审项 | 结果 | 备注 |
|--------|------|------|
| 架构合理性 | 通过 | - |
| API设计 | 通过 | RESTful风格 |
| 数据库设计 | 通过 | 规范化设计 |

---

## 10. 变更历史

| 版本 | 日期 | 修改内容 | 作者 |
|------|------|----------|------|
| v1.0 | 2026-05-12 | 初始版本 | - |