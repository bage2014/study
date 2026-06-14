# Price Compare Frontend

价格比较应用前端 - 基于 Vue 3 + Vite，提供商品价格对比和地址管理的 Web 界面。

## 项目特性

- **商品搜索**：输入商品名称查询三个平台价格
- **价格对比**：直观展示美团、京东外卖、淘宝闪购价格
- **智能推荐**：高亮显示最低价平台
- **地址管理**：添加、管理收货地址
- **响应式设计**：支持移动端和桌面端

## 技术栈

- Vue 3 (Composition API)
- Vite 5
- Axios
- CSS3

## 项目结构

```
study-ai-app-compares-frontend/
├── src/
│   ├── main.js                       # Vue入口
│   ├── App.vue                       # 主应用组件
│   ├── services/
│   │   └── api.js                    # API服务封装
│   └── components/
│       └── AddressModal.vue          # 地址管理弹窗
├── index.html
├── vite.config.js
└── package.json
```

## 快速开始

### 1. 环境要求

- Node.js 18+
- npm 9+

### 2. 安装依赖

```bash
npm install
```

### 3. 开发模式

```bash
npm run dev
```

访问地址：http://localhost:5173

### 4. 生产构建

```bash
npm run build
```

### 5. 预览构建结果

```bash
npm run preview
```

## 配置说明

### 后端 API 地址

开发环境下，前端通过 Vite 代理访问后端 API：

```javascript
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

生产环境下，需要配置环境变量或修改 `src/services/api.js` 中的 `baseURL`。

## 功能说明

### 价格对比

1. 在搜索框输入商品名称（如"生椰拿铁"）
2. 选择收货地址
3. 点击搜索按钮
4. 查看三个平台的价格对比结果
5. 系统自动推荐最低价平台

### 地址管理

1. 点击"添加地址"按钮
2. 填写收货人姓名、手机号码、省市区、详细地址
3. 可选"设为默认地址"
4. 点击保存

## API 接口

前端调用的后端接口：

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/compare/search` | POST | 搜索并比较价格 |
| `/api/addresses` | GET | 获取地址列表 |
| `/api/addresses` | POST | 创建地址 |
| `/api/addresses/{id}` | GET | 获取单个地址 |
| `/api/addresses/{id}/update` | POST | 更新地址 |
| `/api/addresses/{id}/delete` | POST | 删除地址 |

## License

MIT