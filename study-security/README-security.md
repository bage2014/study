# Web Security #

Web 应用安全,,,很重要！！！

## 安全类型 ##

### SQL 注入 ###
**基本定义**

指应用程序中事先定义好的查询语句的结尾上添加额外的SQL语句

**例子**

正常逻辑

	String sql = "delete from tb_xxx where id = " + param;

攻击逻辑

	String sql = "delete from tb_xxx where id = " + param;

**防范措施**

预编译

	String sql = "delete from tb_xxx where id = ?";

### 认证授权 ###

**基本定义**

受保护资源页面不能未认证授权下进行访问

**例子**

正常逻辑

	login.html -> profile.html

攻击逻辑

	profile.html

**防范措施**

登录过程 + 页面权限控制

### DDOS 拒绝服务攻击  ###
**基本定义**

最基本的DoS攻击就是利用合理的服务请求来占用过多的服务资源，从而使合法用户无法得到服务的响应。

**例子**

正常逻辑

	login.html -> xx-business.html

攻击逻辑

	login.html -> timeout.html

**防范措施**

监控、限流等

### XSS攻击 ###
**基本定义**

跨站脚本攻击是一种恶意web用户将代码植入到提供给其它用户使用的页面中

**例子**
正常逻辑

	content = "今天星期二，考试得0分";

攻击逻辑

	content = "今天星期二，考试得0分<script>alert('呵呵')</script>";

**防范措施**

decode


### 开放重定向 ###

**基本定义**

篡改请求重定向URL，而把用户重定向到外部的恶意URL

**例子**

正常逻辑

	login.html -> home.html
	
攻击逻辑

	login2.html -> forgetPwd.html -> login.html —> home.html

**防范措施**

禁止开放重定向

### CSRF ###

**基本定义**

跨站请求伪造是一种挟制用户在当前已登录的Web应用程序上执行非本意的操作的攻击方法。 
跟跨网站脚本（XSS）相比，XSS利用的是用户对指定网站的信任，CSRF 利用的是网站对用户网页浏览器的信任

**例子**

正常逻辑

	login.html -> money-business.html -> ok.html
	
攻击逻辑

	login.html -> money-business.html(ajax transfer2Bage()) -> ok.html

**防范措施**

sign 签名，请求无法进行伪造

### 文件上传漏洞 ###
**基本定义**

文件上传漏洞指攻击者利用程序缺陷绕过系统对文件的验证与处理策略将恶意程序上传到服务器并获得服务器端执行能力或其他操作

**例子**

正常逻辑

	login.html -> upload.html -> ok.html
	
攻击逻辑

	login.html -> upload.html(list-all.jsp) -> ok.html

	访问对应文件，即 list-all.jsp

**防范措施**

文件校验、禁止重命名、权限管理


## 道路千万条，安全第一条  ##

- cookie提高了用户体验，但不应该存放一些敏感数据，使用 secure/httpOnly cookies
- 不同的web站点的作用域应区分开，以避免出现不必要的麻烦
- HTTPS
- 登出之后销毁会话 ID
- 密码重置后销毁所有活跃的会话
- OAuth2 验证必须包含 state 参数
- 当解析用户注册/登陆的输入时，过滤 javascript://、 data:// 以及其他 CRLF 字符.
- 限制单个用户 Login、Verify OTP、 Resend OTP、generate OTP 等 API 的调用次数，使用 Captcha 等手段防止暴力破解
- 敏感信息泄露，比如.git 等
- 检查邮件或短信里的重置密码的 token，确保随机性（无法猜测）
- 给重置密码的 token 设置过期时间.
- 检查所有机器默认端口，关闭没有必要开放的端口
- 默认密码，特别是 MongoDB 和 Redis
- 不安全的反序列化漏洞

## 参考链接 ##
- security-guide-for-developers 
[https://github.com/FallibleInc/security-guide-for-developers](https://github.com/FallibleInc/security-guide-for-developers "security-guide-for-developers")
