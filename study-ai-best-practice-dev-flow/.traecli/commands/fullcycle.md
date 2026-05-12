# /fullcycle 命令
# 全流程总指挥命令

## 功能描述
执行完整的开发周期，从需求到部署。

## 自动执行的任务序列

1. `/start` - 需求澄清与启动
   - 头脑风暴需求
   - 生成需求文档
   - 拆解子任务

2. `/design` - 技术方案与评审
   - 生成数据库设计
   - 生成 API 定义
   - 安全和性能评审

3. SOLO 并行开发
   - 后端开发（单元测试覆盖率≥85%）
   - 前端开发（Playwright 测试）

4. 集成验证
   - 后端单元测试
   - 前端 Playwright 测试
   - 生成测试报告

5. `/deploy-config` - 部署配置
   - 生成 Dockerfile
   - 生成 docker-compose.yml
   - 生成 CI/CD 流水线

6. `/report` - 进度报告
   - 汇总需求状态
   - 汇总代码状态
   - 汇总测试状态
   - 生成最终报告

## 使用方式

```
/fullcycle [需求描述]
```

## 示例

```
/fullcycle 实现用户历史轨迹功能
```

## 输出

- 需求文档：`docs/requirements/REQ_[模块]_v[版本].md`
- 设计文档：`docs/design/DESIGN_[模块]_v[版本].md`
- 测试报告：`docs/reports/TEST_[日期].md`
- 部署配置：项目根目录