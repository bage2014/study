# 通用APP设计文档

## 1. 基础功能模块

### 1.1 用户认证系统
#### 1.1.1 登录功能
- **手机号登录**
  - 输入手机号获取验证码
  - 验证码有效期5分钟
  - 每日最多发送5次验证码

- **密码登录**
  - 账号密码+图形验证码
  - 密码错误5次后锁定30分钟

#### 1.1.2 注册流程
1. 手机号验证
2. 设置密码(8-20位，需包含字母和数字)
3. 完善基本信息(昵称、头像)
4. 注册成功跳转首页

#### 1.1.3 找回密码
- 通过绑定的手机/邮箱验证身份
- 重置密码链接有效期24小时

### 1.2 个人中心
#### 1.2.1 个人信息管理
- 头像上传(支持裁剪)
- 昵称修改(30天内最多修改3次)
- 个人简介(最多200字)

#### 1.2.2 账号安全
- 修改密码(需验证原密码)
- 绑定/解绑第三方账号
- 登录设备管理

### 1.3 系统功能
#### 1.3.1 检查更新
- 自动检测版本更新
- 增量更新包下载
- 强制更新机制

#### 1.3.2 消息通知
- 系统消息
- 用户互动消息
- 推送通知设置

## 2. 详细设计

### 2.1 技术架构
- **前端**: Flutter跨平台框架
- **后端**: Spring Boot微服务
- **数据库**: MySQL主从架构
- **缓存**: Redis集群

### 2.2 API设计
```rest
POST /api/v1/login
Content-Type: application/json

{
  "phone": "13800138000",
  "code": "123456"
}
```



docker run -d -p 8000:8080 --name myappendpoint myappendpoint:20250725


docker run -d -p 8000:8080 --name myappendpoint myappendpoint:20250727


bage@192 myappendpoint % 
./mvnw spring-boot:run


API 文档生成【DeepSeek优先】


1、 基于markdown 格式，给 XXX 生成  API文档，
2、 使用命令行将结果自动写入到 xxx.md文件中， 
3、 至少包括请求入参、响应的 请求样例、响应格式、错误响应 ;

1、 基于markdown 格式，给 UserController 生成  API文档，
2、 使用命令行将结果自动写入到 user.md文件中， 
3、 至少包括请求入参、响应的 请求样例、响应格式、错误响应 ;


1、 基于markdown 格式，给 AppVersionController 生成  API文档，
2、 使用命令行将结果自动写入到 app-versions.md文件中， 
3、 至少包括请求入参、响应的 请求样例、响应格式、错误响应 ;

1、 基于markdown 格式，给 FamilyController 生成  API文档，
2、 使用命令行将结果自动写入到 family.md文件中， 
3、 至少包括请求入参、响应的 请求样例、响应格式、错误响应 ;


1、 基于markdown 格式，给 MapTrajectoryController 生成  API文档，
2、 使用命令行将结果自动写入到 map-trajectory.md文件中， 
3、 至少包括请求入参、响应的 请求样例、响应格式、错误响应 ;

1、 基于markdown 格式，给 IptvController 生成  API文档，
2、 使用命令行将结果自动写入到 iptv.md文件中， 
3、 至少包括请求入参、响应的 请求样例、响应格式、错误响应 ;
