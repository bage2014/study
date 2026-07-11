# AI Pipeline UI

基于 Vue 3 + Vite 的 AI 研发流水线前端界面。

## 技术栈

- Vue 3.4+
- Vue Router 4.2+
- Axios 1.6+
- Vite 5.0+

## 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0

## 快速开始

### 安装依赖

```bash
cd ai-pipeline-ui
npm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:8082

### 生产构建

```bash
npm run build
```

### 预览构建结果

```bash
npm run preview
```

## 项目结构

```
ai-pipeline-ui/
├── src/
│   ├── api/           # API 封装
│   │   └── index.js   # Axios 配置与接口定义
│   ├── router/        # 路由配置
│   │   └── index.js   # Vue Router 路由定义
│   ├── views/         # 页面组件
│   │   ├── Home.vue           # 首页：需求录入与流水线启动
│   │   ├── PipelineList.vue   # 流水线列表
│   │   └── PipelineDetail.vue # 流水线详情：阶段进度、生成代码
│   ├── App.vue        # 根组件
│   └── main.js        # 应用入口
├── index.html         # HTML 模板
├── vite.config.js     # Vite 配置
├── package.json       # 依赖配置
└── .gitignore         # Git 忽略配置
```

## API 代理配置

开发模式下，`/api` 请求会代理到 Gateway 服务：

```javascript
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8083',
      changeOrigin: true
    }
  }
}
```

## 路由配置

| 路径 | 组件 | 说明 |
|------|------|------|
| `/` | Home | 首页，需求录入 |
| `/pipelines` | PipelineList | 流水线列表 |
| `/pipelines/:id` | PipelineDetail | 流水线详情 |

## 端口说明

- 前端服务：8082
- Gateway 服务：8083（后端代理）
- API 服务：8080（后端核心）
