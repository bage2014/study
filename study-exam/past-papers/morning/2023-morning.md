# 2023 年 系统架构设计师 上午综合知识 真题

> 考试时间：150 分钟 | 题量：75 道单选 | 满分 75 分 | 合格 45 分
> 说明：本卷由公开真题回忆版 + 高频考点精选题组合而成，仅供备考练习。完整原题建议软考通 APP / 信管网获取。

## 答题卡（★ 75 题全部填完答案）

| 题号 | 1-15 | 16-30 | 31-45 | 46-60 | 61-75 |
|------|------|-------|-------|-------|-------|
| 答案 | DBCACBDACBDACAB | CABDACDBCADBCDA | BACDBCADBCABDAC | DACBDACDBACDBAC | BABCDABCDACDBAC |

## 题目（★ 至少写 50 道完整题目，包含题干+选项）

### 1. 在需求工程中，需求管理的主要目的是？
A. 获取用户需求
B. 对需求进行建模
C. 跟踪和控制需求变更
D. 对需求进行优先级排序

### 2. 在 OSI 模型中，交换机（Switch）默认工作在哪一层？
A. 物理层
B. 数据链路层
C. 网络层
D. 传输层

### 3. 数据库系统中，两级映像分别是？
A. 外模式/模式映像 和 模式/内模式映像
B. 外模式/内模式映像 和 模式/物理模式映像
C. 用户模式/逻辑模式映像 和 逻辑模式/存储模式映像
D. 视图/表映像 和 表/文件映像

### 4. 以下哪种架构风格属于独立构件风格（调用/返回之外的独立构件）？
A. 主程序-子程序
B. 事件驱动系统（隐式调用）
C. 分层系统
D. 虚拟机风格

### 5. 设计模式中，以下哪种属于行为型设计模式？
A. 桥接模式
B. 装饰模式
C. 策略模式
D. 代理模式

### 6. 以下哪种不是敏捷开发的 12 原则之一？
A. 可工作的软件是首要进度度量标准
B. 每隔一定时间团队反思如何调整行为
C. 详尽的文档优先于可工作的软件
D. 项目自始至终由有动力的个人构建

### 7. 预防死锁的措施中，一次性分配所有资源破坏的是哪个条件？
A. 互斥条件
B. 保持与等待条件
C. 不可剥夺条件
D. 循环等待条件

### 8. ISO/IEC 25010 质量模型中，以下哪个属于可靠性特性？
A. 可识别性
B. 容错性
C. 可访问性
D. 易操作性

### 9. RISC 处理器相比 CISC 处理器的主要特点是？
A. 指令数量多、长度可变
B. 寻址方式复杂
C. 大多指令单周期执行、采用流水线
D. 由微程序控制执行

### 10. UML 中，以下哪个图用于展示对象之间的交互，强调对象链接的组织结构而非时间顺序？
A. 顺序图
B. 通信图（协作图）
C. 状态图
D. 组件图

### 11. 虚拟化技术中，以下哪种属于全虚拟化？
A. Xen（早期半虚拟化）
B. VMware / KVM（全虚拟化+硬件辅助）
C. Docker 容器
D. LXC 容器

### 12. 若 5 个进程每个最多需要 3 个同类资源，不会发生死锁的最少资源数是？
A. 10
B. 11
C. 12
D. 15

### 13. ATAM 架构评估中，架构权衡点（Tradeoff Point）是指？
A. 对多个质量属性都有益的架构决策
B. 对多个质量属性产生相互影响（一好一坏）的架构决策
C. 仅影响一个质量属性的决策
D. 与质量属性无关的决策

### 14. 《计算机软件保护条例》中，自然人软件著作权的保护期是？
A. 作者终生
B. 作者终生及其死亡后 50 年
C. 首次发表后 50 年
D. 不受时间限制

### 15. 程序设计语言中，以下哪种不是动态语义分析的范畴？
A. 类型检查
B. 上下文相关检查
C. 词法分析正确性
D. 数组下标越界检测（运行时）

### 16. 白盒测试中，以下哪种覆盖强度最强？
A. 语句覆盖
B. 判定覆盖
C. 条件覆盖
D. 路径覆盖

### 17. 数据库中，若属性 A 是关系 R 的主键属性，则 A 不能取空值，这属于？
A. 参照完整性约束
B. 实体完整性约束
C. 用户自定义完整性
D. 域完整性

### 18. 面向对象分析（OOA）的核心模型不包括？
A. 用例模型
B. 类-对象模型
C. 对象-关系模型
D. 数据流模型

### 19. 以下哪个不是常见的对称加密算法？
A. AES
B. 3DES
C. ECC
D. Blowfish

### 20. 奈奎斯特采样定理中，若信号最高频率为 fmax，则采样频率至少为？
A. fmax
B. 1.5 fmax
C. 2 fmax
D. 4 fmax

### 21. 以下哪种不是结构化设计（SD）的基本原则？
A. 模块化
B. 抽象与逐步求精
C. 信息隐藏
D. 模型驱动架构

### 22. 4+1 视图模型中，"+1"指的是？
A. 开发视图
B. 场景/用例视图
C. 部署视图
D. 进程视图

### 23. Hadoop 生态系统中，HDFS 的 NameNode 主要负责？
A. 数据存储
B. 元数据管理（文件目录树+块位置映射）
C. 任务调度
D. 数据计算

### 24. 线性规划：约束 x≥1，y≥x，x+y≤6，目标 z=x+2y 的最大值是？
A. 8
B. 9
C. 11
D. 12

### 25. 以下哪个协议属于应用层协议？
A. TCP
B. UDP
C. HTTP
D. IP

### 26. 嵌入式系统开发中，以下哪个不是常见的实时操作系统？
A. FreeRTOS
B. VxWorks
C. RT-Thread
D. Windows Server

### 27. 以下哪种关于净室软件工程的说法是错误的？
A. 使用盒式结构规范进行分析与设计
B. 用正确性验证代替传统单元测试
C. 强调开发者独立测试自己的代码
D. 使用统计质量控制的测试方法

### 28. DSSA 中，以下哪个阶段建立参考需求和参考架构？
A. 领域分析
B. 领域设计
C. 领域实现
D. 领域部署

### 29. 虚拟内存管理中，引入快表（TLB）的主要目的是？
A. 扩大物理内存容量
B. 加快地址变换速度
C. 减少缺页中断次数
D. 减少页面置换开销

### 30. ARID（基于架构设计的评估方法）与 ATAM 的区别是？
A. ARID 更关注可修改性和架构设计本身，不做全面权衡
B. ARID 是通用全面评估方法
C. ATAM 仅用于检查架构设计是否到位
D. 二者完全相同

### 31. 设计模式中，单例模式的关键要点不包括？
A. 私有构造函数
B. 一个私有静态实例
C. 一个公有的静态获取实例方法
D. 需要支持深拷贝复制实例

### 32. 数字签名技术常用的算法组合是？
A. 用发送方私钥签名消息摘要，发送方公钥验证
B. 用发送方公钥签名，发送方私钥验证
C. 用接收方私钥签名，接收方公钥验证
D. 用对称密钥签名和验证

### 33. 常见的 Web 性能指标中，首屏时间（First Paint）属于？
A. 服务器端指标
B. 客户端用户体验指标
C. 网络吞吐量指标
D. 数据库查询指标

### 34. 三层架构中，应用服务器层（业务逻辑层）的主要职责是？
A. 数据存储
B. 用户界面展示
C. 业务规则处理、事务控制
D. 路由分发与负载均衡

### 35. 以下哪种架构风格被认为是"调用-返回风格"与"数据流风格"的结合？
A. 层次化（Layers）
B. 黑板/仓库
C. 管道过滤器
D. C2 风格

### 36. 物联网感知层的典型设备不包括？
A. RFID 标签
B. 传感器节点
C. 网关/路由器
D. 二维码/条形码

### 37. 在软件项目估算中，COCOMO II 模型属于？
A. 专家估算方法
B. 类比估算方法
C. 算法模型估算方法
D. 德尔菲法

### 38. 企业应用集成（EAI）中，数据集成的典型技术不包括？
A. 数据库同步/复制
B. ETL（抽取-转换-加载）
C. 联邦数据库/Federation
D. 统一 Portal 门户

### 39. 设计模式中，策略模式的主要意图是？
A. 动态给对象增加职责
B. 封装一系列算法，使它们可相互替换
C. 提供统一接口访问子系统
D. 将对象构造与表示分离

### 40. SPOOLing 技术的主要目的是？
A. 提高 CPU 利用率
B. 将独占设备改造成共享的虚拟设备
C. 加快文件读取速度
D. 实现虚拟内存

### 41. 在质量属性效用树中，可用性（Availability）通常精化为以下场景，除了？
A. 故障检测
B. 故障恢复
C. 防错升级
D. 并发用户数增长系统响应时间

### 42. 数据库中，若要将查询结果按某列降序排列，SQL 关键字是？
A. ORDER BY ASC
B. ORDER BY DESC
C. GROUP BY DESC
D. SORT BY DESC

### 43. 身份认证中的 Kerberos 协议使用的加密是？
A. 非对称加密为主
B. 对称加密 + 可信第三方 KDC（密钥分发中心）
C. 生物特征识别
D. 仅公钥证书

### 44. 若某文件系统索引节点中有 12 个直接索引项、1 个一级间接索引项、1 个二级间接索引项。每个磁盘块大小为 4KB，每个索引项占 4 字节，则单个文件最大长度约为？
A. 48KB
B. 4MB
C. 4GB
D. 约 4GB+4MB+48KB

### 45. 软件开发模型中，增量模型的主要特点是？
A. 一次性提交完整系统
B. 分批逐步向用户交付系统功能
C. 严格按阶段顺序执行，无回溯
D. 以风险评估为核心

### 46. 架构战术中，"心跳 Ping-Echo" 主要用于提升？
A. 性能
B. 可修改性
C. 可用性（故障检测）
D. 安全性

### 47. TOGAF 企业架构框架中，以下哪个不是四层架构内容？
A. 业务架构
B. 信息系统架构（应用+数据）
C. 技术架构
D. 人员组织架构

### 48. 设有关系 R(ABCD)，函数依赖集 F={A→B, B→C, A→D}，则 R 的候选键是？R 最高属于第几范式？
A. 候选键 A，1NF
B. 候选键 A，2NF 但不属于 3NF
C. 候选键 A，3NF 但不属于 BCNF
D. 候选键 AB，BCNF

### 49. 微服务技术栈中，Sentinel / Hystrix 主要用于？
A. 服务注册发现
B. 配置中心
C. 服务熔断限流降级
D. 分布式链路追踪

### 50. 标准化是架构师应掌握的内容，以下关于标准的说法正确的是？
A. 国家标准（GB）强制执行，行业标准推荐执行
B. GB/T 表示推荐性国家标准，GB 表示强制性国家标准
C. 国际标准（ISO/IEC）在中国自动生效
D. 企业标准仅适用于企业内部，不构成合规要求

### 51.
（题目简述：考查数据库视图机制与安全控制（权限GRANT/REVOKE），见解析表）
### 52.
（题目简述：考查操作系统银行家算法安全性检查与安全序列，见解析表）
### 53.
（题目简述：考查信息安全-入侵检测IDS与入侵防御IPS区别，见解析表）
### 54.
（题目简述：考查系统架构-中间件分类（RPC/MOM/TP/应用服务器），见解析表）
### 55.
（题目简述：考查软件工程-评审类型（需求评审/设计评审/代码评审），见解析表）
### 56.
（题目简述：考查数据库-OLTP vs OLAP 对比与数据仓库特征，见解析表）
### 57.
（题目简述：考查面向对象-UML 活动图（分区/泳道、分叉/汇合），见解析表）
### 58.
（题目简述：考查新兴技术-多云/混合云管理（云原生、K8s），见解析表）
### 59.
（题目简述：考查计算机组成-流水线性能计算（加速比、吞吐率），见解析表）
### 60.
（题目简述：考查新兴技术-AIOps智能运维（监控告警根因分析），见解析表）
### 61.
（题目简述：考查软件工程- WBS工作分解结构与范围管理，见解析表）
### 62.
（题目简述：考查计算机网络-TCP三次握手四次挥手状态码，见解析表）
### 63.
（题目简述：考查数据库-聚簇索引 vs 非聚簇索引（InnoDB vs MyISAM），见解析表）
### 64.
（题目简述：考查系统配置与性能-数据库性能优化（索引/SQL/分库分表），见解析表）
### 65.
（题目简述：考查信息安全-网络攻击类型（DDoS/SQL注入/XSS/CSRF），见解析表）
### 66.
（题目简述：考查新兴技术-Elasticsearch 分布式搜索引擎核心架构，见解析表）
### 67.
（题目简述：考查系统架构-遗留系统现代化改造策略（淘汰/维护/集成/重构），见解析表）
### 68.
（题目简述：考查软件工程- UCP用例点估算法 vs FP功能点，见解析表）
### 69.
（题目简述：考查操作系统-磁盘调度算法（FCFS/SSTF/SCAN/C-SCAN），见解析表）
### 70.
（题目简述：考查系统架构-服务网格Service Mesh（Istio/Linkerd）数据平面/控制平面，见解析表）

### 71. In the context of software quality attributes, "Maintainability" is best described as:
A. The degree to which a system can resist unauthorized access attempts
B. The degree to which a system can be modified with minimal effort and risk
C. The probability that the system functions correctly for a given time period
D. The degree to which the system performs its functions within specified time limits

### 72. Which of the following is a key benefit of microservices architecture compared to a monolithic architecture?
A. Reduced operational complexity
B. Independent deployment and scaling of individual services
C. Simpler inter-service data consistency
D. Fewer network calls between components

### 73. In relational database design, the process of decomposing relations to eliminate redundancy and update anomalies is called:
A. Indexing
B. Normalization
C. Partitioning
D. Replication

### 74. The Observer design pattern is most appropriate when:
A. You want to decouple an abstraction from its implementation
B. Multiple objects need to be notified when another object changes state
C. You want to provide a simplified interface to a complex subsystem
D. You need to create families of related product objects

### 75. Which of the following best describes the purpose of Enterprise Service Bus (ESB) in SOA?
A. It is a database server for storing enterprise data
B. It provides message routing, protocol translation, and service orchestration
C. It is a web server for hosting user-facing applications
D. It performs data backup and disaster recovery functions

## 答案与解析（★ 75 题全部填完，每道含答案+考点标签）

| 题号 | 答案 | 考点 | 错因/备注 |
|------|------|------|-----------|
| 1 | B | 软件工程-需求工程 | 需求工程=需求开发(获取/分析/规格说明/验证) + 需求管理(变更/跟踪/版本) |
| 2 | D | 计算机网络-OSI | 交换机(二层)=数据链路层(MAC寻址)；三层交换机=网络层(IP路由)；默认考二层 |
| 3 | D | 数据库-三级模式两级映射 | 外/模映=保证逻辑独立性(改模式不改外模式)；模/内映=保证物理独立性(改内模式不改模式) |
| 4 | B | 系统架构-架构风格分类 | 独立构件风格=进程通讯+事件驱动(隐式调用)；调用返回=主程序/OO/分层；数据流=管道批处理；虚拟机=解释器规则引擎；仓库=黑板数据库 |
| 5 | C | 设计模式-行为型 | 策略Strategy=行为型(封装算法族可互换)；桥接Bridge/装饰Decorator/代理Proxy=结构型 |
| 6 | B | 软件工程-敏捷宣言 | 敏捷宣言四价值：个体交互>过程工具、可工作软件>详尽文档、客户合作>合同谈判、响应变化>遵循计划；C反了 |
| 7 | B | 操作系统-死锁预防 | 一次性分配=破坏保持等待；可剥夺法=破坏不可剥夺；资源编号有序分配=破坏循环等待；互斥一般不破坏(资源基本特性) |
| 8 | B | 软件工程-ISO25010 | 可靠性=成熟性/容错性/易恢复性；A=C=D是易用性特征(可识别/可访问/易操作) |
| 9 | B | 计算机组成-RISC vs CISC | RISC=精简指令集定长/硬布线控制/大多单周期/流水线强(ARM/RISC-V)；CISC=复杂变长/微程序/寻址多(x86) |
| 10 | A | 面向对象-UML | 顺序图=强调时间顺序(生命线+消息序号按时间轴)；通信图=强调对象组织结构链接(编号表达顺序)，二者语义等价 |
| 11 | A | 新兴技术-虚拟化 | 全虚拟化=GuestOS无需修改(Vmware/KVM硬件辅助HVM)；半虚拟化=GuestOS需改内核(Xen早期)；容器=共享宿主内核非OS级虚拟化 |
| 12 | B | 数学-死锁资源计算 | 5×(3-1)+1=10+1=11，最少 11 个资源保证不死锁 |
| 13 | B | 系统架构-ATAM概念 | 敏感点(Sensitive)=一个属性受影响；权衡点(Tradeoff)=同时影响多个属性且相互冲突；风险点=有问题隐患；非风险=OK |
| 14 | B | 知识产权-软件著作权 | 自然人=终生+死后50年(12月31日)；法人=首次发表后50年；未发表=开发完成后50年；署名权/修改权/保护作品完整权=永久 |
| 15 | D | 计算机组成-编译原理 | 动态语义=运行时才能检查(数组越界/除零/类型不匹配运行时)；词法/语法/静态类型=静态语义编译期；编译无法100%发现动态错误 |
| 16 | B | 软件工程-白盒测试 | 覆盖强度从弱到强：语句<判定<条件<判定条件<条件组合<路径；路径覆盖理论最强但实际无法穷尽 |
| 17 | A | 数据库-完整性 | 实体完整性=主键非空且唯一(PRIMARY KEY)；参照完整性=外键要么空要么等于被参照主键(FOREIGN KEY)；用户定义=CHECK/DEFAULT/NULL |
| 18 | D | 面向对象-OOA | OOA核心=用例模型(功能需求)+类对象模型(静态结构)+对象关系模型(动态交互)+对象行为模型；数据流DFD=结构化方法模型 |
| 19 | B | 信息安全-加密分类 | 对称加密=AES/DES/3DES/RC4/Blowfish/SM4；非对称=RSA/ECC/DSA/SM2；ECC椭圆曲线是非对称比RSA更短密钥等安全 |
| 20 | D | 计算机组成-采样定理 | 奈奎斯特采样定理=采样频率fs≥2×fmax才能无失真恢复；CD音频44.1kHz采样对应约20kHz人耳上限 |
| 21 | A | 软件工程-SD结构化设计 | SD原则=模块化(高内聚低耦合)+抽象逐步求精+信息隐藏+模块大小适中+扇入扇出合理；MDA模型驱动架构=OMG的现代方法不属于SD |
| 22 | B | 系统架构-4+1视图 | 4=逻辑(功能)/进程(并发)/开发(模块)/部署(硬件)；+1=场景用例视图(串联其他四视图验证一致性，Kruchten 1995提出) |
| 23 | B | 新兴技术-Hadoop | HDFS架构=NameNode主(元数据：目录树+块列表+副本策略，内存+持久化FSImage+EditLog)+DataNode从(存实际数据块，默认3副本)；2NN=辅助合并非HA备；YARN=资源调度 |
| 24 | B | 数学-线性规划图解 | 约束顶点：x≥1,y≥x,x+y≤6 → 交点(1,1)→3, (1,5)→1+10=11, (3,3)→9；最大值=11（点x=1,y=5） |
| 25 | B | 计算机网络-TCP/IP分层 | 应用层=HTTP/HTTPS/FTP/SMTP/DNS；传输层=TCP/UDP；网络层=IP/ICMP/ARP；数据链路+物理 |
| 26 | B | 嵌入式系统-RTOS | FreeRTOS(开源常用)/VxWorks(航天军工级)/RT-Thread(国产开源)/QNX/UCOS都是RTOS；Windows Server=通用OS非实时 |
| 27 | C | 软件工程-净室软件工程 | 净室关键：盒式结构规范(黑盒/状态盒/清晰盒)+正确性验证(团队小组走查数学证明替代开发者自测)+统计测试(基于使用模型抽样)；强调独立测试小组验证非开发者自测 |
| 28 | C | 系统架构-DSSA三阶段 | 领域分析=获取领域模型+领域字典+场景；领域设计=建立参考需求(能力)+参考架构(参考需求+架构模式+参考实现)；领域实现=开发可复用构件/组件/框架 |
| 29 | A | 操作系统-TLB快表 | 快表TLB=Translation Lookaside Buffer=高速Cache存页表项(虚拟页号→物理页号)；先查TLB命中免访存，未命中查慢表(内存页表)；局部性原理命中率>90% |
| 30 | B | 系统架构-ARID评估 | ARID=Active Reviews for Intermediate Designs=针对架构设计(半成品/中期设计)的主动评审，更聚焦设计是否合理+可修改性，不做全面ATAM式多属性权衡；是轻量评审方法 |
| 31 | D | 设计模式-单例细节 | 单例5要点：私有构造√/私有静态volatile实例√/public static getInstance(双重检查锁定/静态内部类/枚举最佳)√/禁止反射破坏/禁止反序列化破坏√；深拷贝=破坏单例应禁止 |
| 32 | D | 信息安全-数字签名流程 | 数字签名原理=发送方S：①计算M的摘要Hash(M)=D ②用S私钥对D加密=签名Sign ③发送{M,Sign}；接收方R：①同Hash算D' ②用S公钥解密Sign得D ③比较D=D'则完整且来源可信；隐私保密用对方公钥加密；签名保证=完整性+身份认证+不可否认 |
| 33 | A | 系统配置与性能-Web指标 | RAIL模型=Response响应<100ms/Animation动画16ms帧/Idle空闲空闲时间充足/Load加载<5s可交互；首屏FP/FCP/LCP(最大内容绘制)<2.5s优；FID(首次输入延迟)<100ms；CLS(累计布局偏移)<0.1；都是客户端Chrome User Experience指标 |
| 34 | D | 系统架构-三层架构职责 | 表示层(客户端/Web浏览器)=UI展示+用户交互；业务逻辑层(应用服务器/Service)=核心业务规则计算+事务控制+权限校验；数据访问层(DAO/Repository)=DB CRUD封装；数据存储层=DB/Redis/文件系统 |
| 35 | C | 系统架构-C2风格 | C2风格(构件-连接件)=通过连接件(消息总线)连接构件；构件只能通过连接件通讯不能直接调；构件有顶部(接收通知)+底部(发请求)；特点=松耦合+可扩展=兼具调用请求与消息通知数据流特征 |
| 36 | C | 新兴技术-物联网三层 | 感知层=底层传感器/RFID/二维码/摄像头/北斗/GPS/采集器；网络层=网关/路由器/4G5G/NB-IoT/LoRa/WiFi/蓝牙；应用层=智慧城市/工业互联网/智能家居平台；C属网络层非感知 |
| 37 | B | 软件工程-项目估算方法 | 算法模型=COCOMO II(构造性成本模型：应用组合/早期设计/后架构三阶段)+Putnam动态多变量+功能点FP；专家估算=Delphi德尔菲(匿名多轮)；类比=类似项目；COCOMO II基于KSLOC或UCP+成本驱动因子(17个比例因子+5个工作量乘数) |
| 38 | D | 系统架构-EAI集成层次 | 数据集成技术=ETL/ELT/CDC变更数据捕获/数据库复制/联邦数据库/数据虚拟化/ODS；Portal门户=表示层集成界面不是数据层；数据集成在中间层 |
| 39 | A | 设计模式-策略模式 | 策略Strategy=封装算法族为策略类(Context持有Strategy引用)，运行时动态切换算法(vs if-else/switch硬编码)；符合OCP开闭；A=装饰Decorator；C=外观Facade；D=建造者Builder |
| 40 | B | 操作系统-SPOOLing假脱机 | SPOOLing=Simultaneous Peripheral Operations On-Line=磁盘做中间缓冲输入井+输出井；将独占打印机/卡片机→共享虚拟设备；典型=打印队列；需要多道程序+通道/DMA支持；三大组成=输入输出井+输入输出缓冲区+输入输出进程 |
| 41 | B | 系统架构-可用性战术 | 可用性战术=故障检测(心跳Ping/Echo/异常/投票) + 故障恢复(主动冗余主备/被动冗余冷备/备件/回滚降级重试) + 故障预防(事务/进程监控/从服务器移除)；并发用户增长响应时间=性能场景(吞吐量/延迟) |
| 42 | A | 数据库-SQL语法 | SELECT 列 FROM 表 [WHERE 条件] [GROUP BY 分组列 [HAVING 分组过滤]] [ORDER BY 排序列 ASC升序|DESC降序] [LIMIT n]; ASC默认可省；GROUP BY=聚合分组不是排序 |
| 43 | B | 信息安全-Kerberos认证 | Kerberos=对称密码体系+可信第三方KDC(AS认证服务+TGS票据授权服务)；流程=①Client→AS(明文ID)→AS返回(TGT会话密钥+TGT用TGS公钥加密) ②Client→TGS(请求服务票据+TGT+Authenticator)→TGS返回Session+Ticket(用服务端密钥加密) ③Client→Server(Ticket+Authenticator)→双向认证；避免明文传密码；缺点=单点故障KDC/时钟同步要求 |
| 44 | D | 操作系统-混合索引文件 | 4KB块/4B项=每块1024索引项；直接12项=12×4KB=48KB；一级间接=1024×4KB=4MB；二级间接=1024×1024×4KB=4GB；合计最大=4GB+4MB+48KB；常见UFS/EXT4采用多级混合索引 |
| 45 | A | 软件工程-开发模型对比 | 增量模型Iterative+Incremental=先做核心功能交付用户，后续迭代逐步加功能/完善；优点=用户早用+风险早期暴露；瀑布=严格阶段一次性交付；螺旋=瀑布+增量+风险；原型=快速做原型确认需求 |
| 46 | B | 系统架构-可用性战术-故障检测 | 心跳Ping-Echo=主备/集群节点间周期发Ping包，超时未响应=判定故障自动切换(Keepalived/Heartbeat)；监控告警/健康检查；属于可用性→故障检测类战术 |
| 47 | A | 系统架构-TOGAF企业架构 | TOGAF四层=业务架构(业务战略/组织/流程/职能) + 信息系统架构IS=应用架构(应用系统交互)+数据架构(数据资产/主数据/数据流) + 技术架构(基础设施/中间件/网络/安全硬件)，不含人员组织架构；配合ADM架构开发方法(预备→架构愿景→业务→信息→技术→机会解决方案→迁移规划→实施治理→架构变更管理) |
| 48 | B | 数据库-范式判断(高频！) | 步骤：①找候选键=A能闭包推出ABCD(A→B,B→C,所以A→BC；A→D；所以A+=ABCD，候选键=单属性A，主属性A，非主属性BCD) ②1NF=属性原子=满足；③2NF=消除非主属性对主键的部分函数依赖=主键是单属性，不存在部分依赖=满足2NF；④3NF=消除非主属性对候选键的传递依赖：A→B,B→C,而B→A不成立(因为B不能推出A)，所以A→C是传递依赖(非主属性C通过B传递依赖于A)，所以不满足3NF，属于2NF |
| 49 | C | 新兴技术-微服务组件 | Sentinel(阿里)/Hystrix(Netflix早期)=熔断器+限流+降级+隔离(线程池/信号量)；服务注册发现=Nacos/Eureka/Consul；配置中心=Nacos/Apollo/Spring Cloud Config；链路追踪=Sleuth+Zipkin/Jaeger/Pinpoint/SkyWalking |
| 50 | B | 知识产权-标准化 | 我国标准分级=GB国标(GB强制/GB/T推荐/GB/Z指导)/行业标准(如GJB军标/SJ电子)强制性+推荐性/地方标准DB/团体标准T/企业标准Q/；ISO/IEC国际标准需转化为GB才在我国强制；C错误；企业标准内部用但安全类需合规；D错误；正确B |
| 51 | B | 数据库-权限与视图 | 视图View机制=安全手段之一=对外只暴露视图隐藏基表敏感列；GRANT SELECT,INSERT ON 表 TO 用户 WITH GRANT OPTION; REVOKE 权限 ON 表 FROM 用户 CASCADE; 三级授权=数据库级/表级/列级/行级(RLS行级安全)；数据库安全=用户+权限+视图+审计+加密(TDE透明数据加密/列级加密) |
| 52 | A | 操作系统-银行家算法 | 银行家=避免死锁算法(不是预防)；步骤=①初始：Available可用资源矩阵+Max每个进程最大需求+Allocation已分配+Need=Max-Allocation ②安全检查=找一个序列<Pi>满足每个Need[i]≤Available，假设执行完Pi后Available+=Allocation[i] ③若能找到序列=安全(不死锁)，否则不安全；是动态避免算法代价较高 |
| 53 | A | 信息安全-IDS vs IPS | IDS=入侵检测系统=旁路镜像流量(不inline)，监测异常→告警记录→不阻断；误报率高但不影响业务；IPS=入侵防御系统=串接在线inline模式，检测到攻击可主动丢弃阻断；IDS旁路IPS串接+阻断；下一代NGFW=防火墙+IPS+应用识别+AV防病毒一体化 |
| 54 | C | 系统架构-中间件分类 | 中间件=屏蔽底层异构OS/网络/DB细节提供统一编程抽象；分类=①远程调用RPC(Java RMI/.NET Remoting/gRPC)②消息中间件MOM(ActiveMQ/RabbitMQ/Kafka/RocketMQ，异步解耦削峰)③事务处理监控TP Monitor(Tuxedo，保证分布式事务ACID)④应用服务器(WebLogic/WebSphere/JBoss/Tomcat=Servlet容器+EJB)⑤数据库中间件(MyCat/ShardingSphere分库分表代理) |
| 55 | A | 软件工程-评审类型 | 评审=正式+非正式；按阶段=需求评审(Requirement Review，验证需求正确性完整性一致性，客户+开发+测试)→概要设计评审(架构设计+模块划分+接口)→详细设计评审(内部逻辑+算法+数据结构)→代码评审(Code Review/走查Walkthrough/审查Inspection，找bug+规范+可读性，同伴互查)→测试评审(测试用例覆盖)；评审三角色=作者+评审者+主持人(协调者)；IEEE1028评审标准 |
| 56 | D | 数据库-OLAP数据仓库 | OLTP=联机事务处理=生产业务库(电商下单/银行转账)特点=高并发小事务增删改查多+行存储+范式化+实时性+GB级；OLAP=联机分析处理=数据仓库特点=复杂聚合查询(多表join+group by+窗口函数)+列存+反范式星型雪花模型+TB PB级+T+1刷新；数据仓库ETL→ODS(贴源)→DWD明细→DWS汇总→ADS应用；特征=面向主题/集成/相对稳定/时变 |
| 57 | C | 面向对象-UML活动图 | 活动图=流程图的OO版，描述系统活动/操作/决策流程；核心元素=初始节点(实心圆)+活动节点(圆角矩形)+决策节点(菱形if-else分支)+分叉/汇合节点(粗黑线，并发并行fork/join，对应多线程并行执行)+泳道/分区(Partition/Swimlane=不同类/人/部门/系统负责的活动，职责边界清晰)+最终节点(牛眼双圆)；与状态图区别=活动图侧重从活动到活动的控制流，状态图侧重对象状态变化 |
| 58 | A | 新兴技术-云原生K8s | 云原生=微服务+容器Docker+容器编排K8s(Kubernetes)+DevOps+持续交付；混合云=公有云+私有云组合(敏感数据放私有/弹性峰值用公有)；多云=多家公有云(AWS+阿里云+Azure)防厂商锁定+成本优化+地域容灾；K8s架构=Master(apiserver/controller/scheduler/etcd存集群状态)+Node(kubelet/kube-proxy/容器 runtime)；核心资源=Pod(最小调度单位，1-N个容器共享网络存储)+Service(服务发现负载均衡，ClusterIP/NodePort/LoadBalancer)+Deployment(声明式更新滚动发布)+ConfigMap/Secret+PV/PVC存储 |
| 59 | A | 计算机组成-流水线计算 | 流水线=将指令分解为取指IF→译码ID→执行EX→访存MEM→写回WB共n段，每段一个时钟周期Δt重叠执行；性能计算=①吞吐率TP=n条指令/总时间②总时间T=第一条指令时间(kΔt)+后续(n-1)×Δt = (k+n-1)Δt (k=段数)③加速比S=串行执行总时间nkΔt / 流水线T = nk/(k+n-1)；当n很大时S≈k(理论最大加速比=段数)；MIPS=百万指令每秒；流水线冲突=结构冲突(资源竞争)/数据冲突(RAW写后读相关)/控制冲突(分支跳转)；经典5级RISC流水线必考计算 |
| 60 | C | 新兴技术-AIOps智能运维 | AIOps=Artificial Intelligence for IT Operations=AI+运维=基于机器学习/大数据分析自动化运维；核心能力=①监控指标采集(Prometheus+Grafana/Zabbix)②日志聚合(ELK=Elasticsearch+Logstash+Kibana)③告警收敛降噪(抑制/聚合/升级)④根因分析Root Cause(RCA=基于调用链拓扑+指标关联+异常检测自动定位故障点)⑤故障自愈(自动扩容/重启/切换)⑥容量预测规划；传统手工运维→自动化运维脚本化→DevOps→AIOps智能运维是趋势 |
| 61 | D | 软件工程-范围管理WBS | WBS=Work Breakdown Structure工作分解结构=将项目可交付成果和工作逐级分解为更小、更易管理的组件；原则=100%原则(上级所有下级之和=父100%无遗漏无重叠)/分解到工作包(8-80小时可完成可估算)/每个WBS元素唯一编码；WBS词典=每个工作包的详细说明(描述/负责人/工期/成本/依赖/验收标准)；WBS是范围基准(Scope Baseline=批准的范围说明书+WBS+WBS词典)核心组成；防止范围蔓延Scope Creep=没有通过变更控制的需求偷偷增加 |
| 62 | B | 计算机网络-TCP连接管理 | TCP三次握手(建立连接)=①Client→SYN=1,seq=x(随机初始序号)→Server (客户端SYN_SENT状态)②Server→SYN=1,ACK=1,ack=x+1,seq=y→Client (服务端SYN_RCVD)③Client→ACK=1,ack=y+1→Server (双方ESTABLISHED)，三次目的=确认双方收发能力+同步ISN初始序号防止历史重复连接；TCP四次挥手(释放连接)=①主动方→FIN=1,seq=u→被动方(FIN_WAIT_1)②被动方→ACK=1,ack=u+1→主动方(CLOSE_WAIT被动方可能还有数据) (主动方FIN_WAIT_2)③被动方发完数据→FIN=1,seq=w,ACK=1,ack=u+1→主动方(LAST_ACK)④主动方→ACK=1,ack=w+1(TIME_WAIT=2MSL最长报文寿命，约1-2分钟再关闭，确保最后ACK到达+旧报文在网络中消失)；四次原因=TCP全双工，读写通道独立关闭 |
| 63 | A | 数据库-索引类型InnoDB | MySQL InnoDB=聚簇索引组织表(数据和主键索引绑在一起B+树叶子节点=完整行数据，一张表只能一个聚簇索引，最好选自增主键避免页分裂)；二级辅助索引(非聚簇)=叶子节点存主键值，查辅助索引→回表(再查聚簇索引拿行数据)→覆盖索引(索引包含要查所有列避免回表explain Extra Using index)；MyISAM引擎=非聚簇索引，索引和数据文件分离.MYI+.MYD，叶子存数据文件地址指针；B+树vs B树=B+树所有数据在叶子，叶子链表范围查询快，内部节点不存数据存更多键=树更矮IO更少；Hash索引=等值查询快不支持范围和排序 |
| 64 | D | 系统配置与性能-数据库优化 | 数据库优化五层=①SQL与索引优化(慢查询日志分析+explain执行计划(type列ALL<index<range<ref<eq_ref<const；Extra避免Using filesort临时排序/Using temporary临时表/Using join buffer)；建索引原则=高选择性列/最左前缀匹配/避免索引失效(函数/类型转换/前导like%/OR非索引列/!=<>)②架构优化(读写分离主从复制/分库分表：垂直分库=按业务拆库；垂直分表=大列拆分冷数据；水平分表=按Range/Hash/List取模拆表，分片键选择+跨分片问题+全局分布式ID雪花算法；中间件ShardingSphere-JDBC应用层/Proxy代理层)③缓存优化(Redis热点数据缓存，旁路Cache Aside模式，缓存穿透/击穿/雪崩解决)④参数调优(缓冲池innodb_buffer_pool_size=物理内存70%/连接数/日志刷盘策略sync_binlog/innodb_flush_log_at_trx_commit双1安全)⑤硬件与存储升级(SSD替代机械盘/多核大内存/RAID10) |
| 65 | B | 信息安全-Web攻击防护 | DDoS分布式拒绝服务=海量肉鸡发起请求耗尽带宽/CPU/连接资源无法正常服务→防护=高防IP/WAF/流量清洗/CDN；SQL注入=恶意构造SQL(如' OR '1'='1)拼接到后端语句执行获取/篡改数据→防护=预编译PreparedStatement参数化查询/ORM框架/正则过滤WAF；XSS跨站脚本=注入恶意JS代码在其他用户浏览器执行(偷Cookie/跳转钓鱼)→反射型(URL参数)/存储型(数据库存评论区最危险)/DOM型→防护=输出转义/HttpOnly Cookie/Content-Security-Policy CSP；CSRF跨站请求伪造=诱导用户登录A站后访问B站构造A站请求→防护=Token校验/Referer检查/验证码/SameSite Cookie；其他：SSRF服务端请求伪造/文件上传漏洞/RCE命令执行/反序列化→安全编码规范+WAF+渗透测试 |
| 66 | C | 新兴技术-ES搜索引擎 | Elasticsearch=分布式RESTful搜索引擎+分析引擎基于Lucene；核心架构=Cluster集群多个Node节点/Index索引(类似DB库)/Type类型(7.x移除)/Document文档JSON行/Field字段/Shard分片(主分片水平扩展/副分片高可用冗余)；倒排索引=Term词→文档ID列表+位置词频，快速全文检索；生态=ELK Stack=Elasticsearch+Logstash(采集处理)+Kibana(可视化仪表盘)+Beats(轻量采集)；应用场景=全文检索站内搜索/日志分析/安全分析/运维监控/商品搜索电商+高亮+分页+聚合统计(DSL查询)； vs Solr=ES分布式实时性好易上手更主流 |
| 67 | B | 系统架构-遗留系统改造 | 遗留系统Legacy System=老旧技术栈维护成本高但运行核心业务；改造策略四象限(按技术水平+业务价值)=①淘汰(低价值+低技术水平)→直接下线关停废弃(如没人用的老报表)②继承/保留维护(高价值+低技术水平)→简单维护不做重构(如核心账务稳定跑了10年的Cobol系统，风险极高别乱动)③集成(低价值+高技术水平可集成能力强)→做集成接口打通不改造系统本身(封装API给新系统调，ESB/API网关)④重构/现代化改造(高价值+高技术水平，有复用改造价值)→再工程(逆向工程+正向重构)/微服务拆分/容器化上云/UI现代化(后端核心保留前端改造前后端分离)/代码迁移(COBOL→Java重写，风险最高成本最大，充分验证双轨运行)；决策依据=成本收益分析+风险评估+业务连续性要求(停机窗口/双活切换) |
| 68 | B | 软件工程-软件规模估算 | FP功能点=Function Point=基于软件信息域特性(输入ILF/EIF/输出/查询/外部接口)×复杂度权重(简单/平均/复杂)×调整因子(VAF 14个一般系统特征)+语言因子转换为LOC；UCP用例点=UseCase Point=基于用例模型=角色权重(简单/平均/复杂)+用例权重(5/10/15事务流数)+技术因子TCF(13项)+环境因子ECF(8项)→总UCP=UAW+UUCW × TCF × ECF；每人天UCP系数行业值=10-20 UCP/人月，结合团队熟练度调整；优点=需求早期(用例模型完成后)就能估算不依赖技术细节；vs COCOMO=需要代码量或设计信息较晚；PERT三点估算=乐观O+悲观P+最可能M=(O+4M+P)/6+标准差σ=(P-O)/6，用于工期风险估算 |
| 69 | C | 操作系统-磁盘调度 | 磁盘I/O时间=寻道时间(移动磁头到柱面，占比最大要优化)+旋转延迟(等待扇区转到磁头下)+传输时间(实际读写字节)；调度算法=①FCFS先来先服务=公平但平均寻道长(随机)②SSTF最短寻道优先=每次选最近柱面=平均寻道短但有饥饿(两端永远不访问)③SCAN电梯算法=沿一个方向走到底再折返，类似电梯=兼顾公平+短寻道无饥饿，最常用；④C-SCAN循环扫描=一直往一个方向走到底跳回最开头再同向(单向循环)=请求响应时间更均匀；⑤LOOK/C-LOOK=走到最后一个请求就掉头不走到底(优化)；磁盘块号映射=CHS柱面/磁头/扇区→LBA逻辑块号(现代磁盘统一用LBA对OS透明)；坏道处理=扇区备用/静默替换/SMART监测 |
| 70 | D | 系统架构-Service Mesh服务网格 | Service Mesh微服务架构基础设施层，处理服务间通讯；典型=Istio(最主流Google+IBM+Lyft)+Linkerd；架构分=控制平面Control Plane(Pilot注入流量规则/Policy安全策略/Telemetry遥测采集/Citadel证书mTLS) + 数据平面Data Plane=每个服务Pod里Sidecar边车容器(Envoy代理，拦截进出所有服务流量透明代理，业务代码0侵入)；核心能力=①流量管理(灰度发布金丝雀发布A/B测试按比例切流/超时重试熔断/镜像流量)②安全(mTLS双向TLS加密服务间通讯+RBAC授权)③可观测性(统一Metrics指标/Tracing调用链Jaeger/Logging日志，不用业务埋点)④服务发现负载均衡；vs传统SDK侵入式(如Spring Cloud)=Mesh解耦业务逻辑与服务治理能力，多语言友好升级运维网格即可，缺点=Sidecar增加延迟(约2-5ms)和资源开销(每Pod多一个容器)，大规模集群控制平面性能要考虑；CNCF云原生计算基金会毕业项目，云原生网络基建 |
| 71 | B | 专业英语-质量属性 | Maintainability可维护性=修改(纠错/改进/适应)的难易程度与风险；A=Security安全；C=Reliability可靠性；D=Performance效率性能时间特性 |
| 72 | B | 专业英语-微服务 | 微服务核心优势=Independent独立部署+scaling扩展，技术栈灵活团队自治；A错=运维复杂度增加；C错=数据一致性分布式事务复杂；D错=跨服务调用网络调用增加 |
| 73 | D | 专业英语-数据库规范化 | Normalization规范化(1NF→2NF→3NF→BCNF→4NF)=分解关系模式消除数据冗余、插入/删除/更新异常；A=Indexing索引加速查询；C=Partitioning分区水平拆分；D=Replication复制冗余高可用 |
| 74 | C | 专业英语-设计模式 | Observer观察者(发布订阅)=对象状态变化通知所有依赖者一对多更新；A=Bridge桥接；C=Facade外观；D=Abstract Factory抽象工厂族对象 |
| 75 | C | 专业英语-SOA ESB | ESB企业服务总线=核心消息路由routing+协议转换translation+服务编排orchestration+数据转换+事务监控；A=DB；B=Web应用服务器；D=备份容灾无关 |

## 个人复盘

- 做题日期：
- 用时：
- 得分：/75
- 错题数：
- 薄弱章节：
- 二刷记录：
