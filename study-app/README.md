# study-app #
应用实践

todo vue code generator 



## 目录结构 ##

### api 项目结构 ###



    com.bage.demo.api
    
     	-constant // 常量
    	-model // 模型
        DemoService // 服务接口



### core 项目结构 ###

```
com.bage.demo.core

        -adapter // 适配、代理
        
        -config // 配置
        
            - ds // 数据源
            - prop // 属性
            - bean // bean注册

        -constant // 常量
        
        -mapping // 模型转化

        -gateway // 网关，程序入口
        
            - controller // 控制器 
            - mapping // 映射
            - service // 服务实现
            - job // 定时任务
            - mq // 消息中间件
            
        -model // 模型

        -repo // 数据库仓储实现
        
            - mybatis
                - entity
                - mapper
                - mapping
            

        -logic // 最小业务单元逻辑
                
        -utils // 工具

```



举例

### provider 项目结构 ###





code generate order



--------- api ---------

1. api-constant
2. api-model
3. api-service

--------- core ---------

1. core-model
2. core-constant
3. core-repo
4. core-repo-impl-mybatis-entity
5. core-repo-impl-mybatis-mapper
6. core-repo-impl-mybatis-mapping
7. core-repo-impl-mybatis-xmlMapper
8. core-repo-impl-mybatis
9. core-logic
10. core-gateway-mapping
11. core-gateway-controller
12. core-gateway-service-impl



## 参考链接 ##

UI 资源 [https://github.com/qyxxjd/UI-Design-Resources](https://github.com/qyxxjd/UI-Design-Resources)、[https://github.com/material-components/material-components-android](https://github.com/material-components/material-components-android)

[https://www.creative-tim.com/templates/free](https://www.creative-tim.com/templates/free)

UI APP Kit [https://www.uistore.design/categories/apps/](https://www.uistore.design/categories/apps/)
[https://freebiesbug.com/psd-freebies/ui-kits/](https://freebiesbug.com/psd-freebies/ui-kits/)
[https://www.invisionapp.com/inside-design/design-resources/](https://www.invisionapp.com/inside-design/design-resources/)
[https://medium.muz.li/50-user-profile-page-design-inspiration-5c45aeeda400](https://medium.muz.li/50-user-profile-page-design-inspiration-5c45aeeda400)
[https://www.appcues.com/blog/30-awesome-free-ui-vector-kits-for-your-mockups-and-wireframes](https://www.appcues.com/blog/30-awesome-free-ui-vector-kits-for-your-mockups-and-wireframes)
[https://dribbble.com/tags/free_ui_kit](https://dribbble.com/tags/free_ui_kit)

图表
https://github.com/PhilJay/MPAndroidChart
https://github.com/AnyChart/AnyChart-Android
https://github.com/huangyanbin/SmartChart

vue 前端框架 https://vuetifyjs.com/zh-Hans/components/navigation-drawers/

app 更新 https://github.com/jenly1314/AppUpdater

APP 更新后台实现 done 

APP 版本增删改查 done 

APP 版本的APP端接入 todo

html + vue


## App ##
基于 [https://github.com/project-travel-mate/Travel-Mate](https://github.com/project-travel-mate/Travel-Mate)
改造升级打怪

iosched
[https://github.com/google/iosched/releases](https://github.com/google/iosched/releases)

plaid
[https://github.com/android/plaid/releases/tag/1.1.0](https://github.com/android/plaid/releases/tag/1.1.0)

集成库
[https://github.com/AriesHoo/FastLib](https://github.com/AriesHoo/FastLib)

material-components-android-examples
[https://github.com/material-components/material-components-android-examples](https://github.com/material-components/material-components-android-examples)

icons
[https://github.com/google/material-design-icons](https://github.com/google/material-design-icons)

APP Idea [https://www.zhihu.com/question/26876628?sort=created](https://www.zhihu.com/question/26876628?sort=created)

icon [http://fontello.com/](http://fontello.com/)

## TODO ##

服务实例单元支持 微服务部署或单机部署



gateway -> auth-server 问题
软件环境准备
APP应用升级，全部切换成新架构

ELK日志集中到ELK 
与 pagehelper-spring-boot-starter 1.2.10 版本冲突，升级到 1.2.12

demo 集成问题处理

Android HTTPS问题

github.io 做域名，放前台页面



APP 

登录页面，画页面完成，功能待完成

主页面，画页面，功能待完成

设置页面，画页面，功能待完成

参数传递给下游问题 [https://blog.csdn.net/Crystalqy/article/details/79083857](https://blog.csdn.net/Crystalqy/article/details/79083857) DONE

admin配置每一个表的配置，包名，类请求路径，testcase等

### 后台 ###
功能职能
- 登录注册
 登录，验证码或邮箱验证码
 注册，邮箱验证码
- 队列，APP
- 监控，APP
- 调用链，APP
- 日志，APP
- 队列，APP
- 定时任务，APP
- 配置中心，APP
- 注册中心
- 网关
- 报警
- 消息中心，短信、邮件
- 文件系统，附件，图片等
- 算法
- 安全
- 支付
- 商品
- 分布式锁、队列、事务
- 数据库访问
- 分布式ID
- 项目脚手架,模块划分，api层抽取
- 验证码 https://github.com/botaruibo/xvcode

### APP ###
- 熟悉代码
- 改造登录
- 改造logo
- 改造页面
- APP更新


- 域名

- idea 
		
	
	| 业务项目  | 概要说明                     | reference                                                    |
	| --------- | ---------------------------- | ------------------------------------------------------------ |
	| 开源      | 受欢迎，趋势，top，开发者    |                                                              |
	| 冷        | 冷知识的科普                 |                                                              |
	| 兴趣-音乐 | 音乐人，音乐菜单，音乐分享   |                                                              |
	| 账号      | 已注册账号、账号密码         |                                                              |
	| 快递      | 个人快递                     | [快递开放API](https://www.baidu.com/s?ie=UTF-8&wd=%E5%BF%AB%E9%80%92%E5%BC%80%E6%94%BEAPI) |
	| 天气      | 当天天气，近一周天气情况     |                                                              |
	| 插件      | 比如Chrome、IDE插件          | [天气APP](https://github.com/bage2014/study/blob/master/study-app/README-weather.md) |
	| 家        | 家里的成员情况（地理，状态） |                                                              |
	


## 项目模块 ##

| 项目模块 | 项目说明 |  |
| ------ | ------ | ------ |
| app-auth  |  认证中心 | 管理所有用户，用户登录退出等；用户头像上传到单独的文件服务 |
| app-getway  |  网关 | 请求入口，鉴权，全程使用jwt，动态路由 |
| app-file-storage | 文件系统 | 管理文件，文件暂时上传到项目根目录uploadDir下；抽取API模块，增加mapping和DTO |



### 文件管理 ###

免费API接口 参考链接 https://mp.weixin.qq.com/s/ihUqgErhrKAtew9bQHiQSg

**各类无次数限制的免费API接口整理，主要是聚合数据上和API Store上的一些，还有一些其他的。**

**聚合数据提供30大类,160种以上基础数据API服务,国内最大的基础数据API服务，下面就罗列一些免费的各类API接口。**

手机号码归属地API接口：

https://www.juhe.cn/docs/api/id/11

历史上的今天API接口：

https://www.juhe.cn/docs/api/id/63

股票数据API接口：

https://www.juhe.cn/docs/api/id/21

全国WIFI接口：

https://www.juhe.cn/docs/api/id/18

星座运势接口：

https://www.juhe.cn/docs/api/id/58

黄金数据接口：

https://www.juhe.cn/docs/api/id/29

语音识别接口：

https://www.juhe.cn/docs/api/id/134

周公解梦接口：

https://www.juhe.cn/docs/api/id/64

天气预报API接口：

https://www.juhe.cn/docs/api/id/73

身份证查询API接口：

https://www.juhe.cn/docs/api/id/38

笑话大全API接口：

https://www.juhe.cn/docs/api/id/95

邮编查询接口：

https://www.juhe.cn/docs/api/id/66

老黄历接口：

https://www.juhe.cn/docs/api/id/65

网站安全检测接口：

https://www.juhe.cn/docs/api/id/19

手机固话来电显示接口：

https://www.juhe.cn/docs/api/id/72

基金财务数据接口：

https://www.juhe.cn/docs/api/id/28

成语词典接口：

https://www.juhe.cn/docs/api/id/157

新闻头条接口：

https://www.juhe.cn/docs/api/id/235

IP地址接口：

https://www.juhe.cn/docs/api/id/1

问答机器人接口：

https://www.juhe.cn/docs/api/id/112

汇率API接口：

https://www.juhe.cn/docs/api/id/80

电影票房接口：

https://www.juhe.cn/docs/api/id/44

万年历API接口：

https://www.juhe.cn/docs/api/id/177

NBA赛事接口：

https://www.juhe.cn/docs/api/id/92

IP地址查询

http://apistore.baidu.com/apiworks/servicedetail/114.html

频道新闻API_易源

http://apistore.baidu.com/apiworks/servicedetail/688.html

微信热门精选 ：

http://apistore.baidu.com/apiworks/servicedetail/632.html

天气查询

http://apistore.baidu.com/apiworks/servicedetail/112.html

中国和世界天气预报

http://apistore.baidu.com/apiworks/servicedetail/478.html

股票查询

http://apistore.baidu.com/apiworks/servicedetail/115.html

身份证查询：

http://apistore.baidu.com/apiworks/servicedetail/113.html

美女图片

http://apistore.baidu.com/apiworks/servicedetail/720.html

音乐搜索

http://apistore.baidu.com/apiworks/servicedetail/1020.html

图灵机器人

http://apistore.baidu.com/apiworks/servicedetail/736.html

汇率转换

http://apistore.baidu.com/apiworks/servicedetail/119.html

节假日

http://apistore.baidu.com/apiworks/servicedetail/1116.html

pullword在线分词服务

http://apistore.baidu.com/apiworks/servicedetail/143.html

去哪儿网火车票

http://apistore.baidu.com/apiworks/servicedetail/697.html

笑话大全

http://apistore.baidu.com/apiworks/servicedetail/864.html

银行卡查询服务

http://apistore.baidu.com/apiworks/servicedetail/735.html

语音合成

http://apistore.baidu.com/apiworks/servicedetail/867.html

宅言API-动漫台词接口

http://apistore.baidu.com/apiworks/servicedetail/446.html

去哪儿景点门票查询

http://apistore.baidu.com/apiworks/servicedetail/140.html

手机号码归属地

http://apistore.baidu.com/apiworks/servicedetail/794.html

体育新闻

http://apistore.baidu.com/apiworks/servicedetail/711.html

手机归属地查询

http://apistore.baidu.com/apiworks/servicedetail/709.html

科技新闻

http://apistore.baidu.com/apiworks/servicedetail/1061.html

空气质量指数

http://apistore.baidu.com/apiworks/servicedetail/116.html

天狗健康菜谱

http://apistore.baidu.com/apiworks/servicedetail/987.html

热门游记列表

http://apistore.baidu.com/apiworks/servicedetail/520.html

天狗药品查询

http://apistore.baidu.com/apiworks/servicedetail/916.html

汉字转拼音

http://apistore.baidu.com/apiworks/servicedetail/1124.html

国际新闻

http://apistore.baidu.com/apiworks/servicedetail/823.html

彩票

http://apistore.baidu.com/apiworks/servicedetail/164.html

微信精选

http://apistore.baidu.com/apiworks/servicedetail/863.html

天狗健康资讯

http://apistore.baidu.com/apiworks/servicedetail/888.html

兴趣点检索

http://apistore.baidu.com/apiworks/servicedetail/182.html

用药参考

http://apistore.baidu.com/apiworks/servicedetail/754.html

天狗健康知识

http://apistore.baidu.com/apiworks/servicedetail/899.html

奇闻趣事

http://apistore.baidu.com/apiworks/servicedetail/633.html

花边新闻

http://apistore.baidu.com/apiworks/servicedetail/768.html

天狗医院大全

http://apistore.baidu.com/apiworks/servicedetail/988.html

生活健康

http://apistore.baidu.com/apiworks/servicedetail/989.html

一些其他的API接口：

豆瓣开放

https://developers.douban.com/wiki/

淘宝开放平台

http://open.taobao.com/

图灵语音

http://www.tuling123.com/help/h_cent_andriodsdk.jhtml

讯飞语音http://www.xfyun.cn/robots/solution

马化腾的微信开放平台（对应的还有腾讯开放平台）

https://open.weixin.qq.com/

融云IM

https://developer.rongcloud.cn/signin

百度开发者中心

http://developer.baidu.com/

人脸识别

http://www.faceplusplus.com.cn/

高德地图:

http://lbs.amap.com/

蜻蜓:

FMhttp://open.qingting.fm



