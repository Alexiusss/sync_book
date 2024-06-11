package com.example.sync_book.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class MinioTestData {
    private static final String MINIO_IMAGE = "minio/minio";
    public static final String MINIO_USERNAME = "admin";
    public static final String MINIO_PASSWORD = "password";
    public static final int MINIO_PORT = 9000;
    public static final String NOT_FOUND_MESSAGE = "The specified key does not exist.";

    public static MinIOContainer initMinioContainer() {
        log.info("Initializing MinIO container with image: {}", MINIO_IMAGE);
        return new MinIOContainer(MINIO_IMAGE)
                .withUserName(MINIO_USERNAME)
                .withPassword(MINIO_PASSWORD)
                .withExposedPorts(MINIO_PORT)
                .withCommand("server /data")
                .waitingFor(new HttpWaitStrategy()
                        .forPath("/minio/health/ready")
                        .forPort(MINIO_PORT)
                        .withStartupTimeout(Duration.ofSeconds(10)));
    }

    public static void setMinioTestProperties(MinIOContainer container) {
        System.setProperty("MINIO_URL", container.getHost());
        System.setProperty("MINIO_PORT", container.getMappedPort(MINIO_PORT).toString());
        System.setProperty("MINIO_ROOT_USER", MINIO_USERNAME);
        System.setProperty("MINIO_ROOT_PASSWORD", MINIO_PASSWORD);
        log.info("MinIO container properties set: URL=http://{}:{}, User={}, Password={}",
                container.getHost(), container.getMappedPort(MINIO_PORT),
                MINIO_USERNAME, MINIO_PASSWORD);
    }

    public static MockMultipartFile getNewFile(String fileName, String mediaType) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return new MockMultipartFile("file", fileName, mediaType, loader.getResourceAsStream(fileName));
    }
}