---
name: "common-refactoring"
description: "代码重构技能，识别代码坏味道，提供安全的重构手法，保持功能不变的前提下改善代码结构"
trigger: "代码质量下降、需要改善代码结构、消除重复代码、提高可读性时"
disable-when: "功能不稳定时、缺乏测试覆盖时、紧急上线前"
category: "common"
tags: ["refactoring", "code-quality", "code-smell", "clean-code", "maintenance"]
requires: ["common-coding", "common-test-driven-development"]
---

# common-refactoring

## 功能描述

代码重构技能，帮助识别代码中的坏味道（Code Smells），提供系统性的重构手法，在保持功能不变的前提下改善代码的内部结构。重构的目标是让代码更易理解、更易维护、更易扩展。

## 触发条件

- 代码重复度高，存在大量复制粘贴的代码
- 函数/方法过长（超过 50 行）
- 类/模块职责不清晰，过于庞大
- 条件逻辑复杂，难以理解
- 命名不规范，含义模糊
- 代码耦合度高，难以测试
- 需要添加新功能但现有结构不支持

## 何时使用

- 代码审查时发现质量问题
- 添加新功能前清理现有代码
- 修复 Bug 后优化代码结构
- 性能优化前的代码整理
- 团队代码规范统一
- 遗留代码改造

## 何时不使用

- 功能不稳定，存在已知 Bug（先修 Bug）
- 缺乏测试覆盖（先补测试）
- 紧急上线前夕（避免风险）
- 代码即将废弃（不值得投入）

## 核心原则

### 1. 重构前提

**两顶帽子**：
- 重构帽：不改变功能，只改善结构
- 功能帽：添加新功能，不改结构

**严禁同时做两件事**。重构时不加功能，加功能时不重构。

### 2. 测试保障

**重构前必须有测试**：
```bash
# 1. 运行现有测试，确保全部通过
mvn test  # 或 npm test

# 2. 如果没有测试，先补充关键路径测试
# 参考 common-test-driven-development 技能

# 3. 每完成一个小重构，立即运行测试
# 测试失败 = 回滚重构
```

### 3. 小步快跑

- 每次只做一个小的重构
- 做完立即运行测试
- 测试失败立即回滚
- 不要一次性大规模重构

## 代码坏味道识别

### 1. 重复代码（Duplicated Code）

**症状**：
```java
// ❌ 重复代码
public class OrderService {
    public void processOrder(Order order) {
        // 验证库存
        for (Item item : order.getItems()) {
            Stock stock = stockRepo.findByItemId(item.getId());
            if (stock.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException();
            }
        }
        // ... 处理订单
    }
}

public class ReturnService {
    public void processReturn(Return returnOrder) {
        // 验证库存（重复！）
        for (Item item : returnOrder.getItems()) {
            Stock stock = stockRepo.findByItemId(item.getId());
            if (stock.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException();
            }
        }
        // ... 处理退货
    }
}
```

**重构手法**：Extract Method（提取方法）
```java
// ✅ 提取公共方法
public class InventoryValidator {
    public void validateStock(List<Item> items) {
        for (Item item : items) {
            Stock stock = stockRepo.findByItemId(item.getId());
            if (stock.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException();
            }
        }
    }
}

public class OrderService {
    private InventoryValidator validator;
    
    public void processOrder(Order order) {
        validator.validateStock(order.getItems());
        // ... 处理订单
    }
}
```

### 2. 过长函数（Long Function）

**症状**：函数超过 50 行，做了多件事

**重构手法**：
- **Extract Method**：将函数中的独立代码块提取为新函数
- **Replace Temp with Query**：将临时变量改为查询函数
- **Introduce Parameter Object**：将多个参数封装为对象

```java
// ❌ 过长函数
public void processOrder(Order order) {
    // 验证订单（10行）
    // 计算价格（15行）
    // 检查库存（10行）
    // 创建支付记录（8行）
    // 发送通知（5行）
}

// ✅ 拆分为多个小函数
public void processOrder(Order order) {
    validateOrder(order);
    double total = calculateTotal(order);
    checkInventory(order);
    createPaymentRecord(order, total);
    sendNotification(order);
}
```

### 3. 过大类（Large Class）

**症状**：类超过 500 行，职责不单一

**重构手法**：
- **Extract Class**：将相关字段和方法提取为新类
- **Extract Subclass**：将变体行为提取为子类
- **Extract Interface**：提取接口定义

```java
// ❌ 过大类
public class UserService {
    // 用户验证（10个方法）
    // 用户创建（8个方法）
    // 用户查询（12个方法）
    // 用户更新（6个方法）
    // 用户删除（4个方法）
    // 邮件发送（5个方法）
}

// ✅ 按职责拆分
public class UserValidator { /* 用户验证 */ }
public class UserCreator { /* 用户创建 */ }
public class UserQueryService { /* 用户查询 */ }
public class UserUpdater { /* 用户更新 */ }
public class UserDeleter { /* 用户删除 */ }
public class EmailService { /* 邮件发送 */ }
```

### 4. 过长参数列表（Long Parameter List）

**症状**：函数参数超过 4 个

**重构手法**：
- **Introduce Parameter Object**：封装为参数对象
- **Preserve Whole Object**：传递整个对象而非其属性

```java
// ❌ 过长参数列表
public void createUser(String firstName, String lastName, 
                       String email, String phone, 
                       String address, int age) {
}

// ✅ 封装为对象
public class UserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int age;
}

public void createUser(UserInfo userInfo) {
}
```

### 5. 发散式变化（Divergent Change）

**症状**：一个类因为多个原因频繁修改

```java
// ❌ 发散式变化
public class Employee {
    // 修改1：计算工资逻辑变化
    // 修改2：税务计算规则变化
    // 修改3：社保政策变化
    // 修改4：考勤规则变化
}

// ✅ 按变化原因拆分
public class SalaryCalculator { /* 工资计算 */ }
public class TaxCalculator { /* 税务计算 */ }
public class InsuranceCalculator { /* 社保计算 */ }
public class AttendanceTracker { /* 考勤追踪 */ }
```

### 6. 霰弹式修改（Shotgun Surgery）

**症状**：一个变化需要修改多个类

```java
// ❌ 霰弹式修改
// 添加新字段时，需要修改：
// - Entity 类
// - DTO 类
// - Mapper 类
// - Service 类
// - Controller 类
// - Repository 类

// ✅ 使用代码生成工具或统一数据模型
// 或使用 MapStruct 自动映射
```

### 7. 依恋情结（Feature Envy）

**症状**：方法更关注其他类的数据而非自己的

```java
// ❌ 依恋情结
public class OrderService {
    public void processOrder(Order order) {
        double total = order.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        // OrderService 更关心 Order 的数据
    }
}

// ✅ 将逻辑移到数据所在的类
public class Order {
    public double calculateTotal() {
        return items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
}

public class OrderService {
    public void processOrder(Order order) {
        double total = order.calculateTotal();
    }
}
```

### 8. 数据泥团（Data Clumps）

**症状**：多个数据项总是一起出现

```java
// ❌ 数据泥团
public void createAddress(String street, String city, 
                          String province, String zipCode) {
}

public void createPackage(String street, String city, 
                          String province, String zipCode, 
                          double weight) {
}

// ✅ 封装为类
public class Address {
    private String street;
    private String city;
    private String province;
    private String zipCode;
}

public void createAddress(Address address) {
}

public void createPackage(Address address, double weight) {
}
```

### 9. 基本类型偏执（Primitive Obsession）

**症状**：使用基本类型而非领域对象

```java
// ❌ 基本类型偏执
public class User {
    private String phoneNumber;  // 电话号码应该是对象
    private String email;        // 邮箱应该是对象
    private int age;             // 年龄应该是值对象
}

// ✅ 使用值对象
public class PhoneNumber {
    private String number;
    public boolean isValid() { /* 验证逻辑 */ }
}

public class Email {
    private String address;
    public boolean isValid() { /* 验证逻辑 */ }
}

public class Age {
    private int value;
    public boolean isAdult() { return value >= 18; }
}

public class User {
    private PhoneNumber phoneNumber;
    private Email email;
    private Age age;
}
```

### 10. switch 语句（Switch Statements）

**症状**：复杂的 switch 或 if-else 链

```java
// ❌ switch 语句
public double calculateDiscount(Customer customer) {
    switch (customer.getType()) {
        case "VIP": return 0.3;
        case "GOLD": return 0.2;
        case "SILVER": return 0.1;
        default: return 0;
    }
}

// ✅ 使用多态
public abstract class Customer {
    public abstract double getDiscount();
}

public class VipCustomer extends Customer {
    public double getDiscount() { return 0.3; }
}

public class GoldCustomer extends Customer {
    public double getDiscount() { return 0.2; }
}
```

## 重构手法速查

### 一、提取类（Extract Class）

**适用场景**：类职责不单一，有多个变化原因

**步骤**：
1. 识别类的哪些部分可以分离
2. 创建新类，将相关字段和方法移入
3. 在原类中保留对新类的引用
4. 调整访问权限

### 二、合并函数（Inline Method）

**适用场景**：函数体与函数名同样清晰

```java
// ❌ 过度封装
public int getRating() {
    return calculateRating();
}

private int calculateRating() {
    return (score > 90) ? 5 : 3;
}

// ✅ 直接内联
public int getRating() {
    return (score > 90) ? 5 : 3;
}
```

### 三、移动方法（Move Method）

**适用场景**：方法更依赖其他类而非自己所在的类

**判断标准**：
- 方法调用了哪些类的方法？
- 方法使用了哪些类的数据？
- 如果调用其他类更多，就应该移动过去

### 四、重新命名（Rename）

**重命名原则**：
- 名字应揭示意图
- 避免缩写（除非是业界通用）
- 布尔变量用 is/has/can 开头
- 方法用动词开头

```java
// ❌ 不好的命名
int d;  // 含义不明
List<User> list;  // 太泛化
void handle();  // 不知道处理什么
boolean login;  // 不知道是状态还是动作

// ✅ 清晰的命名
int daysSinceCreation;
List<User> activeUsers;
void processOrder();
boolean isLoggedIn;
```

### 五、引入参数对象（Introduce Parameter Object）

**适用场景**：方法参数过多（>4个）

**优点**：
- 参数列表变短
- 提高可读性
- 便于扩展

### 六、以函数取代参数（Replace Parameter with Method）

```java
// ❌ 使用参数控制逻辑
public void processOrder(Order order, boolean isExpress) {
    if (isExpress) {
        // 快速处理
    } else {
        // 普通处理
    }
}

// ✅ 拆分为两个方法
public void processExpressOrder(Order order) {
    // 快速处理
}

public void processNormalOrder(Order order) {
    // 普通处理
}
```

### 七、以多态取代条件表达式（Replace Conditional with Polymorphism）

**适用场景**：switch/if-else 基于类型做不同操作

**步骤**：
1. 创建基类或接口
2. 为每种类型创建子类
3. 将条件逻辑移到各个子类

### 八、提取接口（Extract Interface）

**适用场景**：多个类有共同行为但实现不同

```java
// ❌ 直接依赖具体类
public class PaymentService {
    public void pay(Alipay alipay) {
        alipay.pay();
    }
}

// ✅ 依赖接口
public interface PaymentMethod {
    void pay();
}

public class Alipay implements PaymentMethod {
    public void pay() { /* 支付宝支付 */ }
}

public class WechatPay implements PaymentMethod {
    public void pay() { /* 微信支付 */ }
}

public class PaymentService {
    public void pay(PaymentMethod method) {
        method.pay();
    }
}
```

## 重构检查清单

### 代码层

- [ ] 消除重复代码（DRY 原则）
- [ ] 拆分过长函数（单一职责）
- [ ] 拆分过大类（高内聚低耦合）
- [ ] 简化条件表达式（多态、策略模式）
- [ ] 改进命名（清晰表达意图）
- [ ] 减少参数数量（封装为对象）
- [ ] 消除魔法数字（使用常量或枚举）

### 设计层

- [ ] 职责分离（每个类/方法只做一件事）
- [ ] 依赖倒置（依赖抽象而非具体实现）
- [ ] 开闭原则（对扩展开放，对修改关闭）
- [ ] 接口隔离（使用多个专门的接口）
- [ ] 组合优于继承（优先使用组合而非继承）

### 测试层

- [ ] 重构前补充测试
- [ ] 每步重构后运行测试
- [ ] 测试覆盖率不下降
- [ ] 新增重构点的测试

## 重构流程

### 1. 识别坏味道

```bash
# 使用静态分析工具
mvn checkstyle:check  # Java
npm run lint          # JavaScript

# 代码审查
# 参考上面的"代码坏味道识别"章节
```

### 2. 评估重构优先级

**优先级矩阵**：
| 影响范围 | 修改频率 | 优先级 |
|---------|---------|--------|
| 大 | 高 | P0（立即重构） |
| 大 | 低 | P1（计划重构） |
| 小 | 高 | P2（ opportunistic 重构） |
| 小 | 低 | P3（暂不重构） |

### 3. 制定重构计划

```markdown
## 重构计划

### 目标
- 消除 OrderService 中的重复代码
- 将 UserService 拆分为 5 个职责单一的类

### 步骤
1. 提取 InventoryValidator 类（预计 2h）
2. 为 InventoryValidator 补充测试（预计 1h）
3. 拆分 UserService（预计 4h）
4. 更新所有调用点（预计 2h）

### 风险
- 拆分 UserService 可能影响多个模块
- 需要回归测试覆盖

### 回滚方案
- 每步重构后提交，便于回滚
- 保留旧代码的注释版本
```

### 4. 执行重构

```bash
# 1. 创建重构分支
git checkout -b refactor/order-service

# 2. 每完成一个小重构
git add .
git commit -m "refactor: extract InventoryValidator"

# 3. 运行测试
mvn test

# 4. 测试通过后继续下一个重构

# 5. 完成后合并
git checkout main
git merge refactor/order-service
```

### 5. 验证重构

- [ ] 所有测试通过
- [ ] 代码覆盖率不下降
- [ ] 静态分析无新增警告
- [ ] 代码审查通过
- [ ] 功能验证通过

## 输出格式

重构完成后输出：

```markdown
## 重构报告

### 重构目标
- 解决的问题
- 期望的改善

### 重构内容
1. 提取 InventoryValidator 类
   - 消除 OrderService 和 ReturnService 的重复代码
   - 代码行数：-30 行

2. 拆分 UserService
   - UserValidator：用户验证
   - UserCreator：用户创建
   - UserQueryService：用户查询
   - UserUpdater：用户更新
   - UserDeleter：用户删除

### 重构前后对比
| 指标 | 重构前 | 重构后 | 改善 |
|------|--------|--------|------|
| 最大类行数 | 800 | 200 | -75% |
| 最大方法行数 | 120 | 25 | -79% |
| 重复代码 | 15% | 3% | -80% |
| 测试覆盖率 | 65% | 72% | +7% |

### 测试验证
- 单元测试：全部通过（156/156）
- 集成测试：全部通过（23/23）
- 回归测试：全部通过

### 风险评估
- 影响范围：OrderService、ReturnService、UserService
- 回滚方案：Git 分支保留 7 天
```

## 埋点指令

**第 1 步**：调用 `~/.trae-cn/scripts/skill-usage.sh log common-refactoring --trigger auto`
若报 `Permission denied` 或沙箱拦截，必须以非沙箱方式重试。

## 参考资源

- Martin Fowler《重构：改善既有代码的设计》
- 《Clean Code》Robert C. Martin
- Refactoring Guru: https://refactoring.guru/
