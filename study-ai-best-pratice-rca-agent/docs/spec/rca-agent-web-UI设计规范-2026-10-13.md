# UI 设计规范：rca-agent-web（RCA 根因分析控制台）

## 基本信息

| 项目 | 内容 |
|------|------|
| Spec编号 | SPEC-20261013-01 |
| 模块 | rca-agent-web |
| 创建日期 | 2026-10-13 |
| 状态 | 已实施 |
| 技术栈 | Vue 3 + vue-router 4 + Element Plus 2.7 + Vite 5 |
| 基线规范 | common-ui-design-spec（Ant Design / Material 3 / WCAG 2.2 AA） |

## 1. 产品定位

面向研发和 SRE 的根因分析控制台：提交 RCA 分析请求、浏览分析历史、查看录制明细、回放对比、监控服务健康。属于**数据密集型工具台**，设计目标是信息密度与可读性平衡，不做营销化视觉。

## 2. 设计令牌（Design Tokens）

### 2.1 颜色系统

沿用 Element Plus 默认主题色，不自定义色板，保证组件状态一致性。

| 令牌 | 色值 | 用途 |
|------|------|------|
| primary | `#409EFF` | 品牌主色、主按钮、激活导航、链接 |
| success | `#67C23A` | CONFIRMED 结论、SUPPORT 证据、健康 UP |
| warning | `#E6A23C` | SUSPECTED 结论、变更事件、中等置信度 |
| danger | `#F56C6C` | 错误、REFUTE 证据、ALERT 事件、健康 DOWN |
| info | `#909399` | INCONCLUSIVE、中性标签、辅助说明文字 |
| text-primary | `#303133` | 标题、品牌名 |
| text-regular | `#606266` | 正文、次级信息 |
| text-secondary | `#909399` | 提示、muted 文本 |
| border | `#E4E7ED` | 卡片/表单边框、表头分割线 |
| bg-page | `#F5F7FA` | 主内容区底色 |
| bg-card | `#FFFFFF` | 卡片、导航栏底色 |

语义映射（业务状态 → 颜色，全站统一）：

| 业务状态 | 映射 | 出现位置 |
|----------|------|----------|
| resultLevel CONFIRMED / SUSPECTED / INCONCLUSIVE | success / warning / info | 分析结果头标签 |
| verdict SUPPORT / REFUTE / NEUTRAL | success / danger / info | 证据表裁决列 |
| stage SUCCESS / 其他 | success / danger | 阶段轨迹表 |
| timeline ALERT / CHANGE / SIGNAL | danger / warning / primary | 时间线节点 |
| 置信度 ≥60% / 35%~60% / <35% | success / warning / info | 假设全景置信度列 |

### 2.2 排版

| 元素 | 字号 | 字重 | 行高 |
|------|------|------|------|
| 品牌名 | 18px | 600 | 1.2 |
| 卡片标题 | 16px（el-card 默认） | 600 | 1.3 |
| 正文/表单 | 14px | 400 | 1.5 |
| 辅助说明（form-tip/muted） | 12px | 400 | 1.4 |
| 字体族 | `Helvetica Neue, Arial, PingFang SC, Microsoft YaHei, sans-serif`（Element Plus 默认） | | |

### 2.3 间距 / 圆角 / 阴影

| 令牌 | 值 | 用途 |
|------|-----|------|
| 间距基准 | 4px 倍数 | 组件内距 8/12/16px |
| 页面padding | 20px | el-main |
| 卡片间距 | 16px（`mb-16`） | 卡片纵向堆叠 |
| 圆角 | 4px（Element 默认） | 卡片、按钮、输入框 |
| 导航阴影 | `0 1px 4px rgba(0,21,41,.08)` | 顶栏 |
| 卡片阴影 | Element `shadow="hover"` / `never` | 结论卡 hover 浮起 |

## 3. 布局规范

```
┌──────────────────────────────────────────────────────┐
│ 顶栏 60px：品牌图标+名称 │ 水平导航菜单（5 项，router 联动）│
├──────────────────────────────────────────────────────┤
│ el-main（#F5F7FA，padding 20px，纵向滚动）              │
│   页面内容：卡片堆叠，最大宽度不限制，密度优先             │
└──────────────────────────────────────────────────────┘
```

- 顶栏白底 + 底部 1px 边框 + 轻阴影，固定在视口顶部（el-container height: 100vh）。
- 导航菜单 `mode="horizontal"`，`router` 模式，当前路由高亮（主色下划线）。
- 内容区卡片纵向堆叠，卡片间距 16px。
- 响应式：本控制台面向桌面端（≥992px 完整布局）；<768px 时导航允许横向滚动，表格允许横向滚动，不做移动端专门适配（内部工具）。

## 4. 页面规范

### 4.1 RCA 分析（/analyze）

| 区块 | 组件 | 规范 |
|------|------|------|
| 请求表单 | el-card + el-form | label 右对齐宽 120px；告警描述 textarea 3 行；场景下拉含 7 个 Mock 场景；两个开关带 12px info 色提示文字 |
| 操作区 | el-button | 主按钮「执行分析」primary；次按钮「Mock 场景一键分析」默认；两者 loading 态同步 |
| 结果卡 | el-card | 头部左侧标题，右侧 resultLevel 大标签 + LLM 调用状态标签（info） |
| 元信息 | el-descriptions | 3 列 border small，告警描述独占 3 列 |
| 结果明细 | el-tabs 5 页签 | 结论与建议（timeline+卡片区）/ 阶段轨迹（表）/ 假设全景（表）/ 时间线（timeline）/ 证据（表） |
| 空态 | el-empty | 无结果时显示「提交请求后在此展示分析结果」 |

### 4.2 分析历史（/history）

- el-table 展示历史记录，支持点击行查看详情；空态用 el-empty；加载态用 `v-loading`。
- 表格状态规范：加载中骨架/遮罩、空状态文案、错误状态带重试按钮。

### 4.3 录制明细（/recording）

- 录制列表（表格）+ 明细抽屉/展开区；工具入参出参为 JSON，用等宽字体（`Menlo, Consolas, monospace`）13px 展示，超长折叠。

### 4.4 回放对比（/replay）

- 双栏对比布局（el-row :gutter=16，各 span 12）；差异字段用 warning 色高亮；一致用 success。

### 4.5 服务健康（/health）

- 服务卡片栅格（el-row/el-col，span 8）；每个服务一个卡片：名称、UP/DOWN 状态标签（success/danger）、关键指标描述列表；DOWN 时卡片顶部 2px danger 色条。

## 5. 组件与状态通用规范

| 组件 | 规范 |
|------|------|
| 按钮 | 主操作 primary，次操作默认；异步操作必须 loading 态并禁用重复点击 |
| 表格 | `border stripe size="small"`；状态列一律用 el-tag 着色（映射见 2.1）；空数据显示 el-empty |
| 表单 | 必填项 label 前置红星；提交前本地校验，失败用 ElMessage.warning；服务端错误用 ElMessage.error 并携带后端 message |
| 反馈 | 操作成功 ElMessage.success；警告 warning；错误 error；页面级错误用 el-alert |
| 加载 | 按钮级 `:loading`；区块级 `v-loading`；页面级骨架屏 |
| 标签 | 状态着色统一按 2.1 语义映射表，禁止页面内自定义同色异义 |

状态优先级（与基线一致）：`禁用 > 加载 > 错误 > 成功 > 聚焦 > 悬停 > 默认`。

## 6. 无障碍（WCAG 2.2 AA）

| 项 | 要求 |
|----|------|
| 对比度 | 正文 ≥4.5:1；Element 默认主题文字均满足；info 色 `#909399` 仅用于 12px 辅助文字且底色为白（对比度 3.5:1，限非关键信息） |
| 键盘 | 表单 Tab 顺序 = 视觉顺序；菜单/开关/下拉均可键盘操作（Element 原生支持） |
| 标签关联 | el-form-item label 与控件自动关联；图标按钮必须有 aria-label |
| 状态传达 | 状态不只依赖颜色：状态列同时使用文字（SUCCESS/CONFIRMED 等）+ 颜色 |
| 触摸目标 | 桌面端不强制 44px；按钮默认 32px 高度可接受 |

## 7. 验收标准

| 编号 | 验收项 | 标准 | 状态 |
|------|--------|------|------|
| AC-1 | 设计令牌统一 | 全站颜色/字号/间距来自第 2 节令牌，无散落的硬编码异义色 | ✅ |
| AC-2 | 语义色一致 | 5 个页面状态着色与 2.1 映射表一致 | ✅ |
| AC-3 | 空态/加载/错误 | 每页具备空态、加载态、错误提示 | ✅ |
| AC-4 | 布局 | 顶栏固定、导航路由高亮、卡片 16px 间距、内容区 #F5F7FA | ✅ |
| AC-5 | 表单反馈 | 分析页空告警描述拦截 warning；请求失败 error 提示 | ✅ |
| AC-6 | UI 自动化 | Playwright 覆盖 5 页面核心路径（见 docs/report UI 测试报告） | ✅ |

## 8. 参考

- [common-ui-design-spec 基线规范](common-ui-design-spec)
- [rca-agent-接口文档-2026-10-11](../api/rca-agent-接口文档-2026-10-11.md)
- [rca-record-replay-需求文档-2026-10-11](../requirement/rca-record-replay-需求文档-2026-10-11.md)
