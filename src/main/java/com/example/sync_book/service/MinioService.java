package com.example.sync_book.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.example.sync_book.util.MinioUtil.getBucketName;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    public void save(MultipartFile file) {
        try (BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(getBucketName())
                    .object(file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to upload file. Please try again.", e);
        }
    }

        public byte[] get(String fileName) {
            try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(getBucketName())
                    .object(fileName)
                    .build())) {
                return stream.readAllBytes();
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to download file. Please try again.", e);
        }
    }
}