

## 背景

JVMTI

调用链

热更新

线上debug

## 概述

Java 探针

JDK 1.5 +

attach 方式 JDK 1.6+

## 基本功能

- 类加载前修改字节码
- 运行时修改字节码
- 获取 类、对象 信息

## API 接口

包结构 

![instrument-packages](.\images\instrument-packages.png)

Instrumentation

![instrument-methods](.\images\instrument-methods.png)

## 实例Demo

### 启动时候挂载

**实现ClassFileTransformer**

![code-trsf](.\images\code-trsf.png)

```
public class MyClassInfo {

    private String className;
    private List<String> methodNames;
    // getter setter etc.
}
```

**编写 MyAgent 类，premain 方法，装配 MyClassFileTransformer**

![code-agt](.\images\code-agt.png)

**配置 MyAgent，使之生效**

```
<plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                        </manifest>
                        <manifestEntries>
                            <Can-Redefine-Classes>true</Can-Redefine-Classes>
                            <Premain-Class>com.bage.study.agent.transform.service.MyAgent</Premain-Class>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
```

**打包启动**

```
mvn package 

mvn assembly:assembly

java -javaagent:transform-service-1.0-SNAPSHOT.jar -jar demo-service-1.0-SNAPSHOT.jar


```

运行结果

![run-result-1](.\images\run-result-1.png)



### 运行时更新



**编写 MyAgent 类，agentmain 方法**

![code-agt](.\images\code-agt.png)

**配置 MyAgent，使之生效**

```
<plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
          </descriptorRefs>
          <archive>
            <manifest>
              <addClasspath>true</addClasspath>
            </manifest>
            <manifestEntries>
              <Can-Redefine-Classes>true</Can-Redefine-Classes>
              <Re-Transform-Classes>true</Re-Transform-Classes>
              <Agent-Class>com.bage.study.agent.hotload.service.MyAgent</Agent-Class>
            </manifestEntries>
          </archive>
        </configuration>
      </plugin>
```

**打包启动**

```
mvn assembly:assembly

```



**运行时候关联**

![code-agt-2](.\images\code-agt-2.png)



运行效果

![run-result-2](.\images\run-result-2.png)

## 注意事项

- 不能新增、删除、重命名属性或方法，也不能修改方法的签名
- 修改字节码：1.基于源代码修改重新编译 2.javassist等等
- 执行命令顺序不能更改



## 参考连接

[JVM源码分析之javaagent原理完全解读](http://lovestblog.cn/blog/2015/09/14/javaagent/)

[java agent探究](https://zhuanlan.zhihu.com/p/74255330)

[JAVA热部署，通过agent进行代码增量热替换！！！](http://www.manongjc.com/detail/19-xyhrjmatcudvkdo.html)





