# TODO-MANUAL.md - 人工处理待办事项

> 本文档列出所有**必须由人工完成**的任务，无法通过代码自动化实现。

---

## 一、API权限申请（核心任务）

### 1.1 淘宝/天猫API权限申请

**状态**: ⏳ 待申请

**申请地址**: https://open.taobao.com/

**申请步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 注册淘宝开放平台账号 | 使用企业支付宝账号注册 |
| 2 | 创建应用 | 选择"网页应用"类型 |
| 3 | 填写应用信息 | 应用名称、描述、域名等 |
| 4 | 提交审核 | 等待淘宝审核通过 |
| 5 | 获取App Key和App Secret | 审核通过后在应用详情页获取 |
| 6 | 申请API权限 | 需要申请"商品搜索"、"订单查询"等接口权限 |

**所需资料**:
- 企业营业执照
- 企业支付宝账号
- 网站域名备案信息
- 应用功能说明文档

**配置位置**:
```yaml
# application.yml
platform:
  taobao:
    enabled: true
    app-key: YOUR_TAOBAO_APP_KEY
    app-secret: YOUR_TAOBAO_APP_SECRET
```

---

### 1.2 京东API权限申请

**状态**: ⏳ 待申请

**申请地址**: https://open.jd.com/

**申请步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 注册京东开放平台账号 | 使用企业账号注册 |
| 2 | 创建应用 | 选择应用类型（ISV应用） |
| 3 | 填写应用信息 | 应用名称、描述、回调地址等 |
| 4 | 提交审核 | 等待京东审核 |
| 5 | 获取App Key和App Secret | 审核通过后获取 |
| 6 | 获取Access Token | 需要用户授权或使用ISV令牌 |
| 7 | 申请API权限 | 需要申请"商品搜索"、"价格查询"等接口 |

**所需资料**:
- 企业营业执照
- 企业对公账户信息
- 网站域名备案信息
- 应用功能说明

**配置位置**:
```yaml
# application.yml
platform:
  jd:
    enabled: true
    app-key: YOUR_JD_APP_KEY
    app-secret: YOUR_JD_APP_SECRET
    access-token: YOUR_JD_ACCESS_TOKEN
```

---

### 1.3 美团API权限申请

**状态**: ⏳ 待申请

**申请地址**: https://developer.meituan.com/

**申请步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 注册美团开发者平台账号 | 使用企业账号注册 |
| 2 | 创建应用 | 选择"外卖"或"餐饮"相关分类 |
| 3 | 填写应用信息 | 应用名称、描述、回调地址等 |
| 4 | 提交审核 | 等待美团审核 |
| 5 | 获取App Key和App Secret | 审核通过后获取 |
| 6 | 申请接口权限 | 需要申请"商家搜索"、"商品查询"、"价格查询"等 |

**所需资料**:
- 企业营业执照
- 食品经营许可证（如涉及餐饮）
- 网站域名备案信息
- 应用功能说明文档

**配置位置**:
```yaml
# application.yml
platform:
  meituan:
    enabled: true
    app-key: YOUR_MEITUAN_APP_KEY
    app-secret: YOUR_MEITUAN_APP_SECRET
```

---

## 二、生产环境部署

### 2.1 服务器配置

**状态**: ⏳ 待配置

**配置步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 购买云服务器 | 推荐阿里云ECS或腾讯云CVM，配置至少2核4G |
| 2 | 安装操作系统 | Ubuntu 22.04 LTS 或 CentOS 7+ |
| 3 | 配置安全组 | 开放80、443、8080端口 |
| 4 | 安装Java 21 | 使用SDKMAN或官方包安装 |
| 5 | 安装MySQL 8.0+ | 配置数据库用户和权限 |
| 6 | 安装Redis | 配置密码和持久化 |
| 7 | 安装Nginx | 配置反向代理和HTTPS |

---

### 2.2 数据库配置

**状态**: ⏳ 待配置

**配置步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 创建数据库 | `CREATE DATABASE pricecompare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;` |
| 2 | 创建用户 | `CREATE USER 'pricecompare'@'%' IDENTIFIED BY 'your_password';` |
| 3 | 授权 | `GRANT ALL PRIVILEGES ON pricecompare.* TO 'pricecompare'@'%';` |
| 4 | 配置连接池 | 在application-prod.yml中配置数据库连接 |
| 5 | 初始化数据 | 运行Flyway或手动执行初始化SQL |

**环境变量配置**:
```bash
# .env.production
DB_HOST=your-db-host
DB_PORT=3306
DB_NAME=pricecompare
DB_USERNAME=pricecompare
DB_PASSWORD=your-password
```

---

### 2.3 HTTPS证书配置

**状态**: ⏳ 待配置

**配置步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 申请域名 | 在域名服务商处购买域名 |
| 2 | 域名备案 | 完成ICP备案（中国大陆服务器） |
| 3 | 申请SSL证书 | 使用Let's Encrypt免费证书或购买商业证书 |
| 4 | 配置Nginx | 配置SSL证书路径和HTTPS监听 |
| 5 | 配置强制HTTPS | 重定向HTTP到HTTPS |

**Nginx配置示例**:
```nginx
server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /path/to/fullchain.pem;
    ssl_certificate_key /path/to/privkey.pem;
    
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    location / {
        root /path/to/dist;
        try_files $uri $uri/ /index.html;
    }
}
```

---

## 三、安全配置

### 3.1 API密钥安全管理

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 不硬编码密钥 | 在生产环境使用环境变量 |
| 2 | 使用密钥管理服务 | 推荐使用阿里云KMS、腾讯云密钥管理等 |
| 3 | 定期轮换密钥 | 每90天轮换一次API密钥 |
| 4 | 限制IP访问 | 在API平台后台配置允许访问的IP白名单 |

---

### 3.2 敏感数据加密

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 加密手机号 | 使用AES加密存储用户手机号 |
| 2 | 配置加密密钥 | 在application.yml中配置AES密钥 |
| 3 | 使用Jasypt | 加密配置文件中的敏感信息 |

---

### 3.3 接口限流配置

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 配置接口限流 | 使用Spring Cloud Gateway或Bucket4j |
| 2 | 设置限流规则 | 例如：每个用户每分钟最多调用10次比价接口 |
| 3 | 配置熔断降级 | 使用Resilience4j配置熔断策略 |

---

## 四、监控与告警

### 4.1 日志配置

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 配置日志采集 | 使用ELK Stack或阿里云日志服务 |
| 2 | 配置日志级别 | 生产环境设置为INFO级别 |
| 3 | 配置日志分割 | 使用Logback配置按天分割日志 |
| 4 | 配置日志清理 | 设置日志保留时间（如30天） |

---

### 4.2 指标监控

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 集成Prometheus | 添加Micrometer依赖 |
| 2 | 配置Grafana | 创建监控仪表盘 |
| 3 | 配置关键指标 | API响应时间、错误率、QPS等 |
| 4 | 配置告警规则 | CPU使用率>80%、内存使用率>85%等 |

---

### 4.3 链路追踪

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 集成OpenTelemetry | 添加OTel依赖 |
| 2 | 配置Jaeger或Zipkin | 部署链路追踪服务 |
| 3 | 配置采样率 | 生产环境可设置较低采样率 |

---

## 五、运维管理

### 5.1 自动化部署

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 配置CI/CD | 使用GitHub Actions或GitLab CI |
| 2 | 配置自动构建 | 代码提交后自动编译打包 |
| 3 | 配置自动部署 | 打包完成后自动部署到服务器 |
| 4 | 配置回滚策略 | 部署失败时自动回滚到上一版本 |

**GitHub Actions示例**:
```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Backend
        run: cd backend && mvn clean package
      - name: Deploy to Server
        uses: appleboy/ssh-action@v1.0.3
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SERVER_KEY }}
          script: |
            sudo systemctl stop pricecompare
            cp target/pricecompare.jar /opt/pricecompare/
            sudo systemctl start pricecompare
```

---

### 5.2 进程管理

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 创建Systemd服务 | 配置自动启动和重启 |
| 2 | 配置服务文件 | `/etc/systemd/system/pricecompare.service` |
| 3 | 启用服务 | `systemctl enable pricecompare` |
| 4 | 启动服务 | `systemctl start pricecompare` |

**Systemd服务配置示例**:
```ini
[Unit]
Description=Price Compare Service
After=network.target mysql.service redis.service

[Service]
Type=simple
User=pricecompare
WorkingDirectory=/opt/pricecompare
ExecStart=/usr/bin/java -jar pricecompare.jar --spring.profiles.active=prod
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

---

## 六、业务配置

### 6.1 商品分类配置

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 定义商品分类 | 咖啡、快餐、甜点、生鲜等 |
| 2 | 配置分类映射 | 各平台分类与系统分类的映射关系 |
| 3 | 配置热门搜索词 | 根据业务需求配置热门搜索关键词 |

---

### 6.2 优惠规则配置

**状态**: ⏳ 待配置

**操作步骤**:

| 步骤 | 操作 | 备注 |
|------|------|------|
| 1 | 配置优惠类型 | 满减、折扣、优惠券等 |
| 2 | 配置优惠规则 | 各平台优惠的解析规则 |
| 3 | 配置推荐策略 | 综合价格、配送时间、评分等因素 |

---

## 七、测试验证

### 7.1 真实API测试

**状态**: ⏳ 待测试

**测试步骤**:

| 步骤 | 操作 | 验证内容 |
|------|------|----------|
| 1 | 配置真实API密钥 | 在测试环境配置真实密钥 |
| 2 | 测试商品搜索 | 搜索多个关键词，验证返回结果 |
| 3 | 测试价格对比 | 对比三个平台价格，验证推荐逻辑 |
| 4 | 测试异常场景 | 网络超时、API限流、密钥过期等 |
| 5 | 性能测试 | 使用JMeter进行压测，验证QPS |

---

### 7.2 安全测试

**状态**: ⏳ 待测试

**测试步骤**:

| 步骤 | 操作 | 验证内容 |
|------|------|----------|
| 1 | SQL注入测试 | 验证参数过滤是否有效 |
| 2 | XSS攻击测试 | 验证输入内容是否被正确转义 |
| 3 | 接口鉴权测试 | 验证未授权访问是否被拒绝 |
| 4 | 敏感信息泄露测试 | 验证日志和响应中是否包含敏感信息 |

---

## 八、文档完善

### 8.1 运营文档

**状态**: ⏳ 待编写

**需要编写的文档**:

| 文档 | 内容 | 优先级 |
|------|------|--------|
| 用户操作手册 | 如何使用价格比较功能 | 高 |
| 常见问题解答 | 用户常见问题及解决方案 | 高 |
| 隐私政策 | 用户数据收集和使用说明 | 高 |
| 服务条款 | 服务使用条款 | 高 |

---

### 8.2 技术文档

**状态**: ⏳ 待编写

**需要编写的文档**:

| 文档 | 内容 | 优先级 |
|------|------|--------|
| 部署手册 | 生产环境部署步骤 | 高 |
| API文档 | 使用Swagger生成接口文档 | 高 |
| 架构文档 | 系统架构设计说明 | 中 |
| 故障排查手册 | 常见故障及排查方法 | 中 |

---

## 九、合规性检查

### 9.1 数据合规

**状态**: ⏳ 待检查

**检查项**:

| 检查项 | 要求 | 状态 |
|--------|------|------|
| 用户隐私保护 | 遵守《个人信息保护法》 | ⏳ |
| Cookie政策 | 配置Cookie同意弹窗 | ⏳ |
| 数据加密传输 | 使用HTTPS | ⏳ |
| 数据存储安全 | 敏感数据加密存储 | ⏳ |

---

### 9.2 平台合规

**状态**: ⏳ 待检查

**检查项**:

| 检查项 | 要求 | 状态 |
|--------|------|------|
| API使用规范 | 遵守各平台API使用协议 | ⏳ |
| 数据来源标注 | 明确标注数据来源 | ⏳ |
| 反爬虫措施 | 遵守平台反爬虫规则 | ⏳ |
| 商用授权 | 获取商用使用授权 | ⏳ |

---

## 十、优先级汇总

### 高优先级（必须完成）

| 序号 | 任务 | 预计耗时 | 负责人 |
|------|------|----------|--------|
| 1 | 申请淘宝API权限 | 3-5个工作日 | 待分配 |
| 2 | 申请京东API权限 | 3-5个工作日 | 待分配 |
| 3 | 申请美团API权限 | 3-5个工作日 | 待分配 |
| 4 | 生产服务器配置 | 1-2天 | 待分配 |
| 5 | HTTPS证书配置 | 1天 | 待分配 |
| 6 | 真实API测试 | 2-3天 | 待分配 |

### 中优先级（建议完成）

| 序号 | 任务 | 预计耗时 | 负责人 |
|------|------|----------|--------|
| 7 | 监控告警配置 | 2-3天 | 待分配 |
| 8 | 自动化部署配置 | 2天 | 待分配 |
| 9 | 接口限流配置 | 1-2天 | 待分配 |
| 10 | 用户操作手册编写 | 2天 | 待分配 |

### 低优先级（可延后）

| 序号 | 任务 | 预计耗时 | 负责人 |
|------|------|----------|--------|
| 11 | 链路追踪配置 | 2-3天 | 待分配 |
| 12 | 商品分类配置 | 1天 | 待分配 |
| 13 | 优惠规则配置 | 2天 | 待分配 |
| 14 | 架构文档编写 | 3天 | 待分配 |

---

## 更新记录

| 日期 | 更新内容 | 更新人 |
|------|----------|--------|
| 2026-06-27 | 初版创建 | System |
