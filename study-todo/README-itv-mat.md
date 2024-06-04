# study-MAT #
## 简介



找到最大的对象，因为MAT提供显示合理的累积大小（`retained size`）

探索对象图，包括`inbound`和`outbound`引用，即引用此对象的和此对象引出的。

查找无法回收的对象，可以计算从垃圾收集器根到相关对象的路径

找到内存浪费，比如冗余的String对象，空集合对象等等



1. 对象信息：类、成员变量、直接量以及引用值；
2. 类信息 类加载器 、 名称 、 超类 、 静态成员；
3. Garbage Collections Roots JVM 可达的对象；
4. 线程栈以及本地变量 获取快照时的线程栈信息 以及局部变量的详细信息 。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7a535f67b9a04f69bb9155ddd5d0a8b8~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

`Details`：显示了一些统计信息，包括HeapDump的大小、类（Class）的数量、对象（Object）的数量、类加载器（Class  Loader)的数量。

`Biggest Objects by Retained Size` ：以饼图的方式直观地显示了在dump中最大的几个对象，当鼠标光标移到某块区域的时候会在左边`Inspector`和`Attributes`窗口中显示详细的信息，在区块上点击左键可以通过菜单获取更详细的信息。

`Actions` ：几种常用到的操作，包括` Histogram、Dominator Tree、Top Consumers、Duplicate Classes`。

`Reports` ：列出了常用的报告信息，包括` Leak Suspects`和`Top Components`。

`Step By Step` ：以向导的方式逐步的引导使用功能，包括`Component Report`。



![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/5/3/171d90d577d85828~tplv-t2oaga2asx-jj-mark:3024:0:0:0:q75.awebp)





**Actions** 

| sdf               | sdf                                                          |
| ----------------- | ------------------------------------------------------------ |
| histogram         | 列举内存中对象存在的个数和大小                               |
| Dominator tree    | 该视图会以占用总内存的百分比来列举所有实例对象，注意这个地方是对象而不是类了，这个视图是用来发现大内存对象的 |
| Top Consumers     | 该视图会显示可能的内存泄漏点                                 |
| Duplicate Classes | 该视图显示重复的类等信息                                     |





## Key关键点

- 



## 内存计算依据

MAT 计算对象占据内存的两种方式。

第一种是 Shallow heap，指的是对象自身所占据的内存。

第二种是 Retained heap，指的是当对象不再被引用时，垃圾回收器所能回收的总内存，包括对象自身所占据的内存，以及仅能够通过该对象引用到的其他对象所占据的内存



## Histogram【直方图】

Histogram直方图，用于展示每个类型的实例的数量，以及 shallow size 和 retained size ：

1. `shallow size`：浅堆，代表了对象本身的内存占用，包括对象自身的内存占用，以及“为了引用”其他对象所占用的内存。
2. 非数组类型的对象的 shallow heap=对象头+各成员变量大小之和+对齐填充。其中，各成员变量大小之和就是实例数据，如果存在继承的情况，需要包括父类成员变量
3. 数组类型的对象的shallow heap=对象头+类型变量大小*数组长度+对齐填充，如果是引用类型，则是四字节或者八字节（64 位系统），如果是 boolean 类型，则是一个字节。这里类型变量大小*数组长度就是数组实例数据，强调是变量不实际是对象本身。
4. `Retained heap`：深堆，一个统计结果，会循环计算引用的具体对象所占用的内存。但是深堆和“对象大小”有一点不同，深堆指的是一个对象被垃圾回收后，能够释放的内存大小，这些被释放的对象集合，叫做保留集（Retained Set）。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0d411ca76d9e406c87adcff5ef12c76c~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)



### outgoing reference与incoming reference【引用关系】

我们之间来看 String 是被谁引用的，选中那一行，右键点击 List objects。

- with incoming references 表示的是当前查看的对象，被外部应用
- with outGoing references 表示的是当前对象，引用了外部对象

当右键单击任何对象时，将看到下拉菜单，如果选择“ListObjects”菜单项，可以查看对象的outgoing reference（对象的引出）和incoming reference（对象的引入）。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5a1bb054d903470b9568f37dd6bf5c69~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

Incomming Reference 指的是引用当前对象的对象，Outgoing Reference 指的是当前对象引用的对象。对象的incomming reference 保证对象处于 alive 从而免于被垃圾回收掉 ；Outgoing reference 则展示了对象内部的具体内容，有助于我们分析对象的属性 。



## dominator tree【支配树】

支配树的概念源自图论。

- 对象X `Dominator`（支配）对象Y，当且仅当在对象树中所有到达Y的路径都必须经过X
- 对象Y的直接`Dominator`，是指在对象树中距离Y最近的`Dominator`
- `Dominator tree`利用对象树构建出来。在`Dominator tree`中每一个对象都是他的直接`Dominator`的子节点。

对象树和`Dominator tree`的对应关系如下:



![](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2019/8/29/16cdd6c7f512cfc8~tplv-t2oaga2asx-jj-mark:3024:0:0:0:q75.png)

如上图，因为A和B都引用到C，所以A释放时，C内存不会被释放。所以这块内存不会被计算到A或者B的Retained Heap中，因此，对象树在转换成`Dominator tree`时，会A、B、C三个是平级的。





在一则流图（flow diagram）中，如果从入口节点到 b 节点的所有路径都要经过 a 节点，那么 a 支配（dominate）b。在 a 支配 b，且 a 不同于 b 的情况下（即 a 严格支配 b），如果从 a 节点到 b 节点的所有路径中不存在支配 b 的其他节点，那么 a 直接支配（immediate dominate）b。这里的支配树指的便是由节点的直接支配节点所组成的树状结构。

列出Heap Dump中处于活跃状态中的最大的几个对象，默认按 retained size进行排序，因此很容易找到占用内存最多的对象。

排在第一的最大的对象就是占用内存最多的对象，它在树中的子节点都是被该对象直接或间接引用的对象（这意味着当这个对象被回收的时候它的子节点对象也会被回收）。

一般定位OOM的时候，都是直接查看支配树的最大的对象，我们的Heap Dump中的支配树中，很明显ArrayList占用了最大的内存，里面的元素就是一个个拼接的UUID字符串，就是因为这个原因导致了OOM。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ee83c59fb5dc4f009cabdb97b93371bc~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)



## unreachable【不可达对象】

另外，就是MAT好像默认是自动过滤unreachable对象的。也就是说，即便是你在用jamp dump的时候没有加上live参数，等你用MAT打开dump文件的时候，那些对象也会被MAT过滤掉。在Settings里打开开关就好。



## Thread Overview【线程视图】

点击上面的黄色齿轮，可以生成Heap Dump文件的时候线程视图Thread Overview，查看线程的运行情况，抛出的异常的分析。

![在这里插入图片描述](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8dc99c653b7b4d4fb78442a85c57d2e2~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

我们的Heap Dump中的Thread Overview，结果如下，很明显OutOfMemoryError是由main线程抛出的：

![在这里插入图片描述](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/23b59bd42acf4105b68bd1d3d3bf0be8~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)





## GC root

在执行GC时，是通过对象可达性来判断是否回收对象的，一个对象是否可达，也就是看这个对象的引用连是否和`GC Root`相连。一个`GC root`指的是可以从堆外部访问的对象，有以下原因可以使一个对象成为`GC root`对象。

- `System Class`: 通过bootstrap/system类加载器加载的类，如rt.jar中的java.util.*
- `JNI Local`: JNI方法中的变量或者方法形参
- `JNI Global`：JNI方法中的全局变量
- `Thread Block`：线程里面的变量，一个活着的线程里面的对象肯定不能被回收
- `Thread`：处于激活状态的线程
- `Busy Monitor`：调用了wait()、notify()方法，或者是同步对象，例如调用synchronized(Object) 或者进入一个synchronized方法后的当前对象
- `Java Local`：本地变量，例如方法的输入参数或者是方法内部创建的仍在线程堆栈里面的对象
- `Native Stack`：Java 方法中的变量或者方法形参.
- `Finalizable`：等待运行finalizer的对象
- `Unfinalized`：有finalize方法，但未进行finalized，且不在finalizer队列的对象。
- `Unreachable`：通过其它root都不可达的对象，MAT会把它标记为root以便于分析回收。
- `Java Stack Frame`：java栈帧
- `Unknown`





## 参考链接

官方网址：https://help.eclipse.org/latest/index.jsp?topic=/org.eclipse.mat.ui.help/welcome.html

稀土掘金：

https://juejin.cn/post/6844903927528292365

https://juejin.cn/post/7018806258392104973

https://juejin.cn/post/7030664794843660302

https://juejin.cn/post/6857697471964020749

https://juejin.cn/post/7081255851620564999

入门 https://juejin.cn/post/6908665391136899079

进阶 https://juejin.cn/post/6911624328472133646