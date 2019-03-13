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