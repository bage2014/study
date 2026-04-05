# API 设计规范 SKILL

## 1. 技能概述

本技能定义了家族树应用的API设计规范，特别强调了参数传递的方式，禁止在URL路径中直接放置参数。通过本规范，开发者可以确保API设计的一致性和可维护性。

## 2. 核心规范

### 2.1 参数传递规范

**禁止**：将参数直接放在URL路径中

❌ 错误示例：
- `/families/{familyId}/members`
- `/members/{memberId}/relationships`

**允许**：
1. **GET请求**：使用查询参数（query parameters）传递参数
2. **POST/PUT请求**：使用请求体（request body）传递参数
3. **资源ID**：只有资源的唯一标识符（ID）可以放在URL路径中

✅ 正确示例：
- **GET请求**：`/members/family?familyId=1`
- **POST请求**：`/members` 配合请求体 `{ "familyId": 1, "name": "张三" }`
- **资源获取**：`/members/1`（这里的1是成员ID，属于资源标识符）

### 2.2 规范原因

1. **安全性**：敏感参数不应该暴露在URL中
2. **可读性**：URL更清晰，参数语义更明确
3. **一致性**：统一的参数传递方式便于维护
4. **灵活性**：查询参数和请求体可以更好地处理复杂参数

### 2.3 常见场景处理

#### 2.3.1 根据父资源获取子资源列表

❌ 错误：
```
GET /api/families/{familyId}/members
```

✅ 正确：
```
GET /api/members/family?familyId=1
```

#### 2.3.2 创建子资源

❌ 错误：
```
POST /api/families/{familyId}/members
{
  "name": "张三"
}
```

✅ 正确：
```
POST /api/members
{
  "familyId": 1,
  "name": "张三"
}
```

#### 2.3.3 获取单个资源（允许使用ID在URL中）

✅ 允许：
```
GET /api/members/1
PUT /api/members/1
DELETE /api/members/1
```

## 3. 目录结构

```
.trae/skills/
└── api-design/
    └── SKILL.md  # API设计规范文档
```

## 4. 代码示例

### 4.1 后端Spring Boot示例

#### 4.1.1 错误的实现（禁止）

```java
@RestController
@RequestMapping("/api")
public class MemberController {
    
    // ❌ 禁止：familyId在URL路径中
    @GetMapping("/families/{familyId}/members")
    public ApiResponse<List<Member>> getMembers(@PathVariable Long familyId) {
        // ...
    }
    
    // ❌ 禁止：familyId在URL路径中
    @PostMapping("/families/{familyId}/members")
    public ApiResponse<Member> addMember(
            @PathVariable Long familyId, 
            @RequestBody Member member) {
        // ...
    }
}
```

#### 4.1.2 正确的实现（推荐）

```java
@RestController
@RequestMapping("/api")
public class MemberController {
    
    // ✅ 正确：使用查询参数
    @GetMapping("/members/family")
    public ApiResponse<List<Member>> getMembers(@RequestParam Long familyId) {
        // ...
    }
    
    // ✅ 正确：使用请求体
    @PostMapping("/members")
    public ApiResponse<Member> addMember(@RequestBody Member member) {
        // member对象中包含familyId字段
        // ...
    }
}
```

### 4.2 前端Vue示例

#### 4.2.1 错误的实现（禁止）

```javascript
// ❌ 禁止：familyId在URL路径中
async fetchMembersByFamilyId(familyId) {
  const response = await api.get(`/families/${familyId}/members`)
  // ...
}

// ❌ 禁止：familyId在URL路径中
async createMember(memberData) {
  const { familyId, ...memberDataWithoutFamilyId } = memberData
  const response = await api.post(`/families/${familyId}/members`, memberDataWithoutFamilyId)
  // ...
}
```

#### 4.2.2 正确的实现（推荐）

```javascript
// ✅ 正确：使用查询参数
async fetchMembersByFamilyId(familyId) {
  const response = await api.get('/members/family', {
    params: { familyId }
  })
  // ...
}

// ✅ 正确：使用请求体
async createMember(memberData) {
  const response = await api.post('/members', memberData)
  // memberData对象中包含familyId字段
  // ...
}
```

## 5. 检查清单

在开发新的API接口时，请检查以下内容：

- [ ] 所有参数都没有直接放在URL路径中（除了资源ID）
- [ ] GET请求使用查询参数传递参数
- [ ] POST/PUT请求使用请求体传递参数
- [ ] 资源ID（如/members/{id}）可以放在URL路径中
- [ ] API契约文档已经更新
- [ ] 前端和后端实现保持一致

## 6. 规范执行

所有新开发的API接口必须严格遵守本规范。对于已有的不符合规范的接口，应在后续的迭代中逐步重构，以保持代码库的一致性。

## 7. 总结

本API设计规范的核心原则是：**禁止将参数直接放在URL路径中，除了资源的唯一标识符**。通过统一的参数传递方式，可以提高代码的可读性、可维护性和安全性。
