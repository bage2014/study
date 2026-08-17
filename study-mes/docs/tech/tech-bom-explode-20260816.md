# BOM 展开算法实现

> 创建时间：2026-08-16
> 模块：tech

BOM 多层展开、用量计算、虚拟件穿透、循环检测的工程实现思路。

## 一、问题定义

给定父物料 + 数量，递归分解为所有底层采购件清单（含用量）。

### 输入
```
parentItemId = A
quantity = 10
bomVersion = (有效版本)
```

### 期望输出
```
X  20  (10 × 2)
Y  30  (10 × 3)
Z  50  (10 × 5)
W  40  (10 × 4)
```

## 二、核心规则

| 规则 | 说明 |
|------|------|
| 多层递归 | 展开到采购件（无 BOM 或不再分解） |
| 用量计算 | 子件用量 = 父件数量 × 行用量 × (1 + 损耗率) |
| 虚拟件穿透 | 虚拟件不入库，需求直接传到下层 |
| 有效性 | 取当前有效的 BOM 版本 |
| 循环检测 | 防止 A→B→A 死循环 |
| 同料合并 | 同一采购件跨路径合并数量 |
| 替代料 | 主料缺货时按规则切换（展开阶段通常只展开主料） |

## 三、数据结构

### BOM 查询接口

```java
public interface BomRepository {
    Optional<Bom> findEffectiveBom(Long parentItemId, LocalDate date);
    List<BomLine> findLines(Long bomId);
}
```

### 展开结果

```java
public class BomNode {
    Long itemId;
    BigDecimal quantity;       // 含损耗
    BigDecimal rawQuantity;    // 不含损耗
    int level;                 // 层级
    Long parentItemId;
    List<BomNode> children;
}

public class FlatBomEntry {
    Long itemId;
    BigDecimal totalQuantity;  // 合并后总量
    String path;               // 展开路径（A/B/X）
}
```

## 四、递归展开算法

```java
public class BomExploder {

    public List<FlatBomEntry> explode(Long rootItemId, BigDecimal qty, LocalDate date) {
        Map<Long, BigDecimal> aggregated = new HashMap<>();
        Set<Long> visiting = new HashSet<>();   // 循环检测
        List<FlatBomEntry> result = new ArrayList<>();

        doExplode(rootItemId, qty, date, visiting, aggregated, rootItemId + "");

        aggregated.forEach((itemId, total) ->
            result.add(new FlatBomEntry(itemId, total)));
        return result;
    }

    private void doExplode(Long parentId, BigDecimal qty, LocalDate date,
                           Set<Long> visiting, Map<Long, BigDecimal> agg, String path) {

        // 循环检测
        if (!visiting.add(parentId)) {
            throw new BomCycleException("检测到 BOM 循环引用: " + path);
        }

        Optional<Bom> bomOpt = bomRepository.findEffectiveBom(parentId, date);
        if (bomOpt.isEmpty()) {
            // 叶子节点（采购件），累加需求
            agg.merge(parentId, qty, BigDecimal::add);
            visiting.remove(parentId);
            return;
        }

        List<BomLine> lines = bomRepository.findLines(bomOpt.get().getId());
        for (BomLine line : lines) {
            // 用量 = 父数量 × 行用量 × (1 + 损耗率)
            BigDecimal childQty = qty
                .multiply(line.getQuantity())
                .multiply(BigDecimal.ONE.add(line.getLossRate()));

            if (line.isPhantom()) {
                // 虚拟件：不入库，直接穿透到下层
                doExplode(line.getChildItemId(), childQty, date, visiting, agg,
                          path + "/" + line.getChildItemId());
            } else {
                // 非虚拟件：先累加自身需求，再继续展开下层
                agg.merge(line.getChildItemId(), childQty, BigDecimal::add);
                doExplode(line.getChildItemId(), childQty, date, visiting, agg,
                          path + "/" + line.getChildItemId());
            }
        }
        visiting.remove(parentId);
    }
}
```

### 关键点
- `visiting` 集合做循环检测，进入前 add，退出时 remove。
- 虚拟件穿透：不累加自身，直接递归下层。
- 同料合并：用 Map 累加。
- 叶子判定：无有效 BOM 视为采购件。

## 五、迭代展开（避免栈溢出）

深层 BOM（>1000 层）递归会栈溢出，改迭代：

```java
public List<FlatBomEntry> explodeIterative(Long root, BigDecimal qty, LocalDate date) {
    Map<Long, BigDecimal> agg = new HashMap<>();
    Deque<ExplodeTask> stack = new ArrayDeque<>();
    Set<Long> pathSet = new HashSet<>();

    stack.push(new ExplodeTask(root, qty, root + ""));

    while (!stack.isEmpty()) {
        ExplodeTask task = stack.pop();

        if (!pathSet.add(task.itemId)) {
            throw new BomCycleException("循环: " + task.path);
        }

        Optional<Bom> bom = bomRepository.findEffectiveBom(task.itemId, date);
        if (bom.isEmpty()) {
            agg.merge(task.itemId, task.qty, BigDecimal::add);
            pathSet.remove(task.itemId);
            continue;
        }

        List<BomLine> lines = bomRepository.findLines(bom.get().getId());
        for (BomLine line : lines) {
            BigDecimal childQty = task.qty
                .multiply(line.getQuantity())
                .multiply(BigDecimal.ONE.add(line.getLossRate()));

            if (!line.isPhantom()) {
                agg.merge(line.getChildItemId(), childQty, BigDecimal::add);
            }
            stack.push(new ExplodeTask(line.getChildItemId(), childQty,
                                       task.path + "/" + line.getChildItemId()));
        }
        pathSet.remove(task.itemId);
    }
    // 转 FlatBomEntry ...
}
```

## 六、有效性查询优化

### 问题
- 每个节点查一次 BOM 版本 + 行，N 个节点 = 2N 次 DB 查询。
- 深层 BOM 性能差。

### 优化方案

**1. 批量预取**

先扫一遍把所有相关 BOM 一次性查出来：

```java
// 第一步：广度优先扫描所有涉及物料
Set<Long> allItems = scanAllItems(root, date);
// 第二步：一次性查全部 BOM
Map<Long, Bom> bomMap = bomRepository.findEffectiveBoms(allItems, date);
Map<Long, List<BomLine>> lineMap = bomRepository.findLinesByBomIds(bomMap.values());
// 第三步：内存中展开
```

**2. 缓存**

- 已发布 BOM 不常变，缓存到 Redis。
- Key: `bom:effective:{itemId}:{date}`，TTL 1h。
- BOM 变更时主动失效缓存。

**3. 物化视图**

报表场景预计算 BOM 展开，存物化视图，定时刷新。

## 七、循环检测的健壮性

### 创建时校验（防患于未然）

```java
public void validateNoCycle(Long parentId, Long childId) {
    // 反向追溯 child 的所有上层，看是否回到 parent
    Set<Long> visited = new HashSet<>();
    Deque<Long> stack = new ArrayDeque<>();
    stack.push(childId);
    while (!stack.isEmpty()) {
        Long cur = stack.pop();
        if (!visited.add(cur)) continue;
        if (cur.equals(parentId)) {
            throw new BomCycleException("新增将形成循环");
        }
        // 查 cur 的所有父件
        bomRepository.findParents(cur).forEach(stack::push);
    }
}
```

### 运行时兜底
- 展开时 `visiting` 集合兜底。
- 限制最大展开深度（如 50 层），超出报错。

## 八、替代料处理

展开阶段通常**只展开主料**，替代料在 MRP/配料阶段处理：

### MRP 阶段替代料逻辑

```
1. 计算主料净需求
2. 查主料可用库存
3. 缺口部分 → 查替代料
4. 替代料按优先级 + 库存分配
5. 生成采购/生产建议
```

### 配料阶段替代料
- 工单配料时主料不足，按替代规则切换。
- 切换需记录（追溯）。

## 九、BOM 反查（Where-Used）

查询某物料用于哪些父件：

```java
public List<WhereUsedEntry> findWhereUsed(Long itemId) {
    // 反向遍历 BOM 行
    List<BomLine> lines = bomLineMapper.findByChildItemId(itemId);
    return lines.stream()
        .map(line -> new WhereUsedEntry(
            line.getBom().getParentItemId(),
            line.getQuantity(),
            line.getLossRate()))
        .collect(toList());
}

// 多层反查（递归向上）
public List<WhereUsedEntry> findWhereUsedMultiLevel(Long itemId) {
    // 类似展开，但方向相反
}
```

用于变更影响评估。

## 十、性能与扩展

| 场景 | 优化 |
|------|------|
| 实时展开（工单配料） | 批量预取 + 缓存，目标 < 200ms |
| MRP 批量运算 | 离线任务 + 物化视图 |
| 复杂产品（万级节点） | 分页展开 + 流式处理 |
| 多工厂 BOM | 按工厂维度缓存 |

## 十一、测试要点

| 测试 | 说明 |
|------|------|
| 单层展开 | 基础正确性 |
| 多层嵌套 | 递归正确性 |
| 虚拟件穿透 | 不累加虚拟件，下层用量正确 |
| 损耗率 | 用量含损耗 |
| 同料合并 | 跨路径合并 |
| 循环检测 | A→B→A 抛异常 |
| 深层 BOM | 不栈溢出 |
| 有效性 | 取正确版本 |

## 十二、相关文档

- [核心数据模型](./tech-data-model-20260816.md)
- [BOM 概述](../bom/bom-overview-20260816.md)
- [BOM 业务流程](../bom/bom-workflow-20260816.md)
