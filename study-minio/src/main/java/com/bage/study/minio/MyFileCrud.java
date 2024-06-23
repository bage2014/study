package com.bage.study.minio;

import java.util.Arrays;
import java.util.List;

public class MyFileCrud {

    public static void main(String[] args) {
        String bucket = "hello-bage-minio";
        MinioService minioService = new MinioService();

        // 创建 bucket
        minioService.createBucket(bucket);

        // 上传文件 Upload './tobe-upload.txt.zip' as object name 'tobe-upload.zip' to bucket
        String filePath = "./study-minio/src/main/resources/tobe-upload.txt.zip";
        String fileName = "tobe-upload" + System.currentTimeMillis() % 100000 + ".txt.zip";
        minioService.upload(bucket, filePath, fileName);

        String versionId = minioService.get(bucket, fileName);
        System.out.println(versionId);

        String url = minioService.generateVisitUrl(bucket, fileName);
        System.out.println(url);

        List<String> list = minioService.getList(bucket);
        System.out.println(Arrays.toString(list.toArray()));

    }
}
