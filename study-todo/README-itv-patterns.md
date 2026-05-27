# study-patterns 设计模式

## 简介

设计模式是软件开发中反复使用的最佳实践总结，是解决特定问题的成熟方案。它不是具体的代码实现，而是一种编程思想和方法论。

## 目录

- [设计模式分类](#设计模式分类)
- [创建型模式](#创建型模式)
  - [单例模式](#单例模式)
  - [工厂模式](#工厂模式)
  - [建造者模式](#建造者模式)
- [结构型模式](#结构型模式)
  - [适配器模式](#适配器模式)
  - [装饰器模式](#装饰器模式)
  - [代理模式](#代理模式)
  - [组合模式](#组合模式)
- [行为型模式](#行为型模式)
  - [策略模式](#策略模式)
  - [模版方法](#模版方法)
  - [观察者模式](#观察者模式)
  - [责任链模式](#责任链模式)
  - [命令模式](#命令模式)
- [设计原则](#设计原则)

## 设计模式分类

| 类别 | 包含模式 | 核心解决 |
|------|----------|----------|
| 创建型 | 单例、工厂、建造者、原型 | 对象创建问题 |
| 结构型 | 适配器、装饰器、代理、组合 | 类/对象结构问题 |
| 行为型 | 策略、模版方法、观察者、责任链 | 对象行为/职责问题 |

## Key关键点

- 基本设计模式概念：23种经典模式
- 经典案例：每种模式对应的实际应用场景
- 为何使用：模式带来的好处（解耦、可扩展、可维护）
- 注意事项：模式误用和过度使用的问题

---

## 创建型模式

## 单例模式

### 概念

确保一个类只有一个实例，并提供一个全局访问点。

### 实现方式

#### 1. 饿汉式（静态常量）

```java
public class Singleton {
    // 类加载时就创建实例
    private static final Singleton INSTANCE = new Singleton();

    // 私有构造函数
    private Singleton() {}

    // 对外提供获取实例的方法
    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

#### 2. 饿汉式（静态代码块）

```java
public class Singleton {
    private static final Singleton INSTANCE;

    static {
        INSTANCE = new Singleton();
    }

    private Singleton() {}

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

#### 3. 懒汉式（线程不安全）

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

#### 4. 懒汉式（线程安全 synchronized）

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

#### 5. 双重检查锁（DCL）

```java
public class Singleton {
    // volatile 确保可见性和有序性
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {           // 第一次检查
            synchronized (Singleton.class) {
                if (instance == null) {  // 第二次检查
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

#### 6. 静态内部类

```java
public class Singleton {
    private Singleton() {}

    // 静态内部类不会随外部类加载而加载，只有调用 getInstance 时才会加载
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

#### 7. 枚举方式（推荐）

```java
public enum Singleton {
    INSTANCE;

    public void doSomething() {
        // 具体方法
    }
}
```

### 各实现方式对比

| 方式 | 线程安全 | 延迟加载 | 效率 | 推荐度 |
|------|----------|----------|------|--------|
| 饿汉式静态常量 | 是 | 否 | 高 | ★★★★ |
| 饿汉式静态块 | 是 | 否 | 高 | ★★★★ |
| 懒汉式synchronized | 是 | 是 | 低 | ★★ |
| 双重检查锁 | 是 | 是 | 中 | ★★★★ |
| 静态内部类 | 是 | 是 | 高 | ★★★★★ |
| 枚举 | 是 | 是 | 高 | ★★★★★ |

### 使用场景

- 数据库连接池
- 日志对象
- 配置信息管理
- 任务调度器

### 注意事项

- 反射可以破坏单例（可用枚举防止）
- 序列化可能破坏单例（需实现 `readResolve` 方法）
- 避免过度使用单例导致全局状态耦合

---

## 工厂模式

### 概念

定义一个创建对象的接口，让子类决定实例化哪个类。工厂方法使一个类的实例化延迟到其子类。

### 简单工厂（不属于GOF23）

```java
// 产品接口
public interface Product {
    void operation();
}

// 具体产品A
public class ConcreteProductA implements Product {
    @Override
    public void operation() {
        System.out.println("产品A操作");
    }
}

// 具体产品B
public class ConcreteProductB implements Product {
    @Override
    public void operation() {
        System.out.println("产品B操作");
    }
}

// 简单工厂
public class SimpleFactory {
    public Product createProduct(String type) {
        if ("A".equals(type)) {
            return new ConcreteProductA();
        } else if ("B".equals(type)) {
            return new ConcreteProductB();
        }
        throw new IllegalArgumentException("未知产品类型");
    }
}
```

### 工厂方法模式

```java
// 抽象工厂
public interface Factory {
    Product createProduct();
}

// 具体工厂A
public class FactoryA implements Factory {
    @Override
    public Product createProduct() {
        return new ConcreteProductA();
    }
}

// 具体工厂B
public class FactoryB implements Factory {
    @Override
    public Product createProduct() {
        return new ConcreteProductB();
    }
}

// 使用
Factory factory = new FactoryA();
Product product = factory.createProduct();
```

### 抽象工厂模式

```java
// 产品族接口
public interface Button {
    void render();
}

public interface TextField {
    void render();
}

// 具体产品
public class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("渲染Windows风格按钮");
    }
}

public class WindowsTextField implements TextField {
    @Override
    public void render() {
        System.out.println("渲染Windows风格文本框");
    }
}

public class MacButton implements Button {
    @Override
    public void render() {
        System.out.println("渲染Mac风格按钮");
    }
}

public class MacTextField implements TextField {
    @Override
    public void render() {
        System.out.println("渲染Mac风格文本框");
    }
}

// 抽象工厂
public interface GUIFactory {
    Button createButton();
    TextField createTextField();
}

// 具体工厂
public class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public TextField createTextField() {
        return new WindowsTextField();
    }
}

public class MacFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public TextField createTextField() {
        return new MacTextField();
    }
}
```

### 使用场景

- 对象创建逻辑复杂
- 需要统一创建入口
- 解耦对象创建和使用

---

## 建造者模式

### 概念

将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。

### 传统实现

```java
// 产品类
public class Product {
    private String partA;
    private String partB;
    private String partC;

    // Getter/Setter
    public void setPartA(String partA) { this.partA = partA; }
    public void setPartB(String partB) { this.partB = partB; }
    public void setPartC(String partC) { this.partC = partC; }
}

// 抽象建造者
public interface Builder {
    void buildPartA();
    void buildPartB();
    void buildPartC();
    Product getResult();
}

// 具体建造者
public class ConcreteBuilder implements Builder {
    private Product product = new Product();

    @Override
    public void buildPartA() {
        product.setPartA("PartA完成");
    }

    @Override
    public void buildPartB() {
        product.setPartB("PartB完成");
    }

    @Override
    public void buildPartC() {
        product.setPartC("PartC完成");
    }

    @Override
    public Product getResult() {
        return product;
    }
}

// 指挥者
public class Director {
    public void construct(Builder builder) {
        builder.buildPartA();
        builder.buildPartB();
        builder.buildPartC();
    }
}

// 使用
Director director = new Director();
Builder builder = new ConcreteBuilder();
director.construct(builder);
Product product = builder.getResult();
```

### Lombok @Builder（推荐）

```java
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private String partA;
    private String partB;
    private String partC;
}

// 使用
Product product = Product.builder()
    .partA("A")
    .partB("B")
    .partC("C")
    .build();
```

### 使用场景

- 构造参数较多
- 需要链式调用
- 对象创建逻辑稳定但表示可灵活组合

### 注意事项

- 建造者模式与工厂模式区别：建造者更关注对象构建过程，工厂更关注对象创建

---

## 结构型模式

## 适配器模式

> 将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。

### 类适配器（继承）

```java
// 目标接口
public interface Target {
    void request();
}

// 被适配者
public class Adaptee {
    public void specificRequest() {
        System.out.println("被适配者的方法");
    }
}

// 类适配器（继承方式）
public class ClassAdapter extends Adaptee implements Target {
    @Override
    public void request() {
        System.out.println("类适配器转换");
        specificRequest();
    }
}
```

### 对象适配器（组合）

```java
// 对象适配器（组合方式）
public class ObjectAdapter implements Target {
    private Adaptee adaptee;

    public ObjectAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        System.out.println("对象适配器转换");
        adaptee.specificRequest();
    }
}
```

### 使用场景

- 集成第三方SDK
- 兼容老系统接口
- 统一不同数据源接口

### 优缺点

- 优点：更好的复用性，更好的可扩展性
- 缺点：过多的使用适配器，会让系统非常凌乱，不容易整体进行把握

---

## 装饰器模式

### 概念

动态地给对象添加一些额外的职责。就功能来说，装饰器模式相比继承更加灵活。

### 实现

```java
// 基础接口
public interface Component {
    void operation();
}

// 具体组件
public class ConcreteComponent implements Component {
    @Override
    public void operation() {
        System.out.println("基础组件操作");
    }
}

// 基础装饰器
public abstract class Decorator implements Component {
    protected Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        component.operation();
    }
}

// 具体装饰器A
public class ConcreteDecoratorA extends Decorator {
    public ConcreteDecoratorA(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        System.out.println("装饰器A添加的功能");
    }
}

// 具体装饰器B
public class ConcreteDecoratorB extends Decorator {
    public ConcreteDecoratorB(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        System.out.println("装饰器B添加的功能");
    }
}

// 使用
Component component = new ConcreteComponent();
component = new ConcreteDecoratorA(component);
component = new ConcreteDecoratorB(component);
component.operation();
```

### 使用场景

- Java I/O流（BufferedInputStream装饰FileInputStream）
- Web过滤器链
- 日志增强、事务管理

### 与继承对比

| 特性 | 继承 | 装饰器 |
|------|------|--------|
| 编译时绑定 | 是 | 否（运行时组合） |
| 类爆炸 | 是 | 否 |
| 动态添加 | 否 | 是 |

---

## 代理模式

### 概念

为其他对象提供一种代理以控制对这个对象的访问。

### 静态代理

```java
// 接口
public interface Subject {
    void request();
}

// 真实主题
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("真实对象处理请求");
    }
}

// 代理类
public class Proxy implements Subject {
    private RealSubject realSubject;

    @Override
    public void request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        beforeRequest();
        realSubject.request();
        afterRequest();
    }

    private void beforeRequest() {
        System.out.println("代理：前置处理");
    }

    private void afterRequest() {
        System.out.println("代理：后置处理");
    }
}
```

### 动态代理（JDK）

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy implements InvocationHandler {
    private Object target;

    public JdkProxy(Object target) {
        this.target = target;
    }

    public Object bind() {
        return Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("JDK代理：前置处理");
        Object result = method.invoke(target, args);
        System.out.println("JDK代理：后置处理");
        return result;
    }
}

// 使用
Subject realSubject = new RealSubject();
Subject proxy = (Subject) new JdkProxy(realSubject).bind();
proxy.request();
```

### CGLIB代理

```java
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class CglibProxy implements MethodInterceptor {
    private Object target;

    public CglibProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("CGLIB代理：前置处理");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("CGLIB代理：后置处理");
        return result;
    }
}

// 使用
RealSubject realSubject = new RealSubject();
RealSubject proxy = (RealSubject) new CglibProxy(realSubject).getProxyInstance();
proxy.request();
```

### 代理模式应用场景

| 类型 | 用途 |
|------|------|
| 远程代理 | 访问远程对象 |
| 虚拟代理 | 延迟加载大对象 |
| 安全代理 | 权限控制 |
| 智能引用 | 访问时额外处理（如日志） |

---

## 组合模式

### 概念

将对象组合成树形结构以表示"部分-整体"的层次结构。组合模式使得用户对单个对象和组合对象的使用具有一致性。

### 实现

```java
import java.util.ArrayList;
import java.util.List;

// 抽象组件
public abstract class FileSystemComponent {
    protected String name;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    public abstract void display(int depth);
}

// 文件（叶子节点）
public class File extends FileSystemComponent {
    public File(String name) {
        super(name);
    }

    @Override
    public void display(int depth) {
        System.out.println("-".repeat(depth) + name);
    }
}

// 文件夹（容器节点）
public class Folder extends FileSystemComponent {
    private List<FileSystemComponent> children = new ArrayList<>();

    public Folder(String name) {
        super(name);
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public void display(int depth) {
        System.out.println("-".repeat(depth) + "+" + name);
        for (FileSystemComponent component : children) {
            component.display(depth + 2);
        }
    }
}

// 使用
Folder root = new Folder("根目录");
Folder doc = new Folder("文档");
Folder img = new Folder("图片");

File file1 = new File("简历.pdf");
File file2 = new File("照片.jpg");

doc.add(file1);
img.add(file2);
root.add(doc);
root.add(img);

root.display(0);
```

### 使用场景

- 文件系统
- 组织架构
- UI组件树

---

## 行为型模式

## 策略模式

策略模式定义了一系列的算法，并将每一个算法封装起来，使每个算法可以相互替代，使算法本身和使用算法的客户端分割开来，相互独立。

### 深入理解策略模式

- **策略模式的作用**：就是把具体的算法实现从业务逻辑中剥离出来，成为一系列独立算法类，使得它们可以相互替换。
- **策略模式的着重点**：不是如何来实现算法，而是如何组织和调用这些算法，从而让我们的程序结构更加的灵活、可扩展。

### 传统实现

```java
// 策略接口
public interface Strategy {
    void algorithm();
}

// 具体策略A
public class StrategyA implements Strategy {
    @Override
    public void algorithm() {
        System.out.println("策略A算法");
    }
}

// 具体策略B
public class StrategyB implements Strategy {
    @Override
    public void algorithm() {
        System.out.println("策略B算法");
    }
}

// 上下文
public class Context {
    private Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        strategy.algorithm();
    }
}

// 使用
Context context = new Context(new StrategyA());
context.execute();  // 输出：策略A算法

context.setStrategy(new StrategyB());
context.execute();  // 输出：策略B算法
```

### Lambda实现（Java 8+）

```java
// 使用函数式接口
@FunctionalInterface
public interface Strategy {
    void execute();
}

// 直接使用Lambda
public class StrategyLambda {
    public void execute(Strategy strategy) {
        strategy.execute();
    }
}

// 使用
StrategyLambda context = new StrategyLambda();
context.execute(() -> System.out.println("策略A实现"));
context.execute(() -> System.out.println("策略B实现"));
```

### 扩展上下文的实现

- **优点**：具体的策略实现风格很是统一，策略实现所需要的数据都是从上下文中获取的，在上下文中添加的数据，可以视为公共的数据，其他的策略实现也可以使用。
- **缺点**：如果某些数据只是特定的策略实现需要，大部分的策略实现不需要，那这些数据有"浪费"之嫌，另外如果每次添加算法数据都扩展上下文，很容易导致上下文的层级很是复杂。

### 在具体的策略实现上添加所需要的数据的实现

- **优点**：容易想到，实现简单
- **缺点**：与其他的策略实现风格不一致，其他的策略实现所需数据都是来自上下文，而这个策略实现一部分数据来自于自身，一部分数据来自于上下文；外部在使用这个策略实现的时候也和其他的策略实现不一致了，难以以一个统一的方式动态的切换策略实现。

### 使用场景

- 支付方式选择（支付宝、微信、银行卡）
- 排序算法选择（快排、归并、堆排）
- 压缩算法选择（ZIP、RAR、7Z）

---

## 模版方法

模板方法（`Template Method`）模式就是带有模板功能的模式，组成模板方法的方法被定义在父类中，这些方法是抽象方法，在模板方法中规定了这些方法的执行流程，这些抽象方法需要子类来具体实现。

### 实现

```java
// 抽象父类
public abstract class AbstractClass {
    // 模板方法（定义为final，防止子类重写）
    public final void templateMethod() {
        step1();
        step2();
        step3();
    }

    // 通用步骤
    protected void step1() {
        System.out.println("步骤1：通用处理");
    }

    // 抽象步骤（子类必须实现）
    protected abstract void step2();

    protected abstract void step3();

    // 钩子方法（子类可选择重写）
    protected boolean isEnable() {
        return true;
    }
}

// 具体子类A
public class ConcreteClassA extends AbstractClass {
    @Override
    protected void step2() {
        System.out.println("子类A：步骤2处理");
    }

    @Override
    protected void step3() {
        System.out.println("子类A：步骤3处理");
    }
}

// 具体子类B
public class ConcreteClassB extends AbstractClass {
    @Override
    protected void step2() {
        System.out.println("子类B：步骤2处理");
    }

    @Override
    protected void step3() {
        System.out.println("子类B：步骤3处理");
    }

    @Override
    protected boolean isEnable() {
        return false;  // 钩子方法重写
    }
}

// 使用
AbstractClass a = new ConcreteClassA();
a.templateMethod();
```

### 使用场景

- Spring Data JdbcTemplate
- Spring Transaction事务模板
- Servlet中service方法

---

## 观察者模式

### 概念

定义对象间的一种一对多的依赖关系，当一个对象状态发生改变时，所有依赖它的对象都会收到通知并自动更新。

### 实现

```java
import java.util.ArrayList;
import java.util.List;

// 观察者接口
public interface Observer {
    void update(String message);
}

// 具体观察者
public class ConcreteObserver implements Observer {
    private String name;

    public ConcreteObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " 收到通知：" + message);
    }
}

// 主题接口
public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyAllObservers();
}

// 具体主题
public class ConcreteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String message;

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void setMessage(String message) {
        this.message = message;
        notifyAllObservers();
    }
}

// 使用
ConcreteSubject subject = new ConcreteSubject();
Observer o1 = new ConcreteObserver("观察者1");
Observer o2 = new ConcreteObserver("观察者2");

subject.attach(o1);
subject.attach(o2);
subject.setMessage("主题状态更新了！");
```

### Java内置实现

```java
import java.util.Observable;
import java.util.Observer;

// 主题（已过时，Java 9推荐使用PropertyChangeSupport）
public class NewsAgency extends Observable {
    private String news;

    public void setNews(String news) {
        this.news = news;
        setChanged();
        notifyObservers(news);
    }
}

// 观察者
public class NewsChannel implements Observer {
    private String news;

    @Override
    public void update(Observable o, Object arg) {
        this.news = (String) arg;
        System.out.println("收到新闻：" + news);
    }
}

// 使用
NewsAgency agency = new NewsAgency();
NewsChannel channel = new NewsChannel();
agency.addObserver(channel);
agency.setNews("突发新闻！");
```

### 使用场景

- 消息推送
- 事件监听
- MVC模式中的Model-View通信

---

## 责任链模式

### 概念

使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。

### 实现

```java
// 处理者抽象类
public abstract class Handler {
    protected Handler nextHandler;

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public final void handleRequest(Request request) {
        if (canHandle(request)) {
            doHandle(request);
        } else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        } else {
            System.out.println("没有处理器能处理该请求");
        }
    }

    protected abstract boolean canHandle(Request request);
    protected abstract void doHandle(Request request);
}

// 请求类
public class Request {
    private String level;  // "low", "medium", "high"

    public Request(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}

// 具体处理器A
public class HandlerA extends Handler {
    @Override
    protected boolean canHandle(Request request) {
        return "low".equals(request.getLevel());
    }

    @Override
    protected void doHandle(Request request) {
        System.out.println("处理器A处理了请求：" + request.getLevel());
    }
}

// 具体处理器B
public class HandlerB extends Handler {
    @Override
    protected boolean canHandle(Request request) {
        return "medium".equals(request.getLevel());
    }

    @Override
    protected void doHandle(Request request) {
        System.out.println("处理器B处理了请求：" + request.getLevel());
    }
}

// 使用
Handler h1 = new HandlerA();
Handler h2 = new HandlerB();
h1.setNextHandler(h2);

Request req1 = new Request("low");
Request req2 = new Request("medium");
Request req3 = new Request("high");

h1.handleRequest(req1);  // 处理器A处理
h1.handleRequest(req2);  // 处理器B处理
h1.handleRequest(req3);  // 没有处理器
```

### 使用场景

- Servlet Filter过滤器链
- Spring Security过滤器链
- 日志切面链

---

## 命令模式

### 概念

将请求封装成对象，从而允许用不同的请求参数化客户，对请求排队或者记录请求日志，以及支持可撤销的操作。

### 实现

```java
// 命令接口
public interface Command {
    void execute();
    void undo();
}

// 具体命令
public class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

// 接收者
public class Light {
    public void on() {
        System.out.println("灯打开了");
    }

    public void off() {
        System.out.println("灯关闭了");
    }
}

// 调用者
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }

    public void pressUndo() {
        command.undo();
    }
}

// 使用
Light light = new Light();
Command lightOn = new LightOnCommand(light);

RemoteControl remote = new RemoteControl();
remote.setCommand(lightOn);
remote.pressButton();   // 灯打开
remote.pressUndo();     // 灯关闭
```

### 使用场景

- 任务队列
- 撤销/重做功能
- 宏命令

---

## 设计原则

### SOLID原则

| 原则 | 含义 |
|------|------|
| **S** 单一职责 | 一个类只负责一件事 |
| **O** 开闭原则 | 对扩展开放，对修改关闭 |
| **L** 里氏替换 | 子类可以替换父类 |
| **I** 接口隔离 | 客户端不应该依赖它不需要的接口 |
| **D** 依赖倒置 | 依赖抽象而非具体实现 |

### 其他重要原则

- **DRY** (Don't Repeat Yourself)：不重复原则
- **KISS** (Keep It Simple, Stupid)：保持简单
- **YAGNI** (You Aren't Gonna Need It)：不要过度设计
- **高内聚低耦合**：模块内部高内聚，模块之间低耦合

---

## 参考链接

- 图解设计模式：https://design-patterns.readthedocs.io/zh_CN/latest/index.html
- 策略模式7种：https://www.kancloud.cn/imnotdown1019/java_core_full/1005228
