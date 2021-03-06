## 朴素贝叶斯

### 参考链接

第4章 基于概率论的分类方法: 朴素贝叶斯 https://github.com/apachecn/AiLearning/blob/master/docs/ml/4.md

3天学会机器学习- 07 朴素贝叶斯算法原理 https://www.youtube.com/watch?v=3LFJjrcDBnE

浙江大学机器学习课程：30-概率密度估计 – 朴素贝叶斯分类器 https://haokan.baidu.com/v?vid=1299530084135768190&pd=bjh&fr=bjhauthor&type=video

算法杂货铺——分类算法之朴素贝叶斯分类(Naive Bayesian classification) https://www.cnblogs.com/leoo2sk/archive/2010/09/17/naive-bayesian-classifier.html

机器学习实战教程（五）：朴素贝叶斯实战篇之新浪新闻分类 https://cuijiahua.com/blog/2017/11/ml_5_bayes_2.html

### 背景知识

- 联合概率

P(a , b) ：a 和 b 同时发生的概率

- 条件概率

```
P(a|b) = P(a , b)  /  P(b) 
```

P(a|b) ：b 条件下，a 发生的概率

- 概率公式

相互独立条件下：

```
p(a,b) = p(a) * p(b)

p(x1,x2,x3|y) = p(x1|y) * p(x2|y) * p(x3|y) 
```



### 朴素贝叶斯

#### 背景概念

- 贝叶斯

英国数学家

贝叶斯 ( Thomas Bayes 1702-1761)

- 朴素?

假设 相互独立

- 贝叶斯法则

对于给出的待分类项，求解在此项出现的条件下各个类别出现的概率，哪个最大，就认为此待分类项属于哪个类别，贝叶斯决策理论的核心思想，即选择具有最高概率的决策

- 贝叶斯公式

```
P(y|x) = p(x|y) * p(y) / p(x)
```

#### 前提条件

- 相互独立
- 离散分布
- 同等重要

#### 优缺点

- 优点

算法逻辑简单，时空开销小，易于实现

数据缺失下仍然可以处理

- 缺点

相互独立，往往不好成立

对输入数据的准备方式敏感

### 实例

- 数据

|      | 症状   | 职业     | 疾病   |
| ---- | ------ | -------- | ------ |
| 1    | 打喷嚏 | 护士     | 感冒   |
| 2    | 打喷嚏 | 农夫     | 过敏   |
| 3    | 头痛   | 建筑工人 | 脑震荡 |
| 4    | 头痛   | 建筑工人 | 感冒   |
| 5    | 打喷嚏 | 教师     | 感冒   |
| 6    | 头痛   | 教师     | 脑震荡 |

- 问题

来了第7个病人，是一个打喷嚏的建筑工人

请问他是不是患上感冒？

- 计算

根据贝叶斯公式：

```
P(A|B) = P(B|A) * P(A) / P(B)
```

代入得：

```
P(感冒|打喷嚏, 建筑工人)
　　　　= P(打喷嚏, 建筑工人|感冒)
         * P(感冒)
　　　　  / P(打喷嚏, 建筑工人)
```

由特征独立得：

```
       = P(打喷嚏|感冒) * P(建筑工人|感冒) 
         * P(感冒)
　　　　   / [P(打喷嚏) * P(建筑工人)]
```

计算：

```
      =  (2 / 3)  * (1 / 3)
　　　　   * (3 / 6)
　　　　   / [P(3 / 6) * P(2 / 6)]
　　　　=  2 / 3
```

### 贝叶斯分类

#### 工作原理

```
提取所有文档中的词条并进行去重
获取文档的所有类别
计算每个类别中的文档数目
对每篇训练文档: 
    对每个类别: 
        如果词条出现在文档中-->增加该词条的计数值（for循环或者矩阵相加）
        增加所有词条的计数值（此类别下词条总数）
对每个类别: 
    对每个词条: 
        将该词条的数目除以总词条数目得到的条件概率（P(词条|类别)）
返回该文档属于每个类别的条件概率（P(类别|文档的所有词条)）
```

### 总结

#### 注意事项

使用的大前提

存在误差

#### 高斯朴素贝叶斯

比如身高特征：特征的概率假设为高斯分布

#### 优化

- 拉普拉斯平滑

p(x1|y) * p(x2|y) * p(x3|y) ，如果其中有一个概率值为0，那么最后的成绩也为0；为降低这种影响，可以将所有词的出现数初始化为1，并将分母初始化为2。又被称为加1平滑，是比较常用的平滑方法

- 取自然对数

下溢出问题，这是由于太多很小的数相乘造成的。在程序中，在相应小数位置进行四舍五入，计算结果可能就变成0了。同时，采用自然对数进行处理不会有任何损失

- 文本优化

关键字提取：提前剔除掉对结果没有影响的词语，比如：的，时候
词语组合：S 、 B 、 SB



