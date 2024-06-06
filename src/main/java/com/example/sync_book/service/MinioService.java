package com.example.sync_book.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.example.sync_book.util.MinioUtil.getBucketName;

/**
 * Service class for managing audio, image and text files in MinIO.
 * This service provides functionalities to save and retrieve files from a MinIO server.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    /**
     * Saves a file to the MinIO server.
     *
     * @param file the file to be saved.
     * @throws RuntimeException if an error occurs during file upload.
     */
    public void save(MultipartFile file) {
        try (BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(getBucketName())
                    .object(file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .build());
            log.info("File '{}' uploaded successfully.", file.getOriginalFilename());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error occurred while uploading file '{}': {}", file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("Failed to upload file. Please try again.", e);
        }
    }

    /**
     * Retrieves a file from the MinIO server.
     *
     * @param fileName the name of the file to be retrieved.
     * @return the byte content of the file.
     * @throws RuntimeException if an error occurs during file download.
     */
    public byte[] get(String fileName) {
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(getBucketName())
                .object(fileName)
                .build())) {
            log.info("File '{}' downloaded successfully.", fileName);
            return stream.readAllBytes();
        } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error occurred while downloading file '{}': {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to download file. Please try again.", e);
        }
    }
}