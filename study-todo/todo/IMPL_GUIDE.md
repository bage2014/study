# Java Agent 探针框架 — 实现参考手册

> 变更时间：2026-07-13  
> 变更人：某同学  
> 核心变更：基于源码全量分析，产出面向跨语言参考实现的技术文档

---

## 一、一句话定位

这是一个 **"插件化字节码增强框架"**：在 JVM 启动时，通过 Java Instrumentation API 拦截类加载过程，用 ByteBuddy 在内存中修改字节码，将用户定义的插件逻辑（Before/After/OnException）无侵入地织入目标类的方法中。

---

## 二、核心原理

### 2.1 启动时序

```
java -javaagent:probe-agent.jar -jar app.jar
         │
         ▼
  JVM 在 main() 之前调用 ProbeAgent.premain(args, instrumentation)
         │
         ├─ 1. PluginBootstrap.loadPlugins()
         │        │
         │        ├─ AgentClassLoader.initDefaultLoader()   // 创建自定义 ClassLoader
         │        ├─ PluginResourceResolver.getResources()  // 扫描 plugins/*.jar 里的 probe-plugin.def
         │        ├─ PluginCfg.INSTANCE.load()              // 解析 def 文件，得到插件类名列表
         │        └─ Class.forName(pluginClass, agentClassLoader).newInstance()  // 实例化各插件
         │
         ├─ 2. PluginFinder(plugins)
         │        ├─ NameMatch 插件 → nameMatchDefine (HashMap)
         │        └─ IndirectMatch 插件 → signatureMatchDefine (List)
         │
         └─ 3. ByteBuddy AgentBuilder
                  ├─ .type(pluginFinder.buildMatch())        // 构建 OR 联合匹配条件
                  ├─ .transform(new Transformer(pluginFinder))
                  └─ .installOn(instrumentation)            // 注册 ClassFileTransformer
```

### 2.2 类加载时触发（核心热路径）

```
JVM 加载任意类
    │
    ▼
AgentBuilder.Transformer.transform() 被回调
    │
    ├─ pluginFinder.find(typeDescription)   // 查找匹配该类的所有插件
    │
    └─ for each matchedPlugin:
           plugin.define(typeDescription, builder, classLoader, enhanceContext)
                │
                ├─ enhanceClass()      // 增强静态方法
                └─ enhanceInstance()   // 增强实例方法 + 构造方法
                         │
                         ├─ 注入 _$EnhancedCLass_ws 字段（仅首次，保证幂等）
                         ├─ 让目标类实现 EnhancedInstance 接口
                         └─ MethodDelegation → Interceptor.intercept()
```

---

## 三、关键抽象层（逐层说明）

### 3.1 插件协议（Plugin Contract）

插件开发者需要：

1. **新建一个 jar**，在 `resources/probe-plugin.def` 里写：
   ```
   pluginName=com.example.YourPluginDefine
   ```

2. **实现插件类**，继承 `ClassEnhancePluginDefine`，实现 4 个抽象方法：

| 方法 | 返回值 | 说明 |
|------|--------|------|
| `enhanceClass()` | `ClassMatch` | 声明要拦截哪个类 |
| `getInstanceMethodsInterceptPoint()` | `InstanceMethodsInterceptPoint[]` | 实例方法拦截点 |
| `getStaticMethodsInterceptPoint()` | `StaticMethodsInterceptPoint[]` | 静态方法拦截点 |
| `getConstructMethodsInterceptPoint()` | `ConstructorMethodsInterceptPoint[]` | 构造方法拦截点 |

3. **实现拦截器**，实现 `StatelessInstanceMethodsAroundInterceptor` 等接口：

```java
public interface StatelessInstanceMethodsAroundInterceptor {
    void beforeMethod(EnhancedInstance instance, Method method,
                      Object[] allArguments, Class<?>[] parameterType);
    void afterMethod(EnhancedInstance instance, Method method,
                     Object[] allArguments, Class<?>[] parameterType, Object result);
    void handleEx(EnhancedInstance instance, Method method,
                  Object[] allArguments, Class<?>[] parameterType, Object result, Throwable t);
}
```

### 3.2 类匹配体系（ClassMatch 层级）

```
ClassMatch (标记接口)
├── NameMatch           → 精确类名，存入 HashMap，O(1) 查找
│     └── MultiClasNameMatch  → 多个精确类名（注意：源码拼写错误少了一个 's'）
└── IndirectMatch       → 条件匹配，存入 List，类加载时全量扫描
      └── ClassAnnotationNameMatch  → 要求类上有指定注解（多注解取 AND）
```

**两类匹配的性能差异**：
- `NameMatch`：HashMap.containsKey，几乎零开销
- `IndirectMatch`：每次类加载都遍历 signatureMatchDefine 列表，插件多时有性能影响

### 3.3 拦截器类型矩阵

框架提供 6 种拦截器适配器（Interceptor = 框架层胶水代码，AroundInterceptor = 插件层用户代码）：

| 方法类型 | 状态模式 | 框架适配器 | 用户需实现的接口 |
|----------|----------|-----------|----------------|
| 实例方法 | 无状态 | `StatelessInstanceMethodsInterceptor` | `StatelessInstanceMethodsAroundInterceptor` |
| 实例方法 | 有状态 | `StatefulInstanceMethodsInterceptor` | `StatefulInstanceMethodsAroundInterceptor` |
| 静态方法 | 无状态 | `StatelessStaticMethodsInterceptor` | `StatelessStaticMethodsAroundInterceptor` |
| 静态方法 | 有状态 | `StatefulStaticMethodsInterceptor` | `StatefulStaticMethodsAroundInterceptor` |
| 构造方法 | — | `ConstructorInterceptor` | `ConstructorInterceptor`（接口） |

**无状态 vs 有状态的区别**：
- **无状态**：框架负责 try/catch/finally 的 Around 骨架，用户只需填 `beforeMethod`/`afterMethod`/`handleEx` 三段
- **有状态**：用户拿到完整的 `Callable<?> zuper`，自行控制调用时机和异常处理，灵活度更高但复杂度也更高

### 3.4 EnhancedInstance —— 跨方法上下文透传

目标类被增强后，框架通过 ByteBuddy：
1. 注入一个私有 volatile 字段 `_$EnhancedCLass_ws`（类型 Object）
2. 让目标类实现 `EnhancedInstance` 接口（提供 get/set 方法）

这样，插件代码可以在 `beforeMethod` 中把状态存入 `instance.setProbeDynamicField(xxx)`，在 `afterMethod` 中取出，实现跨方法的上下文传递（如记录方法开始时间、存储 SQL 参数等）。

**幂等保证**：`EnhanceContext.isObjectEnhanced()` 标记位确保同一个类只注入一次字段，即使被多个插件匹配也不会重复注入。

---

## 四、ClassLoader 隔离设计

### 4.1 问题背景

插件 jar 可能依赖第三方库（如 commons-lang），这些依赖不应污染业务应用的 classpath，否则会引发版本冲突。

### 4.2 解决方案

```
Business ClassLoader (App ClassLoader)
        │ parent
        ▼
AgentClassLoader (DEFAULT_LOADER)
    └─ 扫描 agent.jar 同级 /plugins/ 目录下所有 .jar
    └─ 覆盖 findClass() / getResources() / getResource()
               直接从 JarFile 中读取字节码，通过 defineClass() 加载

InterceptorInstanceLoader.load(interceptorName, businessClassLoader)
    └─ new AgentClassLoader(businessClassLoader)  // 以业务 ClassLoader 为 parent
    └─ Class.forName(interceptorName, agentClassLoader)
```

**关键约束**：加载拦截器的 ClassLoader 必须以被拦截类的 ClassLoader 为父类，否则拦截器代码无法访问目标类的类型。

### 4.3 目录约定

```
agent-package/
├── probe-agent.jar          # bootstrap 入口 jar（含 premain）
└── plugins/
    ├── spring-mvc-plugin.jar
    ├── mysql-plugin.jar
    └── ...
```

---

## 五、Around 拦截骨架（无状态实例方法的完整逻辑）

```java
// 无状态实例方法拦截器的核心 intercept() 方法，逻辑是：
// beforeMethod → 原始方法 → (异常? handleEx) → afterMethod（无论是否异常）→ 重新抛出异常

Object result = null;
Throwable originalException = null;

try {
    interceptor.beforeMethod(targetObj, method, args, paramTypes);
} catch (Exception e) { /* 吞掉，保证业务方法一定被调用 */ }

try {
    result = zuper.call();   // 调用原始方法
} catch (Throwable e) {
    originalException = e;
    try {
        interceptor.handleEx(targetObj, method, args, paramTypes, result, e);
    } catch (Exception ex) { /* 吞掉 */ }
} finally {
    try {
        interceptor.afterMethod(targetObj, method, args, paramTypes, result);
    } catch (Exception e) { /* 吞掉 */ }
}

if (originalException != null) throw originalException;  // 原始异常必须重新抛出
return result;
```

**注意**：异常发生时，`result` 是 null（因为 `zuper.call()` 未正常返回），但 `afterMethod` 仍然会被调用，`result` 参数为 null。

---

## 六、插件 def 文件格式

```
# 注释行（以 # 开头）
# 格式：pluginName=全类名（等号两侧无空格）
pluginName=com.example.SpringMvcInstrumentation
pluginName=com.example.MysqlInstrumentation
```

`PluginCfg` 使用 `PluginDefine.build(line)` 解析，以 `=` 分割，左边是 key（忽略），右边是实现类全名。

---

## 七、跨语言参考实现要点

### 7.1 Go 语言

Go 没有 JVM 字节码增强，但可用以下等价方案：

| Java 机制 | Go 等价实现 |
|-----------|------------|
| `premain` + `Instrumentation` | `init()` 函数 + 使用 `github.com/goccy/go-reflect` 或编译期代码生成 |
| ByteBuddy 字节码增强 | `github.com/datadog/dd-trace-go` 的 compile-time instrumentation 或 eBPF uprobe |
| 插件 + ClassLoader 隔离 | Go plugin (`plugin.Open`) 或直接编译进主程序 |
| Around 拦截 | 函数包装 + defer/recover 模拟 before/after/exception |
| `EnhancedInstance` 字段注入 | 侵入式：在结构体中预留 `ProbeContext interface{}` 字段 |

### 7.2 Python

| Java 机制 | Python 等价实现 |
|-----------|----------------|
| `premain` + Instrumentation | `sitecustomize.py` + `sys.settrace` / `sys.setprofile` |
| ByteBuddy | `wrapt.wrap_function_wrapper` 或 `functools.wraps` |
| 插件 def 文件 | `importlib.metadata` entry_points，在 `setup.cfg` 中声明 |
| ClassLoader 隔离 | 无需（Python 无此问题，或用 virtualenv 隔离） |
| Around 拦截 | `@wrapt.decorator` 实现 before/after/exception |
| `EnhancedInstance` | `threading.local` 或给对象动态添加 `__probe_context__` 属性 |

### 7.3 核心不变量（任何语言都必须遵守）

1. **插件发现**：启动时扫描 def 文件 → 实例化插件 → 构建匹配索引，这是有序的三步
2. **两级匹配索引**：精确名称匹配用 HashMap（O(1)），条件匹配用 List（O(n)）；先查 HashMap 再扫 List
3. **隔离加载**：拦截器的类路径必须能访问被拦截类的类型定义，否则运行时类型转换失败
4. **Around 吞异常**：before/after/handleEx 内部异常必须吞掉，不能影响原始业务逻辑
5. **原始异常透传**：原始方法抛出的异常必须重新抛出，不能被框架静默吞掉
6. **字段注入幂等**：一个类即使匹配多个插件，上下文字段只注入一次

---

## 八、完整的插件实现示例（Java）

```java
// 1. 声明插件（ClassEnhancePluginDefine 子类）
public class MyServiceInstrumentation extends ClassEnhancePluginDefine {

    @Override
    protected ClassMatch enhanceClass() {
        return NameMatch.byName("com.example.MyService");  // 精确类名匹配
        // 或注解匹配：ClassAnnotationNameMatch.byClassAnnotationMatch("org.springframework.stereotype.Service")
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoint() {
        return new InstanceMethodsInterceptPoint[]{
            new InstanceMethodsInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("doWork");  // 匹配方法名
                }
                @Override
                public String getMethodInterceptor() {
                    return "com.example.MyServiceInterceptor";  // 拦截器全类名
                }
                @Override
                public boolean stateless() { return true; }
            }
        };
    }

    @Override
    protected ConstructorMethodsInterceptPoint[] getConstructMethodsInterceptPoint() { return null; }

    @Override
    protected StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoint() { return null; }
}

// 2. 实现拦截器
public class MyServiceInterceptor implements StatelessInstanceMethodsAroundInterceptor {

    @Override
    public void beforeMethod(EnhancedInstance instance, Method method,
                             Object[] allArguments, Class<?>[] parameterType) {
        instance.setProbeDynamicField(System.currentTimeMillis());  // 存开始时间
    }

    @Override
    public void afterMethod(EnhancedInstance instance, Method method,
                            Object[] allArguments, Class<?>[] parameterType, Object result) {
        long start = (Long) instance.getProbeDynamicField();
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void handleEx(EnhancedInstance instance, Method method,
                         Object[] allArguments, Class<?>[] parameterType, Object result, Throwable t) {
        System.err.println("方法异常：" + t.getMessage());
    }
}

// 3. probe-plugin.def（放在插件 jar 的 resources 目录）
// pluginName=com.example.MyServiceInstrumentation
```

---

## 九、已知缺陷与禁区

| 缺陷 | 影响 |
|------|------|
| `MultiClasNameMatch` 拼写错误（少一个 's'） | 引用时注意，不能写 `MultiClassNameMatch` |
| `InterceptorInstanceLoader.load()` 每次都创建新 `AgentClassLoader` | 高频拦截时有 ClassLoader 对象开销，生产环境应加缓存 |
| `PluginCfg` 是 Enum 单例，`pluginClassList` 无线程安全保护 | 多线程并发调用 `loadPlugins()` 会出现竞争 |
| 无拦截器接口版本校验 | 插件 jar 与框架 jar 版本不匹配时运行时才会报错 |
| `ProbeAgent.premain` 对异常直接 `return`（静默失败） | Agent 加载失败无日志，排查困难 |
