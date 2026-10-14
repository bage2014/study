# 测试报告：rca-agent-web UI 自动化测试

## 基本信息

| 项目 | 内容 |
|------|------|
| 报告编号 | TR-20261013-01 |
| 模块 | rca-agent-web |
| 测试类型 | UI 端到端测试（Playwright 1.64 + Chromium） |
| 测试环境 | 本地 dev（vite 5173）+ 后端 8082 |
| 创建日期 | 2026-10-13 |

## 测试范围

依据 [rca-agent-web-UI设计规范](../spec/rca-agent-web-UI设计规范-2026-10-13.md) 第 7 节验收标准，覆盖 5 个页面核心路径。API 层全部 `page.route` 打桩，测试可离线重复执行；另做一轮真实后端浏览器人工实测。

## 测试用例统计

| 类别 | 数量 |
|------|------|
| 测试用例总数 | 15 |
| 通过 | 15 |
| 失败 | 0 |
| 通过率 | 100% |

## 用例清单

| 文件 | 用例 | 验证点 |
|------|------|--------|
| e2e/navigation.spec.js | 默认路由重定向 | `/` → `/analyze` |
| e2e/navigation.spec.js | 导航到 5 个页面 ×5 | URL、标题、菜单高亮 is-active |
| e2e/navigation.spec.js | 品牌区 | 平台名称展示 |
| e2e/analyze.spec.js | 告警描述为空拦截 | warning 提示、不发请求、保持空态 |
| e2e/analyze.spec.js | Mock 场景一键分析 | 结果卡、等级标签、LLM 标签、5 个 Tab 内容（结论/阶段/假设/时间线/证据） |
| e2e/analyze.spec.js | 后端 500 | error 提示 |
| e2e/pages.spec.js | 历史列表渲染 | 表格行、CONFIRMED 标签 |
| e2e/pages.spec.js | 录制列表与明细空态 | 列表行、空态文案 |
| e2e/pages.spec.js | 回放页空态 | 选择框占位、执行回放按钮禁用 |
| e2e/pages.spec.js | 健康 UP | 「服务运行中」+ 服务名 |
| e2e/pages.spec.js | 健康检查失败 | error 提示 + 空态 |

## 真实后端浏览器实测（人工）

| 步骤 | 结果 |
|------|------|
| `/` 跳转 /analyze，品牌+导航+表单渲染 | ✅ |
| Mock 一键分析（真实后端 8082） | ✅ 结果卡 + INCONCLUSIVE + 5 Tab 均有内容 |
| /history 出现刚完成的分析记录（H2 落库） | ✅ |
| /health 显示 UP / rca-agent | ✅ |
| Console | 仅 1 条 Element Plus ElTag prop 类型警告，无错误 |

## 执行方式

```bash
cd rca-agent-web
npm install
npx playwright install chromium --only-shell
npx playwright test        # 需 dev server 运行在 5173（npm run dev）
```

## 缺陷与修复记录（测试开发过程中）

| 问题 | 原因 | 修复 |
|------|------|------|
| 首批 14 用例全挂 | 5173 被 9/24 残留旧 vite 进程占用，页面 JS 未执行 | 清理残留进程重启 dev server |
| 历史/回放页桩数据不生效 | 桩体用了 Spring Page 结构，前端实际契约是 `{items,total}` | 修正桩体 |
| 回放页空态断言失败 | el-empty 仅在 original/replayed 存在时渲染 | 改为断言选择框占位+按钮禁用 |
| strict mode violation | 多个 Tab 页签表格同时渲染导致 `.el-table__row` 多匹配 | 断言加 `.first()` |

## 测试结论

15/15 通过 + 真实链路实测通过，符合设计规范 AC-6。建议后续把 `npx playwright test` 接入 CI，并补充录制明细交互、回放执行两条真实链路用例。
