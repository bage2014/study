# study-best-practice

程序异常分析 



## 一般思路

### 大方向定位

内存使用排序

CPU 使用排序 



**参考链接**

30分钟左右： https://www.bilibili.com/video/BV1R841147bY/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1




## GC频繁

找到内存占⽤用最多的Class

```
jmap -histo[:live] {pid}

com.bage.study.best.practice.test.GcTest
```

查看查询 

```
show processlist;
```



## 分析过程 





