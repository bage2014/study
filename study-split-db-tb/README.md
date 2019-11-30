# study-split-db-tb #
分库分表 学习笔记

## 参考链接 ##
开源 数据库分库分表 [https://www.aliyun.com/citiao/999897.html](https://www.aliyun.com/citiao/999897.html)]、[https://my.oschina.net/u/2335525/blog/1855103](https://my.oschina.net/u/2335525/blog/1855103)

## 背景 ##
单表数据量过大，比如一张表的日增数据量达到10000条，则一年有 3650000条，基于数据库的查询，出现了性能瓶颈

## 概述 ##
顾名思义，将数据，分别存储到不同的数据库中
比如

    原来用户表tb_user的数据全部存储在mydb中，

现在改为：


    用户表tb_user的数据，经由一定规则，分别存储到mydb1、mydb2、mydb3中

## 优缺点 ##
- 优点
 1.	解决性能瓶颈问题，提高查询效率
 2.	降低数据库读写压力
 3.	
- 缺点
 1. join、queryByPage、group by等操作变得难实现或不能实现
 2. 增加程序实现的复杂性

## 基本原理 ##



## 开源实现 ##


## 重难点实现思路 ##




