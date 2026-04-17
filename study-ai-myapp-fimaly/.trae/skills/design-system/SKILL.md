---
name: "design-system"
description: "提供设计系统的标准规范，包括设计原则、视觉风格、组件库等。Invoke when designing new features or components for the application."
---

# 设计系统规范

## 1. 设计原则

### 1.1 核心原则

- **简洁性**：保持设计简洁明了，减少视觉干扰
- **一致性**：确保整个应用的设计风格一致
- **可用性**：优先考虑用户体验和易用性
- **可访问性**：确保所有用户都能访问和使用应用
- **响应式**：适配不同屏幕尺寸和设备

### 1.2 设计理念

- **以人为本**：以用户需求为中心，设计符合用户期望的界面
- **功能驱动**：设计应服务于功能，而非为了设计而设计
- **持续改进**：根据用户反馈和使用数据不断优化设计

## 2. 视觉风格

### 2.1 色彩系统

#### 2.1.1 主色调
- **主色**：绿色系列（`#10B981` - `#059669`）
- **辅助色**：蓝色系列（`#3B82F6` - `#2563EB`）、紫色系列（`#8B5CF6` - `#7C3AED`）
- **中性色**：灰色系列（`#F9FAFB` - `#111827`）

#### 2.1.2 语义色彩
- **成功**：绿色（`#10B981`）
- **警告**：黄色（`#F59E0B`）
- **错误**：红色（`#EF4444`）
- **信息**：蓝色（`#3B82F6`）

### 2.2 字体系统

#### 2.2.1 字体选择
- **主要字体**：无衬线字体，如 `Inter` 或系统默认无衬线字体
- **次要字体**：等宽字体，用于代码和数据展示

#### 2.2.2 字体大小
- **标题**：`text-2xl`（24px）、`text-xl`（20px）、`text-lg`（18px）
- **正文**：`text-base`（16px）
- **辅助文字**：`text-sm`（14px）、`text-xs`（12px）

#### 2.2.3 字重
- **粗体**：`font-bold`（700）
- **中等**：`font-medium`（500）
- **常规**：`font-normal`（400）

### 2.3 间距系统

- **基础间距**：4px 为一个单位
- **常用间距**：`p-4`（16px）、`m-4`（16px）、`space-x-4`（16px）
- **响应式间距**：根据屏幕尺寸调整间距

### 2.4 阴影系统

- **轻微阴影**：`shadow-sm`
- **中等阴影**：`shadow`
- **重度阴影**：`shadow-lg`、`shadow-xl`

### 2.5 圆角系统

- **小圆角**：`rounded`（4px）
- **中等圆角**：`rounded-md`（6px）
- **大圆角**：`rounded-lg`（8px）、`rounded-xl`（12px）

## 3. 组件库

### 3.1 基础组件

#### 3.1.1 按钮
- **主要按钮**：`bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md shadow hover:shadow-lg transition-all duration-200`
- **成功按钮**：`bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md shadow hover:shadow-lg transition-all duration-200`
- **危险按钮**：`bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md shadow hover:shadow-lg transition-all duration-200`
- **次要按钮**：`bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-md shadow hover:shadow-lg transition-all duration-200`

#### 3.1.2 输入框
- **基础输入框**：`w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent`
- **错误状态**：`border-red-500 focus:ring-red-500`
- **禁用状态**：`bg-gray-100 cursor-not-allowed`

#### 3.1.3 选择框
- **基础选择框**：`w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent`

#### 3.1.4 卡片
- **基础卡片**：`bg-white p-6 rounded-lg shadow`
- **悬停效果**：`hover:shadow-lg transition-all duration-200`

#### 3.1.5 表格
- **基础表格**：`min-w-full divide-y divide-gray-200`
- **表头**：`bg-gray-50`
- **表体**：`bg-white divide-y divide-gray-200`
- **行悬停**：`hover:bg-gray-50`

### 3.2 布局组件

#### 3.2.1 容器
- **固定宽度容器**：`max-w-7xl mx-auto px-4 sm:px-6 lg:px-8`
- **流体容器**：`w-full px-4`

#### 3.2.2 网格
- **基础网格**：`grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4`

#### 3.2.3 弹性布局
- **基础弹性布局**：`flex items-center justify-between`
- **垂直弹性布局**：`flex flex-col`

### 3.3 导航组件

#### 3.3.1 头部导航
- **基础头部**：`bg-white shadow h-16`
- **内容布局**：`flex items-center justify-between`

#### 3.3.2 侧边导航
- **基础侧边栏**：`w-64 bg-white shadow-md`
- **导航项**：`px-4 py-3 hover:bg-gray-100`

### 3.4 反馈组件

#### 3.4.1 提示框
- **成功提示**：`bg-green-100 text-green-800 p-4 rounded-md`
- **错误提示**：`bg-red-100 text-red-800 p-4 rounded-md`
- **警告提示**：`bg-yellow-100 text-yellow-800 p-4 rounded-md`
- **信息提示**：`bg-blue-100 text-blue-800 p-4 rounded-md`

#### 3.4.2 加载状态
- **基础加载**：`flex justify-center items-center`
- **加载动画**：使用 spinner 动画

## 4. 页面设计

### 4.1 页面布局

- **基础布局**：Header + Main + Footer
- **内容区域**：`max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8`
- **响应式布局**：适配不同屏幕尺寸

### 4.2 页面类型

#### 4.2.1 列表页面
- **布局**：筛选器 + 列表 + 分页
- **表格**：使用标准表格组件
- **操作**：每行提供编辑、删除等操作

#### 4.2.2 详情页面
- **布局**：头部信息 + 详情内容 + 相关操作
- **信息展示**：使用卡片和表格展示信息

#### 4.2.3 表单页面
- **布局**：表单标题 + 表单内容 + 操作按钮
- **表单控件**：使用标准输入框、选择框等组件
- **验证**：实时表单验证和错误提示

### 4.3 页面示例

#### 4.3.1 登录页面
- **布局**：居中表单，包含邮箱、密码输入框和登录按钮
- **样式**：使用卡片组件，添加适当的阴影和间距

#### 4.3.2 首页
- **布局**：网格布局，展示功能卡片
- **样式**：使用卡片组件，添加悬停效果

#### 4.3.3 家族管理页面
- **布局**：顶部操作栏 + 家族列表表格
- **样式**：使用表格组件，添加适当的间距和阴影

## 5. 图标系统

### 5.1 图标选择
- **图标库**：使用 Heroicons 或其他现代图标库
- **风格**：线性图标，保持一致性
- **大小**：根据使用场景选择适当的图标大小

### 5.2 图标使用
- **导航图标**：16px - 20px
- **操作图标**：14px - 16px
- **状态图标**：12px - 14px

## 6. 交互设计

### 6.1 微交互

- **按钮悬停**：轻微放大和阴影变化
- **输入框聚焦**：边框和阴影变化
- **卡片悬停**：轻微上浮和阴影加深

### 6.2 动画效果

- **页面过渡**：平滑的页面切换动画
- **元素进入**：淡入、滑动等动画效果
- **加载状态**：旋转、脉冲等动画效果

### 6.3 反馈机制

- **操作反馈**：成功/失败提示
- **加载反馈**：加载动画
- **错误反馈**：错误提示和表单验证

## 7. 响应式设计

### 7.1 断点设计

- **移动端**：< 640px
- **平板**：640px - 1024px
- **桌面**：> 1024px

### 7.2 适配策略

- **移动端**：单列布局，简化导航
- **平板**：双列布局，适当调整间距
- **桌面**：多列布局，充分利用空间

### 7.3 触摸优化

- **触摸目标**：至少 44px × 44px
- **间距**：适当增加触摸元素之间的间距
- **手势**：支持常见的触摸手势

## 8. 设计工具

### 8.1 设计软件
- **Figma**：用于UI设计和原型制作
- **Sketch**：用于UI设计
- **Adobe XD**：用于UI设计和原型制作

### 8.2 协作工具
- **Zeplin**：用于设计规范和资产交付
- **InVision**：用于原型分享和反馈

## 9. 设计流程

### 9.1 设计阶段

1. **需求分析**：理解功能需求和用户需求
2. **低保真原型**：创建简单的线框图
3. **高保真原型**：创建详细的设计稿
4. **设计评审**：团队评审设计方案
5. **设计交付**：向开发团队交付设计规范和资产

### 9.2 设计规范维护

- **版本控制**：使用Git或其他版本控制工具
- **定期更新**：根据产品迭代更新设计规范
- **反馈收集**：收集用户和开发团队的反馈

## 10. 最佳实践

- **组件复用**：使用设计系统中的组件，保持一致性
- **设计系统维护**：定期更新和优化设计系统
- **用户测试**：进行用户测试，验证设计效果
- **性能考虑**：设计时考虑性能影响，如图片大小和动画复杂度
- **可访问性**：确保设计符合可访问性标准

通过遵循本设计系统规范，可以确保应用的设计风格一致、用户体验良好，同时提高设计和开发效率。