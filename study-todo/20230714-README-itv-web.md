# study-web  #
## 简介





## Key关键点

- 行业调研、优势、代替方案等
- 架构
- GET POST 请求差异
- Session Cookie
- Servlet 生命周期
- 转发 和 重定向 
- HTTP 协议
- 长链接 和 短链接 





## **概要** 



## **架构** 





## Servlet 生命周期 

Servlet对象是Servlet容器创建的，生命周期方法都是由容器调用的。

Servlet 生命周期可被定义为从创建直到毁灭的整个过程。以下是 Servlet 遵循的过程：

- Servlet 通过调用 **init ()** 方法进行初始化。只能被调用一次。
- Servlet 调用 **service()** 方法来处理客户端的请求。对于 Servlet 的每一次访问请求，Servlet 容器都会调用一次 Servlet 的 service() 方法
- Servlet 通过调用 **destroy()** 方法终止（结束）。
- Servlet 最后是由 JVM 的垃圾回收器进行垃圾回收的。





## 长链接 和 短链接 

https://zhuanlan.zhihu.com/p/522429315

https://www.jianshu.com/p/3fc3646fad80

![](https://img-blog.csdnimg.cn/e8feb70514844f0dbf2119f4febf46e5.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5Lmx5by55LiW55WM,size_20,color_FFFFFF,t_70,g_se,x_16)



## Bilibili 



## 参考链接 

https://baijiahao.baidu.com/s?id=1724160081335984106&wfr=spider&for=pc