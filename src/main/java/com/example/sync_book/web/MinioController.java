package com.example.sync_book.web;

import com.example.sync_book.service.MinioService;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = MinioController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MinioController {
    public static final String REST_URL = "/api/v1/minio";

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioService minioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        minioService.save(file);
        return ResponseEntity.ok(file.getOriginalFilename());
    }

    @GetMapping
    public ResponseEntity<?> download(@RequestParam String fileName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        byte[] file = minioService.get(fileName);
        return ResponseEntity.ok(file);
    }
}