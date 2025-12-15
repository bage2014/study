# 家族管理功能需求文档

## 1. 功能概述

家族管理功能是一个用于记录、维护和可视化展示家族成员关系的模块。用户可以通过该功能添加、编辑和删除家族成员，建立成员之间的关系，并以树形结构可视化展示家族关系。

## 2. 详细功能需求

### 2.1 家族成员管理

| 功能ID | 功能名称 | 功能描述 |
|--------|----------|----------|
| FM001  | 添加成员 | 允许用户添加新的家族成员，包括基本信息和个人资料 |
| FM002  | 编辑成员 | 允许用户修改现有家族成员的信息 |
| FM003  | 删除成员 | 允许用户删除家族成员，同时删除与该成员相关的所有关系 |
| FM004  | 查看成员详情 | 允许用户查看家族成员的详细信息和相关关系 |
| FM005  | 搜索成员 | 允许用户按关键词搜索家族成员 |
| FM006  | 分页查询 | 支持分页查询家族成员列表 |

### 2.2 家族关系管理

| 功能ID | 功能名称 | 功能描述 |
|--------|----------|----------|
| FR001  | 添加关系 | 允许用户在两个家族成员之间建立关系 |
| FR002  | 删除关系 | 允许用户删除两个家族成员之间的关系 |
| FR003  | 关系认证 | 允许用户验证家族关系的真实性 |
| FR004  | 关系类型管理 | 支持多种预设的关系类型 |

### 2.3 家族树可视化

| 功能ID | 功能名称 | 功能描述 |
|--------|----------|----------|
| FT001  | 家族树展示 | 以树形结构展示家族成员关系 |
| FT002  | 多代展示 | 支持展示指定代数的家族关系 |
| FT003  | 树节点操作 | 支持展开/折叠树节点，点击查看详情 |
| FT004  | 缩放功能 | 支持放大/缩小家族树视图 |
| FT005  | 导出功能 | 支持导出家族树为图片或其他格式 |

### 2.4 模拟数据生成

| 功能ID | 功能名称 | 功能描述 |
|--------|----------|----------|
| SD001  | 生成模拟数据 | 允许用户生成模拟家族树数据用于测试和演示 |

## 3. 数据模型

### 3.1 FamilyMember（家族成员）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | 主键 | 成员ID |
| name | String | 非空 | 成员姓名 |
| avatar | String | 可选 | 成员头像URL |
| gender | Gender | 可选 | 性别（枚举：MALE, FEMALE） |
| birthDate | LocalDate | 可选 | 出生日期 |
| birthPlace | String | 可选 | 出生地点 |
| occupation | String | 可选 | 职业 |
| education | String | 可选 | 教育程度 |
| contactInfo | String | 可选 | 联系方式 |
| deceased | boolean | 可选 | 是否已去世 |
| deathDate | LocalDate | 可选 | 去世日期 |
| generation | Integer | 非空 | 代际数 |

### 3.2 FamilyRelationship（家族关系）

| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | Long | 主键 | 关系ID |
| member1 | FamilyMember | 非空 | 关系成员1 |
| member2 | FamilyMember | 非空 | 关系成员2 |
| type | RelationshipType | 非空 | 关系类型 |
| verificationStatus | VerificationStatus | 非空 | 认证状态（默认PENDING） |
| startDate | LocalDate | 可选 | 关系开始日期 |
| endDate | LocalDate | 可选 | 关系结束日期 |

### 3.3 RelationshipType（关系类型枚举）

| 枚举值 | 描述 |
|--------|------|
| PARENT_CHILD | 父子 |
| SPOUSE | 夫妻 |
| SIBLING | 兄弟姐妹 |
| GRANDPARENT_GRANDCHILD | 祖孙 |
| FATHER | 父亲 |
| MOTHER | 母亲 |
| SON | 儿子 |
| DAUGHTER | 女儿 |
| HUSBAND | 丈夫 |
| WIFE | 妻子 |
| BROTHER | 兄弟 |
| SISTER | 姐妹 |
| GRANDFATHER | 祖父 |
| GRANDMOTHER | 祖母 |
| GRANDSON | 孙子 |
| GRANDDAUGHTER | 孙女 |
| UNCLE | 叔叔/伯父 |
| AUNT | 阿姨/姑姑 |
| NEPHEW | 侄子 |
| NIECE | 侄女 |
| OTHER | 其他 |

## 4. API接口

### 4.1 家族成员管理接口

| API路径 | 方法 | 功能描述 | 请求参数 | 响应数据 |
|---------|------|----------|----------|----------|
| /family/members/add | POST | 添加家族成员 | FamilyMember对象 | 成功添加的成员信息 |
| /family/members/{id} | GET | 获取成员详情 | id（路径参数） | FamilyMember对象 |
| /family/members/delete | POST | 删除成员 | id（请求体） | 删除结果 |
| /family/members/query | GET | 查询成员列表 | keyword（查询参数）, page（查询参数）, size（查询参数） | 成员列表（分页） |

### 4.2 家族关系管理接口

| API路径 | 方法 | 功能描述 | 请求参数 | 响应数据 |
|---------|------|----------|----------|----------|
| /family/relationships | POST | 添加成员关系 | FamilyRelationship对象 | 成功添加的关系信息 |
| /family/relationships/delete | POST | 删除成员关系 | id（请求体） | 删除结果 |

### 4.3 家族树接口

| API路径 | 方法 | 功能描述 | 请求参数 | 响应数据 |
|---------|------|----------|----------|----------|
| /family/tree/{rootId} | GET | 获取家族树 | rootId（路径参数）, generations（查询参数，默认3） | 家族树数据结构 |

### 4.4 模拟数据接口

| API路径 | 方法 | 功能描述 | 请求参数 | 响应数据 |
|---------|------|----------|----------|----------|
| /family/save/mock | POST | 生成模拟数据 | 无 | 模拟数据生成结果 |

## 5. 数据库表结构

```sql
-- 家族成员表
CREATE TABLE IF NOT EXISTS family_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    avatar VARCHAR(255),
    gender VARCHAR(10),
    birth_date DATE,
    birth_place VARCHAR(200),
    occupation VARCHAR(100),
    education VARCHAR(100),
    contact_info VARCHAR(200),
    deceased BOOLEAN DEFAULT false,
    death_date DATE,
    generation INT NOT NULL
);

-- 家族关系表
CREATE TABLE IF NOT EXISTS family_relationship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member1_id BIGINT REFERENCES family_member(id),
    member2_id BIGINT REFERENCES family_member(id),
    relationship_type VARCHAR(20) NOT NULL,
    verification_status VARCHAR(20) DEFAULT 'PENDING',
    start_date DATE,
    end_date DATE
);
```

## 6. 界面设计

### 6.1 家族树页面

- **页面结构**：
  - 顶部：标题栏（包含添加成员按钮）
  - 搜索栏：成员搜索功能
  - 主体：家族树可视化区域（支持缩放、平移）
  - 底部：操作按钮（返回、放大、缩小、导出）

- **交互设计**：
  - 点击节点可展开/折叠子树
  - 点击节点可查看成员详情
  - 支持从任意成员开始查看家族树

### 6.2 成员详情页面

- **页面结构**：
  - 顶部：标题栏（包含编辑按钮）
  - 基本信息区：展示成员姓名、性别、出生日期、职业、教育程度等
  - 关系图谱区：可视化展示与其他成员的关系
  - 操作按钮区：编辑、删除、分享功能

### 6.3 成员管理页面

- **页面结构**：
  - 顶部：标题栏（包含添加按钮）
  - 搜索筛选区：支持按姓名、性别、代际等条件筛选
  - 成员列表区：表格展示所有家族成员，包含头像、姓名、性别、代际等信息
  - 分页控制区：支持分页查询

### 6.4 成员编辑页面

- **页面结构**：
  - 顶部：标题栏（包含保存按钮）
  - 表单区：包含所有成员信息的输入字段
  - 头像上传区：支持上传和预览头像
  - 操作按钮区：保存、取消功能

## 7. 技术实现要求

1. **后台技术栈**：Spring Boot、JPA、MySQL
2. **前端技术栈**：Flutter、GetX、Dio
3. **数据存储**：MySQL数据库
4. **API设计**：RESTful风格
5. **认证授权**：JWT认证
6. **性能要求**：
   - 家族树查询响应时间不超过2秒
   - 成员列表查询响应时间不超过1秒
7. **安全要求**：
   - 所有API接口必须进行身份认证
   - 敏感数据必须进行加密存储

## 8. 测试要求

1. **单元测试**：对核心业务逻辑进行单元测试，覆盖率不低于80%
2. **集成测试**：对API接口进行集成测试，确保接口功能正常
3. **UI测试**：对前端界面进行UI测试，确保界面美观、交互流畅
4. **性能测试**：对家族树查询等性能敏感的功能进行性能测试

## 9. 部署要求

1. **后台部署**：使用Docker容器部署Spring Boot应用
2. **前端部署**：
   - Flutter应用：生成APK和IPA文件
   - Web应用：部署到Nginx服务器
3. **数据库部署**：使用MySQL数据库，支持主从复制

## 10. 维护要求

1. **日志管理**：记录系统运行日志，便于问题排查
2. **监控管理**：监控系统运行状态，及时发现和处理问题
3. **版本管理**：定期发布新版本，修复bug和添加新功能
4. **文档管理**：维护系统文档，便于后续开发和维护

## 11. 代码实现说明

### 11.1 后端实现

#### 11.1.1 FamilyMember实体类

```java
@Entity
@Table(name = "family_member")
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String avatar;
    private String gender;
    private LocalDate birthDate;
    private String birthPlace;
    private String occupation;
    private String education;
    private String contactInfo;
    private boolean deceased;
    private LocalDate deathDate;
    private Integer generation;
    
    // getter和setter方法
}
```

#### 11.1.2 FamilyRelationship实体类

```java
@Entity
@Table(name = "family_relationship")
public class FamilyRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "member1_id")
    private FamilyMember member1;
    
    @ManyToOne
    @JoinColumn(name = "member2_id")
    private FamilyMember member2;
    
    private String type;
    private String verificationStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    
    // getter和setter方法
}
```

#### 11.1.3 FamilyController类

```java
@RestController
@RequestMapping("/family")
public class FamilyController {
    
    @Autowired
    private FamilyService familyService;
    
    // 添加成员
    @PostMapping("/members/add")
    public ApiResponse<FamilyMember> addMember(@RequestBody FamilyMember member) {
        // 实现代码
    }
    
    // 获取成员详情
    @GetMapping("/members/{id}")
    public ApiResponse<FamilyMember> getMember(@PathVariable Long id) {
        // 实现代码
    }
    
    // 删除成员
    @PostMapping("/members/delete")
    public ApiResponse<String> deleteMember(@RequestBody FamilyMember member) {
        // 实现代码
    }
    
    // 查询成员列表
    @RequestMapping("/members/query")
    public ApiResponse<QueryFamilyResponse> queryMembers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 实现代码
    }
    
    // 添加关系
    @PostMapping("/relationships")
    public ApiResponse<FamilyRelationship> addRelationship(@RequestBody FamilyRelationship relationship) {
        // 实现代码
    }
    
    // 删除关系
    @RequestMapping("/relationships/delete")
    public ApiResponse<String> deleteRelationship(@RequestBody FamilyRelationship relationship) {
        // 实现代码
    }
    
    // 获取家族树
    @GetMapping("/tree/{rootId}")
    public ApiResponse<FamilyMemberTree> getFamilyTree(
            @PathVariable Long rootId, 
            @RequestParam(defaultValue = "3") int generations) {
        // 实现代码
    }
    
    // 生成模拟数据
    @RequestMapping("/save/mock")
    public ApiResponse<FamilyMember> addMember() {
        // 实现代码
    }
}
```

#### 11.1.4 FamilyService类

```java
@Service
public class FamilyService {
    
    @Autowired
    private FamilyMemberRepository memberRepository;
    
    @Autowired
    private FamilyRelationshipRepository relationshipRepository;
    
    public FamilyMember saveMember(FamilyMember member) {
        // 实现代码
    }
    
    public FamilyMember getMemberById(Long id) {
        // 实现代码
    }
    
    public void deleteMember(FamilyMember member) {
        // 实现代码
    }
    
    public FamilyRelationship saveRelationship(FamilyRelationship relationship) {
        // 实现代码
    }
    
    public void deleteRelationship(Long id) {
        // 实现代码
    }
    
    public FamilyMemberTree getFamilyTree(Long rootId, int generations) {
        // 实现代码
    }
    
    public void validateRelationship(FamilyRelationship relationship) {
        // 实现代码
    }
}
```

### 11.2 前端实现

#### 11.2.1 家族树页面

```dart
class FamilyPage extends StatefulWidget {
  @override
  _FamilyPageState createState() => _FamilyPageState();
}

class _FamilyPageState extends State<FamilyPage> {
  final _httpClient = HttpClient();
  FamilyData? _familyData;
  bool _isLoading = true;
  final Map<String, bool> _expandedStates = {};
  
  @override
  void initState() {
    super.initState();
    _loadFamilyData();
  }
  
  Future<void> _loadFamilyData() async {
    // 加载家族数据
  }
  
  TreeNode _buildFamilyTreeNode(FamilyData member) {
    // 构建家族树节点
  }
  
  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'family_tree'.tr,
      body: _isLoading ? CircularProgressIndicator() : TreeView(nodes: [_buildFamilyTreeNode(_familyData!)],),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Get.toNamed(AppRoutes.FAMILY_EDIT),
        child: Icon(Icons.add),
      ),
    );
  }
}
```

#### 11.2.2 成员编辑页面

```dart
class FamilyEditPage extends StatefulWidget {
  @override
  _FamilyEditPageState createState() => _FamilyEditPageState();
}

class _FamilyEditPageState extends State<FamilyEditPage> {
  final _nameController = TextEditingController();
  final _birthPlaceController = TextEditingController();
  final _occupationController = TextEditingController();
  final _educationController = TextEditingController();
  final _contactInfoController = TextEditingController();
  String _gender = 'MALE';
  bool _deceased = false;
  int _generation = 0;
  DateTime? _birthDate;
  DateTime? _deathDate;
  File? _avatarImage;
  
  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'add_member'.tr,
      body: SingleChildScrollView(
        child: Column(
          children: [
            // 头像上传
            // 表单字段
            // 关系选择
          ],
        ),
      ),
    );
  }
}
```

## 12. 功能扩展建议

1. **关系自动推断**：根据已有的关系自动推断出新的关系（例如，根据父子关系和夫妻关系推断出儿媳关系）
2. **家族统计报表**：生成家族成员统计报表，如年龄分布、职业分布等
3. **家族事件管理**：记录家族重要事件，如生日、婚礼、葬礼等
4. **多语言支持**：支持多种语言的家族树展示
5. **云端同步**：支持家族数据的云端同步和备份
6. **社交分享**：支持将家族树分享到社交媒体

## 13. 常见问题及解决方案

1. **家族树加载缓慢**：
   - 解决方案：实现分页加载、异步加载和数据缓存

2. **环形关系问题**：
   - 解决方案：在添加关系时进行环形检测，避免出现循环引用

3. **大数据量处理**：
   - 解决方案：优化数据库查询，使用索引，实现数据分片

4. **用户权限管理**：
   - 解决方案：实现基于角色的访问控制，限制用户对家族数据的操作权限

5. **数据安全性**：
   - 解决方案：对敏感数据进行加密存储，实现数据备份和恢复机制