# 家族关系与维护APP设计文档

## 1. 文档概述

### 1.1 文档目的
本文档详细描述家族关系与维护APP的设计方案，包括需求分析、架构设计、数据库设计、API设计、前后端实现方案等，为后续APP开发提供完整的技术指导。

### 1.2 术语定义
| 术语 | 解释 |
|------|------|
| 家族树 | 以树状结构可视化展示家族成员关系的图形 |
| 宗亲关系 | 基于父系血缘的家族关系 |
| 姻亲关系 | 基于婚姻的家族关系 |
| 家族管理员 | 负责维护家族信息的家族成员 |
| 家族事件 | 家族重要的纪念日、聚会等活动 |

## 2. 需求分析

### 2.1 核心需求
1. **家族成员管理**：添加、编辑、删除家族成员信息
2. **关系定义与维护**：建立和管理家族成员之间的关系
3. **家族树可视化**：以直观方式展示家族关系
4. **家族事件管理**：记录和提醒家族重要事件
5. **家族媒体管理**：共享家族照片、视频和文档
6. **家族通讯**：家族成员之间的沟通功能
7. **家族统计分析**：提供家族数据的统计和分析

### 2.2 目标用户
- **家族长辈**：记录和传承家族历史
- **家族管理员**：维护家族信息的核心成员
- **普通家族成员**：了解家族关系，参与家族活动
- **家族研究者**：研究家族历史和谱系

### 2.3 功能需求列表
| 功能模块 | 具体功能 |
|----------|----------|
| 用户管理 | 注册、登录、个人信息管理、权限管理 |
| 家族管理 | 创建家族、加入家族、家族信息设置 |
| 成员管理 | 添加成员、编辑成员、删除成员、成员列表 |
| 关系管理 | 定义关系、验证关系、查询关系 |
| 家族树 | 生成家族树、查看家族树、导出家族树 |
| 事件管理 | 创建事件、编辑事件、删除事件、事件提醒 |
| 媒体管理 | 上传照片/视频、创建相册、分享媒体 |
| 通讯功能 | 家族群聊、私信、通知推送 |
| 统计分析 | 家族人数统计、年龄分布、地域分布 |

## 3. 架构设计

### 3.1 整体架构
采用前后端分离的架构设计：

```
+----------------+     +----------------+     +----------------+
|   移动端APP    |     |   API网关      |     |   后端服务     |
+----------------+     +----------------+     +----------------+
        |                        |                        |
        | HTTP/HTTPS             | HTTP/HTTPS             |
        |----------------------->|----------------------->|
        |                        |                        |
        |                        |                        |  +----------------+
        |                        |                        |  |   数据库       |
        |                        |                        |  +----------------+
        |                        |                        |         |
        |                        |                        |         |
        |                        |                        |<--------|
        |                        |                        |
        |<-----------------------|<-----------------------|
+----------------+     +----------------+     +----------------+
```

### 3.2 技术选型

#### 3.2.1 前端技术
- **跨平台框架**：Flutter
- **状态管理**：Provider
- **网络请求**：Dio
- **本地存储**：Hive
- **UI组件库**：Flutter Material Components

#### 3.2.2 后端技术
- **框架**：Spring Boot 3.x
- **语言**：Java 17
- **数据库**：MySQL 8.0 + Redis 7.0
- **ORM框架**：MyBatis Plus
- **认证授权**：Spring Security + JWT
- **API文档**：Swagger 3.0

#### 3.2.3 云服务
- **文件存储**：阿里云OSS
- **推送服务**：极光推送
- **短信服务**：阿里云短信

## 4. 数据库设计

### 4.1 核心实体关系图

```
[用户] 1:n [家族成员]
[家族] 1:n [家族成员]
[家族成员] n:m [关系] n:m [家族成员]
[家族] 1:n [家族事件]
[家族] 1:n [家族媒体]
[家族] 1:n [家族通讯]
```

### 4.2 详细表设计

#### 4.2.1 用户表（user）
| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码（加密存储） |
| phone | VARCHAR(20) | UNIQUE | 手机号 |
| email | VARCHAR(100) | UNIQUE | 邮箱 |
| avatar | VARCHAR(255) | | 头像URL |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### 4.2.2 家族表（family）
| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 家族ID |
| name | VARCHAR(100) | NOT NULL | 家族名称 |
| description | TEXT | | 家族描述 |
| founder_id | BIGINT | NOT NULL | 创始人用户ID |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### 4.2.3 家族成员表（family_member）
| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 成员ID |
| family_id | BIGINT | NOT NULL | 家族ID |
| user_id | BIGINT | | 关联用户ID（可为空，用于历史成员） |
| name | VARCHAR(50) | NOT NULL | 成员姓名 |
| gender | TINYINT | NOT NULL | 性别（0:未知, 1:男, 2:女） |
| birth_date | DATE | | 出生日期 |
| death_date | DATE | | 去世日期 |
| birth_place | VARCHAR(100) | | 出生地 |
| current_residence | VARCHAR(100) | | 当前居住地 |
| occupation | VARCHAR(50) | | 职业 |
| bio | TEXT | | 个人简介 |
| avatar | VARCHAR(255) | | 头像URL |
| is_alive | TINYINT | NOT NULL DEFAULT 1 | 是否在世（0:否, 1:是） |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### 4.2.4 关系表（relationship）
| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 关系ID |
| family_id | BIGINT | NOT NULL | 家族ID |
| member1_id | BIGINT | NOT NULL | 成员1 ID |
| member2_id | BIGINT | NOT NULL | 成员2 ID |
| relationship_type | VARCHAR(20) | NOT NULL | 关系类型（如：父子、母子、夫妻等） |
| is_direct | TINYINT | NOT NULL DEFAULT 1 | 是否直接关系（0:间接, 1:直接） |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### 4.2.5 家族事件表（family_event）
| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 事件ID |
| family_id | BIGINT | NOT NULL | 家族ID |
| title | VARCHAR(100) | NOT NULL | 事件标题 |
| description | TEXT | | 事件描述 |
| event_time | DATETIME | NOT NULL | 事件时间 |
| location | VARCHAR(100) | | 事件地点 |
| event_type | VARCHAR(20) | NOT NULL | 事件类型（如：生日、婚礼、聚会等） |
| create_by | BIGINT | NOT NULL | 创建人ID |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### 4.2.6 家族媒体表（family_media）
| 字段名 | 数据类型 | 约束 | 描述 |
|--------|----------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 媒体ID |
| family_id | BIGINT | NOT NULL | 家族ID |
| title | VARCHAR(100) | NOT NULL | 媒体标题 |
| description | TEXT | | 媒体描述 |
| media_url | VARCHAR(255) | NOT NULL | 媒体URL |
| media_type | VARCHAR(10) | NOT NULL | 媒体类型（如：image, video, document） |
| upload_by | BIGINT | NOT NULL | 上传人ID |
| upload_time | DATETIME | NOT NULL | 上传时间 |
| album_id | BIGINT | | 所属相册ID |

## 5. API设计

### 5.1 API基础信息
- **API前缀**：`/api/v1`
- **认证方式**：JWT Token，放在请求头`Authorization: Bearer {token}`
- **响应格式**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {}
  }
  ```

### 5.2 核心API列表

#### 5.2.1 用户管理API
| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| /auth/register | POST | 用户注册 |
| /auth/login | POST | 用户登录 |
| /auth/logout | POST | 用户登出 |
| /user/profile | GET | 获取用户信息 |
| /user/profile | PUT | 更新用户信息 |

#### 5.2.2 家族管理API
| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| /family | POST | 创建家族 |
| /family | GET | 获取家族列表 |
| /family/{id} | GET | 获取家族详情 |
| /family/{id} | PUT | 更新家族信息 |
| /family/{id}/join | POST | 加入家族 |

#### 5.2.3 家族成员API
| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| /family/{familyId}/member | POST | 添加家族成员 |
| /family/{familyId}/member | GET | 获取家族成员列表 |
| /member/{id} | GET | 获取成员详情 |
| /member/{id} | PUT | 更新成员信息 |
| /member/{id} | DELETE | 删除成员 |

#### 5.2.4 关系管理API
| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| /relationship | POST | 建立成员关系 |
| /relationship/{memberId} | GET | 获取成员关系列表 |
| /relationship/path | GET | 查询两个成员之间的关系路径 |

#### 5.2.5 家族树API
| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| /family-tree/{familyId} | GET | 生成家族树数据 |
| /family-tree/{familyId}/export | GET | 导出家族树 |

#### 5.2.6 事件管理API
| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| /event | POST | 创建家族事件 |
| /family/{familyId}/event | GET | 获取家族事件列表 |
| /event/{id} | GET | 获取事件详情 |
| /event/{id} | PUT | 更新事件信息 |
| /event/{id} | DELETE | 删除事件 |

## 6. 前端设计

### 6.1 页面结构
1. **登录注册模块**
   - 登录页
   - 注册页
   - 忘记密码页

2. **家族管理模块**
   - 家族列表页
   - 家族详情页
   - 创建家族页
   - 加入家族页

3. **成员管理模块**
   - 成员列表页
   - 成员详情页
   - 添加成员页
   - 编辑成员页

4. **家族树模块**
   - 家族树展示页
   - 家族树搜索页
   - 家族树导出页

5. **事件管理模块**
   - 事件列表页
   - 事件详情页
   - 创建事件页
   - 事件日历页

6. **媒体管理模块**
   - 相册列表页
   - 照片详情页
   - 上传媒体页

7. **通讯模块**
   - 家族群聊页
   - 私信列表页
   - 私信详情页

### 6.2 核心页面流程

#### 6.2.1 家族树查看流程
```
1. 用户登录APP
2. 进入家族列表页，选择一个家族
3. 点击家族树入口，进入家族树展示页
4. 系统加载家族树数据，可视化展示
5. 用户可缩放、平移查看家族树
6. 点击家族成员，查看成员详情
7. 可搜索特定成员，定位到家族树中
```

#### 6.2.2 添加家族成员流程
```
1. 进入家族详情页
2. 点击"添加成员"按钮
3. 填写成员基本信息
4. 选择成员与其他成员的关系
5. 确认并提交
6. 系统验证关系合法性
7. 添加成功后，更新家族树
```

## 7. 后端设计

### 7.1 核心业务逻辑

#### 7.1.1 家族关系计算
- **直接关系**：通过关系表直接查询
- **间接关系**：使用图算法（如深度优先搜索）计算两个成员之间的关系路径
- **关系验证**：确保添加的关系符合逻辑规则（如一个人只能有一个父亲和一个母亲）

#### 7.1.2 家族树生成
- **算法选择**：采用递归算法生成家族树结构
- **数据结构**：使用树形结构存储家族树数据
- **优化策略**：对于大型家族，采用分页加载和懒加载策略

#### 7.1.3 权限管理
- **家族管理员权限**：可添加、编辑、删除所有家族成员和关系
- **普通成员权限**：只能查看家族信息，部分成员可添加事件和媒体
- **访客权限**：只能查看公开的家族信息

### 7.2 关键类设计

#### 7.2.1 家族成员服务
```java
@Service
public class FamilyMemberService {
    public FamilyMember addMember(FamilyMember member);
    public FamilyMember updateMember(Long id, FamilyMember member);
    public void deleteMember(Long id);
    public FamilyMember getMemberById(Long id);
    public List<FamilyMember> getMembersByFamilyId(Long familyId);
}
```

#### 7.2.2 关系服务
```java
@Service
public class RelationshipService {
    public Relationship addRelationship(Relationship relationship);
    public List<Relationship> getRelationshipsByMemberId(Long memberId);
    public List<Long> getRelationshipPath(Long member1Id, Long member2Id);
    public boolean validateRelationship(Relationship relationship);
}
```

#### 7.2.3 家族树服务
```java
@Service
public class FamilyTreeService {
    public FamilyTreeNode generateFamilyTree(Long familyId, Long rootMemberId);
    public void exportFamilyTree(Long familyId, OutputStream outputStream);
}
```

## 8. 部署与运维

### 8.1 服务器部署
- **后端服务**：Docker容器化部署，使用Kubernetes进行编排
- **数据库**：主从复制架构，定期备份
- **文件存储**：阿里云OSS，开启CDN加速

### 8.2 监控与日志
- **应用监控**：Prometheus + Grafana
- **日志管理**：ELK Stack
- **异常告警**：通过邮件和短信通知管理员

### 8.3 安全策略
- **数据加密**：传输层使用HTTPS，存储层对敏感数据加密
- **访问控制**：基于角色的访问控制（RBAC）
- **防止SQL注入**：使用参数化查询和ORM框架
- **防止XSS攻击**：对用户输入进行过滤和转义

## 9. 测试计划

### 9.1 单元测试
- **测试框架**：JUnit 5 + Mockito
- **测试覆盖率**：核心业务逻辑覆盖率达到80%以上

### 9.2 集成测试
- **测试框架**：TestContainers
- **测试内容**：API接口测试、数据库交互测试

### 9.3 端到端测试
- **测试框架**：Appium
- **测试内容**：核心业务流程测试

### 9.4 性能测试
- **测试工具**：JMeter
- **测试指标**：响应时间、并发用户数、系统吞吐量

## 10. 项目计划

### 10.1 开发阶段
| 阶段 | 时间 | 主要工作 |
|------|------|----------|
| 需求分析与设计 | 2周 | 完成需求分析、架构设计、数据库设计 |
| 后端开发 | 4周 | 实现后端API和核心业务逻辑 |
| 前端开发 | 4周 | 实现前端页面和交互逻辑 |
| 测试与调试 | 2周 | 进行单元测试、集成测试和端到端测试 |
| 上线准备 | 1周 | 服务器部署、性能优化、安全加固 |

### 10.2 迭代计划
- **V1.0版本**：实现核心功能（家族管理、成员管理、家族树展示）
- **V1.1版本**：添加事件管理和媒体管理功能
- **V1.2版本**：添加通讯功能和统计分析功能
- **V2.0版本**：优化用户体验，添加更多高级功能

## 11. 附录

### 11.1 关系类型列表
| 关系类型 | 描述 |
|----------|------|
| 父子 | 父亲与儿子的关系 |
| 父女 | 父亲与女儿的关系 |
| 母子 | 母亲与儿子的关系 |
| 母女 | 母亲与女儿的关系 |
| 夫妻 | 丈夫与妻子的关系 |
| 兄弟 | 哥哥与弟弟的关系 |
| 姐弟 | 姐姐与弟弟的关系 |
| 兄妹 | 哥哥与妹妹的关系 |
| 姐妹 | 姐姐与妹妹的关系 |
| 祖孙 | 祖父/祖母与孙子/孙女的关系 |

### 11.2 事件类型列表
| 事件类型 | 描述 |
|----------|------|
| 生日 | 家族成员生日 |
| 婚礼 | 家族成员婚礼 |
| 葬礼 | 家族成员葬礼 |
| 聚会 | 家族聚会 |
| 庆典 | 家族庆典活动 |
| 纪念日 | 家族重要纪念日 |

---

**文档版本**：V1.0
**编制日期**：2024-01-06
**编制人**：AI Assistant