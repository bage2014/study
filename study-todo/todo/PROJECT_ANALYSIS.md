# 我们小组的探针 Agent 项目全面梳理

> 变更时间：2026-07-13
> 变更人：某同学

---

## 一、基本功能

这是一套基于 **Java Instrumentation API + ByteBuddy** 构建的**通用探针 Agent 框架**，用于在不修改业务代码的前提下，对 JVM 运行时的目标类进行字节码增强，实现：

| 功能类别 | 具体能力 |
|----------|----------|
| **方法拦截** | 实例方法、静态方法、构造方法的 Before/After/OnException 钩子 |
| **插件化扩展** | 通过 `probe-plugin.def` 文件声明插件，运行时动态加载 |
| **类匹配** | 支持精确类名匹配、多类名匹配、注解匹配、间接条件匹配 |
| **上下文透传** | 通过 `EnhancedInstance` 接口在增强类实例中存储跨方法状态 |
| **状态管理** | 有状态 / 无状态拦截器两种模式，满足不同场景需求 |

**适用场景**：APM 性能监控、分布式链路追踪、方法埋点、故障诊断。

---

## 二、基本原理

```
JVM 启动
  └─ -javaagent:probe-agent.jar
       └─ ProbeAgent.premain()
            ├─ PluginBootstrap.loadPlugins()          // 扫描 plugins/*.jar 内的 probe-plugin.def
            │    └─ AgentClassLoader.initDefaultLoader() // 自定义 ClassLoader 隔离插件依赖
            ├─ PluginFinder 构建匹配索引
            │    ├─ NameMatch → HashMap 精确命中
            │    └─ IndirectMatch → ElementMatcher 条件匹配
            └─ ByteBuddy AgentBuilder.installOn(instrumentation)
                 └─ 类加载时触发 Transformer
                      └─ ClassEnhancePluginDefine.enhance()
                           ├─ 静态方法 → StaticMethodsInterceptor
                           ├─ 实例方法 → InstanceMethodsInterceptor
                           └─ 构造方法 → ConstructorInterceptor
```

**核心机制**：
1. **JVM TI (Tool Interface)**：通过 `premain` 在应用启动前注入，获得 `Instrumentation` 句柄
2. **ByteBuddy**：在类加载阶段修改字节码，插入拦截逻辑（ASM 上层封装）
3. **ClassLoader 隔离**：`AgentClassLoader` 加载插件，避免与业务类路径冲突
4. **Around 模式**：拦截器实现 `beforeMethod → 原方法 → afterMethod / handleException` 的环绕执行

---

## 三、方案设计

### 模块划分

```
probe-agent
├── bootstrap（入口层）
│   ├── ProbeAgent.java          premain 入口，装配 ByteBuddy
│   └── BytebuddyListener.java   字节码增强事件监听
└── core（框架层）
    ├── boot/AgentPackagePath    定位 Agent 自身 jar 路径
    ├── plugin/
    │   ├── PluginBootstrap      插件加载编排
    │   ├── PluginCfg(Enum)      插件配置单例
    │   ├── PluginFinder         类匹配索引构建
    │   ├── match/               匹配策略（名称/注解/间接）
    │   ├── interceptor/         拦截器抽象（有状态/无状态 × 实例/静态/构造）
    │   └── loader/
    │       ├── AgentClassLoader          插件隔离类加载器
    │       └── InterceptorInstanceLoader 拦截器实例管理
    └── plugin/enhance/
        ├── EnhancedInstance     增强实例接口（用于存储上下文）
        └── EnhanceContext       增强上下文封装
```

### 插件扩展协议

插件 jar 内放置 `probe-plugin.def`：
```
pluginName=com.xxx.YourPluginDefine
```
实现 `AbstractClassEnhancePluginDefine` → 声明匹配类 + 拦截点 → 框架自动织入。

### 关键设计模式

| 模式 | 实现 | 用途 |
|------|------|------|
| **Plugin** | AbstractClassEnhancePluginDefine | 定义可插拔的字节码增强插件 |
| **Builder** | ByteBuddy AgentBuilder | 流畅构建字节码增强规则 |
| **Matcher** | ElementMatcher (ByteBuddy) | 灵活的类和方法匹配 |
| **Singleton** | PluginCfg (Enum) | 全局插件配置管理 |
| **Custom ClassLoader** | AgentClassLoader | 隔离加载插件及依赖 |
| **Interceptor** | Around Pattern | 方法前后置和异常处理 |

---

## 四、竞品分析

| 对比维度 | **本项目** | **SkyWalking Agent** | **Pinpoint Agent** | **Elastic APM Java** |
|----------|-----------|---------------------|-------------------|--------------------|
| **定位** | 内部通用探针框架 | 开源全功能 APM Agent | 开源全功能 APM Agent | 商业/开源 APM |
| **字节码引擎** | ByteBuddy | ByteBuddy | ASM | ByteBuddy |
| **插件机制** | `probe-plugin.def` 文件 | `skywalking-plugin.def` 文件 | 同类机制 | 内置插件体系 |
| **插件数量** | 内部自建（少） | 200+ 开源插件 | 200+ 开源插件 | 100+ 插件 |
| **链路追踪** | 框架层未内置（需插件实现） | 完整 OpenTelemetry/OT 支持 | 完整追踪 | 完整追踪 |
| **Metrics 采集** | 无内置 | 有 | 有 | 有 |
| **配置中心集成** | 无 | gRPC/HTTP 动态配置 | 无 | 有 |
| **社区生态** | 无 | 极强 | 强 | 强 |
| **定制成本** | 低（内部可控） | 高（需遵守开源协议+架构约束） | 高 | 高 |

> **结论**：本项目是 SkyWalking Agent 框架层的裁剪版复刻，去掉了追踪上报、配置中心、UI 等运营侧能力，保留了字节码增强的核心骨架，适合作为**内部私有探针的底座**。

---

## 五、优势 vs 不足

### 优势

| 优势 | 说明 |
|------|------|
| **轻量** | 仅保留框架层，无追踪/上报等依赖，产物 jar 小 |
| **可控** | 内部维护，可深度定制匹配规则和拦截逻辑 |
| **隔离性好** | `AgentClassLoader` 避免插件依赖污染业务 classpath |
| **扩展简单** | 插件协议清晰，新增插件只需实现抽象类 + 一个 def 文件 |
| **有/无状态分离** | 拦截器状态模型设计合理，无状态可复用单例，降低内存压力 |

### 不足

| 不足 | 影响 |
|------|------|
| **无追踪上报能力** | 需要各插件自己实现数据采集和上报，重复造轮子风险高 |
| **无动态配置支持** | 插件启用/禁用需重启 JVM，无法热切换 |
| **无 Agent 版本管理** | 插件与框架版本兼容性缺乏约束机制 |
| **缺少测试体系** | 未见测试目录，字节码增强的回归验证成本高 |
| **无监控/可观测性** | 框架自身的增强成功/失败、拦截耗时无埋点 |
| **无官方插件库** | 常见中间件（Dubbo、Redis、HTTP Client）需从零实现，而 SkyWalking 已有现成插件 |
| **命名规范问题** | `MultiClasNameMatch`（少了一个 's'）等命名问题，暗示缺乏 Code Review 流程 |

---

## 六、成本评估

### 研发成本（现状）

| 维度 | 估算 |
|------|------|
| **框架层复杂度** | 低，约 ~30 个核心类，参考 SkyWalking 实现裁剪而来 |
| **新增一个插件** | 熟悉框架后约 0.5~2 人天（视拦截复杂度） |
| **框架维护** | 低，除 ByteBuddy 版本升级外几乎无需变更 |

### 运行时成本

| 维度 | 影响 |
|------|------|
| **启动耗时** | 类扫描 + ByteBuddy 编译增加约 100~500ms（视插件数量） |
| **内存占用** | AgentClassLoader + 插件类 + 增强后字节码，增量约 10~50MB |
| **运行时 CPU** | 拦截器逻辑 overhead 一般 <1%，但有状态拦截器需评估 |

### 与直接集成 SkyWalking 的成本对比

| 方案 | 研发投入 | 功能完整性 | 可控性 |
|------|----------|-----------|--------|
| **当前方案（自建框架）** | 中（框架 + 每个插件自建） | 低（无现成插件） | 高 |
| **直接使用 SkyWalking** | 低（直接用开源插件） | 高（200+ 插件开箱即用） | 中 |
| **在 SkyWalking 基础上扩展** | 低（只写差异化插件） | 高 | 中 |

> **核心问题**：若目标是链路追踪/APM，直接集成 SkyWalking 的 ROI 远高于自建；若目标是**私有探针/安全审计/业务埋点**等需要高度定制的场景，当前方案有其合理性。

---

## 七、一句话总结

这是一个 SkyWalking Agent 框架层的内部裁剪版，适合作为私有插件探针底座，但缺乏追踪上报、动态配置、测试覆盖等生产级能力，若业务目标是 APM 建议评估直接复用 SkyWalking 开源插件的可行性。
