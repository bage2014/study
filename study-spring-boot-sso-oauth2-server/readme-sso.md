# SSO #



## 基本概念 ##

### 什么是SSO ###
登陆

认证

授权

Single Sign On，单点登录，一次登录，处处【子系统】登录



### OAuth2.0相关概念 ###

#### 四个职责划分 ####
- resource owner

      An entity capable of granting access to a protected resource.
      When the resource owner is a person, it is referred to as an
      end-user.

- resource server

      The server hosting the protected resources, capable of accepting
      and responding to protected resource requests using access tokens.

- client

      An application making protected resource requests on behalf of the
      resource owner and with its authorization.  The term "client" does
      not imply any particular implementation characteristics (e.g.,
      whether the application executes on a server, a desktop, or other
      devices).

- authorization server

      The server issuing access tokens to the client after successfully
      authenticating the resource owner and obtaining authorization.

#### 四种模式 ####
- 授权码模式
老大，666，不会错
- 密码模式
传密码，不提倡
- 简化模式
不经过client的server端，用户透明，不建议
- 客户端模式
client + secret，不细化到用户，不会用

## 背景 ##
- 单体应用逐渐不能满足要求
- 企业内部独立服务众多与统一账号管理存在矛盾
- 集群以及多系统登录复杂性

## 实现方案 ##
### 自实现 ###
- 独立自主完成，全部自己实现一个单点登录
### CAS ###
Central Authentication Service，耶鲁大学的统一鉴权服务

#### 优点 ####
- 开源，可定制化
- Spring Webflow/Spring Boot Java server component
- 多种认证支持 (LDAP, Database, X.509, SPNEGO, JAAS, JWT, RADIUS, MongoDb, etc)
- 多种协议支持 (CAS, SAML, WS-Federation, OAuth2, OpenID, OpenID Connect, REST)
- 实时监控和跟踪应用程序行为，统计信息和日志。
- 跨平台的客户端支持 (Java, .Net, PHP, Perl, Apache, etc)

#### 不足 ####
- 基于cookie
- 客户端管理问题？

#### 核心 ####
- CAS Server
	TicketGrantingTicketCheckAction 校验ticket
	AcceptUsersAuthenticationHandler 校验用户登录
- CAS Clients
	AuthenticationFilter 核心入口，校验登录 + 重定向等
	AbstractTicketValidationFilter(AuthenticationFilter调用) 校验ticket
	CasHttpServletRequestWrapper 

#### 认证流程 ####
![Web flow diagram](https://apereo.github.io/cas/4.2.x/images/cas_flow_diagram.png)

### Spring OAuth2.0 ###
Spring 的，基于 OAuth2.0 协议的， SSO实现方案

#### 优点 ####
- Spring 全家桶之一，与Spring配合默契，喜欢的样子它都有
- 客户端统一管理 零成本

#### 不足 ####
- OAuth2.0 的Java 应用客户端

#### 核心 ####
- Spring Security
	实现 *AuthenticationProvider* 用户登录验证
	实现 *UserDetailsService* 用户loadUserByUsername
	集成 *WebSecurityConfigurerAdapter* 装配 自定义用户登录验证 + URL Pattern 
- Spring 集成 OAuth2.0
	*AuthorizationEndpoint* 认证登录
	*TokenEndpoint* 验证Token
	*OAuth2AuthenticationProcessingFilter* is used to load the Authentication for the request given an authenticated access token.
	
	*ClientDetailsServiceConfigurer* 客户端信息配置
	*AuthorizationServerSecurityConfigurer*: defines the security constraints on the token endpoint.
	*AuthorizationServerEndpointsConfigurer*: defines the authorization and token endpoints and the token services.

#### 认证流程 ####
授权码模式

     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |                               |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+
    
                     Figure 1: Abstract Protocol Flow

   用户访问受保护资源，客户端验证用户登录状态，未登录，则进入步骤A

   (A)  客户端请求用户给予授权，页面跳转到登录页面，用户输入用户名、密码进行登录，登录成功后，则进入步骤B

   (B)  授权后，返回一个code + state(可选)，进入步骤C

   (C)  客户端拿到授权码，凭借授权码code请求授权中心验证，进入步骤D

   (D)  若验证成功，则返回AccessToken.则进入步骤E

   (E)  客户端拿到AccessToken，带上，请求受保护资源.

   (F)  资源服务器验证token合法，则返回受保护的资源.



### Spring OAuth2.0 SSO ###

服务端-拦截规则

```java
    @Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/oauth/**","/login/**", "/logout").permitAll()
            .anyRequest().authenticated()   // 其他地址的访问均需验证权限
            .and()
            .formLogin()
            .loginPage("/login")
            .and()
            .logout().logoutSuccessUrl("/");
}
```
服务端-资源路径

	@Override
	public void configure(HttpSecurity http) throws Exception {
	    http.requestMatchers().antMatchers("/user/**")
	            .and()
	            .authorizeRequests()
	            .anyRequest().authenticated();
	}
服务端-客户端列表

```java
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource);
    /*
        clients.inMemory()
                .withClient("sampleClientId")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("authorization_code")
                .scopes("user_info")
                .autoApprove(true)
                .redirectUris("http://localhost:8001/client1/login")
        // .accessTokenValiditySeconds(3600)
        ; // 1 hour
*/
}
```

SQL 

```sql
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sampleClientId', '$2a$10$i7bPh8Npg8lsUTxOAwp7suAwMxTw8tNyRkvDJT8/uZGcSzdFocHS6', 'user_info',
	'password,authorization_code,refresh_token,client_credentials', 'http://localhost:8001/client1/login,http://localhost:8002/client2/login', null, 36000, 36000, null, true);
```
客户端- application.yml

```yml
server:
  port: 8001
  servlet:
   context-path: /client1

security:
  oauth2:
    client:
      client-id: sampleClientId
      client-secret: secret
      access-token-uri: http://localhost:8080/oauth/token
      user-authorization-uri: http://localhost:8080/oauth/authorize
    resource:
      user-info-uri: http://localhost:8080/user/me
```
  客户端-拦截规则

```java
@EnableOAuth2Sso
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ClientSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().logoutSuccessUrl("/")
                .and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        ;
    }
}
```
完整代码	


- Auth Server  [https://github.com/bage2014/study/tree/master/study-spring-boot-sso-oauth2-server](https://github.com/bage2014/study/tree/master/study-spring-boot-sso-oauth2-server)
- Client [https://github.com/bage2014/study/tree/master/study-spring-boot-sso-oauth2-client1](https://github.com/bage2014/study/tree/master/study-spring-boot-sso-oauth2-client1)

Ctrip SSO [http://git.ctripcorp.com/global-rail-gds/gds-admin-application](http://git.ctripcorp.com/global-rail-gds/gds-admin-application)

### JWT ### 
核心实现	

1. 登录过程

	public String login(HttpServletRequest request){
		
		String account = request.getParameter("account");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("account", account);
		map.put("password", request.getParameter("password"));
		map = userService.query(map);
		
		if(map != null ) { //登录成功
			
			Key key = MacProvider.generateKey();
			Map<String,Object> cliams = new HashMap<String,Object>();
			cliams.put("jti", String.valueOf(System.currentTimeMillis()));
			String jws = Jwts.builder()
					.setClaims(cliams )
					.setIssuer("com.bage") // iss: 该JWT的签发者，是否使用是可选的
					.setSubject(account) // sub: 该JWT所面向的用户，是否使用是可选的；
					//.setAudience(aud) // aud: 接收该JWT的一方，是否使用是可选的
					.setExpiration(DateUtils.getJwtsExpirationDate()) // exp(expires): 什么时候过期，这里是一个Unix时间戳，是否使用是可选的
					.setIssuedAt(DateUtils.now()) // iat(issued at): 在什么时候签发的(UNIX时间)，是否使用是可选的
					//.setNotBefore(DateUtils.getNotBeforeDate()) // nbf (Not Before)：如果当前时间在nbf里的时间之前，则Token不被接受；一般都会留一些余地，比如几分钟；是否使用是可选的
					.signWith(SignatureAlgorithm.HS512, key)
					.compact();
			
			System.out.println("jws:" + jws);
			
			RedisUtils.put(Constants.redis_key_jwt + "_" + account, jws);
			RedisUtils.put(Constants.redis_key_jwtkey + "_" + account, key);
			
			map = new HashMap<String,Object>();
			map.put("jws", jws);
			map.put("now", DateUtils.now());
			map.put("expirationDate", DateUtils.getJwtsExpirationDate());
		
			return JsonUtils.toJson(map);
			
		} else { // 登录失败
			return "";
		}
	}
2. 校验逻辑

```java
if(!isExcludeUri(request)) {
		String claimsJws = request.getHeader("Authorization");
		System.out.println("参数compactJws：" + claimsJws);
		// 签名验证
		try {
			Claims claims = JwtsBuilder.decodeTokenClaims(claimsJws);
			String subject = claims.getSubject();
			Key key = (Key) RedisUtils.get(Constants.redis_key_jwt + "_" + subject);
			Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
			System.out.println("jti:" + jws.getBody().get("jti"));
			String currentCompactJws = RedisUtils.getString(Constants.redis_key_jwt + "_" + subject);
			if(currentCompactJws == null || !currentCompactJws.equals(claimsJws)) {
				System.out.println("签名不合法:\ncompactJws：" + claimsJws);
				System.out.println("currentCompactJws：" + currentCompactJws);
				checkJwtFail(request, response);
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解析签名报错");
			checkJwtFail(request, response);
			return ;
		}
	}
	
	chain.doFilter(request, response);
```

3. 区分请求类型

```java
private void checkJwtFail(HttpServletRequest request,HttpServletResponse response) {
	try {
		String requestType = request.getHeader("X-Requested-With");
		if("XMLHttpRequest".equals(requestType)){ // TODO 增加跨域请求校验
		    System.out.println("AJAX请求..");
		    response.getWriter().print("Authorization Failed");
		}else{
		    System.out.println("非AJAX请求..");
		    response.sendRedirect(request.getServletContext().getContextPath() + "/");
		    //此时requestType为null
		}
		//request.getSession().removeAttribute(Constants.session_attribute_currentuser);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
```

完整代码

单体应用 [https://github.com/bage2014/study/tree/master/study-jwt](https://github.com/bage2014/study/tree/master/study-jwt)

## 总结 ##

### SSO的本质 ###
- 共享登录信息(共用Session || JWT)
- 认证中心 + 客户端模式

### oauth2.0 与 SSO 的关系 ###
- Oauth2.0 是 一种协议，SSO是 一个功能
- SSO可以基于 OAuth 2.0协议实现
- Oauth2.0可以用于其他功能，比如资源授权
### SSO实现方案的抉择 ###
- 合适的就是最好的
- 企业之间 || Restful，推荐 Spring OAuth2.0
### Spring OAuth2.0 JWT ###
- Session 弱化场景
- 防重放
- JWT失效时间控制

## 参考链接 ##
- 单点登录 CAS 官网 [https://www.apereo.org/projects/cas](https://www.apereo.org/projects/cas)
- cas 文档 [https://apereo.github.io/cas/4.2.x/index.html](https://apereo.github.io/cas/4.2.x/index.html)
- cas github地址 [https://github.com/apereo/cas](https://github.com/apereo/cas)
- CAS 登录过程源码解析 [https://segmentfault.com/a/1190000014001205?utm_source=channel-hottest](https://segmentfault.com/a/1190000014001205?utm_source=channel-hottest)
- Oauth2.0协议 [http://www.rfcreader.com/#rfc6749](http://www.rfcreader.com/#rfc6749)
- 阮一峰 理解OAuth 2.0 [http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html](http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html)
- Spring OAuth 2.0 官网 [https://projects.spring.io/spring-security-oauth/docs/oauth2.html](https://projects.spring.io/spring-security-oauth/docs/oauth2.html)
- 携程单点登录接入Conf [http://conf.ctripcorp.com/pages/viewpage.action?pageId=157466083](http://conf.ctripcorp.com/pages/viewpage.action?pageId=157466083)、[http://conf.ctripcorp.com/pages/viewpage.action?pageId=135658955](http://conf.ctripcorp.com/pages/viewpage.action?pageId=135658955)
