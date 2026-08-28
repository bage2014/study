# 测试策略

> 创建时间：2026-08-16
> 模块：tech

MES/PLM 类工业系统的测试分层、关键场景、自动化方案。

## 一、测试金字塔

```
        ┌───┐
        │E2E│      少量，关键链路
       ┌─┴───┴─┐
       │ 集成   │   适中，模块/接口
      ┌┴────────┴┐
      │  单元    │   大量，业务规则
      └──────────┘
```

| 层级 | 占比 | 范围 | 速度 |
|------|------|------|------|
| 单元测试 | 60%+ | 领域逻辑、算法 | 毫秒 |
| 集成测试 | 25% | 模块、接口、DB | 秒 |
| 端到端 | 10% | 关键业务链 | 分钟 |
| 手工 | 5% | 探索性、UI | 慢 |

## 二、单元测试

### 重点
- 领域模型（实体、值对象）
- 领域服务（状态机、规则）
- 算法（BOM 展开、OEE、SPC、追溯）
- 工具类

### 不测
- Controller（薄，归集成）
- Mapper/DB（归集成）
- 框架本身

### 示例（BOM 展开）

```java
@ExtendWith(MockitoExtension.class)
class BomExploderTest {

    @InjectMocks BomExploder exploder;
    @Mock BomRepository bomRepo;

    @Test
    void 单层展开() {
        // given
        when(bomRepo.findEffectiveBom(A_ID, any())).thenReturn(Optional.of(bomA));
        when(bomRepo.findLines(bomA.getId())).thenReturn(List.of(lineX, lineY));
        when(bomRepo.findEffectiveBom(X_ID, any())).thenReturn(Optional.empty());
        when(bomRepo.findEffectiveBom(Y_ID, any())).thenReturn(Optional.empty());

        // when
        List<FlatBomEntry> result = exploder.explode(A_ID, ten, today);

        // then
        assertThat(result).containsExactlyInAnyOrder(
            entry(X, 20), entry(Y, 30));
    }

    @Test
    void 虚拟件穿透不累加自身() { ... }

    @Test
    void 循环引用抛异常() { ... }

    @Test
    void 损耗率计入用量() { ... }
}
```

### 状态机测试

```java
@Test
void 工单状态_执行中不可下达() {
    WorkOrder wo = new WorkOrder(IN_PROGRESS, ...);
    assertThatThrownBy(() -> engine.transit(IN_PROGRESS, RELEASE))
        .isInstanceOf(IllegalStateTransitionException.class);
}

@Test
void 已关闭终态不可流转() {
    assertThatThrownBy(() -> engine.transit(CLOSED, COMPLETE))
        .isInstanceOf(IllegalStateTransitionException.class);
}
```

### 测试工具
- JUnit 5
- Mockito（mock 依赖）
- AssertJ（流式断言）
- TestContainers（需真实 DB 的场景下沉）

## 三、集成测试

### 重点
- 应用服务（事务编排）
- Mapper/Repository（SQL 正确性）
- 与中间件（Redis/MQ）
- 跨聚合交互

### TestContainers（真实 DB）

```java
@SpringBootTest
@Testcontainers
class WorkReportIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void 报工后完工数累加且防超额() {
        // 工单计划 10
        reportService.report(reportCmd(woId, goodQty=6));
        reportService.report(reportCmd(woId, goodQty=4));
        assertThat(workOrderRepo.findOrThrow(woId).getCompletedQty()).isEqualByComparingTo(10);

        // 第3次超额
        assertThatThrownBy(() -> reportService.report(reportCmd(woId, goodQty=1)))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("超额");
    }
}
```

### 契约测试（跨服务）
Pact / Spring Cloud Contract：消费方驱动契约，保证接口不破坏。

## 四、端到端测试

### 关键链路（不多，覆盖核心）
- 工单下达 → 报工 → 完工入库
- BOM 创建 → 审核 → 发布 → 展开
- 质量检验 → 不合格 → 隔离 → 评审
- 追溯查询（正反向）

### 工具
- Playwright / Cypress

```ts
// playwright
test('工单报工全流程', async ({ page }) => {
  await page.goto('/workorder')
  await page.click('text=新建工单')
  await fillWorkOrderForm(page, { item: '产品A', qty: 10 })
  await page.click('text=下达')
  await page.click('text=报工')
  await fillReportForm(page, { goodQty: 10 })
  await expect(page.locator('text=已完工')).toBeVisible()
})
```

### 数据准备
- 独立测试库
- 每次运行前种子数据（@BeforeEach）
- 不依赖其他用例顺序

## 五、测试数据策略

| 策略 | 适用 |
|------|------|
| Builder 模式 | 复杂对象构造 |
| Fixture（母数据） | 集成/E2E 共用 |
| TestFactory | 生成常用测试对象 |
| @BeforeEach | 用例前准备 |

```java
public class WorkOrderFixture {
    public static WorkOrder.Builder builder() {
        return WorkOrder.builder()
            .itemId(TestData.PRODUCT_A)
            .quantity(BigDecimal.TEN)
            .status(CREATED);
    }
}
```

## 六、专项测试

### 并发测试
```java
@Test
void 并发报工不超额() throws InterruptedException {
    int threads = 20;
    ExecutorService exec = Executors.newFixedThreadPool(threads);
    CountDownLatch start = new CountDownLatch(1);
    AtomicInteger success = new AtomicInteger(0);

    for (int i = 0; i < threads; i++) {
        exec.submit(() -> {
            start.await();
            try { reportService.report(reportCmd(1)); success.incrementAndGet(); }
            catch (Exception ignored) {}
        });
    }
    start.countDown();
    exec.shutdown();
    exec.awaitTermination(10, SECONDS);

    // 计划 10，20 并发各报 1，应只 10 个成功
    assertThat(success.get()).isEqualTo(10);
}
```

### 性能测试
- BOM 展开：深层结构 < 200ms
- 追溯：千节点 < 1s
- 报工：1000 TPS

工具：JMeter / k6 / Gatling。

## 七、CI 集成

```yaml
# .github/workflows/ci.yml
jobs:
  test:
    steps:
      - run: ./mvnw test                    # 单元
      - run: ./mvnw verify -Pintegration   # 集成（TestContainers）
      - run: npx playwright test           # E2E
      - uses: codecov/codecov-action@v3   # 覆盖率
```

### 覆盖率目标
- 行覆盖 ≥ 70%
- 关键模块（状态机、算法）≥ 90%
- 不盲目追 100%，重在关键路径。

## 八、测试原则

| 原则 | 说明 |
|------|------|
| 测行为不测实现 | 不依赖私有细节 |
| 快速反馈 | 单元毫秒级 |
| 可重复 | 不依赖环境、顺序 |
| 失败即清晰 | 断言明确，定位快 |
| 守护关键规则 | 状态机、算法全覆盖 |
| 不测框架 | 不测 MyBatis 本身 |

## 九、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [工单状态机](./tech-workorder-state-20260816.md)
- [BOM 展开算法](./tech-bom-explode-20260816.md)
- [部署与运维](./tech-devops-20260816.md)
