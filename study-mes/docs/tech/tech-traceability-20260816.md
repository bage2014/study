# 追溯查询实现

> 创建时间：2026-08-16
> 模块：tech

产品正反向追溯的工程实现思路：数据采集、图模型、查询算法、性能优化。

## 一、追溯目标

### 正向追溯（物料 → 成品）
> "这批原材料用在了哪些成品上？发给了哪个客户？"

输入：物料批次号 / 序列号
输出：所有关联成品 + 发货记录 + 客户

### 反向追溯（成品 → 物料）
> "这台成品的某零件来自哪个供应商哪批料？加工参数是什么？"

输入：成品序列号 / 批次
输出：全部用料批次 + 供应商 + 加工记录

## 二、追溯的本质：事务链图

每次报工形成一条"投入-产出"边：

```
投入批次/序列号 ──(报工)──► 产出批次/序列号
```

多次报工串联成图：

```
供应商批次 X ──► 报工#1 ──► 半成品 B(批) ──► 报工#3 ──► 成品 A(序)
供应商批次 Y ──► 报工#1 ──┘
供应商批次 Z ──► 报工#2 ──► 半成品 C(批) ──► 报工#3 ──┘
```

追溯 = 在这张图上做 BFS/DFS。

## 三、数据采集（关键前提）

追溯质量取决于采集的完整性。

### 必采字段（每次报工）

| 字段 | 说明 |
|------|------|
| work_order_id | 工单 |
| operation_id | 工序 |
| operator_id | 操作员 |
| work_center_id | 设备/工位 |
| report_time | 报工时间 |
| good_qty / bad_qty | 合格/不合格数 |
| output_lot / output_serial | 产出批次/序列号 |
| input_lots / input_serials | 投入批次/序列号（多） |
| process_params | 加工参数（温度、压力等） |

### 投入-产出关系表（核心）

```sql
genealogy_edge (族谱边)
├── id
├── work_report_id      报工记录
├── work_order_id
├── operation_id
├── input_item_id       投入物料
├── input_lot_no        投入批次
├── input_serial_no     投入序列号
├── output_item_id      产出物料
├── output_lot_no       产出批次
├── output_serial_no    产出序列号
├── quantity            消耗数量
├── report_time
└── create_time
```

### 设计要点
- 每条报工的每个投入-产出组合写一条边。
- 序列号场景：1 输入 1 输出。
- 批次场景：N 输入 1 输出（或 1 输出多）。
- 边表为 append-only，不可修改。

## 四、追溯算法

### 节点标识

追溯节点 = (item_id, lot_no) 或 (item_id, serial_no)。

```java
public record TraceNode(
    Long itemId,
    String lotNo,        // 批次场景
    String serialNo      // 序列号场景
) {}
```

### 反向追溯（成品 → 物料）

从成品节点出发，沿"投入"方向反向遍历。

```java
public class TraceabilityService {

    public TraceResult traceBack(TraceNode root) {
        Map<TraceNode, List<Edge>> graph = new HashMap<>();
        Set<TraceNode> visited = new HashSet<>();
        Deque<TraceNode> queue = new ArrayDeque<>();

        queue.push(root);
        visited.add(root);

        while (!queue.isEmpty()) {
            TraceNode cur = queue.pop();
            // 查所有以 cur 为产出的边（即 cur 是 output）
            List<GenealogyEdge> edges = edgeMapper.findByOutput(cur.itemId, cur.lotNo, cur.serialNo);
            for (GenealogyEdge e : edges) {
                TraceNode input = new TraceNode(e.getInputItemId(), e.getInputLotNo(), e.getInputSerialNo());
                graph.computeIfAbsent(cur, k -> new ArrayList<>())
                     .add(new Edge(input, cur, e));
                if (visited.add(input)) {
                    queue.push(input);
                }
            }
        }
        return new TraceResult(root, graph);
    }
}
```

### 正向追溯（物料 → 成品）

从物料节点出发，沿"产出"方向正向遍历。

```java
public TraceResult traceForward(TraceNode root) {
    Map<TraceNode, List<Edge>> graph = new HashMap<>();
    Set<TraceNode> visited = new HashSet<>();
    Deque<TraceNode> queue = new ArrayDeque<>();

    queue.push(root);
    visited.add(root);

    while (!queue.isEmpty()) {
        TraceNode cur = queue.pop();
        // 查所有以 cur 为投入的边（即 cur 是 input）
        List<GenealogyEdge> edges = edgeMapper.findByInput(cur.itemId, cur.lotNo, cur.serialNo);
        for (GenealogyEdge e : edges) {
            TraceNode output = new TraceNode(e.getOutputItemId(), e.getOutputLotNo(), e.getOutputSerialNo());
            graph.computeIfAbsent(cur, k -> new ArrayList<>())
                 .add(new Edge(cur, output, e));
            if (visited.add(output)) {
                queue.push(output);
            }
        }
    }
    return new TraceResult(root, graph);
}
```

### 算法选择
- BFS（推荐）：层级清晰，便于展示。
- DFS：路径明确，便于深度追溯。
- 通常 BFS + 限制最大深度（如 20 层）防异常。

## 五、查询性能优化

### 问题
- 每个节点一次 DB 查询，N 节点 = N 次查询。
- 大批次合并生产时节点多。

### 优化方案

**1. 索引**

```sql
CREATE INDEX idx_edge_output ON genealogy_edge(output_item_id, output_lot_no, output_serial_no);
CREATE INDEX idx_edge_input  ON genealogy_edge(input_item_id,  input_lot_no,  input_serial_no);
```

**2. 批量查询（BFS 分层批查）**

```java
while (!queue.isEmpty()) {
    List<TraceNode> batch = drain(queue, 500);
    // 一次性查 500 个节点的边
    List<GenealogyEdge> edges = edgeMapper.findByOutputs(batch);
    // 分组、入队
}
```

**3. 物化路径表（可选）**

对每个成品预计算全链路路径，写入物化表：

```sql
genealogy_path
├── root_serial_no      成品序列号
├── node_item_id        中间节点
├── node_lot_no
├── level
├── path                路径字符串
└── work_report_id
```

适合追溯频繁、变更少的场景。报工完成时异步刷新。

**4. 图数据库（极端场景）**

百万级节点 + 高频追溯 → Neo4j / NebulaGraph。
普通工业场景关系型 + 索引足够。

## 六、序列号 vs 批次

### 序列号追溯（1:1）
- 单件产品唯一序列号。
- 边关系清晰，精度高。
- 适合电子、半导体、汽车。

### 批次追溯（N:M）
- 一批物料投入多个产出。
- 需记录"这批投入分到哪几个产出"。
- 用量比例可能不均。

### 批次追溯的难点
- 一批投入料可能跨多张报工。
- 产出批次可能合并多批投入。
- 多对多关系，追溯结果为"可能范围"而非精确。

### 实现建议
- 投入批次按比例分摊到产出。
- 边表记录实际消耗数量。
- 追溯结果展示"所有可能批次"，非单一答案。

## 七、追溯结果展示

### 树形结构
```
成品 A (SN: A001)
├── 报工#3 (装配)
│   ├── 半成品 B (Lot: B202608)
│   │   ├── 报工#1 (加工)
│   │   │   ├── 原料 X (Lot: X001, 供应商 S1)
│   │   │   └── 原料 Y (Lot: Y001, 供应商 S2)
│   └── 半成品 C (Lot: C202608)
│       └── 报工#2 (加工)
│           └── 原料 Z (Lot: Z009, 供应商 S3)
└── 标准件 D (Lot: D100)
```

### 关联信息
每条边可关联：
- 报工详情（操作员、时间、设备）
- 加工参数（温度、压力、转速）
- 检验记录
- 工艺版本

## 八、辅助追溯维度

除物料追溯外，还可沿其他维度追溯：

| 维度 | 实现 |
|------|------|
| 设备追溯 | 某设备加工了哪些产品（按 work_center_id 查） |
| 人员追溯 | 某操作员加工了哪些产品（按 operator_id 查） |
| 工艺追溯 | 某工艺版本影响了哪些产品（按 routing_version） |
| 参数追溯 | 某参数异常影响了哪些产品 |

## 九、召回场景

> "供应商 S1 的批次 X001 有质量问题，召回所有受影响成品。"

```
1. 正向追溯 X001 → 所有成品序列号
2. 查发货记录 → 客户清单
3. 通知客户、安排返工/更换
```

性能关键：正向追溯 + 发货记录关联。

## 十、数据治理

| 要点 | 说明 |
|------|------|
| 强制扫码投料 | 避免漏录投入批次 |
| 防呆校验 | 投料前校验物料正确 |
| 边表不可改 | 报工纠错走冲红 + 新报工 |
| 定期对账 | 边表与报工记录一致性 |
| 归档 | 历史数据归档冷存储，保留合规年限 |

## 十一、相关文档

- [核心数据模型](./tech-data-model-20260816.md)
- [报工高并发](./tech-concurrency-20260816.md)
- [MES 业务流程](../mes/mes-workflow-20260816.md)
