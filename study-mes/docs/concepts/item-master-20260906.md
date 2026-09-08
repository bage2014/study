# 物料主数据管理

> 创建时间：2026-09-06
> 模块：concepts

物料（Item）主数据的概念、业务流程、Java 实现方案。物料是 MES/PLM/ERP 的核心主数据。

## 一、核心概念

### 1. 物料分类

| 类型 | 说明 | 示例 |
|------|------|------|
| 原材料 Raw Material | 采购直接使用 | 钢材、塑料粒子 |
| 半成品 Semi-Finished | 自制中间件 | 机加工件 |
| 成品 Finished Goods | 最终销售产品 | 整机 |
| 包材 Packaging | 包装材料 | 纸箱、标签 |
| 标准件 Standard | 通用件 | 螺栓、垫片 |
| 虚拟件 Phantom | 不入库，仅分组 | 紧固件包 |
| 辅料 Consumable | 辅助材料 | 油漆、胶水 |

### 2. 物料属性

| 属性 | 说明 |
|------|------|
| 编码 | 唯一业务键 |
| 名称/规格 | 描述 |
| 单位 | 基本计量单位 |
| 分类 | 物料分类树 |
| 批次管理 | 是否按批次追溯 |
| 序列号管理 | 是否单件追溯 |
| 效期管理 | 是否管理保质期 |
| 默认仓库/库位 | 存储位置 |
| 采购属性 | 供应商、采购提前期 |
| 生产属性 | 自制/外购、提前期 |
| 质量属性 | 检验方式（免检/抽检/全检） |
| 财务属性 | 计价方式、成本 |

### 3. 物料生命周期

```
[创建] → [研发中] → [试制] → [量产] → [受限] → [失效]
```

## 二、数据模型

```sql
item (物料主表)
├── id
├── code              物料编码（唯一）
├── name
├── spec              规格
├── unit_id           单位
├── category_id       分类
├── item_type         类型
├── status            生命周期状态
├── is_phantom        虚拟件
├── lot_managed       批次管理
├── serial_managed    序列号管理
├── shelf_life_days   保质期（天）
├── purchase_lead_time 采购提前期（天）
├── production_lead_time 生产提前期（天）
├── inspection_type   免检/抽检/全检
├── default_warehouse_id
├── default_location_id
├── create_by/time
└── ...

item_category (物料分类)
├── id
├── parent_id         树形
├── name
├── code
└── ...

item_supplier (物料-供应商)
├── id
├── item_id
├── supplier_id
├── priority          优先级
├── purchase_price
├── is_primary
└── ...

item_alternative (替代料)
├── id
├── item_id           主料
├── alt_item_id       替代料
├── priority
├── ratio             用量比
└── ...
```

## 三、业务流程

### 1. 物料创建流程

```
1. 申请物料编码（查重）
2. 填写属性（分类、单位、管理方式）
3. 关联供应商（采购件）
4. 提交审核
5. 审核通过 → 状态 [研发中]
6. 关联文档（图纸、规格书）
```

### 2. 物料发布流程

```
研发中 → 试制验证 → 发布 → [量产]
```

发布后才能被 BOM/工单引用。

### 3. 物料变更流程

```
变更申请 → 评估 → 审批 → 修改属性 → 通知下游（BOM/库存/采购）
```

### 4. 物料停用流程

```
[量产] → [受限]（不再新建单）→ 库存耗尽 → [失效]
```

## 四、Java 实现

### 领域模型

```java
public class Item extends BaseAggregate {
    private Long id;
    private String code;
    private String name;
    private ItemType type;
    private ItemStatus status;
    private boolean phantom;
    private boolean lotManaged;
    private boolean serialManaged;
    private Integer shelfLifeDays;

    // 发布
    public void release() {
        Assert.hasText(name, "名称不能为空");
        Assert.notNull(type, "类型不能为空");
        this.status = ItemStatus.RELEASED;
    }

    // 受限
    public void restrict() {
        this.status = ItemStatus.RESTRICTED;
    }

    // 失效
    public void obsolete() {
        this.status = ItemStatus.OBSOLETE;
    }

    public boolean isUsable() {
        return status == RELEASED || status == RESTRICTED;
    }
}
```

### 应用服务

```java
@Service
public class ItemApplicationService {

    @Autowired private ItemRepository itemRepo;
    @Autowired private ItemCategoryRepository categoryRepo;
    @Autowired private SequenceService seqService;
    @Autowired private DuplicateChecker duplicateChecker;

    // 创建物料
    @Transactional
    public Long create(ItemCreateCmd cmd) {
        // 1. 查重（推荐重用）
        List<Item> similar = duplicateChecker.findSimilar(cmd.getName(), cmd.getSpec(), cmd.getCategoryId());
        if (!similar.isEmpty()) {
            throw new BusinessException("存在相似物料，请确认是否重用: " + similar);
        }

        // 2. 生成编码
        String code = cmd.getCode() != null ? cmd.getCode()
            : generateCode(cmd.getCategoryId());

        // 3. 编码唯一性
        Assert.isTrue(!itemRepo.existsByCode(code), "物料编码已存在: " + code);

        // 4. 创建
        Item item = Item.create(code, cmd);
        itemRepo.save(item);
        return item.getId();
    }

    // 生成编码：分类前缀 + 流水号
    private String generateCode(Long categoryId) {
        ItemCategory cat = categoryRepo.findOrThrow(categoryId);
        String prefix = cat.getCodePrefix();
        String seq = seqService.next("ITEM_" + prefix, 6);
        return prefix + seq;
    }

    // 修改属性
    @Transactional
    public void update(Long itemId, ItemUpdateCmd cmd) {
        Item item = itemRepo.findOrThrow(itemId);
        Assert.isTrue(item.isUsable(), "物料已失效，不可修改");
        item.update(cmd);
        itemRepo.save(item);
        // 通知 BOM/库存/采购刷新
        eventPublisher.publish(new ItemChangedEvent(itemId));
    }

    // 发布
    @Transactional
    public void release(Long itemId) {
        Item item = itemRepo.findOrThrow(itemId);
        item.release();
        itemRepo.save(item);
        eventPublisher.publish(new ItemReleasedEvent(item));
    }

    // 失效
    @Transactional
    public void obsolete(Long itemId) {
        Item item = itemRepo.findOrThrow(itemId);
        // 校验：无有效 BOM 引用、无在制工单、无库存
        Assert.isTrue(!bomRepo.hasEffectiveBom(itemId), "存在有效 BOM 引用，不能失效");
        Assert.isTrue(workOrderRepo.hasActive(itemId), "存在在制工单，不能失效");
        Assert.isTrue(stockRepo.getQty(itemId).compareTo(ZERO) == 0, "存在库存，不能失效");
        item.obsolete();
        itemRepo.save(item);
    }
}
```

### 查重与重用推荐

```java
@Component
public class DuplicateChecker {

    public List<Item> findSimilar(String name, String spec, Long categoryId) {
        // 按名称相似度、规格、分类检索
        return itemRepo.search(name, spec, categoryId);
    }
}
```

### 批量导入

```java
@Transactional
public ImportResult importItems(MultipartFile file) {
    List<ItemRow> rows = ExcelUtil.read(file, ItemRow.class);
    ImportResult result = new ImportResult();
    for (ItemRow row : rows) {
        try {
            Long id = create(row.toCmd());
            result.addSuccess(id);
        } catch (Exception e) {
            result.addFail(row, e.getMessage());
        }
    }
    return result;  // 返回成功/失败明细
}
```

## 五、主数据分发

物料是跨系统主数据，见 [主数据事件同步](../tech/tech-mdm-sync-20260816.md)：

```
PLM 创建/变更物料 → 本地消息表 → MQ → ERP/MES 消费
```

## 六、物料编码规则

| 规则 | 示例 | 说明 |
|------|------|------|
| 分类+流水 | RM000001 | 类别前缀 + 流水号 |
| 分段编码 | 10-02-001 | 大类-中类-流水 |
| 智能编码 | M-STEEL-001 | 类型-材质-流水 |
| 流水号 | 100001 | 纯流水（无业务含义） |

推荐：分类前缀 + 流水号，兼顾可读性与扩展性。

## 七、关键设计要点

| 要点 | 说明 |
|------|------|
| 编码唯一 | 业务键，全局唯一 |
| 查重重用 | 创建前查相似件，减少冗余 |
| 生命周期状态 | 驱动可用性 |
| 失效前校验 | BOM/工单/库存均清空 |
| 跨系统同步 | 事件驱动，PLM 为源 |
| 批量导入 | 支持 Excel，返回明细结果 |

## 八、相关文档

- [BOM 概述](../bom/bom-overview-20260816.md)
- [BOM 管理服务实现](./bom-service-impl-20260906.md)
- [主数据事件同步](../tech/tech-mdm-sync-20260816.md)
- [PLM 核心模块](../plm/plm-modules-20260816.md)
- [集成架构](../integration/integration-architecture-20260816.md)
