package com.example.sync_book.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.example.sync_book.util.MinioUtil.getBucketName;

@Service
public class MinioService {

    @Autowired
    MinioClient minioClient;

    public void save(MultipartFile file) {
        try (BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(getBucketName())
                    .object(file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .build());

            response.etag();
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] get(String fileName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs.builder()
                        .bucket(getBucketName())
                        .object(fileName)
                        .build())
                .readAllBytes();
    }
}