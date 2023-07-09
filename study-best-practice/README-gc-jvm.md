# study-best-practice

程序异常分析 




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





