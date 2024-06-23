package com.bage.study.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileUploader {
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("https://play.min.io")
                            .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
                            .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            // Upload './tobe-upload.txt.zip' as object name 'tobe-upload.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("asiatrip")
                            .object("tobe-upload.txt.zip")
                            .filename("./study-minio/src/main/resources/tobe-upload.txt.zip")
                            .build());
            System.out.println(
                    "'/tobe-upload.txt.zip' is successfully uploaded as "
                            + "object 'tobe-upload.txt.zip' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}
