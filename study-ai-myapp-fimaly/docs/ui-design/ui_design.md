# 家庭族谱APP UI设计规范

## 1. 设计风格

### 1.1 整体风格

- **风格定位**：现代简约风格，融合传统家族文化元素
- **设计理念**：以用户为中心，注重用户体验，界面简洁直观
- **视觉效果**：温暖、和谐、有层次感，体现家族的温馨氛围

### 1.2 设计原则

- **一致性**：保持界面元素的一致性，确保用户操作流畅
- **简洁性**：减少视觉干扰，突出核心功能
- **可访问性**：确保不同年龄层用户都能轻松使用
- **响应式**：适配不同屏幕尺寸，提供良好的跨设备体验

## 2. 颜色方案

### 2.1 主色调

| 颜色名称 | 色值 | 用途 |
|---------|------|------|
| 主色 | #22C55E | 品牌标识、主要按钮、重要信息强调 |
| 主色暗 | #16A34A | 按钮 hover 状态、边框 |
| 主色亮 | #4ADE80 | 次要按钮、背景色 |

### 2.2 辅助色

| 颜色名称 | 色值 | 用途 |
|---------|------|------|
| 成功色 | #22C55E | 成功状态、确认按钮、主要操作 |
| 警告色 | #F59E0B | 警告状态、提示信息 |
| 错误色 | #EF4444 | 错误状态、删除按钮 |
| 信息色 | #3B82F6 | 信息提示、链接 |
| 紫色 | #8B5CF6 | AI功能、特殊强调 |
| 青色 | #06B6D4 | 科技感功能、特殊强调 |

### 2.3 中性色

| 颜色名称 | 色值 | 用途 |
|---------|------|------|
| 白色 | #FFFFFF | 背景色、文本背景 |
| 浅灰 | #F3F4F6 | 页面背景、卡片背景 |
| 中灰 | #E5E7EB | 边框、分隔线 |
| 深灰 | #6B7280 | 次要文本、提示信息 |
| 黑色 | #111827 | 主要文本、标题 |

### 2.4 渐变背景

| 渐变名称 | 色值 | 用途 |
|---------|------|------|
| 绿色渐变 | from-green-100 to-green-200 | 默认头像背景、成功状态 |
| 紫色渐变 | from-purple-100 to-purple-200 | AI功能模块、创意功能 |
| 蓝色渐变 | from-blue-100 to-blue-200 | 信息提示模块 |
| 登录页渐变 | from-green-50 via-blue-50 to-indigo-100 | 登录/注册页面背景 |

## 3. 字体规范

### 3.1 字体选择

- **主要字体**：系统默认无衬线字体
  - Web端：Roboto、San Francisco、PingFang SC
  - Android端：Roboto、Noto Sans SC

### 3.2 字体大小

| 字体类型 | 大小 | 用途 |
|---------|------|------|
| 标题1 | 24px | 页面标题 |
| 标题2 | 20px | 模块标题 |
| 标题3 | 18px | 卡片标题 |
| 正文 | 16px | 主要文本 |
| 小文本 | 14px | 辅助信息、说明文本 |
| 微型文本 | 12px | 标签、时间戳 |

### 3.3 字体权重

| 权重 | 用途 |
|------|------|
| 700 (Bold) | 标题、重要信息 |
| 500 (Medium) | 次级标题、强调文本 |
| 400 (Regular) | 正文、普通文本 |

## 4. 布局规范

### 4.1 网格系统

- **Web端**：12列网格，间距16px
- **Android端**：8列网格，间距16px

### 4.2 间距规范

| 间距类型 | 大小 | 用途 |
|---------|------|------|
| 微型 | 4px | 小元素间距 |
| 小 | 8px | 元素内间距 |
| 中 | 16px | 模块间距 |
| 大 | 24px | 页面区块间距 |
| 超大 | 32px | 页面顶部/底部间距 |

### 4.3 响应式断点

| 断点 | 设备类型 | 布局调整 |
|------|---------|----------|
| < 768px | 移动设备 | 单列布局，折叠菜单 |
| 768px - 1024px | 平板设备 | 双列布局，简化导航 |
| > 1024px | 桌面设备 | 多列布局，完整导航 |

## 5. 组件规范

### 5.1 按钮

| 按钮类型 | 样式 | 尺寸 |
|---------|------|------|
| 主要按钮 | 主色背景，白色文本 | 高度48px，圆角8px |
| 次要按钮 | 白色背景，主色边框和文本 | 高度48px，圆角8px |
| 文本按钮 | 无背景，主色文本 | 高度32px，无圆角 |
| 图标按钮 | 圆形，主色背景，白色图标 | 直径40px |

### 5.2 卡片

- **样式**：白色背景，阴影效果，圆角8px
- **阴影**：box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1)
- **内边距**：16px
- **间距**：16px

### 5.3 输入框

- **样式**：白色背景，灰色边框，圆角4px
- **高度**：48px
- **内边距**：12px
- **焦点状态**：主色边框，轻微阴影

### 5.4 列表

- **样式**：白色背景，底部边框
- **高度**：56px
- **内边距**：16px
- **hover状态**：浅灰色背景

### 5.5 导航栏

- **Web端**：顶部固定，白色背景，阴影效果
- **Android端**：顶部固定，主色背景，白色文本
- **高度**：64px (Web) / 56px (Android)

### 5.6 底部导航

- **仅Android端**：底部固定，白色背景，阴影效果
- **高度**：56px
- **图标大小**：24px
- **文本大小**：12px

### 5.7 头像组件

#### 5.7.1 默认头像规范

| 尺寸 | 应用场景 |
|------|---------|
| 40px (w-10) | 列表项、表格行 |
| 32px (w-8) | 导航栏用户头像 |
| 96px (w-24) | 个人信息页面 |

#### 5.7.2 头像样式

- **有头像**：圆形图片，`object-cover`，灰色边框
- **无头像**：绿色渐变背景（`from-green-100 to-green-200`）+ 用户图标
- **悬停效果**：边框变为主色（`hover:border-green-400`），过渡动画200ms

#### 5.7.3 头像HTML模板

```html
<!-- 有头像时显示图片 -->
<img 
  v-if="avatarUrl" 
  :src="avatarUrl" 
  :alt="displayName"
  class="rounded-full object-cover border-2 border-gray-200 hover:border-green-400 transition-all duration-200"
/>

<!-- 无头像时显示默认图标 -->
<div v-else class="rounded-full bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center border-2 border-gray-200 hover:border-green-400 transition-all duration-200">
  <svg xmlns="http://www.w3.org/2000/svg" class="text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
  </svg>
</div>
```

### 5.8 卡片组件

- **样式**：白色背景，阴影效果，圆角12px（`rounded-xl`）
- **阴影**：`shadow-lg`，hover时增强为`shadow-xl`
- **内边距**：16px
- **间距**：16px
- **悬停效果**：上移2px（`hover:-translate-y-1`），过渡动画300ms

### 5.9 弹框（Modal）组件

#### 5.9.1 弹框结构

| 层级 | 样式 | 说明 |
|------|------|------|
| 遮罩层 | `fixed inset-0 bg-black/50` | 半透明黑色背景 |
| 弹框容器 | `rounded-xl shadow-2xl` | 圆角卡片样式 |
| 弹框内容 | `max-w-md w-full p-6` | 居中白色面板 |

#### 5.9.2 弹框动画

| 动画 | 层级 | 效果 |
|------|------|------|
| `animate-fade-in` | 遮罩层 | 淡入效果 300ms |
| `animate-scale-in` | 弹框内容 | 缩放进入 300ms |

#### 5.9.3 弹框HTML模板

```html
<!-- 遮罩层 -->
<div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showModal = false">
  <!-- 弹框内容 -->
  <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
    <!-- 标题区 -->
    <div class="flex items-center mb-4">
      <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center mr-3">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="..." />
        </svg>
      </div>
      <h3 class="text-lg font-semibold text-gray-900">弹框标题</h3>
    </div>
    <!-- 表单内容 -->
    <form>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1.5">标签</label>
          <input type="text" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
        </div>
      </div>
      <!-- 按钮区 -->
      <div class="mt-6 flex justify-end space-x-3">
        <button type="button" class="px-4 py-2.5 border border-gray-200 rounded-xl text-sm font-medium text-gray-700 hover:bg-gray-50 hover:border-gray-300 transition-all duration-200">
          取消
        </button>
        <button type="submit" class="px-4 py-2.5 bg-green-500 text-white rounded-xl text-sm font-medium shadow-md hover:shadow-lg hover:bg-green-600 hover:-translate-y-0.5 transition-all duration-200">
          确认
        </button>
      </div>
    </form>
  </div>
</div>
```

#### 5.9.4 不同功能弹框的图标颜色

| 功能类型 | 图标渐变 | 图标颜色 |
|---------|---------|---------|
| 用户/成员相关 | `from-green-100 to-green-200` | `text-green-600` |
| 家族相关 | `from-purple-100 to-purple-200` | `text-purple-600` |
| 编辑相关 | `from-blue-100 to-blue-200` | `text-blue-600` |
| 日期/事件相关 | `from-orange-100 to-orange-200` | `text-orange-600` |
| 媒体相关 | `from-pink-100 to-pink-200` | `text-pink-600` |
| 关系相关 | `from-amber-100 to-amber-200` | `text-amber-600` |
| 安全/管理员相关 | `from-cyan-100 to-cyan-200` | `text-cyan-600` |

## 6. 图标规范

### 6.1 图标风格

- **风格**：线性图标，简约现代
- **线条粗细**：2px
- **圆角**：4px

### 6.2 常用图标

| 图标名称 | 用途 |
|---------|------|
| 家族树 | 家族树页面入口 |
| 成员 | 成员管理页面入口 |
| 历史 | 历史记录页面入口 |
| 媒体 | 多媒体库页面入口 |
| 设置 | 系统设置页面入口 |
| 添加 | 添加操作 |
| 编辑 | 编辑操作 |
| 删除 | 删除操作 |
| 搜索 | 搜索功能 |
| 分享 | 分享功能 |

## 7. 页面设计

### 7.1 登录/注册页

- **布局**：居中布局，简洁大方
- **元素**：logo、应用名称、登录表单、注册链接、忘记密码链接
- **视觉效果**：主色背景渐变，突出品牌形象

### 7.2 首页

- **布局**：卡片式布局，信息丰富
- **元素**：家族概览卡片、最近活动列表、快速入口图标、通知中心
- **视觉效果**：清晰的信息层次，快捷的功能入口

### 7.3 家族树页

- **布局**：全屏布局，交互丰富
- **元素**：家族树可视化区域、控制按钮（缩放、平移）、成员信息卡片
- **视觉效果**：直观的树形结构，流畅的交互体验

### 7.4 成员列表页

- **布局**：列表布局，信息清晰
- **元素**：搜索框、筛选器、成员列表（头像、姓名、关系）、添加按钮
- **视觉效果**：整齐的列表，方便的筛选功能

### 7.5 成员详情页

- **布局**：卡片式布局，信息完整
- **元素**：成员照片、基本信息（姓名、性别、出生日期等）、编辑按钮、相关成员
- **视觉效果**：详细的信息展示，便捷的编辑功能

### 7.6 历史记录页

- **布局**：时间轴布局，条理清晰
- **元素**：时间轴、事件卡片（标题、描述、日期、照片）、添加按钮
- **视觉效果**：直观的时间顺序，丰富的事件信息

### 7.7 多媒体库页

- **布局**：网格布局，视觉丰富
- **元素**：媒体缩略图、上传按钮、分类筛选、搜索框
- **视觉效果**：美观的媒体展示，方便的管理功能

### 7.8 家族管理页

- **布局**：表单布局，功能集中
- **元素**：家族信息表单、成员列表、邀请按钮、权限管理
- **视觉效果**：清晰的功能分区，便捷的管理操作

### 7.9 系统设置页

- **布局**：列表布局，选项清晰
- **元素**：设置选项列表（通知设置、隐私设置、数据备份等）、开关按钮、操作按钮
- **视觉效果**：简洁的选项展示，明确的操作反馈

## 8. 交互设计

### 8.1 动画效果

#### 8.1.1 页面进入动画

| 动画名称 | 效果 | 持续时间 | 应用场景 |
|---------|------|---------|---------|
| `animate-fade-in` | 淡入效果 | 500ms | 页面整体进入 |
| `animate-slide-up` | 从下方滑入 | 500ms | 卡片、表单区域 |
| `animate-slide-in-left` | 从左侧滑入 | 400ms | 侧边栏、次要内容 |
| `animate-slide-in-right` | 从右侧滑入 | 400ms | 详情面板 |
| `animate-scale-in` | 缩放进入 | 300ms | 弹窗、模态框 |

#### 8.1.2 元素交互动画

| 动画类型 | 效果 | 持续时间 | 应用场景 |
|---------|------|---------|---------|
| 悬停缩放 | `hover:scale-105` | 200ms | 卡片、按钮 |
| 悬停上移 | `hover:-translate-y-1` | 300ms | 卡片 |
| 按钮反馈 | `hover:-translate-y-0.5` | 200ms | 按钮 |
| 边框变化 | `hover:border-green-400` | 200ms | 头像、卡片 |
| 阴影增强 | `hover:shadow-lg` | 200ms | 卡片、按钮 |

#### 8.1.3 CSS动画代码

```css
/* 页面进入动画 */
.animate-fade-in {
  animation: fade-in 0.5s ease-out forwards;
}

.animate-slide-up {
  animation: slide-up 0.5s ease-out forwards;
}

.animate-slide-in-left {
  animation: slide-in-left 0.4s ease-out forwards;
}

.animate-slide-in-right {
  animation: slide-in-right 0.4s ease-out forwards;
}

.animate-scale-in {
  animation: scale-in 0.3s ease-out forwards;
}

@keyframes fade-in {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slide-up {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes slide-in-left {
  from { opacity: 0; transform: translateX(-20px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes slide-in-right {
  from { opacity: 0; transform: translateX(20px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes scale-in {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}
```

### 8.2 手势操作

- **Web端**：支持鼠标悬停、点击、拖拽操作
- **Android端**：支持点击、滑动、长按操作

### 8.3 反馈机制

- **成功反馈**：操作成功后显示绿色提示信息，持续2秒
- **错误反馈**：操作失败后显示红色错误信息，持续3秒
- **加载反馈**：操作过程中显示加载动画，避免用户等待焦虑

### 8.4 按钮交互规范

| 按钮类型 | 样式 | 悬停效果 |
|---------|------|---------|
| 主要按钮 | 绿色背景、白色文本、圆角12px | 背景加深、阴影增强、轻微上移 |
| 次要按钮 | 白色背景、绿色边框、绿色文本 | 边框加深、阴影增强 |
| 文本按钮 | 无背景、绿色文本 | 文本颜色加深 |

### 8.5 列表项交互

- **悬停效果**：背景色变浅（`hover:bg-green-50`），过渡200ms
- **选中状态**：边框高亮（`border-green-500`），背景色高亮（`bg-green-50`）

## 9. 无障碍设计

### 9.1 视觉无障碍

- **颜色对比度**：确保文本与背景的对比度符合WCAG AA标准
- **字体大小**：支持字体大小调整，适应不同视力用户
- **图标描述**：为图标添加文字描述，方便屏幕阅读器识别

### 9.2 操作无障碍

- **键盘导航**：支持键盘Tab键导航，确保所有可交互元素都能被访问
- **屏幕阅读器**：兼容屏幕阅读器，为所有元素添加适当的ARIA属性
- **触控目标**：确保触控目标大小不小于48x48px，方便手指操作

## 10. 设计资源

### 10.1 图标资源

- **来源**：使用Material Design Icons或Font Awesome
- **格式**：SVG格式，确保在不同设备上显示清晰

### 10.2 图片资源

- **格式**：JPG、PNG、WebP
- **分辨率**：根据设备类型和用途选择适当的分辨率
- **压缩**：对图片进行适当压缩，确保加载速度

### 10.3 设计工具

- **设计软件**：Figma、Sketch、Adobe XD
- **原型工具**：Figma、Axure RP
- **协作工具**：Zeplin、InVision