# study-minIO

MinIO是一款高性能高可用的文件系统服务，可以用来替换FastDFS，这里介绍一下简单安装与使用。部署方式采用单机单硬盘模式。



## 参考链接

官网文档 https://github.com/minio/minio



## 快速开始

Docker 部署环境 

https://github.com/bage2014/study/tree/master/study-docker



## 应用实践

GitHub: [minio/minio-java](https://github.com/minio/minio-java)

Latest version: 8.5.10

Quickstart Guide: [Java Quickstart Guide](https://min.io/docs/minio/linux/developers/java/minio-java.html#minio-java-quickstart)

Reference: [Java Client API Reference](https://min.io/docs/minio/linux/developers/java/API.html)

### 快速上手

Java 客户端

https://min.io/docs/minio/linux/developers/minio-drivers.html?ref=docs#java-sdk

```
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.10</version>
</dependency>
```



**Java 样例**

创建bucket、上传文件、查询文件、生成URL、查看LIST

```

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MinioService {
    private MinioClient client;

    public MinioClient getClient() {
        return client;
    }

    public void setClient(MinioClient client) {
        this.client = client;
    }

    private void init() {
        if (client == null) {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            client =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .credentials("bage", "bage-password-666")
                            .build();
        }
    }

    public MinioClient getInstance() {
        if (client == null) {
            init();
        }
        return client;
    }

    public void createBucket(String bucket) {
        // Make bucket if not exist.
        try {
            boolean found = getInstance().bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                // Make a new bucket called.
                getInstance().makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                System.out.println("Bucket " + bucket + " create success.");
            } else {
                System.out.println("Bucket " + bucket + " already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int upload(String bucket, String filePath, String minioFileName) {
        try {
            ObjectWriteResponse objectWriteResponse = getInstance().uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucket)
                            .object(minioFileName)
                            .filename(filePath)
                            .build());
            return objectWriteResponse != null && objectWriteResponse.versionId() != null ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String get(String bucket, String fileName) {
        try {
            GetObjectResponse object = getInstance().getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build());
            return object.object();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getList(String bucket) {
        List<String> list = new ArrayList<>();
        try {
            Iterable<Result<Item>> listObjects = getInstance().listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .build());
            for (Result<Item> listObject : listObjects) {
                list.add(listObject.get().objectName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String generateVisitUrl(String bucket, String fileName) {
        try {
            return getInstance().getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .bucket(bucket)
                    .object(fileName)
                    .expiry(1, TimeUnit.HOURS)
                    .method(Method.GET)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

```



### 应用进阶 

更加完善的用例

https://min.io/docs/minio/linux/developers/java/API.html

https://github.com/minio/minio-java/tree/release/examples



## 原理解析

基本原理