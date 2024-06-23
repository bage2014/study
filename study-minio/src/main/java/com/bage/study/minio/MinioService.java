package com.bage.study.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;

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
                            .credentials("bage", "bage-666666")
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
