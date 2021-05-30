## 朴素贝叶斯

### 参考链接

第4章 基于概率论的分类方法: 朴素贝叶斯 https://github.com/apachecn/AiLearning/blob/master/docs/ml/4.md

3天学会机器学习- 07 朴素贝叶斯算法原理 https://www.youtube.com/watch?v=3LFJjrcDBnE

浙江大学机器学习课程：30-概率密度估计 – 朴素贝叶斯分类器 https://haokan.baidu.com/v?vid=1299530084135768190&pd=bjh&fr=bjhauthor&type=video

### 学习目标



### 背景知识

- 联合概率

P(a , b)

- 条件概率

P(a|b) = P(a , b)  /  P(b)

- 概率公式

相互独立条件下：

1. p(a,b) = p(a) * p(b)
2. p(x1,x2,x3|y) = p(x1|y) * p(x2|y) * p(x3|y) 

### 朴素贝叶斯

#### 基本概念

贝叶斯决策理论的核心思想，即选择具有最高概率的决策



```
P(y|x) = p(x|y) * p(y) / p(x)
```



![https://github.com/apachecn/AiLearning/blob/master/docs/ml/img/NB_5.png](https://github.com/apachecn/AiLearning/blob/master/docs/ml/img/NB_5.png)





#### 前提条件

- 相互独立
- 离散分布
- 同等重要

#### 优缺点

- 优点

在数据较少的情况下仍然有效，可以处理多类别问题。

- 缺点

对于输入数据的准备方式较为敏感。

### 总结





