# study-web #
Web学习笔记
## 功能说明 ##
- GET 和 POST的区别: 详情请查看 `com.bage.web.getpost`
 - GET 请求查询字符串（名称/值对）是在URL 中发送的
 - GET 请求可被缓存
 - GET 请求保留在浏览器历史记录中
 - GET 请求可被收藏为书签
 - GET 请求不应在处理敏感数据时使用
 - GET 请求有长度限制
 - GET 请求只应当用于取回数据
 - POST POST 请求查询字符串（名称/值对）是在 POST 请求的 HTTP 消息主体中发送的：
 - POST POST 请求不会被缓存
 - POST POST 请求不会保留在浏览器历史记录中
 - POST POST 不能被收藏为书签
 - POST POST 请求对数据长度没有要求

                                GET	                                POST
        后退按钮/刷新	        无害	                            数据会被重新提交（浏览器应该告知用户数据会被重新提交）。
        书签	                可收藏为书签	                    不可收藏为书签
        缓存	                能被缓存                            不能缓存
        编码类型                application/x-www-form-urlencoded   application/x-www-form-urlencoded 或 multipart/form-data。为二进制数据使用多重编码。
        历史	                参数保留在浏览器历史中。               参数不会保存在浏览器历史中。
        对数据长度的限制        GET 的URL 的长度是受限制的           无限制。
        对数据类型的限制	    只允许 ASCII 字符。	            没有限制。也允许二进制数据。
        安全性                    较差                           密码或其他敏感信息时绝必须使用 POST，参数不会被保存在浏览器历史或 web 服务器日志
 		
- 自定义HttpServletRequestWrapper和HttpServletResponseWrapper: `com.bage.web.reqres`
 -  用于封装自定义的request和response，统一设置设置cookie等
 
- 重写ajax,增加自定义参数： `src\main\webapp\ajax`
- ajax请求监听：`src\main\webapp\ajax-hook`
- Ztree基本使用： `src\main\webapp\ztree`

## Cookie、Session ##
- cookie数据存放在客户的浏览器上，session数据放在服务器上。
- cookie不是很安全，别人可以分析存放在本地的cookie并进行cookie欺骗，考虑到安全应当使用session。
- session会在一定时间内保存在服务器上。当访问增多，会比较占用你服务器的性能，考虑到减轻服务器性能方面，应当使用cookie。
- 单个cookie保存的数据不能超过4K，很多浏览器都限制一个站点最多保存20个cookie。
- 可以考虑将登陆信息等重要信息存放为session，其他信息如果需要保留，可以放在cookie中。

## HTTP  ##
[https://www.cnblogs.com/zhangyfr/p/8662673.html](https://www.cnblogs.com/zhangyfr/p/8662673.html)

### HTTP1.0和HTTP1.1的一些区别 ###
- 缓存处理，在HTTP1.0中主要使用header里的If-Modified-Since,Expires来做为缓存判断的标准，HTTP1.1则引入了更多的缓存控制策略例如Entity tag，If-Unmodified-Since, If-Match, If-None-Match等更多可供选择的缓存头来控制缓存策略。

- 带宽优化及网络连接的使用，HTTP1.0中，存在一些浪费带宽的现象，例如客户端只是需要某个对象的一部分，而服务器却将整个对象送过来了，并且不支持断点续传功能，HTTP1.1则在请求头引入了range头域，它允许只请求资源的某个部分，即返回码是206（Partial Content），这样就方便了开发者自由的选择以便于充分利用带宽和连接。

- 错误通知的管理，在HTTP1.1中新增了24个错误状态响应码，如409（Conflict）表示请求的资源与资源的当前状态发生冲突；410（Gone）表示服务器上的某个资源被永久性的删除。

- Host头处理，在HTTP1.0中认为每台服务器都绑定一个唯一的IP地址，因此，请求消息中的URL并没有传递主机名（hostname）。但随着虚拟主机技术的发展，在一台物理服务器上可以存在多个虚拟主机（Multi-homed Web Servers），并且它们共享一个IP地址。HTTP1.1的请求消息和响应消息都应支持Host头域，且请求消息中如果没有Host头域会报告一个错误（400 Bad Request）。

- 长连接，HTTP 1.1支持长连接（PersistentConnection）和请求的流水线（Pipelining）处理，在一个TCP连接上可以传送多个HTTP请求和响应，减少了建立和关闭连接的消耗和延迟，在HTTP1.1中默认开启Connection： keep-alive，一定程度上弥补了HTTP1.0每次请求都要创建连接的缺点。


### HTTP2.0和HTTP1.X相比的新特性 ###

- 新的二进制格式（Binary Format），HTTP1.x的解析是基于文本。基于文本协议的格式解析存在天然缺陷，文本的表现形式有多样性，要做到健壮性考虑的场景必然很多，二进制则不同，只认0和1的组合。基于这种考虑HTTP2.0的协议解析决定采用二进制格式，实现方便且健壮。

- 多路复用（MultiPlexing），即连接共享，即每一个request都是是用作连接共享机制的。一个request对应一个id，这样一个连接上可以有多个request，每个连接的request可以随机的混杂在一起，接收方可以根据request的 id将request再归属到各自不同的服务端请求里面。

- header压缩，如上文中所言，对前面提到过HTTP1.x的header带有大量信息，而且每次都要重复发送，HTTP2.0使用encoder来减少需要传输的header大小，通讯双方各自cache一份header fields表，既避免了重复header的传输，又减小了需要传输的大小。

- 服务端推送（server push），同SPDY一样，HTTP2.0也具有server push功能。


## 代码规范 ##

- 工程项目(UTF-8)
- 目录（分模块）
- 包(分模块)
- 类（MVC、Exception、Base、Impl、Tests、Factory、Proxy）
- 方法（驼峰、动名词、insert、delete、update、query）
- 变量（英文、_、常量大写）
- 大括号必须存在、运算符左右增加空格、缩进4空格
- SQL、预编译、大小写
- 数据库字段尽可能充分利用
- 编写测试用例
- 事务、业务逻辑、重写hashcode、equals、toString
- 序列化增加属性不修改 serialVersionUID
- switch 包含break、default
- try-catch-finally区分异常、finally释放资源


## Copy ##
howto_js_copy_clipboard [https://www.w3schools.com/howto/howto_js_copy_clipboard.asp](https://www.w3schools.com/howto/howto_js_copy_clipboard.asp)

