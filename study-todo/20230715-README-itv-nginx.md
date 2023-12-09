# Study-Nginx  #
## 简介

HTTP服务器和反向代理服务器的web服务器

Nginx 是一款轻量级的 Web 服务器、反向代理服务器

由于它的内存占用少，启动极快，高并发能力强，在互联网项目中广泛应用。

## Key关键点

- 行业调研、优势、代替方案等
- 架构
- nginx.conf文件 和 常用配置 
- 负载均衡
- fair 插件
- location 规则
- 压缩
- 常见优化配置
- 请求过程
- 优点 和 缺点不足
- 自动剔除机制？自动添加？动态配置
- 限流【】
- 静态文件缓存
- 压缩
- 高可用
- 动态请求 
- 请求过程解析
- 设置请求头请求体
- nginx扩展功能,限流,lua脚本.失败过滤





## **概要** 

5000 QPS 

开源



## **架构** 



## 目录结构

```powershell
/usr/local/nginx
├── client_body_temp
├── conf # Nginx所有配置文件的目录
│ ├── fastcgi.conf # fastcgi相关参数的配置文件
│ ├── fastcgi.conf.default         # fastcgi.conf的原始备份文件
│ ├── fastcgi_params # fastcgi的参数文件
│ ├── fastcgi_params.default       
│ ├── koi-utf
│ ├── koi-win
│ ├── mime.types # 媒体类型
│ ├── mime.types.default
│ ├── nginx.conf # Nginx主配置文件
│ ├── nginx.conf.default
│ ├── scgi_params # scgi相关参数文件
│ ├── scgi_params.default  
│ ├── uwsgi_params # uwsgi相关参数文件
│ ├── uwsgi_params.default
│ └── win-utf
├── fastcgi_temp # fastcgi临时数据目录
├── html # Nginx默认站点目录
│ ├── 50x.html # 错误页面优雅替代显示文件，例如当出现502错误时会调用此页面
│ └── index.html # 默认的首页文件
├── logs # Nginx日志目录
│ ├── access.log # 访问日志文件
│ ├── error.log # 错误日志文件
│ └── nginx.pid # pid文件，Nginx进程启动后，会把所有进程的ID号写到此文件
├── proxy_temp # 临时目录
├── sbin # Nginx命令目录
│ └── nginx # Nginx的启动命令
├── scgi_temp # 临时目录
└── uwsgi_temp # 临时目录

```



## 配置

nginx.conf 

```shell
worker_processes  1；# worker进程的数量
events { # 事件区块开始
    worker_connections  1024；# 每个worker进程支持的最大连接数
} # 事件区块结束
http { # HTTP区块开始
    include       mime.types；# Nginx支持的媒体类型库文件
    default_type application/octet-stream；# 默认的媒体类型
    sendfile on；# 开启高效传输模式
    keepalive_timeout 65；# 连接超时
    server { # 第一个Server区块开始，表示一个独立的虚拟主机站点
        listen       80；# 提供服务的端口，默认80
        server_name localhost；# 提供服务的域名主机名
        location / { # 第一个location区块开始
            root   html；# 站点的根目录，相当于Nginx的安装目录
            index index.html index.htm；# 默认的首页文件，多个用空格分开
        } # 第一个location区块结果
        error_page 500502503504  /50x.html；# 出现对应的http状态码时，使用50x.html回应客户
        location = /50x.html { # location区块开始，访问50x.html
            root   html；# 指定对应的站点目录为html
        }
    }
    ......

```



## 优点

- 非阻塞、高并发连接：处理 2-3 万并发连接数，官方监测能支持 5 万并发
- 内存消耗小：开启 10 个 Nginx 才占 150M 内存。
- 跨平台、配置简单

- 成本低廉，且开源。
- 内置的健康检查功能：如果有一个服务器宕机，会做一个健康检查，再发送的请求就不会发送到宕机的服务器了。重新将请求提交到其他的节点上



## 使用场景

- http 服务器。Nginx 是一个 http 服务可以独立提供 http 服务。可以做网页静态服务器。

- 虚拟主机。可以实现在一台服务器虚拟出多个网站，例如个人网站使用的虚拟机。

- 反向代理，负载均衡。当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用 nginx 做反向代理。并且多台服务器可以平均分担负载，不会应为某台服务器负载高宕机而某台服务器闲置的情况。

- nginx 中也可以配置安全管理、比如可以使用 Nginx 搭建 API 接口网关, 对每个接口服务进行拦截。



## 请求过程

- 首先，Nginx 在启动时，会解析配置文件，得到需要监听的端口与 IP 地址，然后在 Nginx 的 Master 进程里面先初始化好这个监控的 Socket(创建 S ocket，设置 addr、reuse 等选项，绑定到指定的 ip 地址端口，再 listen 监听)。

- 然后，再 fork(一个现有进程可以调用 fork 函数创建一个新进程。由 fork 创建的新进程被称为子进程) 出多个子进程出来。

- 之后，子进程会竞争 accept 新的连接。此时，客户端就可以向 nginx 发起连接了。当客户端与 nginx 进行三次握手，与 nginx 建立好一个连接后。此时，某一个子进程会 accept 成功，得到这个建立好的连接的 Socket ，然后创建 nginx 对连接的封装，即 ngx_connection_t 结构体。

- 接着，设置读写事件处理函数，并添加读写事件来与客户端进行数据的交换。

- 最后，Nginx 或客户端来主动关掉连接，到此，一个连接就寿终正寝了。



## 为啥快



**Answer1** 

一个主要过程，多个工作过程，每个工作过程可以处理多个请求，每个进来request，处理worker进程会有一个。

但不是全程处理，处理到可能堵塞的地方，比如向上游(后端)服务器转发request，等待请求返回。

然后，worker继续处理其他请求，一旦上游服务器返回，事件将触发，worker将接管，request将继续下去。

由于web server的工作性质决定了每个request的大部分生命都在网络传输中，但实际上在server机器上花费的时间片并不多。解决高并发问题的秘密是几个过程。也就是说，@skoo所说的webserver正好属于网络io密集型应用，而不是计算密集型。



**Answer2**

​			如果一个 server 采用一个进程 (或者线程) 负责一个 request 的方式，那么进程数就是并发数。那么显而易见的，就是会有很多进程在等待中。等什么？最多的应该是等待网络传输。

​			而 Nginx 的异步非阻塞工作方式正是利用了这点等待的时间。在需要等待的时候，这些进程就空闲出来待命了。因此表现为少数几个进程就解决了大量的并发问题。

​			Nginx 是如何利用的呢，简单来说：同样的 4 个进程，如果采用一个进程负责一个 request 的方式，那么，同时进来 4 个 request 之后，每个进程就负责其中一个，直至会话关闭。期间，如果有第 5 个 request 进来了。就无法及时反应了，因为 4 个进程都没干完活呢，因此，一般有个调度进程，每当新进来了一个 request ，就新开个进程来处理。

​			Nginx 不这样，每进来一个 request ，会有一个 worker 进程去处理。但不是全程的处理，处理到什么程度呢？处理到可能发生阻塞的地方，比如向上游（后端）服务器转发 request ，并等待请求返回。那么，这个处理的 worker 不会这么傻等着，他会在发送完请求后，注册一个事件：“如果 upstream 返回了，告诉我一声，我再接着干”。于是他就休息去了。此时，如果再有 request 进来，他就可以很快再按这种方式处理。而一旦上游服务器返回了，就会触发这个事件，worker 才会来接手，这个 request 才会接着往下走。

​			这就是为什么说，Nginx 基于事件模型。

​			由于 web server 的工作性质决定了每个 request 的大部份生命都是在网络传输中，实际上花费在 server 机器上的时间片不多。这是几个进程就解决高并发的秘密所在。即：

​			webserver 刚好属于网络 IO 密集型应用，不算是计算密集型。

​			异步，非阻塞，使用 epoll ，和大量细节处的优化。也正是 Nginx 之所以然的技术基石



## 负载算法

- 轮询 (默认)
- 权重 weight
- IP Hash 
- Fair 【第三方，必须安装upstream_fair】
- URL Hash 【需要安装hash插件】





## 参考链接

官方网址 http://nginx.org/en/

中文 https://blog.redis.com.cn/doc/

基本配置解析 https://juejin.cn/post/7306041273822527514



【2023-07-15】https://zhuanlan.zhihu.com/p/398768594

【2023-07-15】https://m.elecfans.com/article/1974990.html

【2023-07-15】https://www.tulingxueyuan.cn/tlzx/jsp/1600.html

【2023-06-12】 https://baijiahao.baidu.com/s?id=1575766392326093&wfr=spider&for=pc

【2023-07-15】https://www.jianshu.com/p/6777b36cf566

## Bilibi

【2023-06-11】https://www.bilibili.com/video/BV1em4y1A7ey?p=7&vd_source=72424c3da68577f00ea40a9e4f9001a1