# 质量管理与 SPC

> 创建时间：2026-08-16
> 模块：tech

质量检验流程、SPC 统计过程控制、CPK 计算的工程实现思路。

## 一、质量数据模型

### 检验主表

```sql
inspection (检验记录)
├── id
├── inspection_no        检验单号
├── type                  IQC/IPQC/OQC/FQC
├── item_id               物料
├── lot_no / serial_no    批次/序列号
├── work_order_id         关联工单
├── operation_id          关联工序
├── inspection_plan_id    检验计划
├── result                PASSED / FAILED / CONCESSION(让步)
├── inspector_id
├── inspect_time
└── status                DRAFT / COMPLETED

inspection_item (检验项)
├── id
├── inspection_id
├── check_item_id        检验项定义
├── target_value          标准值
├── upper_limit           上限
├── lower_limit           下限
├── actual_value          实测值
├── result                OK / NG
└── remark

inspection_defect (不良记录)
├── id
├── inspection_id
├── defect_code           不良代码
├── defect_qty
└── ...
```

### 检验计划

```sql
inspection_plan (检验计划)
├── id
├── item_id
├── type                  IQC/IPQC/OQC
├── sample_size           抽样数
├── aql_level             AQL 等级
└── status

inspection_plan_item (计划检验项)
├── plan_id
├── check_item_id
├── target_value
├── upper_limit
├── lower_limit
├── spc_enabled           是否启用 SPC
└── control_chart_type    X-R / X-S / P
```

## 二、检验流程

```
1. 触发检验（进料/工序/完工）
2. 查检验计划
3. 生成检验单（DRAFT）
4. 录入实测值
5. 自动判定（OK/NG）
6. 结果：通过/不合格/让步
7. 不合格 → 隔离 → 评审 → 处理
8. SPC 更新（若启用）
```

```java
@Service
public class InspectionService {

    @Transactional
    public Long create(InspectionCreateCmd cmd) {
        InspectionPlan plan = planRepo.findByItem(cmd.getItemId(), cmd.getType())
            .orElseThrow(() -> new BusinessException("无检验计划"));

        Inspection ins = Inspection.of(cmd, plan);
        inspectionRepo.save(ins);

        List<InspectionItem> items = plan.getItems().stream()
            .map(p -> InspectionItem.of(ins, p))
            .toList();
        inspectionItemRepo.saveAll(items);

        return ins.getId();
    }

    @Transactional
    public void recordValue(Long itemId, BigDecimal value) {
        InspectionItem item = itemRepo.findOrThrow(itemId);
        item.recordActual(value);
        itemRepo.save(item);

        // 自动判定
        if (allItemsRecorded(item.getInspectionId())) {
            autoJudge(item.getInspectionId());
        }
    }

    private void autoJudge(Long inspectionId) {
        List<InspectionItem> items = itemRepo.findByInspection(inspectionId);
        boolean allOk = items.stream().allMatch(InspectionItem::isOk);
        Inspection ins = inspectionRepo.findOrThrow(inspectionId);
        ins.judge(allOk ? PASSED : FAILED);
        inspectionRepo.save(ins);

        if (ins.isFailed()) {
            quarantineService.quarantine(ins);
        }
        if (ins.isSpcEnabled()) {
            spcService.update(ins);
        }
    }
}
```

## 三、SPC 统计过程控制

### 核心概念

| 指标 | 说明 |
|------|------|
| CL | Center Line，中心线（均值） |
| UCL | Upper Control Limit，上控制限 = μ + 3σ |
| LCL | Lower Control Limit，下控制限 = μ - 3σ |
| USL/LSL | 规格上下限 |
| Cp | 过程能力 = (USL-LSL) / 6σ |
| Cpk | 过程能力指数，考虑偏移 |

### 控制图数据模型

```sql
spc_sample (样本组)
├── id
├── check_item_id        检验项
├── subgroup_no          子组号
├── sample_size          子组样本量
├── mean                  子组均值
├── range                子组极差
├── std_dev               子组标准差
├── collect_time
└── ...

spc_control_limit (控制限)
├── check_item_id
├── cl / ucl / lcl
├── cp / cpk
├── calculate_time
└── ...
```

### 均值-极差（X̄-R）图计算

```java
@Service
public class SpcService {

    public ControlLimit calculateXbarR(Long checkItemId, int subgroupSize) {
        // 取最近 N 个子组
        List<SpcSample> samples = sampleRepo.findRecent(checkItemId, subgroupSize * 25);

        // 计算总均值 X̿ 和平均极差 R̄
        double xbarBar = samples.stream().mapToDouble(SpcSample::getMean).average().orElse(0);
        double rBar = samples.stream().mapToDouble(SpcSample::getRange).average().orElse(0);

        // 控制限系数（查表，依样本量）
        double d4 = ControlLimitTable.d4(subgroupSize);  // R 图 UCL 系数
        double d3 = ControlLimitTable.d3(subgroupSize);  // R 图 LCL 系数
        double a2 = ControlLimitTable.a2(subgroupSize);  // X̄ 图系数

        // X̄ 图控制限
        double xucl = xbarBar + a2 * rBar;
        double xlcl = xbarBar - a2 * rBar;
        // R 图控制限
        double rucl = d4 * rBar;
        double rlcl = d3 * rBar;

        return ControlLimit.of(xbarBar, xucl, xlcl, rBar, rucl, rlcl);
    }

    public CpkResult calculateCpk(Long checkItemId, double usl, double lsl) {
        List<SpcSample> samples = sampleRepo.findRecent(checkItemId, 100);
        double mean = samples.stream().mapToDouble(SpcSample::getMean).average().orElse(0);
        double sigma = stdDev(samples);  // 总体标准差

        // Cp = (USL - LSL) / 6σ
        double cp = (usl - lsl) / (6 * sigma);
        // Cpk = min((USL-μ)/(3σ), (μ-LSL)/(3σ))
        double cpu = (usl - mean) / (3 * sigma);
        double cpl = (mean - lsl) / (3 * sigma);
        double cpk = Math.min(cpu, cpl);

        return new CpkResult(cp, cpk, mean, sigma);
    }

    @Transactional
    public void update(Inspection ins) {
        for (InspectionItem item : ins.getItems()) {
            if (!item.isSpcEnabled()) continue;
            // 加入子组
            sampleRepo.addToSubgroup(item.getCheckItemId(), item.getActualValue(), ins.getInspectTime());
            // 重算控制限
            ControlLimit limit = calculateXbarR(item.getCheckItemId(), item.getSubgroupSize());
            limitRepo.save(item.getCheckItemId(), limit);
            // 异常判定
            detectOutOfControl(item.getCheckItemId(), limit);
        }
    }
}
```

## 四、判异规则（Nelson Rules）

8 条判异规则，触发即报警：

```java
public class NelsonRuleDetector {

    public List<RuleViolation> detect(List<SpcSample> samples, ControlLimit limit) {
        List<RuleViolation> violations = new ArrayList<>();
        // 规则1：单点超出 3σ
        // 规则2：连续9点在中心线同一侧
        // 规则3：连续6点单调递增/递减
        // 规则4：连续14点交替上下
        // 规则5-8：...
        return violations;
    }
}
```

判异触发报警，通知质量工程师。

## 五、不良柏拉图

```java
public class DefectPareto {

    public ParetoResult analyze(Long plantId, LocalDate from, LocalDate to) {
        List<DefectStat> stats = defectRepo.countByCode(plantId, from, to);
        stats.sort(Comparator.comparing(DefectStat::getQty).reversed());

        long total = stats.stream().mapToLong(DefectStat::getQty).sum();
        long cum = 0;
        for (DefectStat s : stats) {
            cum += s.getQty();
            s.setCumQty(cum);
            s.setCumRate((double) cum / total);
        }
        return new ParetoResult(stats, total);
    }
}
```

输出 Top 不良 + 累计占比，识别 80/20 关键不良。

## 六、隔离与评审

```java
@Service
public class QuarantineService {

    public void quarantine(Inspection ins) {
        // 库存状态：可用 → 冻结/在检
        stockService.changeStatus(ins.getItemId(), ins.getLotNo(), FROZEN);

        // 创建评审单
        Review review = Review.of(ins, PENDING);
        reviewRepo.save(review);

        eventPublisher.publish(new QuarantineEvent(ins));
    }
}

// 评审决策：返工/返修/降级/报废/让步
public void decide(Long reviewId, Decision decision) {
    Review review = reviewRepo.findOrThrow(reviewId);
    review.decide(decision);
    reviewRepo.save(review);

    switch (decision) {
        case REWORK -> createReworkOrder(review);
        case SCRAP -> stockService.scrap(review.getItemId(), review.getLotNo());
        case DOWNGRADE -> stockService.downgrade(review.getItemId(), review.getLotNo());
        case CONCESSION -> stockService.release(review.getItemId(), review.getLotNo());
    }
}
```

## 七、设计要点

| 要点 | 说明 |
|------|------|
| 检验计划可配置 | 物料+类型绑定计划 |
| 自动判定 | 实测值与限值比对 |
| SPC 异步计算 | 不阻塞检验主流程 |
| 子组采样 | 按时间/数量分组 |
| 控制限周期重算 | 适应过程漂移 |
| 判异告警 | 实时推送 |
| 不良闭环 | 隔离→评审→处理 |

## 八、相关文档

- [设备数据采集](./tech-iot-collection-20260816.md)
- [实时看板](./tech-dashboard-20260816.md)
- [追溯查询](./tech-traceability-20260816.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
