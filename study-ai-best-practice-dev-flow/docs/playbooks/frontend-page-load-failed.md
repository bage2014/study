# 前端页面加载失败故障排查手册

## 故障描述

前端页面无法正常加载，出现空白页面或加载错误。

## 常见症状

### 1. 白屏/空白页面

浏览器控制台显示：
```
Failed to load resource: net::ERR_CONNECTION_REFUSED
```

### 2. 模块加载错误

```
Uncaught Error: Cannot find module '@/components/XXX'
```

### 3. API 请求跨域错误

```
Access to XMLHttpRequest at 'http://localhost:8080/api/users'
from origin 'http://localhost:5173' has been blocked by CORS policy
```

## 排查步骤

### 1. 检查开发服务器状态

```bash
# 检查前端服务是否运行
npm run dev

# 检查端口占用
lsof -i :5173
```

### 2. 检查后端服务状态

```bash
# 检查后端服务是否运行
curl http://localhost:8080/api/users
```

### 3. 检查 API 代理配置

确认 `vite.config.js` 配置正确：

```javascript
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
```

### 4. 检查网络请求

打开浏览器开发者工具 Network 面板，查看：
- 请求是否发送
- 响应状态码
- 响应数据格式

## 自动修复步骤

### 方案一：重启服务

```bash
# 停止并重启前端
npm run dev

# 停止并重启后端
mvn spring-boot:run
```

### 方案二：清理缓存并重新安装

```bash
# 清理缓存
rm -rf node_modules/.vite
rm -rf dist

# 重新安装
npm install

# 重新构建
npm run build
```

### 方案三：检查并修复跨域配置

确保后端配置允许前端域名：

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## 预防措施

1. 使用 Vite 代理解决开发环境跨域
2. 配置环境变量管理 API 地址
3. 添加错误边界组件处理加载失败
4. 使用 Loading 状态提升用户体验