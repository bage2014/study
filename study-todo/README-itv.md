# study-itv2023 #



## Schedule





## Tomcat 

请求过程 

https://www.bilibili.com/video/BV1dJ411N7Um?p=15&vd_source=72424c3da68577f00ea40a9e4f9001a1



## Retry





## MySQL

有趣历史：MySQL5.5.5版本发布于 2010

MySQL 精选 60 道面试题（含答案） https://blog.csdn.net/hahazz233/article/details/125372412

常见（MySQL）面试题（含答案） https://blog.csdn.net/m0_63592370/article/details/126039076

最完整MySQL数据库面试题（2020最新版）https://zhuanlan.zhihu.com/p/112857507

https://www.ewbang.com/community/article/details/961524446.html



## Mybaits + Plus 

Mybatis 

https://blog.csdn.net/qq_33036061/article/details/105209254

https://blog.csdn.net/m0_59281987/article/details/127768618

https://zhuanlan.zhihu.com/p/424217408

http://www.bjpowernode.com/javazixun/10429.html

https://zhuanlan.zhihu.com/p/347935099

https://mikechen.cc/17252.html



## Lock 

Java锁解析【2023-06-01；ALL】 https://www.cnblogs.com/jtxw/p/16326065.html

https://www.python51.com/jc/73707.html

https://betheme.net/dashuju/96421.html

https://blog.csdn.net/libaowen609/article/details/125343833

https://blog.csdn.net/Q_P777/article/details/124894748

https://www.jb51.net/it/713005.html

https://mp.weixin.qq.com/s?__biz=MzU1MjMzNzUwNg==&mid=2247484229&idx=1&sn=9a67add64bf3c9fee15faf63c6df5c38&chksm=fb82ea48ccf5635ea0edc5ff40662680102d6e9e04ce30686332abd860e940f250b7cae0161c&scene=27

https://blog.csdn.net/GitChat/article/details/104871532?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-3-104871532-blog-109335237.235%5Ev36%5Epc_relevant_default_base3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-3-104871532-blog-109335237.235%5Ev36%5Epc_relevant_default_base3&utm_relevant_index=4



## LOG





## MQ

概述？ https://blog.csdn.net/m0_68850571/article/details/125854787

MQ面试题总结【Rabbit MQ】 https://blog.csdn.net/c704395972/article/details/119596005

基本对比 https://www.cnblogs.com/qingmuchuanqi48/p/11006760.html

基本知识 https://mikechen.cc/26813.html

Rabbit MQ https://baijiahao.baidu.com/s?id=1677690622513281653&wfr=spider&for=pc

https://developer.aliyun.com/article/1152461?spm=a2c6h.12873639.article-detail.36.4c5b3772MSABWs&scm=20140722.ID_community@@article@@1152461._.ID_community@@article@@1152461-OR_rec-V_1-RL_community@@article@@1040043

http://imyhq.com/blockchain/2692.html

https://maimai.cn/article/detail?fid=1750646502&efid=Xlffd0CPyLaqvMuOx_liIw

https://blog.csdn.net/zhoupenghui168/article/details/130322210



## Redis







## Spring + Boot + Cloud 



## Spring 

done- 2023-05-30 

https://mp.weixin.qq.com/s?__biz=MzU5OTc3NDM2Mw==&mid=2247483928&idx=1&sn=a7dd0674a661402975f7d13a2c3f2095&chksm=feae87ecc9d90efa3a8b642314c9b40ce136201fdfe55534d70e0fc984f3eff861774fa9cb17&scene=27

done- 2023-05-30 

http://e.betheme.net/article/show-162498.html

done- 2023-05-30 

https://zhuanlan.zhihu.com/p/93594713

https://ac.nowcoder.com/discuss/955061?type=2&order=0&pos=4&page=1&channel=-1&source_id=discuss_center_2_nctrack 【无答案】

https://blog.csdn.net/javalingyu/article/details/124381167



## Spring 高阶

https://zhuanlan.zhihu.com/p/497330696

https://blog.csdn.net/zollty/category_9275859.html

https://www.jianshu.com/p/b2adcdb59013

https://www.cnblogs.com/zhuxiong/p/7653506.html



https://blog.csdn.net/qq_33373609/article/details/121747756

http://hk.javashuo.com/article/p-pupuntul-cy.html



Spring Boot 



Spring Cloud 



## Java

牛客网

https://www.nowcoder.com/exam/interview?order=0



https://blog.csdn.net/guorui_java/article/details/119299329

https://blog.csdn.net/uuqaz/article/details/123502779

https://www.cnblogs.com/linuxxuexi/p/javamianshi.html

https://baijiahao.baidu.com/s?id=1731248649940070864&wfr=spider&for=pc







## JVM 

JVM 实践 



## Load Test 

JMeter 工具基本使用

如何发起请求

如何校验响应

上下文连接，比如登陆到个人订单详情

结果查看

常用工具对比？



## POI Search

ES 高级使用



## Split DB&Table

Schedule:

创建DB+Table

初始化： 300万、500万、一千万、5千万、1亿的表数据

分别查询、命中主键、唯一索引、常规索引的耗时情况



查看表存储空间、表容量？



数据库分库分表

- 高频写入读取模拟【高并发条件下】
- 大数据量【千万级别】
- 单表行数超过`500万`行或者单表容量超过`2GB`，才推荐进行分库分表
- DB + ES 一起使用
- 将用户信息冗余同步到ES**，同步发送到ES，然后通过ES来查询（**推荐）



### 准备工作

Mysql 安装【Docker版本】

```
Mac-pro:	
docker run --name bage-mysql-pro -v /Users/bage/bage/docker-data/mysql-pro:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=bage -p 3306:3306 -d mysql/mysql-server
```



## 分布式锁

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247498750&idx=1&sn=19fe8b4fff28fe81db14e733053bbc74&chksm=cf2224d7f855adc1d0984980a4e3de31fe33329164a472ca8d8255a8a80b69b2e23850811323&token=2001057130&lang=zh_CN#rd



https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247488142&idx=1&sn=79a304efae7a814b6f71bbbc53810c0c&chksm=cf21cda7f85644b11ff80323defb90193bc1780b45c1c6081f00da85d665fd9eb32cc934b5cf&token=162724582&lang=zh_CN&scene=21#wechat_redirect



## 分布式事务

https://mp.weixin.qq.com/s?__biz=Mzg3NzU5NTIwNg==&mid=2247498750&idx=1&sn=19fe8b4fff28fe81db14e733053bbc74&chksm=cf2224d7f855adc1d0984980a4e3de31fe33329164a472ca8d8255a8a80b69b2e23850811323&token=2001057130&lang=zh_CN#rd



## 项目总结

### 分库分表

### 架构升级2.0

### 团队建设+管理

### 技术点

POI 搜索

低价、转票

邮件抓取

优惠券抵扣

出票策略

SSO、DB、DDD、TIS、上云、GDPR、UID拆分、改签、拼单、转票、低价等

指标监控

日志、买点组件；；；日志模糊



## 参考链接

知识点整理汇总 https://github.com/rbmonster/learning-note

书籍 https://github.com/xjq7/books

知识点汇总整理 https://www.kancloud.cn/imnotdown1019/java_core_full/2177328

Java 体系 https://github.com/whx123/JavaHome

面试体系整理 https://gitee.com/souyunku/DevBooks

bage面试 https://github.com/bage2014/interview

知识汇总 https://github.com/Snailclimb/JavaGuide

itv 汇总 https://github.com/CoderLeixiaoshuai/java-eight-part

Java 基础 https://pdai.tech/md/java/collection/java-map-HashMap&HashSet.html



https://github.com/doocs/advanced-java

https://tech.meituan.com/2016/06/24/java-hashmap.html






【2023-06-30】牛客网 https://www.nowcoder.com/experience/639

牛客网题库 https://www.nowcoder.com/exam/company

分库分表经典15连问 http://blog.csdn.net/BASK2311/article/details/128312076

【2023-06-29】【包含编程题todo】2023Java最新真实面试题汇总（持续更新）https://blog.csdn.net/chuanchengdabing/article/details/118527318

Java 体系整理 https://img-blog.csdnimg.cn/2021071520572359.png

2023年Java面试题及答案整理 https://www.rstk.cn/news/2473.html?action=onClick

【2023-06-28】2023各大厂Java面试题来喽！面试必看！ https://baijiahao.baidu.com/s?id=1757886422930954006&wfr=spider&for=pc

【2023-06-28】119道题 https://mikechen.cc/19105.html

【2023-06-28】知乎汇总 https://zhuanlan.zhihu.com/p/477276448

【2023-06-28】阿里 https://blog.csdn.net/shy111111111/article/details/129317020



## 兴趣点

ISAM 引擎【不是MyISAM】

MySQL 5.5.5发布时间