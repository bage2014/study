# BOM 管理服务实现

> 创建时间：2026-09-06
> 模块：concepts

BOM 管理的 Java 服务层实现：CRUD、版本控制、变更流程、校验、展开。展开算法见 [tech-bom-explode](../tech/tech-bom-explode-20260816.md)，本文聚焦管理服务。

## 一、服务边界

```
BomApplicationService（应用服务）
  ├─ BomRepository（持久化）
  ├─ BomValidator（校验）
  ├─ BomExploder（展开）
  ├─ WorkflowService（审批）
  └─ EventPublisher（事件）
```

## 二、领域模型

```java
public class Bom extends BaseAggregate {
    private Long id;
    private Long parentItemId;       // 父物料
    private BomType type;            // EBOM/MBOM/SBOM
    private String version;          // A/B/C
    private BomStatus status;        // DRAFT/IN_REVIEW/PUBLISHED/SUPERSEDED/OBSOLETE
    private LocalDate effectFrom;
    private LocalDate effectTo;
    private List<BomLine> lines;

    // 新增 BOM 行
    public void addLine(BomLine line) {
        Assert.isTrue(isDraft(), "仅草稿可编辑");
        // 校验：子物料已发布、无循环、用量合法
        validator.validateLine(this, line);
        lines.add(line);
    }

    // 提交审核
    public void submitReview() {
        Assert.notEmpty(lines, "BOM 行不能为空");
        this.status = BomStatus.IN_REVIEW;
    }

    // 审核通过 → 发布
    public void publish() {
        this.status = BomStatus.PUBLISHED;
    }

    // 驳回
    public void reject() {
        this.status = BomStatus.DRAFT;
    }

    // 失效
    public void supersede() {
        this.status = BomStatus.SUPERSEDED;
    }
}

public class BomLine extends BaseEntity {
    private Long bomId;
    private Long childItemId;
    private BigDecimal quantity;      // 用量
    private BigDecimal lossRate;      // 损耗率
    private Long operationId;         // 工序（MBOM）
    private String positionNo;        // 位置号
    private boolean phantom;          // 是否虚拟件
    private Long substituteId;        // 替代料
    private LocalDate effectFrom;
    private LocalDate effectTo;
}
```

## 三、应用服务实现

### 1. 创建 BOM

```java
@Service
public class BomApplicationService {

    @Autowired private BomRepository bomRepo;
    @Autowired private BomValidator validator;
    @Autowired private ItemRepository itemRepo;
    @Autowired private WorkflowService workflowService;
    @Autowired private EventPublisher eventPublisher;

    @Transactional
    public Long create(BomCreateCmd cmd) {
        // 校验父物料存在且已发布
        Item parent = itemRepo.findOrThrow(cmd.getParentItemId());
        Assert.isTrue(parent.isReleased(), "父物料未发布，不能创建 BOM");

        // 生成初始版本
        String version = "A";
        Bom bom = Bom.create(cmd.getParentItemId(), cmd.getType(), version);
        bomRepo.save(bom);
        return bom.getId();
    }
```

### 2. 维护 BOM 行

```java
    @Transactional
    public void addLine(Long bomId, BomLineAddCmd cmd) {
        Bom bom = bomRepo.findOrThrow(bomId);
        // 领域校验（子物料、循环、用量）
        BomLine line = BomLine.of(cmd);
        bom.addLine(line);
        bomRepo.saveLine(line);
    }

    @Transactional
    public void updateLine(Long lineId, BomLineUpdateCmd cmd) {
        BomLine line = bomLineRepo.findOrThrow(lineId);
        Bom bom = bomRepo.findOrThrow(line.getBomId());
        Assert.isTrue(bom.isDraft(), "仅草稿可修改");
        line.update(cmd);
        bomLineRepo.save(line);
    }

    @Transactional
    public void removeLine(Long lineId) {
        BomLine line = bomLineRepo.findOrThrow(lineId);
        Bom bom = bomRepo.findOrThrow(line.getBomId());
        Assert.isTrue(bom.isDraft(), "仅草稿可删除");
        bomLineRepo.delete(lineId);
    }
```

### 3. 提交审核与发布

```java
    @Transactional
    public void submitReview(Long bomId) {
        Bom bom = bomRepo.findOrThrow(bomId);
        bom.submitReview();
        bomRepo.save(bom);
        // 启动 BOM 审批流程
        workflowService.start("BOM_REVIEW", new FlowBizRef("BOM", bomId));
    }

    // 审批通过回调
    @EventListener
    public void onReviewApproved(ReviewApprovedEvent event) {
        if (!"BOM".equals(event.getBizType())) return;
        Bom bom = bomRepo.findOrThrow(event.getBizId());
        bom.publish();
        bomRepo.save(bom);
        // 旧版本失效
        bomRepo.supersedePrevious(bom.getParentItemId(), bom.getVersion());
        // 同步 ERP/MES
        eventPublisher.publish(new BomReleasedEvent(bom));
    }

    @EventListener
    public void onReviewRejected(ReviewRejectedEvent event) {
        if (!"BOM".equals(event.getBizType())) return;
        Bom bom = bomRepo.findOrThrow(event.getBizId());
        bom.reject();
        bomRepo.save(bom);
    }
```

### 4. BOM 变更（ECN）

```java
    @Transactional
    public Long createChange(BomChangeCmd cmd) {
        Bom current = bomRepo.findPublished(cmd.getParentItemId());
        // 基于当前版本复制新版本
        Bom newBom = current.copyToNewVersion();
        newBom.setStatus(DRAFT);
        bomRepo.save(newBom);
        // 复制 BOM 行
        bomLineRepo.copyLines(current.getId(), newBom.getId());
        return newBom.getId();
    }

    // 变更生效
    @Transactional
    public void effectChange(Long bomId, EffectStrategy strategy) {
        Bom bom = bomRepo.findOrThrow(bomId);
        Assert.isTrue(bom.isPublished(), "BOM 未发布");
        // 按策略设置生效
        switch (strategy) {
            case IMMEDIATE -> bom.setEffectFrom(LocalDate.now());
            case DATE -> bom.setEffectFrom(cmd.getEffectDate());
            case LOT_EXHAUST -> bom.setEffectFrom(findLotExhaustDate(bom.getParentItemId()));
        }
        bomRepo.save(bom);
        eventPublisher.publish(new BomChangedEvent(bom, strategy));
    }
```

### 5. BOM 反查（Where-Used）

```java
    public List<WhereUsedEntry> findWhereUsed(Long itemId) {
        // 单层反查
        List<BomLine> lines = bomLineRepo.findByChildItemId(itemId);
        return lines.stream()
            .map(l -> {
                Bom bom = bomRepo.findOrThrow(l.getBomId());
                return new WhereUsedEntry(bom.getParentItemId(), l.getQuantity(), bom.getVersion());
            })
            .collect(toList());
    }

    // 多层反查
    public List<WhereUsedEntry> findWhereUsedMultiLevel(Long itemId) {
        List<WhereUsedEntry> result = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(itemId);
        while (!stack.isEmpty()) {
            Long cur = stack.pop();
            if (!visited.add(cur)) continue;
            List<WhereUsedEntry> parents = findWhereUsed(cur);
            for (WhereUsedEntry p : parents) {
                result.add(p);
                stack.push(p.getParentItemId());
            }
        }
        return result;
    }
```

### 6. BOM 版本比较

```java
    public BomDiff compare(Long bomId1, Long bomId2) {
        List<BomLine> lines1 = bomLineRepo.findByBomId(bomId1);
        List<BomLine> lines2 = bomLineRepo.findByBomId(bomId2);

        Map<Long, BomLine> map1 = lines1.stream()
            .collect(toMap(BomLine::getChildItemId, Function.identity()));
        Map<Long, BomLine> map2 = lines2.stream()
            .collect(toMap(BomLine::getChildItemId, Function.identity()));

        BomDiff diff = new BomDiff();
        // 新增
        map2.keySet().stream().filter(k -> !map1.containsKey(k))
            .forEach(k -> diff.addAdded(map2.get(k)));
        // 删除
        map1.keySet().stream().filter(k -> !map2.containsKey(k))
            .forEach(k -> diff.addRemoved(map1.get(k)));
        // 修改
        map1.keySet().stream().filter(map2::containsKey)
            .forEach(k -> {
                if (!map1.get(k).equals(map2.get(k))) {
                    diff.addModified(map1.get(k), map2.get(k));
                }
            });
        return diff;
    }
```

## 四、校验器

```java
@Component
public class BomValidator {

    @Autowired private ItemRepository itemRepo;
    @Autowired private BomRepository bomRepo;

    public void validateLine(Bom bom, BomLine line) {
        // 1. 子物料存在且已发布
        Item child = itemRepo.findOrThrow(line.getChildItemId());
        Assert.isTrue(child.isReleased(), "子物料未发布: " + child.getCode());

        // 2. 循环引用检测
        Assert.isTrue(!hasCycle(bom.getParentItemId(), line.getChildItemId()),
            "检测到 BOM 循环引用");

        // 3. 用量合法
        Assert.isTrue(line.getQuantity().compareTo(BigDecimal.ZERO) > 0, "用量必须大于 0");

        // 4. 重复行校验
        Assert.isTrue(!bom.hasChild(line.getChildItemId(), line.getOperationId()),
            "BOM 中已存在相同子件+工序");
    }

    private boolean hasCycle(Long parentId, Long childId) {
        // 反查 childId 的所有上层，看是否回到 parentId
        Set<Long> visited = new HashSet<>();
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(childId);
        while (!stack.isEmpty()) {
            Long cur = stack.pop();
            if (!visited.add(cur)) continue;
            if (cur.equals(parentId)) return true;
            bomRepo.findParents(cur).forEach(stack::push);
        }
        return false;
    }
}
```

## 五、缓存策略

已发布 BOM 变更少，缓存：

```java
@Cacheable(value = "bom", key = "#itemId + '_' + #date")
public Bom findEffectiveBom(Long itemId, LocalDate date) {
    return bomRepo.findEffective(itemId, date);
}

// BOM 变更时清缓存
@EventListener
public void onBomChanged(BomChangedEvent event) {
    cacheManager.getCache("bom").clear();
}
```

## 六、关键设计要点

| 要点 | 说明 |
|------|------|
| 版本不可变 | 已发布不修改，变更走新版本 |
| 同物料单版本生效 | 同一时刻仅一个 PUBLISHED |
| 校验前置 | 添加行时强校验（物料、循环、用量） |
| 审批驱动发布 | 状态机 + 工作流 |
| 缓存已发布 BOM | 减少 DB 压力，变更时清缓存 |
| 版本比较 | 支撑变更评审 |

## 七、相关文档

- [BOM 概述](../bom/bom-overview-20260816.md)
- [BOM 类型](../bom/bom-types-20260816.md)
- [BOM 业务流程](../bom/bom-workflow-20260816.md)
- [BOM 展开算法](../tech/tech-bom-explode-20260816.md)
- [审批工作流](../tech/tech-workflow-20260816.md)
