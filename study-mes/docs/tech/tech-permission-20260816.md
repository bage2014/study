# 权限与数据权限实现

> 创建时间：2026-08-16
> 模块：tech

RBAC 权限模型 + 数据权限的工程实现思路：模型设计、鉴权拦截、数据权限注入、缓存。

## 一、权限模型

### RBAC 标准模型

```
用户(User) ──N:N── 角色(Role) ──N:N── 权限(Permission) ──► 资源(Resource)
```

### 实体设计

```sql
user
├── id
├── username
├── password (加密)
├── status
└── org_id          所属组织

role
├── id
├── code            唯一编码
├── name
├── data_scope      数据范围（ALL/DEPT/DEPT_AND_SUB/SELF/CUSTOM）
└── status

permission
├── id
├── code            唯一编码（如 workorder:create）
├── name
├── type            MENU / BUTTON / API
└── parent_id       树形

user_role
├── user_id
└── role_id

role_permission
├── role_id
└── permission_id

role_data_scope       (data_scope = CUSTOM 时)
├── role_id
└── dept_id
```

## 二、功能权限

### 鉴权方式：注解 + AOP

```java
@Target(METHOD) @Retention(RUNTIME)
public @interface RequiresPermission {
    String value();           // 权限码
    Logical logical() default AND;
}

@RestController
@RequestMapping("/workorder")
public class WorkOrderController {

    @PostMapping
    @RequiresPermission("workorder:create")
    public Result<Long> create(@RequestBody WorkOrderCreateDTO dto) { ... }

    @DeleteMapping("/{id}")
    @RequiresPermission("workorder:delete")
    public Result<Void> delete(@PathVariable Long id) { ... }
}
```

### AOP 切面

```java
@Aspect @Component
public class PermissionAspect {

    @Autowired private PermissionChecker checker;

    @Around("@annotation(requiresPermission)")
    public Object check(ProceedingJoinPoint pjp, RequiresPermission requiresPermission) throws Throwable {
        String code = requiresPermission.value();
        if (!checker.hasPermission(SecurityUtils.currentUserId(), code)) {
            throw new ForbiddenException("无权限: " + code);
        }
        return pjp.proceed();
    }
}
```

### 权限缓存

用户权限不常变，登录时加载并缓存：

```java
@Service
public class PermissionChecker {

    @Autowired private PermissionRepository repo;
    @Autowired private RedisTemplate<String, Set<String>> redis;

    public boolean hasPermission(Long userId, String code) {
        Set<String> perms = getUserPermissions(userId);
        return perms.contains(code) || perms.contains("*");
    }

    private Set<String> getUserPermissions(Long userId) {
        String key = "perms:" + userId;
        Set<String> perms = redis.opsForValue().get(key);
        if (perms == null) {
            perms = repo.findPermissionCodesByUserId(userId);
            redis.opsForValue().set(key, perms, Duration.ofHours(2));
        }
        return perms;
    }

    public void evict(Long userId) {
        redis.delete("perms:" + userId);
    }
}
```

### 失效时机
- 用户角色变更。
- 角色权限变更。
- 用户登出。

## 三、数据权限

### 数据范围类型

| 范围 | 说明 |
|------|------|
| ALL | 全部数据 |
| DEPT | 本部门 |
| DEPT_AND_SUB | 本部门及下属 |
| SELF | 仅本人 |
| CUSTOM | 自定义部门集合 |

### 实现方式：SQL 拦截器注入条件

MyBatis 拦截器在 SQL 执行前注入数据权限条件。

#### 1. 注解标记

```java
@Target(METHOD) @Retention(RUNTIME)
public @interface DataPermission {
    String deptField() default "dept_id";
    String userField() default "create_by";
    String alias() default "";     // 表别名
}

@Mapper
public interface WorkOrderMapper {

    @DataPermission(deptField = "plant_id", alias = "wo")
    List<WorkOrder> selectList(@Param("query") WorkOrderQuery query);
}
```

#### 2. 拦截器

```java
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare",
                       args = {Connection.class, Integer.class}))
@Component
public class DataPermissionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 通过反射拿到 Mapper 方法上的 @DataPermission
        // 2. 拿到当前用户的数据范围
        // 3. 拼接 SQL 条件，注入到原 SQL
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();

        DataPermission dp = resolveAnnotation(boundSql);  // 从 MapperMethod 解析
        if (dp == null) return invocation.proceed();

        DataScope scope = SecurityUtils.currentUser().getDataScope();
        String condition = buildCondition(scope, dp);
        if (condition != null) {
            String newSql = injectWhere(sql, condition);
            ReflectUtil.setFieldValue(boundSql, "sql", newSql);
        }
        return invocation.proceed();
    }

    private String buildCondition(DataScope scope, DataPermission dp) {
        String alias = dp.alias().isEmpty() ? "" : dp.alias() + ".";
        return switch (scope.getType()) {
            case ALL -> null;  // 不加条件
            case DEPT -> alias + dp.deptField() + " = " + scope.getDeptId();
            case DEPT_AND_SUB -> alias + dp.deptField() + " IN (" + join(scope.getDeptIds()) + ")";
            case SELF -> alias + dp.userField() + " = " + scope.getUserId();
            case CUSTOM -> alias + dp.deptField() + " IN (" + join(scope.getCustomDeptIds()) + ")";
        };
    }
}
```

### 多工厂场景

工业系统常按工厂隔离：

```java
@DataPermission(deptField = "plant_id", alias = "wo")
List<WorkOrder> selectList(...);
```

用户绑定的工厂集合作为数据范围，自动注入 `plant_id IN (...)`。

## 四、登录与鉴权流程

### 登录

```
1. 用户名 + 密码提交
2. 校验密码（BCrypt）
3. 加载用户、角色、权限、数据范围
4. 生成 Token（JWT / Sa-Token）
5. 缓存用户上下文（Redis）
6. 返回 Token + 用户信息
```

### 请求鉴权

```
1. 拦截器提取 Token
2. 校验 Token 有效性
3. 加载用户上下文（ThreadLocal）
4. 进入业务方法
5. @RequiresPermission 校验功能权限
6. Mapper 执行时注入数据权限
7. 清理 ThreadLocal
```

## 五、用户上下文

```java
public class SecurityContextHolder {
    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    public static void set(UserContext ctx) { HOLDER.set(ctx); }
    public static UserContext get() { return HOLDER.get(); }
    public static void clear() { HOLDER.remove(); }
}

public record UserContext(
    Long userId,
    String username,
    Long orgId,
    Set<Long> plantIds,
    Set<String> permissions,
    DataScope dataScope
) {}
```

### 异步线程传递

`@Async` / 线程池场景 ThreadLocal 丢失，需装饰器传递：

```java
public class ContextAwareTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        UserContext ctx = SecurityContextHolder.get();
        return () -> {
            try {
                SecurityContextHolder.set(ctx);
                runnable.run();
            } finally {
                SecurityContextHolder.clear();
            }
        };
    }
}
```

## 六、菜单与按钮权限（前端）

### 后端返回权限树

```json
{
  "menus": [...],
  "permissions": ["workorder:create", "workorder:delete", ...]
}
```

### 前端动态路由

```ts
// 根据后端返回的 menus 动态生成路由
const routes = buildRoutesFromMenus(userMenus)
router.addRoute(routes)
```

### 前端按钮控制

```ts
// 自定义指令
app.directive('permission', {
  mounted(el, binding) {
    if (!hasPermission(binding.value)) {
      el.parentNode?.removeChild(el)
    }
  }
})

// 使用
<el-button v-permission="'workorder:create'">新建</el-button>
```

> 前端控制仅为体验优化，**真正的权限校验在后端**。

## 七、权限设计要点

| 要点 | 说明 |
|------|------|
| 权限码命名 | `模块:操作`，如 `workorder:create` |
| 角色 vs 权限 | 角色是权限集合，用户绑角色不直接绑权限 |
| 数据范围 | 角色级别配置，不细到权限 |
| 超级管理员 | 内置 `*` 权限，绕过所有检查 |
| 缓存粒度 | 用户级缓存，变更时精准失效 |
| 接口鉴权 | 即使前端隐藏按钮，后端必须校验 |
| 导出控制 | 导出接口同样加权限注解 |

## 八、常见陷阱

| 陷阱 | 对策 |
|------|------|
| 前端藏按钮即以为安全 | 后端必须校验 |
| 数据权限漏拦截 | 统一走拦截器，禁止手写 SQL 绕过 |
| ThreadLocal 内存泄漏 | finally 清理 |
| 异步任务丢上下文 | TaskDecorator 传递 |
| 缓存不失效 | 角色变更主动 evict |
| 越权访问他人数据 | 数据权限 + 资源归属校验 |

## 九、扩展：字段权限

某些场景需控制字段可见性（如成本字段）：

```java
public class FieldPermission {
    String fieldName;
    boolean readable;
    boolean writable;
}

// 序列化时过滤
public class FieldPermissionSerializer {
    public void filter(Object dto, Set<FieldPermission> perms) {
        // 反射移除不可读字段
    }
}
```

实现复杂，仅在强需求场景使用。

## 十、相关文档

- [框架与分层](./tech-framework-20260816.md)
- [核心数据模型](./tech-data-model-20260816.md)
- [管理系统通用能力](../management/management-overview-20260816.md)
