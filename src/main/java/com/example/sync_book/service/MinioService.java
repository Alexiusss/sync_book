package com.example.sync_book.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Service class for managing audio, image and text files in MinIO.
 * This service provides functionalities to save and retrieve files from a MinIO server.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    /**
     * Saves a file to the MinIO server.
     *
     * @param file the file to be saved.
     * @throws ServerException thrown to indicate that S3 service returning HTTP server error
     * @throws ErrorResponseException thrown to indicate S3 service returned an error response.
     * @throws InsufficientDataException thrown to indicate not enough data available in InputStream.
     * @throws InternalException thrown to indicate internal library error.
     * @throws InvalidKeyException thrown to indicate missing of HMAC SHA-256 library.
     * @throws InvalidResponseException thrown to indicate S3 service returned invalid or no error
     *     response.
     * @throws IOException thrown to indicate I/O error on S3 operation.
     * @throws NoSuchAlgorithmException thrown to indicate missing of MD5 or SHA-256 digest library.
     * @throws XmlParserException thrown to indicate XML parsing error.
     */
    public void save(MultipartFile file)
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        try (BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .build());
            log.info("File '{}' uploaded successfully.", file.getOriginalFilename());
        }
    }

    /**
     * Retrieves a file from the MinIO server.
     *
     * @param fileName the name of the file to be retrieved.
     * @return the byte content of the file.
     * @throws ServerException thrown to indicate that S3 service returning HTTP server error
     * @throws ErrorResponseException thrown to indicate S3 service returned an error response.
     * @throws InsufficientDataException thrown to indicate not enough data available in InputStream.
     * @throws InternalException thrown to indicate internal library error.
     * @throws InvalidKeyException thrown to indicate missing of HMAC SHA-256 library.
     * @throws InvalidResponseException thrown to indicate S3 service returned invalid or no error
     *     response.
     * @throws IOException thrown to indicate I/O error on S3 operation.
     * @throws NoSuchAlgorithmException thrown to indicate missing of MD5 or SHA-256 digest library.
     * @throws XmlParserException thrown to indicate XML parsing error.
     */
    public byte[] get(String fileName)
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build())) {
            log.info("File '{}' downloaded successfully.", fileName);
            return stream.readAllBytes();
        }
    }
}