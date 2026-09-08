# 文档管理

> 创建时间：2026-09-06
> 模块：concepts

文档管理的概念、业务流程、Java 实现方案。涵盖图纸、SOP、规格书、检验报告等技术文档的全生命周期。

## 一、核心概念

### 1. 文档管理范畴

| 文档类型 | 示例 | 关联对象 |
|---------|------|---------|
| 图纸 | CAD 装配图、零件图 | 物料、BOM |
| 规格书 | 产品规格、材料规格 | 物料 |
| 工艺文件 | SOP、作业指导书 | 工艺路线、工序 |
| 检验文件 | 检验计划、测试报告 | 物料、检验 |
| 变更文件 | ECN 通知单 | 变更单 |
| 质量文件 | 质量手册、程序文件 | QMS |

### 2. 关键概念

| 术语 | 说明 |
|------|------|
| 文档版本 Revision | 每次修改生成新版本（A/B/C 或 1.0/1.1） |
| 签审 | 编制→校对→审核→会签→批准 |
| Check-in/Check-out | 签入/签出，防止并发修改 |
| 有效性 | 文档生效/失效范围 |
| 关联 | 文档与物料/BOM/工序的关联 |
| 电子化 | 电子签名、在线预览 |

## 二、数据模型

```sql
document (文档主表)
├── id
├── doc_no              文档编号
├── title
├── doc_type            DRAWING/SOP/SPEC/REPORT
├── category_id         分类
├── status              DRAFT/IN_REVIEW/PUBLISHED/SUPERSEDED/OBSOLETE
├── current_revision    当前版本号
├── security_level      保密等级
├── create_by/time
└── ...

document_revision (文档版本)
├── id
├── document_id
├── revision            A/B/C
├── file_id             关联文件元数据
├── change_note         变更说明
├── status
├── review_status       签审状态
├── publish_time
└── ...

document_review (签审记录)
├── id
├── revision_id
├── node_code           编制/校对/审核/批准
├── reviewer_id
├── action
├── comment
├── operate_time
└── ...

document_ref (文档关联)
├── id
├── document_id
├── biz_type            ITEM/BOM/ROUTING/OPERATION
├── biz_id
├── revision            关联文档版本
└── ...
```

## 三、业务流程

### 1. 文档创建与编辑

```
1. 创建文档（草稿）
2. Check-out 锁定编辑
3. 上传文件（写入 file_metadata）
4. Check-in 生成新版本
5. 提交签审
```

### 2. 签审流程

```
编制 → 校对 → 审核 → 会签（多部门） → 批准 → 发布
```

详见 [审批工作流实现](../tech/tech-workflow-20260816.md)。

### 3. 文档发布与分发

```
发布 → 设置有效性 → 通知相关部门 → 关联业务对象（物料/BOM/工序）
```

### 4. 文档变更

```
变更申请 → 生成新版本 → 签审 → 发布 → 旧版本 SUPERSEDED
```

### 5. 文档作废

```
作废申请 → 审批 → 状态 OBSOLETE → 归档（保留历史）
```

### 6. Check-in/Check-out

```
编辑前 Check-out（锁定，他人只读）
编辑后 Check-in（解锁，生成新版本）
```

## 四、Java 实现

### 领域模型

```java
public class Document extends BaseAggregate {
    private Long id;
    private String docNo;
    private DocType docType;
    private DocumentStatus status;
    private String currentRevision;
    private List<DocumentRevision> revisions;
    private boolean checkedOut;
    private Long checkedOutBy;

    // 签出
    public void checkOut(Long userId) {
        Assert.isTrue(!checkedOut, "文档已被签出");
        this.checkedOut = true;
        this.checkedOutBy = userId;
    }

    // 签入并生成新版本
    public DocumentRevision checkIn(Long fileId, String changeNote) {
        Assert.isTrue(checkedOut, "文档未签出");
        String nextRev = RevisionUtil.next(currentRevision);
        DocumentRevision rev = DocumentRevision.create(id, nextRev, fileId, changeNote);
        revisions.add(rev);
        currentRevision = nextRev;
        checkedOut = false;
        checkedOutBy = null;
        return rev;
    }

    // 发布当前版本
    public void publish(String revision) {
        DocumentRevision rev = findRevision(revision);
        Assert.isTrue(rev.isApproved(), "版本未审批通过");
        rev.publish();
        // 旧发布版本置为 SUPERSEDED
        revisions.stream()
            .filter(r -> r.getStatus() == PUBLISHED && !r.getRevision().equals(revision))
            .forEach(DocumentRevision::supersede);
        status = DocumentStatus.PUBLISHED;
    }
}
```

### 应用服务

```java
@Service
public class DocumentApplicationService {

    @Autowired private DocumentRepository docRepo;
    @Autowired private FileService fileService;
    @Autowired private WorkflowService workflowService;

    // 创建文档
    @Transactional
    public Long create(DocumentCreateCmd cmd) {
        // 生成文档编号
        String docNo = seqService.next("DOC", cmd.getDocType().name());
        Document doc = Document.create(docNo, cmd.getDocType());
        docRepo.save(doc);
        return doc.getId();
    }

    // 上传新版本
    @Transactional
    public Long uploadRevision(Long docId, MultipartFile file, String changeNote) {
        Document doc = docRepo.findOrThrow(docId);
        Long currentUserId = SecurityUtils.currentUserId();

        // 自动签出（若未签出）
        if (!doc.isCheckedOut()) {
            doc.checkOut(currentUserId);
        }
        // 上传文件
        FileInfo fileInfo = fileService.upload(file, DOCUMENT);
        // 签入生成新版本
        DocumentRevision rev = doc.checkIn(fileInfo.getId(), changeNote);
        docRepo.save(doc);
        return rev.getId();
    }

    // 提交签审
    @Transactional
    public void submitForReview(Long revisionId) {
        DocumentRevision rev = revisionRepo.findOrThrow(revisionId);
        rev.submitReview();
        // 启动审批流程
        workflowService.start("DOC_REVIEW", new FlowBizRef("DOC_REVISION", revisionId));
    }

    // 签审通过回调
    @EventListener
    public void onReviewApproved(ReviewApprovedEvent event) {
        if (!event.getBizType().equals("DOC_REVISION")) return;
        DocumentRevision rev = revisionRepo.findOrThrow(event.getBizId());
        rev.approve();
        revisionRepo.save(rev);
        // 可自动发布或等待手动发布
    }

    // 关联业务对象
    @Transactional
    public void linkToBiz(Long docId, String bizType, Long bizId) {
        Document doc = docRepo.findOrThrow(docId);
        Assert.isTrue(doc.isPublished(), "仅已发布文档可关联");
        documentRefRepo.save(DocumentRef.of(docId, bizType, bizId, doc.getCurrentRevision()));
    }
}
```

### 文档查询

```java
// 按物料查关联图纸
public List<Document> findDrawingsByItem(Long itemId) {
    List<DocumentRef> refs = documentRefRepo.findByBiz(ITEM, itemId);
    return refs.stream()
        .map(r -> docRepo.findOrThrow(r.getDocumentId()))
        .filter(d -> d.isPublished())
        .collect(toList());
}

// 按工序查 SOP
public List<Document> findSopByOperation(Long operationId) {
    return documentRefRepo.findByBiz(OPERATION, operationId).stream()
        .map(r -> docRepo.findOrThrow(r.getDocumentId()))
        .collect(toList());
}
```

### 权限控制

```java
// 文档按保密等级 + 部门数据权限双重控制
public boolean canDownload(Long docId, Long userId) {
    Document doc = docRepo.findOrThrow(docId);
    User user = userRepo.findOrThrow(userId);
    return user.getSecurityLevel() >= doc.getSecurityLevel()
        && dataPermissionService.canAccess(doc.getCreateDeptId(), user);
}
```

## 五、与文件存储集成

文档的文件实体存储见 [文件存储与预览](../tech/tech-file-preview-20260816.md)：

- 文档版本关联 `file_metadata`。
- 预览走 LibreOffice 转码或 PDF.js。
- 下载走预签名 URL，带权限校验。

## 六、关键设计要点

| 要点 | 说明 |
|------|------|
| 版本不可变 | 已发布版本不修改，变更走新版本 |
| Check-out 防并发 | 编辑前锁定 |
| 签审闭环 | 强制流程，不可跳过 |
| 关联快照 | 关联时记录文档版本 |
| 保密等级 | 权限双重控制 |
| 归档不删除 | 历史版本永久可查 |

## 七、相关文档

- [文件存储与预览](../tech/tech-file-preview-20260816.md)
- [审批工作流实现](../tech/tech-workflow-20260816.md)
- [权限与数据权限](../tech/tech-permission-20260816.md)
- [PLM 核心模块](../plm/plm-modules-20260816.md)
