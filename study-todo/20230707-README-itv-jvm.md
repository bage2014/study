# study-JVM

## 简介

主要说： HotSpot JVM



## Key关键点

- 行业调研、优势、代替方案等
- 架构
- 类加载过程
- 触发类加载
- 类加载顺序
- JVM 虚拟机各种类型
- 参数含义【核心参数、调优】
- 命令行工具
- 可视化工具
- JVM 调优
- JVM 常用参数 
- GC 触发场景和条件【验证GC 过程 】
- 频繁GC的场景
- Full GC 优化
- 内存泄露监控
- 对象头信息
- OOM 场景和处理过程 
- 内存分配
- 三色标记
- 安全点 safepoint



## **概要** 



## 架构 

大概结构 

![](https://images0.cnblogs.com/i/288799/201405/281726404166686.jpg)

### 常用组件 

https://blog.csdn.net/qq_51330350/article/details/127538316

https://www.cnblogs.com/dolphin0520/p/3613043.html

#### 程序计数器

　　程序计数器（Program Counter Register），保存程序当前执行的指令的地址，当CPU需要执行指令时，需要从程序计数器中得到当前需要执行的指令所在存储单元的地址，然后根据得到的地址获取到指令，在得到指令之后，程序计数器便自动加1或者根据转移指针得到下一条指令的地址，如此循环，直至执行完所有的指令。

　　在JVM规范中规定，如果线程执行的是非native方法，则程序计数器中保存的是当前需要执行的指令的地址；如果线程执行的是native方法，则程序计数器中的值是undefined。

　　由于程序计数器中存储的数据所占空间的大小不会随程序的执行而发生改变，因此，对于程序计数器是不会发生内存溢出现象(OutOfMemory)的。



#### Java栈

　　Java栈也称作虚拟机栈（Java Vitual Machine Stack），也就是我们常常所说的栈，跟C语言的数据段中的栈类似。事实上，Java栈是Java方法执行的内存模型。

　　Java栈中存放的是一个个的栈帧，每个栈帧对应一个被调用的方法，在栈帧中包括局部变量表(Local Variables)、操作数栈(Operand Stack)、指向当前方法所属的类的运行时常量池（运行时常量池的概念在方法区部分会谈到）的引用(Reference to runtime constant pool)、方法返回地址(Return Address)和一些额外的附加信息。

​		当线程执行一个方法时，就会随之创建一个对应的栈帧，并将建立的栈帧压栈。当方法执行完毕之后，便会将栈帧出栈。因此可知，线程当前执行的方法所对应的栈帧必定位于Java栈的顶部。，这部分空间的分配和释放都是由系统自动实施的。

![](https://images0.cnblogs.com/i/288799/201405/291429030562182.jpg)



![](https://img-blog.csdnimg.cn/ce4717358430466fb5d122342cd540aa.png)

#### 本地方法栈

　　本地方法栈与Java栈的作用和原理非常相似。区别只不过是Java栈是为执行Java方法服务的，而本地方法栈则是为执行本地方法（Native Method）服务的。

​		在JVM规范中，并没有对本地方发展的具体实现方法以及数据结构作强制规定，虚拟机可以自由实现它。在HotSopt虚拟机中直接就把本地方法栈和Java栈合二为一。

#### 堆

　　Java中的堆是用来存储对象本身的以及数组（当然，数组引用是存放在Java栈中的）。只不过和C语言中的不同，在Java中，程序员基本不用去关心空间释放的问题，Java的垃圾回收机制会自动进行处理。因此这部分空间也是Java垃圾收集器管理的主要区域。另外，堆是被所有线程共享的，在JVM中只有一个堆。

#### 方法区

　　方法区在JVM中也是一个非常重要的区域，它与堆一样，是被线程共享的区域。在方法区中，存储了**类信息**（包括类的名称、方法信息、字段信息）、**静态变量**、**常量**以及编译器编译后的代码等。

　　在Class文件中除了类的字段、方法、接口等描述信息外，还有一项信息是常量池，用来存储编译期间生成的字面量和符号引用。

　　在方法区中有一个非常重要的部分就是运行时常量池，它是每一个类或接口的常量池的运行时表示形式，在类和接口被加载到JVM后，对应的运行时常量池就被创建出来。当然并非Class文件常量池中的内容才能进入运行时常量池，在运行期间也可将新的常量放入运行时常量池中，比如String的intern方法。

　　在JVM规范中，没有强制要求方法区必须实现垃圾回收。很多人习惯将方法区称为“永久代”，是因为**HotSpot虚拟机以永久代来实现方法区**，从而JVM的垃圾收集器可以像管理堆区一样管理这部分区域，从而不需要专门为这部分设计垃圾回收机制。不过自从JDK7之后，Hotspot虚拟机便将运行时常量池从永久代移除了。



### 内存结构 

#### 堆空间 

新生代 + 老年代 

新生代 = Eden + From + To 

Eden 新生区；From 和 To 相互拷贝回收，理论上某一时刻只用到一个



## 字节码文件 

**文件结构属性**

字节码文件信息\常量池\方法表集合\类名\等等

![](https://pdai.tech/images/jvm/java-jvm-class-2.png)



**反编译工具**

输入命令查看输出内容

`javap -verbose -p Main.class` 

```

bage@bagedeMacBook-Pro bage % javac HelloTest.java 
```



## 类加载过程

https://www.cnblogs.com/fanjie/p/6916784.html

https://javaguide.cn/java/basis/java-basic-questions-01.html#%E4%BB%80%E4%B9%88%E6%98%AF%E5%AD%97%E8%8A%82%E7%A0%81-%E9%87%87%E7%94%A8%E5%AD%97%E8%8A%82%E7%A0%81%E7%9A%84%E5%A5%BD%E5%A4%84%E6%98%AF%E4%BB%80%E4%B9%88

![](https://oss.javaguide.cn/github/javaguide/java/basis/jvm-rough-structure-model.png)

**概述**

java运行过程就可以分为  编译  》 类加载  》  执行

**主要过程** 

类加载主要是由jvm虚拟机负责的，过程非常复杂，类加载分三步  加载   》  连接  》初始化

- 加载

  这个很简单，程序运行之前jvm会把编译完成的.class二进制文件加载到内存，供程序使用，用到的就是类加载器classLoader ，这里也可以看出java程序的运行并不是直接依   靠底层的操作系统，而是基于jvm虚拟机。如果没有类加载器，java文件就只是磁盘中的一个普通文件。

- 连接

  连接是很重要的一步，过程比较复杂，分为三步  验证  》准备  》解析  　　

  - 验证：确保类加载的正确性。一般情况由javac编译的class文件是不会有问题的，但是可能有人的class文件是自己通过其他方式编译出来的，这就很有可能不符合jvm的编 译规则，这一步就是要过滤掉这部分不合法文件　
  - 准备：为类的静态变量分配内存，将其初始化为默认值 。我们都知道静态变量是可以不用我们手动赋值的，它自然会有一个初始值 比如int 类型的初始值就是0 ；boolean类型初始值为false，引用类型的初始值为null 。 这里注意，只是为静态变量分配内存，此时是没有对象实例的
  - 解析：把类中的符号引用转化为直接引用。解释一下符号引用和直接引用。比如在方法A中使用方法B，A（）{B（）；}，这里的B（）就是符号引用，初学java时我们都是知道这是java的引用，以为B指向B方法的内存地址，但是这是不完整的，这里的B只是一个符号引用，它对于方法的调用没有太多的实际意义，可以这么认为，他就是给程序员看的一个标志，让程序员知道，这个方法可以这么调用，但是B方法实际调用时是通过一个指针指向B方法的内存地址，这个指针才是真正负责方法调用，他就是直接引用。

- 初始化

  为类的静态变量赋予正确的初始值，上述的准备阶段为静态变量赋予的是虚拟机默认的初始值，此处赋予的才是程序编写者为变量分配的真正的初始值



## 对象初始化

### 类加载检查

“虚拟机遇到一条new指令时，首先将去检查这个指令的参数是否能在常量池中定位到一个类的符号引用，并且检查这个符号引用代表的类是否已被加载、解析和初始化过。如果没有，那必须先执行相应的类加载过程”

摘录来自: 周志明. “深入理解Java虚拟机：JVM高级特性与最佳实践。” Apple Books. 

### 分配内存

“在类加载检查通过后，接下来虚拟机将为新生对象分配内存。对象所需内存的大小在类加载完成后便可完全确定（如何确定将在2.3.2节中介绍），为对象分配空间的任务等同于把一块确定大小的内存从Java堆中划分出来”

摘录来自: 周志明. “深入理解Java虚拟机：JVM高级特性与最佳实践。” Apple Books. 

### 对象初始化

“在上面工作都完成之后，从虚拟机的视角来看，一个新的对象已经产生了，但从Java程序的视角来看，对象创建才刚刚开始——＜init＞方法还没有执行，所有的字段都还为零。所以，一般来说（由字节码中是否跟随invokespecial指令所决定），执行new指令之后会接着执行＜init＞方法，把对象按照程序员的意愿进行初始化，这样一个真正可用的对象才算完全产生出来。”

摘录来自: 周志明. “深入理解Java虚拟机：JVM高级特性与最佳实践。” Apple Books. 



## 内存分配方式

### Bump the Pointer

“假设Java堆中内存是绝对规整的，所有用过的内存都放在一边，空闲的内存放在另一边，中间放着一个指针作为分界点的指示器，那所分配内存就仅仅是把那个指针向空闲空间那边挪动一段与对象大小相等的距离，这种分配方式称为“指针碰撞”（Bump the Pointer）。

### Free List

如果Java堆中的内存并不是规整的，已使用的内存和空闲的内存相互交错，那就没有办法简单地进行指针碰撞了，虚拟机就必须维护一个列表，记录上哪些内存块是可用的，在分配的时候从列表中找到一块足够大的空间划分给对象实例，并更新列表上的记录，这种分配方式称为“空闲列表”（Free List）”

摘录来自: 周志明. “深入理解Java虚拟机：JVM高级特性与最佳实践。” Apple Books. 



## 参数解析

**1、** jps -v 可以查看 jvm 进程显示指定的参数

**2、** 使用 -XX:+PrintFlagsFinal 可以看到 JVM 所有参数的值

**3、** jinfo 可以实时查看和调整虚拟机各项参数

### 堆相关

-Xms【memory size】 初试堆大小 

-Xmx 【memory max】 最大堆大小

-XX:MinHeapFreeRatio 空余内存比例【增大使用】

-XX:MaxHeapFreeRatio 空余内存比如 【减少使用】 

- 堆得内存由-Xms指定，默认是物理内存的1/64；最大的内存由-Xmx指定，默认是物理内存的1/4。
-  默认空余的堆内存小于40%时，就会增大，直到-Xmx设置的内存。具体的比例可以由-XX:MinHeapFreeRatio指定
-  空余的内存大于70%时，就会减少内存，直到-Xms设置的大小。具体由-XX:MaxHeapFreeRatio指定。

–XX:NewRatio 新生代老年代 的比例

新生代 ( Young ) 与老年代 ( Old ) 的比例的值为 1:2 ( 该值可以通过参数 –XX:NewRatio 来指定 )

默认的，Eden : from : to = 8 : 1 : 1 ( 可以通过参数 –XX:SurvivorRatio 来设定 )，即： Eden = 8/10 的新生代空间大小，from = to = 1/10 的新生代空间大小。

*Perm代表的是永久代，不算再堆里面，jdk8 以后移除*

-Xss128k：设置每个线程的栈大小。JDK5.0以后每个线程栈大小为1M，之前每个线程栈大小为256K。应当根据应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。需要注意的是：当这个值被设置的较大（例如>2MB）时将会在很大程度上降低系统的性能。
-Xmn2g：设置年轻代大小为2G。在整个堆内存大小确定的情况下，增大年轻代将会减小年老代，反之亦然。此值关系到JVM垃圾回收，对系统性能影响较大，官方推荐配置为整个堆大小的3/8。
-XX:NewSize=1024m：设置年轻代初始值为1024M。
-XX:MaxNewSize=1024m：设置年轻代最大值为1024M。
-XX:PermSize=256m：设置持久代初始值为256M。
-XX:MaxPermSize=256m：设置持久代最大值为256M。
-XX:NewRatio=4：设置年轻代（包括1个Eden和2个Survivor区）与年老代的比值。表示年轻代比年老代为1:4。
-XX:SurvivorRatio=4：设置年轻代中Eden区与Survivor区的比值。表示2个Survivor区（JVM堆内存年轻代中默认有2个大小相等的Survivor区）与1个Eden区的比值为2:4，即1个Survivor区占整个年轻代大小的1/6。
-XX:MaxTenuringThreshold=7：表示一个对象如果在Survivor区（救助空间）移动了7次还没有被垃圾回收就进入年老代。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代，对于需要大量常驻内存的应用，这样做可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象在年轻代存活时间，增加对象在年轻代被垃圾回收的概率，减少Full GC的频率，这样做可以在某种程度上提高服务稳定性。



### 方法区

- -Xss设置每个线程的堆栈大小
- -XX:+UseParallelGC:选择垃圾收集器为并行收集器。此配置仅对年轻代有效。即上述配置下,年轻代使用并发收集,而年老代仍旧使用串行收集。
- -XX:ParallelGCThreads=20:配置并行收集器的线程数,即:同时多少个线程一起进行垃圾回收。此值最好配置与处理器数目相等。



参数解析 https://blog.csdn.net/Pluto372/article/details/122084515

参数说明 https://zhuanlan.zhihu.com/p/92588056



## 三色标记

在并发的可达性分析算法中我们使用三色标记（Tri-color Marking）来标记对象是否被收集器访问过：

- 白色：表示对象尚未被垃圾收集器访问过。显然在可达性分析刚刚开始的阶段，所有的对象都是白色的，若在分析结束的阶段，仍然是白色的对象，即代表不可达。
- 黑色：表示对象已经被垃圾收集器访问过，且这个对象的所有引用都已经扫描过。黑色的对象代表已经扫描过，它是安全存活的，如果有其他对象引用指向了黑色对象，无须重新扫描一遍。黑色对象不可能直接（不经过灰色对象）指向某个白色对象。
- 灰色：表示对象已经被垃圾收集器访问过，但这个对象上至少存在一个引用还没有被扫描过。

​    可达性分析的扫描过程，其实就是一股以灰色为波峰的波纹从黑向白推进的过程，但是在并发的推进过程中会产生“对象消失”的问题，如图：

链接：https://juejin.cn/post/7303414348932382774

https://zhuanlan.zhihu.com/p/621461833

https://www.bilibili.com/video/BV1KU4y1Y7cu/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1eu4y127EG/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1

![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d0f6395054ae42a0998c33eea98b8b2a~tplv-k3u1fbpfcp-jj-mark:3024:0:0:0:q75.awebp#?w=1095&h=1155&s=442276&e=png&b=fdfdfd)



## GC 名词概念

https://cloud.tencent.com/developer/article/2308202

#### **部分收集(Partial GC)**

##### **新生代收集**

Young GC   Minor GC

Eden、s1、s2的清理，都是发生在新生代

##### **老年代收集**

Major GC ，Old GC

#### **整堆收集**

Full GC，清理整个堆空间，包括年轻代和老年代，理解

## 垃圾回收 

https://cloud.tencent.com/developer/article/2472162

## 回收算法

https://juejin.cn/post/7303414348932382774

https://cloud.tencent.com/developer/article/2308202

### 标记清除算法

标记无用对象，然后进行清除回收，标记-清除算法是一种常见的基础垃圾收集算法，他将垃圾收集分为两个阶段

1. 标记阶段：标记出可以回收的对象
2. 清除阶段：回收标记的对象所占用的空间

优点：实现简单，不需要对象进行移动
 缺点：标记、清除过程效率低，产生大量不连续的内存碎片，提高了垃圾回收的效率



这个算法，就是把死忘的对象标记出来，然后清除掉，那么清除完之后，就会出现碎片问题，因为存活的对象和死亡的对象在内存中是掺杂一起的，把死亡对象清走，留下的就是零零碎碎的位置。

![](https://developer.qcloudimg.com/http-save/9862454/eeeb7395bf13a35400741797f3048283.png)



### 复制算法

为了解决标记-清除算法的效率不高的问题，产生了复制算法。它把内存空间划分为两个相等的区域，每次只使用其中一个区域。垃圾收集时，遍历当前使用的区域，把存活对象复制到另一个区域中，最后将当前使用的区域的可回收对象进行回收

优点：按顺序分配内存即可，实现简单、运行高效，不用考虑内存碎片
 缺点：可用的内存大小缩小为原来的一半，对象存活率高时会频繁进行复制



这个算法，就是为了避免内存碎片问题，标记存活的对象，移动到另一片空闲的位置，空闲区域，之后，以新移动的空间作为现在的活动区，之前活动区域，直接全清理掉。作为空闲区，方便下一次GC。

![](https://developer.qcloudimg.com/http-save/9862454/cefd4fa40aabbeea1ffe46b7a4576141.png)





### 标记压缩算法[标记整理算法]

在新生代中可以使用复制算法，但是在老年代就不能选择复制算法了，因为老年代的对象存活率会较高，这样会有较多的复制操作，导致效率变低。标记-清除算法应该可以应用在老年代中，但是它效率不高，在内存回收后容易产生大量内存碎片。因此就出现了一种标记-整理算法，与标记-清除不同的是，在标记可回收的对象后将所有存活的对象压缩到内存的一端，使他们紧凑的排列在一起，然后对端边界以外的内存进行回收。回收后，已用和未用的内存都各自一边

优点：解决了标记-清除算法存在内存碎片问题
 缺点：仍需要进行局部对象移动，一定程度上降低了效率



这种算法，不会像标记清除算法那样，有内存碎片问题，且不会像标记复制算法那样，需要额外的空间。但是移动的操作，算法的效率就比标记辅助慢些，所以没有万金油，要根据合适的场景。

![](https://developer.qcloudimg.com/http-save/9862454/9c6691059a9a8fb2e60393aeaa7009e5.png)



### **分代收集算法思想**

上面我们看堆空间采用垃圾回收期，采用新生代和老年代，采用的就是分代回收算法的思想，年轻代存活时间短，死的快，就采用高频回收，老年代都是老油条，老不死的，就进行低频回收。

分代算法根据对象特点，年轻代适合标记复制算法，刚才也讲到了，只复制少量存活的，老年代则适合标记清除，标记压

缩算法，因为存活的是多数的，清除少量死亡的。

 通过新生代和老年代的划分，使得MinorGC的频率更高，早早的把死的快的对象回收完，减少Old区内存不足，发生FullGC的频率。



## GC Root

### 回答1

链接：https://www.zhihu.com/question/595485360/answer/3071179499

GC Root（Garbage Collection Root）是指

在Java虚拟机中被直接或间接引用的对象集合，它们被认为是存活对象，不能被垃圾回收器回收。

GC Root包括以下几种类型：

 **1.虚拟机栈中引用的对象**

每次函数调用调用都是一次入栈。在栈中包括局部变量表和操作数栈，局部变量表中的变量可能为引用类型(reference)，他们引用的对象即可作为GC Root。不过随着函数调用结束出栈，这些引用便会消失。

 **2.方法区中类静态属性引用的对象**

简单的说就是我们在类中使用的static声明的引用类型字段

 **3.方法区中常量引用的对象**

简单的说就是我们在类中使用final声明的引用类型字段

 **4.Native方法中引用的对象**

就是程序中native本地方法引用的对象

 **5.活动线程中的对象**

 **6.当前类加载器加载的类的对象**

GC Root的作用是为垃圾回收器提供一个初始的扫描位置，以便确定哪些对象是可达的，哪些对象是不可达的。垃圾回收器会从GC Root开始扫描，并标记所有可达对象，最终将不可达对象回收掉。

### 回答2

#### GC Root 的特点

当前时刻存活的对象

#### GC Root 在哪里

所有Java线程当前活跃的栈帧里指向GC堆里的对象的引用；换句话说，当前所有正在被调用的方法的引用类型的参数/局部变量/临时值。
VM的一些静态数据结构里指向GC堆里的对象的引用，例如说HotSpot VM里的Universe里有很多这样的引用。



## 回收器

https://cloud.tencent.com/developer/article/2308202 腾讯云介绍 

总结归纳 https://pdai.tech/md/java/jvm/java-jvm-gc.html#3-%E6%96%B9%E6%B3%95%E5%8C%BA%E7%9A%84%E5%9B%9E%E6%94%B6 

G1介绍 https://pdai.tech/md/java/jvm/java-jvm-gc-g1.html

官方？ http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/G1GettingStarted/index.html

https://www.bilibili.com/video/BV1Bs4y1N7KZ/?spm_id_from=333.788&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://www.bilibili.com/video/BV1f3411f7LC/?spm_id_from=pageDriver&vd_source=72424c3da68577f00ea40a9e4f9001a1

https://gitee.com/souyunku/DevBooks/blob/master/docs/Jvm/Jvm%E6%9C%80%E6%96%B02021%E5%B9%B4%E9%9D%A2%E8%AF%95%E9%A2%98%E5%8F%8A%E7%AD%94%E6%A1%88%EF%BC%8C%E6%B1%87%E6%80%BB%E7%89%88.md#4%E5%90%84%E7%A7%8D%E5%9B%9E%E6%94%B6%E5%99%A8%E5%90%84%E8%87%AA%E4%BC%98%E7%BC%BA%E7%82%B9%E9%87%8D%E7%82%B9cmsg1

https://cloud.tencent.com/developer/article/2308202

### **Serial（新生代+老年代）**

这类垃圾收集器就比较鸡肋了，在单核cpu环境比较高效，适用于小型的应用，一般javaweb，springboot不会采用这类收集器

算法，新生代采用标记复制，老年代采用标记整理



Serial一般和CMS进行配合，或者和Serial Old,

Serial Old是Serial收集器的老年代版本，jdk5之前和Parallel配合使用，或者作为CMS的备选方案

新生代采用单线程进行标记复制算法的回收，老年代也是单线程，进行标记整理算法，都会发生STW



### Serial Old 

Serial 中专门处理老年代的部分



### **ParNew（新生代）**

工作在年轻代上，和ParNew的区别就是将穿行改为了并行，其他基本和Serial一样，应用大型项目，单核比Serial低

算法：新生代采用复制算法，老年代采用标记整理算法

新生代多个线程进行标记复制算法，老年代取与对应的回收器

### **Parallel（新生代+老年代）**

新生代垃圾收集器，

新生代采用标记复制，老年代采用标记整理

算法：新生代采用标记复制算法，老年代采用标记整理算法，

新生代多个线程进行标记复制，老年代标记整理也是多个线程，并行处理，都会发生STW

Parallel与ParNew区别

-XX:+UseParallelGC仅仅对年轻代有效，不可以和CMS收集器同时使用

-XX:+UseParNewGC设置年轻代为多线程收集，可以和CMS配合使用



### **CMS（老年代）**

执行过程 、并行回收

全程交错Concurrent Mark Sweep，是一款并发的，使用标记清除算法！的垃圾回收期

针对老年代使用的

适用于对响应要求较高的应用程序

整个过程分四步

初始标记、并发标记、重新标记、并发清除

老年代中，初始标记，单线程进行标记与GCROOT直接关联的对象，会发生STW，

并发标记，单个线程处理，此时不会影响用户线程的执行，不会STW

重新标记：处理并发标记出现错误标记的，多个线程重新标记，会发生STW

并发清理：线程与线程并发清理，此时不会STW 



### G1

G1垃圾回收器

Garbage First所以叫做G1

在JDK9的时候成为默认的垃圾收集器

**核心思想**

将内存划分多个独立的区域Region，取消了物理上年轻代与老年代的物理划分，但是保留了逻辑上的年轻代与老年代，新增了一个H区存放大对象，

分代没有固定思想。年轻代与老年代的大小式动态分配的

局部采用标记复制算法，整体采用标记整理算法，没有内存碎片问题

通过动态的判断进行垃圾回收，引入MixedGC，动态回收，尽量避免FullGC的发生，从而提高响应速度

**注意点**！

由于G1的特性，动态分配年轻代与老年代空间的 ，所以不手工设置年轻代大小，比如使用 -Xmn 选项或 -XX:NewRatio 等设置年轻代大小

暂停时间的目标不要设置太小，通常100~200ms比较合理，太短的化，每次只能回收很小一部分，可能导致垃圾堆积

![](https://developer.qcloudimg.com/http-save/9862454/6cbe6b99076967fc50d14149114058ed.png)

内存结构



![](https://pdai.tech/images/pics/9bbddeeb-e939-41f0-8e8e-2b1a0aa7e0a7.png)

**总结**

- Region动态变化，可能垃圾回收之前是年轻代，之后变成老年代，这样回收更精细化
- 整体标记整理，局部标记复制，不产生内存碎片
- 除了YongGC，FullGC，还有混合GC:MixedGC



腾讯云文章 https://cloud.tencent.com/developer/article/2308202

B站视频 https://www.bilibili.com/video/BV1ZV4y1i7hc/?spm_id_from=333.337.search-card.all.click&vd_source=72424c3da68577f00ea40a9e4f9001a1



### GC回收器汇总



![](https://developer.qcloudimg.com/http-save/9862454/4ff8f8c99228573cac6050b409d800ff.png)

## GC 相关

### 执行过程

**GC回收流程如下：**

**1、** 对于整个的GC流程里面，那么最需要处理的就是新生代和老年代的内存清理操作，而元空间（永久代）都不在GC范围内

**2、** 当现在有一个新的对象产生，那么对象一定需要内存空间，平均每个栈内存存4k，每个堆内存存8k，那么对象一定需要进行堆空间的申请

**3、** 首先会判断Eden区是否有内存空间，如果此时有内存空间，则直接将新对象保存在伊甸园区。

**4、** 但是如果此时在伊甸园区内存不足，那么会自动执行一个Minor GC 操作，将伊甸园区的无用内存空间进行清理，Minor GC的清理范围只在Eden园区，清理之后会继续判断Eden园区的内存空间是否充足？如果内存空间充足，则将新对象直接在Eden园区进行空间分配。

**5、** 如果执行Minor GC 之后发现伊甸园区的内存空间依然不足，那么这个时候会执行存活区的判断，如果存活区有剩余空间，则将Eden园区部分活跃对象保存在存活区，那么随后继续判断Eden园区的内存空间是否充足，如果充足怎则将新对象直接在Eden园区进行空间分配。

**6、** 此时如果存活区没有内存空间，则继续判断老年区。则将部分存活对象保存在老年代，而后存活区将有空余空间。

**7、** 如果这个时候老年代也满了，那么这个时候将产生Major GC（Full GC）,那么这个时候将进行老年代的清理

**8、** 如果老年代执行Full GC之后，无法进行对象的保存，则会产生OOM异常,OutOfMemoryError异常

### 分代执行过程

分为新生代和老年代，新生代默认占总空间的 1/3，老年代默认占 2/3。

新生代使用复制算法，有 3 个分区：Eden、To Survivor、From Survivor，它们的默认占比是 8:1:1。

当新生代中的 Eden 区内存不足时，就会触发 Minor GC，过程如下：

**1、** 在 Eden 区执行了第一次 GC 之后，存活的对象会被移动到其中一个 Survivor 分区；

**2、** Eden 区再次 GC，这时会采用复制算法，将 Eden 和 from 区一起清理，存活的对象会被复制到 to 区；

**3、** 移动一次，对象年龄加 1，对象年龄大于一定阀值会直接移动到老年代

**4、** Survivor 区相同年龄所有对象大小的总和 (Survivor 区内存大小 * 这个目标使用率)时，大于或等于该年龄的对象直接进入老年代。其中这个使用率通过 -XX:TargetSurvivorRatio 指定，默认为 50%

**5、** Survivor 区内存不足会发生担保分配

**6、** 超过指定大小的对象可以直接进入老年代

Major GC，指的是老年代的垃圾清理，但并未找到明确说明何时在进行Major GC

FullGC，整个堆的垃圾收集，触发条件：

**1、** 每次晋升到老年代的对象平均大小>老年代剩余空间

**2、** MinorGC后存活的对象超过了老年代剩余空间

**3、** 元空间不足

**4、** System.gc() 可能会引起

**5、** CMS GC异常，promotion failed:MinorGC时，survivor空间放不下，对象只能放入老年代，而老年代也放不下造成；concurrent mode failure:GC时，同时有对象要放入老年代，而老年代空间不足造成

**6、** 堆内存分配很大的对象

### 触发条件 

https://zhuanlan.zhihu.com/p/618722706





常用优化



不同类型差别



### GC 日志

```
[346.934s][info ][gc,start    ] GC(271) Pause Young (Normal) (G1 Evacuation Pause)
[346.934s][info ][gc,task     ] GC(271) Using 8 workers of 8 for evacuation
[346.937s][info ][gc,phases   ] GC(271)   Pre Evacuate Collection Set: 0.1ms
[346.937s][info ][gc,phases   ] GC(271)   Merge Heap Roots: 0.1ms
[346.937s][info ][gc,phases   ] GC(271)   Evacuate Collection Set: 1.8ms
[346.937s][info ][gc,phases   ] GC(271)   Post Evacuate Collection Set: 0.6ms
[346.937s][info ][gc,phases   ] GC(271)   Other: 0.2ms
[346.937s][info ][gc,heap     ] GC(271) Eden regions: 426->0(426)
[346.937s][info ][gc,heap     ] GC(271) Survivor regions: 4->4(54)
[346.937s][info ][gc,heap     ] GC(271) Old regions: 170->170
[346.937s][info ][gc,heap     ] GC(271) Archive regions: 2->2
[346.937s][info ][gc,heap     ] GC(271) Humongous regions: 0->0
[346.937s][info ][gc,metaspace] GC(271) Metaspace: 59579K(60096K)->59579K(60096K) NonClass: 52231K(52480K)->52231K(52480K) Class: 7348K(7616K)->7348K(7616K)
[346.937s][info ][gc          ] GC(271) Pause Young (Normal) (G1 Evacuation Pause) 1200M->347M(1434M) 2.672ms
[346.937s][info ][gc,cpu      ] GC(271) User=0.01s Sys=0.00s Real=0.00s
2023-08-01T09:06:56.477+08:00  INFO 39312 --- [io-8000-exec-51] c.b.s.b.p.s.mock.UserServiceMockImpl     : UserServiceMockImpl mockOne cost = 14

```



user代表进程在用户态消耗的CPU时间，

sys代表代表进程在内核态消耗的CPU时间，

real代表程序从开始到结束所用的时钟时间。这个时间包括其他进程使用的时间片和进程阻塞的时间（比如等待 I/O 完成）。





## 内存分配

大对象直接进老年代 

https://blog.csdn.net/zhou920786312/article/details/123536294



## 初始过程 

**比如父类静态数据，构造函数，字段，子类静态数据，构造函数，字段，他们的执行顺序**

先静态、先父后子。

先静态：父静态 > 子静态

优先级：父类 > 子类 静态代码块 > 非静态代码块 > 构造函数



## SafePoint

https://zhuanlan.zhihu.com/p/286110609

### 安全点 

STW并不会只发生在内存回收的时候。现在程序员这么卷，碰到几次safepoint的问题几率也是比较大的。

当发生GC时，用户线程必须全部停下来，才可以进行垃圾回收，这个状态我们可以认为JVM是安全的（safe），整个堆的状态是稳定的。

如果在GC前，有线程迟迟进入不了safepoint，那么整个JVM都在等待这个阻塞的线程，造成了整体GC的时间变长。





- 现在可达性分析算法耗时最长的查找引用链的过程已经可以做到与用户线程一起并发，但根节点枚举始终还是必须在一个能保障一致性的快照中才得以进行

- 即使是号称停顿时间可控，或者（几乎）不会发生停顿的CMS、G1、 ZGC等收集器，枚举根节点时也是必须要停顿的



- 用户程序执行时并非在代码指令流的任意位置都能够停顿下来开始垃圾收集，而是强制要求必须执行到达安全点后才能够暂停。



### 选择标准

安全点位置的选择标准是：是否能让程序长时间执行；所以会在方法调用、循环跳转、异常跳转等处才会产生安全点

HotSpot会在所有方法的临返回之前，以及所有非counted loop的循环的回跳之前放置安全点。

“长时间执行”的最明显特征就是指令序列的复用，例如方法调用、循环跳转、异常跳转 等都属于指令序列复用，所以只有具有这些功能的指令才会产生安全点。





### 中断停顿

- 抢先式中断

“抢先式中断不需要线程的执行代码主动去配合，在GC发生时，首先把所有线程全部中断，如果发现有线程中断的地方不在安全点上，就”

“恢复线程，让它“跑”到安全点上。现在几乎没有虚拟机实现采用抢先式中断来暂停线程从而响应GC事件。

- 主动式中断

而主动式中断的思想是当GC需要中断线程的时候，不直接对线程操作，仅仅简单地设置一个标志，各个线程执行时主动去轮询这个标志，发现中断标志为真时就自己中断挂起。轮询标志的地方和安全点是重合的，另外再加上创建对象需要分配内存的地方。”



### 安全域

“使用Safepoint似乎已经完美地解决了如何进入GC的问题，但实际情况却并不一定。Safepoint机制保证了程序执行时，在不太长的时间内就会遇到可进入GC的Safepoint。但是，程序“不执行”的时候呢？所谓的程序不执行就是没有分配CPU时间，典型的例子就是线程处于Sleep状态或者Blocked状态，这时候线程无法响应JVM的中断请求，“走”到安全的地方去中断挂起，JVM也显然不太可能等待线程重新被分配CPU时间。对于这种情况，就需要安全区域（Safe Region）来解决。”





## MAT

参考链接 https://c.m.163.com/news/a/HL40JTCI0552ZNXL.html

入门 https://juejin.cn/post/6908665391136899079

进阶 https://juejin.cn/post/6911624328472133646

官网 https://help.eclipse.org/latest/index.jsp?topic=/org.eclipse.mat.ui.help/welcome.html





### 概况

Leak Suspects 》 System Overview

- Heap Dump Overview 
- System Properties
- Thread Overview
- Top Consumers
  - Biggest Objects (Overview)
  - Biggest Objects
  - Biggest Top-Level Dominator Classes (Overview)
  - Biggest Top-Level Dominator Classes
  - Biggest Top-Level Dominator Class Loaders (Overview)
  - Biggest Top-Level Dominator Class Loaders
  - Biggest Top-Level Dominator Packages
- Class Histogram



## 架构



## Bilibili 



## 参考链接 

JVM 知识汇总 https://juejin.cn/post/7303386068606812199

GC 解析 https://juejin.cn/post/7303414348932382774



jvm参数配置 https://www.oracle.com/java/technologies/javase/vmoptions-jsp.html

内容解析 https://www.zhihu.com/column/c_1268282634804400128

加载过程解析 https://my.oschina.net/u/4090830/blog/10141305



【itv】https://github.com/whx123/JavaHome/blob/master/Java%E9%9D%A2%E8%AF%95%E9%A2%98%E9%9B%86%E7%BB%93%E5%8F%B7/JVM%20%E7%AF%87/JVM%E9%9D%A2%E8%AF%95%E9%A2%98.md

【itv2】

https://github.com/rbmonster/learning-note/blob/master/src/main/java/com/toc/JVM.md



https://blog.csdn.net/zouliping123456/article/details/124522802

https://blog.csdn.net/weixin_59262008/article/details/125676787

https://zhuanlan.zhihu.com/p/375202547

https://www.cnblogs.com/hanease/p/15869727.html