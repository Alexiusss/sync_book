package com.example.sync_book.config;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.example.sync_book.util.MinioUtil.*;

@Configuration
public class MinioConfig {

    @Value("${minio.root.user}")
    String login;
    @Value("${minio.root.password}")
    String password;
    @Value("${minio.endpoint}")
    String endpoint;

    @Bean
    MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(login, password)
                .build();

        if (!isBucketExists(minioClient, getBucketName())) {
            makeBucket(minioClient, getBucketName());
        }

        return minioClient;
    }
}