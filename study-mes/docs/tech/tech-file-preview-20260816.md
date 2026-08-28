# 文件存储与预览

> 创建时间：2026-08-16
> 模块：tech

图纸、SOP、工艺文档、检验报告等文件的管理、存储、在线预览实现思路。

## 一、需求场景

| 场景 | 文件类型 | 操作 |
|------|---------|------|
| 图纸管理 | CAD/PDF | 版本、预览、下载 |
| SOP 工艺文件 | PDF/视频 | 下发车间大屏 |
| 检验报告 | PDF/图片 | 上传、归档 |
| BOM 文档 | PDF/Excel | 关联物料 |
| 变更文档 | Word/PDF | 签审、发布 |

## 二、整体架构

```
前端 ──上传/下载──► 文件服务 ──存储──► 对象存储(MinIO)
                      │
                      ├─元数据──► DB
                      ├─预览──► 转码服务 ──► 预览文件
                      └─权限──► 权限服务
```

## 三、存储方案

### 对象存储选型

| 方案 | 特点 | 适用 |
|------|------|------|
| MinIO | 自建、S3 兼容、开源 | 推荐 |
| 阿里 OSS / 腾讯 COS | 云原生、免运维 | 云上 |
| 本地磁盘 | 简单 | 单机、小量（不推荐生产） |

### 存储结构

```
bucket: mes-files
├── drawings/2026/08/{hash}.pdf      图纸
├── sop/2026/08/{hash}.pdf            SOP
├── reports/2026/08/{hash}.pdf       报告
├── temp/{uuid}.bin                   临时分片
└── preview/{hash}.jpg                预览图
```

按内容 hash 命名，去重存储。

## 四、数据模型

```sql
file_metadata (文件元数据)
├── id
├── file_key          存储路径（hash）
├── original_name      原始文件名
├── content_type       MIME
├── size               字节数
├── md5                内容指纹（去重）
├── preview_key        预览文件路径
├── preview_status     PENDING/PROCESSING/DONE/FAILED
├── create_by/time
└── ...

file_ref (文件引用，关联业务)
├── id
├── file_id            元数据 ID
├── biz_type           ITEM/BOM/WORKORDER/INSPECTION
├── biz_id             业务 ID
├── file_purpose       DRAWING/SOP/REPORT
├── version            业务版本
└── status             DRAFT/PUBLISHED/SUPERSEDED
```

设计要点：元数据与引用分离，同一文件可被多业务引用。

## 五、上传流程

### 普通上传

```java
@RestController
public class FileController {

    @PostMapping("/files")
    public Result<FileInfo> upload(@RequestParam MultipartFile file, FilePurpose purpose) {
        return Result.ok(fileService.upload(file, purpose));
    }
}

@Service
public class FileService {

    @Transactional
    public FileInfo upload(MultipartFile file, FilePurpose purpose) {
        String md5 = DigestUtils.md5Hex(file.getInputStream());
        // 去重：同内容已存在则复用
        FileMetadata meta = metaRepo.findByMd5(md5)
            .orElseGet(() -> storeNew(file, md5));
        // 触发预览转码
        if (needPreview(meta)) {
            previewService.enqueuePreview(meta.getId());
        }
        return FileInfo.of(meta);
    }

    private FileMetadata storeNew(MultipartFile file, String md5) {
        String key = buildKey(md5, file.getContentType());
        minioClient.putObject("mes-files", key, file.getInputStream(), ...);
        FileMetadata meta = FileMetadata.of(file, key, md5);
        metaRepo.save(meta);
        return meta;
    }
}
```

### 大文件分片上传

```
1. 前端切片（每片 5MB）
2. 初始化上传任务，返回 uploadId
3. 并行上传各片
4. 合并（completeMultipartUpload）
```

```java
@PostMapping("/files/multipart/init")
public Result<String> init(String fileName, long size) {
    String uploadId = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set("upload:" + uploadId, fileName, Duration.ofHours(2));
    return Result.ok(uploadId);
}

@PostMapping("/files/multipart/{uploadId}/{partNo}")
public Result<Void> uploadPart(String uploadId, int partNo, MultipartFile part) {
    minioClient.putObject("mes-files", "temp/" + uploadId + "/" + partNo, part.getInputStream(), ...);
    return Result.ok();
}

@PostMapping("/files/multipart/{uploadId}/complete")
public Result<FileInfo> complete(String uploadId) {
    // 合并分片 → 生成最终文件 → 计算md5 → 元数据落库
}
```

### 秒传
上传前先查 md5，存在则直接返回：

```java
@GetMapping("/files/check")
public Result<FileInfo> check(String md5) {
    return metaRepo.findByMd5(md5).map(Result::ok).orElse(Result.empty());
}
```

## 六、下载

### 直连预签名 URL

避免经应用服务器中转，直接下载：

```java
@GetMapping("/files/{id}/download")
public Result<String> downloadUrl(@PathVariable Long id) {
    FileMetadata meta = metaRepo.findOrThrow(id);
    checkPermission(meta);
    String url = minioClient.getPresignedObjectUrl("mes-files", meta.getFileKey(),
        Method.GET, Duration.ofMinutes(10), null);
    return Result.ok(url);
}
```

### 权限控制
- 元数据查权限
- 业务引用查数据权限
- 生成带签名的临时 URL（短时效）

## 七、在线预览

### 方案对比

| 方案 | 适用 | 特点 |
|------|------|------|
| PDF.js | PDF | 浏览器原生，无需转码 |
| Office Online / OnlyOffice | Word/Excel/PPT | 在线协作 |
| LibreOffice 转码 | Office → PDF | 服务端转码 |
| CAD 轻量化（JT/3D PDF） | CAD | 专用查看器 |
| 缩略图 | 图片 | 简单 |

### PDF 预览
直接用 PDF.js，无需服务端处理：
```html
<iframe :src="`/pdfjs/viewer.html?file=${fileUrl}`"></iframe>
```

### Office 转码预览
LibreOffice headless 转换：
```java
@Service
public class OfficePreviewService {

    public void convertToPdf(Long fileId) {
        FileMetadata meta = metaRepo.findOrThrow(fileId);
        if (meta.getPreviewStatus() == DONE) return;
        metaRepo.markProcessing(fileId);

        try {
            // 下载原文件到临时目录
            File src = downloadTemp(meta);
            // LibreOffice 转换
            File pdf = libreOfficeConverter.toPdf(src);
            // 上传预览文件
            String previewKey = "preview/" + meta.getMd5() + ".pdf";
            minioClient.putObject("mes-files", previewKey, pdf);
            metaRepo.markDone(fileId, previewKey);
        } catch (Exception e) {
            metaRepo.markFailed(fileId);
        }
    }
}
```

### 图片缩略图
```java
public void generateThumb(Long fileId) {
    FileMetadata meta = metaRepo.findOrThrow(fileId);
    BufferedImage img = ImageIO.read(minioClient.getObject("mes-files", meta.getFileKey()));
    BufferedImage thumb = Thumbnails.of(img).size(200, 200).asBufferedImage();
    // 上传缩略图
}
```

## 八、版本管理

文件随业务对象有版本：

```java
@Transactional
public Long publishVersion(String bizType, Long bizId, Long fileId) {
    // 旧版本置为 SUPERSEDED
    fileRefRepo.supersedePrevious(bizType, bizId);
    // 新版本 PUBLISHED
    FileRef ref = new FileRef(fileId, bizType, bizId, "v" + nextVersion, PUBLISHED);
    fileRefRepo.save(ref);
    return ref.getId();
}
```

历史版本归档可查，当前版本对外。

## 九、与业务集成

### 物料-图纸
```java
public List<FileRef> findDrawings(Long itemId) {
    return fileRefRepo.findByBiz(ITEM, itemId, DRAWING, PUBLISHED);
}
```

### 工单-SOP 下发
工单下达时关联当前生效 SOP：
```java
@EventListener
public void onWorkOrderRelease(WorkOrderReleasedEvent event) {
    List<FileRef> sops = fileRefRepo.findByBiz(ITEM, event.itemId(), SOP, PUBLISHED);
    // 推送到车间工位终端
    terminalService.pushSop(event.workOrderId(), sops);
}
```

## 十、设计要点

| 要点 | 说明 |
|------|------|
| 内容寻址 | hash 去重，省存储 |
| 元数据-引用分离 | 一文件多引用 |
| 直连存储 | 预签名 URL，不经应用 |
| 预览异步 | 不阻塞上传 |
| 权限校验 | 下载/预览都校验 |
| 版本管理 | 历史可追溯 |
| 冷热分离 | 冷文件归档低频存储 |

## 十一、相关文档

- [权限与数据权限](./tech-permission-20260816.md)
- [框架与分层](./tech-framework-20260816.md)
- [PLM 核心模块](../plm/plm-modules-20260816.md)
