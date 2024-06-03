package com.example.sync_book.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.root.user}")
    String login;
    @Value("${minio.root.password}")
    String password;
    @Value("${minio.endpoint}")
    String endpoint;

    @Bean
    MinioClient minioClient() {
        return  MinioClient.builder()
                .endpoint(endpoint)
                .credentials(login, password)
                .build();
    }
}