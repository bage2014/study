# Nginx 技术总结

## 1. 基础概念

### 1.1 什么是 Nginx
Nginx 是一款轻量级的 Web 服务器、反向代理服务器和电子邮件（IMAP/POP3）代理服务器。它由俄罗斯程序员 Igor Sysoev 开发，于 2004 年首次公开发布。Nginx 以其高性能、稳定性、丰富的功能集、简单的配置和低资源消耗而闻名。

### 1.2 Nginx 的核心价值
- **高性能**：采用事件驱动架构，支持高并发连接
- **稳定性**：经过大规模生产环境验证，可靠性高
- **低资源消耗**：内存占用少，CPU 使用率低
- **功能丰富**：支持反向代理、负载均衡、缓存、限流等多种功能
- **易于配置**：配置文件简洁明了，易于理解和维护
- **热部署**：支持在不停止服务的情况下更新配置和升级版本
- **跨平台**：支持 Linux、Windows、macOS 等多种操作系统

### 1.3 Nginx 与其他 Web 服务器的对比

| 服务器 | 优势 | 劣势 |
|--------|------|------|
| Nginx | 高并发处理能力强，内存消耗低，配置简单，功能丰富 | 动态内容处理能力相对较弱（需要与其他服务配合） |
| Apache | 功能全面，模块丰富，生态成熟，动态内容处理能力强 | 高并发下性能下降明显，内存消耗大 |
| IIS | 与 Windows 系统集成良好，管理界面友好 | 仅支持 Windows 平台，性能和灵活性不如 Nginx |
| Tomcat | 专注于 Java Web 应用，支持 Servlet/JSP | 静态资源处理性能不如 Nginx，配置相对复杂 |

## 2. 核心功能

### 2.1 HTTP 服务器
- **静态资源服务**：高效处理 HTML、CSS、JavaScript、图片等静态文件
- **动态内容代理**：将动态请求转发给后端应用服务器（如 Tomcat、Node.js 等）
- **HTTPS 支持**：内置 SSL/TLS 支持，提供安全的 HTTPS 服务
- **HTTP/2 支持**：支持 HTTP/2 协议，提高传输效率

### 2.2 反向代理
- **请求转发**：将客户端请求转发给后端服务器
- **响应代理**：将后端服务器的响应返回给客户端
- **协议转换**：支持 HTTP、HTTPS、TCP、UDP 等多种协议
- **路径重写**：可以修改请求路径，实现 URL 重写

### 2.3 负载均衡
- **轮询**：默认负载均衡策略，按顺序分发请求
- **权重**：根据服务器性能设置不同权重
- **IP Hash**：根据客户端 IP 地址进行哈希计算，确保同一客户端请求始终分发到同一服务器
- **Least Connections**：将请求分发到当前连接数最少的服务器
- **Fair**：根据后端服务器的响应时间进行负载均衡（需要安装第三方模块）
- **URL Hash**：根据请求 URL 进行哈希计算，确保同一 URL 请求始终分发到同一服务器（需要安装第三方模块）

### 2.4 静态资源管理
- **静态文件缓存**：设置静态文件的缓存时间，减少重复请求
- **gzip 压缩**：对静态文件进行压缩，减少传输大小
- **断点续传**：支持 HTTP 断点续传功能
- **Range 请求**：支持 HTTP Range 请求，实现大文件分片下载

### 2.5 虚拟主机
- **基于域名**：通过不同域名区分不同网站
- **基于端口**：通过不同端口区分不同网站
- **基于 IP**：通过不同 IP 地址区分不同网站

### 2.6 安全特性
- **访问控制**：基于 IP、用户认证等方式限制访问
- **DDoS 防护**：通过连接限制、速率限制等方式防御 DDoS 攻击
- **HTTPS 加密**：支持 SSL/TLS 协议，保护数据传输安全
- **WAF 集成**：可以集成 ModSecurity 等 Web 应用防火墙

## 3. 架构与工作原理

### 3.1 整体架构
Nginx 采用模块化设计，主要由以下模块组成：

- **核心模块**：负责基本的 HTTP 服务功能
- **事件模块**：负责事件处理，是 Nginx 高性能的关键
- **HTTP 模块**：处理 HTTP 协议相关功能
- **Mail 模块**：处理邮件代理功能
- **Stream 模块**：处理 TCP/UDP 流量（NGINX 1.9.0+）
- **第三方模块**：扩展 Nginx 功能的外部模块

### 3.2 工作进程模型
Nginx 采用多进程模型，主要包含以下进程：

- **Master 进程**：负责管理 Worker 进程，包括启动、停止、重载配置等
- **Worker 进程**：负责处理实际的客户端请求，数量通常设置为 CPU 核心数
- **Cache Loader 进程**：负责加载缓存数据（可选）
- **Cache Manager 进程**：负责管理缓存大小（可选）

### 3.3 事件处理机制
Nginx 采用事件驱动架构，支持多种事件处理模型：

- **epoll**：Linux 平台下的高效事件处理模型
- **kqueue**：BSD 平台下的高效事件处理模型
- **select**：通用事件处理模型，兼容性好但性能较低
- **poll**：select 的改进版，性能略好于 select

Nginx 的事件处理流程：
1. 初始化事件模块，创建事件监听
2. Worker 进程等待事件发生
3. 当事件发生时（如客户端连接、数据可读/可写），Worker 进程处理事件
4. 处理完事件后，Worker 进程继续等待下一个事件

### 3.4 请求处理流程

**请求处理流程图**
```
+------------------------+
| 1. 连接建立            |
| 客户端 → Nginx TCP连接  |
+------------------------+
            |
            v
+------------------------+
| 2. 请求解析            |
| 解析HTTP请求头         |
+------------------------+
            |
            v
+------------------------+
| 3. 路由匹配            |
| 匹配location配置       |
+------------------------+
            |
            v
+------------------------+
| 4. 请求处理            |
+------------------------+
            |
            +----------------------+
            |                      |
            v                      v
+------------------------+  +------------------------+
| 静态文件请求            |  | 动态请求                |
| 直接读取文件并返回      |  | 转发给后端服务器处理     |
+------------------------+  +------------------------+
            |                      |
            |                      v
            |              +------------------------+
            |              | 接收后端响应            |
            |              +------------------------+
            |                      |
            v                      v
+------------------------+
| 5. 响应处理            |
| 处理响应头和响应体     |
| 发送响应给客户端       |
+------------------------+
            |
            v
+------------------------+
| 6. 连接关闭            |
| 根据HTTP协议决定        |
+------------------------+
```

**详细流程说明**
1. **连接建立**：客户端与 Nginx 建立 TCP 连接
2. **请求解析**：Nginx 解析 HTTP 请求头，获取请求方法、URI、HTTP 版本等信息
3. **路由匹配**：根据请求 URI 匹配对应的 location 配置
4. **请求处理**：
   - 静态文件请求：直接读取文件并返回
   - 动态请求：转发给后端服务器处理
5. **响应处理**：
   - 接收后端服务器的响应（如果是反向代理）
   - 处理响应头和响应体
   - 发送响应给客户端
6. **连接关闭**：根据 HTTP 协议决定是否关闭连接

## 4. 配置管理

### 4.1 配置文件结构
Nginx 的主配置文件通常为 `nginx.conf`，位于 `/etc/nginx/` 或 `/usr/local/nginx/conf/` 目录下。配置文件采用层级结构，主要包含以下部分：

```nginx
# 全局配置
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /var/run/nginx.pid;

# 事件配置
events {
    worker_connections 1024;
    use epoll;
}

# HTTP 配置
http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;
    
    # 日志配置
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
    access_log /var/log/nginx/access.log main;
    
    # 性能优化配置
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    
    # 虚拟主机配置
    server {
        listen 80;
        server_name example.com;
        
        location / {
            root /usr/share/nginx/html;
            index index.html index.htm;
        }
        
        error_page 500 502 503 504 /50x.html;
        location = /50x.html {
            root /usr/share/nginx/html;
        }
    }
    
    # 负载均衡配置
    upstream backend {
        server 192.168.1.100:8080 weight=5;
        server 192.168.1.101:8080 weight=3;
        server 192.168.1.102:8080 backup;
    }
}
```

### 4.2 核心配置指令

#### 4.2.1 全局配置
- **user**：指定 Nginx 工作进程的用户
- **worker_processes**：指定工作进程数量，通常设置为 CPU 核心数
- **error_log**：指定错误日志文件路径和级别
- **pid**：指定 PID 文件路径

#### 4.2.2 事件配置
- **worker_connections**：每个工作进程的最大连接数
- **use**：指定事件处理模型（epoll、kqueue 等）
- **multi_accept**：是否允许同时接受多个连接

#### 4.2.3 HTTP 配置
- **include**：包含其他配置文件
- **default_type**：默认的 MIME 类型
- **log_format**：定义日志格式
- **access_log**：指定访问日志文件路径和格式
- **sendfile**：是否启用 sendfile 系统调用
- **keepalive_timeout**：长连接超时时间
- **gzip**：是否启用 gzip 压缩

#### 4.2.4 服务器配置
- **listen**：监听的端口和 IP 地址
- **server_name**：服务器名称，支持多个域名
- **root**：网站根目录
- **index**：默认索引文件
- **location**：URL 路径匹配规则
- **proxy_pass**：反向代理目标地址
- **upstream**：定义后端服务器集群

### 4.3 常用配置示例

#### 4.3.1 静态网站配置
```nginx
server {
    listen 80;
    server_name example.com;
    
    root /var/www/html;
    index index.html index.htm;
    
    location / {
        try_files $uri $uri/ =404;
    }
    
    # 静态文件缓存
    location ~* \.(css|js|jpg|jpeg|png|gif|ico|svg)$ {
        expires 30d;
        add_header Cache-Control "public, max-age=2592000";
    }
}
```

#### 4.3.2 反向代理配置
```nginx
server {
    listen 80;
    server_name example.com;
    
    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### 4.3.3 负载均衡配置
```nginx
upstream backend {
    # 轮询（默认）
    # server 192.168.1.100:8080;
    # server 192.168.1.101:8080;
    
    # 权重
    server 192.168.1.100:8080 weight=5;
    server 192.168.1.101:8080 weight=3;
    server 192.168.1.102:8080 backup;
    
    # IP Hash
    # ip_hash;
    
    # 最少连接
    # least_conn;
}

server {
    listen 80;
    server_name example.com;
    
    location / {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

#### 4.3.4 HTTPS 配置
```nginx
server {
    listen 443 ssl http2;
    server_name example.com;
    
    ssl_certificate /etc/nginx/ssl/example.com.crt;
    ssl_certificate_key /etc/nginx/ssl/example.com.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384;
    ssl_prefer_server_ciphers off;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    
    location / {
        root /var/www/html;
        index index.html index.htm;
    }
}

# HTTP 重定向到 HTTPS
server {
    listen 80;
    server_name example.com;
    return 301 https://$host$request_uri;
}
```

## 5. 高级特性

### 5.1 限流
Nginx 提供多种限流方式，用于保护后端服务：

#### 5.1.1 连接数限流
```nginx
limit_conn_zone $binary_remote_addr zone=conn_limit_per_ip:10m;

server {
    location / {
        limit_conn conn_limit_per_ip 10; # 每个 IP 最多 10 个连接
        proxy_pass http://backend;
    }
}
```

#### 5.1.2 请求速率限流
```nginx
limit_req_zone $binary_remote_addr zone=req_limit_per_ip:10m rate=10r/s;

server {
    location / {
        limit_req zone=req_limit_per_ip burst=20 nodelay; # 每秒最多 10 个请求，突发 20 个
        proxy_pass http://backend;
    }
}
```

### 5.2 缓存
Nginx 可以缓存后端服务器的响应，提高访问速度：

```nginx
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=10g inactive=60m use_temp_path=off;

server {
    location / {
        proxy_cache my_cache;
        proxy_cache_key "$scheme$request_method$host$request_uri";
        proxy_cache_valid 200 304 10m;
        proxy_cache_valid any 1m;
        proxy_pass http://backend;
    }
}
```

### 5.3 压缩
启用 gzip 压缩可以减少传输大小，提高加载速度：

```nginx
gzip on;
gzip_comp_level 6;
gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
gzip_min_length 1024;
gzip_proxied any;
gzip_vary on;
```

### 5.4 健康检查
通过 ngx_http_upstream_module 模块实现后端服务器健康检查：

```nginx
upstream backend {
    server 192.168.1.100:8080 max_fails=3 fail_timeout=30s;
    server 192.168.1.101:8080 max_fails=3 fail_timeout=30s;
}

server {
    location / {
        proxy_pass http://backend;
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
    }
}
```

### 5.5 高可用部署
使用 Keepalived 实现 Nginx 高可用：

1. **安装 Keepalived**
2. **配置 Keepalived**
3. **启动 Keepalived 服务**

### 5.6 Lua 脚本扩展
通过 ngx_http_lua_module 模块，使用 Lua 脚本扩展 Nginx 功能：

```nginx
location /lua {
    default_type 'text/plain';
    content_by_lua_block {
        ngx.say('Hello, Lua!')
    }
}
```

## 6. 最佳实践

### 6.1 性能优化

- **调整工作进程数**：设置为 CPU 核心数
- **调整连接数**：根据服务器资源合理设置
- **启用 sendfile**：提高文件传输效率
- **优化 TCP 参数**：启用 tcp_nopush 和 tcp_nodelay
- **合理设置缓存**：对静态文件启用浏览器缓存
- **启用压缩**：对文本内容启用 gzip 压缩
- **使用 epoll 事件模型**：提高并发处理能力
- **限制请求体大小**：防止大文件上传攻击

### 6.2 安全配置

- **隐藏版本信息**：server_tokens off
- **限制访问**：使用 allow/deny 指令
- **启用 HTTPS**：配置 SSL/TLS
- **设置安全头部**：如 HSTS、X-XSS-Protection 等
- **防止点击劫持**：X-Frame-Options 头部
- **限制请求方法**：仅允许必要的 HTTP 方法
- **使用 WAF**：集成 ModSecurity 等 Web 应用防火墙

### 6.3 监控与日志

- **启用访问日志**：记录客户端请求
- **启用错误日志**：记录服务器错误
- **使用 JSON 格式日志**：便于日志分析工具处理
- **配置日志轮转**：防止日志文件过大
- **监控 Nginx 状态**：使用 ngx_http_stub_status_module
- **集成监控工具**：如 Prometheus、Grafana 等

### 6.4 部署建议

- **使用容器化部署**：如 Docker
- **使用配置管理工具**：如 Ansible、SaltStack 等
- **实现自动化部署**：CI/CD 流程
- **定期备份配置文件**：防止配置丢失
- **使用版本控制**：管理配置文件变更
- **测试配置**：使用 nginx -t 测试配置语法
- **平滑重启**：使用 nginx -s reload 重载配置

## 7. 面试题解析

### 7.1 常见 Nginx 面试问题

#### 7.1.1 Nginx 是什么？它的主要功能有哪些？
**答案**：
Nginx 是一款轻量级的 Web 服务器、反向代理服务器和电子邮件代理服务器。它的主要功能包括：
- **HTTP 服务器**：提供静态文件服务
- **反向代理**：将请求转发给后端服务器
- **负载均衡**：在多个后端服务器之间分配请求
- **虚拟主机**：在一台服务器上托管多个网站
- **安全特性**：支持 HTTPS、访问控制等
- **高级特性**：限流、缓存、压缩等

#### 7.1.2 Nginx 为什么性能这么高？
**答案**：
Nginx 性能高的原因主要包括：
1. **事件驱动架构**：采用非阻塞 I/O 模型，使用 epoll/kqueue 等高效事件处理机制
2. **多进程模型**：Master-Worker 进程架构，Worker 进程独立处理请求，避免进程间竞争
3. **内存管理**：内存占用低，采用池化内存管理，减少内存碎片
4. **模块化设计**：核心模块精简，只处理基本功能，其他功能通过模块扩展
5. **sendfile 系统调用**：减少数据在内核空间和用户空间之间的复制
6. **连接复用**：支持长连接，减少连接建立和关闭的开销

#### 7.1.3 Nginx 的负载均衡策略有哪些？
**答案**：
Nginx 支持以下负载均衡策略：
1. **轮询**：默认策略，按顺序分发请求
2. **权重**：根据服务器性能设置不同权重
3. **IP Hash**：根据客户端 IP 地址进行哈希计算，确保同一客户端请求始终分发到同一服务器
4. **Least Connections**：将请求分发到当前连接数最少的服务器
5. **Fair**：根据后端服务器的响应时间进行负载均衡（需要安装第三方模块）
6. **URL Hash**：根据请求 URL 进行哈希计算，确保同一 URL 请求始终分发到同一服务器（需要安装第三方模块）

#### 7.1.4 Nginx 和 Apache 的区别是什么？
**答案**：
| 特性 | Nginx | Apache |
|------|-------|--------|
| 架构 | 事件驱动，非阻塞 I/O | 多进程/多线程，阻塞 I/O |
| 并发处理 | 高，支持数万并发连接 | 低，并发连接数有限 |
| 内存消耗 | 低，处理 10000 连接仅需数 MB 内存 | 高，每个连接需要单独的进程/线程 |
| 配置风格 | 简洁明了，基于指令 | 复杂，基于模块和指令 |
| 动态内容处理 | 需要通过 FastCGI、uWSGI 等与后端语言集成 | 内置支持 PHP 等动态语言 |
| 扩展性 | 模块化设计，支持第三方模块 | 丰富的模块生态 |
| 适用场景 | 高并发静态资源服务、反向代理、负载均衡 | 传统 Web 应用，特别是需要大量模块的场景 |

#### 7.1.5 如何配置 Nginx 实现 HTTPS？
**答案**：
配置 Nginx 实现 HTTPS 需要以下步骤：
1. **获取 SSL 证书**：可以从证书颁发机构（CA）购买，或使用 Let's Encrypt 免费证书
2. **配置 Nginx**：
   ```nginx
   server {
       listen 443 ssl http2;
       server_name example.com;
       
       ssl_certificate /path/to/certificate.crt;
       ssl_certificate_key /path/to/private.key;
       ssl_protocols TLSv1.2 TLSv1.3;
       ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384;
       ssl_prefer_server_ciphers off;
       ssl_session_cache shared:SSL:10m;
       ssl_session_timeout 10m;
       
       # 其他配置...
   }
   
   # HTTP 重定向到 HTTPS
   server {
       listen 80;
       server_name example.com;
       return 301 https://$host$request_uri;
   }
   ```
3. **测试配置**：`nginx -t`
4. **重载配置**：`nginx -s reload`

#### 7.1.6 Nginx 如何实现限流？
**答案**：
Nginx 可以通过以下方式实现限流：

1. **连接数限流**：使用 limit_conn 模块
   ```nginx
   limit_conn_zone $binary_remote_addr zone=conn_limit_per_ip:10m;
   
   server {
       location / {
           limit_conn conn_limit_per_ip 10; # 每个 IP 最多 10 个连接
           # 其他配置...
       }
   }
   ```

2. **请求速率限流**：使用 limit_req 模块
   ```nginx
   limit_req_zone $binary_remote_addr zone=req_limit_per_ip:10m rate=10r/s;
   
   server {
       location / {
           limit_req zone=req_limit_per_ip burst=20 nodelay; # 每秒最多 10 个请求，突发 20 个
           # 其他配置...
       }
   }
   ```

#### 7.1.7 Nginx 的请求处理流程是怎样的？
**答案**：
Nginx 的请求处理流程如下：
1. **连接建立**：客户端与 Nginx 建立 TCP 连接
2. **请求解析**：Nginx 解析 HTTP 请求头，获取请求方法、URI、HTTP 版本等信息
3. **路由匹配**：根据请求 URI 匹配对应的 location 配置
4. **请求处理**：
   - 静态文件请求：直接读取文件并返回
   - 动态请求：转发给后端服务器处理
5. **响应处理**：
   - 接收后端服务器的响应（如果是反向代理）
   - 处理响应头和响应体
   - 发送响应给客户端
6. **连接关闭**：根据 HTTP 协议决定是否关闭连接

#### 7.1.8 如何实现 Nginx 的高可用？
**答案**：
实现 Nginx 高可用的常用方法是使用 Keepalived：

1. **安装 Keepalived**：在两台 Nginx 服务器上安装 Keepalived
2. **配置 Keepalived**：
   - 主服务器配置：
     ```conf
     vrrp_instance VI_1 {
         state MASTER
         interface eth0
         virtual_router_id 51
         priority 100
         advert_int 1
         authentication {
             auth_type PASS
             auth_pass 1111
         }
         virtual_ipaddress {
             192.168.1.100
         }
     }
     ```
   - 备服务器配置：
     ```conf
     vrrp_instance VI_1 {
         state BACKUP
         interface eth0
         virtual_router_id 51
         priority 90
         advert_int 1
         authentication {
             auth_type PASS
             auth_pass 1111
         }
         virtual_ipaddress {
             192.168.1.100
         }
     }
     ```
3. **启动服务**：启动 Nginx 和 Keepalived 服务
4. **测试高可用**：当主服务器故障时，虚拟 IP 会自动切换到备服务器

## 8. 参考链接

### 8.1 官方文档
- [Nginx 官方网站](http://nginx.org/en/)
- [Nginx 官方文档](http://nginx.org/en/docs/)
- [Nginx 模块文档](http://nginx.org/en/docs/modules/)

### 8.2 教程资源
- [Nginx 入门指南](https://nginx.org/en/docs/beginners_guide.html)
- [Nginx 管理指南](https://nginx.org/en/docs/admin_guide.html)
- [Nginx 配置示例](https://www.nginx.com/resources/wiki/start/topics/examples/full/)

### 8.3 博客文章
- [Nginx 架构设计解析](https://zhuanlan.zhihu.com/p/398768594)
- [Nginx 性能优化实践](https://m.elecfans.com/article/1974990.html)
- [Nginx 负载均衡配置详解](https://www.tulingxueyuan.cn/tlzx/jsp/1600.html)
- [Nginx 高可用部署方案](https://baijiahao.baidu.com/s?id=1575766392326093&wfr=spider&for=pc)
- [Nginx 安全配置最佳实践](https://www.jianshu.com/p/6777b36cf566)

### 8.4 视频教程
- [Nginx 核心特性详解](https://www.bilibili.com/video/BV1em4y1A7ey/)
- [Nginx 从入门到精通](https://www.bilibili.com/video/BV1W5411w7Gx/)
- [Nginx 实战教程](https://www.bilibili.com/video/BV1Q5411j7jG/)

### 8.5 工具与资源
- [Nginx 配置生成器](https://nginxconfig.io/)
- [Nginx 日志分析工具](https://goaccess.io/)
- [Let's Encrypt（免费 SSL 证书）](https://letsencrypt.org/)
- [Certbot（自动申请 SSL 证书）](https://certbot.eff.org/)