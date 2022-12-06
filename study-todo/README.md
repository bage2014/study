# study-TODO #
## 项目代办

| 模块       | 功能概述   | 完成状态 |       时间区间        | 边界条件                                                     |
| ---------- | ---------- | -------- | :-------------------: | ------------------------------------------------------------ |
| 用户登陆   | 账号登陆   | done     | 2022/10/23-2022/10/24 | - 根据邮箱、输入正确密码，即可成功登陆<br />- 校验输入合法性，账号、密码不能为空<br />- 输入密码错误N【暂定2】次后，需要输入验证码 |
|            | 自动登陆   | todo     | 2022/10/12-2022/10/？ | - 只要存在登陆成功后，token存储到本地<br />- N【暂定7】天内免登陆，自动续约token<br />- 自动记录，最近的一次登陆账号信息<br /> - 不记录密码信息 |
|            | 退出登陆   | todo     | 2022/10/11-2022/10/12 | - 用户退出，退出当前登陆状态，需要重新进行登陆<br />- 移除所有登陆状态 |
|            |            |          |                       |                                                              |
| 用户注册   | 邮箱注册   | Check    |                       | - 用户注册？                                                 |
|            |            |          |                       |                                                              |
| 用户详情   | 登陆状态   |          |                       | - 用户登陆成功后，需要显示用户基本信息<br />- 用户可以查看、编辑个人详细信息<br />- 用户可以更新头像或者自定义头像 |
|            |            |          |                       |                                                              |
| 密码重置   | 找回密码   | todo     |                       | - 通过邮箱找回密码？                                         |
|            |            |          |                       |                                                              |
| - 应用设置 | 应用更新   |          |                       | - APP 检查更新<br />- APP 内可以更新到最新的版本<br />- 兜底打开浏览器，访问下载链接 |
|            |            |          |                       |                                                              |
|            |            |          |                       |                                                              |
| - 基础建设 | 无网络提示 | todo     |                       | - 无网络连接，给予无网络提示<br />- 服务器无响应，基于服务器错误提示<br />- 其他错误，兜底服务器错误提示 |
|            | 环境切换   | Done     | 2022/10/18-2022/10/21 | - 程序默认初始化好三个环境配置：dev\test\prod<br />- app 可以进行环境切换 |
|            | 环境切换   | ING      | 2022/12/06-2022/12/06 | - 开关切换是否开启Mock网络请求                               |
|            | 公告页面   | todo     |                       | - 公告增删改查维护<br />- 公告查看<br />- 公告关闭           |
|            |            |          |                       |                                                              |
| 学习时间   | 学时列表   | todo     |                       | - 查看自己的所有页面                                         |
|            | 新增学时   | todo     |                       | - 新增一个学习规律，比如每节课30分钟，休息10分钟ß            |
|            |            |          |                       |                                                              |
| 个人名片   | 名片新增   | todo     |                       | - 特定模板进行添加<br />- 个性化自定义添加                   |
|            | 名片查看   | todo     |                       | - 列表查看<br />- 自定义查看顺序                             |
|            | 名片分享   | todo     |                       | - 选择特定卡片分享给他人<br />- 常用模板分享给他人<br />- 分享到社交媒体 |
|            |            |          |                       |                                                              |
| 用户留言   | 用户留言   | todo     |                       | - 用户给APP 官方留言                                         |
|            | 系统回复   | todo     |                       | - 系统给用户留言进行回复                                     |
|            | 点赞踩踩   | todo     |                       | - 点赞/踩 他人的留言                                         |
|            |            |          |                       |                                                              |
| 闹钟设置   | 间隔时长   | todo     |                       | - 每固定间隔【比如40分钟】响铃一次，相当于番茄学习           |
|            | 休息时间   | todo     |                       | - 固定学习时间后，进入休息时间                               |
|            |            |          |                       |                                                              |
|            |            |          |                       |                                                              |
|            |            |          |                       |                                                              |
|            |            |          |                       |                                                              |







## 技术代办

## 代办事项

免费开放API

https://github.com/modood/Administrative-divisions-of-China

http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/ 官方数据来源

https://github.com/WenryXu/ChinaUniversity 高校

https://github.com/public-apis/public-apis



学校

https://github.com/516134941/cn-universitysrv

### APP 代办

https://github.com/florinpop17/app-ideas APP 想法

公告页面【栏】

查询列表【返回最近有效的最新公告列表，APP 显示轮播图】

APP 测关闭公告，，



一个月内免费登陆？？？

用户注册API

API 方法 VS Logic 直接调用

Auth Server 设置自定义User ID字段

统一校验登陆状态；

名片编辑页面

2022年结束！！！完成一个APP 上线

基本功能：

2 个左右主打功能

登陆注册

留言、更新功能



后端，，，

文件上传-待自测

Run --app 

app 名字更新确定

UI route 设置部分页面拦截登陆



- 删除demo页
- 更新验证码输入页面： https://github.com/Vignesh0404/Flutter-UI-Kit/blob/main/Auth-otp-CustomKeypad/lib/verifyOTP.dart
- 微信登陆 https://www.authingyun.com/solutions/wechat 
- 登陆注册APP更新页面





### 技术代办

Kafaka

PacificA : kafka采用的一致性协议

Paxos 一致性协议

压测

线上问题定位

Dump 文件分析

Spring Cloud Alibaba 相关环境搭建

Docker 版本

搭建Spring Cloud 环境

注册中心、啥的 【docker版本】

Spring Test 



核心技能

高级JVM 分析

线上问题排查

常用工具高级用法



注册中心：

https://nacos.io/zh-cn/index.html

http://c.biancheng.net/springcloud/nacos.html







## Flutter

Mac 搭建Flutter 环境

下载Flutter 

下载Android Studio



配置fluter 环境变量



安装 Dart 插件、Flutter 插件



ZipException 处理

Flutter run ZipException

https://www.jianshu.com/p/8e1007e65c66

```

  
```

### 





## Idea



https://www.baidu.com/s?wd=%E6%9C%89%E4%BB%80%E4%B9%88%E6%96%B0%E9%A2%96app%E6%83%B3%E6%B3%95&pn=20&oq=%E6%9C%89%E4%BB%80%E4%B9%88%E6%96%B0%E9%A2%96app%E6%83%B3%E6%B3%95&ie=utf-8&fenlei=256&rsv_idx=1&rsv_pq=c6ff598c000f331e&rsv_t=fea2kaljO0xoo8gvRpHo%2F67HDIO4yMP4OXIncyW%2BgDAgqJy0rgXbySsUv2M





1、、、、、、、、

最近网课又出现在我的生活里了。

  有个想法，我尽量用简单的语言描述一下。

  背景：我选择用ipad+iPhone pencil听网课，有做笔记的习惯

**产生的问题：**

  ①当我使用ipad听网课时我就无法做笔记，当我用ipad做笔记时就无法看网课。（同一设备）

  ②分屏功能也不错，但是问题在于分屏的话我的网课画面就会变小，和用手机看有什么区别呢？甚至还不如手机屏幕大。

  ③当然我也可以用电脑看网课，用pad或本子做笔记，但我更想拥有一个这样的软件，后期我再整理纸质笔记。

**关于应用的设想：**

  这个应用可以**漂浮**在各应用之上，有点悬浮窗的感觉，而这个笔记的纸它本身是一个**透明的**（或者是可以设置为透明的），可以让我在屏幕的各个地方、各个软件上随意做笔记，同时设置截图、录音、加减纸张甚至录制背景视频的**快捷键**。当我打开软件查看我的笔记本时，也可以查看我当时的笔记。

作者：风骨可无羡

链接：https://www.zhihu.com/question/299878221/answer/1731615384

来源：知乎

著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。



2、、、、、、、、

付费WiFi，到其他地方没流量了，付费给别人的WiFi让别人告诉你密码就可以了，怎么样。



3、、、、、、、、、

应用监控，，正在打开那个应用？？？？